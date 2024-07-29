package com.romanpulov.violetnote.view;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.model.BasicNoteA;
import com.romanpulov.violetnote.model.BasicNoteGroupA;
import com.romanpulov.violetnote.view.core.ActionBarCompatActivity;
import com.romanpulov.violetnote.view.helper.InputManagerHelper;

public class BasicNoteEditActivity extends ActionBarCompatActivity {

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
        InputManagerHelper.showInputDelayed(mViewHolder.mTitle);
    }

    @Override
    protected void onPause() {
        super.onPause();
        InputManagerHelper.hideInput(mViewHolder.mTitle);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //workaround for issue with losing data after navigating via back button
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private static class ViewHolder {
        final Context mContext;
        final EditText mTitle;
        final RadioButton mNoteTypeChecked;
        final CheckBox mIsEncrypted;

        ViewHolder(Activity activity) {
            mContext = activity;
            mTitle = activity.findViewById(R.id.title_edit_text);
            mNoteTypeChecked = activity.findViewById(R.id.note_type_checked);
            mIsEncrypted = activity.findViewById(R.id.is_encrypted_check_box);
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
        long noteGroupId = BasicNoteGroupA.DEFAULT_NOTE_GROUP_ID;

        BasicNoteGroupA noteGroupA = getIntent().getParcelableExtra(BasicNoteGroupA.BASIC_NOTE_GROUP_DATA);

        if (noteGroupA != null) {
            noteGroupId = noteGroupA.getId();
        }

        return BasicNoteA.newEditInstance(
                noteGroupId,
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
