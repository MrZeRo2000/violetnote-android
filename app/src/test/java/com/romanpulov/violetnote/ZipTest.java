package com.romanpulov.violetnote;

import com.romanpulov.violetnote.helper.ZIPFileHelper;

import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import static org.junit.Assert.assertTrue;

/**
 * Created by romanpulov on 29.09.2016.
 */

public class ZipTest {
    private static final String FILE_PATH = "../data/";
    private static final String FILE_NAME = "ziptest.txt";
    private static final String ZIP_FILE_NAME = "ziptest.zip";

    @Test
    public void test1() {
        assertTrue(1==1);
    }

    public void clearTestFile() throws Exception{
        File f = new File(FILE_PATH + FILE_NAME);
        if (f.exists())
            assertTrue(f.delete());

        f = new File(FILE_PATH + ZIP_FILE_NAME);
        if (f.exists())
            assertTrue(f.delete());
    }

    public void createTestFile() throws Exception{
        String fileName = FILE_PATH + FILE_NAME;
        File f = new File(fileName);
        FileWriter fw = new FileWriter(f);

        StringWriter wr = new StringWriter();
        wr.append("Test line 1\n");
        wr.append("Test line 2\n");

        fw.write(wr.toString());

        fw.flush();
        fw.close();

    }

    public void zipTest() throws Exception{
        createTestFile();

        String zipFileName = FILE_PATH + ZIP_FILE_NAME;

        ZipEntry ze = new ZipEntry(FILE_NAME);

        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFileName));
        zos.putNextEntry(ze);

        String fileName = FILE_PATH + FILE_NAME;

        FileInputStream is = new FileInputStream(fileName);

        byte[] buffer = new byte[1024];
        int len;
        while ((len = is.read(buffer)) > 0) {
            zos.write(buffer, 0, len);
        }

        is.close();
        zos.closeEntry();

        zos.flush();
        zos.close();

        System.out.println(fileName);
        System.out.println(fileName.substring(0, fileName.lastIndexOf(".")));
    }

    @Test
    public void ZipFileHelperTest() throws Exception {
        clearTestFile();

        createTestFile();

        assertTrue(ZIPFileHelper.zipFile(FILE_PATH, FILE_NAME));
    }
}
