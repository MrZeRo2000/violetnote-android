package com.romanpulov.violetnote.view.preference;

import android.preference.PreferenceFragment;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.db.DBBasicNoteHelper;
import com.romanpulov.violetnote.db.DBStorageManager;
import com.romanpulov.violetnote.loader.AbstractLoader;
import com.romanpulov.violetnote.loader.DropboxFileLoader;
import com.romanpulov.violetnote.loader.FileLoader;
import com.romanpulov.violetnote.loader.RestoreDropboxFileLoader;
import com.romanpulov.violetnote.loader.RestoreDropboxLoadPathProvider;

import java.io.File;

/**
 * Restore from DropBox processor
 * Created by romanpulov on 20.09.2017.
 */

public class PreferenceRestoreDropboxProcessor extends PreferenceLoaderProcessor<FileLoader> {

    public PreferenceRestoreDropboxProcessor(PreferenceFragment preferenceFragment) {
        super(preferenceFragment);
    }

    private FileLoader createRestoreDropboxLoader() {
        //mLoader = new RestoreDropboxFileLoader(mContext);
        mLoader = new RestoreDropboxFileLoader(mContext);

        mLoader.setOnLoadedListener(new AbstractLoader.OnLoadedListener() {
            @Override
            public void onLoaded(String result) {
                PreferenceRepository.setDropboxRestoreDefaultPreferenceSummary(mPreferenceFragment);

                if (result != null)
                    PreferenceRepository.displayMessage(mContext, result);
                else {
                    FileLoader fileLoader = PreferenceRestoreDropboxProcessor.this.getRestoreDropboxLoader();
                    File file = new File(fileLoader.getLoadPathProvider().getDestPath());
                    if (file.exists()) {
                        DBBasicNoteHelper.getInstance(mContext).closeDB();

                        DBStorageManager storageManager = new DBStorageManager(mContext, file.getParent());
                        String restoreResult = storageManager.restoreLocalBackup();

                        String restoreMessage;
                        if (restoreResult == null)
                            restoreMessage = mContext.getString(R.string.error_restore);
                        else
                            restoreMessage = mContext.getString(R.string.message_backup_cloud_restored);

                        DBBasicNoteHelper.getInstance(mContext).openDB();

                        PreferenceRepository.displayMessage(mContext, restoreMessage);

                    } else
                        PreferenceRepository.displayMessage(mContext, mPreferenceFragment.getString(R.string.error_restore));
                }
            }

            @Override
            public void onPreExecute() {
                PreferenceRepository.updateDropboxRestorePreferenceSummary(mPreferenceFragment, PreferenceRepository.PREF_LOAD_LOADING);
            }
        });

        return mLoader;
    }

    public FileLoader getRestoreDropboxLoader() {
        if (mLoader == null)
            mLoader = createRestoreDropboxLoader();

        return mLoader;
    }
}
