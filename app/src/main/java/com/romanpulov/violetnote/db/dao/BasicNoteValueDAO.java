package com.romanpulov.violetnote.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import androidx.annotation.NonNull;

import com.romanpulov.violetnote.db.tabledef.DBCommonDef;
import com.romanpulov.violetnote.db.tabledef.NoteValuesTableDef;
import com.romanpulov.violetnote.model.BasicEntityNoteA;
import com.romanpulov.violetnote.model.BasicNoteA;
import com.romanpulov.violetnote.model.BasicNoteValueA;

import java.util.ArrayList;
import java.util.List;

/**
 * BasicNoteValue DAO
 */
public final class BasicNoteValueDAO extends AbstractDAO<BasicNoteValueA> {

    public BasicNoteValueDAO(Context context) {
        super(context);
    }

    @NonNull
    public List<BasicNoteValueA> getNoteValues(@NonNull final BasicNoteA note) {
        final List<BasicNoteValueA> result = new ArrayList<>();

        readCursor(new CursorReaderHandler() {
            @Override
            public Cursor createCursor() {
                return mDB.query(
                        NoteValuesTableDef.TABLE_NAME,
                        new String[]{DBCommonDef.ID_COLUMN_NAME, NoteValuesTableDef.VALUE_COLUMN_NAME},
                        DBCommonDef.NOTE_ID_COLUMN_NAME + " = ?",
                        new String[]{String.valueOf(note.getId())},
                        null,
                        null,
                        NoteValuesTableDef.VALUE_COLUMN_NAME
                );
            }

            @Override
            public void readFromCursor(Cursor c) {
                result.add(
                        BasicNoteValueA.newInstance(
                                c.getLong(0),
                                c.getString(1)
                        )
                );
            }
        });

        return result;
    }

    @Override
    public long update(@NonNull BasicNoteValueA object) {
        ContentValues cv = new ContentValues();

        cv.put(NoteValuesTableDef.VALUE_COLUMN_NAME, object.getValue());

        return mDB.update(NoteValuesTableDef.TABLE_NAME, cv, DBCommonDef.ID_COLUMN_NAME + " = ?", new String[] {String.valueOf(object.getId())});
    }

    public long insertWithNote(@NonNull BasicEntityNoteA note, String value) {
        ContentValues cv = new ContentValues();

        cv.put(NoteValuesTableDef.NOTE_ID_COLUMN_NAME, note.getId());
        cv.put(NoteValuesTableDef.VALUE_COLUMN_NAME, value);

        return mDB.insert(NoteValuesTableDef.TABLE_NAME, null, cv);
    }

    @Override
    public long delete(@NonNull BasicNoteValueA object) {
        return internalDeleteById(NoteValuesTableDef.TABLE_NAME, object.getId());
    }
}
