package com.romanpulov.violetnote.model;

import android.app.Application;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.romanpulov.violetnote.db.dao.BasicNoteDAO;
import com.romanpulov.violetnote.db.dao.BasicNoteItemDAO;
import com.romanpulov.violetnote.view.action.UIAction;
import com.romanpulov.violetnote.view.core.BasicCommonNoteViewModel;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class BasicNoteNamedItemViewModel extends BasicCommonNoteViewModel<BasicNoteItemA> {
    private BasicNoteDAO mBasicNoteDAO;
    private BasicNoteItemDAO mBasicNoteItemDAO;

    private MutableLiveData<Boolean> mNoteGroupsChanged;

    private BasicNoteA mBasicNote;
    private MutableLiveData<List<BasicNoteItemA>> mBasicNoteItems;

    private MutableLiveData<List<BasicNoteA>> mRelatedNotes;

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
            loadNoteItems();
        }
    }

    public LiveData<List<BasicNoteItemA>> getBasicNoteItems() {
        if (mBasicNoteItems == null) {
            mBasicNoteItems = new MutableLiveData<>();
            loadNoteItems();
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

    private void loadNoteItems() {
        if (mBasicNoteItems == null) {
            mBasicNoteItems = new MutableLiveData<>();
        }
        mBasicNoteItems.setValue(getDAO().getNoteItems(mBasicNote));
    }

    @Override
    public void add(BasicNoteItemA item, UIAction<BasicNoteItemA> action) {
        item.setNoteId(mBasicNote.getId());
        super.add(item, action);
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
