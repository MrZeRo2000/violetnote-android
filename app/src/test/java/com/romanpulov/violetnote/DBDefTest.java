package com.romanpulov.violetnote;

import com.romanpulov.violetnote.db.tabledef.DBDefFactory;

import org.junit.Assert;
import org.junit.Test;

public class DBDefTest {
    @Test
    public void testDBCreate() {
        for (String s : DBDefFactory.buildDBCreate()) {
            System.out.println(s);
        }
    }

    @Test
    public void testDBUpgradeFrom2() {
        for (String s : DBDefFactory.buildDBUpgrade(2)) {
            System.out.println(s);
        }
    }
}
