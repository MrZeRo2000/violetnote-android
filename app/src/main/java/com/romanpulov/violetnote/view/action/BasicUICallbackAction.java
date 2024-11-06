package com.romanpulov.violetnote.view.action;

import java.util.Collection;

public class BasicUICallbackAction<T extends Collection<?>> implements UIAction<T> {
    private final UIActionCallback mExecuteCallback;

    public BasicUICallbackAction(UIActionCallback mExecuteCallback) {
        this.mExecuteCallback = mExecuteCallback;
    }

    @Override
    public void execute(T data) {
        if (mExecuteCallback != null) {
            mExecuteCallback.onExecute();
        }
    }
}
