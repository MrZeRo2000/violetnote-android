package com.romanpulov.violetnote.view.preference;

import androidx.navigation.Navigation;
import androidx.preference.PreferenceFragmentCompat;

import com.romanpulov.violetnote.view.SettingsFragmentDirections;

import java.util.Objects;

public class BasicNoteGroupsPreferenceSetup extends PreferenceSetup {

    public BasicNoteGroupsPreferenceSetup(PreferenceFragmentCompat preferenceFragment) {
        super(preferenceFragment, PreferenceRepository.PREF_INTERFACE_BASIC_NOTE_GROUPS);
    }

    @Override
    public void execute() {
        mPreference.setOnPreferenceClickListener(preference -> {
            Navigation.findNavController(Objects.requireNonNull(mPreferenceFragment.getView())).navigate(
                    SettingsFragmentDirections.actionSettingsToBasicNoteGroup());
            return true;
        });
    }
}
