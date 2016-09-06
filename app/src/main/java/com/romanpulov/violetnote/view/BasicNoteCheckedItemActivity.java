package com.romanpulov.violetnote.view;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.db.DBNoteManager;
import com.romanpulov.violetnote.model.BasicNoteItemA;
import com.romanpulov.violetnote.view.core.ActionBarCompatActivity;
import com.romanpulov.violetnote.view.core.BasicNoteDataPasswordActivity;

public class BasicNoteCheckedItemActivity extends BasicNoteDataPasswordActivity {

    @Override
    protected int getFragmentContainerId() {
        return R.id.fragment_id;
    }

    @Override
    protected void refreshFragment() {
        Fragment fragment = BasicNoteCheckedItemFragment.newInstance(mBasicNoteData);
        removeFragment().beginTransaction().add(getFragmentContainerId(), fragment).commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //for ToolBar
        setContentView(R.layout.activity_basic_note_checked_item);

        //setup ToolBar instead of ActionBar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setupActionBar();

        refreshFragment();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_checked_item, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Fragment fragment = getFragment();
        if (fragment != null) {
            switch (item.getItemId()) {
                case R.id.action_add:
                    ((BasicNoteCheckedItemFragment) fragment).showAddLayout();
                    return true;
                case R.id.action_check_all:
                    ((BasicNoteCheckedItemFragment) fragment).updateNoteDataChecked(true);
                    return true;
                case R.id.action_uncheck_all:
                    ((BasicNoteCheckedItemFragment) fragment).updateNoteDataChecked(false);
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
