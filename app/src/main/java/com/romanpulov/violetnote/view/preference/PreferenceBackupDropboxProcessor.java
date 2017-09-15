package com.romanpulov.violetnote.view.preference;

import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.romanpulov.violetnote.loader.AbstractLoader;
import com.romanpulov.violetnote.loader.BackupDropboxUploader;

/**
 * Created by romanpulov on 14.09.2017.
 */

public class PreferenceBackupDropboxProcessor extends PreferenceLoaderProcessor{

    public PreferenceBackupDropboxProcessor(PreferenceFragment preferenceFragment) {
        super(preferenceFragment);
    }

    private AbstractLoader createBackupDropboxUploader() {
        mLoader = new BackupDropboxUploader(mPreferenceFragment.getActivity());

        mLoader.setOnLoadedListener(new AbstractLoader.OnLoadedListener() {
            @Override
            public void onLoaded(String result) {

                if (result == null) {
                    long loadedTime = System.currentTimeMillis();
                    Preference pref = mPreferenceFragment.findPreference(PreferenceRepository.PREF_KEY_BASIC_NOTE_CLOUD_BACKUP);
                    pref.getPreferenceManager().getSharedPreferences().edit().putLong(PreferenceRepository.PREF_KEY_BASIC_NOTE_CLOUD_BACKUP_LAST_LOADED, loadedTime).apply();
                    PreferenceRepository.updateDropboxBackupPreferenceSummary(mPreferenceFragment, loadedTime);
                } else {
                    PreferenceRepository.displayMessage(mPreferenceFragment.getActivity(), result);
                    PreferenceRepository.updateDropboxBackupPreferenceSummary(mPreferenceFragment, PreferenceRepository.PREF_LOAD_CURRENT_VALUE);
                }
            }

            @Override
            public void onPreExecute() {
                PreferenceRepository.updateDropboxBackupPreferenceSummary(mPreferenceFragment, PreferenceRepository.PREF_LOAD_LOADING);
            }
        });

        return mLoader;
    }

    public AbstractLoader executeLoader() {
        if (mLoader == null)
            createBackupDropboxUploader();

        PreferenceLoaderProcessor.executeLoader(mLoader);

        return mLoader;
    }
}
