package com.romanpulov.violetnote.view.preference.processor;

import androidx.preference.PreferenceFragmentCompat;

import com.romanpulov.library.common.loader.core.AbstractContextLoader;
import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.db.DBStorageManager;
import com.romanpulov.violetnote.loader.local.BackupLocalLoader;
import com.romanpulov.violetnote.view.preference.PreferenceRepository;

import java.util.Locale;

/**
 * Local backup executor class
 * Created by romanpulov on 15.12.2017.
 */

public class PreferenceBackupLocalProcessor extends PreferenceLoaderProcessor {

    private static final String PREF_KEY_NAME = PreferenceRepository.PREF_KEY_BASIC_NOTE_LOCAL_BACKUP;

    public PreferenceBackupLocalProcessor(PreferenceFragmentCompat preferenceFragment) {
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

            String backupMessage = String.format(Locale.getDefault(), mContext.getString(R.string.message_backup_created), (new DBStorageManager(mContext)).getBackupFolderName());
            PreferenceRepository.displayMessage(mContext, backupMessage);
        } else {
            PreferenceRepository.displayMessage(mContext, result);
            PreferenceRepository.updatePreferenceKeySummary(mPreferenceFragment, PREF_KEY_NAME, PreferenceRepository.PREF_LOAD_CURRENT_VALUE);
        }
    }

    public static Class<? extends AbstractContextLoader> getLoaderClass() {
        return BackupLocalLoader.class;
    }
}
