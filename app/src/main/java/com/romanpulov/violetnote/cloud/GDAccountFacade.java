package com.romanpulov.violetnote.cloud;

import android.app.Activity;

import androidx.appcompat.app.AlertDialog;

import com.romanpulov.library.common.account.AbstractCloudAccountManager;
import com.romanpulov.library.gdrive.OnGDActionListener;
import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.loader.gdrive.BackupGDriveUploader;
import com.romanpulov.violetnote.loader.gdrive.RestoreGDriveDownloader;
import com.romanpulov.violetnote.loader.gdrive.SilentBackupGDriveUploader;
import com.romanpulov.violetnote.picker.HrPickerNavigator;
import com.romanpulov.violetnote.view.helper.DisplayMessageHelper;
import com.romanpulov.violetnote.view.preference.PreferenceRepository;

public class GDAccountFacade implements CloudAccountFacade{
    @Override
    public void interactiveSetupAccount(Activity activity) {
        final AlertDialog.Builder alert = new AlertDialog.Builder(activity, R.style.AlertDialogTheme);
        alert
                .setTitle(R.string.ui_dialog_title_confirmation)
                .setPositiveButton(R.string.ui_dialog_button_login, (dialog, which) -> GDHelper.getInstance().login(
                        new OnGDActionListener<>() {
                            @Override
                            public void onActionSuccess(Void unused) {
                                DisplayMessageHelper.displayInfoMessage(activity, R.string.message_gdrive_successfully_logged_in);
                            }

                            @Override
                            public void onActionFailure(Exception e) {
                                DisplayMessageHelper.displayInfoMessage(activity, R.string.error_gdrive_login, e.getMessage());
                            }
                        }
                ))
                .setNegativeButton(R.string.ui_dialog_button_logout, (dialog, which) -> GDHelper.getInstance().logout(
                        activity, new OnGDActionListener<>() {
                            @Override
                            public void onActionSuccess(Void unused) {
                                DisplayMessageHelper.displayInfoMessage(activity, R.string.message_gdrive_successfully_logged_out);
                            }

                            @Override
                            public void onActionFailure(Exception e) {
                                DisplayMessageHelper.displayInfoMessage(activity, R.string.error_gdrive_logout, e.getMessage());
                            }
                        }
                ))
                .show();
    }

    @Override
    public AbstractCloudAccountManager<?> getAccountManager(Activity activity) {
        return new GDCloudAccountManager(activity);
    }

    @Override
    public HrPickerNavigator getHrPickerNavigator() {
        return null;
    }

    @Override
    public String getBackupLoaderClassName() {
        return BackupGDriveUploader.class.getName();
    }

    @Override
    public String getSilentBackupLoaderClassName() {
        return SilentBackupGDriveUploader.class.getName();
    }

    @Override
    public String getRestoreLoaderClassName() {
        return RestoreGDriveDownloader.class.getName();
    }

    @Override
    public String getDocumentLoaderClassName() {
        return null;
    }

    @Override
    public String getPreferenceAccountKey() {
        return PreferenceRepository.PREF_KEY_ACCOUNT_GD;
    }
}
