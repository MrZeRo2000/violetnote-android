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

    private ExecutorService mExecutorService;

    private String mPassword;

    public void setPassword(String password) {
        this.mPassword = password;
    }

    private MutableLiveData<PassDataA> mPassData = new MutableLiveData<>();
    private MutableLiveData<String> mLoadErrorText = new MutableLiveData<>();

    public MutableLiveData<String> getLoadErrorText() {
        return mLoadErrorText;
    }

    private DocumentPassDataLoader documentPassDataLoader;

    public PassDataViewModel(@NonNull Application application) {
        super(application);
        mContext = application.getApplicationContext();
        documentPassDataLoader = DocumentPassDataLoader.newInstance(application);
    }

    public void loadPassData() {
        if (mExecutorService == null) {
            mExecutorService = Executors.newFixedThreadPool(1);
        }

        mExecutorService.submit(() -> {
            File file = new File(DocumentPassDataLoader.getDocumentFileName(mContext));
            if (file.exists()) {
                PassDataA passData = documentPassDataLoader.loadPassDataA(file.getAbsolutePath(), mPassword);
                if (documentPassDataLoader.getLoadErrorList().size() > 0) {
                    mLoadErrorText.postValue(documentPassDataLoader.getLoadErrorList().get(0));
                } else {
                    mLoadErrorText.postValue(null);
                }
                mPassData.postValue(passData);
            } else {
                mPassData.postValue(null);
                mLoadErrorText.setValue(mContext.getString(R.string.error_file_not_found));
            }
        });
    }

    public LiveData<PassDataA> getPassData() {
        return mPassData;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (mExecutorService != null) {
            mExecutorService.shutdown();
        }
    }
}
