package com.romanpulov.violetnote.view;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.widget.ActionMenuView;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.db.manager.DBNoteManager;
import com.romanpulov.violetnote.model.BasicNoteA;
import com.romanpulov.violetnote.model.BasicNoteDataA;
import com.romanpulov.violetnote.model.BasicNoteGroupA;
import com.romanpulov.violetnote.view.core.ActionBarCompatActivity;
import com.romanpulov.violetnote.view.core.PasswordActivity;

public class BasicNoteActivity extends ActionBarCompatActivity implements BasicNoteFragment.OnBasicNoteFragmentInteractionListener, BottomToolbarProvider {
    public static final String NOTE = "Note";

    private BasicNoteGroupA mBasicNoteGroup;

    private BasicNoteFragment mFragment;
    private ActionMenuView mBottomToolbar;

    @Override
    protected void setupLayout() {
        //for ToolBar
        setContentView(R.layout.activity_toolbar_fragment_toolbar);

        //setup ToolBar instead of ActionBar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getTitle());
        setSupportActionBar(toolbar);

        //bottom toolbar
        mBottomToolbar = findViewById(R.id.toolbar_bottom);
        if (mBottomToolbar != null) {
            getMenuInflater().inflate(R.menu.menu_listitem_bottom_move_actions, mBottomToolbar.getMenu());
            mBottomToolbar.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        if ((extras != null) && ((mBasicNoteGroup = (BasicNoteGroupA)extras.get(BasicNoteGroupA.BASIC_NOTE_GROUP_DATA)) != null)) {

            setTitle(mBasicNoteGroup.getGroupName());


            FragmentManager fm = getSupportFragmentManager();

            DBNoteManager noteManager = new DBNoteManager(this);

            mFragment = getFragment();
            if (mFragment == null) {
                mFragment = BasicNoteFragment.newInstance(mBasicNoteGroup);
                fm.beginTransaction().replace(R.id.fragment_id, mFragment).commit();
            } else {
                //mFragment.refreshList(noteManager);
            }
        }
    }

    private BasicNoteFragment getFragment() {
        FragmentManager fm = getSupportFragmentManager();
        return (BasicNoteFragment)fm.findFragmentById(R.id.fragment_id);
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
        //mFragment.refreshList(new DBNoteManager(this));
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mFragment != null) {
            mFragment.hideAddLayout();
        }
    }

    /*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_add) {
            Intent intent = new Intent(this, BasicNoteEditActivity.class);
            intent.putExtra(BasicNoteGroupA.BASIC_NOTE_GROUP_DATA, mBasicNoteGroup);
            startActivityForResult(intent, 0);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ((data != null) && (data.getComponent() != null) && (data.getComponent().getClassName().equals(BasicNoteEditActivity.class.getName()))) {
            BasicNoteA newNote = data.getParcelableExtra(NOTE);
            if (newNote != null) {
                mFragment.performAddAction(newNote);
            }
        }
    }

     */

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

    @Override
    public ActionMenuView getBottomToolbar() {
        return mBottomToolbar;
    }
}
