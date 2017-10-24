package com.romanpulov.violetnote.view.preference.processor;

import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.romanpulov.violetnote.loader.AbstractLoader;
import com.romanpulov.violetnote.loader.DocumentLoaderFactory;
import com.romanpulov.violetnote.view.preference.PreferenceRepository;

/**
 * Loader processes executor class
 * Created by romanpulov on 11.09.2017.
 */

public class PreferenceDocumentLoaderProcessor extends PreferenceLoaderProcessor{

    public PreferenceDocumentLoaderProcessor(PreferenceFragment preferenceFragment) {
        super(preferenceFragment);
    }

    @Override
    public void loaderPreExecute() {
        PreferenceRepository.updateLoadPreferenceSummary(mPreferenceFragment, PreferenceRepository.PREF_LOAD_LOADING);
    }

    @Override
    public void loaderPostExecute(String result) {
        Preference pref = mPreferenceFragment.findPreference(PreferenceRepository.PREF_KEY_LOAD);

        if (result == null) {
            long loadedTime = System.currentTimeMillis();
            pref.getPreferenceManager().getSharedPreferences().edit().putLong(PreferenceRepository.PREF_KEY_LAST_LOADED, loadedTime).apply();
            PreferenceRepository.updateLoadPreferenceSummary(mPreferenceFragment, loadedTime);
        } else {
            PreferenceRepository.displayMessage(mContext, result);
            PreferenceRepository.updateLoadPreferenceSummary(mPreferenceFragment, PreferenceRepository.PREF_LOAD_CURRENT_VALUE);
        }
    }

    /**
     * Updates preference with load status when running
     */
    public void updateLoadPreferenceStatus() {
        if (isTaskRunning())
            PreferenceRepository.updateLoadPreferenceSummary(mPreferenceFragment, PreferenceRepository.PREF_LOAD_LOADING);
    }
}
