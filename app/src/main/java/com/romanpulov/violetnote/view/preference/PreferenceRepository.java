package com.romanpulov.violetnote.view.preference;

import android.content.Context;
import android.content.res.Resources;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragment;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import android.widget.Toast;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.view.core.ProgressPreference;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Preference repository class to be used for preference configuration
 * Created by romanpulov on 08.09.2017.
 */

public class PreferenceRepository {
    public static final int SOURCE_TYPE_FILE = 0;
    public static final int SOURCE_TYPE_DROPBOX = 1;
    public static final int SOURCE_TYPE_MSGRAPH = 2;

    public static final int DEFAULT_SOURCE_TYPE = 0;

    public static boolean isCloudSourceType(int sourceType) {
        return sourceType != SOURCE_TYPE_FILE;
    }

    public static final int CLOUD_SOURCE_TYPE_DROPBOX = 0;
    public static final int CLOUD_SOURCE_TYPE_MSGRAPH = 1;
    public static final int DEFAULT_CLOUD_SOURCE_TYPE = 0;

    public static final int DEFAULT_CHECKED_UPDATE_REFRESH_INTERVAL = 0;

    public static final long PREF_LOAD_NEVER = 0;
    public static final long PREF_LOAD_LOADING = 1;
    public static final long PREF_LOAD_CURRENT_VALUE = 2;

    public static final String PREF_INTERFACE_BASIC_NOTE_GROUPS = "pref_interface_basic_note_groups";
    public static final String PREF_KEY_INTERFACE_CHECKED_LAST = "pref_interface_checked_last";
    public static final String PREF_KEY_INTERFACE_CHECKED_UPDATE_INTERVAL = "pref_interface_checked_update_interval";

    public static final String PREF_KEY_SOURCE_PATH = "pref_source_path";
    public static final String PREF_KEY_SOURCE_TYPE = "pref_source_type";

    public static final String PREF_KEY_ACCOUNT_DROPBOX = "pref_account_dropbox";
    public static final String PREF_KEY_ACCOUNT_MSGRAPH = "pref_account_onedrive";

    public static final String PREF_KEY_BASIC_NOTE_CLOUD_STORAGE =  "pref_basic_note_cloud_storage";

    public static final String PREF_KEY_DOCUMENT_LOAD = "pref_load";
    private static final String PREF_KEY_DOCUMENT_LAST_LOADED = "pref_last_loaded";

    public static final String PREF_KEY_BASIC_NOTE_LOCAL_BACKUP = "pref_basic_note_local_backup";
    private static final String PREF_KEY_BASIC_NOTE_LOCAL_BACKUP_LAST_LOADED = "pref_basic_note_local_backup_last_loaded";

    public static final String PREF_KEY_BASIC_NOTE_LOCAL_RESTORE = "pref_basic_note_local_restore";
    private static final String PREF_KEY_BASIC_NOTE_LOCAL_RESTORE_LAST_LOADED = "pref_basic_note_local_restore_last_loaded";

    public static final String PREF_KEY_BASIC_NOTE_CLOUD_BACKUP =  "pref_basic_note_cloud_backup";
    private static final String PREF_KEY_BASIC_NOTE_CLOUD_BACKUP_LAST_LOADED =  "pref_basic_note_cloud_backup_last_loaded";

    public static final String PREF_KEY_BASIC_NOTE_CLOUD_RESTORE =  "pref_basic_note_cloud_restore";
    private static final String PREF_KEY_BASIC_NOTE_CLOUD_RESTORE_LAST_LOADED =  "pref_basic_note_cloud_restore_last_loaded";

    private static final Map<String, String> PREF_KEYS_LAST_LOADED = new HashMap<>();
    static
    {
        PREF_KEYS_LAST_LOADED.put(PREF_KEY_DOCUMENT_LOAD, PREF_KEY_DOCUMENT_LAST_LOADED);
        PREF_KEYS_LAST_LOADED.put(PREF_KEY_BASIC_NOTE_LOCAL_BACKUP, PREF_KEY_BASIC_NOTE_LOCAL_BACKUP_LAST_LOADED);
        PREF_KEYS_LAST_LOADED.put(PREF_KEY_BASIC_NOTE_LOCAL_RESTORE, PREF_KEY_BASIC_NOTE_LOCAL_RESTORE_LAST_LOADED);
        PREF_KEYS_LAST_LOADED.put(PREF_KEY_BASIC_NOTE_CLOUD_BACKUP, PREF_KEY_BASIC_NOTE_CLOUD_BACKUP_LAST_LOADED);
        PREF_KEYS_LAST_LOADED.put(PREF_KEY_BASIC_NOTE_CLOUD_RESTORE, PREF_KEY_BASIC_NOTE_CLOUD_RESTORE_LAST_LOADED);
    }

    /**
     * Sets and commits Source Path Preference value
     * @param value new value to set
     *
     */
    public static void setSourcePathPreferenceValue(PreferenceFragmentCompat preferenceFragment, String value) {
        preferenceFragment.getPreferenceManager().getSharedPreferences().edit().putString(PREF_KEY_SOURCE_PATH, value).commit();
        preferenceFragment.findPreference(PREF_KEY_SOURCE_PATH).setSummary(value);
    }

    /**
     * Display message common routine
     * @param context Context
     * @param message Message to display
     */
    public static void displayMessage(Context context, CharSequence message) {
        try {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Updated preference summary
     * @param preferenceFragment Fragment
     * @param preferenceKey preference name
     * @param preferenceLastLoadedKey preference last loaded time
     * @param value value to update
     */
    public static void updateLoadPreferenceSummary(PreferenceFragmentCompat preferenceFragment, String preferenceKey, String preferenceLastLoadedKey, long value) {
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

    /**
     * Sets preference long value
     * @param context Context
     * @param preferenceKey preference key
     * @param value value
     */
    private static void setPreferenceLong(Context context, String preferenceKey, long value) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putLong(preferenceKey, value).apply();
    }

    /**
     * Updates last loaded time by preference key
     * @param preferenceFragment PreferenceFragment
     * @param preferenceKey preference key
     * @param value value to set
     */
    public static void updatePreferenceKeySummary(PreferenceFragmentCompat preferenceFragment, String preferenceKey, long value) {
        updateLoadPreferenceSummary(preferenceFragment, preferenceKey, PREF_KEYS_LAST_LOADED.get(preferenceKey), value);
    }

    /**
     * Sets preference last loaded time
     * @param context Context
     * @param preferenceKey preference key
     * @param loadedTime last loaded time
     */
    public static void setPreferenceKeyLastLoadedTime(Context context, String preferenceKey, long loadedTime) {
        setPreferenceLong(context, PREF_KEYS_LAST_LOADED.get(preferenceKey), loadedTime);
    }

    /**
     * Set preference key last loaded time as current time
     * @param context Context
     * @param preferenceKey preference key
     */
    public static void setPreferenceKeyLastLoadedCurrentTime(Context context, String preferenceKey) {
        long loadedTime = System.currentTimeMillis();
        setPreferenceKeyLastLoadedTime(context, preferenceKey, loadedTime);
    }

    /**
     * Set progress preference progress bar visibility
     * @param preferenceFragment PreferenceFragment
     * @param preferenceKey preference key
     * @param v visibility value
     */
    public static void setProgressPreferenceVisibility(PreferenceFragmentCompat preferenceFragment, String preferenceKey, boolean v) {
        Preference preference = preferenceFragment.findPreference(preferenceKey);
        if (preference instanceof ProgressPreference) {
            ProgressPreference progressPreference = (ProgressPreference) preference;
            progressPreference.setProgressVisibility(v);
        }
    }

    public static boolean isInterfaceCheckedLast(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(PreferenceRepository.PREF_KEY_INTERFACE_CHECKED_LAST, false);
    }

    public static int getInterfaceCheckedUpdateInterval(Context context) {
        int prefUpdateInterval = PreferenceManager.getDefaultSharedPreferences(context).getInt(PREF_KEY_INTERFACE_CHECKED_UPDATE_INTERVAL, DEFAULT_CHECKED_UPDATE_REFRESH_INTERVAL);

        String intervalResourceValue = null;
        try {
            intervalResourceValue = context.getResources().getStringArray(R.array.pref_checked_update_interval_entry_values)[prefUpdateInterval];
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }

        if (intervalResourceValue != null) {
            return Integer.valueOf(intervalResourceValue);
        } else {
            return 0;
        }
    }
}
