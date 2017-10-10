package com.romanpulov.violetnote.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Messenger;
import android.support.annotation.Nullable;
import android.util.Log;

import com.romanpulov.violetnote.loader.AbstractLoader;

/**
 * Created by romanpulov on 10.10.2017.
 */

public class LoaderService extends IntentService {
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
        log("onHandleEvent");
        try {
            Thread.sleep(5000);
            //mLoader.execute();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        log("onHandleEvent completed");
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
