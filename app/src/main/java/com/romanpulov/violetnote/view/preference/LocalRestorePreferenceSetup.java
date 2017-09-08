package com.romanpulov.violetnote.view.preference;

import android.content.DialogInterface;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.db.DBBasicNoteHelper;
import com.romanpulov.violetnote.db.DBStorageManager;

import java.util.Locale;

/**
 * Local restore configuration
 * Created by romanpulov on 08.09.2017.
 */

public class LocalRestorePreferenceSetup extends PreferenceSetup {

    public LocalRestorePreferenceSetup(PreferenceFragment preferenceFragment) {
        super(preferenceFragment, PreferenceRepository.PREF_KEY_BASIC_NOTE_LOCAL_RESTORE);
    }

    @Override
    public void execute() {
        mPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                final AlertDialog.Builder alert = new AlertDialog.Builder(mActivity, R.style.AlertDialogTheme);
                alert
                        .setTitle(R.string.ui_question_are_you_sure)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DBBasicNoteHelper.getInstance(mActivity).closeDB();

                                DBStorageManager storageManager = new DBStorageManager(mActivity);
                                String restoreResult = storageManager.restoreLocalBackup();

                                String restoreMessage;
                                if (restoreResult == null)
                                    restoreMessage = mContext.getString(R.string.error_restore);
                                else
                                    restoreMessage = String.format(Locale.getDefault(), mContext.getString(R.string.message_backup_restored), restoreResult);

                                DBBasicNoteHelper.getInstance(mActivity).openDB();

                                PreferenceRepository.displayMessage(mContext, restoreMessage);
                            }
                        })
                        .setNegativeButton(R.string.cancel, null)
                        .show();

                return true;
            }
        });
    }
}
