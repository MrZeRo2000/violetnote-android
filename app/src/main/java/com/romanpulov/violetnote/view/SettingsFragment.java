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
import com.romanpulov.violetnote.loader.DocumentLoader;
import com.romanpulov.violetnote.loader.DocumentLoaderFactory;
import com.romanpulov.violetnote.filechooser.FileChooserActivity;
import com.romanpulov.violetnote.dropbox.DropBoxHelper;
import com.romanpulov.violetnote.view.preference.AccountDropboxPreferenceSetup;
import com.romanpulov.violetnote.view.preference.LocalBackupPreferenceSetup;
import com.romanpulov.violetnote.view.preference.LocalRestorePreferenceSetup;
import com.romanpulov.violetnote.view.preference.PreferenceRepository;
import com.romanpulov.violetnote.view.preference.SourcePathPreferenceSetup;
import com.romanpulov.violetnote.view.preference.SourceTypePreferenceSetup;

public class SettingsFragment extends PreferenceFragment {
    public static final String TAG = "SettingsFragment";

    private DocumentLoader mDocumentLoader;

    private final DocumentLoader.OnLoadedListener mDocumentLoaderListener = new DocumentLoader.OnLoadedListener() {
        @Override
        public void onLoaded(String result) {
            Preference prefLoad = findPreference(PreferenceRepository.PREF_KEY_LOAD);

            if (result == null) {
                prefLoad.getPreferenceManager().getSharedPreferences().edit().putLong(PreferenceRepository.PREF_KEY_LAST_LOADED, System.currentTimeMillis()).commit();
            } else {
                PreferenceRepository.displayMessage(getActivity(), result);
            }

            PreferenceRepository.updateLoadPreferenceSummary(SettingsFragment.this, prefLoad.getPreferenceManager().getSharedPreferences().getLong(PreferenceRepository.PREF_KEY_LAST_LOADED, PreferenceRepository.PREF_LOAD_NEVER));

            mDocumentLoader = null;
        }

        @Override
        public void onPreExecute() {
            if (mDocumentLoader.getLoadAppearance() == DocumentLoader.LOAD_APPEARANCE_ASYNC)
                PreferenceRepository.updateLoadPreferenceSummary(SettingsFragment.this, PreferenceRepository.PREF_LOAD_LOADING);
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

        setupPrefLoad();

        new SourceTypePreferenceSetup(this).execute();
        new SourcePathPreferenceSetup(this).execute();
        new AccountDropboxPreferenceSetup(this).execute();
        new LocalBackupPreferenceSetup(this).execute();
        new LocalRestorePreferenceSetup(this).execute();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (mDocumentLoader != null)
            PreferenceRepository.updateLoadPreferenceSummary(this, PreferenceRepository.PREF_LOAD_LOADING);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ((data != null) && (data.hasExtra(FileChooserActivity.CHOOSER_RESULT_PATH))) {
            String resultPath = data.getStringExtra(FileChooserActivity.CHOOSER_RESULT_PATH);
            PreferenceRepository.setSourcePathPreferenceValue(this, resultPath);
        }
    }

    private void setupPrefLoad() {
        Preference prefLoad = findPreference(PreferenceRepository.PREF_KEY_LOAD);
        PreferenceRepository.updateLoadPreferenceSummary(this, prefLoad.getPreferenceManager().getSharedPreferences().getLong(PreferenceRepository.PREF_KEY_LAST_LOADED, PreferenceRepository.PREF_LOAD_NEVER));

        prefLoad.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                final Preference prefSourceType = findPreference(PreferenceRepository.PREF_KEY_SOURCE_TYPE);
                int type = prefSourceType.getPreferenceManager().getSharedPreferences().getInt(prefSourceType.getKey(), PreferenceRepository.DEFAULT_SOURCE_TYPE);

                if (mDocumentLoader == null) {
                    mDocumentLoader = DocumentLoaderFactory.fromType(getActivity(), type);
                    if (mDocumentLoader != null) {
                        mDocumentLoader.setOnLoadedListener(mDocumentLoaderListener);
                        mDocumentLoader.execute();
                    }
                } else
                    PreferenceRepository.displayMessage(getActivity(), getText(R.string.error_load_process_running));

                return true;
            }
        });
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
