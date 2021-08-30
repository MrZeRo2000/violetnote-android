package com.romanpulov.violetnote.loader.msgraph;

import android.content.Context;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.loader.document.DocumentLoadPathProvider;
import com.romanpulov.violetnote.loader.helper.LoaderNotificationHelper;
import com.romanpulov.violetnote.view.preference.PreferenceRepository;

import static com.romanpulov.violetnote.common.NotificationRepository.NOTIFICATION_ID_LOADER;

public class DocumentMSGraphFileLoader extends AbstractMSGraphFileLoader {
    public DocumentMSGraphFileLoader(Context context) {
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
