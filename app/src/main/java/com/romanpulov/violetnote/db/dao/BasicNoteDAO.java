package com.romanpulov.violetnote.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.romanpulov.violetnote.db.DBRawQueryRepository;
import com.romanpulov.violetnote.db.DateTimeFormatter;
import com.romanpulov.violetnote.db.tabledef.DBCommonDef;
import com.romanpulov.violetnote.db.tabledef.NoteItemsHistoryTableDef;
import com.romanpulov.violetnote.db.tabledef.NoteValuesTableDef;
import com.romanpulov.violetnote.db.tabledef.NotesTableDef;
import com.romanpulov.violetnote.model.BasicNoteA;
import com.romanpulov.violetnote.model.BasicNoteDataA;
import com.romanpulov.violetnote.model.BasicNoteItemA;
import com.romanpulov.violetnote.model.BooleanUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

/**
 * BasicNoteDAO class
 */
public final class BasicNoteDAO extends AbstractBasicNoteDAO<BasicNoteA> {

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

    public BasicNoteDataA createNoteDataFromNote(BasicNoteA note) {
        ArrayList<BasicNoteA> notes = new ArrayList<>();
        List<BasicNoteA> relatedNotes = getRelatedNotes(note);

        //get note
        notes.add(note);

        return BasicNoteDataA.newInstance(null, notes, relatedNotes);
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

    public Collection<String> getNoteValues(@NonNull final BasicNoteA note, final boolean isOrdered) {
        final Collection<String> result = new HashSet<>();

        if (!note.isEncrypted()) {
            readCursor(new CursorReaderHandler() {
                @Override
                public Cursor createCursor() {
                    return mDB.query(
                            NoteValuesTableDef.TABLE_NAME,
                            new String[]{NoteValuesTableDef.VALUE_COLUMN_NAME},
                            DBCommonDef.NOTE_ID_COLUMN_NAME + " = ?",
                            new String[]{String.valueOf(note.getId())},
                            null,
                            null,
                            isOrdered ? NoteValuesTableDef.VALUE_COLUMN_NAME : null
                    );
                }

                @Override
                public void readFromCursor(Cursor c) {
                    result.add(c.getString(0));
                }
            });
        }

        return result;
    }

    @NonNull
    public Collection<String> getNoteValues(@NonNull final BasicNoteA note) {
        return getNoteValues(note, false);
    }

    public void fillNoteValues(@NonNull final BasicNoteA note) {
        note.setValues(getNoteValues(note));
    }

    public void fillNotes(@NonNull ArrayList<BasicNoteA> notes) {
        List<BasicNoteA> newNotes = getTotals();
        notes.clear();
        notes.addAll(newNotes);
    }

    @Override
    public long insert(@NonNull BasicNoteA object) {
        ContentValues cv = new ContentValues();

        cv.put(NotesTableDef.LAST_MODIFIED_COLUMN_NAME, System.currentTimeMillis());
        cv.put(NotesTableDef.ORDER_COLUMN_NAME, mDBHelper.getMaxOrderId(NotesTableDef.TABLE_NAME, 0) + 1);
        cv.put(NotesTableDef.GROUP_ID_COLUMN_NAME, object.getNoteGroupId());
        cv.put(NotesTableDef.NOTE_TYPE_COLUMN_NAME, object.getNoteType());
        cv.put(NotesTableDef.TITLE_COLUMN_NAME, object.getTitle());
        cv.put(NotesTableDef.IS_ENCRYPTED_COLUMN_NAME, BooleanUtils.toInt(object.isEncrypted()));
        cv.put(NotesTableDef.ENCRYPTED_STRING_COLUMN_NAME, object.getEncryptedString());

        return mDB.insert(NotesTableDef.TABLE_NAME, null, cv);
    }

    @Override
    public long delete(@NonNull BasicNoteA object) {
        String[] noteIdArgs = new String[] {String.valueOf(object.getId())};

        //history
        mDB.delete(NoteItemsHistoryTableDef.TABLE_NAME, DBCommonDef.NOTE_ID_SELECTION_STRING, noteIdArgs);
        //values
        internalDeleteById(NoteValuesTableDef.TABLE_NAME, DBCommonDef.NOTE_ID_COLUMN_NAME, object.getId());
        //items
        BasicNoteItemDAO basicNoteItemDAO = new BasicNoteItemDAO(mContext);
        List<BasicNoteItemA> noteItems = basicNoteItemDAO.getByNote(object);
        for (BasicNoteItemA noteItem : noteItems) {
            basicNoteItemDAO.delete(noteItem);
        }
        //h
        BasicHNoteCOItemDAO basicHNoteCOItemDAO = new BasicHNoteCOItemDAO(mContext);
        basicHNoteCOItemDAO.deleteEvent(object);

        //note
        return internalDeleteById(NotesTableDef.TABLE_NAME, object.getId());
    }

    @Override
    public long update(@NonNull BasicNoteA object) {
        ContentValues cv = new ContentValues();

        cv.put(NotesTableDef.LAST_MODIFIED_COLUMN_NAME, System.currentTimeMillis());
        cv.put(NotesTableDef.NOTE_TYPE_COLUMN_NAME, object.getNoteType());
        cv.put(NotesTableDef.TITLE_COLUMN_NAME, object.getTitle());
        cv.put(NotesTableDef.IS_ENCRYPTED_COLUMN_NAME, BooleanUtils.toInt(object.isEncrypted()));
        cv.put(NotesTableDef.ENCRYPTED_STRING_COLUMN_NAME, object.getEncryptedString());

        return mDB.update(NotesTableDef.TABLE_NAME, cv, DBCommonDef.ID_COLUMN_NAME + "=" + object.getId(), null);
    }

    public void checkOut(@NonNull BasicNoteA note) {
        for (BasicNoteItemA item : note.getItems()) {
            if (item.isChecked()) {

                //add note values and history for not encrypted only
                if (!note.isEncrypted()) {
                    //insert value
                    if (note.getValues().add(item.getValue()))
                        getBasicNoteValueDAO().insertWithNote(note, item.getValue());

                    //insert history
                    getBasicNoteHistoryDAO().insertNoteValue(note, item.getValue());

                    //insert h
                    getBasicHNoteCOItemDAO().saveEvent(note, item);
                }

                //delete note
                getBasicNoteItemDAO().delete(item);
            }
        }
    }

}
