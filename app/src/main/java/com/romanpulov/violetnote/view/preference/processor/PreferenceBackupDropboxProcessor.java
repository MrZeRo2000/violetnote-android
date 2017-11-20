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

    public PreferenceBackupDropboxProcessor(PreferenceFragment preferenceFragment) {
        super(preferenceFragment);
    }

    @Override
    public void loaderPreExecute() {
        PreferenceRepository.updateDropboxBackupPreferenceSummary(mPreferenceFragment, PreferenceRepository.PREF_LOAD_LOADING);
    }

    @Override
    public void loaderPostExecute(String result) {
        if (result == null) {
            long loadedTime = System.currentTimeMillis();
            PreferenceRepository.updateDropboxBackupPreferenceSummary(mPreferenceFragment, loadedTime);
        } else {
            PreferenceRepository.displayMessage(mContext, result);
            PreferenceRepository.updateDropboxBackupPreferenceSummary(mPreferenceFragment, PreferenceRepository.PREF_LOAD_CURRENT_VALUE);
        }
    }

    public static Class<? extends AbstractContextLoader> getLoaderClass() {
        return BackupDropboxUploader.class;
    }
}
