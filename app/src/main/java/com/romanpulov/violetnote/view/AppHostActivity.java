package com.romanpulov.violetnote.view;

import android.os.Bundle;
import com.romanpulov.violetnote.databinding.ActivityAppHostBinding;
import com.romanpulov.violetnote.view.core.ActionBarCompatActivity;

public class AppHostActivity extends ActionBarCompatActivity {

    @Override
    protected void setupLayout() {
        ActivityAppHostBinding  binding = ActivityAppHostBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.appbar.toolbar);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}