package com.romanpulov.violetnote.view.preference;

import android.content.Intent;
import android.os.Environment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.romanpulov.library.common.network.NetworkUtils;
import com.romanpulov.violetnote.R;
import com.romanpulov.library.dropbox.DropboxHelper;
import com.romanpulov.violetnote.dropboxchooser.DropboxChooserActivity;
import com.romanpulov.violetnote.filechooser.FileChooserActivity;
import com.romanpulov.library.common.account.AbstractCloudAccountManager;
import com.romanpulov.violetnote.loader.account.OneDriveCloudAccountManager;
import com.romanpulov.violetnote.onedrivechooser.OneDriveChooserActivity;

import static com.romanpulov.violetnote.view.preference.PreferenceRepository.DEFAULT_SOURCE_TYPE;
import static com.romanpulov.violetnote.view.preference.PreferenceRepository.PREF_KEY_SOURCE_PATH;
import static com.romanpulov.violetnote.view.preference.PreferenceRepository.PREF_KEY_SOURCE_TYPE;
import static com.romanpulov.violetnote.view.preference.PreferenceRepository.SOURCE_TYPE_DROPBOX;
import static com.romanpulov.violetnote.view.preference.PreferenceRepository.SOURCE_TYPE_FILE;
import static com.romanpulov.violetnote.view.preference.PreferenceRepository.SOURCE_TYPE_ONEDRIVE;

/**
 * Source path preference setup
 * Created by romanpulov on 08.09.2017.
 */

public class SourcePathPreferenceSetup extends PreferenceSetup {
    public static final int OPEN_SOURCE_RESULT_CODE = 100;

    public SourcePathPreferenceSetup (PreferenceFragmentCompat preferenceFragment) {
        super(preferenceFragment, PREF_KEY_SOURCE_PATH);
    }

    @Override
    public void execute() {
        final String sourcePath = mPreference.getPreferenceManager().getSharedPreferences().getString(mPreference.getKey(), null);
        mPreference.setSummary(sourcePath);

        mPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                int sourceType = preference.getPreferenceManager().getSharedPreferences().getInt(PREF_KEY_SOURCE_TYPE, DEFAULT_SOURCE_TYPE);

                Intent intent;
                switch (sourceType) {
                    case SOURCE_TYPE_FILE:
                        /*
                        intent =  new Intent(mActivity, FileChooserActivity.class);
                        intent.putExtra(FileChooserActivity.CHOOSER_INITIAL_PATH, mPreferenceFragment.getPreferenceManager().getSharedPreferences().getString(PREF_KEY_SOURCE_PATH, Environment.getRootDirectory().getAbsolutePath()));
                        mPreferenceFragment.startActivityForResult(intent, 0);
                         */
                        intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                        intent.addCategory(Intent.CATEGORY_OPENABLE);
                        intent.setType("*/*");
                        mPreferenceFragment.startActivityForResult(intent, OPEN_SOURCE_RESULT_CODE);
                        return true;
                    case SOURCE_TYPE_DROPBOX:
                        if (!NetworkUtils.isNetworkAvailable(mContext))
                            PreferenceRepository.displayMessage(mActivity,mActivity.getResources().getString(R.string.error_internet_not_available));
                        else {
                            try {
                                DropboxHelper.getInstance(mActivity.getApplication()).validateDropBox();

                                intent = new Intent(mActivity, DropboxChooserActivity.class);
                                intent.putExtra(DropboxChooserActivity.CHOOSER_INITIAL_PATH, mPreferenceFragment.getPreferenceManager().getSharedPreferences().getString(PREF_KEY_SOURCE_PATH, Environment.getRootDirectory().getAbsolutePath()));
                                mPreferenceFragment.startActivityForResult(intent, 0);
                                return true;
                            } catch (DropboxHelper.DBHNoAccessTokenException e) {
                                PreferenceRepository.displayMessage(mActivity, mActivity.getResources().getString(R.string.error_dropbox_auth));
                            } catch (DropboxHelper.DBHException e) {
                                PreferenceRepository.displayMessage(mActivity, String.format(mActivity.getResources().getString(R.string.error_dropbox_other), e.getMessage()));
                            }
                        }
                        return true;
                    case SOURCE_TYPE_ONEDRIVE:
                        if (!NetworkUtils.isNetworkAvailable(mContext))
                            PreferenceRepository.displayMessage(mActivity,mActivity.getResources().getString(R.string.error_internet_not_available));
                        else {
                            final OneDriveCloudAccountManager oneDriveAccountManager = new OneDriveCloudAccountManager(mActivity);
                            oneDriveAccountManager.setOnAccountSetupListener(new AbstractCloudAccountManager.OnAccountSetupListener() {
                                @Override
                                public void onAccountSetupSuccess() {
                                    Intent intent = new Intent(mActivity, OneDriveChooserActivity.class);
                                    intent.putExtra(OneDriveChooserActivity.CHOOSER_INITIAL_PATH, mPreferenceFragment.getPreferenceManager().getSharedPreferences().getString(PREF_KEY_SOURCE_PATH, Environment.getRootDirectory().getAbsolutePath()));
                                    mPreferenceFragment.startActivityForResult(intent, 0);
                                }

                                @Override
                                public void onAccountSetupFailure(String errorText) {
                                    PreferenceRepository.displayMessage(mActivity, errorText);
                                }
                            });

                            oneDriveAccountManager.setupAccount();

                            return true;

                        }
                        return true;
                    default:
                        return false;
                }
            }
        });
    }
}
