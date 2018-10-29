package com.romanpulov.violetnote.db;

/**
 * Raw queries
 * Created by romanpulov on 13.12.2016.
 */

public class DBRawQueryRepository {

    public static final String NOTES_WITH_TOTALS =
        "SELECT " +
        "n._id, " +
        "n.last_modified," +
        "n.order_id," +
        "n.group_id," +
        "n.note_type," +
        "n.title," +
        "n.is_encrypted," +
        "n.encrypted_string," +
        "(SELECT COUNT(ni._id) FROM note_items ni WHERE ni.note_id = n._id) AS count_total, " +
        "(SELECT SUM(ni.checked) FROM note_items ni WHERE ni.note_id = n._id) AS count_checked " +
        "FROM notes n " +
        "ORDER BY n.order_id";
}
