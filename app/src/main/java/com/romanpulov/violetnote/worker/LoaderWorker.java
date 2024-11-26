package com.romanpulov.violetnote.worker;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.ExistingWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.romanpulov.library.common.loader.core.Loader;
import com.romanpulov.library.common.loader.core.LoaderFactory;
import com.romanpulov.library.common.network.NetworkUtils;
import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.view.helper.LoggerHelper;

import java.util.concurrent.ExecutionException;

public class LoaderWorker extends Worker {
    private final static String TAG = LoaderWorker.class.getSimpleName();

    public static final String WORKER_NAME = LoaderWorker.class.getSimpleName() + "NAME";
    public static final String WORKER_RESULT_ERROR_MESSAGE = LoaderWorker.class.getSimpleName() + "ResultErrorMessage";

    private static final String WORKER_TAG = LoaderWorker.class.getSimpleName() + "TAG";
    private static final String WORKER_PARAM_LOADER_NAME = LoaderWorker.class.getSimpleName() + "LoaderName";

    private final String mLoaderClassName;

    public LoaderWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.mLoaderClassName = workerParams.getInputData().getString(WORKER_PARAM_LOADER_NAME);
        setProgressAsync(new Data.Builder().putString(WORKER_PARAM_LOADER_NAME, this.mLoaderClassName).build());
    }

    @NonNull
    @Override
    public Result doWork() {
        LoggerHelper.logContext(getApplicationContext(), TAG, "Work started");

        String errorMessage;

        if (!NetworkUtils.isNetworkAvailable(getApplicationContext())) {
            errorMessage = getApplicationContext().getString(R.string.error_internet_not_available);
            LoggerHelper.logContext(getApplicationContext(), TAG, errorMessage);
        } else if (mLoaderClassName == null) {
            errorMessage = "Loader class name not provided";
            LoggerHelper.logContext(getApplicationContext(), TAG, errorMessage);
        } else {
            Loader loader = LoaderFactory.fromClassName(getApplicationContext(), mLoaderClassName);
            if (loader == null) {
                errorMessage = "Failed to create loader " + mLoaderClassName;
                LoggerHelper.logContext(getApplicationContext(), TAG, errorMessage);
            } else {
                LoggerHelper.logContext(getApplicationContext(), TAG, "Created loader " + mLoaderClassName);
                try {
                    loader.load();
                    errorMessage = null;
                    LoggerHelper.logContext(getApplicationContext(), TAG, "Loaded successfully");
                } catch (Exception e) {
                    errorMessage = e.getMessage();
                    LoggerHelper.logContext(getApplicationContext(), TAG, "Error loading:" + e);
                }
            }
        }

        if (errorMessage == null) {
            LoggerHelper.logContext(getApplicationContext(), TAG, "Successful");
            Data outputResult = new Data.Builder()
                    .putString(WORKER_PARAM_LOADER_NAME, mLoaderClassName)
                    .build();
            return Result.success(outputResult);
        } else {
            Data outputResult = new Data.Builder()
                    .putString(WORKER_PARAM_LOADER_NAME, mLoaderClassName)
                    .putString(WORKER_RESULT_ERROR_MESSAGE, errorMessage)
                    .build();
            return Result.failure(outputResult);
        }
    }

    private static void internalScheduleWorker(Context context, String loaderClassName) {
        LoggerHelper.logContext(context, TAG, "Scheduling work");

        if (loaderClassName == null) {
            LoggerHelper.logContext(context, TAG, "Loaded class name empty, worker is not scheduled");
        } else {

            Data inputData = (new Data.Builder()).putString(WORKER_PARAM_LOADER_NAME, loaderClassName).build();
            Constraints constraints = (new Constraints.Builder())
                    .setRequiredNetworkType(NetworkType.NOT_ROAMING)
                    .build();

            OneTimeWorkRequest oneTimeWorkRequest = new OneTimeWorkRequest.Builder(LoaderWorker.class)
                    .addTag(WORKER_TAG)
                    .setInputData(inputData)
                    .setConstraints(constraints)
                    .build();

            WorkManager.getInstance(context)
                    .enqueueUniqueWork(
                            WORKER_NAME,
                            ExistingWorkPolicy.KEEP,
                            oneTimeWorkRequest
                    );


            LoggerHelper.logContext(context, TAG, "Work scheduled");
        }
    }

    public static void cancelAllWorkers(Context context) {
        // https://stackoverflow.com/questions/54456396/android-workmanager-doesnt-trigger-one-of-the-two-scheduled-workers
        LoggerHelper.logContext(context, TAG, "Cancelling old works with tag " + WORKER_TAG);
        try {
            WorkManager.getInstance(context).cancelAllWorkByTag(WORKER_TAG).getResult().get();
            WorkManager.getInstance(context).pruneWork().getResult().get();
            LoggerHelper.logContext(context, TAG, "Works cancelled successfully");
        } catch (InterruptedException | ExecutionException e) {
            LoggerHelper.logContext(context, TAG, "Error cancelling old works:" + e);
        }
    }

    public static void scheduleWorker(Context context, String loaderClassName) {
        internalScheduleWorker(context, loaderClassName);
    }

    public static boolean isRunning(Context context) {
        return !WorkManager.getInstance(context).getWorkInfosByTag(WORKER_TAG).isDone();
    }
}
