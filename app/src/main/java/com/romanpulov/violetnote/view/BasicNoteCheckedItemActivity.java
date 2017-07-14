package com.romanpulov.violetnote.view;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.db.DBNoteManager;
import com.romanpulov.violetnote.view.core.BasicNoteDataPasswordActivity;

public class BasicNoteCheckedItemActivity extends BasicNoteDataPasswordActivity {

    private BasicNoteCheckedItemFragment mFragment;

    @Override
    protected int getFragmentContainerId() {
        return R.id.fragment_id;
    }

    @Override
    protected void refreshFragment() {
        /*
            mFragment = BasicNoteCheckedItemFragment.newInstance(mBasicNoteData);
            removeFragment().beginTransaction().add(getFragmentContainerId(), mFragment).commit();
        */
        FragmentManager fm = getSupportFragmentManager();

        mFragment = (BasicNoteCheckedItemFragment)fm.findFragmentById(getFragmentContainerId());
        if (mFragment == null) {
            mFragment = BasicNoteCheckedItemFragment.newInstance(mBasicNoteData);
            fm.beginTransaction().replace(getFragmentContainerId(), mFragment).commit();
        } else {
            mFragment.refreshList(new DBNoteManager(this));
            }
    }

    @Override
    protected void updatePassword(String password) {
        super.updatePassword(password);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //for ToolBar
        setContentView(R.layout.activity_basic_note_checked_item);

        //setup ToolBar instead of ActionBar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(mBasicNoteData.getNote().getTitle());
        setSupportActionBar(toolbar);
        setupActionBar();

        refreshFragment();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Fragment fragment = getFragment();
        if ((fragment != null) && (fragment instanceof BasicNoteCheckedItemFragment)) {
            ((BasicNoteCheckedItemFragment) fragment).hideAddLayout();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_checked_item, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Fragment fragment = getFragment();
        if ((fragment != null) && (fragment instanceof BasicNoteCheckedItemFragment)) {
            switch (item.getItemId()) {
                case R.id.action_add:
                    ((BasicNoteCheckedItemFragment) fragment).showAddLayout();
                    return true;
                case R.id.action_check_all:
                    ((BasicNoteCheckedItemFragment) fragment).performUpdateChecked(true);
                    return true;
                case R.id.action_uncheck_all:
                    ((BasicNoteCheckedItemFragment) fragment).performUpdateChecked(false);
                    return true;
                case R.id.action_checkout:
                    ((BasicNoteCheckedItemFragment) fragment).checkOut();
                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }
        } else
            return super.onOptionsItemSelected(item);
    }
}
