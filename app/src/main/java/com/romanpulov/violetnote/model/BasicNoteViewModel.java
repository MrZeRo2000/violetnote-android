package com.romanpulov.violetnote.model;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import com.romanpulov.violetnote.db.dao.BasicNoteDAO;
import com.romanpulov.violetnote.db.dao.BasicNoteItemDAO;
import com.romanpulov.violetnote.view.action.UIAction;
import com.romanpulov.violetnote.view.core.BasicCommonNoteViewModel;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class BasicNoteViewModel extends BasicCommonNoteViewModel<BasicNoteA> {

    private BasicNoteDAO mBasicNoteDAO;
    private BasicNoteItemDAO mBasicNoteItemDAO;

    private BasicNoteGroupA mBasicNoteGroup;
    private MutableLiveData<List<BasicNoteA>> mBasicNotes;

    public BasicNoteGroupA getBasicNoteGroup() {
        return mBasicNoteGroup;
    }

    public void setBasicNoteGroup(BasicNoteGroupA mBasicNoteGroup) {
        if (!Objects.equals(this.mBasicNoteGroup, mBasicNoteGroup)) {
            this.mBasicNoteGroup = mBasicNoteGroup;
            loadNotes();
        }
    }

    public MutableLiveData<List<BasicNoteA>> getBasicNotes() {
        if (mBasicNotes == null) {
            mBasicNotes = new MutableLiveData<>();
            loadNotes();
        }
        return mBasicNotes;
    }

    public BasicNoteViewModel(@NotNull Application application) {
        super(application);
    }

    @Override
    protected void onDataChangeActionCompleted() {
        loadNotes();
    }

    @Override
    protected @NonNull BasicNoteDAO getDAO() {
        if (mBasicNoteDAO == null) {
            mBasicNoteDAO = new BasicNoteDAO(getApplication());
        }
        return mBasicNoteDAO;
    }

    private BasicNoteItemDAO getBasicNoteItemDAO() {
        if (mBasicNoteItemDAO == null) {
            mBasicNoteItemDAO = new BasicNoteItemDAO(getApplication());
        }
        return mBasicNoteItemDAO;
    }

    private void loadNotes() {
        if (mBasicNotes != null) {
            mBasicNotes.setValue(getDAO().getTotalsByGroup(mBasicNoteGroup));
        }
    }

    public void duplicate(BasicNoteA item, String title, UIAction<List<? extends BasicCommonNoteA>> action) {
        BasicNoteA newNote = ParcelableUtils.duplicateParcelableObject(item, BasicNoteA.CREATOR);
        newNote.setTitle(title);

        //get items
        getBasicNoteItemDAO().fillNoteDataItemsWithSummary(newNote);

        //get new id
        long newItemId = getDAO().insert(newNote);
        if (newItemId != -1) {
            //insert items ignoring any errors
            for (BasicNoteItemA noteItem : newNote.getItems()) {
                noteItem.setNoteId(newItemId);
                getBasicNoteItemDAO().insert(noteItem);
            }

            setAction(action);
            onDataChangeActionCompleted();
        }
    }
}
