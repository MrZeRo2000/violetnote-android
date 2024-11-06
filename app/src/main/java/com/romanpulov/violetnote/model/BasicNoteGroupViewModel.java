package com.romanpulov.violetnote.model;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import com.romanpulov.violetnote.db.dao.BasicNoteDAO;
import com.romanpulov.violetnote.db.dao.BasicNoteGroupDAO;
import com.romanpulov.violetnote.loader.document.DocumentPassDataLoader;
import com.romanpulov.violetnote.view.action.UIAction;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BasicNoteGroupViewModel extends AndroidViewModel {

    private BasicNoteGroupDAO mBasicNoteGroupDAO;
    private BasicNoteDAO mBasicNoteDAO;

    private UIAction<List<BasicNoteGroupA>> mAction;
    private MutableLiveData<List<BasicNoteGroupA>> mAllWithTotals;
    private MutableLiveData<List<BasicNoteGroupA>> mGroups;

    private BasicNoteGroupDAO getBasicNoteGroupDAO() {
        if (mBasicNoteGroupDAO == null) {
            mBasicNoteGroupDAO = new BasicNoteGroupDAO(getApplication());
        }
        return mBasicNoteGroupDAO;
    }

    private BasicNoteDAO getBasicNoteDAO() {
        if (mBasicNoteDAO == null) {
            mBasicNoteDAO = new BasicNoteDAO(getApplication());
        }
        return mBasicNoteDAO;
    }

    public BasicNoteGroupViewModel(@NotNull Application application) {
        super(application);
    }

    public UIAction<List<BasicNoteGroupA>> getAction() {
        return mAction;
    }

    public void setAction(UIAction<List<BasicNoteGroupA>> mAction) {
        this.mAction = mAction;
    }

    public void resetAction() {
        mAction = null;
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

    public void add(BasicNoteGroupA item, UIAction<List<BasicNoteGroupA>> action) {
        if (getBasicNoteGroupDAO().insert(item) != -1) {
            setAction(action);
            loadGroups();
            mAllWithTotals = null;
        }
    }

    public void delete(BasicNoteGroupA item, UIAction<List<BasicNoteGroupA>> action) {
        if (getBasicNoteGroupDAO().delete(item) != 0) {
            setAction(action);
            loadGroups();
            mAllWithTotals = null;
        }
    }

    public void edit(BasicNoteGroupA item, UIAction<List<BasicNoteGroupA>> action) {
        if (getBasicNoteGroupDAO().update(item) != -1) {
            setAction(action);
            loadGroups();
            mAllWithTotals = null;
        }
    }

    public boolean isGroupEmpty(BasicNoteGroupA item) {
        return getBasicNoteDAO().getByGroup(item).isEmpty();
    }
}
