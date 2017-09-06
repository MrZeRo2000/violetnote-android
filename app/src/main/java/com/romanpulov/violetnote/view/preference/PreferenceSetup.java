package com.romanpulov.violetnote.view.preference;

import android.content.Context;
import android.preference.Preference;

/**
 * Created by romanpulov on 06.09.2017.
 */

public abstract class PreferenceSetup {
    final Preference mPreference;
    final Context mContext;

    public PreferenceSetup(Preference preference) {
        mPreference = preference;
        mContext = mPreference.getContext();
    }

    public abstract void execute();
}
