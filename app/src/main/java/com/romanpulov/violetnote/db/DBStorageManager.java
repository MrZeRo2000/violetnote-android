package com.romanpulov.violetnote.db;

import android.content.Context;

import com.romanpulov.jutilscore.io.ZipFileUtils;
import com.romanpulov.library.common.db.DBBackupManager;

/**
 * Facade for BackupUtils
 * Created by romanpulov on 19.12.2016.
 */

public final class DBStorageManager {
    private static DBStorageManager mInstance = null;

    private static final String LOCAL_BACKUP_FOLDER_NAME = "VioletNoteBackup";
    private static final String LOCAL_BACKUP_FILE_NAME = "violetnotedb_" + DBBasicNoteOpenHelper.DATABASE_VERSION;

    public static DBStorageManager getInstance(Context context) {
        if (null == mInstance) {
            mInstance = new DBStorageManager(context);
        }
        return mInstance;
    }

    private final DBBackupManager mDBBackupManager;

    private DBStorageManager(Context context) {
        mDBBackupManager = new DBBackupManager(
                context,
                LOCAL_BACKUP_FOLDER_NAME,
                LOCAL_BACKUP_FILE_NAME,
                DBBasicNoteHelper.getInstance(context)
        );
    }

    public DBBackupManager getDBBackupManager() {
        return mDBBackupManager;
    }

    public static DBBackupManager getDBBackupManager(Context context) {
        return getInstance(context).getDBBackupManager();
    }

    /**
     * Returns local backup Zip file name
     * @return Zip file name
     */
    public static String getBackupZipFileName() {
        return ZipFileUtils.getZipFileName(LOCAL_BACKUP_FILE_NAME);
    }
}
