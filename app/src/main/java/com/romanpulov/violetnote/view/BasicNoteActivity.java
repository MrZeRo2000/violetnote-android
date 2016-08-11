package com.romanpulov.violetnote.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.view.core.ActionBarCompatActivity;

public class BasicNoteActivity extends ActionBarCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = new BasicNoteActivityFragment();
        fm.beginTransaction().add(android.R.id.content, fragment).commit();

    }

}
