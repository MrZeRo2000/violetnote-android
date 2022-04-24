package com.romanpulov.violetnote;

import android.app.Application;

import com.romanpulov.library.common.logger.BaseLoggerHelper;

public class VioletNoteApplication extends Application {
    static {
        BaseLoggerHelper.configure("VioletNote", "Log");
    }
}
