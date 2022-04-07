package com.romanpulov.violetnote.loader.gdrive;

import android.content.Context;

import com.romanpulov.jutilscore.io.FileUtils;
import com.romanpulov.library.common.db.DBBackupManager;
import com.romanpulov.library.common.loader.core.AbstractContextLoader;
import com.romanpulov.library.gdrive.GDActionException;
import com.romanpulov.violetnote.cloud.GDHelper;
import com.romanpulov.violetnote.db.DBStorageManager;
import com.romanpulov.violetnote.loader.cloud.CloudLoaderRepository;
import com.romanpulov.violetnote.view.preference.PreferenceRepository;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.List;

public class SilentBackupGDriveUploader extends AbstractBackupGDriveUploader {
    public SilentBackupGDriveUploader(Context context) {
        super(context);
    }

    @Override
    protected void beforeLoad() throws GDActionException {
        GDHelper.getInstance().silentLogin(mContext);
    }
}
