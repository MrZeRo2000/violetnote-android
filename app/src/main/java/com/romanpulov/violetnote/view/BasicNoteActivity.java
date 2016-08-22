package com.romanpulov.violetnote.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
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
    public static final String NOTE = "Note";

    private ArrayList<BasicNoteA> mNoteList;
    private BasicNoteActivityFragment mFragment;
    private DBNoteManager mNoteManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mNoteManager = new DBNoteManager(this);
        mNoteList = mNoteManager.queryNotes();

        FragmentManager fm = getSupportFragmentManager();
        mFragment = BasicNoteActivityFragment.newInstance(mNoteList);
        fm.beginTransaction().add(android.R.id.content, mFragment).commit();
    }

    class ActionBarCallBack implements ActionMode.Callback {

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // TODO Auto-generated method stub
            mode.getMenuInflater().inflate(R.menu.menu_listitem_generic_actions, menu);
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            // TODO Auto-generated method stub

        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            // TODO Auto-generated method stub

            mode.setTitle("CheckBox is Checked");
            return false;
        }
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
                //Intent intent = new Intent(this, BasicNoteEditActivity.class);
                //startActivityForResult(intent, 0);
                this.startSupportActionMode(new ActionBarCallBack());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ((data != null) && (data.getComponent().getClassName().equals(BasicNoteEditActivity.class.getName()))) {
            BasicNoteA newNote = data.getParcelableExtra(NOTE);
            if (newNote != null) {
                if ((new DBNoteManager(this)).insertNote(newNote) != -1) {
                    mNoteList.clear();
                    mNoteList.addAll(mNoteManager.queryNotes());
                    mFragment.updateList();
                }
            }
        }



        //Toast.makeText(this, "requestCode=" + requestCode + ", resultCode=" + resultCode + ", intent=" + data, Toast.LENGTH_SHORT).show();
        //Toast.makeText(this, "component=" + data.getComponent().getClassName(), Toast.LENGTH_SHORT).show();;
        //super.onActivityResult(requestCode, resultCode, data);
    }
}
