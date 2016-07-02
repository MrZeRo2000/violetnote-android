package com.romanpulov.violetnote;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.dropbox.core.DbxWebAuth;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.MediaInfo;
import com.dropbox.core.v2.files.Metadata;
import com.romanpulov.violetnote.dropbox.DropBoxHelper;

/**
 * Created by romanpulov on 01.07.2016.
 */
public class DocumentDropboxLoader extends DocumentLoader {
    private DbxClientV2 mClient;
    private DropBoxHelper mDropBoxHelper;

    private static void log(String message) {
        Log.d("DocumentDropboxLoader", message);
    }

    @Override
    protected void load() throws Exception {
        log("Running load DropBox");
        String accessToken = mDropBoxHelper.getAccessToken();
        if (accessToken == null)
            throw new Exception(mContext.getResources().getString(R.string.error_dropbox_auth));
        log("Access token:" + accessToken);

        ListFolderResult folderResult = mClient.files().listFolder("");
        for (Metadata m : folderResult.getEntries()) {
            log(m.toString());
        }
    }

    public DocumentDropboxLoader(Context context) {
        super(context);
        mLoadAppearance = LOAD_APPEARANCE_ASYNC;
        mDropBoxHelper = DropBoxHelper.getInstance(context.getApplicationContext());
        mClient = mDropBoxHelper.getClient();
    }
}
