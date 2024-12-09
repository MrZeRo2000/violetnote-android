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

    private MutableLiveData<BasicNoteGroupA> mBasicNoteGroup = new MutableLiveData<>();
    private MutableLiveData<List<BasicNoteA>> mBasicNotes = new MutableLiveData<>();

    public MutableLiveData<BasicNoteGroupA> getBasicNoteGroup() {
        return mBasicNoteGroup;
    }

    public MutableLiveData<List<BasicNoteA>> getBasicNotes() {
        return mBasicNotes;
    }

    public BasicNoteViewModel(@NotNull Application application) {
        super(application);
    }

    @Override
    protected void onDataChangeActionCompleted() {

    }

    @Override
    protected @NonNull BasicNoteDAO getDAO() {
        if (mBasicNoteDAO == null) {
            mBasicNoteDAO = new BasicNoteDAO(getApplication());
        }
        return mBasicNoteDAO;
    }

    private void loadTotals() {
        if (mBasicNoteGroup.getValue() != null) {
            mBasicNotes.setValue(getDAO().getTotalsByGroup(mBasicNoteGroup.getValue()));
        }
    }
}
