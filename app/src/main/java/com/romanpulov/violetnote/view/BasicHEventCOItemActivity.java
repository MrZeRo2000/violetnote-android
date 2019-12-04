package com.romanpulov.violetnote.view;

import android.os.Bundle;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.widget.ActionMenuView;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.db.manager.DBHManager;
import com.romanpulov.violetnote.model.BasicNoteA;
import com.romanpulov.violetnote.view.core.ActionBarCompatActivity;

public class BasicHEventCOItemActivity extends ActionBarCompatActivity {

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

        BasicNoteA note = getIntent().getParcelableExtra(getClass().getName());
        if (note != null) {
            setTitle(getString(R.string.title_activity_basic_history_event_co_item, note.getTitle()));

            FragmentManager fm = getSupportFragmentManager();

            BasicHEventCOItemFragment fragment = (BasicHEventCOItemFragment)fm.findFragmentById(R.id.fragment_id);
            if (fragment == null) {
                fragment = BasicHEventCOItemFragment.newInstance(note);
                fm.beginTransaction().replace(R.id.fragment_id, fragment).commit();
            } else {
                fragment.refreshList(new DBHManager(this));
            }

        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch ((item.getItemId())) {
            // Workaround for issue with back button in toolbar default behavior
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
