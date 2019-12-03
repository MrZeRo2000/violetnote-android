package com.romanpulov.violetnote.loader.dropbox;

import android.content.Context;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.db.DBBasicNoteHelper;
import com.romanpulov.violetnote.db.DBStorageManager;
import com.romanpulov.violetnote.loader.cloud.RestoreCloudLoadPathProvider;
import com.romanpulov.violetnote.loader.helper.LoaderNotificationHelper;
import com.romanpulov.violetnote.view.preference.PreferenceRepository;

import java.io.File;

import static com.romanpulov.violetnote.common.NotificationRepository.NOTIFICATION_ID_LOADER;

/**
 * Loader to restore from Dropbox
 * Created by romanpulov on 11.10.2017.
 */

public class RestoreDropboxFileLoader extends DropboxFileLoader {

    public RestoreDropboxFileLoader(Context context) {
        super(context, new RestoreCloudLoadPathProvider(context));
    }

    @Override
    public void load() throws Exception {
        super.load();

        boolean isRestoreSuccess = DBStorageManager.restoreFromBackupPath(mContext, getLoadPathProvider().getDestPath());

        String restoreMessage = mContext.getString(isRestoreSuccess ? R.string.message_backup_cloud_restored : R.string.error_restore);

        PreferenceRepository.setPreferenceKeyLastLoadedCurrentTime(mContext, PreferenceRepository.PREF_KEY_BASIC_NOTE_CLOUD_RESTORE);
        LoaderNotificationHelper.notify(mContext, restoreMessage, NOTIFICATION_ID_LOADER,
                isRestoreSuccess ? LoaderNotificationHelper.NOTIFICATION_TYPE_SUCCESS : LoaderNotificationHelper.NOTIFICATION_TYPE_FAILURE);
    }
}
