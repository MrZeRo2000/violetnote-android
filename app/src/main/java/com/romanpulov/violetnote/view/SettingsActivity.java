package com.romanpulov.violetnote.view;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;

import com.romanpulov.violetnote.cloud.GDHelper;
import com.romanpulov.violetnote.view.core.ActionBarCompatActivity;
import com.romanpulov.violetnote.view.helper.PermissionRequestHelper;

public class SettingsActivity extends ActionBarCompatActivity {
    public final static int PERMISSION_REQUEST_LOCAL_BACKUP = 101;
    public final static int PERMISSION_REQUEST_LOCAL_RESTORE = 102;
    public final static int PERMISSION_REQUEST_CLOUD_RESTORE = 103;
    public final static int PERMISSION_REQUEST_CLOUD_BACKUP = 104;
    public final static int PERMISSION_REQUEST_DOCUMENT_LOAD = 105;

    private Fragment getSettingsFragment() {
        return getSupportFragmentManager().findFragmentById(android.R.id.content);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Display the fragment as the main content./
        if (getSettingsFragment() == null)
            getSupportFragmentManager().beginTransaction()
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
                case PERMISSION_REQUEST_CLOUD_RESTORE:
                    settingsFragment.executeCloudRestore();
                    break;
                case PERMISSION_REQUEST_CLOUD_BACKUP:
                    settingsFragment.executeCloudBackup();
                case PERMISSION_REQUEST_DOCUMENT_LOAD:
                    settingsFragment.executeDocumentLoad();
                default:
                    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        } else
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        GDHelper.handleActivityResult(this, requestCode, resultCode, data);
    }
}
