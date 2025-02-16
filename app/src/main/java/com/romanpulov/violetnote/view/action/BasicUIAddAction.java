package com.romanpulov.violetnote.view.action;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BasicUIAddAction<T> implements UIAction<T> {
    private final RecyclerView mRecyclerView;

    public BasicUIAddAction(RecyclerView recyclerView) {
        this.mRecyclerView = recyclerView;
    }

    @Override
    public void execute(List<T> data) {
        int position = data.size() - 1;
        if (position > -1) {
            mRecyclerView.scrollToPosition(position);
        }
    }
}
