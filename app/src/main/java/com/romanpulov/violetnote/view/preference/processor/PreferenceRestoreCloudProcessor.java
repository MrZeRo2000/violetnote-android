package com.romanpulov.violetnote.view.preference.processor;

import androidx.preference.PreferenceFragmentCompat;

import com.romanpulov.violetnote.view.preference.PreferenceRepository;

/**
 * Restore from Cloud processor
 * Created by romanpulov on 20.09.2017.
 */
public class PreferenceRestoreCloudProcessor extends PreferenceLoaderProcessor {

    private static final String PREF_KEY_NAME = PreferenceRepository.PREF_KEY_BASIC_NOTE_CLOUD_RESTORE;

    public PreferenceRestoreCloudProcessor(PreferenceFragmentCompat preferenceFragment) {
        super(preferenceFragment);
    }

    @Override
    public void loaderPreExecute() {
        PreferenceRepository.updatePreferenceKeySummary(mPreferenceFragment, PREF_KEY_NAME, PreferenceRepository.PREF_LOAD_LOADING);
        PreferenceRepository.setProgressPreferenceVisibility(mPreferenceFragment, PREF_KEY_NAME, true);
    }

    @Override
    public void loaderPostExecute(String result) {
        PreferenceRepository.setProgressPreferenceVisibility(mPreferenceFragment, PREF_KEY_NAME, false);
        if (result == null) {
            long loadedTime = System.currentTimeMillis();
            PreferenceRepository.updatePreferenceKeySummary(mPreferenceFragment, PREF_KEY_NAME, loadedTime);
        }
        else
            PreferenceRepository.updatePreferenceKeySummary(mPreferenceFragment, PREF_KEY_NAME, PreferenceRepository.PREF_LOAD_CURRENT_VALUE);
    }

}
