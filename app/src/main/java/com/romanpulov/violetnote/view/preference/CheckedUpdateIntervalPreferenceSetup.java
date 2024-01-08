package com.romanpulov.violetnote.view.preference;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.appcompat.app.AlertDialog;

import com.romanpulov.violetnote.R;

import static com.romanpulov.violetnote.view.preference.PreferenceRepository.DEFAULT_CHECKED_UPDATE_REFRESH_INTERVAL;
import static com.romanpulov.violetnote.view.preference.PreferenceRepository.PREF_KEY_INTERFACE_CHECKED_UPDATE_INTERVAL;

public class CheckedUpdateIntervalPreferenceSetup extends PreferenceSetup {

    public CheckedUpdateIntervalPreferenceSetup(PreferenceFragmentCompat preferenceFragment) {
        super(preferenceFragment, PREF_KEY_INTERFACE_CHECKED_UPDATE_INTERVAL);
    }

    private int getPreferenceValue(Preference preference) {
        return preference.getPreferenceManager().getSharedPreferences().getInt(mPreference.getKey(), DEFAULT_CHECKED_UPDATE_REFRESH_INTERVAL);
    }

    @Override
    public void execute() {
        final String[] entries = mContext.getResources().getStringArray(R.array.pref_checked_update_interval_entries);

        mPreference.setSummary(entries[getPreferenceValue(mPreference)]);

        mPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(final Preference preference) {
                class SelectionResult {
                    int which;
                    private SelectionResult(int which) {
                        this.which = which;
                    }
                }

                final SelectionResult result = new SelectionResult(getPreferenceValue(preference));

                final AlertDialog.Builder alert = new AlertDialog.Builder(mActivity, R.style.AlertDialogTheme);
                alert
                        .setTitle(R.string.pref_title_interface_checked_update_interval)
                        .setSingleChoiceItems(R.array.pref_checked_update_interval_entries, result.which, (dialog, which) -> result.which = which)
                        .setPositiveButton(R.string.ok, (dialog, which) -> {
                            int oldValue = getPreferenceValue(preference);

                            if (oldValue != result.which) {
                                preference.getPreferenceManager().getSharedPreferences().edit().putInt(PREF_KEY_INTERFACE_CHECKED_UPDATE_INTERVAL, result.which).apply();
                                preference.setSummary(entries[result.which]);
                            }
                        })
                        .setNegativeButton(R.string.cancel, null)
                        .create()
                        .show();

                return true;
            }
        });
    }
}
