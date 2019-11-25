package com.romanpulov.violetnote.loader.account;

import android.app.Activity;
import android.content.Context;

public abstract class AbstractAccountManager <T>  {
    protected T mAccountHolder;

    protected final Activity mActivity;
    protected final Context mContext;

    protected T getAccountHolder() {
        return mAccountHolder;
    }

    protected void setAccountHolder(T value) {
        mAccountHolder = value;
    }

    protected abstract T createAccountHolder();

    public interface OnAccountSetupListener {
        void onAccountSetupSuccess();
        void onAccountSetupFailure(String errorText);
    }

    protected OnAccountSetupListener mOnAccountSetupListener;

    public void setOnAccountSetupListener(OnAccountSetupListener value) {
        this.mOnAccountSetupListener = value;
    }

    protected abstract void internalSetupAccount();

    public void setupAccount() {
        if (mOnAccountSetupListener != null) {
            internalSetupAccount();
        }
    };

    public AbstractAccountManager(Activity activity) {
        mActivity = activity;
        mContext = activity.getApplicationContext();
        mAccountHolder = createAccountHolder();
    }
}
