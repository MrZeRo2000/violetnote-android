package com.romanpulov.violetnote.view.action;

import com.romanpulov.violetnote.db.manager.DBNoteManager;

/**
 * Base abstract class for actions
 * Contains data and execution method
 * @param <T> data type
 */
public abstract class BasicAction<T> {
    protected final T mData;

    public BasicAction(T data) {
        this.mData = data;
    }

    public abstract boolean execute(DBNoteManager noteManager);
}

