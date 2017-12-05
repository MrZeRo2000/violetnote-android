package com.romanpulov.violetnote.view.preference.processor;

import android.preference.PreferenceFragment;

import com.romanpulov.violetnote.view.preference.PreferenceRepository;

/**
 * Loader processes executor class
 * Created by romanpulov on 11.09.2017.
 */

public class PreferenceDocumentLoaderProcessor extends PreferenceLoaderProcessor{

    private static final String PREF_KEY_NAME = PreferenceRepository.PREF_KEY_DOCUMENT_LOAD;

    public PreferenceDocumentLoaderProcessor(PreferenceFragment preferenceFragment) {
        super(preferenceFragment);
    }

    @Override
    public void loaderPreExecute() {
        PreferenceRepository.updatePreferenceKeySummary(mPreferenceFragment, PREF_KEY_NAME, PreferenceRepository.PREF_LOAD_LOADING);
    }

    @Override
    public void loaderPostExecute(String result) {
        if (result == null) {
            long loadedTime = System.currentTimeMillis();
            PreferenceRepository.updatePreferenceKeySummary(mPreferenceFragment, PREF_KEY_NAME, loadedTime);
        } else {
            PreferenceRepository.displayMessage(mContext, result);
            PreferenceRepository.updatePreferenceKeySummary(mPreferenceFragment, PREF_KEY_NAME, PreferenceRepository.PREF_LOAD_CURRENT_VALUE);
        }
    }
}
