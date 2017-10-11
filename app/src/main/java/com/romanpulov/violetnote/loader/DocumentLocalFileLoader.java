package com.romanpulov.violetnote.loader;

import android.content.Context;

/**
 * Document loader from local file
 * Created by romanpulov on 11.10.2017.
 */

public class DocumentLocalFileLoader extends LocalFileLoader {

    public DocumentLocalFileLoader(Context context) {
        super(context, new DocumentLoadPathProvider(context));
    }
}
