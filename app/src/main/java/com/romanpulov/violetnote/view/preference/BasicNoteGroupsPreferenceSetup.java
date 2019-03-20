package com.romanpulov.violetnote.view.preference;

import android.content.Intent;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.romanpulov.violetnote.view.BasicNoteGroupActivity;

public class BasicNoteGroupsPreferenceSetup extends PreferenceSetup {

    public BasicNoteGroupsPreferenceSetup(PreferenceFragment preferenceFragment) {
        super(preferenceFragment, PreferenceRepository.PREF_INTERFACE_BASIC_NOTE_GROUPS);
    }

    @Override
    public void execute() {
        mPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                mContext.startActivity(new Intent(mActivity, BasicNoteGroupActivity.class));
                return true;
            }
        });
    }
}
