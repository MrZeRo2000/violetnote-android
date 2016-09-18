package com.romanpulov.violetnote.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.romanpulov.violetnote.helper.DateTimeFormatterHelper;
import com.romanpulov.violetnote.model.BasicCommonNoteA;
import com.romanpulov.violetnote.model.BasicEntityNoteA;
import com.romanpulov.violetnote.model.BasicNoteA;
import com.romanpulov.violetnote.model.BasicNoteDataA;
import com.romanpulov.violetnote.model.BasicNoteItemA;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by romanpulov on 17.08.2016.
 */
public class DBNoteManager extends BasicCommonNoteManager {

    public DBNoteManager(Context context) {
        super(context);
    }

    private static BasicNoteA noteFromCursor(Cursor c, DateTimeFormatterHelper dtf) {
        return BasicNoteA.newInstance(
                c.getLong(0),
                c.getLong(1),
                dtf.formatDateTimeDelimited(new Date(c.getLong(1)), "\n"),
                c.getLong(2),
                c.getInt(3),
                c.getString(4),
                BasicNoteA.fromInt(c.getInt(5)),
                c.getString(6)
        );
    }

    private static BasicNoteItemA noteItemFromCursor(Cursor c, DateTimeFormatterHelper dtf) {
        return BasicNoteItemA.newInstance(
                c.getLong(0),
                c.getLong(1),
                dtf.formatDateTimeDelimited(new Date(c.getLong(1)), "\n"),
                c.getLong(2),
                c.getString(4),
                c.getString(5),
                BasicCommonNoteA.fromInt(c.getInt(6))
        );
    }

    public ArrayList<BasicNoteA> queryNotes() {
        ArrayList<BasicNoteA> result = new ArrayList<>();

        Cursor c = null;
        try {
            c = mDB.query(
                    DBBasicNoteOpenHelper.NOTES_TABLE_NAME, DBBasicNoteOpenHelper.NOTES_TABLE_COLS,
                    null, null, null, null, DBBasicNoteOpenHelper.ORDER_COLUMN_NAME
            );

            for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                result.add(noteFromCursor(c, mDTF));
            }
        } finally {
            if ((c !=null) && !c.isClosed())
                c.close();
        }

        return result;
    }

    public Collection<String> queryNoteValues(BasicNoteA note) {
        Set<String> values = new HashSet<>();

        Cursor c = null;
        try {
            c = mDB.query(
                    DBBasicNoteOpenHelper.NOTE_VALUES_TABLE_NAME, new String[] {DBBasicNoteOpenHelper.NOTE_VALUES_TABLE_COLS[2]},
                    "note_id=?", new String[] {String.valueOf(note.getId())}, null, null, null
            );

            for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                values.add(c.getString(0));
            }

        } finally {
            if ((c !=null) && !c.isClosed())
                c.close();
        }

        return values;
    }

    public BasicNoteDataA queryNoteData(BasicNoteA note) {
        ArrayList<BasicNoteA> notes = new ArrayList<>();

        //get note
        notes.add(note);

        //get items
        queryNoteDataItems(note);

        //get values
        queryNoteDataValues(note);

        return BasicNoteDataA.newInstance(null, notes);
    }

    public void queryNoteDataItems(BasicNoteA note) {
        //clear items
        note.getItems().clear();

        //get items
        Cursor c = null;
        try {
            c = mDB.query(
                    DBBasicNoteOpenHelper.NOTE_ITEMS_TABLE_NAME, DBBasicNoteOpenHelper.NOTE_ITEMS_TABLE_COLS,
                    "note_id=?", new String[] {String.valueOf(note.getId())}, null, null, DBBasicNoteOpenHelper.ORDER_COLUMN_NAME
            );

            for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                note.getItems().add(noteItemFromCursor(c, mDTF));
            }

        } finally {
            if ((c !=null) && !c.isClosed())
                c.close();
        }
    }

    public void queryNoteDataValues(BasicNoteA note) {
        //clear values
        note.getValues().clear();

        if (!note.isEncrypted()) {
            //get values
            Cursor c = null;
            try {
                c = mDB.query(
                        DBBasicNoteOpenHelper.NOTE_VALUES_TABLE_NAME, new String[]{DBBasicNoteOpenHelper.NOTE_VALUES_TABLE_COLS[2]},
                        "note_id=?", new String[]{String.valueOf(note.getId())}, null, null, null
                );

                for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                    note.getValues().add(c.getString(0));
                }

            } finally {
                if ((c != null) && !c.isClosed())
                    c.close();
            }
        }
    }

    public void refreshNotes(ArrayList<BasicNoteA> notes) {
        ArrayList<BasicNoteA> newNotes = queryNotes();
        notes.clear();
        notes.addAll(newNotes);
    }

    public void updateNoteDataChecked(BasicNoteDataA noteData, boolean checked) {
        for (BasicNoteItemA item : noteData.getNoteList().get(0).getItems()) {
            ContentValues cv = new ContentValues();

            cv.put(DBBasicNoteOpenHelper.NOTE_ITEMS_TABLE_COLS[1], System.currentTimeMillis());
            cv.put(DBBasicNoteOpenHelper.NOTE_ITEMS_TABLE_COLS[6], BasicCommonNoteA.toInt(checked));

            mDB.update(DBBasicNoteOpenHelper.NOTE_ITEMS_TABLE_NAME, cv, DBBasicNoteOpenHelper.ID_COLUMN_NAME + " = ?" , new String[] {String.valueOf(item.getId())});
        }
    }

    public BasicNoteA queryById(long id) {
        Cursor c = null;
        try {
            c = mDB.query(
                    DBBasicNoteOpenHelper.NOTES_TABLE_NAME, DBBasicNoteOpenHelper.NOTES_TABLE_COLS,
                    DBBasicNoteOpenHelper.ID_COLUMN_NAME + "=?", new String[] {String.valueOf(id)}, null, null, null
            );

            c.moveToFirst();
            if (!c.isAfterLast()) {
                return noteFromCursor(c, mDTF);
            } else
                return null;
        } finally {
            if ((c !=null) && !c.isClosed())
                c.close();
        }
    }

    public long insertNote(BasicNoteA note) {
        ContentValues cv = new ContentValues();

        cv.put(DBBasicNoteOpenHelper.NOTES_TABLE_COLS[1], System.currentTimeMillis());
        cv.put(DBBasicNoteOpenHelper.NOTES_TABLE_COLS[2], DBBasicNoteHelper.getInstance(mContext).getMaxOrderId(DBBasicNoteOpenHelper.NOTES_TABLE_NAME, 0) + 1);
        cv.put(DBBasicNoteOpenHelper.NOTES_TABLE_COLS[3], note.getNoteType());
        cv.put(DBBasicNoteOpenHelper.NOTES_TABLE_COLS[4], note.getTitle());
        cv.put(DBBasicNoteOpenHelper.NOTES_TABLE_COLS[5], BasicNoteA.toInt(note.isEncrypted()));
        cv.put(DBBasicNoteOpenHelper.NOTES_TABLE_COLS[6], note.getEncryptedString());

        return mDB.insert(DBBasicNoteOpenHelper.NOTES_TABLE_NAME, null, cv);
    }

    public long deleteNote(BasicNoteA note) {
        return mDB.delete(DBBasicNoteOpenHelper.NOTES_TABLE_NAME, DBBasicNoteOpenHelper.NOTES_TABLE_COLS[0] + "=" + note.getId(), null);
    }

    public long updateNote(BasicNoteA note) {
        ContentValues cv = new ContentValues();
        cv.put(DBBasicNoteOpenHelper.NOTES_TABLE_COLS[1], System.currentTimeMillis());
        cv.put(DBBasicNoteOpenHelper.NOTES_TABLE_COLS[3], note.getNoteType());
        cv.put(DBBasicNoteOpenHelper.NOTES_TABLE_COLS[4], note.getTitle());
        cv.put(DBBasicNoteOpenHelper.NOTES_TABLE_COLS[5], BasicNoteA.toInt(note.isEncrypted()));
        cv.put(DBBasicNoteOpenHelper.NOTES_TABLE_COLS[6], note.getEncryptedString());

        return mDB.update(DBBasicNoteOpenHelper.NOTES_TABLE_NAME, cv, DBBasicNoteOpenHelper.NOTES_TABLE_COLS[0] + "=" + note.getId(), null);
    }

    public long insertNoteItem(BasicNoteA note, BasicNoteItemA item) {
        ContentValues cv = new ContentValues();

        cv.put(DBBasicNoteOpenHelper.NOTE_ITEMS_TABLE_COLS[1], System.currentTimeMillis());
        cv.put(DBBasicNoteOpenHelper.NOTES_TABLE_COLS[2], DBBasicNoteHelper.getInstance(mContext).getMaxOrderId(DBBasicNoteOpenHelper.NOTE_ITEMS_TABLE_NAME, note.getId()) + 1);
        cv.put(DBBasicNoteOpenHelper.NOTE_ITEMS_TABLE_COLS[3], note.getId());
        cv.put(DBBasicNoteOpenHelper.NOTE_ITEMS_TABLE_COLS[4], item.getName());
        cv.put(DBBasicNoteOpenHelper.NOTE_ITEMS_TABLE_COLS[5], item.getValue());
        cv.put(DBBasicNoteOpenHelper.NOTE_ITEMS_TABLE_COLS[6], item.getChecked());

        return mDB.insert(DBBasicNoteOpenHelper.NOTE_ITEMS_TABLE_NAME, null, cv);
    }

    public long deleteNoteItem(BasicNoteItemA item) {
        return mDB.delete(DBBasicNoteOpenHelper.NOTE_ITEMS_TABLE_NAME, DBBasicNoteOpenHelper.ID_COLUMN_NAME + "=?", new String[] {String.valueOf(item.getId())});
    }

    public long deleteEntityNote(String tableName, BasicEntityNoteA item) {
        return mDB.delete(tableName, DBBasicNoteOpenHelper.ID_COLUMN_NAME + "=?", new String[] {String.valueOf(item.getId())});
    }

    public long checkNoteItem(BasicNoteItemA item) {
        ContentValues cv = new ContentValues();

        cv.put(DBBasicNoteOpenHelper.NOTE_ITEMS_TABLE_COLS[1], System.currentTimeMillis());
        cv.put(DBBasicNoteOpenHelper.NOTE_ITEMS_TABLE_COLS[6], !item.getChecked());

        return mDB.update(DBBasicNoteOpenHelper.NOTE_ITEMS_TABLE_NAME, cv, DBBasicNoteOpenHelper.NOTE_ITEMS_TABLE_COLS[0] + " = ?" , new String[] {String.valueOf(item.getId())});
    }

    public long updateNoteItemNameValue(BasicNoteItemA item) {
        ContentValues cv = new ContentValues();

        cv.put(DBBasicNoteOpenHelper.NOTE_ITEMS_TABLE_COLS[1], System.currentTimeMillis());
        if (item.getName() != null)
            cv.put(DBBasicNoteOpenHelper.NOTE_ITEMS_TABLE_COLS[4], item.getName());
        cv.put(DBBasicNoteOpenHelper.NOTE_ITEMS_TABLE_COLS[5], item.getValue());

        return mDB.update(DBBasicNoteOpenHelper.NOTE_ITEMS_TABLE_NAME, cv, DBBasicNoteOpenHelper.NOTE_ITEMS_TABLE_COLS[0] + " = ?" , new String[] {String.valueOf(item.getId())});
    }

    public void checkOut(BasicNoteA note) {
        for (BasicNoteItemA item : note.getItems()) {
            if (item.getChecked()) {
                //add note values for not encrypted only
                if (!note.isEncrypted()) {
                    if (note.getValues().add(item.getValue()))
                        insertNoteValue(note, item.getValue());
                }

                //delete note
                deleteNoteItem(item);
            }
        }
    }

    public long insertNoteValue(BasicNoteA note, String value) {
        ContentValues cv = new ContentValues();

        cv.put(DBBasicNoteOpenHelper.NOTE_VALUES_TABLE_COLS[1], note.getId());
        cv.put(DBBasicNoteOpenHelper.NOTE_VALUES_TABLE_COLS[2], value);

        return mDB.insert(DBBasicNoteOpenHelper.NOTE_VALUES_TABLE_NAME, null, cv);
    }

    public BasicNoteItemA getNoteItem(long id) {
        Cursor c = null;
        try {
            c = mDB.query(
                    DBBasicNoteOpenHelper.NOTE_ITEMS_TABLE_NAME, DBBasicNoteOpenHelper.NOTE_ITEMS_TABLE_COLS,
                    DBBasicNoteOpenHelper.ID_COLUMN_NAME + "=?", new String[]{String.valueOf(id)}, null, null, null
            );
            c.moveToFirst();
            if (!c.isAfterLast())
                return noteItemFromCursor(c, mDTF);
            else
                return null;
        } finally {
            if ((c !=null) && !c.isClosed())
                c.close();
        }

    }

    public BasicNoteA get(long id) {
        Cursor c = null;
        try {
            c = mDB.query(
                    DBBasicNoteOpenHelper.NOTES_TABLE_NAME, DBBasicNoteOpenHelper.NOTES_TABLE_COLS,
                    DBBasicNoteOpenHelper.ID_COLUMN_NAME + "=?", new String[]{String.valueOf(id)}, null, null, null
            );
            c.moveToFirst();
            if (!c.isAfterLast())
                return noteFromCursor(c, mDTF);
            else
                return null;
        } finally {
            if ((c !=null) && !c.isClosed())
                c.close();
        }
    }
}
