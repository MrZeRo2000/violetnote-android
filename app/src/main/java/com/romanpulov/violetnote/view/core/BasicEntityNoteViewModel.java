package com.romanpulov.violetnote.view.core;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import com.romanpulov.violetnote.db.dao.AbstractDAO;
import com.romanpulov.violetnote.view.action.UIAction;
import java.util.Collection;


public abstract class BasicEntityNoteViewModel<T> extends AndroidViewModel {
    private UIAction<T> mAction;

    public UIAction<T> getAction() {
        return mAction;
    }

    protected void setAction(UIAction<T> mAction) {
        this.mAction = mAction;
    }

    public void resetAction() {
        mAction = null;
    }

    protected abstract void onDataChangeActionCompleted();
    protected abstract AbstractDAO<T> getDAO();

    public BasicEntityNoteViewModel(@NonNull Application application) {
        super(application);
    }

    public void add(T item, UIAction<T> action) {
        if (getDAO().insert(item) != -1) {
            setAction(action);
            onDataChangeActionCompleted();
        }
    }

    public void delete(T item, UIAction<T> action) {
        if (getDAO().delete(item) != 0) {
            setAction(action);
            onDataChangeActionCompleted();
        }
    }

    public void delete(Collection<T> items, UIAction<T> action) {
        items.forEach(item -> getDAO().delete(item));
        setAction(action);
        onDataChangeActionCompleted();
    }

    public void edit(T item, UIAction<T> action) {
        if (getDAO().update(item) == 1) {
            setAction(action);
            onDataChangeActionCompleted();
        }
    }
}
