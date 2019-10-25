package com.romanpulov.violetnote.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;

import com.romanpulov.violetnote.db.tabledef.HNoteCOItemsTableDef;
import com.romanpulov.violetnote.model.BasicHEventA;
import com.romanpulov.violetnote.model.BasicHNoteCOItemA;
import com.romanpulov.violetnote.model.BasicHNoteCOItemParam;
import com.romanpulov.violetnote.model.BasicNoteA;
import com.romanpulov.violetnote.model.BasicNoteItemA;
import com.romanpulov.violetnote.model.BasicNoteItemParams;
import com.romanpulov.violetnote.model.vo.BasicNoteItemParamValueA;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * BasicHNoteCOItemA DAO
 */
public final class BasicHNoteCOItemDAO extends AbstractBasicHEventItemParamDAO<BasicHNoteCOItemA> {

    public BasicHNoteCOItemDAO(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public List<BasicHNoteCOItemA> getAll() {
        final List<BasicHNoteCOItemA> result = new ArrayList<>();

        readCursor(new CursorReaderHandler() {
            @Override
            public Cursor createCursor() {
                return mDB.query(
                        HNoteCOItemsTableDef.TABLE_NAME,
                        HNoteCOItemsTableDef.TABLE_COLS,
                        null,
                        null,
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

    public List<BasicHNoteCOItemA> getByNoteId(final long noteId) {
        final List<BasicHNoteCOItemA> result = new ArrayList<>();

        readCursor(new CursorReaderHandler() {
            @Override
            public Cursor createCursor() {
                return mDB.query(
                        HNoteCOItemsTableDef.TABLE_NAME,
                        HNoteCOItemsTableDef.TABLE_COLS,
                        HNoteCOItemsTableDef.NOTE_ID_COLUMN_NAME + " = ?",
                        new String[] {String.valueOf(noteId)},
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

    @Override
    public long delete(@NonNull BasicHNoteCOItemA object) {
        return internalDeleteById(HNoteCOItemsTableDef.TABLE_NAME, object.getId());
    }

    @Override
    public long insert(@NonNull BasicHNoteCOItemA object) {
        ContentValues cv = new ContentValues();

        cv.put(HNoteCOItemsTableDef.EVENT_ID_COLUMN_NAME, object.getEventId());
        cv.put(HNoteCOItemsTableDef.NOTE_ID_COLUMN_NAME, object.getNoteId());
        cv.put(HNoteCOItemsTableDef.VALUE_COLUMN_NAME, object.getValue());

        return mDB.insert(HNoteCOItemsTableDef.TABLE_NAME, null, cv);

    }

    private long insertHNoteCOItemParams(long hNoteCOItemId, @NonNull BasicNoteItemParams params) {
        long result = 0;

        List<BasicNoteItemParamValueA> paramList = BasicNoteItemParams.paramValuesToList(params.paramValues);
        for (BasicNoteItemParamValueA param : paramList) {
            result = getBasicHNoteCOItemParamDAO().insert(BasicHNoteCOItemParam.newInstance(
                    0,
                    hNoteCOItemId,
                    param.noteItemParamTypeId,
                    param.paramValue.vInt,
                    param.paramValue.vText
            ));
            if (result == -1)
                return result;
        }

        return result;
    }

    public long saveEvent(@NonNull BasicNoteA note, @NonNull List<BasicNoteItemA> noteItems) {
        if (noteItems.size() > 0) {
            long eventId = getBasicHEventDAO().insert(BasicHEventA.fromEventType(mDBHelper.getDBDictionaryCache().getCheckoutHEventParamId()));
            if (eventId != -1) {
                long result = -1;
                for (BasicNoteItemA noteItem : noteItems) {
                    long hNoteCOItemId = insert(BasicHNoteCOItemA.fromEventData(eventId, note, noteItem));
                    BasicNoteItemParams params = noteItem.getNoteItemParams();
                    if ((hNoteCOItemId != -1) && (params.size() > 0)) {
                        result = insertHNoteCOItemParams(hNoteCOItemId, noteItem.getNoteItemParams());
                    }
                }
                return result;
            } else
                return eventId;
        } else {
            return 0;
        }
    }

    public long deleteEvent(@NonNull BasicNoteA note) {
        long result = 0;

        Set<Long> events = new HashSet<>();

        List<BasicHNoteCOItemA> hNoteCOItems = getByNoteId(note.getId());
        for (BasicHNoteCOItemA hNoteCOItem : hNoteCOItems) {
            //params
            getBasicHNoteCOItemParamDAO().deleteByNoteCOItemId(hNoteCOItem.getId());
            //data
            result = delete(hNoteCOItem);
            //save event
            events.add(hNoteCOItem.getEventId());
        }

        if (result != 0) {
            getBasicHEventDAO().deleteByIds(events);
        }

        return result;
    }
}
