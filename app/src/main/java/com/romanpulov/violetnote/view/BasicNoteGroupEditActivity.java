package com.romanpulov.violetnote.view;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.model.BasicNoteGroupA;
import com.romanpulov.violetnote.view.core.ActionBarCompatActivity;
import com.romanpulov.violetnote.view.helper.DrawableSelectionHelper;
import com.romanpulov.violetnote.view.helper.InputManagerHelper;

public class BasicNoteGroupEditActivity extends ActionBarCompatActivity {

    private BasicNoteGroupA mNoteGroup;

    private EditText mTitle;
    private Spinner mImgSelector;

    private CheckBox mShowTotal;
    private CheckBox mShowUnchecked;
    private CheckBox mShowChecked;

    @Override
    protected void setupLayout() {
        setContentView(R.layout.activity_basic_note_group_edit);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mTitle = findViewById(R.id.title_edit_text);
        mImgSelector = findViewById(R.id.img_selector);
        mImgSelector.setAdapter(new BasicNoteGroupImageAdapter(this,
                R.layout.view_img_selector_spinner,
                DrawableSelectionHelper.getDrawableList())
        );

        mShowTotal = findViewById(R.id.show_total);
        mShowUnchecked = findViewById(R.id.show_unchecked);
        mShowChecked = findViewById(R.id.show_checked);

        mNoteGroup = getIntent().getParcelableExtra(BasicNoteGroupA.BASIC_NOTE_GROUP_DATA);

        if (mNoteGroup == null) {
            setTitle(R.string.title_activity_basic_note_group_add);
            mImgSelector.setSelection(0);
            mTitle.requestFocus();
        } else {
            setTitle(getString(R.string.title_activity_basic_note_group_edit, mNoteGroup.getDisplayTitle()));

            mTitle.setText(mNoteGroup.getGroupName());

            int drawable = DrawableSelectionHelper.getDrawableForNoteGroup(mNoteGroup);
            int position = DrawableSelectionHelper.getDrawablePosition(drawable);
            mImgSelector.setSelection(position);

            mShowTotal.setChecked(mNoteGroup.getDisplayOptions().getTotalFlag());
            mShowUnchecked.setChecked(mNoteGroup.getDisplayOptions().getUncheckedFlag());
            mShowChecked.setChecked(mNoteGroup.getDisplayOptions().getCheckedFlag());
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
            InputManagerHelper.showInputDelayed(this.mTitle);
        }
    }

    private void updateNoteGroup() {
        if (mNoteGroup != null) {
            mNoteGroup.setGroupName(mTitle.getText().toString().trim());
            mNoteGroup.setGroupIcon(
                DrawableSelectionHelper.getGroupIconByPosition(mImgSelector.getSelectedItemPosition())
            );

            mNoteGroup.getDisplayOptions().setTotalFlag(mShowTotal.isChecked());
            mNoteGroup.getDisplayOptions().setUncheckedFlag(mShowUnchecked.isChecked());
            mNoteGroup.getDisplayOptions().setCheckedFlag(mShowChecked.isChecked());
        }
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
