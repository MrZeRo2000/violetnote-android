package com.romanpulov.violetnote.view;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.db.DBStorageManager;
import com.romanpulov.violetnote.filechooser.FileChooserActivity;
import com.romanpulov.violetnote.dropbox.DropBoxHelper;
import com.romanpulov.violetnote.loader.AbstractLoader;
import com.romanpulov.violetnote.loader.DocumentDropboxFileLoader;
import com.romanpulov.violetnote.loader.DocumentLocalFileLoader;
import com.romanpulov.violetnote.network.NetworkUtils;
import com.romanpulov.violetnote.service.LoaderService;
import com.romanpulov.violetnote.service.LoaderServiceManager;
import com.romanpulov.violetnote.view.preference.AccountDropboxPreferenceSetup;
import com.romanpulov.violetnote.view.preference.CloudStorageTypePreferenceSetup;
import com.romanpulov.violetnote.view.preference.LocalBackupPreferenceSetup;
import com.romanpulov.violetnote.view.preference.LocalRestorePreferenceSetup;
import com.romanpulov.violetnote.view.preference.PreferenceBackupDropboxProcessor;
import com.romanpulov.violetnote.view.preference.PreferenceDocumentLoaderProcessor;
import com.romanpulov.violetnote.view.preference.PreferenceLoaderProcessor;
import com.romanpulov.violetnote.view.preference.PreferenceRepository;
import com.romanpulov.violetnote.view.preference.PreferenceRestoreDropboxProcessor;
import com.romanpulov.violetnote.view.preference.SourcePathPreferenceSetup;
import com.romanpulov.violetnote.view.preference.SourceTypePreferenceSetup;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class SettingsFragment extends PreferenceFragment {
    private static void log(String message) {
        Log.d("SettingsFragment", message);
    }

    private PreferenceDocumentLoaderProcessor mPreferenceDocumentLoaderProcessor;
    private PreferenceBackupDropboxProcessor mPreferenceBackupDropboxProcessor;
    private PreferenceRestoreDropboxProcessor mPreferenceRestoreDropboxProcessor;

    private Map<String, PreferenceLoaderProcessor> mPreferenceLoadProcessors = new HashMap<>();

    private LoaderServiceManager mLoaderServiceManager;
    private BroadcastReceiver mLoaderServiceBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            log("Receiving from " + context);
            String loaderClassName = intent.getStringExtra(LoaderService.SERVICE_RESULT_LOADER_NAME);
            String errorMessage = intent.getStringExtra(LoaderService.SERVICE_RESULT_ERROR_MESSAGE);
            log("Loader class name: " + loaderClassName);
            if (errorMessage.isEmpty())
                log("No errors");
            else
                log("Error:" + errorMessage);

            PreferenceLoaderProcessor preferenceLoaderProcessor = mPreferenceLoadProcessors.get(loaderClassName);
            log("Found load processor:" + preferenceLoaderProcessor);
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

        mPreferenceDocumentLoaderProcessor = new PreferenceDocumentLoaderProcessor(this);
        mPreferenceLoadProcessors.put(DocumentLocalFileLoader.class.getName(), mPreferenceDocumentLoaderProcessor);
        mPreferenceLoadProcessors.put(DocumentDropboxFileLoader.class.getName(), mPreferenceDocumentLoaderProcessor);

        //setupPrefDocumentLoad();
        setupPrefDocumentLoadService();

        mPreferenceBackupDropboxProcessor = new PreferenceBackupDropboxProcessor(this);
        setupPrefDropboxBackupLoad();

        mPreferenceRestoreDropboxProcessor = new PreferenceRestoreDropboxProcessor(this);
        setupPrefDropboxRestoreLoad();

        new SourceTypePreferenceSetup(this).execute();
        new SourcePathPreferenceSetup(this).execute();
        new AccountDropboxPreferenceSetup(this).execute();
        new LocalBackupPreferenceSetup(this).execute();
        new LocalRestorePreferenceSetup(this).execute();
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

    private void setupPrefDocumentLoad() {
        PreferenceRepository.updateLoadPreferenceSummary(this, PreferenceRepository.PREF_LOAD_CURRENT_VALUE);

        Preference pref = findPreference(PreferenceRepository.PREF_KEY_LOAD);
        pref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                if (mPreferenceDocumentLoaderProcessor.isTaskRunning())
                    PreferenceRepository.displayMessage(getActivity(), getText(R.string.error_load_process_running));
                else {
                    final Preference prefSourceType = findPreference(PreferenceRepository.PREF_KEY_SOURCE_TYPE);
                    int type = prefSourceType.getPreferenceManager().getSharedPreferences().getInt(prefSourceType.getKey(), PreferenceRepository.DEFAULT_SOURCE_TYPE);

                    AbstractLoader loader = mPreferenceDocumentLoaderProcessor.getDocumentLoader(type);
                    if (loader.isInternetRequired() && !checkInternetConnection())
                        return true;
                    else
                        PreferenceLoaderProcessor.executeLoader(loader);
                }

                return true;
            }
        });
    }

    private void setupPrefDocumentLoadService() {
        if (mLoaderServiceManager == null)
            mLoaderServiceManager = new LoaderServiceManager(this.getActivity());

        PreferenceRepository.updateLoadPreferenceSummary(this, PreferenceRepository.PREF_LOAD_CURRENT_VALUE);

        Preference pref = findPreference(PreferenceRepository.PREF_KEY_LOAD);
        pref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                if (mLoaderServiceManager.isLoaderServiceRunning())
                    PreferenceRepository.displayMessage(getActivity(), getText(R.string.error_load_process_running));
                else {
                    final Preference prefSourceType = findPreference(PreferenceRepository.PREF_KEY_SOURCE_TYPE);
                    int type = prefSourceType.getPreferenceManager().getSharedPreferences().getInt(prefSourceType.getKey(), PreferenceRepository.DEFAULT_SOURCE_TYPE);

                    AbstractLoader loader = mPreferenceDocumentLoaderProcessor.getDocumentLoader(type);
                    Class<? extends AbstractLoader> loaderClass = loader.getClass();

                    boolean loaderInternetConnectionRequired = false;
                    try {
                        Method loaderInternetRequiredMethod = loaderClass.getMethod("isLoaderInternetRequired");
                        loaderInternetConnectionRequired = (Boolean) loaderInternetRequiredMethod.invoke(null);
                        log("Result from InternetRequiredMethod: " + loaderInternetConnectionRequired);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (loaderInternetConnectionRequired && !checkInternetConnection())
                        return true;
                    else {
                        mPreferenceDocumentLoaderProcessor.loaderPreExecute();
                        mLoaderServiceManager.startLoader(loader.getClass());
                    }
                }

                return true;
            }
        });
    }

    /**
     * Dropbox backup
     */
    private void setupPrefDropboxBackupLoad() {
        PreferenceRepository.updateDropboxBackupPreferenceSummary(this, PreferenceRepository.PREF_LOAD_CURRENT_VALUE);

        Preference pref = findPreference(PreferenceRepository.PREF_KEY_BASIC_NOTE_CLOUD_BACKUP);
        pref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                //check if internet is available
                if (!checkInternetConnection())
                    return true;

                if ((mPreferenceRestoreDropboxProcessor.isTaskRunning()) || mPreferenceBackupDropboxProcessor.isTaskRunning())
                    PreferenceRepository.displayMessage(getActivity(), getText(R.string.error_load_process_running));
                else {

                    // create local backup first
                    DBStorageManager storageManager = new DBStorageManager(getActivity());
                    String backupResult = storageManager.createRollingLocalBackup();

                    if (backupResult == null)
                        PreferenceRepository.displayMessage(getActivity(), getString(R.string.error_backup));
                    else {
                        // create remote backup
                        if (mPreferenceBackupDropboxProcessor.isTaskRunning())
                            PreferenceRepository.displayMessage(getActivity(), getText(R.string.error_load_process_running));
                        else
                            PreferenceLoaderProcessor.executeLoader(mPreferenceBackupDropboxProcessor.getBackupDropboxUploader());
                    }
                }

                return true;
            }
        });
    }

    /**
     * Dropbox restore
     */
    private void setupPrefDropboxRestoreLoad() {
        Preference pref = findPreference(PreferenceRepository.PREF_KEY_BASIC_NOTE_CLOUD_RESTORE);
        pref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                //check if internet is available
                if (!checkInternetConnection())
                    return true;

                if ((mPreferenceRestoreDropboxProcessor.isTaskRunning()) || mPreferenceBackupDropboxProcessor.isTaskRunning())
                    PreferenceRepository.displayMessage(getActivity(), getText(R.string.error_load_process_running));
                else {
                    final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme);
                    alert
                            .setTitle(R.string.ui_question_are_you_sure)
                            .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    AbstractLoader loader  = mPreferenceRestoreDropboxProcessor.getRestoreDropboxLoader();
                                    PreferenceLoaderProcessor.executeLoader(loader);

                                    /*

                                    DBBasicNoteHelper.getInstance(getActivity()).closeDB();

                                    DBStorageManager storageManager = new DBStorageManager(getActivity());
                                    String restoreResult = storageManager.restoreLocalBackup();

                                    String restoreMessage;

                                    if (restoreResult == null)
                                        restoreMessage = mContext.getString(R.string.error_restore);
                                    else
                                        restoreMessage = String.format(Locale.getDefault(), mContext.getString(R.string.message_backup_restored), restoreResult);

                                    DBBasicNoteHelper.getInstance(getActivity()).openDB();

                                    PreferenceRepository.displayMessage(getActivity(), restoreMessage);
                                    */
                                }
                            })
                            .setNegativeButton(R.string.cancel, null)
                            .show();

                }

                return true;
            }
        });
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (mPreferenceDocumentLoaderProcessor != null)
            mPreferenceDocumentLoaderProcessor.updateLoadPreferenceStatus();
        LocalBroadcastManager.getInstance(activity).registerReceiver(mLoaderServiceBroadcastReceiver, new IntentFilter(LoaderService.SERVICE_RESULT_INTENT_NAME));
    }

    @Override
    public void onDetach() {
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mLoaderServiceBroadcastReceiver);
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
        DropBoxHelper.getInstance(getActivity().getApplicationContext()).refreshAccessToken();
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
