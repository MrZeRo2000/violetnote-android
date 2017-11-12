package com.romanpulov.violetnote.view.preference;

import android.content.Context;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
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

    public static final long PREF_LOAD_NEVER = 0;
    public static final long PREF_LOAD_LOADING = 1;
    public static final long PREF_LOAD_CURRENT_VALUE = 2;

    public static final String PREF_KEY_SOURCE_PATH = "pref_source_path";
    public static final String PREF_KEY_SOURCE_TYPE = "pref_source_type";

    public static final String PREF_KEY_LOAD = "pref_load";
    public static final String PREF_KEY_LAST_LOADED = "pref_last_loaded";

    public static final String PREF_KEY_ACCOUNT_DROPBOX = "pref_account_dropbox";

    public static final String PREF_KEY_BASIC_NOTE_LOCAL_BACKUP = "pref_basic_note_local_backup";
    public static final String PREF_KEY_BASIC_NOTE_LOCAL_RESTORE = "pref_basic_note_local_restore";

    public static final String PREF_KEY_BASIC_NOTE_CLOUD_STORAGE =  "pref_basic_note_cloud_storage";

    public static final String PREF_KEY_BASIC_NOTE_CLOUD_BACKUP =  "pref_basic_note_cloud_backup";
    public static final String PREF_KEY_BASIC_NOTE_CLOUD_BACKUP_LAST_LOADED =  "pref_basic_note_cloud_backup_last_loaded";

    public static final String PREF_KEY_BASIC_NOTE_CLOUD_RESTORE =  "pref_basic_note_cloud_restore";
    public static final String PREF_KEY_BASIC_NOTE_CLOUD_RESTORE_LAST_LOADED =  "pref_basic_note_cloud_restore_last_loaded";

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

    public static void updateLoadPreferenceSummary(PreferenceFragment preferenceFragment, long value) {
        updateLoadPreferenceSummary(preferenceFragment, PREF_KEY_LOAD, PREF_KEY_LAST_LOADED, value);
    }

    public static void updateDropboxBackupPreferenceSummary(PreferenceFragment preferenceFragment, long value) {
        updateLoadPreferenceSummary(preferenceFragment, PREF_KEY_BASIC_NOTE_CLOUD_BACKUP, PREF_KEY_BASIC_NOTE_CLOUD_BACKUP_LAST_LOADED, value);
    }

    public static void updateDropboxRestorePreferenceSummary(PreferenceFragment preferenceFragment, long value) {
        updateLoadPreferenceSummary(preferenceFragment, PREF_KEY_BASIC_NOTE_CLOUD_RESTORE, PREF_KEY_BASIC_NOTE_CLOUD_RESTORE_LAST_LOADED, value);
    }

    public static void setDropboxRestoreDefaultPreferenceSummary(PreferenceFragment preferenceFragment) {
        Preference pref = preferenceFragment.findPreference(PREF_KEY_BASIC_NOTE_CLOUD_RESTORE);
        pref.setSummary(R.string.pref_summary_basic_note_cloud_restore);
    }

    /**
     * Updated preference summary
     * @param preferenceFragment Fragment
     * @param preferenceKey preference name
     * @param preferenceLastLoadedKey preference last loaded time
     * @param value value to update
     */
    public static void updateLoadPreferenceSummary(PreferenceFragment preferenceFragment, String preferenceKey, String preferenceLastLoadedKey, long value) {
        Preference prefLoad = preferenceFragment.findPreference(preferenceKey);

        if (value == PREF_LOAD_LOADING)
            prefLoad.setSummary(R.string.caption_loading);
        else {
            long displayValue = prefLoad.getPreferenceManager().getSharedPreferences().getLong(preferenceLastLoadedKey, PreferenceRepository.PREF_LOAD_NEVER);
            if (displayValue == PREF_LOAD_NEVER)
                prefLoad.setSummary(R.string.pref_message_last_loaded_never);
            else
                prefLoad.setSummary(String.format(
                        prefLoad.getContext().getResources().getString(R.string.pref_message_last_loaded_format),
                        DateFormat.getDateTimeInstance().format(new Date(displayValue))));
        }
    }

    private static void setPreferenceLong(Context context, String key, long value) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putLong(key, value).apply();
    }

    /**
     * Sets last loaded document time
     * @param context Context for preferences
     * @param loadedTime last loaded time
     */
    public static void setDocumentLastLoadedTime(Context context, long loadedTime) {
        setPreferenceLong(context, PreferenceRepository.PREF_KEY_LAST_LOADED, loadedTime);
    }

    /**
     * Sets last loaded document time to current time
     * @param context Context for preferences
     */
    public static void setDocumentLastLoadedCurrentTime(Context context) {
        long loadedTime = System.currentTimeMillis();
        setDocumentLastLoadedTime(context, loadedTime);
    }

    /**
     * Sets last loaded backup time
     * @param context Context for preferences
     * @param loadedTime last loaded time
     */
    public static void setCloudBackupLastLoadedTime(Context context, long loadedTime) {
        setPreferenceLong(context, PreferenceRepository.PREF_KEY_BASIC_NOTE_CLOUD_BACKUP_LAST_LOADED, loadedTime);
    }

    /**
     * Sets last loaded backup time to current time
     * @param context Context for preferences
     */
    public static void setCloudBackupLastLoadedCurrentTime(Context context) {
        long loadedTime = System.currentTimeMillis();
        setCloudBackupLastLoadedTime(context, loadedTime);
    }
}
