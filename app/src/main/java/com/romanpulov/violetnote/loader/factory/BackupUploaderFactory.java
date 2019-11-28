package com.romanpulov.violetnote.loader.factory;

import com.romanpulov.library.common.loader.core.AbstractContextLoader;
import com.romanpulov.violetnote.loader.dropbox.BackupDropboxUploader;
import com.romanpulov.violetnote.loader.onedrive.BackupOneDriveUploader;
import com.romanpulov.violetnote.view.preference.PreferenceRepository;

public class BackupUploaderFactory {
    public static Class<? extends AbstractContextLoader> classFromCloudType(int type) {
        switch (type) {
            case PreferenceRepository.CLOUD_SOURCE_TYPE_DROPBOX:
                return BackupDropboxUploader.class;
            case PreferenceRepository.CLOUD_SOURCE_TYPE_ONEDRIVE:
                return BackupOneDriveUploader.class;
            default:
                return null;
        }
    }
}
