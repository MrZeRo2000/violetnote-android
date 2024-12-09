package com.romanpulov.violetnote.model;

import android.app.Application;
import androidx.lifecycle.MutableLiveData;
import com.romanpulov.violetnote.VioletNoteApplication;
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
    private MutableLiveData<List<BasicNoteGroupA>> mCurrentGroups;

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
        setNoteGroupsChanged();
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

    public MutableLiveData<List<BasicNoteGroupA>> getCurrentGroups() {
        if (mCurrentGroups == null) {
            mCurrentGroups = new MutableLiveData<>();
        }
        return mCurrentGroups;
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

    public static void setAppNoteGroupsChanged(Application application) {
        if (application instanceof VioletNoteApplication) {
            ((VioletNoteApplication)application).getSharedData().put(
                    BasicNoteGroupViewModel.NOTE_GROUP_CHANGE_KEY,
                    BasicNoteGroupViewModel.NOTE_GROUP_CHANGE_KEY);
        }
    }

    private void setNoteGroupsChanged() {
        BasicNoteGroupViewModel.setAppNoteGroupsChanged(getApplication());
    }

    public boolean isNoteGroupsChanged() {
        return (getApplication() instanceof VioletNoteApplication) &&
                ((VioletNoteApplication)getApplication())
                        .getSharedData()
                        .containsKey(BasicNoteGroupViewModel.NOTE_GROUP_CHANGE_KEY);
    }

    public void resetNoteGroupsChanged() {
        if (getApplication() instanceof VioletNoteApplication) {
            ((VioletNoteApplication)getApplication()).getSharedData().remove(BasicNoteGroupViewModel.NOTE_GROUP_CHANGE_KEY);
        }
    }

    public boolean isGroupEmpty(BasicNoteGroupA item) {
        return getBasicNoteDAO().getByGroup(item).isEmpty();
    }
}
