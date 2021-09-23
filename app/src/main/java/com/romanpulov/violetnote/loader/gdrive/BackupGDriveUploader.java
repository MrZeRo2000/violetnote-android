package com.romanpulov.violetnote.loader.gdrive;

import static com.romanpulov.violetnote.common.NotificationRepository.NOTIFICATION_ID_LOADER;

import android.content.Context;

import com.romanpulov.jutilscore.io.FileUtils;
import com.romanpulov.library.common.db.DBBackupManager;
import com.romanpulov.library.common.loader.core.AbstractContextLoader;
import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.cloud.GDHelper;
import com.romanpulov.violetnote.db.DBStorageManager;
import com.romanpulov.violetnote.loader.cloud.CloudLoaderRepository;
import com.romanpulov.violetnote.loader.helper.LoaderNotificationHelper;
import com.romanpulov.violetnote.view.preference.PreferenceRepository;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.List;

public class BackupGDriveUploader extends AbstractContextLoader {
    public BackupGDriveUploader(Context context) {
        super(context);
    }

    @Override
    public void load() throws Exception {
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
        LoaderNotificationHelper.notify(mContext, mContext.getString(R.string.notification_gdrive_backup_completed), NOTIFICATION_ID_LOADER,
                LoaderNotificationHelper.NOTIFICATION_TYPE_SUCCESS);

    }
}
