package com.romanpulov.violetnote.loader.dropbox;

import android.content.Context;

import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.WriteMode;
import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.db.DBStorageManager;
import com.romanpulov.library.dropbox.DropboxHelper;
import com.romanpulov.library.common.loader.core.AbstractContextLoader;
import com.romanpulov.violetnote.loader.helper.LoaderNotificationHelper;
import com.romanpulov.violetnote.view.preference.PreferenceRepository;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import static com.romanpulov.violetnote.common.NotificationRepository.NOTIFICATION_ID_LOADER;

/**
 * Loader to backup to Dropbox
 * Created by romanpulov on 06.09.2017.
 */

public class BackupDropboxUploader extends AbstractContextLoader {

    private final DropboxHelper mDropboxHelper;
    private final DBStorageManager mDBStorageManager;

    public BackupDropboxUploader(Context context) {
        super(context);
        mDropboxHelper = DropboxHelper.getInstance(context.getApplicationContext());

        mDBStorageManager = new DBStorageManager(context);
    }

    @Override
    public void load() throws Exception {
        String accessToken = mDropboxHelper.getAccessToken();
        if (accessToken == null)
            throw new Exception(mContext.getResources().getString(R.string.error_dropbox_auth));

        DbxClientV2 client = mDropboxHelper.getClient();

        File[] files = mDBStorageManager.getLocalBackupFiles();
        for (File f : files) {
            String remoteFileName = f.getName();
            InputStream inputStream = new FileInputStream(f);
            try {
                client.files().uploadBuilder(DropboxLoaderRepository.REMOTE_PATH + remoteFileName).withMode(WriteMode.OVERWRITE).uploadAndFinish(inputStream);
            } finally {
                try {
                    inputStream.close();
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
        }

        PreferenceRepository.setPreferenceKeyLastLoadedCurrentTime(mContext, PreferenceRepository.PREF_KEY_BASIC_NOTE_CLOUD_BACKUP);
        LoaderNotificationHelper.notify(mContext, mContext.getString(R.string.notification_dropbox_backup_completed), NOTIFICATION_ID_LOADER,
                LoaderNotificationHelper.NOTIFICATION_TYPE_SUCCESS);
    }
}
