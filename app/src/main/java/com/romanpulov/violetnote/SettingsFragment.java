package com.romanpulov.violetnote;


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

import com.romanpulov.violetnote.HrChooser.FileChooserActivity;


public class SettingsFragment extends PreferenceFragment {
    private static final int DEFAULT_SOURCE_TYPE = 0;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        setupPrefSourceType();
        setupPrefSourcePath();
    }

    private void setupPrefSourcePath() {
        Preference prefSourcePath = findPreference("pref_source_path");

        final String sourcePath = prefSourcePath.getPreferenceManager().getSharedPreferences().getString(prefSourcePath.getKey(), Environment.getRootDirectory().getAbsolutePath());

        prefSourcePath.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent intent =  new Intent(getActivity(), FileChooserActivity.class);
                intent.putExtra(FileChooserActivity.CHOOSER_INITIAL_PATH, sourcePath);
                startActivityForResult(intent, 0);
                return true;
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Toast.makeText(getActivity(), "Result=" + data.getStringExtra(FileChooserActivity.CHOOSER_RESULT_PATH) + data.getStringExtra(FileChooserActivity.CHOOSER_RESULT_NAME), Toast.LENGTH_SHORT).show();
    }


    private void setupPrefSourceType() {
        final String[] prefSourceTypeEntries = getResources().getStringArray(R.array.pref_source_type_entries);

        Preference prefSourceType = findPreference("pref_source_type");
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
                                preference.getPreferenceManager().getSharedPreferences().edit().putInt(preference.getKey(), result.which).commit();
                                preference.setSummary(prefSourceTypeEntries[preference.getPreferenceManager().getSharedPreferences().getInt(preference.getKey(), DEFAULT_SOURCE_TYPE)]);
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
