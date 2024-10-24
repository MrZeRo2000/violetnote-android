package com.romanpulov.violetnote.view;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.cloud.GDHelper;
import com.romanpulov.violetnote.databinding.ActivitySettingsBinding;
import com.romanpulov.violetnote.view.core.ActionBarCompatActivity;
import com.romanpulov.violetnote.view.helper.DisplayMessageHelper;

public class SettingsActivity extends ActionBarCompatActivity {
    public final static int PERMISSION_REQUEST_LOCAL_BACKUP = 101;
    public final static int PERMISSION_REQUEST_LOCAL_RESTORE = 102;
    public final static int PERMISSION_REQUEST_CLOUD_RESTORE = 103;
    public final static int PERMISSION_REQUEST_CLOUD_BACKUP = 104;
    public final static int PERMISSION_REQUEST_DOCUMENT_LOAD = 105;

    private ActivitySettingsBinding binding;

    private Fragment getSettingsFragment() {
        return getSupportFragmentManager().findFragmentById(binding.settings.getId());
    }

    @Override
    protected void setupLayout() {
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Display the fragment as the main content./
        if (getSettingsFragment() == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(binding.settings.getId(), new SettingsFragment())
                    .commit();
        }

        // Notification permission
        // According to https://developer.android.com/training/permissions/requesting#java
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityResultLauncher<String> requestPermissionLauncher =
                        registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                            if (isGranted) {
                                DisplayMessageHelper.displayInfoMessage(this, R.string.ui_info_notification_permissions_granted);
                            }
                        });
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        SettingsFragment settingsFragment = (SettingsFragment) getSettingsFragment();

        if ((settingsFragment != null) && ((grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED))) {
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
        GDHelper.handleActivityResult(this, requestCode, data);
    }
}
