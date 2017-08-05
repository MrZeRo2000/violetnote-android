package com.romanpulov.violetnote.view;

import android.app.Fragment;
import android.os.Bundle;

import com.romanpulov.violetnote.view.core.ActionBarCompatActivity;

public class SettingsActivity extends ActionBarCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Display the fragment as the main content.
        Fragment settingsFragment = getFragmentManager().findFragmentById(android.R.id.content);

        if (settingsFragment == null)
            getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }
}
