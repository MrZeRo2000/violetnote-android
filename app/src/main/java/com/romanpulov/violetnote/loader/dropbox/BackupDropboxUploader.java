package com.romanpulov.violetnote.loader.dropbox;

import android.content.Context;

import com.dropbox.core.v2.DbxClientV2;
import com.romanpulov.library.common.db.DBBackupManager;
import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.db.DBStorageManager;
import com.romanpulov.library.dropbox.DropboxHelper;
import com.romanpulov.library.common.loader.core.AbstractContextLoader;
import com.romanpulov.violetnote.loader.cloud.CloudLoaderRepository;
import com.romanpulov.violetnote.loader.helper.LoaderNotificationHelper;
import com.romanpulov.violetnote.view.preference.PreferenceRepository;

import java.io.InputStream;

import static com.romanpulov.violetnote.common.NotificationRepository.NOTIFICATION_ID_LOADER;

/**
 * Loader to backup to Dropbox
 * Created by romanpulov on 06.09.2017.
 */

public class BackupDropboxUploader extends AbstractContextLoader {

    private final DropboxHelper mDropboxHelper;

    public BackupDropboxUploader(Context context) {
        super(context);
        mDropboxHelper = DropboxHelper.getInstance(context.getApplicationContext());
    }

    @Override
    public void load() throws Exception {
        String accessToken = mDropboxHelper.getAccessToken();
        if (accessToken == null)
            throw new Exception(mContext.getResources().getString(R.string.error_dropbox_auth));

        mDropboxHelper.initClient();

        final DBBackupManager backupManager = DBStorageManager.getDBBackupManager(mContext);

        for (String backupFileName: backupManager.getDatabaseBackupFiles()) {
            try (InputStream inputStream = backupManager.createBackupInputStream(backupFileName)) {
                mDropboxHelper.putStream(inputStream, CloudLoaderRepository.REMOTE_PATH + backupFileName);
            }
        }

        PreferenceRepository.setPreferenceKeyLastLoadedCurrentTime(mContext, PreferenceRepository.PREF_KEY_BASIC_NOTE_CLOUD_BACKUP);
        LoaderNotificationHelper.notify(mContext, mContext.getString(R.string.notification_dropbox_backup_completed), NOTIFICATION_ID_LOADER,
                LoaderNotificationHelper.NOTIFICATION_TYPE_SUCCESS);
    }
}
