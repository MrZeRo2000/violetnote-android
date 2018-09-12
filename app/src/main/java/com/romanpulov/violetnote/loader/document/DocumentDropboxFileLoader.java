package com.romanpulov.violetnote.loader.document;

import android.content.Context;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.loader.dropbox.DropboxFileLoader;
import com.romanpulov.violetnote.loader.helper.LoaderNotificationHelper;
import com.romanpulov.violetnote.view.preference.PreferenceRepository;

import static com.romanpulov.violetnote.common.NotificationRepository.NOTIFICATION_ID_LOADER;

/**
 * Document loader from dropbox
 * Created by romanpulov on 11.10.2017.
 */

public class DocumentDropboxFileLoader extends DropboxFileLoader {

    public DocumentDropboxFileLoader(Context context) {
        super(context, new DocumentLoadPathProvider(context));
    }

    @Override
    public void load() throws Exception {
        super.load();
        PreferenceRepository.setPreferenceKeyLastLoadedCurrentTime(mContext, PreferenceRepository.PREF_KEY_DOCUMENT_LOAD);
        LoaderNotificationHelper.notify(mContext, mContext.getString(R.string.notification_password_notes_data_load_completed), NOTIFICATION_ID_LOADER,
                LoaderNotificationHelper.NOTIFICATION_TYPE_SUCCESS);
}
}
