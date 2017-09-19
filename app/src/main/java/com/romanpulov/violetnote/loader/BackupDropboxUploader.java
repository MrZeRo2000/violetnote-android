package com.romanpulov.violetnote.loader;

import android.content.Context;

import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.WriteMode;
import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.db.DBStorageManager;
import com.romanpulov.violetnote.dropbox.DropBoxHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Backup to Dropbox loader
 * Created by romanpulov on 06.09.2017.
 */

public class BackupDropboxUploader extends AbstractLoader {
    private static final String BACKUP_PATH = "/AndroidBackup/";

    private final DbxClientV2 mClient;
    private final DropBoxHelper mDropBoxHelper;
    private final DBStorageManager mDBStorageManager;

    public BackupDropboxUploader(Context context) {
        super(context);
        mLoadAppearance = LOAD_APPEARANCE_ASYNC;
        mDropBoxHelper = DropBoxHelper.getInstance(context.getApplicationContext());
        mClient = mDropBoxHelper.getClient();
        mDBStorageManager = new DBStorageManager(context);
    }

    @Override
    protected void load() throws Exception {
        String accessToken = mDropBoxHelper.getAccessToken();
        if (accessToken == null)
            throw new Exception(mContext.getResources().getString(R.string.error_dropbox_auth));

        File[] files = mDBStorageManager.getLocalBackupFiles();
        for (File f : files) {
            String remoteFileName = f.getName();
            InputStream inputStream = new FileInputStream(f);
            try {
                mClient.files().uploadBuilder(BACKUP_PATH + remoteFileName).withMode(WriteMode.OVERWRITE).uploadAndFinish(inputStream);
            } finally {
                try {
                    inputStream.close();
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public boolean isInternetRequired() {
        return true;
    }
}
