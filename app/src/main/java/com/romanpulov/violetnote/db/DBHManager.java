package com.romanpulov.violetnote.db;

import android.content.Context;
import android.database.Cursor;

import com.romanpulov.violetnote.db.tabledef.HEventTypesTableDef;
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
}
