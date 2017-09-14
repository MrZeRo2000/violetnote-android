package com.romanpulov.violetnote.view.preference;

import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.romanpulov.violetnote.loader.AbstractLoader;
import com.romanpulov.violetnote.loader.DocumentLoader;
import com.romanpulov.violetnote.loader.DocumentLoaderFactory;

/**
 * Loader processes executor class
 * Created by romanpulov on 11.09.2017.
 */

public class PreferenceDocumentLoaderProcessor extends PreferenceLoaderProcessor{

    public PreferenceDocumentLoaderProcessor(PreferenceFragment preferenceFragment) {
        super(preferenceFragment);
    }

    public AbstractLoader createDocumentLoader(int type) {
        mLoader = DocumentLoaderFactory.fromType(mPreferenceFragment.getActivity(), type);
        if (mLoader != null) {
            mLoader.setOnLoadedListener(new AbstractLoader.OnLoadedListener() {
                @Override
                public void onLoaded(String result) {
                    Preference prefLoad = mPreferenceFragment.findPreference(PreferenceRepository.PREF_KEY_LOAD);

                    if (result == null) {
                        long loadedTime = System.currentTimeMillis();
                        prefLoad.getPreferenceManager().getSharedPreferences().edit().putLong(PreferenceRepository.PREF_KEY_LAST_LOADED, loadedTime).apply();
                        PreferenceRepository.updateLoadPreferenceSummary(mPreferenceFragment, loadedTime);
                    } else {
                        PreferenceRepository.displayMessage(mPreferenceFragment.getActivity(), result);
                        PreferenceRepository.updateLoadPreferenceSummary(mPreferenceFragment, PreferenceRepository.PREF_LOAD_CURRENT_VALUE);
                    }

                    //mLoader = null;
                }

                @Override
                public void onPreExecute() {
                    if (mLoader.getLoadAppearance() == DocumentLoader.LOAD_APPEARANCE_ASYNC)
                        PreferenceRepository.updateLoadPreferenceSummary(mPreferenceFragment, PreferenceRepository.PREF_LOAD_LOADING);

                }
            });
        }

        return mLoader;
    }

    /**
     * Creates and executes DocumentLoader
     * @param type Document Type
     * @return DocumentLoader instance
     */
    public AbstractLoader executeDocumentLoader(int type) {
        // create loader if not exists
        if (mLoader == null)
            createDocumentLoader(type);

        if ((mLoader != null) && !mLoader.isTaskRunning())
            mLoader.execute();

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
