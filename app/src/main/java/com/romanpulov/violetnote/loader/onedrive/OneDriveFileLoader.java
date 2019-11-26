package com.romanpulov.violetnote.loader.onedrive;

import android.content.Context;

import com.romanpulov.library.common.loader.core.LoadPathProvider;
import com.romanpulov.library.common.loader.file.FileLoader;
import com.romanpulov.violetnote.onedrive.OneDriveHelper;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class OneDriveFileLoader extends FileLoader {
    private final OneDriveHelper mOneDriveHelper;

    @Override
    public void load() throws Exception {
        String itemId = mBundle.getString("ItemId");
        if (itemId == null) {
            throw new Exception("ItemId not found");
        }

        InputStream inputStream = new BufferedInputStream(mOneDriveHelper.getInputStreamById(itemId));
        File mDestFile = new File(getLoadPathProvider().getDestPath());
        OutputStream outputStream = new FileOutputStream(mDestFile);

        try {
            byte[] buf = new byte[1024];
            int len;
            while ((len = inputStream.read(buf)) > 0) {
                outputStream.write(buf, 0, len);
            }
        }
        finally {
            try {
                outputStream.flush();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @SuppressWarnings({"unused", "SameReturnValue"})
    public static boolean isLoaderInternetRequired() {return true;}

    public OneDriveFileLoader(Context context, LoadPathProvider loadPathProvider) {
        super(context, loadPathProvider);
        mOneDriveHelper = OneDriveHelper.getInstance();
    }
}
