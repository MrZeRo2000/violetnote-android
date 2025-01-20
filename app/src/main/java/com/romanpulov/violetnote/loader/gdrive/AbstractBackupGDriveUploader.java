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

public abstract class AbstractBackupGDriveUploader  extends AbstractContextLoader {
    public AbstractBackupGDriveUploader(Context context) {
        super(context);
    }

    protected void beforeLoad() throws GDActionException {}

    protected void afterLoad() {}

    @Override
    public void load() throws Exception {
        beforeLoad();

        final DBBackupManager backupManager =  DBStorageManager.getDBBackupManager(mContext);

        final List<String> fileNames = backupManager.getDatabaseBackupFiles();

        for (String fileName : fileNames) {
            try (
                    InputStream inputStream = backupManager.createBackupInputStream(fileName);
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream()
            ) {
                FileUtils.copyStream(inputStream, outputStream);
                GDHelper.getInstance().putBytesByPath(mContext, CloudLoaderRepository.REMOTE_PATH + File.separator + fileName, outputStream.toByteArray());
            }
        }

        PreferenceRepository.setPreferenceKeyLastLoadedCurrentTime(mContext, PreferenceRepository.PREF_KEY_BASIC_NOTE_CLOUD_BACKUP);

        afterLoad();
    }
}