package com.romanpulov.violetnote.service;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.List;

/**
 * Utility class for interation with Service
 * Created by romanpulov on 10.10.2017.
 */

public class LoaderServiceManager {
    private static void log(String message) {
        Log.d("LoaderServiceManager", message);
    }

    private final Context mContext;

    public LoaderServiceManager(Context context) {
        mContext = context;
    }

    public boolean startLoader(String loaderClassName) {
        if (isLoaderServiceRunning()) {
            log("the loader service is running");
            return false;
        }
        else {
            Intent serviceIntent = new Intent(mContext, LoaderService.class);
            serviceIntent.putExtra(LoaderService.SERVICE_PARAM_LOADER_NAME, loaderClassName);
            mContext.startService(serviceIntent);
            return true;
        }
    }

    public boolean isLoaderServiceRunning() {
        return isServiceRunning(mContext, LoaderService.class.getName());
    }

    private static boolean isServiceRunning(Context context, String serviceClassName) {
        final ActivityManager activityManager = (ActivityManager) context.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        if (activityManager != null) {
            final List<ActivityManager.RunningServiceInfo> services = activityManager.getRunningServices(Integer.MAX_VALUE);

            for (ActivityManager.RunningServiceInfo runningServiceInfo : services) {
                if ((runningServiceInfo.service.getClassName().equals(serviceClassName)) && (runningServiceInfo.started)) {
                    return true;
                }
            }
        }

        return false;
    }
}
