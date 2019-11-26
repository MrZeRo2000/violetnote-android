package com.romanpulov.violetnote.loader.account;

import android.app.Activity;

import com.romanpulov.library.dropbox.DropboxHelper;
import com.romanpulov.violetnote.R;

public class DropboxAccountManager extends AbstractAccountManager<DropboxHelper> {
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

    public DropboxAccountManager(Activity activity) {
        super(activity);
    }
}
