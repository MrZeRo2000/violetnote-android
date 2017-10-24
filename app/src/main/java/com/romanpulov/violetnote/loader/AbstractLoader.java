package com.romanpulov.violetnote.loader;

import android.content.Context;
import android.os.AsyncTask;

import java.lang.reflect.Method;

/**
 * Common loader class for sync and async load
 * Created by romanpulov on 06.09.2017.
 */

public abstract class AbstractLoader {

    protected final Context mContext;
    private AsyncTask mTask;

    public interface OnLoadedListener {
        void onLoaded(String result);
        void onPreExecute();
    }

    private AbstractLoader.OnLoadedListener mListener;

    AbstractLoader(Context context) {
        mContext = context;
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

    public abstract void load() throws Exception;

    public void execute() {
        mTask = new AbstractLoader.LoadAsyncTask().execute();
    }

    public boolean isTaskRunning() {
        return (mTask != null) && (mTask.getStatus() != AsyncTask.Status.FINISHED);
    }

    public static boolean isLoaderInternetConnectionRequired(Class<? extends AbstractLoader> loaderClass) {
        boolean result = false;
        try {
            Method loaderInternetRequiredMethod = loaderClass.getMethod("isLoaderInternetRequired");
            result = (Boolean) loaderInternetRequiredMethod.invoke(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
