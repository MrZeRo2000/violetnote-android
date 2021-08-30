package com.romanpulov.violetnote.view.preference.processor;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.romanpulov.violetnote.view.preference.PreferenceRepository;

import java.util.Objects;

/**
 * Loader processes executor class
 * Created by romanpulov on 11.09.2017.
 */

public class PreferenceDocumentLoaderProcessor extends PreferenceLoaderProcessor{

    private static final String PREF_KEY_NAME = PreferenceRepository.PREF_KEY_DOCUMENT_LOAD;

    public PreferenceDocumentLoaderProcessor(PreferenceFragmentCompat preferenceFragment) {
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
            final Preference deletePref = Objects.requireNonNull(mPreferenceFragment.findPreference(PreferenceRepository.PREF_KEY_DOCUMENT_DELETE));
            deletePref.setVisible(true);
        } else
            PreferenceRepository.updatePreferenceKeySummary(mPreferenceFragment, PREF_KEY_NAME, PreferenceRepository.PREF_LOAD_CURRENT_VALUE);
    }
}
