package com.romanpulov.violetnote;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class NoteActivity extends AppCompatActivity implements NoteFragment.OnListFragmentInteractionListener{

    public static final String PASS_CATEGORY_ITEM = "PassCategoryItem";
    public static final String PASS_NOTE_DATA = "PassNoteData";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_note);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setIcon(R.mipmap.ic_launcher);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
        PassCategoryA passCategoryItem = getIntent().getParcelableExtra(PASS_CATEGORY_ITEM);
        ArrayList<PassNoteA> passNoteData = getIntent().getParcelableArrayListExtra(PASS_NOTE_DATA);

        actionBar.setTitle(passCategoryItem.getCategoryName());

        if (fragment == null) {
            fragment = NoteFragment.newInstance(passNoteData);
            fm.beginTransaction().add(R.id.note_fragment_container, fragment).commit();
        }
    }

    @Override
    public void onListFragmentInteraction(PassNoteA item) {

    }
}
