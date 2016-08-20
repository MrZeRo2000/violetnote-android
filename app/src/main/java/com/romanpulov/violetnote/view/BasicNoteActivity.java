package com.romanpulov.violetnote.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.db.DBBasicNoteHelper;
import com.romanpulov.violetnote.db.DBNoteManager;
import com.romanpulov.violetnote.model.BasicNoteA;
import com.romanpulov.violetnote.view.core.ActionBarCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class BasicNoteActivity extends ActionBarCompatActivity {
    public static final String NOTE_LIST = "NoteList";

    private ArrayList<BasicNoteA> mNoteList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DBNoteManager noteManager = new DBNoteManager(this);
        mNoteList = noteManager.queryNotes();

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = BasicNoteActivityFragment.newInstance(mNoteList);
        fm.beginTransaction().add(android.R.id.content, fragment).commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_basic_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                Toast.makeText(this, "Action add", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
