package com.romanpulov.violetnote.dropboxchooser;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.FolderMetadata;
import com.romanpulov.violetnote.chooser.ChooseItem;

import com.dropbox.core.v2.files.Metadata;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by romanpulov on 01.07.2016.
 */
public class DropboxChooseItem implements ChooseItem {
    private final DbxClientV2 mClient;
    private final Metadata mMetaData;
    private List<ChooseItem> mItems;
    private String mFillItemsError;

    public DropboxChooseItem(DbxClientV2 client, Metadata metaData) {
        mClient = client;
        mMetaData = metaData;
    }

    @Override
    public void fillItems() {
        mItems = new ArrayList<>();
        try {
            for (Metadata m : mClient.files().listFolder(getItemPath()).getEntries()) {
                mItems.add(new DropboxChooseItem(mClient, m));
            }
        } catch (DbxException e) {
            e.printStackTrace();
            mFillItemsError = e.getMessage();
        }
    }

    @Override
    public String getFillItemsError() {
        return mFillItemsError;
    }

    @Override
    public String getItemPath() {
        if (mMetaData == null)
            return "";
        else
            return mMetaData.getPathDisplay();
    }

    @Override
    public String getItemName() {
        if (mMetaData == null)
            return null;
        else
            return mMetaData.getName();
    }

    @Override
    public int getItemType() {
        if (mMetaData == null)
            return ChooseItem.ITEM_DIRECTORY;

        if (mMetaData instanceof FileMetadata)
            return ChooseItem.ITEM_FILE;
        else if (mMetaData instanceof FolderMetadata)
            return ChooseItem.ITEM_DIRECTORY;
        else
            return ChooseItem.ITEM_UNKNOWN;
    }

    @Override
    public List<ChooseItem> getItems() {
        return mItems;
    }

    private String getParentItemPath(String path) {
        int iPath = path.lastIndexOf("/");
        if (iPath>-1)
            return path.substring(0, iPath);
        else
            return "";
    }

    @Override
    public ChooseItem getParentItem() {
        String parentItemPath = getParentItemPath(getItemPath());
        return fromPath(mClient, parentItemPath);
    }

    public static ChooseItem fromPath(DbxClientV2 client, String path) {
        Metadata metadata = null;
        try {
            metadata = client.files().getMetadata(path);
        } catch (DbxException e) {
            e.printStackTrace();
        }

        return new DropboxChooseItem(client, metadata);
    }
}
