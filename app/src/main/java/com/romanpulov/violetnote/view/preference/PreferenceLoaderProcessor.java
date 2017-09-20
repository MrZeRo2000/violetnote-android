package com.romanpulov.violetnote.view.preference;

import android.preference.PreferenceFragment;

import com.romanpulov.violetnote.loader.AbstractLoader;

/**
 * Base class for preference loader
 * Created by romanpulov on 14.09.2017.
 */

public abstract class PreferenceLoaderProcessor<T extends AbstractLoader> {
    protected final PreferenceFragment mPreferenceFragment;
    protected T mLoader;

    public PreferenceLoaderProcessor(PreferenceFragment preferenceFragment) {
        mPreferenceFragment = preferenceFragment;
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
        if ((loader != null) && !loader.isTaskRunning())
            loader.execute();
    }
}
