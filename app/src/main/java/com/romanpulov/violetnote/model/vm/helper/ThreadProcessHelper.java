package com.romanpulov.violetnote.model.vm.helper;

import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

public class ThreadProcessHelper {
    private static final String TAG = ThreadProcessHelper.class.getSimpleName();

    private ExecutorService mLoadExecutorService;
    private final MutableLiveData<String> mProcessError = new MutableLiveData<>();

    public LiveData<String> getProcessError() {
        return mProcessError;
    }

    public void setProcessError(String processError) {
        mProcessError.postValue(processError);
    }

    public void startProcess(Runnable runnable) {
        setProcessError(null);

        if (mLoadExecutorService == null) {
            mLoadExecutorService = Executors.newSingleThreadExecutor();
        }

        mLoadExecutorService.execute(() -> {
            try {
                runnable.run();
            } catch (Exception e) {
                Log.d(TAG, e.getMessage(), e);
                mProcessError.postValue(e.getMessage());
            }
        });
    }

    public <T> void startProcessForLiveData(
            Supplier<List<T>> dataSupplier,
            MutableLiveData<List<T>> liveData) {
        setProcessError(null);

        if (mLoadExecutorService == null) {
            mLoadExecutorService = Executors.newSingleThreadExecutor();
        }

        mLoadExecutorService.execute(() -> {
            try {
                liveData.postValue(dataSupplier.get());
            } catch (Exception e) {
                Log.d(TAG, e.getMessage(), e);
                mProcessError.postValue(e.getMessage());
            }
        });
    }

    public void shutdown() {
        if (mLoadExecutorService != null) {
            mLoadExecutorService.shutdownNow();
        }
    }
}
