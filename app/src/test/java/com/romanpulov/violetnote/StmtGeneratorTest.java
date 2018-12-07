package com.romanpulov.violetnote;

import com.romanpulov.violetnote.db.tabledef.StmtGenerator;

import org.junit.Assert;
import org.junit.Test;

public class StmtGeneratorTest {
    @Test
    public void testCreate() {
        String createStmt1 = StmtGenerator.createTableStatement(
                "t1",
                new String[] {"_id", "num", "str"},
                new String[] {"INTEGER NOT NULL", "INTEGER", "TEXT"}
                );
        Assert.assertEquals("CREATE TABLE t1 (_id INTEGER NOT NULL, num INTEGER, str TEXT);", createStmt1);

        String createStmt2 = StmtGenerator.createTableStatement(
                "t2",
                new String[] {"_id", "t1_id", "str"},
                new String[] {"INTEGER NOT NULL", "INTEGER", "TEXT"},
                "t1_id",
                "t1",
                "_id"
        );

        Assert.assertEquals("CREATE TABLE t2 (_id INTEGER NOT NULL, t1_id INTEGER, str TEXT, FOREIGN KEY (t1_id) REFERENCES t1(_id));", createStmt2);

        String fkIndexStmt = StmtGenerator.createForeignKeyIndex("t2", "t1_id");
        String uIndex1Stmt = StmtGenerator.createUniqueIndex("t2", "t1_id");
        String uIndex2Stmt = StmtGenerator.createUniqueIndex("t2", new String[]{"t1_id", "str"});

        System.out.println(createStmt1);
        System.out.println(createStmt2);
        System.out.println(fkIndexStmt);
        System.out.println(uIndex1Stmt);
        System.out.println(uIndex2Stmt);
    }
}
