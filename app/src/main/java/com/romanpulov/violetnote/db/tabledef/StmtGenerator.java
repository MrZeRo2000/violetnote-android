package com.romanpulov.violetnote.db.tabledef;

import android.support.annotation.NonNull;

/**
 * Statements generator
 */
public final class StmtGenerator {

    /**
     * Create table without foreign key
     * @param tableName Table name
     * @param tableCols Table columns
     * @param tableColTypes Table column types
     * @return Create table statement
     */
    @NonNull
    public static String createTableStatement(
            String tableName,
            @NonNull String[] tableCols,
            @NonNull String[] tableColTypes
    ) {
        return createTableStatement(tableName, tableCols, tableColTypes, null, null, null);
    }

    /**
     * Create table with foreign key constraint
     * @param tableName Table name
     * @param tableCols Table columns
     * @param tableColTypes Table column types
     * @param foreignKeyColumnName Column name to apply the constraint
     * @param foreignKeyTableName Foreign key table name
     * @param foreignKeyTableColumnName Foreign key table column name
     * @return Create table statement
     */
    @NonNull
    public static String createTableStatement(
            String tableName,
            @NonNull String[] tableCols,
            @NonNull String[] tableColTypes,
            String foreignKeyColumnName,
            String foreignKeyTableName,
            String foreignKeyTableColumnName
            ) {
        if ((tableCols.length != tableColTypes.length) || (tableCols.length == 0))
            throw new IllegalArgumentException();

        StringBuilder tableStructure = new StringBuilder();
        for (int i=0; i<tableCols.length; i++) {
            tableStructure.append(tableCols[i]);
            tableStructure.append(" ");
            tableStructure.append(tableColTypes[i]);
            if ((i < tableCols.length - 1) || (foreignKeyColumnName != null)) {
                tableStructure.append(", ");
            }
        }

        if (foreignKeyColumnName != null) {
            tableStructure.append("FOREIGN KEY (");
            tableStructure.append(foreignKeyColumnName);
            tableStructure.append(") REFERENCES ");
            tableStructure.append(foreignKeyTableName);
            tableStructure.append("(");
            tableStructure.append(foreignKeyTableColumnName);
            tableStructure.append(")");
        }

        return "CREATE TABLE " + tableName + " (" + tableStructure.toString() + ");";
    }
}
