package com.romanpulov.violetnote;

import org.junit.Test;

import java.io.File;

/**
 * Created by romanpulov on 27.05.2016.
 */
public class FileTest {

    @Test
    public void File1() {
        File f = new File("../data");
        String[] fs = f.list();
        for (String s : fs) {
            System.out.println(s);
        }
    }

}
