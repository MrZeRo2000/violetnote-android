package com.romanpulov.violetnote.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.view.core.ActionBarCompatActivity;

public class BasicNoteCheckedItemActivity extends ActionBarCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_note_checked_item);
    }
}
