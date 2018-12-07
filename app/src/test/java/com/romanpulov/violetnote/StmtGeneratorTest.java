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
                new StmtGenerator.ForeignKeyRec[] {new StmtGenerator.ForeignKeyRec("t1_id","t1","_id")}
        );

        Assert.assertEquals("CREATE TABLE t2 (_id INTEGER NOT NULL, t1_id INTEGER, str TEXT, FOREIGN KEY (t1_id) REFERENCES t1(_id));", createStmt2);

        String fkIndexStmt = StmtGenerator.createForeignKeyIndex("t2", "t1_id");
        Assert.assertEquals("CREATE INDEX fk_t2 ON t2 (t1_id);", fkIndexStmt);

        String uIndex1Stmt = StmtGenerator.createUniqueIndex("t2", "t1_id");
        Assert.assertEquals("CREATE UNIQUE INDEX u_t2_t1 ON t2 (t1_id);", uIndex1Stmt);

        String uIndex2Stmt = StmtGenerator.createUniqueIndex("t2", new String[]{"t1_id", "str"});
        Assert.assertEquals("CREATE UNIQUE INDEX u_t2_t1st ON t2 (t1_id, str);", uIndex2Stmt);

        String createStmt3 = StmtGenerator.createTableStatement(
                "t3",
                new String[] {"_id", "t1_id", "t2_id", "str"},
                new String[] {"INTEGER NOT NULL", "INTEGER", "INTEGER", "TEXT"},
                new StmtGenerator.ForeignKeyRec[] {
                        new StmtGenerator.ForeignKeyRec("t1_id","t1","_id"),
                        new StmtGenerator.ForeignKeyRec("t2_id","t2","_id")
                }
        );
        Assert.assertEquals("CREATE TABLE t3 (_id INTEGER NOT NULL, t1_id INTEGER, t2_id INTEGER, str TEXT, FOREIGN KEY (t1_id) REFERENCES t1(_id), FOREIGN KEY (t2_id) REFERENCES t2(_id));", createStmt3);

        /*
        System.out.println(createStmt1);
        System.out.println(createStmt2);
        System.out.println(fkIndexStmt);
        System.out.println(uIndex1Stmt);
        System.out.println(uIndex2Stmt);
        System.out.println(createStmt3);
        */
    }
}
