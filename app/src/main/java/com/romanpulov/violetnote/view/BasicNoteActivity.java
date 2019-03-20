package com.romanpulov.violetnote.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.db.manager.DBNoteManager;
import com.romanpulov.violetnote.model.BasicNoteA;
import com.romanpulov.violetnote.model.BasicNoteDataA;
import com.romanpulov.violetnote.model.BasicNoteGroupA;
import com.romanpulov.violetnote.view.core.ActionBarCompatActivity;
import com.romanpulov.violetnote.view.core.PasswordActivity;

public class BasicNoteActivity extends ActionBarCompatActivity implements BasicNoteFragment.OnBasicNoteFragmentInteractionListener {
    public static final String NOTE = "Note";

    private BasicNoteGroupA mBasicNoteGroup;
    private BasicNoteFragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        if ((extras != null) && ((mBasicNoteGroup = (BasicNoteGroupA)extras.get(BasicNoteGroupA.BASIC_NOTE_GROUP_DATA)) != null)) {

            setTitle(mBasicNoteGroup.getGroupName());

            FragmentManager fm = getSupportFragmentManager();

            DBNoteManager noteManager = new DBNoteManager(this);

            mFragment = (BasicNoteFragment)fm.findFragmentById(android.R.id.content);
            if (mFragment == null) {
                mFragment = BasicNoteFragment.newInstance(noteManager, mBasicNoteGroup);
                fm.beginTransaction().replace(android.R.id.content, mFragment).commit();
            } else {
                mFragment.refreshList(noteManager);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_basic_note, menu);
        return true;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        // to handle back button from underlying activity
        // mFragment.refreshList(new DBNoteManager(this));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                Intent intent = new Intent(this, BasicNoteEditActivity.class);
                intent.putExtra(BasicNoteGroupA.BASIC_NOTE_GROUP_DATA, mBasicNoteGroup);
                startActivityForResult(intent, 0);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ((data != null) && (data.getComponent() != null) && (data.getComponent().getClassName().equals(BasicNoteEditActivity.class.getName()))) {
            BasicNoteA newNote = data.getParcelableExtra(NOTE);
            if (newNote != null) {
                mFragment.performAddAction(newNote);
            }
        }
    }

    @Override
    public void onBasicNoteFragmentInteraction(BasicNoteA item) {
        DBNoteManager noteManager = new DBNoteManager(this);
        BasicNoteDataA noteData = noteManager.mBasicNoteDAO.createNoteDataFromNote(mBasicNoteGroup, item);

        Intent intent = null;

        //select intent
        switch(item.getNoteType()) {
            case BasicNoteA.NOTE_TYPE_CHECKED:
                intent = new Intent(this, BasicNoteCheckedItemActivity.class);
                break;
            case BasicNoteA.NOTE_TYPE_NAMED:
                intent = new Intent(this, BasicNoteNamedItemActivity.class);
        }

        if (intent != null) {
            intent.putExtra(PasswordActivity.PASS_DATA, noteData);
            PasswordActivity.getPasswordValidityChecker().resetPeriod();
            startActivityForResult(intent, 0);
        }
    }
}
