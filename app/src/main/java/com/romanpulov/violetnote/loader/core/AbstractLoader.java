package com.romanpulov.violetnote.loader.core;

import android.content.Context;

/**
 * Common loader class
 * Created by romanpulov on 06.09.2017.
 */

public abstract class AbstractLoader implements Loader {

    protected final Context mContext;

    public AbstractLoader(Context context) {
        mContext = context;
    }

    public abstract void load() throws Exception;

}
