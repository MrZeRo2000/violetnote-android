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

public class NoteDetailsActivity extends PasswordActivity implements NoteDetailsFragment.OnListFragmentInteractionListener {

    @Override
    protected int getFragmentContainerId() {
        return R.id.note_details_fragment_container;
    }

    @Override
    protected void refreshFragment() {
        Fragment fragment = NoteDetailsFragment.newInstance(mPassDataA);
        removeFragment().beginTransaction().add(getFragmentContainerId(), fragment).commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_note_details);

        refreshFragment();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Workaround for issue with back button in toolbar default behavior
        if (item.getItemId() == android.R.id.home) {
            updateResult();
            finish();
            return true;
        } else
            return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListFragmentInteraction(PassNoteA.AttrItem item) {

    }

    @Override
    public void onBackPressed() {
        updateResult();
        finish();
        super.onBackPressed();
    }
}
