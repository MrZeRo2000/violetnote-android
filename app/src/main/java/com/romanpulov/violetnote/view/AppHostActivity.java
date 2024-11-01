package com.romanpulov.violetnote.view;

import android.os.Bundle;

import com.romanpulov.violetnote.databinding.ActivityAppHostBinding;
import com.romanpulov.violetnote.view.core.ActionBarCompatActivity;

public class AppHostActivity extends ActionBarCompatActivity {
    private ActivityAppHostBinding binding;

    @Override
    protected void setupLayout() {
        binding = ActivityAppHostBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.appbar.toolbar);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            DashboardFragment fragment = new DashboardFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(binding.content.mainContent.getId(), fragment)
                    .commit();
        }
        /*
        ViewCompat.setOnApplyWindowInsetsListener(binding.content.mainContent, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

         */
    }
}