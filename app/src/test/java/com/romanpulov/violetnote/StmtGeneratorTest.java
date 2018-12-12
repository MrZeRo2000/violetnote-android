package com.romanpulov.violetnote;

import com.romanpulov.violetnote.db.tabledef.StmtGenerator;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class StmtGeneratorTest {
    @Test
    public void testCreate() {
        String createStmt1 = StmtGenerator.createTableStatement(
                "t1",
                new String[] {"_id", "num", "str"},
                new String[] {"INTEGER PRIMARY KEY", "INTEGER", "TEXT"}
                );
        Assert.assertEquals("CREATE TABLE t1 (_id INTEGER PRIMARY KEY, num INTEGER, str TEXT);", createStmt1);

        String createStmt2 = StmtGenerator.createTableStatement(
                "t2",
                new String[] {"_id", "t1_id", "str"},
                new String[] {"INTEGER PRIMARY KEY", "INTEGER", "TEXT"},
                new StmtGenerator.ForeignKeyRec[] {new StmtGenerator.ForeignKeyRec("t1_id","t1","_id")}
        );

        Assert.assertEquals("CREATE TABLE t2 (_id INTEGER PRIMARY KEY, t1_id INTEGER, str TEXT, FOREIGN KEY (t1_id) REFERENCES t1(_id));", createStmt2);

        String fkIndexStmt = StmtGenerator.createForeignKeyIndexStatement("t2", "t1_id");
        Assert.assertEquals("CREATE INDEX fk_t2_ti ON t2 (t1_id);", fkIndexStmt);

        String uIndex1Stmt = StmtGenerator.createUniqueIndexStatement("t2", "t1_id");
        Assert.assertEquals("CREATE UNIQUE INDEX u_t2_ti ON t2 (t1_id);", uIndex1Stmt);

        String uIndex2Stmt = StmtGenerator.createUniqueIndexStatement("t2", new String[]{"t1_id", "str"});
        Assert.assertEquals("CREATE UNIQUE INDEX u_t2_ti_st ON t2 (t1_id, str);", uIndex2Stmt);

        String createStmt3 = StmtGenerator.createTableStatement(
                "t3",
                new String[] {"_id", "t1_id", "t2_id", "str"},
                new String[] {"INTEGER PRIMARY KEY", "INTEGER", "INTEGER", "TEXT"},
                new StmtGenerator.ForeignKeyRec[] {
                        new StmtGenerator.ForeignKeyRec("t1_id","t1","_id"),
                        new StmtGenerator.ForeignKeyRec("t2_id","t2","_id")
                }
        );
        Assert.assertEquals("CREATE TABLE t3 (_id INTEGER PRIMARY KEY, t1_id INTEGER, t2_id INTEGER, str TEXT, FOREIGN KEY (t1_id) REFERENCES t1(_id), FOREIGN KEY (t2_id) REFERENCES t2(_id));", createStmt3);

        String insertStmt1 = StmtGenerator.createInsertTableStatement(
                "t1",
                new String[] {"num", "str"},
                "SELECT 3, 'test1' UNION ALL SELECT 6, 'test6'"
                );
        Assert.assertEquals("INSERT INTO t1 (num, str) SELECT 3, 'test1' UNION ALL SELECT 6, 'test6';", insertStmt1);

        String alterStmt1 = StmtGenerator.createAlterTableAddStatement("t3", "s1", "TEXT");

        System.out.println(createStmt1);
        System.out.println(createStmt2);
        System.out.println(fkIndexStmt);
        System.out.println(uIndex1Stmt);
        System.out.println(uIndex2Stmt);
        System.out.println(createStmt3);
        System.out.println(insertStmt1);
        System.out.println(alterStmt1);

        /*
        String[] a1 = new String[] {"_id", "t1_id", "t2_id", "str"};
        System.out.println("a1:" + Arrays.toString(a1));
        String[] a2 = Arrays.copyOfRange(a1, 1, a1.length);
        System.out.println("a2:" + Arrays.toString(a2));
        */

    }
}
