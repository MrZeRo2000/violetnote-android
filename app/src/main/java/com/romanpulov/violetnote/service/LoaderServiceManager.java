package com.romanpulov.violetnote.service;

import android.content.Context;
import android.content.Intent;

import com.romanpulov.library.common.service.ServiceUtils;

/**
 * Utility class for interation with Service
 * Created by romanpulov on 10.10.2017.
 */

public class LoaderServiceManager {

    public static void startLoader(Context context, String loaderClassName) {
        if (!isLoaderServiceRunning(context)) {
            Intent serviceIntent = new Intent(context, LoaderService.class);
            serviceIntent.putExtra(LoaderService.SERVICE_PARAM_LOADER_NAME, loaderClassName);
            context.startService(serviceIntent);
        }
    }

    public static boolean isLoaderServiceRunning(Context context) {
        return ServiceUtils.isServiceRunning(context, LoaderService.class.getName());
    }
}
