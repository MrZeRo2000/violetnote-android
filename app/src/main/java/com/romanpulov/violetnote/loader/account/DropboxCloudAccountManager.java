package com.romanpulov.violetnote.loader.account;

import android.app.Activity;

import com.romanpulov.library.dropbox.DropboxHelper;
import com.romanpulov.violetnote.R;

public class DropboxCloudAccountManager extends AbstractCloudAccountManager<DropboxHelper> {
    @Override
    protected DropboxHelper createAccountHolder() {
        return DropboxHelper.getInstance(mActivity.getApplicationContext());
    }

    @Override
    protected void internalSetupAccount() {
        String accessToken = mAccountHolder.getAccessToken();
        if (accessToken == null) {
            mAccountSetupListener.onAccountSetupFailure(mContext.getResources().getString(R.string.error_dropbox_auth));
        } else {
            mAccountSetupListener.onAccountSetupSuccess();
        }
    }

    public DropboxCloudAccountManager(Activity activity) {
        super(activity);
    }
}
