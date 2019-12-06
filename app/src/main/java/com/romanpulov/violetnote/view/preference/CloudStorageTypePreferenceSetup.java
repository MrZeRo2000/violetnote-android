package com.romanpulov.violetnote.view.preference;

import androidx.preference.PreferenceFragmentCompat;

import com.romanpulov.violetnote.R;

import static com.romanpulov.violetnote.view.preference.PreferenceRepository.DEFAULT_CLOUD_SOURCE_TYPE;
import static com.romanpulov.violetnote.view.preference.PreferenceRepository.DEFAULT_SOURCE_TYPE;

/**
 * Cloud storage type setup
 * Created by romanpulov on 08.09.2017.
 */

public class CloudStorageTypePreferenceSetup extends PreferenceSetup {
    private final int mDefaultValue;

    public CloudStorageTypePreferenceSetup(PreferenceFragmentCompat preferenceFragment) {
        super(preferenceFragment, PreferenceRepository.PREF_KEY_BASIC_NOTE_CLOUD_STORAGE);
        mDefaultValue = DEFAULT_CLOUD_SOURCE_TYPE;
    }

    @Override
    public void execute() {
        final String[] prefSourceTypeEntries = mContext.getResources().getStringArray(R.array.pref_cloud_storage_entries);

        mPreference.setSummary(prefSourceTypeEntries[mPreference.getPreferenceManager().getSharedPreferences().getInt(mPreference.getKey(), mDefaultValue)]);



        /*
        if (prefSourceTypeEntries.length == 1) {
            mPreference.setSummary(prefSourceTypeEntries[0]);
        } else {
            // TO-DO: Support for different cloud storage types
        }

         */
    }
}
