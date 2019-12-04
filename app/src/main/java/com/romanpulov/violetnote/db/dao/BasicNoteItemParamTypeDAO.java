package com.romanpulov.violetnote.db.dao;

import android.content.Context;
import android.database.Cursor;
import androidx.annotation.NonNull;

import com.romanpulov.violetnote.db.tabledef.NoteItemParamTypesTableDef;
import com.romanpulov.violetnote.model.BasicNoteItemParamTypeA;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * BasicNoteItemParamType DAO
 */
public final class BasicNoteItemParamTypeDAO extends AbstractDAO<BasicNoteItemParamTypeA> {

    public BasicNoteItemParamTypeDAO(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public List<BasicNoteItemParamTypeA> getAll() {
        final List<BasicNoteItemParamTypeA> result = new ArrayList<>();

        readCursor(new CursorReaderHandler() {
            @Override
            public Cursor createCursor() {
                return mDB.query(
                        NoteItemParamTypesTableDef.TABLE_NAME,
                        NoteItemParamTypesTableDef.TABLE_COLS,
                        null,
                        null,
                        null,
                        null,
                        null
                );
            }

            @Override
            public void readFromCursor(Cursor c) {
                result.add(BasicNoteItemParamTypeA.newInstance(
                        c.getLong(0),
                        c.getString(1)
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
                return mDB.query(
                        NoteItemParamTypesTableDef.TABLE_NAME,
                        NoteItemParamTypesTableDef.TABLE_COLS,
                        null,
                        null,
                        null,
                        null,
                        null
                );
            }

            @Override
            public void readFromCursor(Cursor c) {
                result.put(c.getString(1), c.getLong(0));
            }
        });

        return result;
    }
}
