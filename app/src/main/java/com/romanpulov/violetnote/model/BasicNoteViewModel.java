package com.romanpulov.violetnote.model;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import com.romanpulov.violetnote.db.dao.BasicNoteDAO;
import com.romanpulov.violetnote.view.core.BasicCommonNoteViewModel;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class BasicNoteViewModel extends BasicCommonNoteViewModel<BasicNoteA> {

    private BasicNoteDAO mBasicNoteDAO;

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

    private void loadNotes() {
        if (mBasicNotes != null) {
            mBasicNotes.setValue(getDAO().getTotalsByGroup(mBasicNoteGroup));
        }
    }
}
