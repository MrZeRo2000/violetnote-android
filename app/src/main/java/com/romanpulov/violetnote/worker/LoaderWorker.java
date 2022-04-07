package com.romanpulov.violetnote.worker;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.romanpulov.library.common.loader.core.Loader;
import com.romanpulov.library.common.loader.core.LoaderFactory;

import java.util.concurrent.TimeUnit;

public class LoaderWorker extends Worker {
    private static final String WORKER_TAG = LoaderWorker.class.getSimpleName() + "TAG";
    private static final String WORKER_NAME = LoaderWorker.class.getSimpleName() + "NAME";
    private static final String WORKER_PARAM_LOADER_NAME = LoaderWorker.class.getSimpleName() + "LoaderName";
    private final static String TAG = LoaderWorker.class.getSimpleName();

    private static final boolean DEBUGGING = false;

    private static void log(String message) {
        if (DEBUGGING)
            Log.d(TAG, message);
    }

    public LoaderWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        log("Work started");
        final String loaderClassName = getInputData().getString(WORKER_PARAM_LOADER_NAME);

        if (loaderClassName == null) {
            log("Loader class name not provided");
            return Result.failure();
        } else {
            Loader loader = LoaderFactory.fromClassName(getApplicationContext(), loaderClassName);
            if (loader == null) {
                log("Failed to create loader " + loaderClassName);
                return Result.failure();
            } else {
                log("Created loader " + loaderClassName);
                try {
                    loader.load();
                    return Result.success();
                } catch (Exception e) {
                    log("Error loading:" + e.getMessage());
                    e.printStackTrace();
                    return Result.failure();
                }
            }
        }
    }

    public static void scheduleWorker(Context context, String loaderClassName) {
        // cancel any old works
        WorkManager.getInstance(context).cancelUniqueWork(WORKER_NAME);
        WorkManager.getInstance(context).cancelAllWorkByTag(WORKER_TAG);
        log("Old works cancelled");

        if (loaderClassName == null) {
            log("Loaded class name empty, worker is not scheduled");
        } else {

            Data inputData = (new Data.Builder()).putString(WORKER_PARAM_LOADER_NAME, loaderClassName).build();
            Constraints constraints = (new Constraints.Builder())
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .setRequiredNetworkType(NetworkType.NOT_ROAMING)
                    .build();

            PeriodicWorkRequest request = new PeriodicWorkRequest.Builder(LoaderWorker.class, 14, TimeUnit.DAYS)
                    .addTag(WORKER_TAG)
                    .setInputData(inputData)
                    .setConstraints(constraints)
                    .setInitialDelay(14, TimeUnit.DAYS)
                    .build();

            WorkManager.getInstance(context)
                    .enqueueUniquePeriodicWork(
                            WORKER_NAME,
                            ExistingPeriodicWorkPolicy.KEEP,
                            request
                    );
            log("Work scheduled");
        }
    }
}
