package com.romanpulov.violetnote.loader.onedrive;

import android.content.Context;

import com.onedrive.sdk.core.ClientException;
import com.onedrive.sdk.extensions.Item;
import com.romanpulov.library.common.db.DBBackupManager;
import com.romanpulov.library.common.loader.core.AbstractContextLoader;
import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.db.DBStorageManager;
import com.romanpulov.violetnote.loader.cloud.CloudLoaderRepository;
import com.romanpulov.violetnote.loader.helper.LoaderNotificationHelper;
import com.romanpulov.library.onedrive.OneDriveHelper;
import com.romanpulov.violetnote.view.preference.PreferenceRepository;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

import static com.romanpulov.violetnote.common.NotificationRepository.NOTIFICATION_ID_LOADER;

public class BackupOneDriveUploader extends AbstractContextLoader {
    private final OneDriveHelper mOneDriveHelper;
    private final CountDownLatch mLocker = new CountDownLatch(1);

    public BackupOneDriveUploader(Context context) {
        super(context);
        mOneDriveHelper = OneDriveHelper.getInstance();
    }

    @Override
    public void load() throws Exception {
        final DBBackupManager backupManager =  DBStorageManager.getDBBackupManager(mContext);

        final List<String> fileNames = backupManager.getDatabaseBackupFiles();

        final AtomicReference<Exception> mException = new AtomicReference<>();
        final AtomicReference<Integer> mFileCounter = new AtomicReference<>();
        mFileCounter.set(fileNames.size());

        mOneDriveHelper.setOnOneDriveItemListener(new OneDriveHelper.OnOneDriveItemListener() {
            @Override
            public void onItemSuccess(Item item) {
                mOneDriveHelper.setOnOneDriveItemListener(new OneDriveHelper.OnOneDriveItemListener() {
                    @Override
                    public void onItemSuccess(Item item) {
                        mFileCounter.set(mFileCounter.get() - 1);
                        if (mFileCounter.get() == 0) {
                            mLocker.countDown();
                        }
                    }

                    @Override
                    public void onItemFailure(ClientException ex) {
                        mException.set(ex);
                        mLocker.countDown();
                    }
                });

                try {
                    for (String fileName: fileNames) {
                        try (InputStream inputStream = backupManager.createBackupInputStream(fileName)) {
                            mOneDriveHelper.putStream(inputStream, CloudLoaderRepository.REMOTE_PATH, fileName);
                        }
                    }
                } catch (IOException e) {
                    mException.set(e);
                    mLocker.countDown();
                }
            }

            @Override
            public void onItemFailure(ClientException ex) {
                mException.set(ex);
                mLocker.countDown();
            }
        });

        mOneDriveHelper.createFolder(CloudLoaderRepository.REMOTE_PATH);
        mLocker.await();

        if (mException.get() != null) {
            throw mException.get();
        }

        PreferenceRepository.setPreferenceKeyLastLoadedCurrentTime(mContext, PreferenceRepository.PREF_KEY_BASIC_NOTE_CLOUD_BACKUP);
        LoaderNotificationHelper.notify(mContext, mContext.getString(R.string.notification_onedrive_backup_completed), NOTIFICATION_ID_LOADER,
                LoaderNotificationHelper.NOTIFICATION_TYPE_SUCCESS);

    }
}
