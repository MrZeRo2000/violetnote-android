package com.romanpulov.violetnote.loader;

import android.content.Context;
import android.preference.PreferenceManager;

import com.romanpulov.violetnote.model.Document;
import com.romanpulov.violetnote.view.preference.PreferenceRepository;


/**
 * Base class for DocumentLoader
 * Created by romanpulov on 09.06.2016.
 */
public abstract class DocumentLoader extends FileLoader {

    DocumentLoader(Context context) {
        super(context);
    }

    @Override
    public String getSourcePath() {
        return PreferenceManager.getDefaultSharedPreferences(mContext).getString(PreferenceRepository.PREF_KEY_SOURCE_PATH, null);
    }

    @Override
    public String getDestPath() {
        return mContext.getCacheDir() + Document.DOCUMENT_FILE_NAME;
    }
}
