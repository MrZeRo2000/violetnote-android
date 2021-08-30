package com.romanpulov.violetnote.loader.msgraph;

import android.content.Context;

import com.romanpulov.jutilscore.io.FileUtils;
import com.romanpulov.library.common.db.DBBackupManager;
import com.romanpulov.library.common.loader.core.AbstractContextLoader;
import com.romanpulov.library.msgraph.MSALPutBytesByPathAction;
import com.romanpulov.library.msgraph.MSActionExecutor;
import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.db.DBStorageManager;
import com.romanpulov.violetnote.loader.cloud.CloudLoaderRepository;
import com.romanpulov.violetnote.loader.helper.LoaderNotificationHelper;
import com.romanpulov.violetnote.view.preference.PreferenceRepository;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;

import static com.romanpulov.violetnote.common.NotificationRepository.NOTIFICATION_ID_LOADER;

public class BackupMSGraphUploader extends AbstractContextLoader {
    public BackupMSGraphUploader(Context context) {
        super(context);
    }

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
                MSActionExecutor.executeSync(
                        new MSALPutBytesByPathAction(
                                mContext,
                                "/" + CloudLoaderRepository.REMOTE_PATH + "/" + fileName,
                                outputStream.toByteArray(),
                                null
                        )
                );
            }
        }

        //log("Execution completed");

        PreferenceRepository.setPreferenceKeyLastLoadedCurrentTime(mContext, PreferenceRepository.PREF_KEY_BASIC_NOTE_CLOUD_BACKUP);
        LoaderNotificationHelper.notify(mContext, mContext.getString(R.string.notification_onedrive_backup_completed), NOTIFICATION_ID_LOADER,
                LoaderNotificationHelper.NOTIFICATION_TYPE_SUCCESS);

    }
}
