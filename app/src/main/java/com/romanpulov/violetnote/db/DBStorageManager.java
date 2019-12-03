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
    private static final String BACKUP_FOLDER_NAME = "backup";
    private static final String BACKUP_FILE_NAME = "violetnotedb_" + DBBasicNoteOpenHelper.DATABASE_VERSION;

    private final BackupUtils mBackupUtils;
    private final String mBackupFolderName;
    private final Context mContext;

    private String initBackupFolderName(String backupFolderName) {
        String result;

        if (backupFolderName != null) {
            result =  backupFolderName;
        } else {
            File f;
            f = mContext.getExternalFilesDir(BACKUP_FOLDER_NAME);
            if (f == null) {
                f = new File(mContext.getFilesDir(), BACKUP_FOLDER_NAME);
            }

            result = f.getAbsolutePath();
        }

        return result;
    }

    public DBStorageManager(Context context, String backupFolderName) {
        mContext = context;
        mBackupFolderName = initBackupFolderName(backupFolderName);
        mBackupUtils = new BackupUtils(context.getDatabasePath(DBBasicNoteOpenHelper.DATABASE_NAME).toString(), mBackupFolderName, BACKUP_FILE_NAME);
    }

    public DBStorageManager(Context context) {
        this(context, null);
    }

    /**
     * Restores from backup
     * @param context Context
     * @param path Path with backup file
     * @return restore status
     */
    public static boolean restoreFromBackupPath(Context context, String path) {
        File file = new File(path);
        if (file.exists()) {
            DBStorageManager storageManager = new DBStorageManager(context, file.getParent());
            String restoreResult = storageManager.restoreLocalBackup();

            return  restoreResult != null;
        } else {
            return false;
        }
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
