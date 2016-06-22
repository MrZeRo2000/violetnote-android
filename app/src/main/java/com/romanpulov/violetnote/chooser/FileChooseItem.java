package com.romanpulov.violetnote.chooser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by romanpulov on 27.05.2016.
 */
public class FileChooseItem implements ChooseItem {
    private File mFile;
    private String mPath;
    private String mName;
    private int mItemType;

    private static FileChooseItem newParentItem(File file) {
        FileChooseItem newItem = new FileChooseItem(file);
        newItem.mItemType = ChooseItem.ITEM_PARENT;
        return newItem;
    }

    public FileChooseItem(File file) {
        mFile = file;
        mPath = mFile.getAbsolutePath();
        mName = mFile.getName();
        if (mFile.isDirectory())
            mItemType = ChooseItem.ITEM_DIRECTORY;
        else if (mFile.isFile())
            mItemType = ChooseItem.ITEM_FILE;
        else
            mItemType = ChooseItem.ITEM_UNKNOWN;
    }

    @Override
    public String getItemPath() {
        return mPath;
    }

    @Override
    public String getItemName() {
        return mName;
    }

    @Override
    public int getItemType() {
        return mItemType;
    }

    @Override
    public List<ChooseItem> getItems() {
        List<ChooseItem> result = new ArrayList<>();
        File parentFile = mFile.getParentFile();
        if (parentFile != null)
            result.add(newParentItem(parentFile));
        File[] listFiles = mFile.listFiles();
        if (listFiles != null) {
            for (File f : mFile.listFiles()) {
                if (f.canRead())
                    result.add(new FileChooseItem(f));
            }
        }
        return result;
    }

    public ChooseItem getParentItem() {
        File parentFile = mFile.getParentFile();
        if (parentFile != null)
            return new FileChooseItem(parentFile);
        else
            return null;
    }

    @Override
    public String toString() {
        return "{[Path=" + getItemPath() + "], [Name=" + getItemName() + "], [ItemType=" + getItemType() + "]}";
    }
}