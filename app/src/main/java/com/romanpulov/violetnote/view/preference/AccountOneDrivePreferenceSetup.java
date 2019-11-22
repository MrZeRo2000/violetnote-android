package com.romanpulov.violetnote.view.preference;

import android.content.DialogInterface;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v7.app.AlertDialog;

import com.romanpulov.library.dropbox.DropboxHelper;
import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.onedrive.OneDriveHelper;
import com.romanpulov.violetnote.view.SettingsActivity;

/**
 * Dropbox account configuration
 * Created by romanpulov on 08.09.2017.
 */

public class AccountOneDrivePreferenceSetup extends PreferenceSetup {

    public AccountOneDrivePreferenceSetup(PreferenceFragment preferenceFragment) {
        super(preferenceFragment, PreferenceRepository.PREF_KEY_ACCOUNT_ONEDRIVE);
    }

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
                                OneDriveHelper.getInstance().createClient(mActivity);
                            }
                        })

                        .setNegativeButton(R.string.ui_dialog_button_logout, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                OneDriveHelper.getInstance().logout(mActivity);
                            }
                        })
                        .show();
                return true;
            }
        });
    }
}
