package com.romanpulov.violetnote.view.core;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.view.DashboardActivity;

/**
 * Created by rpulov on 24.05.2016.
 * Abstract base class for Activity
 */
public abstract class ActionBarCompatActivity extends AppCompatActivity {

    /**
     * Setup for ActionBar or ToolBar
     */
    protected void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setIcon(R.mipmap.ic_launcher);
            actionBar.setDisplayUseLogoEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            // disable arrow back for launch activity
            if (!(this.getClass().getName().equals(DashboardActivity.class.getName())))
                actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupActionBar();
    }
}

