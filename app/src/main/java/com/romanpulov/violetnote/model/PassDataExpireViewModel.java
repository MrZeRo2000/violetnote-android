package com.romanpulov.violetnote.model;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class PassDataExpireViewModel extends ViewModel {
    private static final String TAG = PassDataExpireViewModel.class.getSimpleName();
    public static final int EXPIRATION_DELAY = 10000;

    private boolean mDataExpired = false;

    public boolean isDataExpired() {
        return mDataExpired;
    }

    public void setDataExpired(boolean mDataExpired) {
        this.mDataExpired = mDataExpired;
    }

    private ScheduledExecutorService mTimerExecutorService;

    private MutableLiveData<?> mLiveData;

    public MutableLiveData<?> getLiveData() {
        return mLiveData;
    }

    public void setLiveData(MutableLiveData<?> mLiveData) {
        // this.shutdownDataExpiration();
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

    public void shutdownDataExpiration() {
        Log.d(TAG, "Shutting down data expiration");
        if ((mTimerExecutorService != null) && (!mTimerExecutorService.isShutdown())) {
            mTimerExecutorService.shutdownNow();
            try {
                if (!mTimerExecutorService.awaitTermination(1, TimeUnit.SECONDS)) {
                    Log.d(TAG, "Timer did not shut down");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
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
                e.printStackTrace();
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
