package com.romanpulov.violetnote.model;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import com.romanpulov.violetnote.db.dao.BasicNoteDAO;
import com.romanpulov.violetnote.view.core.BasicCommonNoteViewModel;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BasicNoteViewModel extends BasicCommonNoteViewModel<BasicNoteA> {

    private BasicNoteDAO mBasicNoteDAO;

    private BasicNoteGroupA mBasicNoteGroup;
    private MutableLiveData<List<BasicNoteA>> mBasicNotes;

    public BasicNoteGroupA getBasicNoteGroup() {
        return mBasicNoteGroup;
    }

    public void setBasicNoteGroup(BasicNoteGroupA mBasicNoteGroup) {
        if (!this.mBasicNoteGroup.equals(mBasicNoteGroup)) {
            this.mBasicNoteGroup = mBasicNoteGroup;
            loadTotals();
        }
    }

    public MutableLiveData<List<BasicNoteA>> getBasicNotes() {
        if (mBasicNotes == null) {
            mBasicNotes = new MutableLiveData<>();
            loadTotals();
        }
        return mBasicNotes;
    }

    public BasicNoteViewModel(@NotNull Application application) {
        super(application);
    }

    @Override
    protected void onDataChangeActionCompleted() {
        loadTotals();
    }

    @Override
    protected @NonNull BasicNoteDAO getDAO() {
        if (mBasicNoteDAO == null) {
            mBasicNoteDAO = new BasicNoteDAO(getApplication());
        }
        return mBasicNoteDAO;
    }

    private void loadTotals() {
        if (mBasicNoteGroup != null) {
            mBasicNotes.setValue(getDAO().getTotalsByGroup(mBasicNoteGroup));
        }
    }
}
