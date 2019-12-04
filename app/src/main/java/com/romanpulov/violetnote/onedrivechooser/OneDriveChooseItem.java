package com.romanpulov.violetnote.onedrivechooser;

import com.onedrive.sdk.extensions.Item;
import com.romanpulov.violetnote.chooser.AbstractChooseItem;
import com.romanpulov.library.onedrive.OneDriveHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class OneDriveChooseItem extends AbstractChooseItem {

    private Item mOneDriveItem;

    private OneDriveChooseItem(Item item) {
        super(getItemTypeFromItem(item));
        mOneDriveItem = item;
    }

    @Override
    public String getItemPath() {
        String parentPath = mOneDriveItem.parentReference.path == null ? "" : mOneDriveItem.parentReference.path;
        parentPath = parentPath.replace(OneDriveHelper.ROOT_FULL_PATH, "");
        if (parentPath.length() > 0) {
            parentPath = parentPath + File.separator;
        }
        return parentPath + mOneDriveItem.name;
    }

    @Override
    public String getDisplayItemPath() {
        return getItemPath();
    }

    @Override
    public String getItemName() {
        return mOneDriveItem.name;
    }

    private static int getItemTypeFromItem(Item item) {
        if (item == null) {
            return AbstractChooseItem.ITEM_UNKNOWN;
        } else if (item.file != null) {
            return AbstractChooseItem.ITEM_FILE;
        } else if (item.folder != null) {
            return AbstractChooseItem.ITEM_DIRECTORY;
        } else {
            return AbstractChooseItem.ITEM_UNKNOWN;
        }
    }

    @Override
    public void fillItems() {
        mItems = new ArrayList<>();
        if (mOneDriveItem != null) {
            List<Item> oneDriveItems = mOneDriveItem.children.getCurrentPage();

            if (oneDriveItems.size() > 0) {
                String parentPath = oneDriveItems.get(0).parentReference.path;
                if (!(parentPath.equals(OneDriveHelper.ROOT_FULL_PATH))) {
                    String parentItemPath = AbstractChooseItem.getParentItemPath(parentPath);
                    mItems.add(OneDriveRootChooseItem.fromPath(parentItemPath));
                }
            }

            for (Item item : oneDriveItems) {
                mItems.add(fromOneDriveItem(item));
            }
        }
    }

    public static OneDriveChooseItem fromOneDriveItem(Item item) {
        return new OneDriveChooseItem(item);
    }

    public static OneDriveChooseItem createErrorItem(String errorText) {
        OneDriveChooseItem result = new OneDriveChooseItem(null);
        result.mFillItemsError = errorText;
        return result;
    }

}
