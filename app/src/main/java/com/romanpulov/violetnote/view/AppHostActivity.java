package com.romanpulov.violetnote.view;

import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.databinding.ActivityAppHostBinding;
import com.romanpulov.violetnote.view.core.ActionBarCompatActivity;

public class AppHostActivity extends ActionBarCompatActivity {
    private static final String TAG = AppHostActivity.class.getSimpleName();

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
}