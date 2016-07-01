package com.romanpulov.violetnote;

import android.content.Context;

/**
 * Created by romanpulov on 16.06.2016.
 */
public class DocumentLoaderFactory {
    public static DocumentLoader fromType(Context context, int type) {
        switch(type) {
            case SettingsFragment.SOURCE_TYPE_FILE:
                return new DocumentFileLoader(context);
            case SettingsFragment.SOURCE_TYPE_DROPBOX:
                return new DocumentDropboxLoader(context);
            default:
                return null;
        }
    }
}
