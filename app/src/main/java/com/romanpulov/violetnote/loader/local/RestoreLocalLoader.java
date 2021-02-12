package com.romanpulov.violetnote.loader.local;

import android.content.Context;

import com.romanpulov.library.common.loader.core.AbstractContextLoader;
import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.db.DBStorageManager;
import com.romanpulov.violetnote.view.preference.PreferenceRepository;

/**
 * Local restore loader
 * Created by romanpulov on 15.12.2017.
 */
public class RestoreLocalLoader extends AbstractContextLoader {

    public RestoreLocalLoader(Context context) {
        super(context);
    }

    @Override
    public void load() throws Exception {
        String restoreResult = DBStorageManager.getDBBackupManager(mContext).restoreLocalBackup();

        if (restoreResult == null)
            throw new Exception(mContext.getString(R.string.error_restore));

        PreferenceRepository.setPreferenceKeyLastLoadedCurrentTime(mContext, PreferenceRepository.PREF_KEY_BASIC_NOTE_LOCAL_RESTORE);
    }
}
