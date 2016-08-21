package com.romanpulov.violetnote.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.view.core.ActionBarCompatActivity;

public class BasicNoteEditActivity extends ActionBarCompatActivity {
    private final static int PASSWORD_SET_TAG = 0;
    private final static int PASSWORD_CLEAR_TAG = 255;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_note_edit);
    }

    private void updatePasswordButton(Button button, Integer tag) {
        switch (tag) {
            case PASSWORD_SET_TAG:
                button.setTag(PASSWORD_SET_TAG);
                button.setText(R.string.ui_text_set_password);
                button.getCompoundDrawables()[0].setAlpha(PASSWORD_SET_TAG);
                break;
            case PASSWORD_CLEAR_TAG:
                button.setTag(PASSWORD_CLEAR_TAG);
                button.setText(R.string.ui_text_clear_password);
                button.getCompoundDrawables()[0].setAlpha(PASSWORD_CLEAR_TAG);
                break;
        }
    }

    public void okButtonClick(View view) {
        Toast.makeText(this, "Ok clicked " + view.getClass(), Toast.LENGTH_SHORT).show();
    }


}
