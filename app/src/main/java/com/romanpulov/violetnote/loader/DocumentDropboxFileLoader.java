package com.romanpulov.violetnote.loader;

import android.content.Context;
import android.preference.PreferenceManager;

import com.romanpulov.violetnote.model.Document;
import com.romanpulov.violetnote.view.preference.PreferenceRepository;

import java.io.File;

/**
 * Document file loader for DropBox
 * Created by romanpulov on 20.09.2017.
 */

public class DocumentDropboxFileLoader extends DropboxFileLoader {

    DocumentDropboxFileLoader(Context context) {
        super(context);
    }

    @Override
    protected String getSourcePath() {
        return PreferenceManager.getDefaultSharedPreferences(mContext).getString(PreferenceRepository.PREF_KEY_SOURCE_PATH, null);
    }

    @Override
    protected String getDestPath() {
        return mContext.getCacheDir() + File.separator + Document.DOCUMENT_FILE_NAME;
    }
}
