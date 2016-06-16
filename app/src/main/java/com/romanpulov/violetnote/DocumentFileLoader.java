package com.romanpulov.violetnote;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * Created by romanpulov on 15.06.2016.
 */
public class DocumentFileLoader extends DocumentLoader {
    @Override
    protected void load() throws Exception {
        InputStream inputStream = new FileInputStream(mSourcePath);
        FileOutputStream outputStream = new FileOutputStream(mDestFile);
        try {
            byte[] buf = new byte[1024];
            int len;
            while ((len = inputStream.read(buf)) > 0)
                outputStream.write(buf, 0, len);
        } finally {
            inputStream.close();
            outputStream.flush();
            outputStream.close();
        }
    }

    public DocumentFileLoader(Context context) {
        super(context);
    }
}
