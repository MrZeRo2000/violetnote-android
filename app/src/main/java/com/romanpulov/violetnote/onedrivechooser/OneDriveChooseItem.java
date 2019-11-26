package com.romanpulov.violetnote.onedrivechooser;

import com.onedrive.sdk.extensions.Item;
import com.romanpulov.violetnote.chooser.ChooseItem;
import com.romanpulov.violetnote.chooser.ChooseItemUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class OneDriveChooseItem implements ChooseItem {
    public static final String ROOT_FULL_PATH = "/drive/root:";
    public static final String ROOT_PATH = "root:";

    private Item mOneDriveItem;
    private String mErrorText;

    private OneDriveChooseItem() {

    }

    private List<ChooseItem> mItems;

    @Override
    public String getItemPath() {
        String parentPath = mOneDriveItem.parentReference.path == null ? "" : mOneDriveItem.parentReference.path;
        parentPath = parentPath.replace(ROOT_FULL_PATH, "");
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

    @Override
    public int getItemType() {
        if (mOneDriveItem.file != null) {
            return ChooseItem.ITEM_FILE;
        } else if (mOneDriveItem.folder != null) {
            return ChooseItem.ITEM_DIRECTORY;
        } else {
            return ChooseItem.ITEM_UNKNOWN;
        }
    }

    @Override
    public void fillItems() {
        mItems = new ArrayList<>();
        if (mOneDriveItem != null) {
            List<Item> oneDriveItems = mOneDriveItem.children.getCurrentPage();

            if (oneDriveItems.size() > 0) {
                String parentPath = oneDriveItems.get(0).parentReference.path;
                if (!(parentPath.equals(ROOT_FULL_PATH))) {
                    String parentItemPath = ChooseItemUtils.getParentItemPath(parentPath);
                    mItems.add(OneDriveRootChooseItem.fromPath(parentItemPath));
                }
            }

            for (Item item : oneDriveItems) {
                mItems.add(fromOneDriveItem(item));
            }
        }
    }

    @Override
    public String getFillItemsError() {
        return mErrorText;
    }

    @Override
    public List<ChooseItem> getItems() {
        return mItems;
    }

    public static OneDriveChooseItem fromOneDriveItem(Item item) {
        OneDriveChooseItem result = new OneDriveChooseItem();
        result.mOneDriveItem = item;
        return result;
    }

    public static OneDriveChooseItem createErrorItem(String errorText) {
        OneDriveChooseItem result = new OneDriveChooseItem();
        result.mErrorText = errorText;
        return result;
    }

}
