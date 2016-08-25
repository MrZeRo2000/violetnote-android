package com.romanpulov.violetnote.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

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
