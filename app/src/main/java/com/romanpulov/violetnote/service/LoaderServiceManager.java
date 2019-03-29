package com.romanpulov.violetnote.service;

import android.content.Context;
import android.content.Intent;

import com.romanpulov.library.common.service.ServiceUtils;

/**
 * Utility class for interation with Service
 * Created by romanpulov on 10.10.2017.
 */

public class LoaderServiceManager {

    private final Context mContext;

    public LoaderServiceManager(Context context) {
        mContext = context;
    }

    public boolean startLoader(String loaderClassName) {
        if (isLoaderServiceRunning()) {
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
        return ServiceUtils.isServiceRunning(mContext, LoaderService.class.getName());
    }
}
