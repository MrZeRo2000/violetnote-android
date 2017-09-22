package com.romanpulov.violetnote.loader;

import android.content.Context;
import android.preference.PreferenceManager;

import com.romanpulov.violetnote.model.Document;
import com.romanpulov.violetnote.view.preference.PreferenceRepository;

import java.io.File;

/**
 * Load path for Document
 * Created by romanpulov on 22.09.2017.
 */

public class DocumentLoadPathProvider extends ContextLoadPathProvider {

    @Override
    public String getSourcePath() {
        return PreferenceManager.getDefaultSharedPreferences(getContext()).getString(PreferenceRepository.PREF_KEY_SOURCE_PATH, null);
    }

    @Override
    public String getDestPath() {
        return getContext().getCacheDir() + File.separator + Document.DOCUMENT_FILE_NAME;
    }

    public DocumentLoadPathProvider(Context context) {
        super(context);
    }
}
