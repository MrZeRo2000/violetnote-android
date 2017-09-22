package com.romanpulov.violetnote.loader;

import android.content.Context;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.Metadata;
import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.dropbox.DropBoxHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * DropBox file loader
 * Created by romanpulov on 01.07.2016. *
 */
public class DropboxFileLoader extends FileLoader {
    private final DbxClientV2 mClient;
    private final DropBoxHelper mDropBoxHelper;

    @Override
    protected void load() throws Exception {
        String accessToken = mDropBoxHelper.getAccessToken();
        if (accessToken == null)
            throw new Exception(mContext.getResources().getString(R.string.error_dropbox_auth));

        Metadata m = mClient.files().getMetadata(getLoadPathProvider().getSourcePath());
        if ((m == null) || !(m instanceof FileMetadata))
            throw new Exception(String.format(mContext.getText(R.string.error_dropbox_load_file_data).toString(), getLoadPathProvider().getSourcePath()));

        FileMetadata fm = (FileMetadata) m;

        File mDestFile = new File(getLoadPathProvider().getDestPath());
        OutputStream outputStream = new FileOutputStream(mDestFile);
        try {
            mClient.files().download(fm.getPathLower(), fm.getRev()).download(outputStream);
        } finally {
            try {
                outputStream.flush();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean isInternetRequired() {
        return true;
    }

    public DropboxFileLoader(Context context, LoadPathProvider loadPathProvider) {
        super(context, loadPathProvider);
        //appearance
        mLoadAppearance = LOAD_APPEARANCE_ASYNC;

        //dropbox
        mDropBoxHelper = DropBoxHelper.getInstance(context.getApplicationContext());
        mClient = mDropBoxHelper.getClient();
    }
}
