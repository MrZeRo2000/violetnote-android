package com.romanpulov.violetnote.view.core;

import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.view.AppHostActivity;

/**
 * Created by rpulov on 24.05.2016.
 * Abstract base class for Activity
 */
public abstract class ActionBarCompatActivity extends AppCompatActivity {
    protected abstract void setupLayout();

    /**
     * Setup for ActionBar or ToolBar
     */
    protected void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // disable arrow back for launch activity
            if (this.getClass().getName().equals(AppHostActivity.class.getName())) {
                actionBar.setIcon(R.mipmap.ic_launcher);
                actionBar.setDisplayUseLogoEnabled(true);
                actionBar.setDisplayShowHomeEnabled(true);
            } else {
                actionBar.setDisplayHomeAsUpEnabled(true);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupLayout();
        setupActionBar();
    }
}

