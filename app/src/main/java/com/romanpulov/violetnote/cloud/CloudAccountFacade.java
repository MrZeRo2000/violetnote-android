package com.romanpulov.violetnote.cloud;

import android.app.Activity;

import com.romanpulov.library.common.account.AbstractCloudAccountManager;
import com.romanpulov.violetnote.picker.HrPickerNavigator;

public interface CloudAccountFacade {
    void interactiveSetupAccount(Activity activity);
    AbstractCloudAccountManager<?> getAccountManager(Activity activity);
    HrPickerNavigator getHrPickerNavigator();
    String getBackupLoaderClassName();
    String getSilentBackupLoaderClassName();
    String getRestoreLoaderClassName();
    String getDocumentLoaderClassName();
    String getPreferenceAccountKey();
}
