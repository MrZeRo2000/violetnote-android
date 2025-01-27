package com.romanpulov.violetnote.model.vm.helper;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.romanpulov.violetnote.view.helper.LoggerHelper;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class LiveDataExpireHelper {
    private static final String TAG = LiveDataExpireHelper.class.getSimpleName();
    private static final int DEFAULT_EXPIRATION_DELAY = 10;

    private int mExpirationDelay = DEFAULT_EXPIRATION_DELAY;

    public void setExpirationDelay(int mExpirationDelay) {
        this.mExpirationDelay = mExpirationDelay;
    }

    private final MutableLiveData<Boolean> mDataExpired = new MutableLiveData<>(false);

    public LiveData<Boolean> getDataExpired() {
        return mDataExpired;
    }

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
            mDataExpired.postValue(true);
        }, mExpirationDelay, TimeUnit.SECONDS);
    }

    public void initDataExpiration() {
        Log.d(TAG, "Initialize data expiration");
        this.mDataExpired.postValue(false);
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
        if (Boolean.FALSE.equals(mDataExpired.getValue())) {
            initTimer();
        }
    }

    public boolean checkDataExpired() {
        if (Boolean.TRUE.equals(mDataExpired.getValue()) && (mLiveData != null)) {
            Log.d(TAG, "Clear data as expired");
            mLiveData.setValue(null);
        }
        return Boolean.TRUE.equals(mDataExpired.getValue());
    }

    public void shutDown() {
        if (mTimerExecutorService != null) {
            mTimerExecutorService.shutdownNow();
        }
    }
}
