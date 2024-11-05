package com.romanpulov.violetnote.model;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import com.romanpulov.violetnote.db.dao.BasicNoteGroupDAO;
import com.romanpulov.violetnote.loader.document.DocumentPassDataLoader;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BasicNoteGroupViewModel extends AndroidViewModel {
    public static final int ACTION_NONE = 0;
    public static final int ACTION_ADD = 1;

    private BasicNoteGroupDAO mBasicNoteGroupDAO;

    private int mAction = ACTION_NONE;
    private MutableLiveData<List<BasicNoteGroupA>> mAllWithTotals;
    private MutableLiveData<List<BasicNoteGroupA>> mGroups;

    private BasicNoteGroupDAO getBasicNoteGroupDAO() {
        if (mBasicNoteGroupDAO == null) {
            mBasicNoteGroupDAO = new BasicNoteGroupDAO(getApplication());
        }
        return mBasicNoteGroupDAO;
    }

    public BasicNoteGroupViewModel(@NotNull Application application) {
        super(application);
    }

    public int getAction() {
        return mAction;
    }

    public void setAction(int mAction) {
        this.mAction = mAction;
    }

    public void resetAction() {
        mAction = ACTION_NONE;
    }

    public MutableLiveData<List<BasicNoteGroupA>> getAllWithTotals() {
        if (mAllWithTotals == null) {
            mAllWithTotals = new MutableLiveData<>();
            loadAllWithTotals();
        }

        return mAllWithTotals;
    }

    public MutableLiveData<List<BasicNoteGroupA>> getGroups() {
        if (mGroups == null) {
            mGroups = new MutableLiveData<>();
            loadGroups();
        }

        return mGroups;
    }

    private void loadAllWithTotals() {
        mAllWithTotals.setValue(getBasicNoteGroupDAO().getAllWithTotals(
                DocumentPassDataLoader.getDocumentFile(getApplication()) == null)
        );
    }

    private void loadGroups() {
        mGroups.setValue(getBasicNoteGroupDAO().getByGroupType(BasicNoteGroupA.BASIC_NOTE_GROUP_TYPE));
    }

    public void add(BasicNoteGroupA item) {
        if (getBasicNoteGroupDAO().insert(item) != -1) {
            setAction(ACTION_ADD);
            loadGroups();
            mAllWithTotals = null;
        }
    }
}
