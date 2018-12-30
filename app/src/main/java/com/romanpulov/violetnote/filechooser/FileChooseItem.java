package com.romanpulov.violetnote.filechooser;

import android.support.annotation.NonNull;

import com.romanpulov.violetnote.chooser.ChooseItem;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * ChooseItem implementation for choosing file on device
 * Created by romanpulov on 27.05.2016.
 */
public class FileChooseItem implements ChooseItem {
    private final File mFile;

    @Override
    public String getFillItemsError() {
        return null;
    }

    private final String mPath;
    private final String mName;
    private int mItemType;
    private List<ChooseItem> mItems;

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
    public String getDisplayItemPath() {
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
    public void fillItems() {
        mItems = new ArrayList<>();
        File parentFile = mFile.getParentFile();
        if (parentFile != null)
            mItems.add(newParentItem(parentFile));
        File[] listFiles = mFile.listFiles();
        if (listFiles != null) {
            for (File f : mFile.listFiles()) {
                if (f.canRead())
                    mItems.add(new FileChooseItem(f));
            }
        }
    }

    @Override
    public List<ChooseItem> getItems() {
        return mItems;
    }

    @Override
    @NonNull
    public String toString() {
        return "{[Path=" + getItemPath() + "], [Name=" + getItemName() + "], [ItemType=" + getItemType() + "]}";
    }
}
