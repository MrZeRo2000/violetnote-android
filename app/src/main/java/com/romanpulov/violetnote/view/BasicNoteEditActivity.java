package com.romanpulov.violetnote.view;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.model.BasicNoteA;
import com.romanpulov.violetnote.view.core.ActionBarCompatActivity;
import com.romanpulov.violetnote.view.helper.InputManagerHelper;

public class BasicNoteEditActivity extends ActionBarCompatActivity {
    private final static int PASSWORD_SET_TAG = 0;
    private final static int PASSWORD_CLEAR_TAG = 255;

    private ViewHolder mViewHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_note_edit);

        mViewHolder = new ViewHolder(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        mViewHolder.mTitle.requestFocus();
        InputManagerHelper.toggleInputForced(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        InputManagerHelper.hideInput(mViewHolder.mTitle);
    }

    private static class ViewHolder {
        final Context mContext;
        final EditText mTitle;
        final RadioButton mNoteTypeChecked;
        final RadioButton mNoteTypeNamed;
        final CheckBox mIsEncrypted;

        ViewHolder(Activity activity) {
            mContext = activity;
            mTitle = (EditText)activity.findViewById(R.id.title_edit_text);
            mNoteTypeChecked = (RadioButton)activity.findViewById(R.id.note_type_checked);
            mNoteTypeNamed = (RadioButton)activity.findViewById(R.id.note_type_named);
            mIsEncrypted = (CheckBox)activity.findViewById(R.id.is_encrypted_check_box);
        }

        boolean validateInput() {
            if (mTitle.getText().toString().isEmpty()) {
                mTitle.setError(mContext.getResources().getString(R.string.error_field_not_empty));
                return false;
            }
            return true;
        }
    }

    private BasicNoteA newNote() {
        return BasicNoteA.newEditInstance(
                mViewHolder.mNoteTypeChecked.isChecked() ? 0 : 1,
                mViewHolder.mTitle.getText().toString(),
                mViewHolder.mIsEncrypted.isChecked(),
                null
        );
    }

    public void okButtonClick(View view) {
        if (mViewHolder.validateInput()) {
            getIntent().putExtra(BasicNoteActivity.NOTE,
                    newNote());
            setResult(RESULT_OK, getIntent());
            finish();
        }
    }


}
