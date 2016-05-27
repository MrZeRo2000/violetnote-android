package com.romanpulov.violetnote.HrChooser;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by romanpulov on 27.05.2016.
 */
public class FileChooseItem implements ChooseItem {
    private File mParentFile;
    private File mFile;

    public FileChooseItem(File file, File parentFile) {
        this(file);
        mParentFile = parentFile;
    }

    public FileChooseItem(File file) {
        mFile = file;
    }

    @Override
    public String getItemPath() {
        return mFile.getAbsolutePath();
    }

    @Override
    public String getItemName() {
        return mFile.getName();
    }

    @Override
    public int getItemType() {
        if (mFile.isDirectory())
            return ChooseItem.ITEM_DIRECTORY;
        else if (mFile.isFile())
            return ChooseItem.ITEM_FILE;
        else
            return ChooseItem.ITEM_UNKNOWN;
    }

    @Override
    public List<ChooseItem> getItems() {
        List<ChooseItem> result = new ArrayList<>();
        for (File f : mFile.listFiles()) {
            result.add(new FileChooseItem(f, mFile));
        }
        return result;
    }

    @Override
    public String toString() {
        return "{[Path=" + getItemPath() + "], [Name=" + getItemName() + "], [ItemType=" + getItemType() + "]}";
    }
}
