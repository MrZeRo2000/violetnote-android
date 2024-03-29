package com.romanpulov.violetnote.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.util.Log;

import com.romanpulov.library.common.loader.core.Loader;
import com.romanpulov.library.common.loader.core.LoaderFactory;
import com.romanpulov.violetnote.loader.helper.LoaderNotificationHelper;

import static com.romanpulov.violetnote.common.NotificationRepository.NOTIFICATION_ID_LOADER;

/**
 * Loader service
 * Created by romanpulov on 10.10.2017.
 */
public class LoaderService extends IntentService {
    public static final String SERVICE_PARAM_LOADER_NAME = "LoaderService.LoaderName";
    public static final String SERVICE_PARAM_BUNDLE = "LoaderService.Bundle";
    public static final String SERVICE_RESULT_INTENT_NAME = "LoaderServiceResult";
    public static final String SERVICE_RESULT_LOADER_NAME = "LoaderServiceResult.LoaderName";
    public static final String SERVICE_RESULT_ERROR_MESSAGE = "LoaderServiceResult.ErrorMessage";

    private static final String TAG = LoaderService.class.getSimpleName();

    private String mLoaderClassName;

    public LoaderService() {
        super("Loader service");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(this);
            mLoaderClassName = intent.getStringExtra(SERVICE_PARAM_LOADER_NAME);
            Bundle bundle = intent.getBundleExtra(SERVICE_PARAM_BUNDLE);

            String errorMessage = null;
            Log.d(TAG, "onHandleEvent : " + mLoaderClassName);
            if (mLoaderClassName == null) {
                errorMessage = "Class name not set up for loader";
            } else {
                try {
                    Loader loader = LoaderFactory.fromClassName(this, mLoaderClassName);
                    if (loader != null) {
                        Log.d(TAG, "created loader : " + mLoaderClassName);
                        loader.setBundle(bundle);
                        loader.load();
                    } else {
                        errorMessage = "Failed to create loader : " + mLoaderClassName;
                    }
                } catch (Exception e) {
                    errorMessage = e.getMessage();
                }
            }
            Log.d(TAG, "onHandleEvent completed");
            Intent resultIntent = new Intent(SERVICE_RESULT_INTENT_NAME);
            resultIntent.putExtra(SERVICE_RESULT_LOADER_NAME, mLoaderClassName);

            if (errorMessage != null) {
                resultIntent.putExtra(SERVICE_RESULT_ERROR_MESSAGE, errorMessage);
                LoaderNotificationHelper.notify(this, errorMessage, NOTIFICATION_ID_LOADER, LoaderNotificationHelper.NOTIFICATION_TYPE_FAILURE);
            }

            broadcastManager.sendBroadcast(resultIntent);
        }
    }

    @Override
    public void onStart(@Nullable Intent intent, int startId) {
        Log.d(TAG, "onStart service");
        super.onStart(intent, startId);
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate service");
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "Destroying service");
        super.onDestroy();
    }

    public class LoaderBinder extends Binder {
        public LoaderService getService() {
            return LoaderService.this;
        }
    }

    private final IBinder mBinder = new LoaderBinder();

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind service");
        return mBinder;
    }

    public String getLoaderClassName() {
        return mLoaderClassName;
    }
}
