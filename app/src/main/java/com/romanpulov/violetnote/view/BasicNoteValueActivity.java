package com.romanpulov.violetnote.view;

import android.os.Bundle;
import android.view.MenuItem;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.view.core.ActionBarCompatActivity;

public class BasicNoteValueActivity extends ActionBarCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_note_value);
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
