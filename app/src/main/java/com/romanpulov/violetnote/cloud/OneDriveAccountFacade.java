package com.romanpulov.violetnote.cloud;

import android.app.Activity;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;

import com.romanpulov.library.common.account.AbstractCloudAccountManager;
import com.romanpulov.library.onedrive.OneDriveHelper;
import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.loader.document.DocumentOneDriveFileLoader;
import com.romanpulov.violetnote.loader.onedrive.BackupOneDriveUploader;
import com.romanpulov.violetnote.loader.onedrive.RestoreOneDriveFileLoader;
import com.romanpulov.violetnote.view.preference.PreferenceRepository;

import static com.romanpulov.library.onedrive.OneDriveHelper.ONEDRIVE_ACTION_LOGIN;
import static com.romanpulov.library.onedrive.OneDriveHelper.ONEDRIVE_ACTION_LOGOUT;

public class OneDriveAccountFacade implements CloudAccountFacade {

    @Override
    public void interactiveSetupAccount(final Activity activity) {
        final AlertDialog.Builder alert = new AlertDialog.Builder(activity, R.style.AlertDialogTheme);

        final OneDriveHelper.OnOneDriveActionListener onOneDriveActionListener = new OneDriveHelper.OnOneDriveActionListener() {
            @Override
            public void onActionCompleted(int action, boolean result, String message) {
                int successMessageId;
                int failureMessageId;
                int displayMessageId;

                switch(action) {
                    case ONEDRIVE_ACTION_LOGIN:
                        successMessageId = R.string.message_onedrive_successfully_logged_in;
                        failureMessageId = R.string.error_onedrive_login;
                        break;
                    case ONEDRIVE_ACTION_LOGOUT:
                        successMessageId = R.string.message_onedrive_successfully_logged_out;
                        failureMessageId = R.string.error_onedrive_logout;
                        break;
                    default:
                        return;
                }

                if (result) {
                    PreferenceRepository.displayMessage(activity, activity.getString(successMessageId));
                } else {
                    PreferenceRepository.displayMessage(activity, activity.getString(failureMessageId, message));
                }
            }
        };

        alert
                .setTitle(R.string.ui_dialog_title_confirmation)
                .setPositiveButton(R.string.ui_dialog_button_login, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        OneDriveHelper oneDriveHelper = OneDriveHelper.getInstance();
                        oneDriveHelper.setOnOneDriveActionListener(onOneDriveActionListener);
                        oneDriveHelper.createClient(activity);
                    }
                })

                .setNegativeButton(R.string.ui_dialog_button_logout, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        OneDriveHelper oneDriveHelper = OneDriveHelper.getInstance();
                        oneDriveHelper.setOnOneDriveActionListener(onOneDriveActionListener);
                        oneDriveHelper.logout(activity);
                    }
                })
                .show();
    }

    @Override
    public AbstractCloudAccountManager<?> getAccountManager(Activity activity) {
        return new OneDriveCloudAccountManager(activity);
    }

    @Override
    public String getBackupLoaderClassName() {
        return BackupOneDriveUploader.class.getName();
    }

    @Override
    public String getRestoreLoaderClassName() {
        return RestoreOneDriveFileLoader.class.getName();
    }

    @Override
    public String getDocumentLoaderClassName() {
        return DocumentOneDriveFileLoader.class.getName();
    }

    @Override
    public String getPreferenceAccountKey() {
        return PreferenceRepository.PREF_KEY_ACCOUNT_ONEDRIVE;
    }
}
