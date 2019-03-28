package com.romanpulov.violetnote.view;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.model.BasicNoteGroupA;
import com.romanpulov.violetnote.view.core.ActionBarCompatActivity;
import com.romanpulov.violetnote.view.helper.DrawableSelectionHelper;
import com.romanpulov.violetnote.view.helper.InputManagerHelper;

import java.util.Arrays;

public class BasicNoteGroupEditActivity extends ActionBarCompatActivity {

    private BasicNoteGroupA mNoteGroup;

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

        mNoteGroup = getIntent().getParcelableExtra(BasicNoteGroupA.BASIC_NOTE_GROUP_DATA);

        if (mNoteGroup == null) {
            setTitle(R.string.title_activity_basic_note_group_add);
            mImgSelector.setSelection(0);
            mTitle.requestFocus();
        } else {
            setTitle(getString(R.string.title_activity_basic_note_group_edit, mNoteGroup.getDisplayTitle()));
            mTitle.setText(mNoteGroup.getGroupName());
            int drawable = DrawableSelectionHelper.getDrawableForNoteGroup(mNoteGroup);
            int position = Arrays.asList(mImgList).indexOf(drawable);
            if (position == -1) {
                position = 0;
            }
            mImgSelector.setSelection(position);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        InputManagerHelper.hideInput(mTitle);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mNoteGroup == null) {
            InputManagerHelper.toggleInputForced(this);
        }
    }

    private void updateNoteGroup() {
        if (mNoteGroup != null) {
            mNoteGroup.setGroupName(mTitle.getText().toString().trim());
            mNoteGroup.setGroupIcon(getGroupIcon());
        }
    }

    private long getGroupIcon() {
        int selectedItemPos = mImgSelector.getSelectedItemPosition();
        long groupIcon = 0;
        int selectedImg = mImgList[selectedItemPos];
        if (selectedImg != DrawableSelectionHelper.DEFAULT_BASIC_NOTE_GROUP_DRAWABLE) {
            groupIcon = selectedImg;
        }

        return groupIcon;
    }

    private BasicNoteGroupA newNoteGroup() {
        return BasicNoteGroupA.newEditInstance(BasicNoteGroupA.BASIC_NOTE_GROUP_TYPE, null, 0);
    }

    public void okButtonClick(View view) {
        if (mTitle.getText().toString().trim().isEmpty()) {
            mTitle.setError(this.getString(R.string.error_field_not_empty));
        } else {
            if (mNoteGroup == null) {
                mNoteGroup = newNoteGroup();
            }

            updateNoteGroup();

            getIntent().putExtra(BasicNoteGroupA.BASIC_NOTE_GROUP_DATA, mNoteGroup);
            setResult(RESULT_OK, getIntent());
            finish();
        }
    }
}
