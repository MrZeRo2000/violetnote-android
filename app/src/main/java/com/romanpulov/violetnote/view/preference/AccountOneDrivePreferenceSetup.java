package com.romanpulov.violetnote.view.preference;

import android.content.DialogInterface;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.appcompat.app.AlertDialog;

import com.romanpulov.library.onedrive.OneDriveHelper;
import com.romanpulov.violetnote.R;

import static com.romanpulov.library.onedrive.OneDriveHelper.ONEDRIVE_ACTION_LOGIN;
import static com.romanpulov.library.onedrive.OneDriveHelper.ONEDRIVE_ACTION_LOGOUT;

/**
 * OneDrive account configuration
 * Created by romanpulov on 21.11.2019.
 */
public class AccountOneDrivePreferenceSetup extends PreferenceSetup {

    public AccountOneDrivePreferenceSetup(PreferenceFragmentCompat preferenceFragment) {
        super(preferenceFragment, PreferenceRepository.PREF_KEY_ACCOUNT_ONEDRIVE);
    }

    private OneDriveHelper.OnOneDriveActionListener onOneDriveActionListener = new OneDriveHelper.OnOneDriveActionListener() {
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
                displayMessageId = successMessageId;
            } else {
                displayMessageId = failureMessageId;
            }

            PreferenceRepository.displayMessage(mContext, mContext.getString(displayMessageId));
        }
    };

    @Override
    public void execute() {
        mPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                final AlertDialog.Builder alert = new AlertDialog.Builder(mActivity, R.style.AlertDialogTheme);
                alert
                        .setTitle(R.string.ui_dialog_title_confirmation)
                        .setPositiveButton(R.string.ui_dialog_button_login, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                OneDriveHelper oneDriveHelper = OneDriveHelper.getInstance();
                                oneDriveHelper.setOnOneDriveActionListener(onOneDriveActionListener);
                                oneDriveHelper.createClient(mActivity);
                            }
                        })

                        .setNegativeButton(R.string.ui_dialog_button_logout, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                OneDriveHelper oneDriveHelper = OneDriveHelper.getInstance();
                                oneDriveHelper.setOnOneDriveActionListener(onOneDriveActionListener);
                                oneDriveHelper.logout(mActivity);
                            }
                        })
                        .show();
                return true;
            }
        });
    }
}
