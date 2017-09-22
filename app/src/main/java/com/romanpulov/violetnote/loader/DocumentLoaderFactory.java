package com.romanpulov.violetnote.loader;

import android.content.Context;

import com.romanpulov.violetnote.view.preference.PreferenceRepository;

/**
 * Factory for DocumentLoader creation
 * Created by romanpulov on 16.06.2016.
 */
public class DocumentLoaderFactory {
    public static AbstractLoader fromType(Context context, int type) {
        switch(type) {
            case PreferenceRepository.SOURCE_TYPE_FILE:
                //return new DocumentFileLoader(context);
                return new LocalFileLoader(context, new DocumentLoadPathProvider(context));
            case PreferenceRepository.SOURCE_TYPE_DROPBOX:
                //return new DocumentDropboxFileLoader(context);
                return new DropboxFileLoader(context, new DocumentLoadPathProvider(context));
            default:
                return null;
        }
    }
}
