package com.romanpulov.violetnote.model.vm;

import android.app.Application;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.db.dao.BasicHNoteItemDAO;
import com.romanpulov.violetnote.model.BasicHNoteItemA;
import com.romanpulov.violetnote.model.BasicNoteA;
import com.romanpulov.violetnote.model.BasicNoteItemA;
import com.romanpulov.violetnote.model.service.PassNameValueDataJSONCryptService;
import com.romanpulov.violetnote.model.vm.helper.ThreadProcessHelper;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class BasicHEventNamedItemViewModel extends AndroidViewModel {
    private static final String TAG = BasicHEventNamedItemViewModel.class.getSimpleName();

    private BasicNoteA mBasicNote;
    private BasicNoteItemA mNoteItem;
    private MutableLiveData<List<BasicHNoteItemA>> mBasicHNoteItems = new MutableLiveData<>();

    private BasicHNoteItemDAO mBasicHNoteItemDAO;

    public BasicNoteA getBasicNote() {
        return mBasicNote;
    }

    public void setBasicNote(BasicNoteA mBasicNote) {
        this.mBasicNote = mBasicNote;
    }

    public BasicNoteItemA getNoteItem() {
        return mNoteItem;
    }

    public void setNoteItem(BasicNoteItemA noteItem) {
        if (noteItem == null) {
            this.mNoteItem = null;
            mBasicHNoteItems.setValue(List.of());
        } else if (!Objects.equals(this.mNoteItem, noteItem)) {
            this.mNoteItem = noteItem;
            loadItems();
        }
    }

    public LiveData<List<BasicHNoteItemA>> getBasicHNoteItems() {
        if (mBasicHNoteItems == null) {
            mBasicHNoteItems = new MutableLiveData<>();
            loadItems();
        }
        return mBasicHNoteItems;
    }

    public BasicHNoteItemDAO getBasicHNoteItemDAO() {
        if (mBasicHNoteItemDAO == null) {
            mBasicHNoteItemDAO = new BasicHNoteItemDAO(getApplication());
        }
        return mBasicHNoteItemDAO;
    }

    private String mPassword;

    public void setPassword(String password) {
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

    public BasicHEventNamedItemViewModel(@NotNull Application application) {
        super(application);
    }

    private void loadItems() {
        if ((mBasicNote != null) && (mBasicNote.isEncrypted())) {
            getThreadProcessHelper().startProcessForLiveData(() -> {
                Log.d(TAG, "Starting process for live data");
                // get
                List<BasicHNoteItemA> items = getBasicHNoteItemDAO().getByNoteItemIdWithEvents(mNoteItem.getId());

                // decrypt
                int decrypt_errors = items.stream().parallel().reduce(
                        0,
                        (a, item) ->
                                PassNameValueDataJSONCryptService.decryptNameValueData(item, mPassword) ? 0 : 1,
                        Integer::sum);
                Log.d(TAG, "Items decrypt errors: " + decrypt_errors);

                if (decrypt_errors == 0) {
                    return items;
                } else {
                    getThreadProcessHelper().setProcessError(getApplication().getString(R.string.ui_error_wrong_password));
                    return List.of();
                }
            }, mBasicHNoteItems);

        } else {
            mBasicHNoteItems.setValue(getBasicHNoteItemDAO().getByNoteItemIdWithEvents(mNoteItem.getId()));
        }
    }
}
