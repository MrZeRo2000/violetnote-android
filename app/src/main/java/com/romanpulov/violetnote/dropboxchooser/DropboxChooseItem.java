package com.romanpulov.violetnote.dropboxchooser;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.FolderMetadata;
import com.romanpulov.violetnote.chooser.ChooseItem;

import com.dropbox.core.v2.files.Metadata;
import com.romanpulov.violetnote.dropbox.DropBoxHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by romanpulov on 01.07.2016.
 */
public class DropboxChooseItem implements ChooseItem {
    private final DbxClientV2 mClient;
    private final Metadata mMetaData;

    public DropboxChooseItem(DbxClientV2 client, Metadata metaData) {
        mClient = client;
        mMetaData = metaData;
    }

    @Override
    public String getItemPath() {
        return mMetaData.getPathDisplay();
    }

    @Override
    public String getItemName() {
        return mMetaData.getName();
    }

    @Override
    public int getItemType() {
        if (mMetaData instanceof FileMetadata)
            return ChooseItem.ITEM_FILE;
        else if (mMetaData instanceof FolderMetadata)
            return ChooseItem.ITEM_DIRECTORY;
        else
            return ChooseItem.ITEM_UNKNOWN;
    }

    @Override
    public List<ChooseItem> getItems() {
        List<ChooseItem> result = new ArrayList<>();
        try {
            for (Metadata m : mClient.files().listFolder(mMetaData.getPathDisplay()).getEntries()) {
                result.add(new DropboxChooseItem(mClient, m));
            }
        } catch (DbxException e) {
            throw new RuntimeException(e);
        }
        return result;
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
        String parentItemPath = getParentItemPath(mMetaData.getPathDisplay());
        return fromPath(mClient, parentItemPath);
    }

    public static ChooseItem fromPath(DbxClientV2 client, String path) {
        Metadata metadata = null;
        try {
            metadata = client.files().getMetadata(path);
        } catch (DbxException e) {
            e.printStackTrace();
        }
        if (metadata == null) {
            try {
                metadata = client.files().getMetadata("");
            } catch (DbxException e) {
                e.printStackTrace();
                return null;
            }
        }

        return new DropboxChooseItem(client, metadata);
    }
}
