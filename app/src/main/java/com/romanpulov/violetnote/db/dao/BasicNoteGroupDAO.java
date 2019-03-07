package com.romanpulov.violetnote.db.dao;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;

import com.romanpulov.violetnote.db.tabledef.DBCommonDef;
import com.romanpulov.violetnote.db.tabledef.NoteGroupsTableDef;
import com.romanpulov.violetnote.model.BasicNoteGroupA;

import java.util.ArrayList;
import java.util.List;

public class BasicNoteGroupDAO extends AbstractDAO<BasicNoteGroupA> {

    public static BasicNoteGroupA fromCursor(@NonNull Cursor c) {
        return BasicNoteGroupA.newInstance(
                c.getLong(0),
                c.getLong(1),
                c.getString(2),
                c.getLong(3),
                c.getLong(4)
        );
    }

    public BasicNoteGroupDAO(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public List<BasicNoteGroupA> getAll() {
        final List<BasicNoteGroupA> result = new ArrayList<>();

        readCursor(new CursorReaderHandler() {
            @Override
            public Cursor createCursor() {
                return mDB.query(
                        NoteGroupsTableDef.TABLE_NAME,
                        NoteGroupsTableDef.TABLE_COLS,
                        null,
                        null,
                        null,
                        null,
                        NoteGroupsTableDef.NOTE_GROUP_TYPE_COLUMN_NAME + ", " + DBCommonDef.ORDER_COLUMN_NAME
                );
            }

            @Override
            public void readFromCursor(Cursor c) {
                result.add(fromCursor(c));
            }
        });

        return result;
    }
}
