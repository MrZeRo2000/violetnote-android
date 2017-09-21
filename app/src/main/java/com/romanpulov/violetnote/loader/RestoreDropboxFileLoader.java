package com.romanpulov.violetnote.loader;

import android.content.Context;

import com.romanpulov.violetnote.db.DBStorageManager;

import java.io.File;

/**
 * Restore from Dropbox file loader
 * Created by romanpulov on 20.09.2017.
 */

public class RestoreDropboxFileLoader extends DropboxFileLoader {
    private static final String RESTORE_PATH = "/AndroidBackup/";

    public RestoreDropboxFileLoader(Context context) {
        super(context);
    }

    @Override
    public String getSourcePath() {
        return RESTORE_PATH + DBStorageManager.getBackupZipFileName();
    }

    @Override
    public String getDestPath() {
        return mContext.getCacheDir() + File.separator + DBStorageManager.getBackupZipFileName();
    }
}
