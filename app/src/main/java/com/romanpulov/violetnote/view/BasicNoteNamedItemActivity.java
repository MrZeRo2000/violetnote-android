package com.romanpulov.violetnote.view;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.view.core.BasicNoteDataPasswordActivity;

public class BasicNoteNamedItemActivity extends BasicNoteDataPasswordActivity {

    @Override
    protected int getFragmentContainerId() {
        return R.id.fragment_id;
    }

    @Override
    protected void refreshFragment() {
        if (!getProgress()) {
            FragmentManager fm = getSupportFragmentManager();

            Fragment fragment = fm.findFragmentById(getFragmentContainerId());
            //BasicNoteCheckedItemFragment fragment = (BasicNoteCheckedItemFragment) fm.findFragmentById(getFragmentContainerId());
            if((fragment == null) || (!(fragment instanceof BasicNoteCheckedItemFragment))) {
                Fragment newFragment = BasicNoteNamedItemFragment.newInstance(mBasicNoteData, this);
                removeFragment().beginTransaction().add(getFragmentContainerId(), newFragment).commit();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //for ToolBar
        setContentView(R.layout.activity_basic_note_named_item);

        //setup ToolBar instead of ActionBar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(mBasicNoteData.getNote().getTitle());
        setSupportActionBar(toolbar);
        setupActionBar();

        refreshFragment();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_action, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Fragment fragment = getFragment();
        if (fragment != null) {
            switch (item.getItemId()) {
                case R.id.action_add:
                    if (fragment instanceof BasicNoteNamedItemFragment)
                        ((BasicNoteNamedItemFragment)fragment).performAddAction();
                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }
        }  else
            return super.onOptionsItemSelected(item);
    }
}
