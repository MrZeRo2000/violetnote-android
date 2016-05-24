package com.romanpulov.violetnote;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import java.util.ArrayList;

public class NoteActivity extends ActionBarCompatActivity implements NoteFragment.OnListFragmentInteractionListener{

    private PassCategoryA mPassCategoryItem;
    private ArrayList<PassNoteA> mPassNoteData;

    private static void log(String message) {
        Log.d("NoteActivity", message);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        log("OnCreate saveInstanceState=" + savedInstanceState);
        if (savedInstanceState != null) {
            log("getInt=" + savedInstanceState.getInt("TestInt"));
        }

        setContentView(R.layout.activity_fragment_note);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        mPassCategoryItem = getIntent().getParcelableExtra(MainActivity.PASS_CATEGORY_ITEM);
        mPassNoteData = getIntent().getParcelableArrayListExtra(MainActivity.PASS_NOTE_DATA);

        log("PassCategoryItem:" + mPassCategoryItem);
        log("PassNoteData:" + mPassNoteData);

        if ((fragment == null) && (mPassCategoryItem != null) && (mPassNoteData != null)) {
            fragment = NoteFragment.newInstance(mPassCategoryItem, mPassNoteData);
            fm.beginTransaction().add(R.id.note_fragment_container, fragment).commit();
        }
    }

    @Override
    public void onListFragmentInteraction(PassNoteA item) {
        Intent intent = new Intent(this, NoteDetailsActivity.class);
        intent.putExtra(MainActivity.PASS_NOTE_DATA, item);
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



    @Override
    protected void onSaveInstanceState(Bundle outState) {
        log("saving instance state :" + mPassCategoryItem + ", " + mPassNoteData);
        outState.putParcelable(MainActivity.PASS_CATEGORY_ITEM, mPassCategoryItem);
        outState.putParcelableArrayList(MainActivity.PASS_NOTE_DATA, mPassNoteData);
        outState.putInt("TestInt", 1);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        log("restoring instance state");
//        mPassCategoryItem = savedInstanceState.getParcelable(PASS_CATEGORY_ITEM);
//        mPassNoteData = savedInstanceState.getParcelableArrayList(PASS_NOTE_DATA);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Workaround for issue with back button in toolbar default behavior
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        } else
            return super.onOptionsItemSelected(item);
    }
}
