package com.romanpulov.violetnote.loader;

import android.content.Context;

/**
 * Document loader from dropbox
 * Created by romanpulov on 11.10.2017.
 */

public class DocumentDropboxFileLoader extends DropboxFileLoader {

    public DocumentDropboxFileLoader(Context context) {
        super(context, new DocumentLoadPathProvider(context));
    }
}
