package com.romanpulov.violetnote.view;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.model.BasicNoteA;
import com.romanpulov.violetnote.view.core.ActionBarCompatActivity;

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
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    @Override
    protected void onPause() {
        super.onPause();

        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (imm.isAcceptingText())
            imm.hideSoftInputFromWindow(mViewHolder.mTitle.getWindowToken(), 0);

    }

    private static class ViewHolder {
        Context mContext;
        EditText mTitle;
        RadioButton mNoteTypeChecked;
        RadioButton mNoteTypeNamed;
        CheckBox mIsEncrypted;

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
