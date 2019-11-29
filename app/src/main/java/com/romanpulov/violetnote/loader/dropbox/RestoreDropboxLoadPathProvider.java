package com.romanpulov.violetnote.loader.dropbox;

import android.content.Context;

import com.romanpulov.violetnote.db.DBStorageManager;
import com.romanpulov.library.common.loader.core.ContextLoadPathProvider;
import com.romanpulov.violetnote.loader.cloud.CloudLoaderRepository;

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
        return CloudLoaderRepository.REMOTE_PATH + DBStorageManager.getBackupZipFileName();
    }

    @Override
    public String getDestPath() {
        return getContext().getCacheDir() + File.separator + DBStorageManager.getBackupZipFileName();
    }
}
