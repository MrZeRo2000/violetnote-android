package com.romanpulov.violetnote.model.vm;

import android.app.Application;
import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.romanpulov.violetnote.db.dao.BasicNoteDAO;
import com.romanpulov.violetnote.db.dao.BasicNoteItemDAO;
import com.romanpulov.violetnote.model.BasicNoteA;
import com.romanpulov.violetnote.model.BasicNoteItemA;
import com.romanpulov.violetnote.model.service.PassNoteItemJSONCryptService;
import com.romanpulov.violetnote.view.action.UIAction;
import com.romanpulov.violetnote.view.core.BasicCommonNoteViewModel;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class BasicNoteNamedItemViewModel extends BasicCommonNoteViewModel<BasicNoteItemA> {
    private static final String TAG = BasicNoteNamedItemViewModel.class.getSimpleName();

    private BasicNoteDAO mBasicNoteDAO;
    private BasicNoteItemDAO mBasicNoteItemDAO;

    private MutableLiveData<Boolean> mNoteGroupsChanged;

    private BasicNoteA mBasicNote;
    private MutableLiveData<List<BasicNoteItemA>> mBasicNoteItems;

    private MutableLiveData<List<BasicNoteA>> mRelatedNotes;

    private LiveData<String> mPassword;

    public void setPassword(LiveData<String> password) {
        this.mPassword = password;
    }

    private ExecutorService mLoadExecutorService;
    private final MutableLiveData<Exception> mProcessException = new MutableLiveData<>();

    public LiveData<Exception> getProcessException() {
        return mProcessException;
    }

    public void startProcess(Runnable runnable) {
        if (mLoadExecutorService == null) {
            mLoadExecutorService = Executors.newSingleThreadExecutor();
        }

        mLoadExecutorService.execute(() -> {
            try {
                runnable.run();
            } catch (Exception e) {
                mProcessException.postValue(e);
            }
        });
    }

    public <T> void startProcessForLiveData(
            Supplier<List<T>> dataSupplier,
            MutableLiveData<List<T>> liveData) {
        if (mLoadExecutorService == null) {
            mLoadExecutorService = Executors.newSingleThreadExecutor();
        }

        mLoadExecutorService.execute(() -> {
            try {
                liveData.postValue(dataSupplier.get());
            } catch (Exception e) {
                mProcessException.postValue(e);
            }
        });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (mLoadExecutorService != null) {
            mLoadExecutorService.shutdownNow();
        }
    }

    public void setNoteGroupsChanged(MutableLiveData<Boolean> mNoteGroupsChanged) {
        this.mNoteGroupsChanged = mNoteGroupsChanged;
    }

    public BasicNoteA getBasicNote() {
        return mBasicNote;
    }

    public void setBasicNote(BasicNoteA basicNote) {
        if (!Objects.equals(this.mBasicNote, basicNote)) {
            mBasicNote = basicNote;
            mRelatedNotes = null;
            // loadNoteItems();
        }
    }

    public LiveData<List<BasicNoteItemA>> getBasicNoteItems() {
        if (mBasicNoteItems == null) {
            mBasicNoteItems = new MutableLiveData<>();
            //loadNoteItems();
        }
        return mBasicNoteItems;
    }

    public LiveData<List<BasicNoteA>> getRelatedNotes() {
        if (mRelatedNotes == null) {
            mRelatedNotes = new MutableLiveData<>();
            mRelatedNotes.setValue(getBasicNoteDAO().getRelatedNotes(mBasicNote));
        }

        return mRelatedNotes;
    }

    public BasicNoteNamedItemViewModel(@NotNull Application application) {
        super(application);
    }

    @Override
    protected BasicNoteItemDAO getDAO() {
        if (mBasicNoteItemDAO == null) {
            mBasicNoteItemDAO = new BasicNoteItemDAO(getApplication());
        }
        return mBasicNoteItemDAO;
    }

    private BasicNoteDAO getBasicNoteDAO() {
        if (mBasicNoteDAO == null) {
            mBasicNoteDAO = new BasicNoteDAO(getApplication());
        }
        return mBasicNoteDAO;
    }

    @Override
    protected void onDataChangeActionCompleted() {
        loadNoteItems();
        onNoteGroupsChanged();
    }

    private void onNoteGroupsChanged() {
        if (mNoteGroupsChanged != null) {
            mNoteGroupsChanged.setValue(true);
        }
    }

    public void loadNoteItems() {
        if (mBasicNoteItems == null) {
            mBasicNoteItems = new MutableLiveData<>();
        }
        if (mBasicNote.isEncrypted()) {
            startProcessForLiveData(() -> {
                Log.d(TAG, "Starting process for live data");
                // get
                List<BasicNoteItemA> basicNoteItems = getDAO().getNoteItems(mBasicNote);

                // decrypt
                for (BasicNoteItemA item : basicNoteItems) {
                    PassNoteItemJSONCryptService.decryptBasicNoteItem(item, mPassword.getValue());
                }
                Log.d(TAG, "Items decrypted");

                return basicNoteItems;
            }, mBasicNoteItems);
        } else {
            mBasicNoteItems.setValue(getDAO().getNoteItems(mBasicNote));
        }
    }

    @Override
    public void add(BasicNoteItemA item, UIAction<BasicNoteItemA> action) {
        item.setNoteId(mBasicNote.getId());

        if (mBasicNote.isEncrypted()) {
            startProcess(() -> {
                Log.d(TAG, "Starting add process");
                PassNoteItemJSONCryptService.encryptBasicNoteItem(item, mPassword.getValue());
                Log.d(TAG, "Item encrypted");
                getDAO().insert(item);
                Log.d(TAG, "Item inserted");
            });
            setAction(action);
            onDataChangeActionCompleted();
        } else {
            super.add(item, action);
        }
    }

    public void editNameValue(BasicNoteItemA item, UIAction<BasicNoteItemA> action) {
        if (getDAO().updateNameValue(item) != -1) {
            setAction(action);
            onDataChangeActionCompleted();
        }
    }

    public void moveToOtherNote(
            Collection<BasicNoteItemA> items,
            BasicNoteA otherNote,
            UIAction<BasicNoteItemA> action) {
        long result = items
                .stream()
                .reduce(
                        0L,
                        (a, v) -> a + getDAO().moveToOtherNote(v, otherNote),
                        Long::sum);
        if (result > 0) {
            setAction(action);
            onDataChangeActionCompleted();
        }
    }
}
