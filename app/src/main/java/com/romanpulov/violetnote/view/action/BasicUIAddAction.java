package com.romanpulov.violetnote.view.action;

import androidx.recyclerview.widget.RecyclerView;

import java.util.Collection;

public class BasicUIAddAction<T extends Collection<?>> implements UIAction<T> {
    private final RecyclerView mRecyclerView;

    public BasicUIAddAction(RecyclerView recyclerView) {
        this.mRecyclerView = recyclerView;
    }

    @Override
    public void execute(T data) {
        int position = data.size() - 1;
        if (position > -1) {
            mRecyclerView.scrollToPosition(position);
        }
    }
}
