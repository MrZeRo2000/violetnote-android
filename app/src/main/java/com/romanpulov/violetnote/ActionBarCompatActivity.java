package com.romanpulov.violetnote;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * Created by rpulov on 24.05.2016.
 */
public class ActionBarCompatActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setIcon(R.mipmap.ic_launcher);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        /*
        -- hopefully this will not be required
        if (!(this.getClass().getName().equals(MainActivity.class.getName()))){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        */
    }
}

