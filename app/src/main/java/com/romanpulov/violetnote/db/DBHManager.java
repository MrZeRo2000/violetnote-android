package com.romanpulov.violetnote.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;

import com.romanpulov.violetnote.db.tabledef.DBCommonDef;
import com.romanpulov.violetnote.db.tabledef.HEventTypesTableDef;
import com.romanpulov.violetnote.db.tabledef.HEventsTableDef;
import com.romanpulov.violetnote.model.BasicHEventA;
import com.romanpulov.violetnote.model.BasicHEventTypeA;

import java.util.ArrayList;
import java.util.List;

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

    public long insertHEvent(long eventTypeId, String eventSummary) {
        ContentValues cv = new ContentValues();

        cv.put(HEventsTableDef.EVENT_TYPE_ID_COLUMN_NAME, eventTypeId);
        cv.put(HEventsTableDef.EVENT_TIME_COLUMN_NAME, System.currentTimeMillis());
        cv.put(HEventsTableDef.EVENT_SUMMARY_COLUMN_NAME, eventSummary);

        return mDB.insert(HEventsTableDef.TABLE_NAME, null, cv);
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
}
