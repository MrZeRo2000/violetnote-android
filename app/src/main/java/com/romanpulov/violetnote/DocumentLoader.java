package com.romanpulov.violetnote;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

/**
 * Created by romanpulov on 09.06.2016.
 */
public abstract class DocumentLoader {
    public static final int LOAD_APPEARANCE_SYNC = 0;
    public static final int LOAD_APPEARANCE_ASYNC = 1;

    protected Context mContext;
    protected int mLoadAppearance;

    public interface OnDocumentLoadedListener {
        void onDocumentLoaded(String result);
    }

    private OnDocumentLoadedListener mListener;

    public DocumentLoader(Context context) {
        mContext = context;
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
            if (progressDialog != null)
                progressDialog.dismiss();
            if (mListener != null)
                mListener.onDocumentLoaded(result);
        }
    }

    protected abstract void load() throws Exception;

    public void execute() {
        new DocumentLoadAsyncTask().execute();
    }
}
