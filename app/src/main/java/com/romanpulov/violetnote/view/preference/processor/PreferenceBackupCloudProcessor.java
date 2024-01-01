package com.romanpulov.violetnote.view.preference.processor;

import androidx.preference.PreferenceFragmentCompat;

import com.romanpulov.violetnote.view.preference.PreferenceRepository;

/**
 * Preference backup to Cloud processor
 * Created by romanpulov on 14.09.2017.
 */
public class PreferenceBackupCloudProcessor extends PreferenceLoaderProcessor{

    private static final String PREF_KEY_NAME = PreferenceRepository.PREF_KEY_BASIC_NOTE_CLOUD_BACKUP;

    public PreferenceBackupCloudProcessor(PreferenceFragmentCompat preferenceFragment) {
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
        } else
            PreferenceRepository.updatePreferenceKeySummary(mPreferenceFragment, PREF_KEY_NAME, PreferenceRepository.PREF_LOAD_CURRENT_VALUE);
    }
}
