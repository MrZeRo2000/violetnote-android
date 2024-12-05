package com.romanpulov.violetnote.view.core;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import com.romanpulov.violetnote.db.dao.AbstractDAO;
import com.romanpulov.violetnote.db.dao.BasicCommonNoteDAO;
import com.romanpulov.violetnote.model.BasicCommonNoteA;
import com.romanpulov.violetnote.model.BasicOrderedEntityNoteA;
import com.romanpulov.violetnote.view.action.UIAction;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class BasicCommonNoteViewModel<T extends BasicCommonNoteA> extends AndroidViewModel {

    private BasicCommonNoteDAO mBasicCommonNoteDAO;
    private UIAction<List<? extends BasicCommonNoteA>> mAction;

    private BasicCommonNoteDAO getBasicCommonNoteDAO() {
        if (mBasicCommonNoteDAO == null) {
            mBasicCommonNoteDAO = new BasicCommonNoteDAO(getApplication());
        }
        return mBasicCommonNoteDAO;
    }

    public UIAction<List<? extends BasicCommonNoteA>> getAction() {
        return mAction;
    }

    protected void setAction(UIAction<List<? extends BasicCommonNoteA>> mAction) {
        this.mAction = mAction;
    }

    public void resetAction() {
        mAction = null;
    }

    public BasicCommonNoteViewModel(@NotNull Application application) {
        super(application);
    }

    protected abstract void onDataChangeActionCompleted();
    protected abstract AbstractDAO<T> getDAO();

    public void moveUp(List<? extends BasicCommonNoteA> items, UIAction<List<? extends BasicCommonNoteA>> action) {
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
            onDataChangeActionCompleted();
        }
    }

    public void moveTop(List<? extends BasicCommonNoteA> items, UIAction<List<? extends BasicCommonNoteA>> action) {
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
            onDataChangeActionCompleted();
        }
    }

    public void moveDown(List<? extends BasicCommonNoteA> items, UIAction<List<? extends BasicCommonNoteA>> action) {
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
            onDataChangeActionCompleted();
        }
    }

    public void moveBottom(List<? extends BasicCommonNoteA> items, UIAction<List<? extends BasicCommonNoteA>> action) {
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
            onDataChangeActionCompleted();
        }
    }

    public void add(T item, UIAction<List<? extends BasicCommonNoteA>> action) {
        if (getDAO().insert(item) != -1) {
            setAction(action);
            onDataChangeActionCompleted();
        }
    }

    public void delete(T item, UIAction<List<? extends BasicCommonNoteA>> action) {
        if (getDAO().delete(item) != 0) {
            setAction(action);
            onDataChangeActionCompleted();
        }
    }

    public void edit(T item, UIAction<List<? extends BasicCommonNoteA>> action) {
        if (getDAO().update(item) != -1) {
            setAction(action);
            onDataChangeActionCompleted();
        }
    }
}
