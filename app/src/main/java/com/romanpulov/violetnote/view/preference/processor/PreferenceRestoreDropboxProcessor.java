package com.romanpulov.violetnote.view.preference.processor;

import android.preference.PreferenceFragment;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.db.DBBasicNoteHelper;
import com.romanpulov.violetnote.db.DBStorageManager;
import com.romanpulov.library.common.loader.core.AbstractContextLoader;
import com.romanpulov.library.common.loader.file.FileLoader;
import com.romanpulov.violetnote.loader.dropbox.RestoreDropboxFileLoader;
import com.romanpulov.violetnote.view.preference.PreferenceRepository;

import java.io.File;

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
    }

    @Override
    public void loaderPostExecute(String result) {

        if (result != null) {
            PreferenceRepository.displayMessage(mContext, result);
            PreferenceRepository.updatePreferenceKeySummary(mPreferenceFragment, PREF_KEY_NAME, PreferenceRepository.PREF_LOAD_CURRENT_VALUE);
        }
        else {
            FileLoader fileLoader = new RestoreDropboxFileLoader(mContext);
            File file = new File(fileLoader.getLoadPathProvider().getDestPath());
            if (file.exists()) {
                DBBasicNoteHelper.getInstance(mContext).closeDB();

                DBStorageManager storageManager = new DBStorageManager(mContext, file.getParent());
                String restoreResult = storageManager.restoreLocalBackup();

                String restoreMessage;
                if (restoreResult == null) {
                    restoreMessage = mContext.getString(R.string.error_restore);
                    PreferenceRepository.updatePreferenceKeySummary(mPreferenceFragment, PREF_KEY_NAME, PreferenceRepository.PREF_LOAD_CURRENT_VALUE);
                }
                else {
                    restoreMessage = mContext.getString(R.string.message_backup_cloud_restored);
                    long loadedTime = System.currentTimeMillis();
                    PreferenceRepository.updatePreferenceKeySummary(mPreferenceFragment, PREF_KEY_NAME, loadedTime);
                }

                DBBasicNoteHelper.getInstance(mContext).openDB();

                PreferenceRepository.displayMessage(mContext, restoreMessage);

            } else {
                PreferenceRepository.updatePreferenceKeySummary(mPreferenceFragment, PREF_KEY_NAME, PreferenceRepository.PREF_LOAD_CURRENT_VALUE);
                PreferenceRepository.displayMessage(mContext, mPreferenceFragment.getString(R.string.error_restore));
            }
        }
    }

    public static Class<? extends AbstractContextLoader> getLoaderClass() {
        return RestoreDropboxFileLoader.class;
    }
}
