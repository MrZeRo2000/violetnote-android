package com.romanpulov.violetnote.view.preference;

import android.preference.PreferenceFragment;

import com.romanpulov.violetnote.R;

/**
 * Cloud storage type setup
 * Created by romanpulov on 08.09.2017.
 */

public class CloudStorageTypePreferenceSetup extends PreferenceSetup {

    public CloudStorageTypePreferenceSetup(PreferenceFragment preferenceFragment) {
        super(preferenceFragment, PreferenceRepository.PREF_KEY_BASIC_NOTE_CLOUD_STORAGE);
    }

    @Override
    public void execute() {
        final String[] prefSourceTypeEntries = mContext.getResources().getStringArray(R.array.pref_cloud_storage_entries);

        if (prefSourceTypeEntries.length == 1) {
            mPreference.setSummary(prefSourceTypeEntries[0]);
        } else {
            // TO-DO: Support for different cloud storage types
        }
    }
}
