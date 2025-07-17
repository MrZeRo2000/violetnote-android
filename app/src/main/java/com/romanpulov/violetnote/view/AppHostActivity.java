package com.romanpulov.violetnote.view;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.cloud.GDHelper;
import com.romanpulov.violetnote.databinding.ActivityAppHostBinding;
import com.romanpulov.violetnote.view.core.ActionBarCompatActivity;
import com.romanpulov.violetnote.view.helper.DisplayMessageHelper;
import com.romanpulov.violetnote.view.helper.PermissionRequestHelper;

public class AppHostActivity extends ActionBarCompatActivity {

    public final static int PERMISSION_REQUEST_NOTIFICATIONS = 101;

    private ActivityAppHostBinding binding;
    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void setupLayout() {
        binding = ActivityAppHostBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.appbar.toolbar);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        NavHostFragment navHostFragment = binding.mainNavContent.getFragment();
        NavController navController = navHostFragment.getNavController();

        mAppBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);

        // icon only for dashboard, otherwise only text
        navController.addOnDestinationChangedListener((navController1, navDestination, bundle) -> {
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                if (navDestination.getId() == R.id.nav_dashboard) {
                    actionBar.setIcon(R.mipmap.ic_launcher);
                } else {
                    actionBar.setIcon(null);
                }
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            PermissionRequestHelper notificationRequestHelper =
                    new PermissionRequestHelper(
                            this,
                            Manifest.permission.POST_NOTIFICATIONS);
            if (!notificationRequestHelper.isPermissionGranted()) {
                notificationRequestHelper.requestPermission(PERMISSION_REQUEST_NOTIFICATIONS);
            }
        }

        GDHelper.getInstance().registerActivity(this);
    }

    @Override
    public boolean onSupportNavigateUp() {
        if (getOnBackPressedDispatcher().hasEnabledCallbacks()) {
            getOnBackPressedDispatcher().onBackPressed();
            return true;
        } else {
            NavController navController = Navigation.findNavController(this, binding.mainNavContent.getId());
            return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                    || super.onSupportNavigateUp();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if ((PermissionRequestHelper.isGrantResultSuccessful(grantResults)) &&
                (requestCode == PERMISSION_REQUEST_NOTIFICATIONS)) {
            DisplayMessageHelper.displayInfoMessage(this, R.string.ui_info_notification_permissions_granted);
        }
    }
}