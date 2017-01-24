package com.romanpulov.violetnote.document;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import com.romanpulov.violetnote.view.SettingsFragment;
import com.romanpulov.violetnote.model.Document;
import com.romanpulov.violetnote.view.core.ProgressDialogFragment;

import java.io.File;

/**
 * Created by romanpulov on 09.06.2016.
 */
public abstract class DocumentLoader {
    static final int LOAD_APPEARANCE_SYNC = 0;
    static final int LOAD_APPEARANCE_ASYNC = 1;

    final Context mContext;
    int mLoadAppearance;
    final String mSourcePath;
    final File mDestFile;

    public interface OnDocumentLoadedListener {
        void onDocumentLoaded(String result);
        void onPreExecute();
    }

    private OnDocumentLoadedListener mListener;

    DocumentLoader(Context context) {
        mContext = context;
        mSourcePath = getSourcePath();
        mDestFile = getDestFile();
    }

    private String getSourcePath() {
        return PreferenceManager.getDefaultSharedPreferences(mContext).getString(SettingsFragment.PREF_KEY_SOURCE_PATH, null);
    }

    private File getDestFile() {
        return new File(mContext.getCacheDir() + Document.DOCUMENT_FILE_NAME);
    }

    public void setOnDocumentLoadedListener(OnDocumentLoadedListener listener) {
        mListener = listener;
    }

    private class DocumentLoadAsyncTask extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            if (mListener != null)
                mListener.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                load();
                Thread.sleep(5000);
            } catch (Exception e) {
                return e.getMessage();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if (mListener != null)
                mListener.onDocumentLoaded(result);
        }
    }

    protected abstract void load() throws Exception;

    public void execute() {
        new DocumentLoadAsyncTask().execute();
    }
}
