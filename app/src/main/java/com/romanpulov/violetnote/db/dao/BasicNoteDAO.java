package com.romanpulov.violetnote.db.dao;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.romanpulov.violetnote.db.DBRawQueryRepository;
import com.romanpulov.violetnote.db.DateTimeFormatter;
import com.romanpulov.violetnote.db.tabledef.DBCommonDef;
import com.romanpulov.violetnote.db.tabledef.NotesTableDef;
import com.romanpulov.violetnote.model.BasicNoteA;
import com.romanpulov.violetnote.model.BooleanUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * BasicNoteDAO class
 */
public final class BasicNoteDAO extends AbstractDAO<BasicNoteA> {

    public BasicNoteDAO(Context context) {
        super(context);
    }

    @NonNull
    public static BasicNoteA noteFromCursor(@NonNull Cursor c, @NonNull DateTimeFormatter dtf) {
        return BasicNoteA.newInstance(
                c.getLong(0),
                c.getLong(1),
                dtf.formatDateTimeDelimited(new Date(c.getLong(1)), "\n"),
                c.getLong(2),
                c.getInt(3),
                c.getInt(4),
                c.getString(5),
                BooleanUtils.fromInt(c.getInt(6)),
                c.getString(7)
        );
    }

    public static BasicNoteA noteFromCursorWithTotals(Cursor c, DateTimeFormatter dtf) {
        return BasicNoteA.newInstanceWithTotals(
                c.getLong(0),
                c.getLong(1),
                dtf.formatDateTimeDelimited(new Date(c.getLong(1)), "\n"),
                c.getLong(2),
                c.getInt(3),
                c.getInt(4),
                c.getString(5),
                BooleanUtils.fromInt(c.getInt(6)),
                c.getString(7),
                c.getInt(8),
                c.getInt(9)
        );
    }

    @NonNull
    @Override
    public List<BasicNoteA> getAll() {
        final List<BasicNoteA> result = new ArrayList<>();

        readCursor(new CursorReaderHandler() {
            @Override
            public Cursor createCursor() {
                return mDB.query(
                        NotesTableDef.TABLE_NAME,
                        NotesTableDef.TABLE_COLS,
                        null,
                        null,
                        null,
                        null,
                        NotesTableDef.TITLE_COLUMN_NAME
                );
            }

            @Override
            public void readFromCursor(Cursor c) {
                result.add(noteFromCursor(c, mDTF));
            }
        });

        return result;
    }

    /**
     * Returns notes from raw query, with totals
     * @return Note List
     */
    public List<BasicNoteA> getTotals() {
        final List<BasicNoteA> result = new ArrayList<>();

        readCursor(new CursorReaderHandler() {
            @Override
            public Cursor createCursor() {
                return mDB.rawQuery(DBRawQueryRepository.NOTES_WITH_TOTALS, null);
            }

            @Override
            public void readFromCursor(Cursor c) {
                result.add(noteFromCursorWithTotals(c, mDTF));
            }
        });

        return result;
    }

    public List<BasicNoteA> getRelatedNotes(final BasicNoteA note) {
        final List<BasicNoteA> result = new ArrayList<>();

        readCursor(new CursorReaderHandler() {
            @Override
            public Cursor createCursor() {
                return mDB.query(
                        NotesTableDef.TABLE_NAME,
                        NotesTableDef.TABLE_COLS,
                        DBCommonDef.ID_COLUMN_NAME + " != ? AND " +
                                NotesTableDef.NOTE_TYPE_COLUMN_NAME + " = ? AND " +
                                NotesTableDef.IS_ENCRYPTED_COLUMN_NAME + " = ?",
                        new String[]{String.valueOf(note.getId()), String.valueOf(note.getNoteType()), String.valueOf(BooleanUtils.toInt(note.isEncrypted()))},
                        null,
                        null,
                        NotesTableDef.TITLE_COLUMN_NAME
                );
            }

            @Override
            public void readFromCursor(Cursor c) {
                result.add(noteFromCursor(c, mDTF));
            }
        });

        return result;
    }

    @Nullable
    public BasicNoteA getById(final long id) {
        final BasicNoteA[] result = new BasicNoteA[1];

        readCursor(new CursorReaderHandler() {
            @Override
            public Cursor createCursor() {
                return mDB.query(
                        NotesTableDef.TABLE_NAME,
                        NotesTableDef.TABLE_COLS,
                        DBCommonDef.ID_COLUMN_NAME + "=?",
                        new String[] {String.valueOf(id)},
                        null,
                        null,
                        null
                );
            }

            @Override
            public void readFromCursor(Cursor c) {
                result[0] = noteFromCursor(c, mDTF);
            }
        });

        return result[0];
    }
}
