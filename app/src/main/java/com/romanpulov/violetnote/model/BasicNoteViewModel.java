package com.romanpulov.violetnote.model;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.romanpulov.violetnote.db.dao.BasicNoteDAO;
import com.romanpulov.violetnote.db.dao.BasicNoteGroupDAO;
import com.romanpulov.violetnote.db.dao.BasicNoteItemDAO;
import com.romanpulov.violetnote.view.action.UIAction;
import com.romanpulov.violetnote.view.core.BasicCommonNoteViewModel;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class BasicNoteViewModel extends BasicCommonNoteViewModel<BasicNoteA> {

    private BasicNoteGroupDAO mBasicNoteGroupDAO;
    private BasicNoteDAO mBasicNoteDAO;
    private BasicNoteItemDAO mBasicNoteItemDAO;

    private BasicNoteGroupA mBasicNoteGroup;
    private MutableLiveData<List<BasicNoteA>> mBasicNotes;
    private MutableLiveData<List<BasicNoteGroupA>> mRelatedNoteGroups;

    private MutableLiveData<Boolean> mNoteGroupsChanged;

    public void setNoteGroupsChanged(MutableLiveData<Boolean> mNoteGroupsChanged) {
        this.mNoteGroupsChanged = mNoteGroupsChanged;
    }

    public BasicNoteGroupA getBasicNoteGroup() {
        return mBasicNoteGroup;
    }

    public void setBasicNoteGroup(BasicNoteGroupA basicNoteGroup) {
        if (!Objects.equals(this.mBasicNoteGroup, basicNoteGroup)) {
            this.mBasicNoteGroup = basicNoteGroup;
            loadNotes();
            if (mRelatedNoteGroups != null) {
                mRelatedNoteGroups.setValue(null);
            }
        }
    }

    public LiveData<List<BasicNoteA>> getBasicNotes() {
        if (mBasicNotes == null) {
            mBasicNotes = new MutableLiveData<>();
            loadNotes();
        }
        return mBasicNotes;
    }

    public LiveData<List<BasicNoteGroupA>> getRelatedNoteGroups() {
        if (mRelatedNoteGroups == null) {
            mRelatedNoteGroups = new MutableLiveData<>();
            mRelatedNoteGroups.setValue(getBasicNoteGroupDAO().getRelatedNoteGroupList(getBasicNoteGroup()));
        }
        return mRelatedNoteGroups;
    }

    public BasicNoteViewModel(@NotNull Application application) {
        super(application);
    }

    @Override
    protected void onDataChangeActionCompleted() {
        loadNotes();
        onNoteGroupsChanged();
    }

    private void onNoteGroupsChanged() {
        if (mNoteGroupsChanged != null) {
            mNoteGroupsChanged.setValue(true);
        }
    }

    @Override
    protected @NonNull BasicNoteDAO getDAO() {
        if (mBasicNoteDAO == null) {
            mBasicNoteDAO = new BasicNoteDAO(getApplication());
        }
        return mBasicNoteDAO;
    }

    public BasicNoteGroupDAO getBasicNoteGroupDAO() {
        if (mBasicNoteGroupDAO == null) {
            mBasicNoteGroupDAO = new BasicNoteGroupDAO(getApplication());
        }
        return mBasicNoteGroupDAO;
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

    public void duplicate(BasicNoteA item, String title, UIAction<BasicNoteA> action) {
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

    public void moveToOtherNoteGroup(
            List<BasicNoteA> data,
            BasicNoteGroupA otherNoteGroup,
            UIAction<BasicNoteA> action) {
        BasicOrderedEntityNoteA.sortAsc(data);
        data.forEach(note -> getDAO().moveToOtherNoteGroup(note, otherNoteGroup));

        setAction(action);
        onDataChangeActionCompleted();
    }

    public BasicNoteDataA createNoteDataFromNote(BasicNoteGroupA noteGroup, BasicNoteA note) {
        return getDAO().createNoteDataFromNote(noteGroup, note);
    }
}
