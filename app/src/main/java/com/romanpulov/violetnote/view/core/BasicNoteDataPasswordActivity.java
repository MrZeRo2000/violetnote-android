package com.romanpulov.violetnote.view.core;

import android.os.Bundle;

import com.romanpulov.violetnote.model.BasicNoteDataA;

/**
 * Created by romanpulov on 02.09.2016.
 */
public abstract class BasicNoteDataPasswordActivity extends PasswordActivity {
    protected BasicNoteDataA mBasicNoteData;

    @Override
    protected void updatePassword(String password) {
        // TODO: 02.09.2016 actions after password updated
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (mPasswordProvider instanceof BasicNoteDataA) {
            mBasicNoteData = (BasicNoteDataA)mPasswordProvider;
            setPasswordProtected(mBasicNoteData.getNote().getIsEncrypted());
        }
    }
}
