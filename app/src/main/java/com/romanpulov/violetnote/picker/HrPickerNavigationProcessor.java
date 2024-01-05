package com.romanpulov.violetnote.picker;

import java.util.List;

public interface HrPickerNavigationProcessor {
    void onNavigationSuccess(String path, List<HrPickerItem> items);
    void onNavigationFailure(String errorMessage);
}
