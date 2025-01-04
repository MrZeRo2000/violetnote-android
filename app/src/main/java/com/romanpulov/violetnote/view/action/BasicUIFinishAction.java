package com.romanpulov.violetnote.view.action;


import androidx.appcompat.view.ActionMode;

import java.util.List;

public class BasicUIFinishAction<T> implements UIAction<T> {
    private final ActionMode mMode;

    public BasicUIFinishAction(ActionMode mode) {
        this.mMode = mode;
    }

    @Override
    public void execute(List<T> data) {
        mMode.finish();
    }
}
