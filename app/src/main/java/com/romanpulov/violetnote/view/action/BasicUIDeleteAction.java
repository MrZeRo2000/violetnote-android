package com.romanpulov.violetnote.view.action;


import androidx.appcompat.view.ActionMode;

import java.util.Collection;

public class BasicUIDeleteAction<T extends Collection<?>> implements UIAction<T> {
    private final ActionMode mMode;

    public BasicUIDeleteAction(ActionMode mode) {
        this.mMode = mode;
    }

    @Override
    public void execute(T data) {
        mMode.finish();
    }
}
