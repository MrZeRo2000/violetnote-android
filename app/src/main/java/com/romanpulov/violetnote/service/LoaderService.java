package com.romanpulov.violetnote.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Messenger;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.dropbox.core.DbxWebAuth;
import com.romanpulov.violetnote.loader.AbstractLoader;
import com.romanpulov.violetnote.loader.DocumentLoaderFactory;

/**
 * Created by romanpulov on 10.10.2017.
 */

public class LoaderService extends IntentService {
    public static final String SERVICE_PARAM_LOADER_NAME = "LoaderService.LoaderName";
    public static final String SERVICE_RESULT_INTENT_NAME = "LoaderServiceResult";
    public static final String SERVICE_RESULT_LOADER_NAME = "LoaderServiceResult.LoaderName";
    public static final String SERVICE_RESULT_ERROR_MESSAGE = "LoaderServiceResult.ErrorMessage";

    private static void log(String message) {
        Log.d("LoaderService", message);
    }

    private AbstractLoader mLoader;

    final Messenger mMessenger = new Messenger(new IncomingHandler(this));

    private static class IncomingHandler extends Handler {
        private final LoaderService mHostReference;

        IncomingHandler(LoaderService host) {
            mHostReference = host;
        }
    }

    public LoaderService() {
        super("Loader service");
        log("Started service");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(this);
            String loaderClassName = intent.getStringExtra(SERVICE_PARAM_LOADER_NAME);
            String errorMessage = "";
            log("onHandleEvent : " + loaderClassName);
            try {
                AbstractLoader loader = DocumentLoaderFactory.fromClassName(this, loaderClassName);
                if (loader != null)
                    log("created loader : " + loaderClassName);
                Thread.sleep(5000);
                //mLoader.execute();
            } catch (Exception e) {
                errorMessage = e.getMessage();
            }
            log("onHandleEvent completed");
            Intent resultIntent = new Intent(SERVICE_RESULT_INTENT_NAME);
            resultIntent.putExtra(SERVICE_RESULT_LOADER_NAME, loaderClassName);
            resultIntent.putExtra(SERVICE_RESULT_ERROR_MESSAGE, errorMessage);
            broadcastManager.sendBroadcast(resultIntent);
        }
        stopSelf();
    }

    @Override
    public void onDestroy() {
        log("Destroying service");
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }
}
