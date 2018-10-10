package com.romanpulov.violetnote.view;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.romanpulov.library.common.network.NetworkUtils;
import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.db.DBStorageManager;
import com.romanpulov.violetnote.filechooser.FileChooserActivity;
import com.romanpulov.library.dropbox.DropboxHelper;
import com.romanpulov.library.common.loader.core.AbstractContextLoader;
import com.romanpulov.violetnote.loader.dropbox.BackupDropboxUploader;
import com.romanpulov.violetnote.loader.document.DocumentDropboxFileLoader;
import com.romanpulov.violetnote.loader.document.DocumentLoaderFactory;
import com.romanpulov.violetnote.loader.document.DocumentLocalFileLoader;
import com.romanpulov.violetnote.loader.dropbox.RestoreDropboxFileLoader;
import com.romanpulov.library.common.loader.core.LoaderHelper;
import com.romanpulov.violetnote.service.LoaderService;
import com.romanpulov.violetnote.service.LoaderServiceManager;
import com.romanpulov.violetnote.view.helper.PermissionRequestHelper;
import com.romanpulov.violetnote.view.preference.AccountDropboxPreferenceSetup;
import com.romanpulov.violetnote.view.preference.CloudStorageTypePreferenceSetup;
import com.romanpulov.violetnote.view.preference.processor.PreferenceBackupDropboxProcessor;
import com.romanpulov.violetnote.view.preference.processor.PreferenceBackupLocalProcessor;
import com.romanpulov.violetnote.view.preference.processor.PreferenceDocumentLoaderProcessor;
import com.romanpulov.violetnote.view.preference.processor.PreferenceLoaderProcessor;
import com.romanpulov.violetnote.view.preference.PreferenceRepository;
import com.romanpulov.violetnote.view.preference.processor.PreferenceRestoreDropboxProcessor;
import com.romanpulov.violetnote.view.preference.SourcePathPreferenceSetup;
import com.romanpulov.violetnote.view.preference.SourceTypePreferenceSetup;
import com.romanpulov.violetnote.view.preference.processor.PreferenceRestoreLocalProcessor;

import java.util.HashMap;
import java.util.Map;

public class SettingsFragment extends PreferenceFragment {

    private PreferenceDocumentLoaderProcessor mPreferenceDocumentLoaderProcessor;
    private PreferenceBackupDropboxProcessor mPreferenceBackupDropboxProcessor;
    private PreferenceRestoreDropboxProcessor mPreferenceRestoreDropboxProcessor;
    private PreferenceBackupLocalProcessor mPreferenceBackupLocalProcessor;
    private PreferenceRestoreLocalProcessor mPreferenceRestoreLocalProcessor;

    private PermissionRequestHelper mWriteStorageRequestHelper;

    private Map<String, PreferenceLoaderProcessor> mPreferenceLoadProcessors = new HashMap<>();

    private LoaderServiceManager mLoaderServiceManager;
    private BroadcastReceiver mLoaderServiceBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String loaderClassName = intent.getStringExtra(LoaderService.SERVICE_RESULT_LOADER_NAME);
            String errorMessage = intent.getStringExtra(LoaderService.SERVICE_RESULT_ERROR_MESSAGE);

            PreferenceLoaderProcessor preferenceLoaderProcessor = mPreferenceLoadProcessors.get(loaderClassName);
            if (preferenceLoaderProcessor != null)
                preferenceLoaderProcessor.loaderPostExecute(errorMessage);
        }
    };

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        addPreferencesFromResource(R.xml.preferences);

        mWriteStorageRequestHelper = new PermissionRequestHelper(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);

        mPreferenceDocumentLoaderProcessor = new PreferenceDocumentLoaderProcessor(this);
        mPreferenceLoadProcessors.put(DocumentLocalFileLoader.class.getName(), mPreferenceDocumentLoaderProcessor);
        mPreferenceLoadProcessors.put(DocumentDropboxFileLoader.class.getName(), mPreferenceDocumentLoaderProcessor);
        setupPrefDocumentLoadService();

        mPreferenceBackupDropboxProcessor = new PreferenceBackupDropboxProcessor(this);
        mPreferenceLoadProcessors.put(BackupDropboxUploader.class.getName(), mPreferenceBackupDropboxProcessor);
        setupPrefDropboxBackupLoadService();

        mPreferenceRestoreDropboxProcessor = new PreferenceRestoreDropboxProcessor(this);
        mPreferenceLoadProcessors.put(RestoreDropboxFileLoader.class.getName(), mPreferenceRestoreDropboxProcessor);
        setupPrefDropboxRestoreLoadService();

        mPreferenceBackupLocalProcessor = new PreferenceBackupLocalProcessor(this);
        mPreferenceLoadProcessors.put(PreferenceBackupLocalProcessor.getLoaderClass().getName(), mPreferenceBackupLocalProcessor);
        setupPrefLocalBackupLoadService();

        mPreferenceRestoreLocalProcessor = new PreferenceRestoreLocalProcessor(this);
        mPreferenceLoadProcessors.put(PreferenceRestoreLocalProcessor.getLoaderClass().getName(), mPreferenceRestoreLocalProcessor);
        setupPrefLocalRestoreLoadService();

        new SourceTypePreferenceSetup(this).execute();
        new SourcePathPreferenceSetup(this).execute();
        new AccountDropboxPreferenceSetup(this).execute();
        new CloudStorageTypePreferenceSetup(this).execute();
    }

    private boolean checkInternetConnection() {
        if (NetworkUtils.isNetworkAvailable(getActivity()))
            return true;
        else {
            PreferenceRepository.displayMessage(getActivity(), getString(R.string.error_internet_not_available));
            return false;
        }
    }

    void executeDocumentLoad() {
        final Preference prefSourceType = findPreference(PreferenceRepository.PREF_KEY_SOURCE_TYPE);
        final SharedPreferences sharedPref = prefSourceType.getSharedPreferences();
        int type = sharedPref.getInt(prefSourceType.getKey(), PreferenceRepository.DEFAULT_SOURCE_TYPE);

        Class<? extends AbstractContextLoader> loaderClass = DocumentLoaderFactory.classFromType(type);
        if (loaderClass != null) {
            if (!LoaderHelper.isLoaderInternetConnectionRequired(loaderClass) || checkInternetConnection()) {
                mPreferenceDocumentLoaderProcessor.loaderPreExecute();
                mLoaderServiceManager.startLoader(loaderClass.getName());
            }
        }
    }

    /**
     * Load document using service
     */
    private void setupPrefDocumentLoadService() {
        PreferenceRepository.updatePreferenceKeySummary(this, PreferenceRepository.PREF_KEY_DOCUMENT_LOAD, PreferenceRepository.PREF_LOAD_CURRENT_VALUE);

        final Preference pref = findPreference(PreferenceRepository.PREF_KEY_DOCUMENT_LOAD);
        final SharedPreferences sharedPref = pref.getSharedPreferences();
        pref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                if (mLoaderServiceManager == null) {
                    return true;
                } else if (!sharedPref.contains(PreferenceRepository.PREF_KEY_SOURCE_PATH)) {
                    PreferenceRepository.displayMessage(getActivity(), getText(R.string.error_load_remote_path_empty));
                    return true;
                }
                else {
                    if (mLoaderServiceManager.isLoaderServiceRunning())
                        PreferenceRepository.displayMessage(getActivity(), getText(R.string.error_load_process_running));
                    else {
                        if (mWriteStorageRequestHelper.isPermissionGranted())
                            executeDocumentLoad();
                        else
                            mWriteStorageRequestHelper.requestPermission(SettingsActivity.PERMISSION_REQUEST_DOCUMENT_LOAD);
                    }

                    return true;
                }
            }
        });
    }

    void executeDropboxBackup() {
        DBStorageManager storageManager = new DBStorageManager(getActivity());
        String backupResult = storageManager.createRollingLocalBackup();

        if (backupResult == null)
            PreferenceRepository.displayMessage(getActivity(), getString(R.string.error_backup));
        else {
            mPreferenceBackupDropboxProcessor.loaderPreExecute();
            mLoaderServiceManager.startLoader(PreferenceBackupDropboxProcessor.getLoaderClass().getName());
        }
    }

    /**
     * Dropbox backup using service
     */
    private void setupPrefDropboxBackupLoadService() {
        PreferenceRepository.updatePreferenceKeySummary(this, PreferenceRepository.PREF_KEY_BASIC_NOTE_CLOUD_BACKUP, PreferenceRepository.PREF_LOAD_CURRENT_VALUE);

        Preference pref = findPreference(PreferenceRepository.PREF_KEY_BASIC_NOTE_CLOUD_BACKUP);
        pref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                //check if internet is available
                if (!checkInternetConnection())
                    return true;

                if (mLoaderServiceManager == null)
                    return true;
                else {
                    if (mLoaderServiceManager.isLoaderServiceRunning())
                        PreferenceRepository.displayMessage(getActivity(), getText(R.string.error_load_process_running));
                    else {
                        if (mWriteStorageRequestHelper.isPermissionGranted())
                            executeDropboxBackup();
                        else
                            mWriteStorageRequestHelper.requestPermission(SettingsActivity.PERMISSION_REQUEST_DROPBOX_BACKUP);
                    }
                    
                    return true;
                }
            }
        });
    }

    void executeDropboxRestore() {
        mPreferenceRestoreDropboxProcessor.loaderPreExecute();
        mLoaderServiceManager.startLoader(PreferenceRestoreDropboxProcessor.getLoaderClass().getName());
    }

    /**
     * Dropbox restore using service
     */
    private void setupPrefDropboxRestoreLoadService() {
        PreferenceRepository.updatePreferenceKeySummary(this, PreferenceRepository.PREF_KEY_BASIC_NOTE_CLOUD_RESTORE, PreferenceRepository.PREF_LOAD_CURRENT_VALUE);

        Preference pref = findPreference(PreferenceRepository.PREF_KEY_BASIC_NOTE_CLOUD_RESTORE);

        pref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                //check if internet is available
                if (!checkInternetConnection())
                    return true;

                if (mLoaderServiceManager == null)
                    return true;
                else {

                    if (mLoaderServiceManager.isLoaderServiceRunning())
                        PreferenceRepository.displayMessage(getActivity(), getText(R.string.error_load_process_running));
                    else {
                        final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme);
                        alert
                                .setTitle(R.string.ui_question_are_you_sure)
                                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (mWriteStorageRequestHelper.isPermissionGranted())
                                            executeDropboxRestore();
                                        else
                                            mWriteStorageRequestHelper.requestPermission(SettingsActivity.PERMISSION_REQUEST_DROPBOX_RESTORE);
                                    }
                                })
                                .setNegativeButton(R.string.cancel, null)
                                .show();
                    }
                }

                return true;
            }
        });
    }

    void executeLocalBackup() {
        mPreferenceBackupLocalProcessor.loaderPreExecute();
        mLoaderServiceManager.startLoader(PreferenceBackupLocalProcessor.getLoaderClass().getName());
    }

    private void setupPrefLocalBackupLoadService() {
        PreferenceRepository.updatePreferenceKeySummary(this, PreferenceRepository.PREF_KEY_BASIC_NOTE_LOCAL_BACKUP, PreferenceRepository.PREF_LOAD_CURRENT_VALUE);

        Preference pref = findPreference(PreferenceRepository.PREF_KEY_BASIC_NOTE_LOCAL_BACKUP);
        pref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                if (mLoaderServiceManager == null)
                    return true;
                else {
                    if (mLoaderServiceManager.isLoaderServiceRunning())
                        PreferenceRepository.displayMessage(getActivity(), getText(R.string.error_load_process_running));
                    else {
                        if (mWriteStorageRequestHelper.isPermissionGranted())
                            executeLocalBackup();
                        else
                            mWriteStorageRequestHelper.requestPermission(SettingsActivity.PERMISSION_REQUEST_LOCAL_BACKUP);
                    }
                    return true;
                }
            }
        });
    }

    public void executeLocalRestore() {
        mPreferenceRestoreLocalProcessor.loaderPreExecute();
        mLoaderServiceManager.startLoader(PreferenceRestoreLocalProcessor.getLoaderClass().getName());
    }

    private void setupPrefLocalRestoreLoadService() {
        PreferenceRepository.updatePreferenceKeySummary(this, PreferenceRepository.PREF_KEY_BASIC_NOTE_LOCAL_RESTORE, PreferenceRepository.PREF_LOAD_CURRENT_VALUE);

        Preference pref = findPreference(PreferenceRepository.PREF_KEY_BASIC_NOTE_LOCAL_RESTORE);
        pref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                if (mLoaderServiceManager == null)
                    return true;
                else {
                    if (mLoaderServiceManager.isLoaderServiceRunning())
                        PreferenceRepository.displayMessage(getActivity(), getText(R.string.error_load_process_running));
                    else {

                        final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme);
                        alert
                                .setTitle(R.string.ui_question_are_you_sure)
                                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (mWriteStorageRequestHelper.isPermissionGranted())
                                            executeLocalRestore();
                                        else
                                            mWriteStorageRequestHelper.requestPermission(SettingsActivity.PERMISSION_REQUEST_LOCAL_RESTORE);
                                    }
                                })
                                .setNegativeButton(R.string.cancel, null)
                                .show();
                    }
                    return true;
                }
            }
        });
    }


    private LoaderService mBoundService;
    private boolean mIsBound;

    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            // This is called when the connection with the service has been
            // established, giving us the service object we can use to
            // interact with the service.  Because we have bound to a explicit
            // service that we know is running in our own process, we can
            // cast its IBinder to a concrete class and directly access it.
            mBoundService = ((LoaderService.LoaderBinder)service).getService();

            PreferenceLoaderProcessor preferenceLoaderProcessor = mPreferenceLoadProcessors.get(mBoundService.getLoaderClassName());
            if (preferenceLoaderProcessor != null)
                preferenceLoaderProcessor.loaderPreExecute();
        }

        public void onServiceDisconnected(ComponentName className) {
            // This is called when the connection with the service has been
            // unexpectedly disconnected -- that is, its process crashed.
            // Because it is running in our same process, we should never
            // see this happen.
            mBoundService = null;
        }
    };

    void doBindService(Activity activity) {
        // Establish a connection with the service.  We use an explicit
        // class name because we want a specific service implementation that
        // we know will be running in our own process (and thus won't be
        // supporting component replacement by other applications).
        mIsBound = activity.bindService(new Intent(activity,
                LoaderService.class), mConnection, 0);
    }

    void doUnbindService() {
        if (mIsBound) {
            // Detach our existing connection.
            getActivity().unbindService(mConnection);
            mIsBound = false;
        }
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        LocalBroadcastManager.getInstance(activity).registerReceiver(mLoaderServiceBroadcastReceiver, new IntentFilter(LoaderService.SERVICE_RESULT_INTENT_NAME));
        mLoaderServiceManager = new LoaderServiceManager(activity);
        doBindService(activity);
        /*
        if (mLoaderServiceManager.isLoaderServiceRunning())
            doBindService(activity);
            */
    }

    @Override
    public void onDetach() {
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mLoaderServiceBroadcastReceiver);
        doUnbindService();
        super.onDetach();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ((data != null) && (data.hasExtra(FileChooserActivity.CHOOSER_RESULT_PATH))) {
            String resultPath = data.getStringExtra(FileChooserActivity.CHOOSER_RESULT_PATH);
            PreferenceRepository.setSourcePathPreferenceValue(this, resultPath);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        DropboxHelper.getInstance(getActivity().getApplicationContext()).refreshAccessToken();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        if(v != null) {
            ListView lv = (ListView) v.findViewById(android.R.id.list);
            if (lv != null)
                lv.setPadding(0, 0, 0, 0);
        }
        return v;
    }
}
