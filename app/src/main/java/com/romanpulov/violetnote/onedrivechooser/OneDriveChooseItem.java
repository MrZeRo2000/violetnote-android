package com.romanpulov.violetnote.onedrivechooser;

import com.romanpulov.violetnote.chooser.ChooseItem;

import java.util.List;

public class OneDriveChooseItem implements ChooseItem {
    private List<ChooseItem> mItems;

    @Override
    public String getItemPath() {
        return null;
    }

    @Override
    public String getDisplayItemPath() {
        return null;
    }

    @Override
    public String getItemName() {
        return null;
    }

    @Override
    public int getItemType() {
        return 0;
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
        return mItems;
    }
}
