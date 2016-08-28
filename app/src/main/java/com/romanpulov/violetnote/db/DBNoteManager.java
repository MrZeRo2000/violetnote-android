package com.romanpulov.violetnote.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.romanpulov.violetnote.helper.DateTimeFormatterHelper;
import com.romanpulov.violetnote.model.BasicNoteA;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by romanpulov on 17.08.2016.
 */
public class DBNoteManager {
    private final Context mContext;
    private final SQLiteDatabase mDB;
    private final DateTimeFormatterHelper mDTF;

    public DBNoteManager(Context context) {
        mContext = context;
        mDB = DBBasicNoteHelper.getInstance(mContext).getDB();
        mDTF = new DateTimeFormatterHelper(context);
    }

    private static BasicNoteA fromCursor(Cursor c, DateTimeFormatterHelper dtf) {
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

    public ArrayList<BasicNoteA> queryNotes() {
        ArrayList<BasicNoteA> result = new ArrayList<>();

        Cursor c = null;
        try {
            c = mDB.query(
                    DBBasicNoteOpenHelper.NOTES_TABLE_NAME, DBBasicNoteOpenHelper.NOTES_TABLE_COLS,
                    null, null, null, null, DBBasicNoteOpenHelper.ORDER_COLUMN_NAME
            );

            for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                result.add(fromCursor(c, mDTF));
            }
        } finally {
            if ((c !=null) && !c.isClosed())
                c.close();
        }

        return result;
    }

    public long insertNote(BasicNoteA note) {
        ContentValues cv = new ContentValues();

        cv.put(DBBasicNoteOpenHelper.NOTES_TABLE_COLS[1], System.currentTimeMillis());
        cv.put(DBBasicNoteOpenHelper.NOTES_TABLE_COLS[2], DBBasicNoteHelper.getInstance(mContext).getMaxOrderId(DBBasicNoteOpenHelper.NOTES_TABLE_NAME) + 1);
        cv.put(DBBasicNoteOpenHelper.NOTES_TABLE_COLS[3], note.getNoteType());
        cv.put(DBBasicNoteOpenHelper.NOTES_TABLE_COLS[4], note.getTitle());
        cv.put(DBBasicNoteOpenHelper.NOTES_TABLE_COLS[5], BasicNoteA.toInt(note.getIsEncrypted()));
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
        cv.put(DBBasicNoteOpenHelper.NOTES_TABLE_COLS[5], BasicNoteA.toInt(note.getIsEncrypted()));
        cv.put(DBBasicNoteOpenHelper.NOTES_TABLE_COLS[6], note.getEncryptedString());

        return mDB.update(DBBasicNoteOpenHelper.NOTES_TABLE_NAME, cv, DBBasicNoteOpenHelper.NOTES_TABLE_COLS[0] + "=" + note.getId(), null);
    }

    public BasicNoteA get(int id) {
        Cursor c = null;
        try {
            c = mDB.query(
                    DBBasicNoteOpenHelper.NOTES_TABLE_NAME, DBBasicNoteOpenHelper.NOTES_TABLE_COLS,
                    DBBasicNoteOpenHelper.ID_COLUMN_NAME + "=?", new String[]{String.valueOf(id)}, null, null, null
            );
            c.moveToFirst();
            if (!c.isAfterLast()) {

                return fromCursor(c, mDTF);
            } else
                return null;
        } finally {
            if ((c !=null) && !c.isClosed())
                c.close();
        }
    }
}
