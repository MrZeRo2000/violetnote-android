package com.romanpulov.violetnote.picker;

import android.util.Log;
import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class HrPickerScreen {
    public final static String TAG = HrPickerScreen.class.getSimpleName();

    public final static int PICKER_SCREEN_STATUS_READY = 0;
    public final static int PICKER_SCREEN_STATUS_LOADING = 1;
    public final static int PICKER_SCREEN_STATUS_ERROR = 2;

    private int mStatus;

    public int getStatus() {
        return mStatus;
    }

    private String mCurrentPath;

    public String getCurrentPath() {
        return mCurrentPath;
    }

    private String mParentPath;

    public String getParentPath() {
        return mParentPath;
    }

    private final List<HrPickerItem> mItems = new ArrayList<>();

    public List<HrPickerItem> getItems() {
        return mItems;
    }

    private String mErrorMessage;

    public String getErrorMessage() {
        return mErrorMessage;
    }

    public void startNavigation() {
        mErrorMessage = null;
        mStatus = HrPickerScreen.PICKER_SCREEN_STATUS_LOADING;
    }

    public void finishNavigationSuccess(String path, List<HrPickerItem> items) {
        mStatus = HrPickerScreen.PICKER_SCREEN_STATUS_READY;
        mErrorMessage = null;
        mCurrentPath = path;
        mParentPath = getParentFromPath(mCurrentPath);

        Log.d(TAG, "currentPath=" + mCurrentPath + ", parentPath=" + mParentPath);

        mItems.clear();
        if (!mParentPath.isEmpty()) {
            mItems.add(new HrPickerItem(HrPickerItem.ITEM_TYPE_PARENT, null));
        }
        mItems.addAll(items);
        mItems.sort(new HrPickerItemComparator());
    }

    public void finishNavigationFailure(String errorMessage) {
        mStatus = HrPickerScreen.PICKER_SCREEN_STATUS_ERROR;
        mErrorMessage = errorMessage;
    }

    public HrPickerScreen(String currentPath) {
        this.mCurrentPath = currentPath;
    }

    public static String getParentFromPath(String path) {
        int iPath = path.lastIndexOf("/");
        if ((iPath < 0) || path.equals("/")) {
            return "";
        } else if (iPath == 0) {
            return "/";
        } else {
            return path.substring(0, iPath);
        }
    }

    public static String combinePath(String path, HrPickerItem item) {
        return ((item == null) || (item.itemType == HrPickerItem.ITEM_TYPE_PARENT)) ?
                path :
                path + (path.endsWith("/") ? "" : "/") + item.name;
    }

    @Override
    public @NonNull String toString() {
        return "HrPickerScreen{" +
                "mStatus=" + mStatus + "(" +
                (mStatus == 0 ? "Ready" : mStatus == 1 ? "Loading" : mStatus == 2 ? "Error" : "Unknown" )
                + ")" +
                ", mCurrentPath='" + mCurrentPath + '\'' +
                ", mParentPath='" + mParentPath + '\'' +
                ", mItems=" + mItems +
                ", mErrorMessage='" + mErrorMessage + '\'' +
                '}';
    }
}
