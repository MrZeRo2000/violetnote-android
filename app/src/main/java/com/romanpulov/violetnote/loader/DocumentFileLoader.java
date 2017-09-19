package com.romanpulov.violetnote.loader;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * File document loader
 * Created by romanpulov on 15.06.2016.
 */
public class DocumentFileLoader extends DocumentLoader {
    @Override
    protected void load() throws Exception {
        InputStream inputStream = new FileInputStream(mSourcePath);
        OutputStream outputStream = new FileOutputStream(new File(mDestPath));
        try {
            byte[] buf = new byte[1024];
            int len;
            while ((len = inputStream.read(buf)) > 0)
                outputStream.write(buf, 0, len);
        } finally {
            try {
                inputStream.close();
                outputStream.flush();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean isInternetRequired() {
        return false;
    }

    public DocumentFileLoader(Context context) {
        super(context);
    }
}
