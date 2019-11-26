package com.romanpulov.violetnote.loader.account;

import android.app.Activity;

import com.onedrive.sdk.core.ClientException;
import com.onedrive.sdk.extensions.Item;
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
                    mAccountSetupListener.onAccountSetupSuccess();
                } else {
                    mAccountSetupListener.onAccountSetupFailure(message);
                }
            }
        });
        mAccountHolder.createClient(mActivity);
    }

    @Override
    public void setupItemId(String path) {
        mAccountHolder.setOnOneDriveItemListener(new OneDriveHelper.OnOneDriveItemListener() {
            @Override
            public void onItemReceived(Item item) {
                if (mAccountSetupItemListener != null) {
                    mAccountSetupItemListener.onSetupItemSuccess(item.id);
                }
            }

            @Override
            public void onItemFailure(ClientException ex) {
                if (mAccountSetupItemListener != null) {
                    mAccountSetupItemListener.onSetupItemFailure(ex.getMessage());
                }

            }
        });
        mAccountHolder.listItems(mActivity, path);
    }


    public OneDriveAccountManager(Activity activity) {
        super(activity);
    }
}
