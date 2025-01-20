package com.romanpulov.violetnote.loader.msgraph;

import android.content.Context;

import com.romanpulov.jutilscore.io.FileUtils;
import com.romanpulov.library.common.db.DBBackupManager;
import com.romanpulov.library.common.loader.core.AbstractContextLoader;
import com.romanpulov.violetnote.cloud.MSGraphHelper;
import com.romanpulov.violetnote.db.DBStorageManager;
import com.romanpulov.violetnote.loader.cloud.CloudLoaderRepository;
import com.romanpulov.violetnote.view.preference.PreferenceRepository;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;

public class AbstractBackupMSGraphUploader extends AbstractContextLoader {
    public AbstractBackupMSGraphUploader(Context context) {
        super(context);
    }

    protected void afterLoad() {}

    @Override
    public void load() throws Exception {
        final DBBackupManager backupManager =  DBStorageManager.getDBBackupManager(mContext);

        final List<String> fileNames = backupManager.getDatabaseBackupFiles();

        // log("Got backup files:" + fileNames);
        for (String fileName : fileNames) {
            // log("Putting file:" + fileName);
            try (
                    InputStream inputStream = backupManager.createBackupInputStream(fileName);
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream()
            ) {
                FileUtils.copyStream(inputStream, outputStream);
                MSGraphHelper.getInstance().putBytesByPath(
                        mContext,
                        "/" + CloudLoaderRepository.REMOTE_PATH + "/" + fileName,
                        outputStream.toByteArray());
            }
        }

        PreferenceRepository.setPreferenceKeyLastLoadedCurrentTime(mContext, PreferenceRepository.PREF_KEY_BASIC_NOTE_CLOUD_BACKUP);

        afterLoad();
    }
}
