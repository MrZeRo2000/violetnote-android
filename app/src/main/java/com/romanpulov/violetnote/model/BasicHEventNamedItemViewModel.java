package com.romanpulov.violetnote.model;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.romanpulov.violetnote.db.dao.BasicHNoteItemDAO;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class BasicHEventNamedItemViewModel extends AndroidViewModel {
    private BasicNoteItemA mNoteItem;
    private MutableLiveData<List<BasicHNoteItemA>> mBasicHNoteItems = new MutableLiveData<>();

    private BasicHNoteItemDAO mBasicHNoteItemDAO;

    public BasicNoteItemA getNoteItem() {
        return mNoteItem;
    }

    public void setNoteItem(BasicNoteItemA noteItem) {
        if (noteItem == null) {
            this.mNoteItem = null;
            mBasicHNoteItems.setValue(null);
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

    public BasicHEventNamedItemViewModel(@NotNull Application application) {
        super(application);
    }

    private void loadItems() {
        mBasicHNoteItems.setValue(getBasicHNoteItemDAO().getByNoteItemIdWithEvents(mNoteItem.getId()));
    }
}
