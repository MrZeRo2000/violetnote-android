package com.romanpulov.violetnote.view.preference;

import android.content.Intent;
import androidx.preference.PreferenceFragmentCompat;

import com.romanpulov.violetnote.view.BasicNoteGroupActivity;

public class BasicNoteGroupsPreferenceSetup extends PreferenceSetup {

    public BasicNoteGroupsPreferenceSetup(PreferenceFragmentCompat preferenceFragment) {
        super(preferenceFragment, PreferenceRepository.PREF_INTERFACE_BASIC_NOTE_GROUPS);
    }

    @Override
    public void execute() {
        mPreference.setOnPreferenceClickListener(preference -> {
            mActivity.startActivity(new Intent(mActivity, BasicNoteGroupActivity.class));
            return true;
        });
    }
}
