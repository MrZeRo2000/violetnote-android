package com.romanpulov.violetnote.loader;

import com.romanpulov.violetnote.view.preference.PreferenceRepository;

/**
 * Common data for Dropbox
 * Created by romanpulov on 22.09.2017.
 */

public class DropboxLoaderRepository {
    public static final String REMOTE_PATH = "/AndroidBackup/";
    public static final String LAST_LOADED_PREF_KEY = PreferenceRepository.PREF_KEY_BASIC_NOTE_CLOUD_BACKUP_LAST_LOADED;
}
