package com.romanpulov.violetnote.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import androidx.annotation.NonNull;

import com.romanpulov.violetnote.db.DBRawQueryRepository;
import com.romanpulov.violetnote.db.tabledef.DBCommonDef;
import com.romanpulov.violetnote.db.tabledef.HEventsTableDef;
import com.romanpulov.violetnote.db.tabledef.HNoteItemsTableDef;
import com.romanpulov.violetnote.model.BasicHEventA;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * HEvent DAO
 */
public final class BasicHEventDAO extends AbstractDAO<BasicHEventA> {

    public BasicHEventDAO(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public List<BasicHEventA> getAll() {
        return getByType(0);
    }

    @NonNull
    public List<BasicHEventA> getByType(final long eventTypeId) {
        final List<BasicHEventA> result = new ArrayList<>();

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
                result.add(BasicHEventA.newInstance(
                        c.getLong(0),
                        c.getLong(1),
                        c.getLong(2),
                        c.getString(3)
                ));
            }
        });

        return result;
    }

    public List<BasicHEventA> getByCOItemsNoteId(final long noteId) {
        final List<BasicHEventA> result = new ArrayList<>();

        readCursor(new CursorReaderHandler() {
            @Override
            public Cursor createCursor() {
                return mDB.rawQuery(
                        DBRawQueryRepository.H_EVENTS_BY_CO_ITEMS_NOTE_ID,
                        new String[] {String.valueOf(noteId)}
                );
            }

            @Override
            public void readFromCursor(Cursor c) {
                result.add(BasicHEventA.newInstanceWithCount(
                        c.getLong(0),
                        c.getLong(1),
                        c.getLong(2),
                        c.getString(3),
                        c.getLong(4)
                ));
            }
        });

        return result;
    }

    @Override
    public long insert(@NonNull BasicHEventA object) {
        ContentValues cv = new ContentValues();

        cv.put(HEventsTableDef.EVENT_TYPE_ID_COLUMN_NAME, object.getEventTypeId());
        cv.put(HEventsTableDef.EVENT_TIME_COLUMN_NAME, System.currentTimeMillis());
        cv.put(HEventsTableDef.EVENT_SUMMARY_COLUMN_NAME, object.getEventSummary());

        return mDB.insert(HEventsTableDef.TABLE_NAME, null, cv);
    }

    @Override
    public long delete(@NonNull BasicHEventA object) {
        internalDeleteById(HNoteItemsTableDef.TABLE_NAME, HNoteItemsTableDef.EVENT_ID_COLUMN_NAME, object.getId());
        return internalDeleteById(HEventsTableDef.TABLE_NAME, object.getId());
    }

    public void deleteById(long id) {
        internalDeleteById(HEventsTableDef.TABLE_NAME, id);
    }

    public void deleteByIds(Collection<Long> ids) {
        for (long id : ids) {
            deleteById(id);
        }
    }
}
