package com.romanpulov.violetnote.loader.onedrive;

import android.content.Context;

import com.romanpulov.library.common.loader.core.AbstractContextLoader;
import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.db.DBStorageManager;
import com.romanpulov.violetnote.loader.helper.LoaderNotificationHelper;
import com.romanpulov.violetnote.onedrive.OneDriveHelper;
import com.romanpulov.violetnote.view.preference.PreferenceRepository;

import java.io.File;

import static com.romanpulov.violetnote.common.NotificationRepository.NOTIFICATION_ID_LOADER;

public class BackupOneDriveUploader extends AbstractContextLoader {
    private final OneDriveHelper mOneDriveHelper;
    private final DBStorageManager mDBStorageManager;

    public BackupOneDriveUploader(Context context) {
        super(context);
        mOneDriveHelper = OneDriveHelper.getInstance();
        mDBStorageManager = new DBStorageManager(context);
    }

    @Override
    public void load() throws Exception {
        File[] files = mDBStorageManager.getLocalBackupFiles();

        PreferenceRepository.setPreferenceKeyLastLoadedCurrentTime(mContext, PreferenceRepository.PREF_KEY_BASIC_NOTE_CLOUD_BACKUP);
        LoaderNotificationHelper.notify(mContext, mContext.getString(R.string.notification_onedrive_backup_completed), NOTIFICATION_ID_LOADER,
                LoaderNotificationHelper.NOTIFICATION_TYPE_SUCCESS);

    }
}
