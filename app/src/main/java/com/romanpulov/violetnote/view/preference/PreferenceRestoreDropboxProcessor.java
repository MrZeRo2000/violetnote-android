package com.romanpulov.violetnote.view.preference;

import android.preference.PreferenceFragment;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.loader.AbstractLoader;
import com.romanpulov.violetnote.loader.FileLoader;
import com.romanpulov.violetnote.loader.RestoreDropboxFileLoader;

import java.io.File;

/**
 * Restore from DropBox processor
 * Created by romanpulov on 20.09.2017.
 */

public class PreferenceRestoreDropboxProcessor extends PreferenceLoaderProcessor<FileLoader> {

    public PreferenceRestoreDropboxProcessor(PreferenceFragment preferenceFragment) {
        super(preferenceFragment);
    }

    private FileLoader createRestoreDropboxLoader() {
        mLoader = new RestoreDropboxFileLoader(mPreferenceFragment.getActivity());

        mLoader.setOnLoadedListener(new AbstractLoader.OnLoadedListener() {
            @Override
            public void onLoaded(String result) {
                PreferenceRepository.setDropboxRestoreDefaultPreferenceSummary(mPreferenceFragment);

                if (result != null)
                    PreferenceRepository.displayMessage(mPreferenceFragment.getActivity(), result);
                else {
                    FileLoader fileLoader = PreferenceRestoreDropboxProcessor.this.getRestoreDropboxLoader();
                    File file = new File(fileLoader.getDestPath());
                    if (file.exists()) {
                        
                    } else
                        PreferenceRepository.displayMessage(mPreferenceFragment.getActivity(), mPreferenceFragment.getString(R.string.error_restore));
                }
            }

            @Override
            public void onPreExecute() {
                PreferenceRepository.updateDropboxRestorePreferenceSummary(mPreferenceFragment, PreferenceRepository.PREF_LOAD_LOADING);
            }
        });

        return mLoader;
    }

    public FileLoader getRestoreDropboxLoader() {
        if (mLoader == null)
            mLoader = createRestoreDropboxLoader();

        return mLoader;
    }
}
