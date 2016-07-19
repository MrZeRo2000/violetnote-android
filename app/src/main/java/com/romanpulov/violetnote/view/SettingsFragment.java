package com.romanpulov.violetnote.view;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.document.DocumentLoader;
import com.romanpulov.violetnote.document.DocumentLoaderFactory;
import com.romanpulov.violetnote.dropboxchooser.DropboxChooserActivity;
import com.romanpulov.violetnote.filechooser.FileChooserActivity;
import com.romanpulov.violetnote.dropbox.DropBoxHelper;

import java.text.DateFormat;
import java.util.Date;


public class SettingsFragment extends PreferenceFragment {
    public static final int SOURCE_TYPE_FILE = 0;
    public static final int SOURCE_TYPE_DROPBOX = 1;
    private static final int DEFAULT_SOURCE_TYPE = SOURCE_TYPE_FILE;

    public static final String PREF_KEY_SOURCE_PATH = "pref_source_path";
    public static final String PREF_KEY_SOURCE_TYPE = "pref_source_type";
    public static final String PREF_KEY_LOAD = "pref_load";
    public static final String PREF_KEY_LAST_LOADED = "pref_last_loaded";
    public static final String PREF_KEY_ACCOUNT_DROPBOX = "pref_account_dropbox";

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        setupPrefSourceType();
        setupPrefSourcePath();
        setupPrefLoad();
        setupPrefAccountDropbox();
    }

    private void displayError(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    private void setupPrefSourcePath() {
        Preference prefSourcePath = findPreference(PREF_KEY_SOURCE_PATH);

        final String sourcePath = prefSourcePath.getPreferenceManager().getSharedPreferences().getString(prefSourcePath.getKey(), null);
        prefSourcePath.setSummary(sourcePath);

        prefSourcePath.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                int sourceType = preference.getPreferenceManager().getSharedPreferences().getInt(PREF_KEY_SOURCE_TYPE, DEFAULT_SOURCE_TYPE);

                Intent intent;
                switch (sourceType) {
                    case SOURCE_TYPE_FILE:
                        intent =  new Intent(getActivity(), FileChooserActivity.class);
                        intent.putExtra(FileChooserActivity.CHOOSER_INITIAL_PATH, getPreferenceManager().getSharedPreferences().getString(PREF_KEY_SOURCE_PATH, Environment.getRootDirectory().getAbsolutePath()));
                        startActivityForResult(intent, 0);
                        return true;
                    case SOURCE_TYPE_DROPBOX:
                        try {
                            DropBoxHelper.getInstance(getActivity().getApplication()).validateDropBox();

                            intent = new Intent(getActivity(), DropboxChooserActivity.class);
                            intent.putExtra(DropboxChooserActivity.CHOOSER_INITIAL_PATH, getPreferenceManager().getSharedPreferences().getString(PREF_KEY_SOURCE_PATH, Environment.getRootDirectory().getAbsolutePath()));
                            startActivityForResult(intent, 0);
                            return true;
                        } catch (DropBoxHelper.DBHNoAccessTokenException e) {
                            displayError(getActivity().getResources().getString(R.string.error_dropbox_auth));
                        } catch (DropBoxHelper.DBHException e) {
                            displayError(String.format(getActivity().getResources().getString(R.string.error_dropbox_other), e.getMessage()));
                        }

                    default:
                        return false;
                }
            }
        });

    }

    /**
     * Sets and commits Source Path Preference value
     * @param value new value to set
     *
     */
    private void setSourcePathPreferenceValue(String value) {
        getPreferenceManager().getSharedPreferences().edit().putString(PREF_KEY_SOURCE_PATH, value).commit();
        findPreference(PREF_KEY_SOURCE_PATH).setSummary(value);
    }

    /**
     * Updates summary for preference after update
     * @param preference load preference
     * @param value new value
     */
    private void updateLoadPreferenceSummary(Preference preference, long value) {
        if (value == 0)
            preference.setSummary(R.string.pref_message_last_loaded_never);
        else
            preference.setSummary(String.format(
                    getActivity().getResources().getString(R.string.pref_message_last_loaded_format),
                    DateFormat.getDateTimeInstance().format(new Date(value))));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ((data != null) && (data.hasExtra(FileChooserActivity.CHOOSER_RESULT_PATH))) {
            String resultPath = data.getStringExtra(FileChooserActivity.CHOOSER_RESULT_PATH);
            setSourcePathPreferenceValue(resultPath);
        }
    }

    private void setupPrefSourceType() {
        final String[] prefSourceTypeEntries = getResources().getStringArray(R.array.pref_source_type_entries);

        Preference prefSourceType = findPreference(PREF_KEY_SOURCE_TYPE);
        prefSourceType.setSummary(prefSourceTypeEntries[prefSourceType.getPreferenceManager().getSharedPreferences().getInt(prefSourceType.getKey(), DEFAULT_SOURCE_TYPE)]);

        prefSourceType.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(final Preference preference) {

                class SelectionResult {
                    int which;
                    SelectionResult(int which) {
                        this.which = which;
                    }
                }

                final SelectionResult result = new SelectionResult(preference.getPreferenceManager().getSharedPreferences().getInt(preference.getKey(), DEFAULT_SOURCE_TYPE));

                final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme);
                alert
                        .setTitle(R.string.pref_title_source_type)
                        .setSingleChoiceItems(R.array.pref_source_type_entries, result.which, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                result.which = which;
                            }
                        })
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int oldSourceType = preference.getPreferenceManager().getSharedPreferences().getInt(preference.getKey(), DEFAULT_SOURCE_TYPE);
                                int newSourceType = result.which;

                                if (oldSourceType != newSourceType) {
                                    preference.getPreferenceManager().getSharedPreferences().edit().putInt(preference.getKey(), newSourceType).commit();
                                    preference.setSummary(prefSourceTypeEntries[preference.getPreferenceManager().getSharedPreferences().getInt(preference.getKey(), DEFAULT_SOURCE_TYPE)]);
                                    setSourcePathPreferenceValue(null);
                                }
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .create()
                        .show();

                return true;
            }
        });
    }

    private void setupPrefLoad() {
        final Preference prefLoad = findPreference(PREF_KEY_LOAD);
        updateLoadPreferenceSummary(prefLoad, prefLoad.getPreferenceManager().getSharedPreferences().getLong(PREF_KEY_LAST_LOADED, 0L));

        prefLoad.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Preference prefSourceType = findPreference(PREF_KEY_SOURCE_TYPE);
                int type = prefSourceType.getPreferenceManager().getSharedPreferences().getInt(prefSourceType.getKey(), DEFAULT_SOURCE_TYPE);

                DocumentLoader loader = DocumentLoaderFactory.fromType(getActivity(), type);
                if (loader != null) {
                    loader.setOnDocumentLoadedListener(new DocumentLoader.OnDocumentLoadedListener() {
                        @Override
                        public void onDocumentLoaded(String result) {
                            if (result == null) {
                                prefLoad.getPreferenceManager().getSharedPreferences().edit().putLong(PREF_KEY_LAST_LOADED, System.currentTimeMillis()).commit();
                                updateLoadPreferenceSummary(prefLoad, prefLoad.getPreferenceManager().getSharedPreferences().getLong(PREF_KEY_LAST_LOADED, 0L));
                            } else {
                                Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    loader.execute();
                }

                return true;
            }
        });
    }

    private void setupPrefAccountDropbox() {
        final Preference prefAccountDropbox = findPreference(PREF_KEY_ACCOUNT_DROPBOX);
        prefAccountDropbox.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                DropBoxHelper.getInstance(getActivity().getApplicationContext()).invokeAuthActivity(getActivity().getResources().getString(R.string.app_key));
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
