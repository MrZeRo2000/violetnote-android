package com.romanpulov.violetnote.loader;

import android.content.Context;
import android.preference.PreferenceManager;

import com.romanpulov.violetnote.view.preference.PreferenceRepository;

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
        PreferenceRepository.setDocumentLastLoadedCurrentTime(mContext);
    }
}
