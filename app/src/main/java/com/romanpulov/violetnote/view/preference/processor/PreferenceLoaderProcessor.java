package com.romanpulov.violetnote.view.preference.processor;

import android.content.Context;
import android.preference.PreferenceFragment;

import com.romanpulov.violetnote.loader.AbstractLoader;

/**
 * Base class for preference loader
 * Created by romanpulov on 14.09.2017.
 */

public abstract class PreferenceLoaderProcessor<T extends AbstractLoader> {
    protected final PreferenceFragment mPreferenceFragment;
    protected final Context mContext;
    protected T mLoader;

    public PreferenceLoaderProcessor(PreferenceFragment preferenceFragment) {
        mPreferenceFragment = preferenceFragment;
        mContext = preferenceFragment.getActivity();
    }

    /**
     * Checks if task is running
     * @return task running status
     */
    public boolean isTaskRunning() {
        return (mLoader != null) && (mLoader.isTaskRunning());
    }

    /**
     * Executes loader
     * @param loader loader to execute
     */
    public static void executeLoader(AbstractLoader loader) {
        if ((loader != null) && !loader.isTaskRunning()) {
            loader.execute();
        }
    }

    /**
     * Loader pre execute method template
     */
    public abstract void loaderPreExecute();

    /**
     * Loader post execute method template
     * @param result load result, null if successful
     */
    public abstract void loaderPostExecute(String result);
}
