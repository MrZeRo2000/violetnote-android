package com.romanpulov.violetnote.db;

import android.content.Context;
import android.os.Environment;

import com.romanpulov.library.common.io.ZipFileUtils;
import com.romanpulov.library.common.storage.BackupUtils;

import java.io.File;

/**
 * Facade for BackupUtils
 * Created by romanpulov on 19.12.2016.
 */

public final class DBStorageManager {
    private static final String LOCAL_BACKUP_FOLDER_NAME = "VioletNoteBackup";
    private static final String BACKUP_FILE_NAME = "violetnotedb_" + DBBasicNoteOpenHelper.DATABASE_VERSION;

    private final BackupUtils mBackupUtils;
    private final String mBackupFolderName;
    private final Context mContext;

    public DBStorageManager(Context context, String backupFolderName) {
        mContext = context;
        mBackupFolderName = backupFolderName;
        mBackupUtils = new BackupUtils(context.getDatabasePath(DBBasicNoteOpenHelper.DATABASE_NAME).toString(), backupFolderName, BACKUP_FILE_NAME);
    }

    public DBStorageManager(Context context) {
        this(context, Environment.getExternalStorageDirectory() + File.separator + LOCAL_BACKUP_FOLDER_NAME);
    }

    /**
     * Returns list of local backup files
     * @return Files
     */
    public File[] getLocalBackupFiles() {
        return mBackupUtils.getBackupFiles();
    }

    /**
     * Creates local backup with versions
     * @return Backup file if successful
     */
    public String createRollingLocalBackup() {
        DBBasicNoteHelper.getInstance(mContext).closeDB();
        String result = mBackupUtils.createRollingLocalBackup();
        DBBasicNoteHelper.getInstance(mContext).openDB();

        return result;
    }

    /**
     * Restores local backup
     * @return Restored file name if successful
     */
    public String restoreLocalBackup() {
        DBBasicNoteHelper.getInstance(mContext).closeDB();
        String result = mBackupUtils.restoreBackup();
        DBBasicNoteHelper.getInstance(mContext).openDB();

        return result;
    }

    /**
     * Returns local backup Zip file name
     * @return Zip file name
     */
    public static String getBackupZipFileName() {
        return ZipFileUtils.getZipFileName(BACKUP_FILE_NAME);
    }

    /**
     * Returns backup folder name
     * @return backup folder name
     */
    public String getBackupFolderName() {
        return mBackupFolderName;
    }
}
