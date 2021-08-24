package com.romanpulov.violetnote.model;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.loader.document.DocumentPassDataLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PassDataViewModel extends AndroidViewModel {

    private final Context mContext;

    private String mPassword;

    public void setPassword(String password) {
        this.mPassword = password;
    }

    private MutableLiveData<PassDataA> mPassData = new MutableLiveData<>();
    private String mLoadErrorText;

    public String getLoadErrorText() {
        return mLoadErrorText;
    }

    private DocumentPassDataLoader documentPassDataLoader;

    public PassDataViewModel(@NonNull Application application) {
        super(application);
        mContext = application.getApplicationContext();
        documentPassDataLoader = DocumentPassDataLoader.newInstance(application);
    }

    public void loadPassData() {
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        executorService.submit(() -> {
            File file = new File(DocumentPassDataLoader.getDocumentFileName(mContext));
            if (file.exists()) {
                PassDataA passData = documentPassDataLoader.loadPassDataA(file.getAbsolutePath(), mPassword);
                mPassData.postValue(passData);
                if (documentPassDataLoader.getLoadErrorList().size() > 0) {
                    mLoadErrorText = documentPassDataLoader.getLoadErrorList().get(0);
                } else {
                    mLoadErrorText = null;
                }
            } else {
                mPassData.postValue(null);
                mLoadErrorText = mContext.getString(R.string.error_file_not_found);
            }
        });
    }

    public LiveData<PassDataA> getPassData() {
        return mPassData;
    }
}
