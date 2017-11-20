package com.romanpulov.violetnote.view.preference;

import android.content.Intent;
import android.os.Environment;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.romanpulov.library.common.network.NetworkUtils;
import com.romanpulov.violetnote.R;
import com.romanpulov.library.dropbox.DropboxHelper;
import com.romanpulov.violetnote.dropboxchooser.DropboxChooserActivity;
import com.romanpulov.violetnote.filechooser.FileChooserActivity;

import static com.romanpulov.violetnote.view.preference.PreferenceRepository.DEFAULT_SOURCE_TYPE;
import static com.romanpulov.violetnote.view.preference.PreferenceRepository.PREF_KEY_SOURCE_PATH;
import static com.romanpulov.violetnote.view.preference.PreferenceRepository.PREF_KEY_SOURCE_TYPE;
import static com.romanpulov.violetnote.view.preference.PreferenceRepository.SOURCE_TYPE_DROPBOX;
import static com.romanpulov.violetnote.view.preference.PreferenceRepository.SOURCE_TYPE_FILE;

/**
 * Source path preference setup
 * Created by romanpulov on 08.09.2017.
 */

public class SourcePathPreferenceSetup extends PreferenceSetup {

    public SourcePathPreferenceSetup (PreferenceFragment preferenceFragment) {
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
                        intent =  new Intent(mActivity, FileChooserActivity.class);
                        intent.putExtra(FileChooserActivity.CHOOSER_INITIAL_PATH, mPreferenceFragment.getPreferenceManager().getSharedPreferences().getString(PREF_KEY_SOURCE_PATH, Environment.getRootDirectory().getAbsolutePath()));
                        mPreferenceFragment.startActivityForResult(intent, 0);
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

                    default:
                        return false;
                }
            }
        });
    }
}
