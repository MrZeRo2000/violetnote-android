package com.romanpulov.violetnote.view.preference;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import com.romanpulov.violetnote.cloud.CloudAccountFacadeFactory;

import java.util.Objects;

/**
 * Dropbox account configuration
 * Created by romanpulov on 08.09.2017.
 */

public class AccountDropboxPreferenceSetup extends PreferenceSetup {

    public AccountDropboxPreferenceSetup(PreferenceFragmentCompat preferenceFragment) {
        super(preferenceFragment, PreferenceRepository.PREF_KEY_ACCOUNT_DROPBOX);
    }

    @Override
    public void execute() {
        mPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                CloudAccountFacadeFactory
                        .fromCloudSourceType(PreferenceRepository.CLOUD_SOURCE_TYPE_DROPBOX)
                        .interactiveSetupAccount(mActivity);
                return true;
            }
        });
    }
}
