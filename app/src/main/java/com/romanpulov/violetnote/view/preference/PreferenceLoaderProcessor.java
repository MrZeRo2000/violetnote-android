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

public class PreferenceLoaderProcessor {
    private final PreferenceFragment mPreferenceFragment;

    private DocumentLoader mDocumentLoader;

    public DocumentLoader getDocumentLoader() {
        return mDocumentLoader;
    }

    public PreferenceLoaderProcessor(PreferenceFragment preferenceFragment) {
        mPreferenceFragment = preferenceFragment;
    }

    /**
     * Creates and executes DocumentLoader
     * @param type Document Type
     * @return DocumentLoader instance
     */
    public DocumentLoader executeDocumentLoader(int type) {
        if (mDocumentLoader == null) {
            mDocumentLoader = DocumentLoaderFactory.fromType(mPreferenceFragment.getActivity(), type);
            if (mDocumentLoader != null) {
                mDocumentLoader.setOnLoadedListener(new AbstractLoader.OnLoadedListener() {
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

                        mDocumentLoader = null;
                    }

                    @Override
                    public void onPreExecute() {
                        if (mDocumentLoader.getLoadAppearance() == DocumentLoader.LOAD_APPEARANCE_ASYNC)
                            PreferenceRepository.updateLoadPreferenceSummary(mPreferenceFragment, PreferenceRepository.PREF_LOAD_LOADING);

                    }
                });

                mDocumentLoader.execute();
            }
        }

        return mDocumentLoader;
    }

    /**
     * Updates preference with load status when running
     */
    public void updateLoadPreferenceStatus() {
        if (mDocumentLoader != null)
            PreferenceRepository.updateLoadPreferenceSummary(mPreferenceFragment, PreferenceRepository.PREF_LOAD_LOADING);
    }
}
