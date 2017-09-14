package com.romanpulov.violetnote.view.preference;

import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.db.DBStorageManager;

/**
 * Created by romanpulov on 14.09.2017.
 */

public class DropboxBackupPreferenceSetup extends PreferenceSetup {

    public DropboxBackupPreferenceSetup(PreferenceFragment preferenceFragment) {
        super(preferenceFragment, PreferenceRepository.PREF_KEY_BASIC_NOTE_CLOUD_BACKUP);
    }

    @Override
    public void execute() {
        mPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                DBStorageManager storageManager = new DBStorageManager(mActivity);
                String backupResult = storageManager.createRollingLocalBackup();

                if (backupResult == null)
                    PreferenceRepository.displayMessage(mContext, mContext.getString(R.string.error_backup));
                else {

                }

                return true;
            }
        });
    }
}
