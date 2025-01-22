package com.romanpulov.violetnote.view.core;

import android.app.Application;
import com.romanpulov.violetnote.db.dao.BasicCommonNoteDAO;
import com.romanpulov.violetnote.model.core.BasicCommonNoteA;
import com.romanpulov.violetnote.model.core.BasicOrderedEntityNoteA;
import com.romanpulov.violetnote.view.action.UIAction;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class BasicCommonNoteViewModel<T extends BasicCommonNoteA> extends BasicEntityNoteViewModel<T> {

    private BasicCommonNoteDAO mBasicCommonNoteDAO;

    private BasicCommonNoteDAO getBasicCommonNoteDAO() {
        if (mBasicCommonNoteDAO == null) {
            mBasicCommonNoteDAO = new BasicCommonNoteDAO(getApplication());
        }
        return mBasicCommonNoteDAO;
    }

    public BasicCommonNoteViewModel(@NotNull Application application) {
        super(application);
    }

    public void moveUp(List<T> items, UIAction<T> action) {
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

    public void moveTop(List<T> items, UIAction<T> action) {
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

    public void moveDown(List<T> items, UIAction<T> action) {
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

    public void moveBottom(List<T> items, UIAction<T> action) {
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
}
