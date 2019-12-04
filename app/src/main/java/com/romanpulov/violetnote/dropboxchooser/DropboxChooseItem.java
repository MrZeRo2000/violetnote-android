package com.romanpulov.violetnote.dropboxchooser;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.FolderMetadata;
import com.romanpulov.violetnote.chooser.AbstractChooseItem;

import com.dropbox.core.v2.files.Metadata;

import java.util.ArrayList;
import java.util.List;

/**
 * AbstractChooseItem implementation for DropBox
 * Created by romanpulov on 01.07.2016.
 */
public class DropboxChooseItem extends AbstractChooseItem {
    private final DbxClientV2 mClient;
    private final Metadata mMetaData;


    private static int getItemTypeFromMetaData(Metadata metaData) {
        if (metaData == null) {
            return AbstractChooseItem.ITEM_DIRECTORY;
        }
        else if (metaData instanceof FileMetadata) {
            return AbstractChooseItem.ITEM_FILE;
        }
        else if (metaData instanceof FolderMetadata) {
            return AbstractChooseItem.ITEM_DIRECTORY;
        }
        else {
            return AbstractChooseItem.ITEM_UNKNOWN;
        }
    }

    private DropboxChooseItem(DbxClientV2 client, Metadata metaData) {
        super(getItemTypeFromMetaData(metaData));
        mClient = client;
        mMetaData = metaData;
    }

    @Override
    public void fillItems() {
        mItems = new ArrayList<>();

        // for non root folder
        if (mMetaData != null) {
            DropboxChooseItem newItem = (DropboxChooseItem)getParentItem();
            newItem.mItemType = AbstractChooseItem.ITEM_PARENT;
            mItems.add(newItem);
        }

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
    public String getItemPath() {
        if (mMetaData == null)
            return "";
        else
            return mMetaData.getPathDisplay();
    }

    @Override
    public String getDisplayItemPath() {
        if (mMetaData == null)
            return "/";
        else
            return getItemPath();
    }

    @Override
    public String getItemName() {
        if (mMetaData == null)
            return null;
        else
            return mMetaData.getName();
    }

    public static String getParentItemPath(String path) {
        int iPath = path.lastIndexOf("/");
        if (iPath>-1)
            return path.substring(0, iPath);
        else
            return "";
    }

    private AbstractChooseItem getParentItem() {
        String parentItemPath = getParentItemPath(getItemPath());
        return fromPath(mClient, parentItemPath);
    }

    public static AbstractChooseItem fromPath(DbxClientV2 client, String path) {
        Metadata metadata = null;

        if (!path.isEmpty()) {
            try {
                metadata = client.files().getMetadata(path);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return new DropboxChooseItem(client, metadata);
    }
}
