package com.romanpulov.violetnote.db;

import android.content.Context;
import android.os.Environment;

import com.romanpulov.violetnote.helper.FileHelper;
import com.romanpulov.violetnote.helper.ZIPFileHelper;

import java.io.File;

/**
 * Backup and restore for BasicNote database
 * Created by romanpulov on 29.09.2016.
 */

public class DBStorageManager {
    private static final String LOCAL_BACKUP_FOLDER_NAME = "VioletNoteBackup";
    private static final String LOCAL_BACKUP_FILE_NAME = "violetnotedb_" + DBBasicNoteOpenHelper.DATABASE_VERSION;

    private final Context mContext;

    public static String getLocalBackupFolderName() {
        return Environment.getExternalStorageDirectory().toString() + "/" + LOCAL_BACKUP_FOLDER_NAME + "/";
    }

    public static String getLocalBackupFileName() {
        return DBStorageManager.getLocalBackupFolderName() + LOCAL_BACKUP_FILE_NAME;
    }

    public static String getLocalBackupZIPFileName() {
        return ZIPFileHelper.getZipFileName(getLocalBackupFileName());
    }

    public DBStorageManager(Context context) {
        mContext = context;
    }

    private String getDatabasePath() {
        return mContext.getDatabasePath(DBBasicNoteOpenHelper.DATABASE_NAME).toString();
    }

    public String createLocalBackup() {
        //init backup folder
        File backupFolder = new File(DBStorageManager.getLocalBackupFolderName());
        if (!backupFolder.exists()) {
            if (!backupFolder.mkdir()) {
                return null;
            }
        }

        //write file
        if (!FileHelper.copy(getDatabasePath(), getLocalBackupFileName()))
            return null;

        //archive file
        return ZIPFileHelper.zipFile(DBStorageManager.getLocalBackupFolderName(), LOCAL_BACKUP_FILE_NAME);
    }

    public String restoreLocalBackup() {
        String localBackupFileName = getLocalBackupFileName();

        //check backup availability
        File zipFile = new File(getLocalBackupZIPFileName());
        if (!zipFile.exists())
            return null;

        //extract backup
        if (!ZIPFileHelper.unZipFile(DBStorageManager.getLocalBackupFolderName(), ZIPFileHelper.getZipFileName(LOCAL_BACKUP_FILE_NAME)))
            return null;

        //check restored file availability
        File file = new File(localBackupFileName);
        if (!file.exists())
            return null;

        //replace database file
        if (!FileHelper.copy(localBackupFileName, getDatabasePath()))
            return null;

        //delete and ignore any errors
        file.delete();

        return localBackupFileName;
    }

    public String createRollingLocalBackup() {
        //get file names
        String fileName = getLocalBackupFileName();
        String zipFileName = getLocalBackupZIPFileName();

        //roll copies of data: first try rename, then copy
        if (!FileHelper.renameCopies(zipFileName))
            if ((!FileHelper.saveCopies(zipFileName)))
                return null;

        //create backup
        String result = createLocalBackup();

        //delete non zipped file, ignore any errors
        if (result != null)
            FileHelper.delete(fileName);

        return result;
    }
}
