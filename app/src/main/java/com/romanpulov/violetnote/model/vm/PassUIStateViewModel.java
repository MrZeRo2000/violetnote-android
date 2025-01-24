package com.romanpulov.violetnote.model.vm;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Objects;

public class PassUIStateViewModel extends ViewModel {
    public final static int UI_STATE_PASSWORD_REQUIRED = 0;
    public final static int UI_STATE_LOADING = 1;
    public final static int UI_STATE_LOADED = 2;
    public final static int UI_STATE_LOAD_ERROR = 3;

    private final MutableLiveData<Integer> mUIState = new MutableLiveData<>(UI_STATE_PASSWORD_REQUIRED);
    private final MutableLiveData<String> mPassword = new MutableLiveData<>();

    public LiveData<Integer> getUIState() {
        return mUIState;
    }

    public void setUIState(int state) {
        if (!Objects.equals(state, mUIState.getValue())) {
            mUIState.setValue(state);
        }
    }

    public LiveData<String> getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        mPassword.postValue(password);
        if (password == null) {
            mUIState.setValue(UI_STATE_PASSWORD_REQUIRED);
        } else {
            mUIState.setValue(UI_STATE_LOADING);
        }
    }

    public void requirePassword() {
        if (mPassword.getValue() != null) {
            mPassword.postValue(null);
        }
    }
}
