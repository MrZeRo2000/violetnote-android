package com.romanpulov.violetnote.view.preference;

import android.content.Context;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.widget.Toast;

import com.romanpulov.violetnote.R;

import java.text.DateFormat;
import java.util.Date;

/**
 * Preference repository class to be used for preference configuration
 * Created by romanpulov on 08.09.2017.
 */

public class PreferenceRepository {
    public static final int SOURCE_TYPE_FILE = 0;
    public static final int SOURCE_TYPE_DROPBOX = 1;
    public static final int DEFAULT_SOURCE_TYPE = SOURCE_TYPE_FILE;

    public static final String PREF_KEY_SOURCE_PATH = "pref_source_path";
    public static final String PREF_KEY_SOURCE_TYPE = "pref_source_type";
    public static final String PREF_KEY_LOAD = "pref_load";
    public static final String PREF_KEY_LAST_LOADED = "pref_last_loaded";
    public static final String PREF_KEY_ACCOUNT_DROPBOX = "pref_account_dropbox";
    public static final String PREF_KEY_BASIC_NOTE_LOCAL_BACKUP = "pref_basic_note_local_backup";
    public static final String PREF_KEY_BASIC_NOTE_LOCAL_RESTORE = "pref_basic_note_local_restore";

    /**
     * Sets and commits Source Path Preference value
     * @param value new value to set
     *
     */
    public static void setSourcePathPreferenceValue(PreferenceFragment preferenceFragment, String value) {
        preferenceFragment.getPreferenceManager().getSharedPreferences().edit().putString(PREF_KEY_SOURCE_PATH, value).commit();
        preferenceFragment.findPreference(PREF_KEY_SOURCE_PATH).setSummary(value);
    }

    /**
     * Display message common routine
     * @param context Context
     * @param message Message to display
     */
    public static void displayMessage(Context context, CharSequence message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Updates summary for preference after update
     * @param value new value
     */
    public static void updateLoadPreferenceSummary(PreferenceFragment preferenceFragment, long value) {
        Preference prefLoad = preferenceFragment.findPreference(PREF_KEY_LOAD);

        if (value == 0)
            prefLoad.setSummary(R.string.pref_message_last_loaded_never);
        else if (value == 1)
            prefLoad.setSummary(R.string.caption_loading);
        else
            prefLoad.setSummary(String.format(
                    prefLoad.getContext().getResources().getString(R.string.pref_message_last_loaded_format),
                    DateFormat.getDateTimeInstance().format(new Date(value))));
    }
}
