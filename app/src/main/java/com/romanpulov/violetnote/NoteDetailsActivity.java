package com.romanpulov.violetnote;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;

public class NoteDetailsActivity extends ActionBarCompatActivity implements NoteDetailsFragment.OnListFragmentInteractionListener {

    private PassNoteA mPassNoteData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_note_details);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.note_details_fragment_container);
        //PassCategoryA passCategoryItem = getIntent().getParcelableExtra(PASS_CATEGORY_ITEM);
        //ArrayList<PassNoteA> passNoteData = getIntent().getParcelableArrayListExtra(PASS_NOTE_DATA);

        mPassNoteData = getIntent().getParcelableExtra(MainActivity.PASS_NOTE_DATA);

        if (fragment == null) {
            fragment = NoteDetailsFragment.newInstance(mPassNoteData);
            fm.beginTransaction().add(R.id.note_details_fragment_container, fragment).commit();
        }
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

    @Override
    public void onListFragmentInteraction(PassNoteA.AttrItem item) {

    }
}
