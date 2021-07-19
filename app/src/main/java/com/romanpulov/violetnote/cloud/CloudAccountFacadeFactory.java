package com.romanpulov.violetnote.cloud;

import androidx.annotation.NonNull;

import com.romanpulov.violetnote.view.preference.PreferenceRepository;

public class CloudAccountFacadeFactory {
    @NonNull
    public static CloudAccountFacade fromDocumentSourceType(int type) {
        switch (type) {
            case PreferenceRepository.SOURCE_TYPE_DROPBOX:
                return new DropboxAccountFacade();
            case PreferenceRepository.SOURCE_TYPE_ONEDRIVE:
                return new OneDriveAccountFacade();
            case PreferenceRepository.SOURCE_TYPE_MSGRAPH:
                return new MSGraphAccountFacade();
            default:
                throw new NullPointerException();
        }
    }

    @NonNull
    public static CloudAccountFacade fromCloudSourceType(int type) {
        switch (type) {
            case PreferenceRepository.CLOUD_SOURCE_TYPE_DROPBOX:
                return new DropboxAccountFacade();
            case PreferenceRepository.CLOUD_SOURCE_TYPE_ONEDRIVE:
                return new OneDriveAccountFacade();
            case PreferenceRepository.CLOUD_SOURCE_TYPE_MSGRAPH:
                return new MSGraphAccountFacade();
            default:
                throw new NullPointerException();
        }
    }
}
