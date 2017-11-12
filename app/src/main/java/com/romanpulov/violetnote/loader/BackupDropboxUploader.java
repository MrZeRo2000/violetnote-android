package com.romanpulov.violetnote.loader;

import android.content.Context;
import android.preference.PreferenceManager;

import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.WriteMode;
import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.db.DBStorageManager;
import com.romanpulov.violetnote.dropbox.DropBoxHelper;
import com.romanpulov.violetnote.view.preference.PreferenceRepository;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Loader to backup to Dropbox
 * Created by romanpulov on 06.09.2017.
 */

public class BackupDropboxUploader extends AbstractLoader {

    private final DbxClientV2 mClient;
    private final DropBoxHelper mDropBoxHelper;
    private final DBStorageManager mDBStorageManager;

    public BackupDropboxUploader(Context context) {
        super(context);
        mDropBoxHelper = DropBoxHelper.getInstance(context.getApplicationContext());
        mClient = mDropBoxHelper.getClient();
        mDBStorageManager = new DBStorageManager(context);
    }

    @Override
    public void load() throws Exception {
        String accessToken = mDropBoxHelper.getAccessToken();
        if (accessToken == null)
            throw new Exception(mContext.getResources().getString(R.string.error_dropbox_auth));

        File[] files = mDBStorageManager.getLocalBackupFiles();
        for (File f : files) {
            String remoteFileName = f.getName();
            InputStream inputStream = new FileInputStream(f);
            try {
                mClient.files().uploadBuilder(DropboxLoaderRepository.REMOTE_PATH + remoteFileName).withMode(WriteMode.OVERWRITE).uploadAndFinish(inputStream);
            } finally {
                try {
                    inputStream.close();
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
        }

        PreferenceRepository.setCloudBackupLastLoadedCurrentTime(mContext);
    }
}
