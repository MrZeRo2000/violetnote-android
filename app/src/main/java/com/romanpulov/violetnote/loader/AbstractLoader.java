package com.romanpulov.violetnote.loader;

import android.content.Context;
import android.os.AsyncTask;

/**
 * Common loader class for sync and async load
 * Created by romanpulov on 06.09.2017.
 */

public abstract class AbstractLoader {
    public static final int LOAD_APPEARANCE_SYNC = 0;
    public static final int LOAD_APPEARANCE_ASYNC = 1;

    protected final Context mContext;
    protected int mLoadAppearance = LOAD_APPEARANCE_SYNC;
    private AsyncTask mTask;

    public int getLoadAppearance() {
        return mLoadAppearance;
    }

    public interface OnLoadedListener {
        void onLoaded(String result);
        void onPreExecute();
    }

    private AbstractLoader.OnLoadedListener mListener;

    AbstractLoader(Context context) {
        mContext = context;
    }

    public void setOnLoadedListener(AbstractLoader.OnLoadedListener listener) {
        mListener = listener;
    }

    private class LoadAsyncTask extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            if (mListener != null)
                mListener.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                Thread.sleep(3000);
                load();
            } catch (Exception e) {
                return e.getMessage();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if (mListener != null)
                mListener.onLoaded(result);
        }
    }

    protected abstract void load() throws Exception;

    public abstract boolean isInternetRequired();

    public void execute() {
        mTask = new AbstractLoader.LoadAsyncTask().execute();
    }

    public boolean isTaskRunning() {
        return (mTask != null) && (mTask.getStatus() != AsyncTask.Status.FINISHED);
    }
}
