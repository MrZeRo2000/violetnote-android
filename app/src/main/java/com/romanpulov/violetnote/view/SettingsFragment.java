package com.romanpulov.violetnote.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.filechooser.FileChooserActivity;
import com.romanpulov.violetnote.dropbox.DropBoxHelper;
import com.romanpulov.violetnote.loader.DocumentLoader;
import com.romanpulov.violetnote.view.preference.AccountDropboxPreferenceSetup;
import com.romanpulov.violetnote.view.preference.CloudStorageTypePreferenceSetup;
import com.romanpulov.violetnote.view.preference.LocalBackupPreferenceSetup;
import com.romanpulov.violetnote.view.preference.LocalRestorePreferenceSetup;
import com.romanpulov.violetnote.view.preference.PreferenceBackupDropboxProcessor;
import com.romanpulov.violetnote.view.preference.PreferenceDocumentLoaderProcessor;
import com.romanpulov.violetnote.view.preference.PreferenceRepository;
import com.romanpulov.violetnote.view.preference.SourcePathPreferenceSetup;
import com.romanpulov.violetnote.view.preference.SourceTypePreferenceSetup;

public class SettingsFragment extends PreferenceFragment {
    public static final String TAG = "SettingsFragment";

    private PreferenceDocumentLoaderProcessor mPreferenceDocumentLoaderProcessor;
    private PreferenceBackupDropboxProcessor mPreferenceBackupDropboxProcessor;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        addPreferencesFromResource(R.xml.preferences);

        mPreferenceDocumentLoaderProcessor = new PreferenceDocumentLoaderProcessor(this);
        setupPrefDocumentLoad();

        mPreferenceBackupDropboxProcessor = new PreferenceBackupDropboxProcessor(this);
        setupPrefDropboxBackupLoad();

        new SourceTypePreferenceSetup(this).execute();
        new SourcePathPreferenceSetup(this).execute();
        new AccountDropboxPreferenceSetup(this).execute();
        new LocalBackupPreferenceSetup(this).execute();
        new LocalRestorePreferenceSetup(this).execute();
        new CloudStorageTypePreferenceSetup(this).execute();
    }

    private void setupPrefDocumentLoad() {
        PreferenceRepository.updateLoadPreferenceSummary(this, PreferenceRepository.PREF_LOAD_CURRENT_VALUE);

        Preference pref = findPreference(PreferenceRepository.PREF_KEY_LOAD);
        pref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                if (mPreferenceDocumentLoaderProcessor.isTaskRunning())
                    PreferenceRepository.displayMessage(getActivity(), getText(R.string.error_load_process_running));
                else
                    mPreferenceBackupDropboxProcessor.executeLoader();

                return true;
            }
        });
    }

    private void setupPrefDropboxBackupLoad() {
        PreferenceRepository.updateLoadPreferenceSummary(this, PreferenceRepository.PREF_LOAD_CURRENT_VALUE);

        Preference pref = findPreference(PreferenceRepository.PREF_KEY_BASIC_NOTE_CLOUD_BACKUP);
        pref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                if (mPreferenceDocumentLoaderProcessor.isTaskRunning())
                    PreferenceRepository.displayMessage(getActivity(), getText(R.string.error_load_process_running));
                else {
                    final Preference prefSourceType = findPreference(PreferenceRepository.PREF_KEY_SOURCE_TYPE);
                    int type = prefSourceType.getPreferenceManager().getSharedPreferences().getInt(prefSourceType.getKey(), PreferenceRepository.DEFAULT_SOURCE_TYPE);

                    mPreferenceDocumentLoaderProcessor.executeDocumentLoader(type);
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
