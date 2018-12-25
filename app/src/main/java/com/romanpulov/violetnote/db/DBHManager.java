package com.romanpulov.violetnote.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;

import com.romanpulov.violetnote.db.tabledef.DBCommonDef;
import com.romanpulov.violetnote.db.tabledef.HEventTypesTableDef;
import com.romanpulov.violetnote.db.tabledef.HEventsTableDef;
import com.romanpulov.violetnote.db.tabledef.HNoteCOItemParamsTableDef;
import com.romanpulov.violetnote.db.tabledef.HNoteCOItemsTableDef;
import com.romanpulov.violetnote.db.tabledef.HNoteItemsTableDef;
import com.romanpulov.violetnote.model.BasicHEventA;
import com.romanpulov.violetnote.model.BasicHEventTypeA;
import com.romanpulov.violetnote.model.BasicHNoteCOItemA;
import com.romanpulov.violetnote.model.BasicHNoteItemA;
import com.romanpulov.violetnote.model.BasicNoteA;
import com.romanpulov.violetnote.model.BasicNoteItemA;
import com.romanpulov.violetnote.model.BasicNoteItemParams;
import com.romanpulov.violetnote.model.vo.BasicNoteItemParamValueA;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class DBHManager extends BasicDBManager {
    public DBHManager(Context context) {
        super(context);
    }

    public List<BasicHEventTypeA> getHEventTypes() {
        final List<BasicHEventTypeA> result = new ArrayList<>();
        readCursor(new CursorReaderHandler() {
            @Override
            public Cursor createCursor() {
                return mDB.query(HEventTypesTableDef.TABLE_NAME, HEventTypesTableDef.TABLE_COLS,
                        null, null, null, null, null);
            }

            @Override
            public void readFromCursor(Cursor c) {
                result.add(BasicHEventTypeA.newInstance(
                        c.getLong(0),
                        c.getLong(1),
                        c.getString(2),
                        c.getString(3)
                ));
            }
        });

        return result;
    }

    public Map<String, Long> getHEventTypesMap() {
        final Map<String, Long> result = new HashMap<>();
        readCursor(new CursorReaderHandler() {
            @Override
            public Cursor createCursor() {
                return mDB.query(HEventTypesTableDef.TABLE_NAME, HEventTypesTableDef.TABLE_COLS,
                        null, null, null, null, null);
            }

            @Override
            public void readFromCursor(Cursor c) {
                result.put(c.getString(2), c.getLong(0));
            }
        });

        return result;
    }

    public long insertHEvent(long eventTypeId, String eventSummary) {
        ContentValues cv = new ContentValues();

        cv.put(HEventsTableDef.EVENT_TYPE_ID_COLUMN_NAME, eventTypeId);
        cv.put(HEventsTableDef.EVENT_TIME_COLUMN_NAME, System.currentTimeMillis());
        cv.put(HEventsTableDef.EVENT_SUMMARY_COLUMN_NAME, eventSummary);

        return mDB.insert(HEventsTableDef.TABLE_NAME, null, cv);
    }

    public long deleteHEvent(@NonNull BasicHEventA hEvent) {
        mDB.delete(HNoteItemsTableDef.TABLE_NAME, HNoteItemsTableDef.EVENT_ID_COLUMN_NAME + " = ?", new String[]{String.valueOf(hEvent.getId())});
        return deleteById(HEventsTableDef.TABLE_NAME, hEvent.getId());
    }

    public void queryHEvents(@NonNull final List<BasicHEventA> hEvents) {
        queryHEventsByType(hEvents, 0);
    }

    public void queryHEventsByType(@NonNull final List<BasicHEventA> hEvents, final long eventTypeId) {
        hEvents.clear();

        readCursor(new CursorReaderHandler() {
            @Override
            public Cursor createCursor() {
                return mDB.query(
                        HEventsTableDef.TABLE_NAME,
                        HEventsTableDef.TABLE_COLS,
                        eventTypeId == 0 ? null : HEventsTableDef.EVENT_TYPE_ID_COLUMN_NAME + " = ?",
                        eventTypeId == 0 ? null : new String[] {String.valueOf(eventTypeId)},
                        null,
                        null,
                        DBCommonDef.getSelectionFieldDesc(DBCommonDef.ID_COLUMN_NAME)
                        );
            }

            @Override
            public void readFromCursor(Cursor c) {
                hEvents.add(BasicHEventA.newInstance(
                        c.getLong(0),
                        c.getLong(1),
                        c.getLong(2),
                        c.getString(3)
                ));
            }
        });
    }

    @NonNull
    public List<BasicHNoteItemA> queryHNoteItemsByNoteItem(final BasicNoteItemA noteItem) {
        final List<BasicHNoteItemA> result = new ArrayList<>();

        readCursor(new CursorReaderHandler() {
            @Override
            public Cursor createCursor() {
                return mDB.query(
                        HNoteItemsTableDef.TABLE_NAME,
                        HNoteItemsTableDef.TABLE_COLS,
                        HNoteItemsTableDef.NOTE_ITEM_ID_COLUMN_NAME + " = ?",
                        new String[] {String.valueOf(noteItem.getId())},
                        null,
                        null,
                        null
                );
            }

            @Override
            public void readFromCursor(Cursor c) {
                result.add(BasicHNoteItemA.newInstance(
                        c.getLong(0),
                        c.getLong(1),
                        c.getLong(2),
                        c.getString(3),
                        c.getString(4)
                ));
            }
        });

        return result;
    }

    public List<BasicHNoteCOItemA> queryHNoteCOItemsByNote(final BasicNoteA note) {
        final List<BasicHNoteCOItemA> result = new ArrayList<>();

        readCursor(new CursorReaderHandler() {
            @Override
            public Cursor createCursor() {
                return mDB.query(
                        HNoteCOItemsTableDef.TABLE_NAME,
                        HNoteCOItemsTableDef.TABLE_COLS,
                        HNoteCOItemsTableDef.NOTE_ID_COLUMN_NAME + " = ?",
                        new String[] {String.valueOf(note.getId())},
                        null,
                        null,
                        null
                );
            }

            @Override
            public void readFromCursor(Cursor c) {
                result.add(BasicHNoteCOItemA.newInstance(
                        c.getLong(0),
                        c.getLong(1),
                        c.getLong(2),
                        c.getString(3)
                ));
            }
        });

        return result;
    }

    private long insertHNoteItem(long eventId, @NonNull BasicNoteItemA noteItem) {
        ContentValues cv = new ContentValues();

        cv.put(HNoteItemsTableDef.EVENT_ID_COLUMN_NAME, eventId);
        cv.put(HNoteItemsTableDef.NOTE_ITEM_ID_COLUMN_NAME, noteItem.getId());
        cv.put(HNoteItemsTableDef.NAME_COLUMN_NAME, noteItem.getName());
        cv.put(HNoteItemsTableDef.VALUE_COLUMN_NAME, noteItem.getValue());

        return mDB.insert(HNoteItemsTableDef.TABLE_NAME, null, cv);
    }

    private long insertHNoteCOItem(long eventId, @NonNull BasicNoteA note, @NonNull BasicNoteItemA noteItem) {
        ContentValues cv = new ContentValues();

        cv.put(HNoteCOItemsTableDef.EVENT_ID_COLUMN_NAME, eventId);
        cv.put(HNoteCOItemsTableDef.NOTE_ID_COLUMN_NAME, note.getId());
        cv.put(HNoteCOItemsTableDef.VALUE_COLUMN_NAME, noteItem.getValue());

        return mDB.insert(HNoteCOItemsTableDef.TABLE_NAME, null, cv);
    }

    private long insertHNoteCOItemParam(long hNoteCOItemId, @NonNull BasicNoteItemParamValueA param) {
        ContentValues cv = new ContentValues();

        cv.put(HNoteCOItemParamsTableDef.H_NOTE_CO_ITEM_ID_COLUMN_NAME, hNoteCOItemId);
        cv.put(HNoteCOItemParamsTableDef.NOTE_ITEM_PARAM_TYPE_ID_COLUMN_NAME, param.noteItemParamTypeId);
        cv.put(HNoteCOItemParamsTableDef.V_INT_COLUMN_NAME, param.paramValue.vInt);
        cv.put(HNoteCOItemParamsTableDef.V_TEXT_COLUMN_NAME, param.paramValue.vText);

        return mDB.insert(HNoteCOItemParamsTableDef.TABLE_NAME, null, cv);    }

    private long insertHNoteCOItemParams(long hNoteCOItemId, @NonNull BasicNoteItemParams params) {
        long result = 0;

        List<BasicNoteItemParamValueA> paramList = BasicNoteItemParams.paramValuesToList(params.paramValues);
        for (BasicNoteItemParamValueA param : paramList) {
            result = insertHNoteCOItemParam(hNoteCOItemId, param);
            if (result == -1)
                return result;
        }

        return result;
    }

    public long saveNoteItemsEvent(@NonNull BasicNoteItemA noteItem) {
        long eventId = insertHEvent(mDBHelper.getDBDictionaryCache().getNoteItemsHEventParamId(), null);
        if (eventId != -1) {
            return insertHNoteItem(eventId, noteItem);
        } else
            return eventId;
    }

    public long saveNoteCOEvent(@NonNull BasicNoteA note, @NonNull BasicNoteItemA noteItem) {
        long eventId = insertHEvent(mDBHelper.getDBDictionaryCache().getCheckoutHEventParamId(), null);
        if (eventId != -1) {
            long hNoteCOItemId = insertHNoteCOItem(eventId, note, noteItem);
            BasicNoteItemParams params = noteItem.getNoteItemParams();
            if ((hNoteCOItemId != -1) && (params.size() > 0)) {
                return insertHNoteCOItemParams(hNoteCOItemId, noteItem.getNoteItemParams());
            } else
                return hNoteCOItemId;
        } else
            return eventId;
    }

    public long deleteHEvent(long eventId) {
        return deleteById(HEventsTableDef.TABLE_NAME, eventId);
    }

    public long deleteHNoteItem(@NonNull BasicNoteItemA noteItem) {
        long result = 0;

        List<BasicHNoteItemA> hNoteItems = queryHNoteItemsByNoteItem(noteItem);

        for (BasicHNoteItemA hNoteItem : hNoteItems) {
            result = deleteById(HNoteItemsTableDef.TABLE_NAME, hNoteItem.getId());
            deleteHEvent(hNoteItem.getEventId());
        }

        return result;
    }

    public long deleteHNoteCOItems(@NonNull BasicNoteA note) {
        long result = 0;

        List<BasicHNoteCOItemA> hNoteCOItems = queryHNoteCOItemsByNote(note);
        Set<Long> hEvents = new HashSet<>();

        for (BasicHNoteCOItemA hNoteCOItem : hNoteCOItems) {
            //delete params
            deleteById(HNoteCOItemParamsTableDef.TABLE_NAME, HNoteCOItemParamsTableDef.H_NOTE_CO_ITEM_ID_COLUMN_NAME, hNoteCOItem.getId());
            //delete note
            deleteById(HNoteCOItemsTableDef.TABLE_NAME, hNoteCOItem.getId());
            //save event
            hEvents.add(hNoteCOItem.getEventId());
        }

        //delete saved events
        for (long eventId : hEvents) {
            result = deleteHEvent(eventId);
        }

        return result;
    }
}
