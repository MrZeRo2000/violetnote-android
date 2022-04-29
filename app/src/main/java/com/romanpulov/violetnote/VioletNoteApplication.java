package com.romanpulov.violetnote;

import android.app.Application;
import android.net.ConnectivityManager;

import com.romanpulov.library.common.network.NetworkUtils;
import com.romanpulov.violetnote.cloud.CloudAccountFacade;
import com.romanpulov.violetnote.cloud.CloudAccountFacadeFactory;
import com.romanpulov.violetnote.service.LoaderServiceManager;
import com.romanpulov.violetnote.view.helper.LoggerHelper;
import com.romanpulov.violetnote.view.preference.PreferenceRepository;
import com.romanpulov.violetnote.worker.LoaderWorker;

import java.util.concurrent.TimeUnit;

public class VioletNoteApplication extends Application {
    private static final String TAG = VioletNoteApplication.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();

        //cancel works
        LoaderWorker.cancelAllWorkers(getApplicationContext());

        //setup network monitoring
        setupNetworkMonitoring();
    }

    private void setupNetworkMonitoring() {

        ConnectivityManager connectivityManager =
                (ConnectivityManager) (getApplicationContext().getSystemService(ConnectivityManager.class));

        NetworkUtils.ConnectivityMonitor connectivityMonitor = new NetworkUtils.ConnectivityMonitor();
        connectivityMonitor.registerOnInternetAvailableListener(networkCapabilities -> {
            long lastLoadedTime = PreferenceRepository.getPreferenceKeyLastLoadedTime(
                    getApplicationContext(),
                    PreferenceRepository.PREF_KEY_BASIC_NOTE_CLOUD_BACKUP
            );
            LoggerHelper.logContext(getApplicationContext(), TAG, "Last load time:" + lastLoadedTime);

            long daysSinceLastBackup = TimeUnit.DAYS.convert(System.currentTimeMillis() - lastLoadedTime, TimeUnit.MILLISECONDS);
            LoggerHelper.logContext(getApplicationContext(), TAG, "Since last backup:" + daysSinceLastBackup);

            if (daysSinceLastBackup > 1) {
                int cloudType = PreferenceRepository.getPreferenceCloudType(getApplicationContext());
                LoggerHelper.logContext(getApplicationContext(), TAG, "Cloud type:" + cloudType);

                if (NetworkUtils.isNetworkAvailable(getApplicationContext())) {
                    LoggerHelper.logContext(getApplicationContext(), TAG, "Internet available, starting");
                    final CloudAccountFacade cloudAccountFacade = CloudAccountFacadeFactory.fromCloudSourceType(cloudType);

                    LoaderWorker.scheduleWorker(getApplicationContext(), cloudAccountFacade.getSilentBackupLoaderClassName());
                } else {
                    LoggerHelper.logContext(getApplicationContext(), TAG, "Internet not available");
                }
            }
        });

        connectivityManager.registerDefaultNetworkCallback(connectivityMonitor);

    }
}
