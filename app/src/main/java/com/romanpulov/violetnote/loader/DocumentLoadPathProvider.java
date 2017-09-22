package com.romanpulov.violetnote.loader;

import android.content.Context;
import android.preference.PreferenceManager;

import com.romanpulov.violetnote.view.preference.PreferenceRepository;

/**
 * Load path for DocumentPassDataLoader
 * Created by romanpulov on 22.09.2017.
 */

public class DocumentLoadPathProvider extends ContextLoadPathProvider {

    @Override
    public String getSourcePath() {
        return PreferenceManager.getDefaultSharedPreferences(getContext()).getString(PreferenceRepository.PREF_KEY_SOURCE_PATH, null);
    }

    @Override
    public String getDestPath() {
        return DocumentPassDataLoader.getDocumentFileName(getContext());
    }

    public DocumentLoadPathProvider(Context context) {
        super(context);
    }
}
