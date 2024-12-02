package com.romanpulov.violetnote.picker;

import android.content.Context;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class HrPickerViewModel extends ViewModel {

    private final HrPickerNavigationProcessor navigationProcessor = new HrPickerNavigationProcessor() {
        @Override
        public void onNavigationSuccess(String path, List<HrPickerItem> items) {
            HrPickerScreen currentScreen = hrPickerScreen.getValue();
            if (currentScreen != null) {
                currentScreen.finishNavigationSuccess(path, items);
                getHrPickerScreen().postValue(currentScreen);
            }
        }

        @Override
        public void onNavigationFailure(String errorMessage) {
            HrPickerScreen currentScreen = hrPickerScreen.getValue();
            if (currentScreen != null) {
                currentScreen.finishNavigationFailure(errorMessage);
                getHrPickerScreen().postValue(currentScreen);
            }
        }
    };

    private final MutableLiveData<HrPickerScreen> hrPickerScreen = new MutableLiveData<>();

    public MutableLiveData<HrPickerScreen> getHrPickerScreen() {
        return hrPickerScreen;
    }

    private HrPickerNavigator mNavigator;

    public HrPickerNavigator getNavigator() {
        return mNavigator;
    }

    public void setNavigator(HrPickerNavigator mNavigator) {
        this.mNavigator = mNavigator;
    }

    public void navigate(Context context, String path, HrPickerItem item) {
        HrPickerScreen currentScreen = hrPickerScreen.getValue();
        if (currentScreen != null) {
            currentScreen.startNavigation();
            this.mNavigator.onNavigate(context, path, navigationProcessor);
        }
    }
}
