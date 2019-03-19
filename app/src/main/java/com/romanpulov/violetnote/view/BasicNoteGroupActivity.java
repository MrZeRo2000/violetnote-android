package com.romanpulov.violetnote.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.db.manager.DBNoteManager;
import com.romanpulov.violetnote.model.BasicNoteGroupA;
import com.romanpulov.violetnote.view.core.ActionBarCompatActivity;
import com.romanpulov.violetnote.view.core.RecyclerViewHelper;

import java.util.List;

public class BasicNoteGroupActivity extends ActionBarCompatActivity implements BasicNoteGroupFragment.OnBasicNoteGroupFragmentInteractionListener {

    private List<BasicNoteGroupA> mBasicNoteGroupList;
    private DBNoteManager mNoteManager;
    private RecyclerView mRecyclerView;

    @Override
    public void onBasicNoteGroupFragmentInteraction(BasicNoteGroupA item) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FragmentManager fm = getSupportFragmentManager();

        DBNoteManager noteManager = new DBNoteManager(this);

        BasicNoteGroupFragment fragment = (BasicNoteGroupFragment)fm.findFragmentById(android.R.id.content);
        if (fragment == null) {
            fragment = BasicNoteGroupFragment.newInstance();
            fm.beginTransaction().replace(android.R.id.content, fragment).commit();
        } else {
            fragment.refreshList(noteManager);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_action, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                startActivityForResult(new Intent(this, BasicNoteGroupEditActivity.class), 0);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        BasicNoteGroupA noteGroup;
        if ((data != null) && ((noteGroup = data.getParcelableExtra(BasicNoteGroupA.BASIC_NOTE_GROUP_DATA)) != null)) {
            FragmentManager fm = getSupportFragmentManager();

            BasicNoteGroupFragment fragment = (BasicNoteGroupFragment)fm.findFragmentById(android.R.id.content);

            if (fragment != null) {
                fragment.performAddAction(noteGroup);
            }
        }
    }
}
