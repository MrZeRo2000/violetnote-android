package com.romanpulov.violetnote.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.MenuItem;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.model.BasicNoteValueDataA;
import com.romanpulov.violetnote.view.core.ActionBarCompatActivity;

public class BasicNoteValueActivity extends ActionBarCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setContentView(R.layout.activity_basic_note_value);

        BasicNoteValueDataA noteValueData = getIntent().getParcelableExtra(BasicNoteValueDataA.class.getName());
        if (noteValueData != null) {
            FragmentManager fm = getSupportFragmentManager();
            BasicNoteValueFragment fragment = BasicNoteValueFragment.newInstance(noteValueData);
            fm.beginTransaction().add(android.R.id.content, fragment).commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Workaround for issue with back button in toolbar default behavior
        if (item.getItemId() == android.R.id.home) {
            //updateResult();
            finish();
            return true;
        } else
            return super.onOptionsItemSelected(item);
    }
}
