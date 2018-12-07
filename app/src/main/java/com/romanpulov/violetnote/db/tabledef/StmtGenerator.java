package com.romanpulov.violetnote.db.tabledef;

import android.support.annotation.NonNull;

/**
 * Statements generator
 */
public final class StmtGenerator {

    @NonNull
    public static String createTableStatement(String tableName, @NonNull String[] tableCols, @NonNull String[] tableTypes) {
        if ((tableCols.length != tableTypes.length) || (tableCols.length == 0))
            throw new IllegalArgumentException();

        StringBuilder tableStructure = new StringBuilder();
        for (int i=0; i<tableCols.length; i++) {
            tableStructure.append(tableCols[i]);
            tableStructure.append(" ");
            tableStructure.append(tableTypes[i]);
            if (i < tableCols.length - 1) {
                tableStructure.append(", ");
            }
        }

        return "CREATE TABLE " + tableName + " (" + tableStructure.toString() + ");";
    }
}
