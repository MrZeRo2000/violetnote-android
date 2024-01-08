package com.romanpulov.violetnote.service;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.romanpulov.library.common.service.ServiceUtils;

/**
 * Utility class for interation with Service
 * Created by romanpulov on 10.10.2017.
 */

public class LoaderServiceManager {

    public static void startLoader(Context context, String loaderClassName, Bundle bundle) {
        if (!isLoaderServiceRunning(context)) {
            Intent serviceIntent = new Intent(context, LoaderService.class);
            serviceIntent.putExtra(LoaderService.SERVICE_PARAM_LOADER_NAME, loaderClassName);
            if (bundle != null) {
                serviceIntent.putExtra(LoaderService.SERVICE_PARAM_BUNDLE, bundle);
            }
            context.startService(serviceIntent);
        }
    }

    public static boolean isLoaderServiceRunning(Context context) {
        return ServiceUtils.isServiceRunning(context, LoaderService.class.getName());
    }
}
