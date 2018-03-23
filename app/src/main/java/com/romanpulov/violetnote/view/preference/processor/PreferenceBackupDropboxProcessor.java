package com.romanpulov.violetnote.view.preference.processor;

import android.preference.PreferenceFragment;

import com.romanpulov.library.common.loader.core.AbstractContextLoader;
import com.romanpulov.violetnote.loader.dropbox.BackupDropboxUploader;
import com.romanpulov.violetnote.view.preference.PreferenceRepository;

/**
 * Preference backup to Dropbox processor
 * Created by romanpulov on 14.09.2017.
 */
public class PreferenceBackupDropboxProcessor extends PreferenceLoaderProcessor{

    private static final String PREF_KEY_NAME = PreferenceRepository.PREF_KEY_BASIC_NOTE_CLOUD_BACKUP;

    public PreferenceBackupDropboxProcessor(PreferenceFragment preferenceFragment) {
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
        } else
            PreferenceRepository.updatePreferenceKeySummary(mPreferenceFragment, PREF_KEY_NAME, PreferenceRepository.PREF_LOAD_CURRENT_VALUE);
    }

    public static Class<? extends AbstractContextLoader> getLoaderClass() {
        return BackupDropboxUploader.class;
    }
}
