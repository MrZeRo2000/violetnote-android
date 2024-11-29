package com.romanpulov.violetnote.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.preference.CheckBoxPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.appcompat.app.AlertDialog;
import android.view.View;

import androidx.work.WorkInfo;
import com.romanpulov.library.common.network.NetworkUtils;
import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.cloud.CloudAccountFacade;
import com.romanpulov.violetnote.cloud.CloudAccountFacadeFactory;
import com.romanpulov.violetnote.db.DBStorageManager;
import com.romanpulov.library.common.account.AbstractCloudAccountManager;
import com.romanpulov.violetnote.loader.document.DocumentPassDataLoader;
import com.romanpulov.violetnote.loader.local.DocumentUriFileLoader;
import com.romanpulov.violetnote.view.core.RecyclerViewHelper;
import com.romanpulov.violetnote.view.helper.DisplayMessageHelper;
import com.romanpulov.violetnote.view.helper.LoggerHelper;
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
import com.romanpulov.violetnote.worker.LoaderWorker;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.romanpulov.violetnote.view.preference.PreferenceRepository.DEFAULT_CLOUD_SOURCE_TYPE;
import static com.romanpulov.violetnote.view.preference.PreferenceRepository.DEFAULT_SOURCE_TYPE;
import static com.romanpulov.violetnote.view.preference.PreferenceRepository.PREF_KEY_BASIC_NOTE_CLOUD_STORAGE;
import static com.romanpulov.violetnote.view.preference.PreferenceRepository.PREF_KEY_SOURCE_TYPE;


public class SettingsFragment extends PreferenceFragmentCompat {
    private final static String TAG = SettingsFragment.class.getSimpleName();

    private PreferenceDocumentLoaderProcessor mPreferenceDocumentLoaderProcessor;
    private PreferenceBackupCloudProcessor mPreferenceBackupCloudProcessor;
    private PreferenceRestoreCloudProcessor mPreferenceRestoreCloudProcessor;
    private PreferenceBackupLocalProcessor mPreferenceBackupLocalProcessor;
    private PreferenceRestoreLocalProcessor mPreferenceRestoreLocalProcessor;

    private final Map<String, PreferenceLoaderProcessor> mPreferenceLoadProcessors = new HashMap<>();

    private final Observer<List<WorkInfo>> mLoaderWorkerObserver = workInfos -> {
        Log.d(TAG, "WorkerObserver: " + workInfos.size() + " items");
        Log.d(TAG, "WorkerObserver: " + workInfos);
        if (workInfos.size() == 1) {
            WorkInfo workInfo = workInfos.get(0);

            switch (workInfo.getState()) {
                case RUNNING -> {
                    String loaderClassName = LoaderWorker.getLoaderClassName(workInfo.getProgress());
                    PreferenceLoaderProcessor preferenceLoaderProcessor = mPreferenceLoadProcessors.get(loaderClassName);
                    if (preferenceLoaderProcessor != null) {
                        preferenceLoaderProcessor.loaderPreExecute();
                    }
                }
                case SUCCEEDED, FAILED -> {
                    String loaderClassName = LoaderWorker.getLoaderClassName(workInfo.getOutputData());
                    String errorMessage = LoaderWorker.getErrorMessage(workInfo.getOutputData());
                    PreferenceLoaderProcessor preferenceLoaderProcessor = mPreferenceLoadProcessors.get(loaderClassName);
                    if (preferenceLoaderProcessor != null) {
                        preferenceLoaderProcessor.loaderPostExecute(errorMessage);
                    }
                }
            }
        }
    };

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    public @NotNull View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        ViewCompat.setOnApplyWindowInsetsListener(view, (v, windowInsets) -> {
            Insets insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
            // Apply the insets as a margin to the view. This solution sets only the
            // bottom, left, and right dimensions, but you can apply whichever insets are
            // appropriate to your layout. You can also update the view padding if that's
            // more appropriate.
            ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            mlp.leftMargin = insets.left;
            mlp.bottomMargin = insets.bottom;
            mlp.rightMargin = insets.right;
            v.setLayoutParams(mlp);

            // Return CONSUMED if you don't want the window insets to keep passing
            // down to descendant views.
            return WindowInsetsCompat.CONSUMED;
        });

        Log.d(TAG, "View created");
        LoaderWorker.getWorkInfosLiveData(requireContext()).observe(this, mLoaderWorkerObserver);

        return view;
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);

        List<CloudAccountFacade> cloudAccountFacadeList = CloudAccountFacadeFactory.getCloudAccountFacadeList();

        mPreferenceDocumentLoaderProcessor = new PreferenceDocumentLoaderProcessor(this);
        // local
        mPreferenceLoadProcessors.put(DocumentUriFileLoader.class.getName(), mPreferenceDocumentLoaderProcessor);
        // cloud
        for (CloudAccountFacade cloudAccountFacade: cloudAccountFacadeList) {
            mPreferenceLoadProcessors.put(cloudAccountFacade.getDocumentLoaderClassName(), mPreferenceDocumentLoaderProcessor);
        }
        setupPrefDocumentLoadService();
        setupPrefDocumentDelete();

        mPreferenceBackupCloudProcessor = new PreferenceBackupCloudProcessor(this);
        for (CloudAccountFacade cloudAccountFacade: cloudAccountFacadeList) {
            mPreferenceLoadProcessors.put(cloudAccountFacade.getBackupLoaderClassName(), mPreferenceBackupCloudProcessor);
        }
        setupPrefCloudBackupLoadService();

        mPreferenceRestoreCloudProcessor = new PreferenceRestoreCloudProcessor(this);
        for (CloudAccountFacade cloudAccountFacade: cloudAccountFacadeList) {
            mPreferenceLoadProcessors.put(cloudAccountFacade.getRestoreLoaderClassName(), mPreferenceRestoreCloudProcessor);
        }
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

        new CloudAccountPreferenceSetup(this,
                CloudAccountFacadeFactory.fromCloudSourceType(PreferenceRepository.CLOUD_SOURCE_TYPE_MSGRAPH)).execute();
        new CloudAccountPreferenceSetup(this,
                CloudAccountFacadeFactory.fromCloudSourceType(PreferenceRepository.CLOUD_SOURCE_TYPE_GDRIVE)).execute();

        new CommonSourceTypePreferenceSetup(this, PREF_KEY_BASIC_NOTE_CLOUD_STORAGE, R.array.pref_cloud_storage_entries, DEFAULT_CLOUD_SOURCE_TYPE).execute();
        new CommonSourceTypePreferenceSetup(this, PREF_KEY_BASIC_NOTE_CLOUD_STORAGE, R.array.pref_cloud_storage_entries, DEFAULT_CLOUD_SOURCE_TYPE).execute();
        new CheckedUpdateIntervalPreferenceSetup(this).execute();

        setupPrefLogging();

        Log.d(TAG, "Preferences initialized");
    }

    private boolean checkInternetConnection() {
        if (NetworkUtils.isNetworkAvailable(requireActivity()))
            return true;
        else {
            DisplayMessageHelper.displayErrorMessage(requireActivity(), getString(R.string.error_internet_not_available));
            return false;
        }
    }

    private void startDocumentLoad(Context context, String className) {
        LoaderWorker.scheduleWorker(context, className);
    }

    void executeLocalDocumentLoad(Context context) {
        startDocumentLoad(context, DocumentUriFileLoader.class.getName());
    }

    void executeCloudDocumentLoad(int type) {
        final CloudAccountFacade cloudAccountFacade = CloudAccountFacadeFactory.fromDocumentSourceType(type);

        final AbstractCloudAccountManager<?> accountManager = cloudAccountFacade.getAccountManager(getActivity());

        if (accountManager != null) {
            mPreferenceDocumentLoaderProcessor.loaderPreExecute();
            final Context context = requireContext().getApplicationContext();

            accountManager.setOnAccountSetupListener(new AbstractCloudAccountManager.OnAccountSetupListener() {
                @Override
                public void onAccountSetupSuccess() {
                    startDocumentLoad(context, cloudAccountFacade.getDocumentLoaderClassName());
                }

                @Override
                public void onAccountSetupFailure(String errorText) {
                    DisplayMessageHelper.displayErrorMessage(requireActivity(), errorText);
                    mPreferenceDocumentLoaderProcessor.loaderPostExecute(errorText);

                }
            });

            accountManager.setupAccount();
        }
    }

    void executeDocumentLoad() {
        final Preference prefSourceType = Objects.requireNonNull(findPreference(PREF_KEY_SOURCE_TYPE));
        final SharedPreferences sharedPref = Objects.requireNonNull(prefSourceType.getSharedPreferences());
        int type = sharedPref.getInt(prefSourceType.getKey(), PreferenceRepository.DEFAULT_SOURCE_TYPE);

        if (PreferenceRepository.isCloudSourceType(type)) {
            executeCloudDocumentLoad(type);
        } else if (checkInternetConnection()) {
            executeLocalDocumentLoad(requireContext().getApplicationContext());
        }
    }

    /**
     * Load document using service
     */
    private void setupPrefDocumentLoadService() {
        PreferenceRepository.updatePreferenceKeySummary(this, PreferenceRepository.PREF_KEY_DOCUMENT_LOAD, PreferenceRepository.PREF_LOAD_CURRENT_VALUE);

        final Preference pref = Objects.requireNonNull(findPreference(PreferenceRepository.PREF_KEY_DOCUMENT_LOAD));
        final SharedPreferences sharedPref = pref.getSharedPreferences();
        pref.setOnPreferenceClickListener(preference -> {
            if (sharedPref != null && !sharedPref.contains(PreferenceRepository.PREF_KEY_SOURCE_PATH)) {
                DisplayMessageHelper.displayErrorMessage(requireActivity(), getText(R.string.error_load_remote_path_empty));
            }
            else {
                if (LoaderWorker.isRunning(requireContext()))
                    DisplayMessageHelper.displayInfoMessage(requireActivity(), getText(R.string.error_load_process_running));
                else {
                    executeDocumentLoad();
                }
            }
            return true;
        });
    }

    private void setupPrefDocumentDelete() {
        final Preference pref = Objects.requireNonNull(findPreference(PreferenceRepository.PREF_KEY_DOCUMENT_DELETE));
        pref.setVisible(DocumentPassDataLoader.getDocumentFile(pref.getContext()) != null);

        pref.setOnPreferenceClickListener(preference -> {
            File documentFile = DocumentPassDataLoader.getDocumentFile(pref.getContext());
            boolean documentExists = documentFile != null;

            if (documentExists) {
                final AlertDialog.Builder alert = new AlertDialog.Builder(requireActivity(), R.style.AlertDialogTheme);
                alert
                        .setTitle(R.string.ui_question_are_you_sure)
                        .setPositiveButton(R.string.ok, (dialog, which) -> {
                            if (documentFile.delete()) {
                                DisplayMessageHelper.displayInfoMessage(requireActivity(), getText(R.string.ui_info_file_deleted));
                                pref.setVisible(false);
                                PreferenceRepository.setPreferenceKeyLastLoadedTime(
                                        getContext(),
                                        PreferenceRepository.PREF_KEY_DOCUMENT_LOAD,
                                        0
                                );
                                PreferenceRepository.updatePreferenceKeySummary(
                                        SettingsFragment.this,
                                        PreferenceRepository.PREF_KEY_DOCUMENT_LOAD,
                                        PreferenceRepository.PREF_LOAD_NEVER);
                            } else {
                                DisplayMessageHelper.displayErrorMessage(requireActivity(), getText(R.string.ui_error_file_not_deleted));
                            }
                        })
                        .setNegativeButton(R.string.cancel, null)
                        .show();

                return true;
            } else {
                return false;
            }
        });
    }

    void executeCloudBackup() {
        final Preference prefSourceType = Objects.requireNonNull(findPreference(PREF_KEY_BASIC_NOTE_CLOUD_STORAGE));
        final SharedPreferences sharedPref = Objects.requireNonNull(prefSourceType.getSharedPreferences());
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
                        DisplayMessageHelper.displayErrorMessage(requireActivity(), getString(R.string.error_backup));
                    else {
                        LoaderWorker.scheduleWorker(
                                requireContext(),
                                cloudAccountFacade.getBackupLoaderClassName());
                    }
                }

                @Override
                public void onAccountSetupFailure(String errorText) {
                    DisplayMessageHelper.displayErrorMessage(SettingsFragment.this.requireActivity(), errorText);
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
        pref.setOnPreferenceClickListener(preference -> {
            //check if internet is available
            if (!checkInternetConnection())
                return true;

            if (LoaderWorker.isRunning(requireContext()))
                DisplayMessageHelper.displayErrorMessage(requireActivity(), getText(R.string.error_load_process_running));
            else {
                executeCloudBackup();
            }
            return true;
        });
    }

    void executeCloudRestore() {
        final Preference prefSourceType = Objects.requireNonNull(findPreference(PREF_KEY_BASIC_NOTE_CLOUD_STORAGE));
        final SharedPreferences sharedPref = Objects.requireNonNull(prefSourceType.getSharedPreferences());
        int type = sharedPref.getInt(prefSourceType.getKey(), DEFAULT_CLOUD_SOURCE_TYPE);

        final CloudAccountFacade cloudAccountFacade = CloudAccountFacadeFactory.fromCloudSourceType(type);
        final AbstractCloudAccountManager<?> accountManager = cloudAccountFacade.getAccountManager(getActivity());

        if (accountManager != null) {
            mPreferenceRestoreCloudProcessor.loaderPreExecute();

            accountManager.setOnAccountSetupListener(new AbstractCloudAccountManager.OnAccountSetupListener() {
                @Override
                public void onAccountSetupSuccess() {
                    LoaderWorker.scheduleWorker(
                            requireContext(),
                            cloudAccountFacade.getRestoreLoaderClassName());
                }

                @Override
                public void onAccountSetupFailure(String errorText) {
                    DisplayMessageHelper.displayErrorMessage(requireActivity(), errorText);
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
        pref.setOnPreferenceClickListener(preference -> {
            //check if internet is available
            if (!checkInternetConnection())
                return true;

            if (LoaderWorker.isRunning(requireContext()))
                DisplayMessageHelper.displayErrorMessage(requireActivity(), getText(R.string.error_load_process_running));
            else {
                final AlertDialog.Builder alert = new AlertDialog.Builder(requireActivity(), R.style.AlertDialogTheme);
                alert
                        .setTitle(R.string.ui_question_are_you_sure)
                        .setPositiveButton(R.string.ok, (dialog, which) -> executeCloudRestore())
                        .setNegativeButton(R.string.cancel, null)
                        .show();
            }

            return true;
        });
    }

    void executeLocalBackup() {
        LoaderWorker.scheduleWorker(
                requireContext(),
                PreferenceBackupLocalProcessor.getLoaderClass().getName());
    }

    private void setupPrefLocalBackupLoadService() {
        PreferenceRepository.updatePreferenceKeySummary(this, PreferenceRepository.PREF_KEY_BASIC_NOTE_LOCAL_BACKUP, PreferenceRepository.PREF_LOAD_CURRENT_VALUE);

        Preference pref = Objects.requireNonNull(findPreference(PreferenceRepository.PREF_KEY_BASIC_NOTE_LOCAL_BACKUP));
        pref.setOnPreferenceClickListener(preference -> {
            if (LoaderWorker.isRunning(requireContext()))
                DisplayMessageHelper.displayErrorMessage(requireActivity(), getText(R.string.error_load_process_running));
            else {
                executeLocalBackup();
            }
            return true;
        });
    }

    public void executeLocalRestore() {
        LoaderWorker.scheduleWorker(
                requireContext(),
                PreferenceRestoreLocalProcessor.getLoaderClass().getName()
        );
    }

    private void setupPrefLocalRestoreLoadService() {
        PreferenceRepository.updatePreferenceKeySummary(this, PreferenceRepository.PREF_KEY_BASIC_NOTE_LOCAL_RESTORE, PreferenceRepository.PREF_LOAD_CURRENT_VALUE);

        Preference pref = findPreference(PreferenceRepository.PREF_KEY_BASIC_NOTE_LOCAL_RESTORE);
        Objects.requireNonNull(pref).setOnPreferenceClickListener(preference -> {
            if (LoaderWorker.isRunning(requireContext()))
                DisplayMessageHelper.displayErrorMessage(requireActivity(), getText(R.string.error_load_process_running));
            else {

                final AlertDialog.Builder alert = new AlertDialog.Builder(requireActivity(), R.style.AlertDialogTheme);
                alert
                        .setTitle(R.string.ui_question_are_you_sure)
                        .setPositiveButton(R.string.ok, (dialog, which) -> executeLocalRestore())
                        .setNegativeButton(R.string.cancel, null)
                        .show();
            }
            return true;
        });
    }

    private void setupPrefLogging() {
        Preference pref = findPreference(PreferenceRepository.PREF_KEY_LOGGING);
        if (pref instanceof CheckBoxPreference) {
            pref.setOnPreferenceChangeListener((preference, newValue) -> {
                if (newValue instanceof Boolean) {
                    LoggerHelper.getInstance(requireContext()).setEnableLogging((Boolean)newValue);
                }
                return true;
            });
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getListView().addItemDecoration(new RecyclerViewHelper.DividerItemDecoration(getActivity(), RecyclerViewHelper.DividerItemDecoration.VERTICAL_LIST, R.drawable.divider_white_black_gradient));
    }
}
