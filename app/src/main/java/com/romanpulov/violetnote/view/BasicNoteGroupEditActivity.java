package com.romanpulov.violetnote.view;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.view.core.ActionBarCompatActivity;

public class BasicNoteGroupEditActivity extends ActionBarCompatActivity {

    private EditText mTitle;
    private Spinner mImgSelector;

    private static Integer[] mImgList = new Integer[] {
            R.drawable.img_notebook,
            R.drawable.img_app,
            R.drawable.img_bag,
            R.drawable.img_doc,
            R.drawable.img_home,
            R.drawable.img_starry
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_basic_note_group_edit);

        mTitle = findViewById(R.id.title_edit_text);
        mImgSelector = findViewById(R.id.img_selector);

        mImgSelector.setAdapter(new BasicNoteGroupImageAdapter(this, R.layout.view_img_selector_spinner, mImgList));

    }

    public void okButtonClick(View view) {

    }
}
