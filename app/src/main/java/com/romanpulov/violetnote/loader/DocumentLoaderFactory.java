package com.romanpulov.violetnote.loader;

import android.content.Context;

import com.romanpulov.violetnote.view.preference.PreferenceRepository;

/**
 * Factory for DocumentLoader creation
 * Created by romanpulov on 16.06.2016.
 */
public class DocumentLoaderFactory {
    public static DocumentLoader fromType(Context context, int type) {
        switch(type) {
            case PreferenceRepository.SOURCE_TYPE_FILE:
                return new DocumentFileLoader(context);
            case PreferenceRepository.SOURCE_TYPE_DROPBOX:
                return new DocumentDropboxFileLoader(context);
            default:
                return null;
        }
    }
}
