package com.romanpulov.violetnote.db.dao;

import android.content.Context;
import android.database.Cursor;
import androidx.annotation.NonNull;

import com.romanpulov.violetnote.db.tabledef.HEventTypesTableDef;
import com.romanpulov.violetnote.model.BasicHEventTypeA;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * HEventType DAO
 */
public final class BasicHEventTypeDAO extends AbstractDAO<BasicHEventTypeA> {

    public BasicHEventTypeDAO(Context context) {
        super(context);
    }

    @Override
    @NonNull
    public List<BasicHEventTypeA> getAll() {
        final List<BasicHEventTypeA> result = new ArrayList<>();
        readCursor(new CursorReaderHandler() {
            @Override
            public Cursor createCursor() {
                return mDB.query(
                        HEventTypesTableDef.TABLE_NAME,
                        HEventTypesTableDef.TABLE_COLS,
                        null,
                        null,
                        null,
                        null,
                        null);
            }

            @Override
            public void readFromCursor(Cursor c) {
                result.add(BasicHEventTypeA.newInstance(
                        c.getLong(0)
                ));
            }
        });

        return result;
    }

    public Map<String, Long> getAllAsMap() {
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
}
