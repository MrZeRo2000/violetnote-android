package com.romanpulov.violetnote.view.helper;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;

import com.romanpulov.library.common.logger.BaseLoggerHelper;
import com.romanpulov.violetnote.view.preference.PreferenceRepository;

public class LoggerHelper extends BaseLoggerHelper {
    static {
        BaseLoggerHelper.configure("VioletNote", "Log");
    }

    @SuppressLint("StaticFieldLeak")
    private static LoggerHelper INSTANCE;

    public static LoggerHelper getInstance(Context context) {
        LoggerHelper.validateConfiguration();

        if (INSTANCE == null) {
            INSTANCE = new LoggerHelper(context.getApplicationContext());
        }
        return INSTANCE;
    }


    public LoggerHelper(@NonNull Context context) {
        super(context.getApplicationContext());
        setEnableLogging(PreferenceManager
                .getDefaultSharedPreferences(context)
                .getBoolean(PreferenceRepository.PREF_KEY_LOGGING, false)
        );
    }

    public static void logContext(Context context, String tag, String message) {
        if (context != null) {
            getInstance(context).log(tag, message);
        }
    }

    public static void unconditionalLogContext(Context context, String tag, String message) {
        if (context != null) {
            getInstance(context).unconditionalLog(tag, message);
        }
    }

}
