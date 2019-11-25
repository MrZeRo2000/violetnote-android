package com.romanpulov.violetnote.loader.account;

import android.app.Activity;

import com.romanpulov.violetnote.view.preference.PreferenceRepository;

public class AccountManagerFactory {
    public static AbstractAccountManager fromType(Activity activity, int type) {
        switch (type) {
            case PreferenceRepository.SOURCE_TYPE_FILE:
                return null;
            case PreferenceRepository.SOURCE_TYPE_DROPBOX:
                return new DropboxAccountManager(activity);
            default:
                return null;
        }
    }
}
