package com.romanpulov.violetnote.loader.gdrive;

import static com.romanpulov.violetnote.common.NotificationRepository.NOTIFICATION_ID_LOADER;

import android.content.Context;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.loader.helper.LoaderNotificationHelper;

public class BackupGDriveUploader extends AbstractBackupGDriveUploader {
    public BackupGDriveUploader(Context context) {
        super(context);
    }

    @Override
    protected void afterLoad() {
        LoaderNotificationHelper.notify(mContext, mContext.getString(R.string.notification_gdrive_backup_completed), NOTIFICATION_ID_LOADER,
                LoaderNotificationHelper.NOTIFICATION_TYPE_SUCCESS);
    }
}
