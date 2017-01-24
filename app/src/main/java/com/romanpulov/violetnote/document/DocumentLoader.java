package com.romanpulov.violetnote.document;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.view.SettingsFragment;
import com.romanpulov.violetnote.model.Document;

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
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            if (mLoadAppearance == LOAD_APPEARANCE_ASYNC) {
                progressDialog = new ProgressDialog(mContext, R.style.DialogTheme);
                progressDialog.setTitle(R.string.caption_loading);
                progressDialog.show();
            }
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                load();
            } catch (Exception e) {
                return e.getMessage();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                if (progressDialog != null)
                    progressDialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (mListener != null)
                mListener.onDocumentLoaded(result);
        }
    }

    protected abstract void load() throws Exception;

    public void execute() {
        new DocumentLoadAsyncTask().execute();
    }
}
