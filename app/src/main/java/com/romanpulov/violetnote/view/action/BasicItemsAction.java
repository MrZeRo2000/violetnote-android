package com.romanpulov.violetnote.view.action;

import java.util.List;

/**
 * Base abstract class for actions with data and list of items
 * @param <T> data type
 * @param <I> item data type
 */
public abstract class BasicItemsAction<T, I> extends BasicAction<T> {
    protected final List<I> mItems;

    public BasicItemsAction(T data, List<I> items) {
        super(data);
        mItems = items;
    }
}
