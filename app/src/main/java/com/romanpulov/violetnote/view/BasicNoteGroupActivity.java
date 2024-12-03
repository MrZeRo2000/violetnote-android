package com.romanpulov.violetnote.view;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.widget.ActionMenuView;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.model.BasicNoteGroupA;
import com.romanpulov.violetnote.view.core.ActionBarCompatActivity;

public class BasicNoteGroupActivity extends ActionBarCompatActivity{
    @Override
    protected void setupLayout() {
        //for ToolBar
        setContentView(R.layout.activity_toolbar_fragment_toolbar);

        //setup ToolBar instead of ActionBar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getTitle());
        setSupportActionBar(toolbar);

        //bottom toolbar
        ActionMenuView mBottomToolbar = findViewById(R.id.toolbar_bottom);
        if (mBottomToolbar != null) {
            mBottomToolbar.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_action, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_add) {
            startActivityForResult(new Intent(this, BasicNoteGroupEditActivity.class), BasicNoteGroupFragment.ACTIVITY_REQUEST_INSERT);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        BasicNoteGroupA noteGroup;
        if ((requestCode == BasicNoteGroupFragment.ACTIVITY_REQUEST_INSERT) && (resultCode == RESULT_OK) && (data != null) && ((noteGroup = data.getParcelableExtra(BasicNoteGroupA.BASIC_NOTE_GROUP_DATA)) != null)) {
            FragmentManager fm = getSupportFragmentManager();

            BasicNoteGroupFragment fragment = (BasicNoteGroupFragment)fm.findFragmentById(R.id.fragment_id);

            if (fragment != null) {
                fragment.performAddAction(noteGroup);
            }
        }
    }
}
