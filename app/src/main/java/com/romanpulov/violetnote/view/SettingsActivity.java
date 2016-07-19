package com.romanpulov.violetnote.view;

import android.os.Bundle;

import com.romanpulov.violetnote.view.core.ActionBarCompatActivity;

public class SettingsActivity extends ActionBarCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }
}
