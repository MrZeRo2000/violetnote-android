package com.romanpulov.violetnote.view.preference.processor;

import android.preference.PreferenceFragment;

import com.romanpulov.library.common.loader.core.AbstractContextLoader;
import com.romanpulov.violetnote.loader.dropbox.RestoreDropboxFileLoader;
import com.romanpulov.violetnote.view.preference.PreferenceRepository;

/**
 * Restore from DropBox processor
 * Created by romanpulov on 20.09.2017.
 */
public class PreferenceRestoreDropboxProcessor extends PreferenceLoaderProcessor {

    private static final String PREF_KEY_NAME = PreferenceRepository.PREF_KEY_BASIC_NOTE_CLOUD_RESTORE;

    public PreferenceRestoreDropboxProcessor(PreferenceFragment preferenceFragment) {
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

    public static Class<? extends AbstractContextLoader> getLoaderClass() {
        return RestoreDropboxFileLoader.class;
    }
}
