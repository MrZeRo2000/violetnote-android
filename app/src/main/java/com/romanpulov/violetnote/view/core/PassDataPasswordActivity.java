package com.romanpulov.violetnote.view.core;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.model.Document;
import com.romanpulov.violetnote.model.PassDataA;

import java.io.File;
import java.util.List;

/**
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
