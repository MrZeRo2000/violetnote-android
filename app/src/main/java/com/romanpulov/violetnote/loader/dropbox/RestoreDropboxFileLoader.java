package com.romanpulov.violetnote.loader.dropbox;

import android.content.Context;

import com.romanpulov.violetnote.view.preference.PreferenceRepository;

/**
 * Loader to restore from Dropbox
 * Created by romanpulov on 11.10.2017.
 */

public class RestoreDropboxFileLoader extends DropboxFileLoader {

    public RestoreDropboxFileLoader(Context context) {
        super(context, new RestoreDropboxLoadPathProvider(context));
    }

    @Override
    public void load() throws Exception {
        super.load();
        PreferenceRepository.setPreferenceKeyLastLoadedCurrentTime(mContext, PreferenceRepository.PREF_KEY_BASIC_NOTE_CLOUD_RESTORE);
    }
}
