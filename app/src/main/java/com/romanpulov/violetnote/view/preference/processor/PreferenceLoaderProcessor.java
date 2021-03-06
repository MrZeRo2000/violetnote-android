package com.romanpulov.violetnote.view.preference.processor;

import android.content.Context;
import androidx.preference.PreferenceFragmentCompat;

/**
 * Base class for preference loader
 * Created by romanpulov on 14.09.2017.
 */

public abstract class PreferenceLoaderProcessor {
    protected final PreferenceFragmentCompat mPreferenceFragment;
    protected final Context mContext;

    public PreferenceLoaderProcessor(PreferenceFragmentCompat preferenceFragment) {
        mPreferenceFragment = preferenceFragment;
        mContext = preferenceFragment.getActivity();
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
