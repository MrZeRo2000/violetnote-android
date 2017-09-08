package com.romanpulov.violetnote.view.preference;

import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.db.DBStorageManager;

import java.util.Locale;

/**
 * Local backup configuration
 * Created by romanpulov on 08.09.2017.
 */

public class LocalBackupPreferenceSetup extends PreferenceSetup {

    public LocalBackupPreferenceSetup(PreferenceFragment preferenceFragment) {
        super(preferenceFragment, PreferenceRepository.PREF_KEY_BASIC_NOTE_LOCAL_BACKUP);
    }

    @Override
    public void execute() {
        mPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                DBStorageManager storageManager = new DBStorageManager(mActivity);
                String backupResult = storageManager.createRollingLocalBackup();

                String backupMessage;
                if (backupResult == null)
                    backupMessage = mContext.getString(R.string.error_backup);
                else
                    backupMessage = String.format(Locale.getDefault(), mContext.getString(R.string.message_backup_created), backupResult);

                PreferenceRepository.displayMessage(mContext, backupMessage);

                return true;
            }
        });
    }
}
