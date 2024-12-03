package com.romanpulov.violetnote.view.preference;

import android.app.Activity;
import android.content.Context;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

/**
 * Root abstract class for preference configuration
 * Created by romanpulov on 06.09.2017.
 */

public abstract class PreferenceSetup {
    final PreferenceFragmentCompat mPreferenceFragment;
    final Preference mPreference;
    final Activity mActivity;
    final Context mContext;

    public PreferenceSetup(PreferenceFragmentCompat preferenceFragment, String preferenceKey) {
        mPreferenceFragment = preferenceFragment;
        mPreference = preferenceFragment.findPreference(preferenceKey);
        mActivity = preferenceFragment.requireActivity();
        mContext = preferenceFragment.requireContext().getApplicationContext();
    }

    public abstract void execute();
}
