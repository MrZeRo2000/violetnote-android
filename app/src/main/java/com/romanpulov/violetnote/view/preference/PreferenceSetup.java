package com.romanpulov.violetnote.view.preference;

import android.app.Activity;
import android.content.Context;
import android.preference.Preference;
import android.preference.PreferenceFragment;

/**
 * Root abstract class for preference configuration
 * Created by romanpulov on 06.09.2017.
 */

public abstract class PreferenceSetup {
    final PreferenceFragment mPreferenceFragment;
    final Preference mPreference;
    final Activity mActivity;
    final Context mContext;

    public PreferenceSetup(PreferenceFragment preferenceFragment, String preferenceKey) {
        mPreferenceFragment = preferenceFragment;
        mPreference = preferenceFragment.findPreference(preferenceKey);
        mActivity = preferenceFragment.getActivity();
        mContext = mPreference.getContext();
    }

    public abstract void execute();
}
