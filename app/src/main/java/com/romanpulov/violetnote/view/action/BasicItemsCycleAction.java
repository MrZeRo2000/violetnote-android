package com.romanpulov.violetnote.view.action;

import com.romanpulov.violetnote.db.manager.DBNoteManager;

import java.util.List;

/**
 * Base abstract class for processing items in a cycle,
 * provides an abstract method to override for action on a single line item
 * @param <T> data type
 * @param <I> item data type
 */
public abstract class BasicItemsCycleAction<T, I> extends BasicItemsAction<T, I> {

    public BasicItemsCycleAction(T data, List<I> items) {
        super(data, items);
    }

    protected abstract boolean execute(DBNoteManager noteManager, I item);

    @Override
    public boolean execute(DBNoteManager noteManager) {
        boolean result = false;

        for (I item : mItems) {
            if (execute(noteManager, item))
                result = true;
            else
                break;
        }

        return result;
    }
}

