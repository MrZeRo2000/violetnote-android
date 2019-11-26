package com.romanpulov.violetnote.loader.account;

import android.app.Activity;

import com.romanpulov.violetnote.onedrive.OneDriveHelper;

public class OneDriveAccountManager extends AbstractAccountManager<OneDriveHelper> {
    @Override
    protected OneDriveHelper createAccountHolder() {
        return OneDriveHelper.getInstance();
    }

    @Override
    protected void internalSetupAccount() {
        mAccountHolder.setOnOneDriveActionListener(new OneDriveHelper.OnOneDriveActionListener() {
            @Override
            public void onActionCompleted(int action, boolean result, String message) {
                if (result) {
                    mOnAccountSetupListener.onAccountSetupSuccess();
                } else {
                    mOnAccountSetupListener.onAccountSetupFailure(message);
                }
            }
        });
        mAccountHolder.createClient(mActivity);
    }

    public OneDriveAccountManager(Activity activity) {
        super(activity);
    }
}
