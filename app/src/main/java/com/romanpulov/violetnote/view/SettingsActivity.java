package com.romanpulov.violetnote.view;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.romanpulov.violetnote.view.core.ActionBarCompatActivity;
import com.romanpulov.violetnote.view.helper.PermissionRequestHelper;

public class SettingsActivity extends ActionBarCompatActivity {
    public final static int PERMISSION_REQUEST_LOCAL_BACKUP = 101;
    public final static int PERMISSION_REQUEST_LOCAL_RESTORE = 102;
    public final static int PERMISSION_REQUEST_DROPBOX_RESTORE = 103;
    public final static int PERMISSION_REQUEST_DROPBOX_BACKUP = 104;
    public final static int PERMISSION_REQUEST_DOCUMENT_LOAD = 105;

    private Fragment getSettingsFragment() {
        return getFragmentManager().findFragmentById(android.R.id.content);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Display the fragment as the main content.

        if (getSettingsFragment() == null)
            getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        SettingsFragment settingsFragment = (SettingsFragment) getSettingsFragment();

        if ((settingsFragment != null) && (PermissionRequestHelper.isGrantResultSuccessful(grantResults))) {
            switch (requestCode) {
                case PERMISSION_REQUEST_LOCAL_BACKUP:
                    settingsFragment.executeLocalBackup();
                    break;
                case PERMISSION_REQUEST_LOCAL_RESTORE:
                    settingsFragment.executeLocalRestore();
                    break;
                case PERMISSION_REQUEST_DROPBOX_RESTORE:
                    settingsFragment.executeDropboxRestore();
                    break;
                case PERMISSION_REQUEST_DROPBOX_BACKUP:
                    settingsFragment.executeDropboxBackup();
                case PERMISSION_REQUEST_DOCUMENT_LOAD:
                    settingsFragment.executeDocumentLoad();
                default:
                    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        } else
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
