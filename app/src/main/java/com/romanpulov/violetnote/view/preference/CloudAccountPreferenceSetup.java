package com.romanpulov.violetnote.view.preference;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.romanpulov.violetnote.cloud.CloudAccountFacade;

public class CloudAccountPreferenceSetup extends PreferenceSetup {
    private final CloudAccountFacade mCloudAccountFacade;

    public CloudAccountPreferenceSetup(PreferenceFragmentCompat preferenceFragment, CloudAccountFacade cloudAccountFacade) {
        super(preferenceFragment, cloudAccountFacade.getPreferenceAccountKey());
        mCloudAccountFacade = cloudAccountFacade;
    }

    @Override
    public void execute() {
        mPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                mCloudAccountFacade.interactiveSetupAccount(mActivity);
                return true;
            }
        });
    }
}
