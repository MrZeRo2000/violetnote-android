package com.romanpulov.violetnote.picker;

import android.content.Context;

public interface HrPickerNavigator {
    void onNavigate(Context context, String path, HrPickerNavigationProcessor processor);
}
