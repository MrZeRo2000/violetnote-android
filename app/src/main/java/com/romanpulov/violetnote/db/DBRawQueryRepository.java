package com.romanpulov.violetnote.db;

/**
 * Raw queries
 * Created by romanpulov on 13.12.2016.
 */

public class DBRawQueryRepository {

    public static final String NOTE_GROUPS_WITH_TOTALS_WITH_CHECKED =
        "SELECT " +
        "g._id, " +
        "g.note_group_type, " +
        "g.note_group_name, " +
        "g.note_group_icon, " +
        "g.order_id, " +
        "g.note_group_display_options, " +
        "COUNT(DISTINCT n._id) AS count_notes, " +
        "COUNT(CASE WHEN n.note_type = 0 AND ni.checked = 1 THEN 1 END) AS count_checked, " +
        "COUNT(CASE WHEN n.note_type = 0 AND ni.checked = 0 THEN 1 END) AS count_unchecked " +
        "FROM note_groups g " +
        "LEFT OUTER JOIN notes n ON n.group_id = g._id " +
        "LEFT OUTER JOIN note_items ni ON ni.note_id = n._id " +
        "GROUP BY " +
        "g._id, " +
        "g.note_group_type, " +
        "g.note_group_name, " +
        "g.note_group_icon, " +
        "g.order_id, " +
        "g.note_group_display_options " +
        "ORDER BY g.order_id";

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

    public static final String H_EVENTS_BY_CO_ITEMS_NOTE_ID =
            "SELECT " +
            "he._id, " +
            "he.event_type_id, " +
            "he.event_time, " +
            "he.event_summary, " +
             "COUNT(heci._id) AS count_items " +
            "FROM h_events he " +
            "INNER JOIN h_note_co_items heci ON he._id = heci.event_id " +
            "WHERE heci.note_id = ? " +
            "GROUP BY " +
            "he._id, " +
            "he.event_type_id, " +
            "he.event_time, " +
            "he.event_summary " +
            "ORDER BY he.event_time DESC"
            ;
    
    public static final String H_EVENT_CO_ITEMS_TIME = 
            "SELECT  " +
            "  heci._id, " +
            "  heci.event_id, " +
            "  he.event_time " +
            "FROM h_note_co_items heci " +
            "INNER JOIN h_events he ON he._id = heci.event_id " +
            "ORDER BY he.event_time ";

    public static final String H_EVENT_DELETE_ORPHANED =
            "DELETE FROM h_events WHERE _id NOT IN ( " +
            "  SELECT  " +
            "    event_id " +
            "  FROM h_note_co_items " +
            "  UNION   " +
            "  SELECT " +
            "    event_id " +
            "  FROM h_note_items    " +
            ")";

    public static final String H_NOTE_ITEM_EVENTS_BY_NOTE_ITEM_ID =
            "SELECT  " +
            "  ni._id, " +
            "  he.event_type_id, " +
            "  he.event_time, " +
            "  ni.note_item_id, " +
            "  ni.name, " +
            "  ni.value " +
            "FROM h_note_items ni " +
            "INNER JOIN h_events he ON he._id = ni.event_id " +
            "WHERE ni.note_item_id = ? " +
            "ORDER BY ni._id DESC ";
}
