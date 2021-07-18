package com.romanpulov.violetnote.cloud;

import android.app.Activity;

import com.romanpulov.library.common.account.AbstractCloudAccountManager;

public interface CloudAccountFacade {
    void interactiveSetupAccount(Activity activity);
    AbstractCloudAccountManager<?> getAccountManager(Activity activity);
    String getBackupLoaderClassName();
    String getRestoreLoaderClassName();
    String getDocumentLoaderClassName();
}
