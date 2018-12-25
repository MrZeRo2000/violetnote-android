package com.romanpulov.violetnote.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;

import com.romanpulov.violetnote.db.tabledef.HNoteItemsTableDef;
import com.romanpulov.violetnote.model.BasicHEventA;
import com.romanpulov.violetnote.model.BasicHNoteItemA;
import com.romanpulov.violetnote.model.BasicNoteItemA;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * BasicHNoteItem DAO
 */
public final class BasicHNoteItemDAO extends AbstractBasicHEventItemDAO<BasicHNoteItemA> {

    public BasicHNoteItemDAO(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public List<BasicHNoteItemA> getAll() {
        final List<BasicHNoteItemA> result = new ArrayList<>();

        readCursor(new CursorReaderHandler() {
            @Override
            public Cursor createCursor() {
                return mDB.query(
                        HNoteItemsTableDef.TABLE_NAME,
                        HNoteItemsTableDef.TABLE_COLS,
                        null,
                        null,
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

    @NonNull
    public List<BasicHNoteItemA> getByNoteItemId(final long noteItemId) {
        final List<BasicHNoteItemA> result = new ArrayList<>();

        readCursor(new CursorReaderHandler() {
            @Override
            public Cursor createCursor() {
                return mDB.query(
                        HNoteItemsTableDef.TABLE_NAME,
                        HNoteItemsTableDef.TABLE_COLS,
                        HNoteItemsTableDef.NOTE_ITEM_ID_COLUMN_NAME + " = ?",
                        new String[] {String.valueOf(noteItemId)},
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

    @Override
    public long delete(@NonNull BasicHNoteItemA object) {
        return internalDeleteById(HNoteItemsTableDef.TABLE_NAME, object.getId());
    }

    @Override
    public long insert(@NonNull BasicHNoteItemA object) {
        ContentValues cv = new ContentValues();

        cv.put(HNoteItemsTableDef.EVENT_ID_COLUMN_NAME, object.getEventId());
        cv.put(HNoteItemsTableDef.NOTE_ITEM_ID_COLUMN_NAME, object.getNoteItemId());
        cv.put(HNoteItemsTableDef.NAME_COLUMN_NAME, object.getName());
        cv.put(HNoteItemsTableDef.VALUE_COLUMN_NAME, object.getValue());

        return mDB.insert(HNoteItemsTableDef.TABLE_NAME, null, cv);
    }

    public long saveEvent(@NonNull BasicNoteItemA noteItem) {
        long eventId = getBasicHEventDAO().insert(BasicHEventA.fromEventType(mDBHelper.getDBDictionaryCache().getNoteItemsHEventParamId()));
        if (eventId != -1) {
            return insert(BasicHNoteItemA.fromEventData(eventId, noteItem));
        } else
            return eventId;
    }

    public long deleteEvent(@NonNull BasicNoteItemA noteItem) {
        long result = 0;

        Set<Long> events = new HashSet<>();

        List<BasicHNoteItemA> hNoteItems = getByNoteItemId(noteItem.getId());
        for (BasicHNoteItemA hNoteItem : hNoteItems) {
            result = delete(hNoteItem);
            if (result != 0)
                events.add(hNoteItem.getEventId());
        }

        if (result != 0) {
            getBasicHEventDAO().deleteByIds(events);
        }

        return result;
    }
}
