package com.romanpulov.violetnote.view.preference;

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
        mPreference.setOnPreferenceClickListener(preference -> {
            mCloudAccountFacade.interactiveSetupAccount(mActivity);
            return true;
        });
    }
}
