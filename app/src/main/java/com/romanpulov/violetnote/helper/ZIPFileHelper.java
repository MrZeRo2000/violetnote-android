package com.romanpulov.violetnote.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by romanpulov on 29.09.2016.
 */

public class ZIPFileHelper {
    private static String ZIP_EXT = ".zip";

    private static String getZipFileName(String fileName){
        return fileName.substring(0, fileName.lastIndexOf(".")) + ZIP_EXT;
    }

    public static boolean zipFile(String filePath, String fileName) {
        File sourceFile = new File(filePath + fileName);
        if (!sourceFile.exists())
            return false;

        File zipFile = new File(filePath + getZipFileName(fileName));

        InputStream inputStream = null;
        ZipOutputStream zipOutputStream = null;
        try {
            //init streams and entry
            inputStream = new FileInputStream(sourceFile);
            zipOutputStream = new ZipOutputStream(new FileOutputStream(zipFile));

            //next entry
            zipOutputStream.putNextEntry(new ZipEntry(fileName));

            //write
            byte[] buffer = new byte[1024];
            int len;
            while ((len = inputStream.read(buffer)) > 0) {
                zipOutputStream.write(buffer, 0, len);
            }

            //complete entry
            zipOutputStream.closeEntry();
        } catch (IOException e) {
            return false;
        } finally {
            //close input
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            //close output
            if (zipOutputStream != null) {
                try {
                    zipOutputStream.flush();
                    zipOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return true;
    }
}
