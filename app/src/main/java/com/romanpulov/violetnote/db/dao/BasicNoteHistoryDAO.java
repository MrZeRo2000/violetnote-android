package com.romanpulov.violetnote.db.dao;

import android.content.ContentValues;
import android.content.Context;
import androidx.annotation.NonNull;

import com.romanpulov.violetnote.db.tabledef.NoteItemsHistoryTableDef;
import com.romanpulov.violetnote.model.BasicNoteA;

/**
 * BasicNoteHistory DAO
 */
public final class BasicNoteHistoryDAO extends AbstractDAO {

    public BasicNoteHistoryDAO(Context context) {
        super(context);
    }

    public long insertNoteValue(@NonNull BasicNoteA note, String value) {
        ContentValues cv = new ContentValues();

        cv.put(NoteItemsHistoryTableDef.LAST_MODIFIED_COLUMN_NAME, System.currentTimeMillis());
        cv.put(NoteItemsHistoryTableDef.NOTE_ID_COLUMN_NAME, note.getId());
        cv.put(NoteItemsHistoryTableDef.VALUE_COLUMN_NAME, value);

        return mDB.insert(NoteItemsHistoryTableDef.TABLE_NAME, null, cv);
    }
}
