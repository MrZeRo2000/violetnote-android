package com.romanpulov.violetnote.db;

import android.content.Context;

import com.romanpulov.library.common.storage.BackupUtils;

/**
 * Facade for BackupUtils
 * Created by romanpulov on 19.12.2016.
 */

public class DBStorageManager {
    private static final String LOCAL_BACKUP_FOLDER_NAME = "VioletNoteBackup";
    private static final String LOCAL_BACKUP_FILE_NAME = "violetnotedb_" + DBBasicNoteOpenHelper.DATABASE_VERSION;

    private final BackupUtils mBackupUtils;

    public DBStorageManager(Context context) {
        mBackupUtils = new BackupUtils(context, DBBasicNoteOpenHelper.DATABASE_NAME, LOCAL_BACKUP_FOLDER_NAME, LOCAL_BACKUP_FILE_NAME);
    }

    public String createRollingLocalBackup() {
        return mBackupUtils.createRollingLocalBackup();
    }

    public String restoreLocalBackup() {
        return mBackupUtils.restoreLocalBackup();
    }
}
