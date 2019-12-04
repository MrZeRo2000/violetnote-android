package com.romanpulov.violetnote.chooser;

import java.util.List;

/**
 * Created by romanpulov on 27.05.2016.
 * General abstract class for Choose Item
 */
public abstract class AbstractChooseItem {
    public static final int ITEM_UNKNOWN = 10;
    public static final int ITEM_PARENT = 1;
    public static final int ITEM_DIRECTORY = 2;
    public static final int ITEM_FILE = 3;

    protected int mItemType;

    public final int getItemType() {
        return mItemType;
    };

    protected String mFillItemsError;

    public final String getFillItemsError() {
        return mFillItemsError;
    };

    protected List<AbstractChooseItem> mItems;

    public final List<AbstractChooseItem> getItems() {
        return mItems;
    };

    public AbstractChooseItem(int itemType) {
        mItemType = itemType;
    }

    public static String getParentItemPath(String path) {
        int iPath = path.lastIndexOf("/");
        if (iPath>-1)
            return path.substring(0, iPath);
        else
            return "";
    }

    public abstract String getItemPath();
    public abstract String getDisplayItemPath();
    public abstract String getItemName();
    protected abstract void fillItems();
}
