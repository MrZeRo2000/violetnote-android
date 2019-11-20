package com.romanpulov.violetnote.view;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.db.manager.DBHManager;
import com.romanpulov.violetnote.model.BasicNoteItemA;
import com.romanpulov.violetnote.view.core.ActionBarCompatActivity;

public class BasicHEventNamedItemActivity extends ActionBarCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //for ToolBar
        setContentView(R.layout.activity_toolbar_fragment_toolbar);

        //setup ToolBar instead of ActionBar
        Toolbar toolbar = findViewById(R.id.toolbar);
        //toolbar.setTitle(mBasicNoteData.getNote().getTitle());
        setSupportActionBar(toolbar);
        setupActionBar();

        //bottom toolbar
        ActionMenuView bottomToolbar = findViewById(R.id.toolbar_bottom);
        if (bottomToolbar != null) {
            bottomToolbar.setVisibility(View.GONE);
        }

        BasicNoteItemA noteItem = getIntent().getParcelableExtra(BasicNoteItemA.class.getName());
        if (noteItem != null) {
            setTitle(getString(R.string.title_activity_basic_history_event_named_item, noteItem.getName()));

            FragmentManager fm = getSupportFragmentManager();

            BasicHEventNamedItemFragment fragment = (BasicHEventNamedItemFragment)fm.findFragmentById(R.id.fragment_id);
            if (fragment == null) {
                fragment = BasicHEventNamedItemFragment.newInstance(noteItem);
                fm.beginTransaction().replace(R.id.fragment_id, fragment).commit();
            } else {
                fragment.refreshList(new DBHManager(this));
            }
        }

    }
}
