package com.romanpulov.violetnote;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

public class NoteActivity extends AppCompatActivity implements NoteFragment.OnListFragmentInteractionListener{

    private PassCategoryA mPassCategoryItem;
    private ArrayList<PassNoteA> mPassNoteData;

    private static void log(String message) {
        Log.d("NoteActivity", message);
    }

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

        mPassCategoryItem = getIntent().getParcelableExtra(PASS_CATEGORY_ITEM);
        mPassNoteData = getIntent().getParcelableArrayListExtra(PASS_NOTE_DATA);

        log("saveInstanceState=" + savedInstanceState);
        log("PassCategoryItem:" + mPassCategoryItem);
        log("PassNoteData:" + mPassNoteData);

        if (fragment == null) {
            fragment = NoteFragment.newInstance(mPassCategoryItem, mPassNoteData);
            fm.beginTransaction().add(R.id.note_fragment_container, fragment).commit();
        }
    }

    @Override
    public void onListFragmentInteraction(PassNoteA item) {
        Intent intent = new Intent(this, NoteDetailsActivity.class);
        intent.putExtra(NoteActivity.PASS_CATEGORY_ITEM, item);
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        log("Pause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        log("Resume");
    }

    /*
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        log("saving instance state");
        outState.putParcelable(PASS_CATEGORY_ITEM, mPassCategoryItem);
        outState.putParcelableArrayList(PASS_NOTE_DATA, mPassNoteData);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        log("restoring instance state");
//        mPassCategoryItem = savedInstanceState.getParcelable(PASS_CATEGORY_ITEM);
//        mPassNoteData = savedInstanceState.getParcelableArrayList(PASS_NOTE_DATA);
    }

*/

}
