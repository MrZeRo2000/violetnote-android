package com.romanpulov.violetnote.loader.factory;

import com.romanpulov.library.common.loader.core.AbstractContextLoader;
import com.romanpulov.violetnote.loader.dropbox.RestoreDropboxFileLoader;
import com.romanpulov.violetnote.loader.onedrive.RestoreOneDriveFileLoader;
import com.romanpulov.violetnote.view.preference.PreferenceRepository;

public class BackupRestoreFactory {
    public static Class<? extends AbstractContextLoader> classFromCloudType(int type) {
        switch (type) {
            case PreferenceRepository.CLOUD_SOURCE_TYPE_DROPBOX:
                return RestoreDropboxFileLoader.class;
            case PreferenceRepository.CLOUD_SOURCE_TYPE_ONEDRIVE:
                return RestoreOneDriveFileLoader.class;
            default:
                return null;
        }
    }
}
