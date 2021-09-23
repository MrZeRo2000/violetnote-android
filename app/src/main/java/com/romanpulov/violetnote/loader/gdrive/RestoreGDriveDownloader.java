package com.romanpulov.violetnote.loader.gdrive;

import static com.romanpulov.violetnote.common.NotificationRepository.NOTIFICATION_ID_LOADER;

import android.content.Context;

import com.romanpulov.jutilscore.io.FileUtils;
import com.romanpulov.library.common.loader.file.FileLoader;
import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.cloud.GDHelper;
import com.romanpulov.violetnote.db.DBStorageManager;
import com.romanpulov.violetnote.loader.cloud.RestoreCloudLoadPathProvider;
import com.romanpulov.violetnote.loader.helper.LoaderNotificationHelper;
import com.romanpulov.violetnote.view.preference.PreferenceRepository;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class RestoreGDriveDownloader extends FileLoader {
    public RestoreGDriveDownloader(Context context) {
        super(context, new RestoreCloudLoadPathProvider(context));
    }

    @Override
    public void load() throws Exception {
        // get data from cloud
        byte[] bytes = GDHelper.getInstance().getBytesByPath(mContext, getLoadPathProvider().getSourcePath());

        // copy file to dest
        File destFile = new File(getLoadPathProvider().getDestPath());
        try (
                InputStream inputStream = new ByteArrayInputStream(bytes);
                OutputStream outputStream = new FileOutputStream(destFile)
        ) {
            FileUtils.copyStream(inputStream, outputStream);
        }

        boolean isRestoreSuccess = DBStorageManager.getDBBackupManager(mContext).restoreFromBackupPath(getLoadPathProvider().getDestPath());

        String restoreMessage = mContext.getString(isRestoreSuccess ? R.string.message_backup_cloud_restored : R.string.error_restore);

        PreferenceRepository.setPreferenceKeyLastLoadedCurrentTime(mContext, PreferenceRepository.PREF_KEY_BASIC_NOTE_CLOUD_RESTORE);
        LoaderNotificationHelper.notify(mContext, restoreMessage, NOTIFICATION_ID_LOADER,
                isRestoreSuccess ? LoaderNotificationHelper.NOTIFICATION_TYPE_SUCCESS : LoaderNotificationHelper.NOTIFICATION_TYPE_FAILURE);
    }
}
