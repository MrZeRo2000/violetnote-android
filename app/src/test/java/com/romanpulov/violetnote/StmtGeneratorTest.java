package com.romanpulov.violetnote;

import com.romanpulov.violetnote.db.tabledef.StmtGenerator;

import org.junit.Assert;
import org.junit.Test;

public class StmtGeneratorTest {
    @Test
    public void testCreate() {
        String createStmt = StmtGenerator.createTableStatement(
                "testTable",
                new String[] {"_id", "num", "str"},
                new String[] {"INTEGER NOT NULL", "INTEGER", "TEXT"}
                );
        Assert.assertEquals("CREATE TABLE testTable (_id INTEGER NOT NULL, num INTEGER, str TEXT);", createStmt);
    }
}
