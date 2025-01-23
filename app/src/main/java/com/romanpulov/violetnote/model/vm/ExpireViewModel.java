package com.romanpulov.violetnote.model.vm;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.romanpulov.violetnote.view.helper.LoggerHelper;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ExpireViewModel extends ViewModel {
    private static final String TAG = ExpireViewModel.class.getSimpleName();
    public static final int EXPIRATION_DELAY = 10;

    private boolean mDataExpired = false;

    private ScheduledExecutorService mTimerExecutorService;

    private MutableLiveData<?> mLiveData;

    public void setLiveData(MutableLiveData<?> mLiveData) {
        this.mLiveData = mLiveData;
        checkDataExpired();
    }

    private void scheduleDataExpiration() {
        mTimerExecutorService.schedule(() -> {
            // mPassDataResult.postValue(new PassDataResult(null, null));
            Log.d(TAG, "Data expired");
            mDataExpired = true;
        }, EXPIRATION_DELAY, TimeUnit.SECONDS);
    }

    public void initDataExpiration() {
        Log.d(TAG, "Initialize data expiration");
        this.mDataExpired = false;
        initTimer();
    }

    private void initTimer() {
        if (mTimerExecutorService == null) {
            Log.d(TAG, "Creating new timer executor");
            mTimerExecutorService = Executors.newSingleThreadScheduledExecutor();
            scheduleDataExpiration();
        } else {
            Log.d(TAG, "Shutting down expiration timer");
            mTimerExecutorService.shutdownNow();
            try {
                if (mTimerExecutorService.awaitTermination(1, TimeUnit.SECONDS)) {
                    Log.d(TAG, "Timer shut down, scheduling");
                    mTimerExecutorService = Executors.newSingleThreadScheduledExecutor();
                    scheduleDataExpiration();
                } else {
                    Log.d(TAG, "Timer did not shut down");
                }
            } catch (InterruptedException e) {
                LoggerHelper.logDebug(TAG, "Timer interrupted exception:" + e);
            }
        }
    }

    public void prolongDataExpiration() {
        Log.d(TAG, "Prolong data expiration");
        if (!mDataExpired) {
            initTimer();
        }
    }

    public boolean checkDataExpired() {
        if (mDataExpired && (mLiveData != null)) {
            Log.d(TAG, "Clear data as expired");
            mLiveData.setValue(null);
        }
        return mDataExpired;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (mTimerExecutorService != null) {
            mTimerExecutorService.shutdownNow();
        }
    }
}
