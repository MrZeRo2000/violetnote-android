package com.romanpulov.violetnote.loader.document;

import android.content.Context;

import com.romanpulov.library.common.loader.file.UriFileLoader;
import com.romanpulov.violetnote.view.preference.PreferenceRepository;

public class DocumentUriFileLoader extends UriFileLoader {
    public DocumentUriFileLoader(Context context) {
        super(context, new DocumentLoadPathProvider(context));
    }

    @Override
    public void load() throws Exception {
        super.load();
        PreferenceRepository.setPreferenceKeyLastLoadedCurrentTime(mContext, PreferenceRepository.PREF_KEY_DOCUMENT_LOAD);
    }
}
