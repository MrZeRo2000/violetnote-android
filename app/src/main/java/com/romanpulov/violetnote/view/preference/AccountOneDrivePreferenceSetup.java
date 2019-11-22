package com.romanpulov.violetnote.view.preference;

import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.romanpulov.library.dropbox.DropboxHelper;
import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.onedrive.OneDriveHelper;

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
                OneDriveHelper.getInstance().createClient(mActivity);
                return true;
            }
        });
    }
}
