package com.romanpulov.violetnote.view.action;

import java.util.List;

public class BasicUICallbackAction<T> implements UIAction<T> {
    private final UIActionCallback mExecuteCallback;

    public BasicUICallbackAction(UIActionCallback mExecuteCallback) {
        this.mExecuteCallback = mExecuteCallback;
    }

    @Override
    public void execute(List<T> data) {
        if (mExecuteCallback != null) {
            mExecuteCallback.onExecute();
        }
    }
}
