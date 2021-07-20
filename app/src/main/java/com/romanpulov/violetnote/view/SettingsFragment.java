package com.romanpulov.violetnote.view;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.appcompat.app.AlertDialog;
import android.view.View;

import com.romanpulov.library.common.network.NetworkUtils;
import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.cloud.CloudAccountFacade;
import com.romanpulov.violetnote.cloud.CloudAccountFacadeFactory;
import com.romanpulov.violetnote.db.DBStorageManager;
import com.romanpulov.violetnote.filechooser.FileChooserActivity;
import com.romanpulov.library.dropbox.DropboxHelper;
import com.romanpulov.library.common.account.AbstractCloudAccountManager;
import com.romanpulov.violetnote.loader.document.DocumentUriFileLoader;
import com.romanpulov.violetnote.loader.document.DocumentLocalFileLoader;
import com.romanpulov.violetnote.picker.HrPickerActivity;
import com.romanpulov.violetnote.service.LoaderService;
import com.romanpulov.violetnote.service.LoaderServiceManager;
import com.romanpulov.violetnote.view.core.RecyclerViewHelper;
import com.romanpulov.violetnote.view.preference.BasicNoteGroupsPreferenceSetup;
import com.romanpulov.violetnote.view.preference.CheckedUpdateIntervalPreferenceSetup;
import com.romanpulov.violetnote.view.preference.CloudAccountPreferenceSetup;
import com.romanpulov.violetnote.view.preference.CommonSourceTypePreferenceSetup;
import com.romanpulov.violetnote.view.preference.processor.PreferenceBackupCloudProcessor;
import com.romanpulov.violetnote.view.preference.processor.PreferenceBackupLocalProcessor;
import com.romanpulov.violetnote.view.preference.processor.PreferenceDocumentLoaderProcessor;
import com.romanpulov.violetnote.view.preference.processor.PreferenceLoaderProcessor;
import com.romanpulov.violetnote.view.preference.PreferenceRepository;
import com.romanpulov.violetnote.view.preference.processor.PreferenceRestoreCloudProcessor;
import com.romanpulov.violetnote.view.preference.SourcePathPreferenceSetup;
import com.romanpulov.violetnote.view.preference.processor.PreferenceRestoreLocalProcessor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.romanpulov.violetnote.view.preference.PreferenceRepository.DEFAULT_CLOUD_SOURCE_TYPE;
import static com.romanpulov.violetnote.view.preference.PreferenceRepository.DEFAULT_SOURCE_TYPE;
import static com.romanpulov.violetnote.view.preference.PreferenceRepository.PREF_KEY_BASIC_NOTE_CLOUD_STORAGE;
import static com.romanpulov.violetnote.view.preference.PreferenceRepository.PREF_KEY_SOURCE_TYPE;
import static com.romanpulov.violetnote.view.preference.PreferenceRepository.displayMessage;

public class SettingsFragment extends PreferenceFragmentCompat {

    private PreferenceDocumentLoaderProcessor mPreferenceDocumentLoaderProcessor;
    private PreferenceBackupCloudProcessor mPreferenceBackupCloudProcessor;
    private PreferenceRestoreCloudProcessor mPreferenceRestoreCloudProcessor;
    private PreferenceBackupLocalProcessor mPreferenceBackupLocalProcessor;
    private PreferenceRestoreLocalProcessor mPreferenceRestoreLocalProcessor;

    //private PermissionRequestHelper mWriteStorageRequestHelper;

    private final Map<String, PreferenceLoaderProcessor> mPreferenceLoadProcessors = new HashMap<>();

    private LoaderServiceManager mLoaderServiceManager;
    private final BroadcastReceiver mLoaderServiceBroadcastReceiver = new BroadcastReceiver() {
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
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setRetainInstance(true);

        addPreferencesFromResource(R.xml.preferences);

        List<CloudAccountFacade> cloudAccountFacadeList = CloudAccountFacadeFactory.getCloudAccountFacadeList();

        mPreferenceDocumentLoaderProcessor = new PreferenceDocumentLoaderProcessor(this);
        // local
        mPreferenceLoadProcessors.put(DocumentUriFileLoader.class.getName(), mPreferenceDocumentLoaderProcessor);
        mPreferenceLoadProcessors.put(DocumentLocalFileLoader.class.getName(), mPreferenceDocumentLoaderProcessor);
        // cloud
        for (CloudAccountFacade cloudAccountFacade: cloudAccountFacadeList) {
            mPreferenceLoadProcessors.put(cloudAccountFacade.getDocumentLoaderClassName(), mPreferenceDocumentLoaderProcessor);
        }
        setupPrefDocumentLoadService();

        mPreferenceBackupCloudProcessor = new PreferenceBackupCloudProcessor(this);
        for (CloudAccountFacade cloudAccountFacade: cloudAccountFacadeList) {
            mPreferenceLoadProcessors.put(cloudAccountFacade.getBackupLoaderClassName(), mPreferenceBackupCloudProcessor);
        }
        //mPreferenceLoadProcessors.put(BackupDropboxUploader.class.getName(), mPreferenceBackupCloudProcessor);
        //mPreferenceLoadProcessors.put(BackupOneDriveUploader.class.getName(), mPreferenceBackupCloudProcessor);
        setupPrefCloudBackupLoadService();

        mPreferenceRestoreCloudProcessor = new PreferenceRestoreCloudProcessor(this);
        for (CloudAccountFacade cloudAccountFacade: cloudAccountFacadeList) {
            mPreferenceLoadProcessors.put(cloudAccountFacade.getRestoreLoaderClassName(), mPreferenceRestoreCloudProcessor);
        }
        //mPreferenceLoadProcessors.put(RestoreDropboxFileLoader.class.getName(), mPreferenceRestoreCloudProcessor);
        //mPreferenceLoadProcessors.put(RestoreOneDriveFileLoader.class.getName(), mPreferenceRestoreCloudProcessor);
        setupPrefCloudRestoreLoadService();

        mPreferenceBackupLocalProcessor = new PreferenceBackupLocalProcessor(this);
        mPreferenceLoadProcessors.put(PreferenceBackupLocalProcessor.getLoaderClass().getName(), mPreferenceBackupLocalProcessor);
        setupPrefLocalBackupLoadService();

        mPreferenceRestoreLocalProcessor = new PreferenceRestoreLocalProcessor(this);
        mPreferenceLoadProcessors.put(PreferenceRestoreLocalProcessor.getLoaderClass().getName(), mPreferenceRestoreLocalProcessor);
        setupPrefLocalRestoreLoadService();

        new BasicNoteGroupsPreferenceSetup(this).execute();
        new CommonSourceTypePreferenceSetup(this, PREF_KEY_SOURCE_TYPE, R.array.pref_source_type_entries, DEFAULT_SOURCE_TYPE).execute();
        new SourcePathPreferenceSetup(this).execute();
        //new AccountDropboxPreferenceSetup(this).execute();
        new CloudAccountPreferenceSetup(this,
                CloudAccountFacadeFactory.fromCloudSourceType(PreferenceRepository.CLOUD_SOURCE_TYPE_DROPBOX)).execute();
        // new AccountOneDrivePreferenceSetup(this).execute();
        new CloudAccountPreferenceSetup(this,
                CloudAccountFacadeFactory.fromCloudSourceType(PreferenceRepository.CLOUD_SOURCE_TYPE_ONEDRIVE)).execute();
        new CloudAccountPreferenceSetup(this,
                CloudAccountFacadeFactory.fromCloudSourceType(PreferenceRepository.CLOUD_SOURCE_TYPE_MSGRAPH)).execute();
        new CommonSourceTypePreferenceSetup(this, PREF_KEY_BASIC_NOTE_CLOUD_STORAGE, R.array.pref_cloud_storage_entries, DEFAULT_CLOUD_SOURCE_TYPE).execute();
        new CheckedUpdateIntervalPreferenceSetup(this).execute();
    }

    private boolean checkInternetConnection() {
        if (NetworkUtils.isNetworkAvailable(requireActivity()))
            return true;
        else {
            PreferenceRepository.displayMessage(getActivity(), getString(R.string.error_internet_not_available));
            return false;
        }
    }

    private void startDocumentLoad(String className) {
        mPreferenceDocumentLoaderProcessor.loaderPreExecute();
        mLoaderServiceManager.startLoader(className, null);
    }

    void executeLocalDocumentLoad() {
        startDocumentLoad(DocumentUriFileLoader.class.getName());
    }

    void executeCloudDocumentLoad(int type) {
        final CloudAccountFacade cloudAccountFacade = CloudAccountFacadeFactory.fromCloudSourceType(type);

        final AbstractCloudAccountManager<?> accountManager = cloudAccountFacade.getAccountManager(getActivity());

        if (accountManager != null) {
            mPreferenceDocumentLoaderProcessor.loaderPreExecute();

            accountManager.setOnAccountSetupListener(new AbstractCloudAccountManager.OnAccountSetupListener() {
                @Override
                public void onAccountSetupSuccess() {
                    startDocumentLoad(cloudAccountFacade.getDocumentLoaderClassName());
                }

                @Override
                public void onAccountSetupFailure(String errorText) {
                    displayMessage(getActivity(), errorText);
                    mPreferenceDocumentLoaderProcessor.loaderPostExecute(errorText);

                }
            });

            accountManager.setupAccount();
        }
    }

    void executeDocumentLoad() {
        final Preference prefSourceType = Objects.requireNonNull(findPreference(PREF_KEY_SOURCE_TYPE));
        final SharedPreferences sharedPref = prefSourceType.getSharedPreferences();
        int type = sharedPref.getInt(prefSourceType.getKey(), PreferenceRepository.DEFAULT_SOURCE_TYPE);

        if (PreferenceRepository.isCloudSourceType(type)) {
            executeCloudDocumentLoad(type);
        } else if (checkInternetConnection()) {
            executeLocalDocumentLoad();
        }
    }

    /**
     * Load document using service
     */
    private void setupPrefDocumentLoadService() {
        PreferenceRepository.updatePreferenceKeySummary(this, PreferenceRepository.PREF_KEY_DOCUMENT_LOAD, PreferenceRepository.PREF_LOAD_CURRENT_VALUE);

        final Preference pref = Objects.requireNonNull(findPreference(PreferenceRepository.PREF_KEY_DOCUMENT_LOAD));
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
                        executeDocumentLoad();
                    }

                    return true;
                }
            }
        });
    }

    void executeCloudBackup() {
        final Preference prefSourceType = Objects.requireNonNull(findPreference(PREF_KEY_BASIC_NOTE_CLOUD_STORAGE));
        final SharedPreferences sharedPref = prefSourceType.getSharedPreferences();
        int type = sharedPref.getInt(prefSourceType.getKey(), DEFAULT_CLOUD_SOURCE_TYPE);

        final CloudAccountFacade cloudAccountFacade = CloudAccountFacadeFactory.fromCloudSourceType(type);

        final AbstractCloudAccountManager<?> accountManager = cloudAccountFacade.getAccountManager(getActivity());
        if (accountManager != null) {
            mPreferenceBackupCloudProcessor.loaderPreExecute();

            accountManager.setOnAccountSetupListener(new AbstractCloudAccountManager.OnAccountSetupListener() {
                @Override
                public void onAccountSetupSuccess() {
                    String backupResult = DBStorageManager.getDBBackupManager(getActivity()).createLocalBackup();

                    if (backupResult == null)
                        PreferenceRepository.displayMessage(getActivity(), getString(R.string.error_backup));
                    else {
                        mPreferenceBackupCloudProcessor.loaderPreExecute();
                        mLoaderServiceManager.startLoader(cloudAccountFacade.getBackupLoaderClassName(), null);
                    }
                }

                @Override
                public void onAccountSetupFailure(String errorText) {
                    displayMessage(getActivity(), errorText);
                    mPreferenceBackupCloudProcessor.loaderPostExecute(errorText);
                }
            });

            accountManager.setupAccount();
        }
    }

    /**
     * Cloud backup using service
     */
    private void setupPrefCloudBackupLoadService() {
        PreferenceRepository.updatePreferenceKeySummary(
                this,
                PreferenceRepository.PREF_KEY_BASIC_NOTE_CLOUD_BACKUP,
                PreferenceRepository.PREF_LOAD_CURRENT_VALUE
        );

        Preference pref = Objects.requireNonNull(findPreference(PreferenceRepository.PREF_KEY_BASIC_NOTE_CLOUD_BACKUP));
        pref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                //check if internet is available
                if (!checkInternetConnection())
                    return true;

                if (mLoaderServiceManager != null) {
                    if (mLoaderServiceManager.isLoaderServiceRunning())
                        PreferenceRepository.displayMessage(getActivity(), getText(R.string.error_load_process_running));
                    else {
                        executeCloudBackup();
                    }
                }
                return true;
            }
        });
    }

    void executeCloudRestore() {
        final Preference prefSourceType = Objects.requireNonNull(findPreference(PREF_KEY_BASIC_NOTE_CLOUD_STORAGE));
        final SharedPreferences sharedPref = prefSourceType.getSharedPreferences();
        int type = sharedPref.getInt(prefSourceType.getKey(), DEFAULT_CLOUD_SOURCE_TYPE);

        final CloudAccountFacade cloudAccountFacade = CloudAccountFacadeFactory.fromCloudSourceType(type);
        final AbstractCloudAccountManager<?> accountManager = cloudAccountFacade.getAccountManager(getActivity());

        if (accountManager != null) {
            mPreferenceRestoreCloudProcessor.loaderPreExecute();

            accountManager.setOnAccountSetupListener(new AbstractCloudAccountManager.OnAccountSetupListener() {
                @Override
                public void onAccountSetupSuccess() {
                    mPreferenceRestoreCloudProcessor.loaderPreExecute();
                    mLoaderServiceManager.startLoader(cloudAccountFacade.getRestoreLoaderClassName(), null);
                }

                @Override
                public void onAccountSetupFailure(String errorText) {
                    displayMessage(getActivity(), errorText);
                    mPreferenceRestoreCloudProcessor.loaderPostExecute(errorText);
                }
            });

            accountManager.setupAccount();
        }
    }

    /**
     * Cloud restore using service
     */
    private void setupPrefCloudRestoreLoadService() {
        PreferenceRepository.updatePreferenceKeySummary(
                this,
                PreferenceRepository.PREF_KEY_BASIC_NOTE_CLOUD_RESTORE,
                PreferenceRepository.PREF_LOAD_CURRENT_VALUE
        );

        Preference pref = Objects.requireNonNull(findPreference(PreferenceRepository.PREF_KEY_BASIC_NOTE_CLOUD_RESTORE));
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
                        final AlertDialog.Builder alert = new AlertDialog.Builder(requireActivity(), R.style.AlertDialogTheme);
                        alert
                                .setTitle(R.string.ui_question_are_you_sure)
                                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        executeCloudRestore();
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
        mLoaderServiceManager.startLoader(PreferenceBackupLocalProcessor.getLoaderClass().getName(), null);
    }

    private void setupPrefLocalBackupLoadService() {
        PreferenceRepository.updatePreferenceKeySummary(this, PreferenceRepository.PREF_KEY_BASIC_NOTE_LOCAL_BACKUP, PreferenceRepository.PREF_LOAD_CURRENT_VALUE);

        Preference pref = Objects.requireNonNull(findPreference(PreferenceRepository.PREF_KEY_BASIC_NOTE_LOCAL_BACKUP));
        pref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                if (mLoaderServiceManager == null)
                    return true;
                else {
                    if (mLoaderServiceManager.isLoaderServiceRunning())
                        PreferenceRepository.displayMessage(getActivity(), getText(R.string.error_load_process_running));
                    else {
                        executeLocalBackup();
                        /*
                        if (mWriteStorageRequestHelper.isPermissionGranted())
                            executeLocalBackup();
                        else
                            mWriteStorageRequestHelper.requestPermission(SettingsActivity.PERMISSION_REQUEST_LOCAL_BACKUP);

                         */
                    }
                    return true;
                }
            }
        });
    }

    public void executeLocalRestore() {
        mPreferenceRestoreLocalProcessor.loaderPreExecute();
        mLoaderServiceManager.startLoader(PreferenceRestoreLocalProcessor.getLoaderClass().getName(), null);
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
                                        /*
                                        if (mWriteStorageRequestHelper.isPermissionGranted())
                                            executeLocalRestore();
                                        else
                                            mWriteStorageRequestHelper.requestPermission(SettingsActivity.PERMISSION_REQUEST_LOCAL_RESTORE);

                                         */
                                        executeLocalRestore();
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

    private final ServiceConnection mConnection = new ServiceConnection() {
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

    void doBindService(Context context) {
        // Establish a connection with the service.  We use an explicit
        // class name because we want a specific service implementation that
        // we know will be running in our own process (and thus won't be
        // supporting component replacement by other applications).
        mIsBound = context.bindService(new Intent(context,
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
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        LocalBroadcastManager.getInstance(context).registerReceiver(mLoaderServiceBroadcastReceiver, new IntentFilter(LoaderService.SERVICE_RESULT_INTENT_NAME));
        mLoaderServiceManager = new LoaderServiceManager(context);
        doBindService(context);
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
        switch (requestCode) {
            case 0:
                if ((data != null) && (data.hasExtra(FileChooserActivity.CHOOSER_RESULT_PATH))) {
                    String resultPath = data.getStringExtra(FileChooserActivity.CHOOSER_RESULT_PATH);
                    PreferenceRepository.setSourcePathPreferenceValue(this, resultPath);
                }
                break;
            case SourcePathPreferenceSetup.OPEN_SOURCE_RESULT_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    Uri uri = data.getData();
                    if (uri != null) {
                        PreferenceRepository.setSourcePathPreferenceValue(this, uri.toString());
                    }
                }
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        DropboxHelper.getInstance(getActivity().getApplicationContext()).refreshAccessToken();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getListView().addItemDecoration(new RecyclerViewHelper.DividerItemDecoration(getActivity(), RecyclerViewHelper.DividerItemDecoration.VERTICAL_LIST, R.drawable.divider_white_black_gradient));
    }
}
