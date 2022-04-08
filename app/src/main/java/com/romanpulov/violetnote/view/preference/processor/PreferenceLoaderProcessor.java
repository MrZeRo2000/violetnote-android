package com.romanpulov.violetnote.view.preference.processor;

import android.app.Activity;

import androidx.preference.PreferenceFragmentCompat;

/**
 * Base class for preference loader
 * Created by romanpulov on 14.09.2017.
 */

public abstract class PreferenceLoaderProcessor {
    protected final PreferenceFragmentCompat mPreferenceFragment;
    protected final Activity mActivity;

    public PreferenceLoaderProcessor(PreferenceFragmentCompat preferenceFragment) {
        mPreferenceFragment = preferenceFragment;
        mActivity = preferenceFragment.getActivity();
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
