package com.romanpulov.violetnote.view;

import android.os.Bundle;
import androidx.fragment.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.db.manager.DBNoteManager;
import com.romanpulov.violetnote.model.BasicNoteValueDataA;
import com.romanpulov.violetnote.view.core.ActionBarCompatActivity;

public abstract class BasicNoteValueActivity extends ActionBarCompatActivity {
    private BasicNoteValueFragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        BasicNoteValueDataA noteValueData = getIntent().getParcelableExtra(BasicNoteValueDataA.class.getName());
        if (noteValueData != null) {
            setTitle(noteValueData.getNote().getTitle() + " " + getTitle());

            FragmentManager fm = getSupportFragmentManager();

            mFragment = (BasicNoteValueFragment)fm.findFragmentById(android.R.id.content);
            if (mFragment == null) {
                mFragment = BasicNoteValueFragment.newInstance(noteValueData);
                fm.beginTransaction().replace(android.R.id.content, mFragment).commit();
            } else {
                mFragment.refreshList(new DBNoteManager(this));
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_action, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = (item.getItemId());// Workaround for issue with back button in toolbar default behavior
        if (itemId == android.R.id.home) {
            finish();
            return true;
            // add action
        } else if (itemId == R.id.action_add) {
            mFragment.showAddLayout();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
