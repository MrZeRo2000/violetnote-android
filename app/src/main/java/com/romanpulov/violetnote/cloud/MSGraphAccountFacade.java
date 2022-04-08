package com.romanpulov.violetnote.cloud;

import android.app.Activity;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;

import com.romanpulov.library.common.account.AbstractCloudAccountManager;
import com.romanpulov.library.msgraph.OnMSActionListener;
import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.loader.msgraph.BackupMSGraphUploader;
import com.romanpulov.violetnote.loader.msgraph.DocumentMSGraphFileLoader;
import com.romanpulov.violetnote.loader.msgraph.RestoreMSGraphDownloader;
import com.romanpulov.violetnote.loader.msgraph.SilentBackupMSGraphUploader;
import com.romanpulov.violetnote.picker.HrPickerNavigator;
import com.romanpulov.violetnote.view.helper.DisplayMessageHelper;
import com.romanpulov.violetnote.view.preference.PreferenceRepository;

public class MSGraphAccountFacade implements CloudAccountFacade {
    private void displayMessage(Activity activity, int displayMessageId) {
        DisplayMessageHelper.displayInfoMessage(activity, activity.getString(displayMessageId));
    }

    private void displayMessage(Activity activity, int displayMessageId, String message) {
        DisplayMessageHelper.displayInfoMessage(activity, activity.getString(displayMessageId, message));
    }

    @Override
    public void interactiveSetupAccount(final Activity activity) {
        final AlertDialog.Builder alert = new AlertDialog.Builder(activity, R.style.AlertDialogTheme);
        alert
                .setTitle(R.string.ui_dialog_title_confirmation)
                .setPositiveButton(R.string.ui_dialog_button_login, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MSGraphHelper.getInstance().login(
                                activity,
                                new OnMSActionListener<String>() {
                                    @Override
                                    public void onActionSuccess(int action, String data) {
                                        displayMessage(activity, R.string.message_onedrive_successfully_logged_in);
                                    }

                                    @Override
                                    public void onActionFailure(int action, String errorMessage) {
                                        displayMessage(activity, R.string.error_onedrive_login, errorMessage);
                                    }
                                }
                        );
                    }
                })
                .setNegativeButton(R.string.ui_dialog_button_logout, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MSGraphHelper.getInstance().logout(
                                activity,
                                new OnMSActionListener<Void>() {
                                    @Override
                                    public void onActionSuccess(int action, Void data) {
                                        displayMessage(activity, R.string.message_onedrive_successfully_logged_out);
                                    }

                                    @Override
                                    public void onActionFailure(int action, String errorMessage) {
                                        displayMessage(activity, R.string.error_onedrive_logout, errorMessage);
                                    }
                                }
                        );
                    }
                })
                .show();
    }

    @Override
    public AbstractCloudAccountManager<?> getAccountManager(Activity activity) {
        return new MSGraphCloudAccountManager(activity);
    }

    @Override
    public HrPickerNavigator getHrPickerNavigator() {
        return MSGraphHelper.getInstance();
    }

    @Override
    public String getBackupLoaderClassName() {
        return BackupMSGraphUploader.class.getName();
    }

    @Override
    public String getSilentBackupLoaderClassName() {
        return SilentBackupMSGraphUploader.class.getName();
    }

    @Override
    public String getRestoreLoaderClassName() {
        return RestoreMSGraphDownloader.class.getName();
    }

    @Override
    public String getDocumentLoaderClassName() {
        return DocumentMSGraphFileLoader.class.getName();
    }

    @Override
    public String getPreferenceAccountKey() {
        return PreferenceRepository.PREF_KEY_ACCOUNT_MSGRAPH;
    }
}
