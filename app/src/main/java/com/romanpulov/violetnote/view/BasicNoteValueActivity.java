package com.romanpulov.violetnote.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.db.DBNoteManager;
import com.romanpulov.violetnote.model.BasicNoteValueDataA;
import com.romanpulov.violetnote.view.core.ActionBarCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class BasicNoteValueActivity extends ActionBarCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setContentView(R.layout.activity_basic_note_value);

        BasicNoteValueDataA noteValueData = getIntent().getParcelableExtra(BasicNoteValueDataA.class.getName());
        if (noteValueData != null) {
            setTitle(noteValueData.getNote().getTitle() + " " + getTitle());

            FragmentManager fm = getSupportFragmentManager();
            BasicNoteValueFragment fragment = BasicNoteValueFragment.newInstance(noteValueData);
            fm.beginTransaction().add(android.R.id.content, fragment).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_action, menu);
        return super.onCreateOptionsMenu(menu);
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
