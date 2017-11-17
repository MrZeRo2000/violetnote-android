package com.romanpulov.violetnote.loader;

import android.content.Context;

import com.romanpulov.violetnote.loader.core.AbstractLoader;

/**
 * Abstract class for file loader
 * Created by romanpulov on 20.09.2017.
 */

public abstract class FileLoader extends AbstractLoader {
    private final LoadPathProvider mLoadPathProvider;

    public LoadPathProvider getLoadPathProvider() {
        return mLoadPathProvider;
    }

    FileLoader(Context context, LoadPathProvider loadPathProvider) {
        super(context);
        mLoadPathProvider = loadPathProvider;
    }
}
