package com.romanpulov.violetnote.loader;

import android.content.Context;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import com.romanpulov.violetnote.view.SettingsFragment;
import com.romanpulov.violetnote.model.Document;

/**
 * Base class for DocumentLoader
 * Created by romanpulov on 09.06.2016.
 */
public abstract class DocumentLoader extends AbstractLoader {

    DocumentLoader(Context context) {
        super(context);
    }

    @Override
    protected String getSourcePath() {
        return PreferenceManager.getDefaultSharedPreferences(mContext).getString(SettingsFragment.PREF_KEY_SOURCE_PATH, null);
    }

    @Override
    protected String getDestPath() {
        return mContext.getCacheDir() + Document.DOCUMENT_FILE_NAME;
    }
}
