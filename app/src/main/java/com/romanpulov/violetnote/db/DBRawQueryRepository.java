package com.romanpulov.violetnote.db;

/**
 * Raw queries
 * Created by romanpulov on 13.12.2016.
 */

public class DBRawQueryRepository {

    public static final String NOTE_GROUPS_WITH_TOTALS =
        "SELECT " +
        "g._id, " +
        "g.note_group_type, " +
        "g.note_group_name, " +
        "g.note_group_icon, " +
        "g.order_id, " +
        "(SELECT COUNT(n._id) FROM notes n WHERE n.group_id = g._id) AS count_notes " +
        "FROM note_groups g " +
        "ORDER BY g.order_id";

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
        "WHERE group_id = ? " +
        "ORDER BY n.order_id";

    public static final String NOTE_ITEMS_PARAMS  =
        "SELECT " +
        "ni._id, " +
        "nip.note_item_param_type_id, " +
        "nip.v_int AS param_v_int, " +
        "nip.v_text AS param_v_text " +
        "FROM note_items ni " +
        "INNER JOIN note_item_params nip ON ni._id = nip.note_item_id " +
        "WHERE ni.note_id = ? " ;

    public static final String NOTE_ITEMS_EVENTS =
            "SELECT " +
            "hni.note_item_id, " +
            "he._id AS event_id, " +
            "he.event_time, " +
            "hni.name, " +
            "hni.value " +
            "FROM h_note_items hni " +
            "INNER JOIN h_events he ON hni.event_id = he._id " +
            "ORDER BY he.event_time DESC"
            ;
}
