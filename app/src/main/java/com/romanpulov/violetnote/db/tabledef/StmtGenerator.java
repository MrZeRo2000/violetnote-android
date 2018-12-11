package com.romanpulov.violetnote.db.tabledef;

import android.support.annotation.NonNull;
import android.text.TextUtils;

/**
 * Statements generator
 */
public final class StmtGenerator {

    public static final class ForeignKeyRec {
        private final String columnName;
        private final String tableName;
        private final String tableColumnName;

        public ForeignKeyRec(String columnName, String tableName, String tableColumnName) {
            this.columnName = columnName;
            this.tableName = tableName;
            this.tableColumnName = tableColumnName;
        }
    }

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
        return createTableStatement(tableName, tableCols, tableColTypes, null);
    }

    /**
     * Create table with foreign key constraint
     * @param tableName Table name
     * @param tableCols Table columns
     * @param tableColTypes Table column types
     * @param foreignKeys Foreign keys
     * @return Create table statement
     */
    @NonNull
    public static String createTableStatement(
            @NonNull String tableName,
            @NonNull String[] tableCols,
            @NonNull String[] tableColTypes,
            ForeignKeyRec[] foreignKeys
            ) {
        if ((tableCols.length != tableColTypes.length) || (tableCols.length == 0))
            throw new IllegalArgumentException();

        StringBuilder tableStructure = new StringBuilder();
        for (int i=0; i<tableCols.length; i++) {
            tableStructure.append(tableCols[i]);
            tableStructure.append(" ");
            tableStructure.append(tableColTypes[i]);
            if ((i < tableCols.length - 1) || (foreignKeys != null)) {
                tableStructure.append(", ");
            }
        }

        if (foreignKeys != null) {
            for (int i = 0; i < foreignKeys.length; i++) {
                ForeignKeyRec f = foreignKeys[i];

                tableStructure.append("FOREIGN KEY (");
                tableStructure.append(f.columnName);
                tableStructure.append(") REFERENCES ");
                tableStructure.append(f.tableName);
                tableStructure.append("(");
                tableStructure.append(f.tableColumnName);
                tableStructure.append(")");

                if (i < foreignKeys.length - 1) {
                    tableStructure.append(", ");
                }

            }
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
            if (sb.length() > 0) {
                sb.append("_");
            }

            String ss[] = s.split("_");
            if (ss.length > 1) {
                sb.append(ss[0].substring(0, 1));
                sb.append(ss[1].substring(0, 1));
            } else {
                sb.append(s.substring(0, 2));
            }
        }

        return sb.toString();
    }

    /**
     * Create index on foreign key
     * @param tableName Table name
     * @param indexColumnName Foreign key column name for index
     * @return Create foreign key index statement
     */
    @NonNull
    public static String createForeignKeyIndexStatement(
            @NonNull String tableName,
            @NonNull String indexColumnName)
    {
        return "CREATE INDEX fk_" + tableName + " ON " + tableName + " (" + indexColumnName + ");";
    }

    /**
     * Create unique index
     * @param tableName Table Name
     * @param indexColumnName Index column name
     * @return Create unique index statement
     */
    @NonNull
    public static String createUniqueIndexStatement(
            @NonNull String tableName,
            @NonNull String indexColumnName)
    {
        return "CREATE UNIQUE INDEX u_" + tableName + "_" + firstChars(new String[]{indexColumnName}) + " ON " + tableName + " (" + indexColumnName + ");";
    }

    /**
     * Create unique index
     * @param tableName Table Name
     * @param indexColumnNames Index column names
     * @return Create unique index statement
     */
    @NonNull
    public static String createUniqueIndexStatement(@NonNull String tableName, @NonNull String[] indexColumnNames) {
        return "CREATE UNIQUE INDEX u_" + tableName + "_" + firstChars(indexColumnNames)  + " ON " + tableName + " (" + joinStrings(", ", indexColumnNames) + ");";
    }

    @NonNull
    public static String createInsertTableStatement(
            @NonNull String tableName,
            @NonNull String[] tableCols,
            @NonNull String values
    ) {
        return "INSERT INTO " + tableName + " (" +
                joinStrings(", ", tableCols) + ") " +
                values + ";";
    }

    /**
     * Drop table
     * @param tableName Table name to drop
     * @return Drop table statement
     */
    @NonNull
    public static String dropTableStatement(@NonNull String tableName) {
        return "DROP TABLE IF EXISTS " + tableName;
    }
}
