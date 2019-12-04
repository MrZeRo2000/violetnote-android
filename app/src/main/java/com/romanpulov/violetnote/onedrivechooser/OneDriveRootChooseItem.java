package com.romanpulov.violetnote.onedrivechooser;

import com.romanpulov.violetnote.chooser.ChooseItem;

import java.util.List;

import static com.romanpulov.library.onedrive.OneDriveHelper.ROOT_FULL_PATH;
import static com.romanpulov.library.onedrive.OneDriveHelper.ROOT_PATH;

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
        return mPath.replace(ROOT_FULL_PATH, "");
    }

    @Override
    public String getDisplayItemPath() {
        return getItemPath().replace(ROOT_PATH, "");
    }

    @Override
    public String getItemName() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getItemType() {
        return ChooseItem.ITEM_PARENT;
    }

    @Override
    public void fillItems() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getFillItemsError() {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<ChooseItem> getItems() {
        throw new UnsupportedOperationException();
    }
}
