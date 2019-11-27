package com.romanpulov.violetnote.view.preference;

import android.content.DialogInterface;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v7.app.AlertDialog;

import com.romanpulov.violetnote.R;

public class CommonSourceTypePreferenceSetup extends PreferenceSetup {
    private final int mSourceTypeEntriesId;
    private final int mDefaultValue;

    @Override
    public void execute() {

        final String[] prefSourceTypeEntries = mContext.getResources().getStringArray(mSourceTypeEntriesId);

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

                final AlertDialog.Builder alert = new AlertDialog.Builder(mActivity, R.style.AlertDialogTheme);
                alert
                        .setTitle(R.string.pref_title_source_type)
                        .setSingleChoiceItems(mSourceTypeEntriesId, result.which, new DialogInterface.OnClickListener() {
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

    public CommonSourceTypePreferenceSetup(
            PreferenceFragment preferenceFragment,
            String preferenceKey,
            int sourceTypeEntriesId,
            int defaultValue
    ) {
        super(preferenceFragment, preferenceKey);
        this.mSourceTypeEntriesId = sourceTypeEntriesId;
        this.mDefaultValue = defaultValue;
    }
}
