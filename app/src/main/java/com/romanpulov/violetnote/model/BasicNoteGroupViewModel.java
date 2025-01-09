package com.romanpulov.violetnote.model;

import android.app.Application;
import androidx.lifecycle.MutableLiveData;
import com.romanpulov.violetnote.db.dao.BasicNoteDAO;
import com.romanpulov.violetnote.db.dao.BasicNoteGroupDAO;
import com.romanpulov.violetnote.loader.document.DocumentPassDataLoader;
import com.romanpulov.violetnote.view.core.BasicCommonNoteViewModel;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BasicNoteGroupViewModel extends BasicCommonNoteViewModel<BasicNoteGroupA> {
    public static final String NOTE_GROUP_CHANGE_KEY = BasicNoteGroupViewModel.class.getSimpleName() + " change";

    private BasicNoteGroupDAO mBasicNoteGroupDAO;
    private BasicNoteDAO mBasicNoteDAO;

    private MutableLiveData<List<BasicNoteGroupA>> mAllWithTotals;

    private MutableLiveData<List<BasicNoteGroupA>> mGroups;

    private MutableLiveData<Boolean> mNoteGroupsChanged;

    public void setNoteGroupsChanged(MutableLiveData<Boolean> mNoteGroupsChanged) {
        this.mNoteGroupsChanged = mNoteGroupsChanged;
    }

    @Override
    protected BasicNoteGroupDAO getDAO() {
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

    @Override
    protected void onDataChangeActionCompleted() {
        loadGroups();
        if (mNoteGroupsChanged != null) {
            mNoteGroupsChanged.setValue(true);
        }
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

    public void loadAllWithTotals() {
        if (mAllWithTotals != null) {
            mAllWithTotals.setValue(getDAO().getAllWithTotals(
                    DocumentPassDataLoader.getDocumentFile(getApplication()) == null)
            );
        }
    }

    private void loadGroups() {
        if (mGroups != null) {
            mGroups.setValue(getDAO().getByGroupType(BasicNoteGroupA.BASIC_NOTE_GROUP_TYPE));
        }
    }

    public boolean isGroupEmpty(BasicNoteGroupA item) {
        return getBasicNoteDAO().getByGroup(item).isEmpty();
    }
}
