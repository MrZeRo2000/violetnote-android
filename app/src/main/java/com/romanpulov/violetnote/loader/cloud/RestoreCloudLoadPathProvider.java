package com.romanpulov.violetnote.loader.cloud;

import android.content.Context;

import com.romanpulov.library.common.loader.core.ContextLoadPathProvider;
import com.romanpulov.violetnote.db.DBStorageManager;

import java.io.File;

/**
 * Load path provider for Cloud restore
 * Created by romanpulov on 22.09.2017.
 */

public class RestoreCloudLoadPathProvider extends ContextLoadPathProvider {


    public RestoreCloudLoadPathProvider(Context context) {
        super(context);
    }

    @Override
    public String getSourcePath() {
        return File.separator + CloudLoaderRepository.REMOTE_PATH + File.separator + DBStorageManager.getBackupZipFileName();
    }

    @Override
    public String getDestPath() {
        return getContext().getCacheDir() + File.separator + DBStorageManager.getBackupZipFileName();
    }
}
