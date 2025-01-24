package com.romanpulov.violetnote.model.vm;

import android.app.Application;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.db.dao.BasicNoteDAO;
import com.romanpulov.violetnote.db.dao.BasicNoteItemDAO;
import com.romanpulov.violetnote.model.BasicNoteA;
import com.romanpulov.violetnote.model.BasicNoteItemA;
import com.romanpulov.violetnote.model.service.PassNoteItemJSONCryptService;
import com.romanpulov.violetnote.model.vm.helper.ThreadProcessHelper;
import com.romanpulov.violetnote.view.action.UIAction;
import com.romanpulov.violetnote.view.core.BasicCommonNoteViewModel;
import org.jetbrains.annotations.NotNull;

import java.util.*;

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

    private ThreadProcessHelper mThreadProcessHelper;

    private ThreadProcessHelper getThreadProcessHelper() {
        if (mThreadProcessHelper == null) {
            mThreadProcessHelper = new ThreadProcessHelper();
        }
        return mThreadProcessHelper;
    }

    @Nullable
    public String getProcessError() {
        if (mThreadProcessHelper != null) {
            return mThreadProcessHelper.getProcessError().getValue();
        } else {
            return null;
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (mThreadProcessHelper != null) {
            mThreadProcessHelper.shutdown();
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
            getThreadProcessHelper().startProcessForLiveData(() -> {
                Log.d(TAG, "Starting process for live data");
                // get
                List<BasicNoteItemA> basicNoteItems = getDAO().getNoteItems(mBasicNote);

                // decrypt
                int decrypt_errors = basicNoteItems.stream().parallel().reduce(
                        0,
                        (a, item) ->
                                PassNoteItemJSONCryptService.decryptBasicNoteItem(item, mPassword.getValue()) ? 0 : 1,
                        Integer::sum);
                Log.d(TAG, "Items decrypt errors: " + decrypt_errors);

                if (decrypt_errors == 0) {
                    return basicNoteItems;
                } else {
                    getThreadProcessHelper().setProcessError(getApplication().getString(R.string.ui_error_wrong_password));
                    return List.of();
                }
            }, mBasicNoteItems);
        } else {
            mBasicNoteItems.setValue(getDAO().getNoteItems(mBasicNote));
        }
    }

    @Override
    public void add(BasicNoteItemA item, UIAction<BasicNoteItemA> action) {
        item.setNoteId(mBasicNote.getId());

        if (mBasicNote.isEncrypted()) {
            getThreadProcessHelper().startProcess(() -> {
                Log.d(TAG, "Starting add process");
                PassNoteItemJSONCryptService.encryptBasicNoteItem(item, mPassword.getValue());
                Log.d(TAG, "Item encrypted");

                super.add(item, action);
            });
        } else {
            super.add(item, action);
        }
    }

    private void internalEditNameValue(BasicNoteItemA item, UIAction<BasicNoteItemA> action) {
        if (getDAO().updateNameValue(item) != -1) {
            setAction(action);
            onDataChangeActionCompleted();
        }
    }

    public void editNameValue(BasicNoteItemA item, UIAction<BasicNoteItemA> action) {
        if (mBasicNote.isEncrypted()) {
            getThreadProcessHelper().startProcess(() -> {
                Log.d(TAG, "Starting editNameValue process");
                PassNoteItemJSONCryptService.encryptBasicNoteItem(item, mPassword.getValue());
                Log.d(TAG, "Item encrypted");

                internalEditNameValue(item, action);
            });
        } else {
            internalEditNameValue(item, action);
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
