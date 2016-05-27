package com.romanpulov.violetnote;

import com.romanpulov.violetnote.HrChooser.ChooseItem;
import com.romanpulov.violetnote.HrChooser.FileChooseItem;

import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;
/**
 * Created by romanpulov on 27.05.2016.
 */
public class FileTest {

    @Test
    public void File1() {
        File f = new File("C:/Temp");
        String[] fs = f.list();
        for (String s : fs) {
            System.out.println(s);
        }
    }

    @Test
    public void FileChooseItemTest() {
        FileChooseItem i1 = new FileChooseItem(new File("C:/Temp"));
        for (ChooseItem i : i1.getItems()) {
            System.out.println(i.toString());
        }
    }

}