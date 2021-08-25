package com.romanpulov.violetnote.model;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.loader.document.DocumentPassDataLoader;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class PassDataViewModel extends AndroidViewModel {
    private static final String TAG = PassDataViewModel.class.getSimpleName();

    public static final class PassDataResult {
        private final PassDataA mPassData;
        private final String mLoadErrorText;

        public PassDataA getPassData() {
            return mPassData;
        }

        public String getLoadErrorText() {
            return mLoadErrorText;
        }

        public PassDataResult(PassDataA passData, String loadErrorText) {
            this.mPassData = passData;
            this.mLoadErrorText = loadErrorText;
        }
    }

    private Context getContext() {
        return getApplication().getApplicationContext();
    }

    private ExecutorService mLoadExecutorService;
    private ScheduledExecutorService mTimerService;

    private String mPassword;
    private boolean mDataExpired = false;

    public void setPassword(String password) {
        this.mPassword = password;
    }

    private final MutableLiveData<PassDataResult> mPassDataResult = new MutableLiveData<>();

    private final DocumentPassDataLoader documentPassDataLoader;

    public PassDataViewModel(@NonNull Application application) {
        super(application);
        documentPassDataLoader = DocumentPassDataLoader.newInstance(application);
    }

    public void loadPassData() {
        if (mLoadExecutorService == null) {
            mLoadExecutorService = Executors.newFixedThreadPool(1);
        }

        if (mTimerService != null) {
            mTimerService.shutdownNow();
        }

        mDataExpired = false;

        mLoadExecutorService.submit(() -> {
            File file = new File(DocumentPassDataLoader.getDocumentFileName(getContext()));
            if (file.exists()) {
                PassDataA passData = documentPassDataLoader.loadPassDataA(file.getAbsolutePath(), mPassword);

                String loadErrorText = null;
                if (documentPassDataLoader.getLoadErrorList().size() > 0) {
                    loadErrorText = documentPassDataLoader.getLoadErrorList().get(0);
                }

                mPassDataResult.postValue(new PassDataResult(passData, loadErrorText));

                if (mTimerService == null) {
                    mTimerService = Executors.newSingleThreadScheduledExecutor();
                    mTimerService.schedule(() -> {
                        // mPassDataResult.postValue(new PassDataResult(null, null));
                        Log.d(TAG, "Data expired");
                        mDataExpired = true;
                    }, 10, TimeUnit.SECONDS);
                }

            } else {
                mPassDataResult.postValue(new PassDataResult(null, getContext().getString(R.string.error_file_not_found)));
            }
        });
    }

    public void checkDataExpired() {
        if (mDataExpired) {
            Log.d(TAG, "Clear data as expired");
            mPassDataResult.setValue(new PassDataResult(null, null));
        }
    }

    public LiveData<PassDataResult> getPassDataResult() {
        return mPassDataResult;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (mTimerService != null) {
            mTimerService.shutdownNow();
        }
        if (mLoadExecutorService != null) {
            mLoadExecutorService.shutdownNow();
        }
    }
}
