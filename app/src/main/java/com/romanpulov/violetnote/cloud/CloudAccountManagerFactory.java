package com.romanpulov.violetnote.cloud;

import android.app.Activity;

import com.romanpulov.library.common.account.AbstractCloudAccountManager;
import com.romanpulov.violetnote.view.preference.PreferenceRepository;

public class CloudAccountManagerFactory {

    public static AbstractCloudAccountManager<?> fromDocumentSourceType(Activity activity, int type) {
        switch (type) {
            case PreferenceRepository.SOURCE_TYPE_FILE:
                return null;
            case PreferenceRepository.SOURCE_TYPE_DROPBOX:
                return new DropboxCloudAccountManager(activity);
            case PreferenceRepository.SOURCE_TYPE_ONEDRIVE:
                return new OneDriveCloudAccountManager(activity);
            default:
                return null;
        }
    }

    public static AbstractCloudAccountManager<?> fromCloudSourceType(Activity activity, int type) {
        switch (type) {
            case PreferenceRepository.CLOUD_SOURCE_TYPE_DROPBOX:
                return new DropboxCloudAccountManager(activity);
            case PreferenceRepository.CLOUD_SOURCE_TYPE_ONEDRIVE:
                return new OneDriveCloudAccountManager(activity);
            default:
                return null;
        }
    }

}
