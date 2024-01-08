package com.romanpulov.violetnote.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.romanpulov.violetnote.db.tabledef.DBCommonDef;
import com.romanpulov.violetnote.db.tabledef.HNoteCOItemsTableDef;

import java.util.ArrayList;
import java.util.List;

/**
 * Upgrades data
 */
public class DBDataUpgradeManager {

    private final SQLiteDatabase mDB;

    public DBDataUpgradeManager(SQLiteDatabase db) {
        this.mDB = db;
    }

    /** @noinspection SameParameterValue*/
    private void collapseEvents(long retentionPeriod) {

        class EventItemTime {
            private final long _id;
            private final long event_id;
            private final long event_time;

            private EventItemTime(long _id, long event_id, long event_time) {
                this._id = _id;
                this.event_id = event_id;
                this.event_time = event_time;
            }
        }

        final List<EventItemTime> eventItemTimeData = new ArrayList<>();

        Cursor c = null;
        try {
            c = mDB.rawQuery(DBRawQueryRepository.H_EVENT_CO_ITEMS_TIME, new String[]{});
            for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                eventItemTimeData.add(new EventItemTime(
                        c.getLong(0),
                        c.getLong(1),
                        c.getLong(2)
                ));
            }
        } finally {
            if ((c !=null) && !c.isClosed())
                c.close();
        }


        EventItemTime lastItem = null;
        for (EventItemTime item: eventItemTimeData) {
            if (lastItem == null) {
                lastItem = item;
            } else {
                if (item.event_time - lastItem.event_time < retentionPeriod) {
                    //update event id from last item
                    ContentValues cv = new ContentValues();
                    cv.put(HNoteCOItemsTableDef.EVENT_ID_COLUMN_NAME, lastItem.event_id);
                    mDB.update(HNoteCOItemsTableDef.TABLE_NAME, cv, DBCommonDef.ID_COLUMN_NAME + "=?", new String[]{String.valueOf(item._id)});
                } else {
                    lastItem = item;
                }
            }
        }
    }

    private void deleteOrphanedEvents() {
        mDB.execSQL(DBRawQueryRepository.H_EVENT_DELETE_ORPHANED);
    }

    public void upgradeData(int oldVersion) {
        switch(oldVersion) {
            case 4:
                collapseEvents( 10000);
                deleteOrphanedEvents();
                break;
            case 100:
                break;
        }
    }
}
