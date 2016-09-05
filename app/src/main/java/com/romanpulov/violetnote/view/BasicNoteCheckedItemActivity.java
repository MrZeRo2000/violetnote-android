package com.romanpulov.violetnote.view;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.model.BasicNoteItemA;
import com.romanpulov.violetnote.view.core.ActionBarCompatActivity;
import com.romanpulov.violetnote.view.core.BasicNoteDataPasswordActivity;

public class BasicNoteCheckedItemActivity extends BasicNoteDataPasswordActivity {

    @Override
    protected void refreshFragment() {
        Fragment fragment = BasicNoteCheckedItemFragment.newInstance(mBasicNoteData);
        removeFragment().beginTransaction().add(android.R.id.content, fragment).commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        refreshFragment();
    }
}
