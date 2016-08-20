package com.romanpulov.violetnote.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

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

}
