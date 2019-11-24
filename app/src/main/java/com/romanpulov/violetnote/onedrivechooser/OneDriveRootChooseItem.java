package com.romanpulov.violetnote.onedrivechooser;

import com.romanpulov.violetnote.chooser.ChooseItem;

import java.util.List;

public class OneDriveRootChooseItem implements ChooseItem {
    private String mPath;

    private OneDriveRootChooseItem() {

    }

    public static OneDriveRootChooseItem fromPath(String path) {
        OneDriveRootChooseItem instance = new OneDriveRootChooseItem();
        instance.mPath = path;
        return instance;
    }

    @Override
    public String getItemPath() {
        return mPath.replace("/drive/root", "root");
    }

    @Override
    public String getDisplayItemPath() {
        return getItemPath().replace("root:", "");
    }

    @Override
    public String getItemName() {
        return null;
    }

    @Override
    public int getItemType() {
        return ChooseItem.ITEM_PARENT;
    }

    @Override
    public void fillItems() {

    }

    @Override
    public String getFillItemsError() {
        return null;
    }

    @Override
    public List<ChooseItem> getItems() {
        return null;
    }
}
