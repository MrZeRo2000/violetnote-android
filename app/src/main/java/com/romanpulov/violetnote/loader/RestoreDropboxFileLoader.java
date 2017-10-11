package com.romanpulov.violetnote.loader;

import android.content.Context;

/**
 * Loader to restore from Dropbox
 * Created by romanpulov on 11.10.2017.
 */

public class RestoreDropboxFileLoader extends DropboxFileLoader {

    public RestoreDropboxFileLoader(Context context) {
        super(context, new RestoreDropboxLoadPathProvider(context));
    }
}
