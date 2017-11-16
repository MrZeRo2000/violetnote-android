package com.romanpulov.violetnote.loader;

import android.content.Context;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.Metadata;
import com.romanpulov.violetnote.R;
import com.romanpulov.library.dropbox.DropboxHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Base class for DropBox file loader
 * Created by romanpulov on 01.07.2016. *
 */
public abstract class DropboxFileLoader extends FileLoader {
    private final DropboxHelper mDropboxHelper;

    @Override
    public void load() throws Exception {
        String accessToken = mDropboxHelper.getAccessToken();
        if (accessToken == null)
            throw new Exception(mContext.getResources().getString(R.string.error_dropbox_auth));

        DbxClientV2 dbxClient = mDropboxHelper.getClient();

        Metadata m = dbxClient.files().getMetadata(getLoadPathProvider().getSourcePath());
        if ((m == null) || !(m instanceof FileMetadata))
            throw new Exception(String.format(mContext.getText(R.string.error_dropbox_load_file_data).toString(), getLoadPathProvider().getSourcePath()));

        FileMetadata fm = (FileMetadata) m;

        File mDestFile = new File(getLoadPathProvider().getDestPath());
        OutputStream outputStream = new FileOutputStream(mDestFile);
        try {
            dbxClient.files().download(fm.getPathLower(), fm.getRev()).download(outputStream);
        } finally {
            try {
                outputStream.flush();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressWarnings("unused")
    public static boolean isLoaderInternetRequired() {return true;}

    public DropboxFileLoader(Context context, LoadPathProvider loadPathProvider) {
        super(context, loadPathProvider);

        //dropbox
        mDropboxHelper = DropboxHelper.getInstance(context.getApplicationContext());
    }
}
