package com.romanpulov.violetnote.filechooser;

import android.support.annotation.NonNull;

import com.romanpulov.violetnote.chooser.AbstractChooseItem;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * AbstractChooseItem implementation for choosing file on device
 * Created by romanpulov on 27.05.2016.
 */
public class FileChooseItem extends AbstractChooseItem {
    private final File mFile;

    private final String mPath;
    private final String mName;

    private static FileChooseItem newParentItem(File file) {
        FileChooseItem newItem = new FileChooseItem(file);
        newItem.mItemType = AbstractChooseItem.ITEM_PARENT;
        return newItem;
    }

    private static int getItemTypeFromFile(@NonNull File file) {
        if (file.isDirectory())
            return AbstractChooseItem.ITEM_DIRECTORY;
        else if (file.isFile())
            return AbstractChooseItem.ITEM_FILE;
        else
            return AbstractChooseItem.ITEM_UNKNOWN;
    }

    public FileChooseItem(File file) {
        super(getItemTypeFromFile(file));
        mFile = file;
        mPath = mFile.getAbsolutePath();
        mName = mFile.getName();
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
    @NonNull
    public String toString() {
        return "{[Path=" + getItemPath() + "], [Name=" + getItemName() + "], [ItemType=" + getItemType() + "]}";
    }
}
