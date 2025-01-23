package com.romanpulov.violetnote.model.vm;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

public class ThreadProcessingViewModel extends ViewModel {
    private ExecutorService mLoadExecutorService;

    private MutableLiveData<Exception> processException = new MutableLiveData<>();

    public <T> void startProcessForLiveData(
            Supplier<T> dataSupplier,
            MutableLiveData<T> liveData) {
        if (mLoadExecutorService == null) {
            mLoadExecutorService = Executors.newSingleThreadExecutor();
        }

        mLoadExecutorService.execute(() -> {
            try {
                liveData.postValue(dataSupplier.get());
            } catch (Exception e) {
                processException.postValue(e);
            }
        });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (mLoadExecutorService != null) {
            mLoadExecutorService.shutdownNow();
        }
    }
}
