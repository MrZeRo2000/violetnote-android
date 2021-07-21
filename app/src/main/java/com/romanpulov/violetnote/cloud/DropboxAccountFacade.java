package com.romanpulov.violetnote.cloud;

import android.app.Activity;

import com.romanpulov.library.common.account.AbstractCloudAccountManager;
import com.romanpulov.library.dropbox.DropboxHelper;
import com.romanpulov.violetnote.loader.document.DocumentDropboxFileLoader;
import com.romanpulov.violetnote.loader.dropbox.BackupDropboxUploader;
import com.romanpulov.violetnote.loader.dropbox.RestoreDropboxFileLoader;
import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.picker.HrPickerNavigator;
import com.romanpulov.violetnote.view.preference.PreferenceRepository;

public class DropboxAccountFacade implements CloudAccountFacade {
    @Override
    public void interactiveSetupAccount(Activity activity) {
        DropboxHelper.getInstance(activity.getApplicationContext()).invokeAuthActivity(activity.getResources().getString(R.string.app_key));
    }

    @Override
    public AbstractCloudAccountManager<?> getAccountManager(Activity activity) {
        return new DropboxCloudAccountManager(activity);
    }

    @Override
    public HrPickerNavigator getHrPickerNavigator() {
        return null;
    }

    @Override
    public String getRestoreLoaderClassName() {
        return RestoreDropboxFileLoader.class.getName();
    }

    @Override
    public String getBackupLoaderClassName() {
        return BackupDropboxUploader.class.getName();
    }

    @Override
    public String getDocumentLoaderClassName() {
        return DocumentDropboxFileLoader.class.getName();
    }

    @Override
    public String getPreferenceAccountKey() {
        return PreferenceRepository.PREF_KEY_ACCOUNT_DROPBOX;
    }
}
