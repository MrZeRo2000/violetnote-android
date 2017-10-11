package com.romanpulov.violetnote.service;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Messenger;
import android.util.Log;

import com.romanpulov.violetnote.loader.AbstractLoader;

import java.util.List;

/**
 * Utility class for interation with Service
 * Created by romanpulov on 10.10.2017.
 */

public class LoaderServiceManager {
    private static void log(String message) {
        Log.d("LoaderServiceManager", message);
    }

    private final Messenger mMessenger;
    private final Context mContext;

    private boolean mServiceBound = false;

    public LoaderServiceManager(Context context, Handler messageHandler) {
        mContext = context;
        mMessenger = new Messenger(messageHandler);
    }

    public boolean startLoader(Class<? extends AbstractLoader> loaderClass) {
        if (isLoaderServiceRunning() || mServiceBound) {
            log("the loader service is running");
            return false;
        }
        else {
            Intent serviceIntent = new Intent(mContext, LoaderService.class);
            serviceIntent.putExtra(LoaderService.SERVICE_PARAM_LOADER_NAME, loaderClass.getName());
            mContext.startService(serviceIntent);
            return true;
        }
    }

    public boolean isLoaderServiceRunning() {
        return isServiceRunning(mContext, LoaderService.class.getName());
    }

    private static boolean isServiceRunning(Context context, String serviceClassName) {
        final ActivityManager activityManager = (ActivityManager) context.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        final List<ActivityManager.RunningServiceInfo> services = activityManager.getRunningServices(Integer.MAX_VALUE);

        for (ActivityManager.RunningServiceInfo runningServiceInfo : services) {
            if (runningServiceInfo.service.getClassName().equals(serviceClassName)) {
                return true;
            }
        }
        return false;
    }
}
