package com.romanpulov.violetnote.db.manager;

import android.content.Context;

/**
 * Base class for DB manager
 */
public abstract class AbstractDBManager {
    protected final Context mContext;

    AbstractDBManager(Context context) {
        mContext = context;
    }
}
