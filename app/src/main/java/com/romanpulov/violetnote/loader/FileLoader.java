package com.romanpulov.violetnote.loader;

import android.content.Context;

/**
 * Abstract class for file loader
 * Created by romanpulov on 20.09.2017.
 */

public abstract class FileLoader extends AbstractLoader {

    final String mSourcePath;
    final String mDestPath;

    protected abstract String getSourcePath();
    protected abstract String getDestPath();

    FileLoader(Context context) {
        super(context);
        //file path
        mSourcePath = getSourcePath();
        mDestPath = getDestPath();
    }
}
