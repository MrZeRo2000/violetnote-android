package com.romanpulov.violetnote.model;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import com.romanpulov.violetnote.VioletNoteApplication;
import com.romanpulov.violetnote.db.dao.BasicCommonNoteDAO;
import com.romanpulov.violetnote.db.dao.BasicNoteDAO;
import com.romanpulov.violetnote.db.dao.BasicNoteGroupDAO;
import com.romanpulov.violetnote.loader.document.DocumentPassDataLoader;
import com.romanpulov.violetnote.view.action.UIAction;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BasicNoteGroupViewModel extends AndroidViewModel {
    public static final String NOTE_GROUP_CHANGE_KEY = BasicNoteGroupViewModel.class.getSimpleName() + " change";

    private BasicCommonNoteDAO mBasicCommonNoteDAO;
    private BasicNoteGroupDAO mBasicNoteGroupDAO;
    private BasicNoteDAO mBasicNoteDAO;

    private UIAction<List<BasicNoteGroupA>> mAction;
    private MutableLiveData<List<BasicNoteGroupA>> mAllWithTotals;
    private MutableLiveData<List<BasicNoteGroupA>> mGroups;

    private BasicCommonNoteDAO getBasicCommonNoteDAO() {
        if (mBasicCommonNoteDAO == null) {
            mBasicCommonNoteDAO = new BasicCommonNoteDAO(getApplication());
        }
        return mBasicCommonNoteDAO;
    }

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

    public void loadAllWithTotals() {
        if (mAllWithTotals != null) {
            mAllWithTotals.setValue(getBasicNoteGroupDAO().getAllWithTotals(
                    DocumentPassDataLoader.getDocumentFile(getApplication()) == null)
            );
        }
    }

    private void loadGroups() {
        if (mGroups != null) {
            mGroups.setValue(getBasicNoteGroupDAO().getByGroupType(BasicNoteGroupA.BASIC_NOTE_GROUP_TYPE));
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

    public void add(BasicNoteGroupA item, UIAction<List<BasicNoteGroupA>> action) {
        if (getBasicNoteGroupDAO().insert(item) != -1) {
            setAction(action);
            loadGroups();
            setNoteGroupsChanged();
        }
    }

    public void delete(BasicNoteGroupA item, UIAction<List<BasicNoteGroupA>> action) {
        if (getBasicNoteGroupDAO().delete(item) != 0) {
            setAction(action);
            loadGroups();
            setNoteGroupsChanged();
        }
    }

    public void edit(BasicNoteGroupA item, UIAction<List<BasicNoteGroupA>> action) {
        if (getBasicNoteGroupDAO().update(item) != -1) {
            setAction(action);
            loadGroups();
            setNoteGroupsChanged();
        }
    }

    public void moveUp(List<? extends BasicCommonNoteA> items, UIAction<List<BasicNoteGroupA>> action) {
        BasicOrderedEntityNoteA.sortAsc(items);
        boolean result = false;

        for (BasicCommonNoteA item : items) {
            if (getBasicCommonNoteDAO().moveUp(item)) {
                result = true;
            } else {
                break;
            }
        }

        if (result) {
            setAction(action);
            loadGroups();
            setNoteGroupsChanged();
        }
    }

    public void moveTop(List<? extends BasicCommonNoteA> items, UIAction<List<BasicNoteGroupA>> action) {
        BasicOrderedEntityNoteA.sortDesc(items);
        boolean result = false;

        for (BasicCommonNoteA item : items) {
            if (getBasicCommonNoteDAO().moveTop(item)) {
                result = true;
            } else {
                break;
            }
        }

        if (result) {
            setAction(action);
            loadGroups();
            setNoteGroupsChanged();
        }
    }

    public void moveDown(List<? extends BasicCommonNoteA> items, UIAction<List<BasicNoteGroupA>> action) {
        BasicOrderedEntityNoteA.sortDesc(items);
        boolean result = false;

        for (BasicCommonNoteA item : items) {
            if (getBasicCommonNoteDAO().moveDown(item)) {
                result = true;
            } else {
                break;
            }
        }

        if (result) {
            setAction(action);
            loadGroups();
            setNoteGroupsChanged();
        }
    }

    public void moveBottom(List<? extends BasicCommonNoteA> items, UIAction<List<BasicNoteGroupA>> action) {
        BasicOrderedEntityNoteA.sortAsc(items);
        boolean result = false;

        for (BasicCommonNoteA item : items) {
            if (getBasicCommonNoteDAO().moveBottom(item)) {
                result = true;
            } else {
                break;
            }
        }

        if (result) {
            setAction(action);
            loadGroups();
            setNoteGroupsChanged();
        }
    }

    public boolean isGroupEmpty(BasicNoteGroupA item) {
        return getBasicNoteDAO().getByGroup(item).isEmpty();
    }
}
