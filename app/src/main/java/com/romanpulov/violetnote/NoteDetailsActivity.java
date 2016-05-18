package com.romanpulov.violetnote;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

public class NoteDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_note_details);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setIcon(R.mipmap.ic_launcher);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.note_details_fragment_container);
        //PassCategoryA passCategoryItem = getIntent().getParcelableExtra(PASS_CATEGORY_ITEM);
        //ArrayList<PassNoteA> passNoteData = getIntent().getParcelableArrayListExtra(PASS_NOTE_DATA);

        if (fragment == null) {
            fragment = NoteDetailsFragment.newInstance();
            fm.beginTransaction().add(R.id.note_details_fragment_container, fragment).commit();
        }
    }
}
