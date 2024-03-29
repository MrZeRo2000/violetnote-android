package com.romanpulov.violetnote.view.preference;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.appcompat.app.AlertDialog;

import com.romanpulov.violetnote.R;

public class CommonSourceTypePreferenceSetup extends PreferenceSetup {
    private final int mSourceTypeEntriesId;
    private final int mDefaultValue;

    @Override
    public void execute() {

        final String[] prefSourceTypeEntries = mContext.getResources().getStringArray(mSourceTypeEntriesId);

        // fix non existent values
        int entryId = mPreference.getPreferenceManager().getSharedPreferences().getInt(mPreference.getKey(), mDefaultValue);
        if (entryId > prefSourceTypeEntries.length - 1) {
            mPreference.getPreferenceManager().getSharedPreferences().edit().putInt(mPreference.getKey(), prefSourceTypeEntries.length - 1).commit();
        }

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
                        .setSingleChoiceItems(mSourceTypeEntriesId, result.which, (dialog, which) -> result.which = which)
                        .setPositiveButton(R.string.ok, (dialog, which) -> {
                            int oldSourceType = preference.getPreferenceManager().getSharedPreferences().getInt(preference.getKey(), mDefaultValue);
                            int newSourceType = result.which;

                            if (oldSourceType != newSourceType) {
                                preference.getPreferenceManager().getSharedPreferences().edit().putInt(preference.getKey(), newSourceType).commit();
                                preference.setSummary(prefSourceTypeEntries[preference.getPreferenceManager().getSharedPreferences().getInt(preference.getKey(), mDefaultValue)]);
                                PreferenceRepository.setSourcePathPreferenceValue(mPreferenceFragment, null);
                            }
                        })
                        .setNegativeButton(R.string.cancel, (dialog, which) -> {})
                        .create()
                        .show();

                return true;
            }
        });

    }

    public CommonSourceTypePreferenceSetup(
            PreferenceFragmentCompat preferenceFragment,
            String preferenceKey,
            int sourceTypeEntriesId,
            int defaultValue
    ) {
        super(preferenceFragment, preferenceKey);
        this.mSourceTypeEntriesId = sourceTypeEntriesId;
        this.mDefaultValue = defaultValue;
    }
}
