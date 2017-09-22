package com.romanpulov.violetnote.view.preference;

import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.romanpulov.violetnote.loader.AbstractLoader;
import com.romanpulov.violetnote.loader.DocumentLoaderFactory;

/**
 * Loader processes executor class
 * Created by romanpulov on 11.09.2017.
 */

public class PreferenceDocumentLoaderProcessor extends PreferenceLoaderProcessor{

    public PreferenceDocumentLoaderProcessor(PreferenceFragment preferenceFragment) {
        super(preferenceFragment);
    }

    private AbstractLoader createDocumentLoader(int type) {
        mLoader = DocumentLoaderFactory.fromType(mPreferenceFragment.getActivity(), type);
        if (mLoader != null) {
            mLoader.setOnLoadedListener(new AbstractLoader.OnLoadedListener() {
                @Override
                public void onLoaded(String result) {
                    Preference pref = mPreferenceFragment.findPreference(PreferenceRepository.PREF_KEY_LOAD);

                    if (result == null) {
                        long loadedTime = System.currentTimeMillis();
                        pref.getPreferenceManager().getSharedPreferences().edit().putLong(PreferenceRepository.PREF_KEY_LAST_LOADED, loadedTime).apply();
                        PreferenceRepository.updateLoadPreferenceSummary(mPreferenceFragment, loadedTime);
                    } else {
                        PreferenceRepository.displayMessage(mContext, result);
                        PreferenceRepository.updateLoadPreferenceSummary(mPreferenceFragment, PreferenceRepository.PREF_LOAD_CURRENT_VALUE);
                    }
                }

                @Override
                public void onPreExecute() {
                    if (mLoader.getLoadAppearance() == AbstractLoader.LOAD_APPEARANCE_ASYNC)
                        PreferenceRepository.updateLoadPreferenceSummary(mPreferenceFragment, PreferenceRepository.PREF_LOAD_LOADING);

                }
            });
        }

        return mLoader;
    }


    /**
     * Gets or creates a document loader
     * @param type document loader type
     * @return loader
     */
    public AbstractLoader getDocumentLoader(int type) {
        if (mLoader == null)
            return createDocumentLoader(type);
        else
            return mLoader;
    }

    /**
     * Updates preference with load status when running
     */
    public void updateLoadPreferenceStatus() {
        if (isTaskRunning())
            PreferenceRepository.updateLoadPreferenceSummary(mPreferenceFragment, PreferenceRepository.PREF_LOAD_LOADING);
    }
}
