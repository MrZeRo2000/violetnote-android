package com.romanpulov.violetnote.onedrivechooser;

import com.romanpulov.violetnote.chooser.AbstractChooseItem;

import java.util.List;

import static com.romanpulov.library.onedrive.OneDriveHelper.ROOT_FULL_PATH;
import static com.romanpulov.library.onedrive.OneDriveHelper.ROOT_PATH;

public class OneDriveRootChooseItem extends AbstractChooseItem {
    private String mPath;

    private OneDriveRootChooseItem() {
        super(AbstractChooseItem.ITEM_PARENT);
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
    public void fillItems() {
        throw new UnsupportedOperationException();
    }
}
