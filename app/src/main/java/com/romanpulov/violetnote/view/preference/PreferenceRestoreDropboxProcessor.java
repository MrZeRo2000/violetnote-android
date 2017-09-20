package com.romanpulov.violetnote.view.preference;

import android.preference.PreferenceFragment;

import com.romanpulov.violetnote.loader.AbstractLoader;
import com.romanpulov.violetnote.loader.RestoreDropboxFileLoader;

/**
 * Restore from DropBox processor
 * Created by romanpulov on 20.09.2017.
 */

public class PreferenceRestoreDropboxProcessor extends PreferenceLoaderProcessor {

    public PreferenceRestoreDropboxProcessor(PreferenceFragment preferenceFragment) {
        super(preferenceFragment);
    }

    private AbstractLoader createRestoreDropboxLoader() {
        mLoader = new RestoreDropboxFileLoader(mPreferenceFragment.getActivity());

        mLoader.setOnLoadedListener(new AbstractLoader.OnLoadedListener() {
            @Override
            public void onLoaded(String result) {
                PreferenceRepository.setDropboxRestoreDefaultPreferenceSummary(mPreferenceFragment);

                PreferenceRepository.displayMessage(mPreferenceFragment.getActivity(), "Loaded, result :" + result);
            }

            @Override
            public void onPreExecute() {
                PreferenceRepository.updateDropboxRestorePreferenceSummary(mPreferenceFragment, PreferenceRepository.PREF_LOAD_LOADING);
            }
        });

        return mLoader;
    }

    public AbstractLoader getRestoreDropboxLoader() {
        if (mLoader == null)
            mLoader = createRestoreDropboxLoader();

        return mLoader;
    }
}
