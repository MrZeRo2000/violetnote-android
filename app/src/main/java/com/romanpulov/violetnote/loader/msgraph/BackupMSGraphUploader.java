package com.romanpulov.violetnote.loader.msgraph;

import android.content.Context;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.loader.helper.LoaderNotificationHelper;

import static com.romanpulov.violetnote.common.NotificationRepository.NOTIFICATION_ID_LOADER;

public class BackupMSGraphUploader extends AbstractBackupMSGraphUploader {
    public BackupMSGraphUploader(Context context) {
        super(context);
    }

    @Override
    protected void afterLoad() {
        LoaderNotificationHelper.notify(mContext, mContext.getString(R.string.notification_onedrive_backup_completed), NOTIFICATION_ID_LOADER,
                LoaderNotificationHelper.NOTIFICATION_TYPE_SUCCESS);
    }
}
