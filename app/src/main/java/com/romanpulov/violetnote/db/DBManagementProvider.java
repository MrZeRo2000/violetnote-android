package com.romanpulov.violetnote.db;

/**
 * Database management provider interface
 * Created by rpulov on 09.08.2017.
 */

public interface DBManagementProvider {
    /**
     * @return Table name for entity
     */
    String getTableName();

    /**
     *
     * @return Selection for ordering operation
     */
    String getOrderSelection();

    /**
     *
     * @return Selection arguments for ordering operation
     */
    String[] getOrderSelectionArgs();

    /**
     *
     * @return OrderId selection string for exchanging order
     */
    String getOrderIdSelection();
}
