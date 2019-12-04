package com.romanpulov.violetnote;

import com.romanpulov.violetnote.chooser.AbstractChooseItem;
import com.romanpulov.violetnote.filechooser.FileChooseItem;

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

    @Test
    public void FileChooseItemTest() {
        FileChooseItem i1 = new FileChooseItem(new File("../data"));
        i1.fillItems();
        for (AbstractChooseItem i : i1.getItems()) {
            System.out.println(i.toString());
        }
    }

}
