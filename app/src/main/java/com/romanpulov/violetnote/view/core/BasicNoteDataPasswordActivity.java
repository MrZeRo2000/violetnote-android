package com.romanpulov.violetnote.view.core;

import android.os.Bundle;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.model.BasicNoteDataA;
import com.romanpulov.violetnote.view.action.BasicNoteDataActionExecutor;
import com.romanpulov.violetnote.view.action.BasicNoteDataAddItemAction;
import com.romanpulov.violetnote.view.action.BasicNoteDataRefreshAction;

/**
 * Created by romanpulov on 02.09.2016.
 */
public abstract class BasicNoteDataPasswordActivity extends PasswordActivity {
    protected BasicNoteDataA mBasicNoteData;

    @Override
    protected void updatePassword(String password) {
        mBasicNoteData.setPassword(password);
        BasicNoteDataActionExecutor executor = new BasicNoteDataActionExecutor(this);
        executor.addAction(getString(R.string.caption_loading), new BasicNoteDataRefreshAction(mBasicNoteData));
        executor.setOnExecutionCompletedListener(new BasicNoteDataActionExecutor.OnExecutionCompletedListener() {
            @Override
            public void onExecutionCompleted(boolean result) {
                if (result)
                    refreshFragment();
                else
                    setLoadErrorFragment();
            }

        });
        if (mBasicNoteData.getNote().getItems().size() > 0)
            executor.executeAsync();
        else
            executor.execute();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (mPasswordProvider instanceof BasicNoteDataA) {
            mBasicNoteData = (BasicNoteDataA)mPasswordProvider;
            setPasswordProtected(mBasicNoteData.getNote().isEncrypted());
        }
    }
}
