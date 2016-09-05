package com.romanpulov.violetnote.view;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.model.BasicNoteItemA;
import com.romanpulov.violetnote.view.core.ActionBarCompatActivity;
import com.romanpulov.violetnote.view.core.BasicNoteDataPasswordActivity;

public class BasicNoteCheckedItemActivity extends BasicNoteDataPasswordActivity {

    @Override
    protected void refreshFragment() {
        Fragment fragment = BasicNoteCheckedItemFragment.newInstance(mBasicNoteData);
        removeFragment().beginTransaction().add(android.R.id.content, fragment).commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        refreshFragment();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_checked_item, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                Fragment fragment = getFragment();
                if (fragment != null) {
                    ((BasicNoteCheckedItemFragment)fragment).showAddLayout();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
