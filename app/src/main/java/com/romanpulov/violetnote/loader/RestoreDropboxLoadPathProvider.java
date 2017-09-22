package com.romanpulov.violetnote.loader;

import android.content.Context;

import com.romanpulov.violetnote.db.DBStorageManager;

import java.io.File;

/**
 * Load path provider for Dropbox restore
 * Created by romanpulov on 22.09.2017.
 */

public class RestoreDropboxLoadPathProvider extends ContextLoadPathProvider {


    public RestoreDropboxLoadPathProvider(Context context) {
        super(context);
    }

    @Override
    public String getSourcePath() {
        return DropboxLoaderRepository.REMOTE_PATH + DBStorageManager.getBackupZipFileName();
    }

    @Override
    public String getDestPath() {
        return getContext().getCacheDir() + File.separator + DBStorageManager.getBackupZipFileName();
    }
}
