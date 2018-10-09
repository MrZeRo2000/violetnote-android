package com.romanpulov.violetnote.loader.local;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

import com.romanpulov.library.common.loader.core.AbstractContextLoader;
import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.db.DBStorageManager;
import com.romanpulov.violetnote.view.preference.PreferenceRepository;

/** Local backup loader
 * Created by romanpulov on 15.12.2017.
 */

public class BackupLocalLoader extends AbstractContextLoader {

    public BackupLocalLoader(Context context) {
        super(context);
    }

    @Override
    public void load() throws Exception {
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            throw new Exception("Write permission for external folder are not granted");
        } else {

            DBStorageManager storageManager = new DBStorageManager(mContext);
            String backupResult = storageManager.createRollingLocalBackup();

            if (backupResult == null)
                throw new Exception(mContext.getString(R.string.error_backup));

            PreferenceRepository.setPreferenceKeyLastLoadedCurrentTime(mContext, PreferenceRepository.PREF_KEY_BASIC_NOTE_LOCAL_BACKUP);
        }
    }
}
