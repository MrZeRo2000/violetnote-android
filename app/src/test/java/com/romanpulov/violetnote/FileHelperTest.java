package com.romanpulov.violetnote;

import com.romanpulov.violetnote.helper.FileHelper;

import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;

import static org.junit.Assert.assertTrue;

/**
 * Created by romanpulov on 06.10.2016.
 */

public class FileHelperTest {
    private static final String FILE_PATH = "../data/";
    private static final String FILE_NAME = "filetest.txt";

    @Test
    public void test1() {
        assertTrue(1==1);
    }

    @Test
    public void test2() throws Exception{
        String fileName = FILE_PATH + FILE_NAME;
        String tempFileName = FileHelper.getTempFileName(fileName);

        // delete existing
        File f = new File(tempFileName);
        if (f.exists())
            f.delete();

        //create new
        FileOutputStream outputStream = new FileOutputStream(f);
        outputStream.write(32 + (int)(Math.random() * 50));
        outputStream.flush();
        outputStream.close();

        //save copies
        FileHelper.saveCopies(fileName);
        //set new
        FileHelper.renameTempFile(tempFileName);
    }

}
