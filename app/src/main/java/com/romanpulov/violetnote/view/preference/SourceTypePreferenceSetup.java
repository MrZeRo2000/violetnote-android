package com.romanpulov.violetnote.view.preference;

import android.content.DialogInterface;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v7.app.AlertDialog;

import com.romanpulov.violetnote.R;

import static com.romanpulov.violetnote.view.preference.PreferenceRepository.DEFAULT_SOURCE_TYPE;
import static com.romanpulov.violetnote.view.preference.PreferenceRepository.PREF_KEY_SOURCE_TYPE;

/**
 * Source type preference setup
 * Created by romanpulov on 06.09.2017.
 */

public class SourceTypePreferenceSetup extends PreferenceSetup {
    private final int mDefaultValue;

    public SourceTypePreferenceSetup(PreferenceFragment preferenceFragment) {
        super(preferenceFragment, PREF_KEY_SOURCE_TYPE);
        mDefaultValue = DEFAULT_SOURCE_TYPE;
    }

    @Override
    public void execute() {

        final String[] prefSourceTypeEntries = mContext.getResources().getStringArray(R.array.pref_source_type_entries);

        //Preference prefSourceType = findPreference(PREF_KEY_SOURCE_TYPE);
        mPreference.setSummary(prefSourceTypeEntries[mPreference.getPreferenceManager().getSharedPreferences().getInt(mPreference.getKey(), mDefaultValue)]);

        mPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(final Preference preference) {

                class SelectionResult {
                    int which;
                    private SelectionResult(int which) {
                        this.which = which;
                    }
                }

                final SelectionResult result = new SelectionResult(preference.getPreferenceManager().getSharedPreferences().getInt(preference.getKey(), mDefaultValue));

                final AlertDialog.Builder alert = new AlertDialog.Builder(mContext, R.style.AlertDialogTheme);
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
                                int oldSourceType = preference.getPreferenceManager().getSharedPreferences().getInt(preference.getKey(), mDefaultValue);
                                int newSourceType = result.which;

                                if (oldSourceType != newSourceType) {
                                    preference.getPreferenceManager().getSharedPreferences().edit().putInt(preference.getKey(), newSourceType).commit();
                                    preference.setSummary(prefSourceTypeEntries[preference.getPreferenceManager().getSharedPreferences().getInt(preference.getKey(), mDefaultValue)]);
                                    PreferenceRepository.setSourcePathPreferenceValue(mPreferenceFragment, null);
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
}
