package com.romanpulov.violetnote.loader.document;

import android.content.Context;

import com.romanpulov.library.common.loader.file.LocalFileLoader;
import com.romanpulov.violetnote.view.preference.PreferenceRepository;

/**
 * Document loader from local file
 * Created by romanpulov on 11.10.2017.
 */

public class DocumentLocalFileLoader extends LocalFileLoader {

    public DocumentLocalFileLoader(Context context) {
        super(context, new DocumentLoadPathProvider(context));
    }

    @Override
    public void load() throws Exception {
        super.load();
        PreferenceRepository.setPreferenceKeyLastLoadedCurrentTime(mContext, PreferenceRepository.PREF_KEY_DOCUMENT_LOAD);
    }
}
