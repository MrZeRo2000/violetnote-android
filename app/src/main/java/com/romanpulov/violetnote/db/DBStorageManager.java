package com.romanpulov.violetnote.db;

import android.content.Context;
import android.os.Environment;

import com.romanpulov.violetnote.helper.FileHelper;
import com.romanpulov.violetnote.helper.ZIPFileHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Backup and restore for BasicNote database
 * Created by romanpulov on 29.09.2016.
 */

public class DBStorageManager {
    private static final String LOCAL_BACKUP_FOLDER_NAME = "VioletNoteBackup";
    private static final String LOCAL_BACKUP_FILE_NAME = "violetnotedb";

    private final Context mContext;

    public static String getLocalBackupFolderName() {
        return Environment.getExternalStorageDirectory().toString() + "/" + LOCAL_BACKUP_FOLDER_NAME + "/";
    }

    public static String getLocalBackupFileName() {
        return DBStorageManager.getLocalBackupFolderName() + LOCAL_BACKUP_FILE_NAME;
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
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = new FileInputStream(getDatabasePath());
            outputStream = new FileOutputStream(getLocalBackupFileName());

            //write
            byte[] buffer = new byte[1024];
            int len;
            while ((len = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, len);
            }

        } catch (IOException e) {
            return null;
        }
        finally {
            //input stream
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            //output stream
            if (outputStream != null) {
                try {
                    outputStream.flush();
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        //archive file
        return ZIPFileHelper.zipFile(DBStorageManager.getLocalBackupFolderName(), LOCAL_BACKUP_FILE_NAME);
    }

    public String createRollingLocalBackup() {
        //get file names
        String fileName = DBStorageManager.getLocalBackupFolderName() + LOCAL_BACKUP_FILE_NAME;
        String zipFileName = ZIPFileHelper.getZipFileName(DBStorageManager.getLocalBackupFolderName() + LOCAL_BACKUP_FILE_NAME);

        //roll copies of data
        if (!FileHelper.renameCopies(zipFileName))
            return null;

        //create backup
        String result = createLocalBackup();

        //delete non zipped file, ignore any errors
        if (result != null)
            FileHelper.delete(fileName);

        return result;
    }
}
