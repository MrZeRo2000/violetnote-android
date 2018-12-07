package com.romanpulov.violetnote.db.tabledef;

import android.support.annotation.NonNull;
import android.text.TextUtils;

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
            @NonNull String tableName,
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
            @NonNull String tableName,
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

    @NonNull
    public static String joinStrings(@NonNull String delimiter, @NonNull String[] strings) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < strings.length; i++) {
            sb.append(strings[i]);
            if (i < strings.length - 1) {
                sb.append(delimiter);
            }
        }
        return sb.toString();
    }

    public static String firstChars(@NonNull String[] strings) {
        StringBuilder sb = new StringBuilder();

        for (String s: strings) {
            sb.append(s.substring(0, 2));
        }

        return sb.toString();
    }

    @NonNull
    public static String createForeignKeyIndex(@NonNull String tableName, @NonNull String indexColumnName) {
        return "CREATE INDEX fk_" + tableName + " ON " + tableName + " (" + indexColumnName + ");";
    }

    @NonNull
    public static String createUniqueIndex(@NonNull String tableName, @NonNull String indexColumnName) {
        return "CREATE UNIQUE INDEX u_" + tableName + "_" + firstChars(new String[]{indexColumnName}) + " ON " + tableName + " (" + indexColumnName + ");";
    }

    @NonNull
    public static String createUniqueIndex(@NonNull String tableName, @NonNull String[] indexColumnNames) {
        return "CREATE UNIQUE INDEX u_" + tableName + "_" + firstChars(indexColumnNames)  + " ON " + tableName + " (" + joinStrings(", ", indexColumnNames) + ");";
    }
}
