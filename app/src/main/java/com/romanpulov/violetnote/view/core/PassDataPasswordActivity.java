package com.romanpulov.violetnote.view.core;

import android.os.Bundle;

import com.romanpulov.violetnote.model.PassDataA;

/**
 * Password protected activity containing PassDataA
 * Created by romanpulov on 01.09.2016.
 */
public abstract class PassDataPasswordActivity extends PasswordActivity {
    protected PassDataA mPassDataA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (mPasswordProvider instanceof PassDataA) {
            mPassDataA = (PassDataA)mPasswordProvider;
        }
    }
}
