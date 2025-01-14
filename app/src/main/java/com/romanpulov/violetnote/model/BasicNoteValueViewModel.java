package com.romanpulov.violetnote.model;

import android.app.Application;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.romanpulov.violetnote.db.dao.BasicNoteValueDAO;
import com.romanpulov.violetnote.view.core.BasicEntityNoteViewModel;
import org.jetbrains.annotations.NotNull;
import java.util.List;


public class BasicNoteValueViewModel extends BasicEntityNoteViewModel<BasicNoteValueA> {
    private BasicNoteValueDAO mBasicNoteValueDAO;

    private BasicNoteA mBasicNote;
    private MutableLiveData<List<BasicNoteValueA>> mBasicNoteValues;

    public BasicNoteA getBasicNote() {
        return mBasicNote;
    }

    public void setBasicNote(BasicNoteA mBasicNote) {
        this.mBasicNote = mBasicNote;
    }

    public LiveData<List<BasicNoteValueA>> getBasicNoteValues() {
        if (mBasicNoteValues == null) {
            mBasicNoteValues = new MutableLiveData<>();
            loadValues();
        }

        return mBasicNoteValues;
    }

    public BasicNoteValueViewModel(@NotNull Application application) {
        super(application);
    }

    @Override
    public void onDataChangeActionCompleted() {
        loadValues();
    }

    @Override
    protected BasicNoteValueDAO getDAO() {
        if (mBasicNoteValueDAO == null) {
            mBasicNoteValueDAO = new BasicNoteValueDAO(getApplication());
        }
        return mBasicNoteValueDAO;
    }

    private void loadValues() {
        mBasicNoteValues.setValue(getDAO().getNoteValues(mBasicNote));
    }
}
