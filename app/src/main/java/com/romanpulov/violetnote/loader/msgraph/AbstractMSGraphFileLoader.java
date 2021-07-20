package com.romanpulov.violetnote.loader.msgraph;

import android.content.Context;

import com.romanpulov.jutilscore.io.FileUtils;
import com.romanpulov.library.common.loader.core.LoadPathProvider;
import com.romanpulov.library.common.loader.file.FileLoader;
import com.romanpulov.library.msgraph.MSALGetBytesByPathAction;
import com.romanpulov.library.msgraph.MSActionExecutor;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public abstract class AbstractMSGraphFileLoader extends FileLoader {
    public AbstractMSGraphFileLoader(Context context, LoadPathProvider loadPathProvider) {
        super(context, loadPathProvider);
    }

    @Override
    public void load() throws Exception {
        // get data from cloud
        byte[] bytes = MSActionExecutor.executeSync(
                new MSALGetBytesByPathAction(
                        mContext,
                        getLoadPathProvider().getSourcePath(),
                        null
                )
        );

        // copy file to dest
        File destFile = new File(getLoadPathProvider().getDestPath());
        try (
                InputStream inputStream = new ByteArrayInputStream(bytes);
                OutputStream outputStream = new FileOutputStream(destFile)
        ) {
            FileUtils.copyStream(inputStream, outputStream);
        }
    }
}
