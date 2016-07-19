package com.romanpulov.violetnote.document;

import android.content.Context;
import android.util.Log;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.Metadata;
import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.dropbox.DropBoxHelper;

import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * Created by romanpulov on 01.07.2016.
 */
public class DocumentDropboxLoader extends DocumentLoader {
    private DbxClientV2 mClient;
    private DropBoxHelper mDropBoxHelper;

    @Override
    protected void load() throws Exception {
        String accessToken = mDropBoxHelper.getAccessToken();
        if (accessToken == null)
            throw new Exception(mContext.getResources().getString(R.string.error_dropbox_auth));

        Metadata m = mClient.files().getMetadata(mSourcePath);
        if ((m == null) || !(m instanceof FileMetadata))
            throw new Exception(String.format(mContext.getText(R.string.error_dropbox_load_file_data).toString(), mSourcePath));

        FileMetadata fm = (FileMetadata) m;

        OutputStream outputStream = new FileOutputStream(mDestFile);
        try {
            mClient.files().download(fm.getPathLower(), fm.getRev()).download(outputStream);
        } finally {
            outputStream.flush();
            outputStream.close();
        }
    }

    public DocumentDropboxLoader(Context context) {
        super(context);
        mLoadAppearance = LOAD_APPEARANCE_ASYNC;
        mDropBoxHelper = DropBoxHelper.getInstance(context.getApplicationContext());
        mClient = mDropBoxHelper.getClient();
    }
}
