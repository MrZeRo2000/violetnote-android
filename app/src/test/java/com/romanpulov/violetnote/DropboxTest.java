package com.romanpulov.violetnote;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by romanpulov on 02.07.2016.
 */
public class DropboxTest {

    String getParentItemPath(String path) {
        int iPath = path.lastIndexOf("/");
        if (iPath>-1)
            return path.substring(0, iPath);
        else
            return "";
    }

    @Test
    public void getParentItemTest() {
        String path1 = "/Temp/ff";
        String path1Result = "/Temp";
        String path2 = "/Temp";
        String path2Result = "";
        String path3 = "Temp";
        String path3Result = "";


        assertEquals(getParentItemPath(path1), path1Result);
        assertEquals(getParentItemPath(path2), path2Result);
        assertEquals(getParentItemPath(path3), path3Result);
    }

}
