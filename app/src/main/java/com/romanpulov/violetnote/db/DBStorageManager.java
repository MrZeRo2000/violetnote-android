package com.romanpulov.violetnote.db;

import android.content.Context;

import com.romanpulov.library.common.storage.BackupUtils;

import java.io.File;

/**
 * Facade for BackupUtils
 * Created by romanpulov on 19.12.2016.
 */

public class DBStorageManager {
    private static final String LOCAL_BACKUP_FOLDER_NAME = "VioletNoteBackup";
    private static final String LOCAL_BACKUP_FILE_NAME = "violetnotedb_" + DBBasicNoteOpenHelper.DATABASE_VERSION;

    private final BackupUtils mBackupUtils;

    public DBStorageManager(Context context) {
        mBackupUtils = new BackupUtils(context.getDatabasePath(DBBasicNoteOpenHelper.DATABASE_NAME).toString(), LOCAL_BACKUP_FOLDER_NAME, LOCAL_BACKUP_FILE_NAME);
    }

    /**
     * Returns list of local backup files
     * @return Files
     */
    public File[] getLocalBackupFiles() {
        return mBackupUtils.getLocalBackupFiles();
    }

    /**
     * Creates local backup with versions
     * @return Backup file if successful
     */
    public String createRollingLocalBackup() {
        return mBackupUtils.createRollingLocalBackup();
    }

    /**
     * Restores local backup
     * @return Restored file name if successful
     */
    public String restoreLocalBackup() {
        return mBackupUtils.restoreLocalBackup();
    }
}
