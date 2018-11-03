package com.romanpulov.violetnote.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.util.Log;

import com.romanpulov.violetnote.model.BasicCommonNoteA;
import com.romanpulov.violetnote.model.BasicEntityNoteA;
import com.romanpulov.violetnote.model.BasicNoteA;
import com.romanpulov.violetnote.model.BasicNoteDataA;
import com.romanpulov.violetnote.model.BasicNoteHistoryItemA;
import com.romanpulov.violetnote.model.BasicNoteItemA;
import com.romanpulov.violetnote.model.BasicNoteValueA;
import com.romanpulov.violetnote.model.BooleanUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * BasicNoteA database operations
 * Created by romanpulov on 17.08.2016.
 */
public class DBNoteManager extends BasicCommonNoteManager {
    private static void log(String message) {
        Log.d("DBNoteManager", message);
    }

    public DBNoteManager(Context context) {
        super(context);
    }

    public static BasicNoteA noteFromCursor(Cursor c, DateTimeFormatter dtf) {
        return BasicNoteA.newInstance(
                c.getLong(0),
                c.getLong(1),
                dtf.formatDateTimeDelimited(new Date(c.getLong(1)), "\n"),
                c.getLong(3),
                c.getInt(4),
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


    public static BasicNoteItemA noteItemFromCursor(Cursor c, DateTimeFormatter dtf) {
        return BasicNoteItemA.newInstance(
                c.getLong(0),
                c.getLong(1),
                dtf.formatDateTimeDelimited(new Date(c.getLong(1)), "\n"),
                c.getLong(3),
                c.getLong(2),
                c.getLong(7),
                c.getString(4),
                c.getString(5),
                BooleanUtils.fromInt(c.getInt(6))
        );
    }

    private static BasicNoteHistoryItemA noteHistoryItemFromCursor(Cursor c, DateTimeFormatter dtf) {
        return BasicNoteHistoryItemA.newInstance(
                c.getLong(0),
                c.getLong(1),
                dtf.formatDateTimeDelimited(new Date(c.getLong(1)), "\n"),
                c.getString(2)
        );
    }

    /**
     * Returns notes to UI
     * @return Note List
     */
    public ArrayList<BasicNoteA> queryNotes() {
        return queryNotesTotals();
    }

    /**
     * Returns notes from raw query, with totals
     * @return Note List
     */
    public ArrayList<BasicNoteA> queryNotesTotals() {
        ArrayList<BasicNoteA> result = new ArrayList<>();

        Cursor c = null;
        try {
            c = mDB.rawQuery(DBRawQueryRepository.NOTES_WITH_TOTALS, null);
            for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                BasicNoteA newNote = noteFromCursorWithTotals(c, mDTF);
                result.add(newNote);
            }

        } finally {
            if ((c !=null) && !c.isClosed())
                c.close();
        }

        return result;
    }

    public ArrayList<BasicNoteA> queryRelatedNotes(BasicNoteA note) {
        ArrayList<BasicNoteA> result = new ArrayList<>();

        Cursor c = null;
        try {

            c = mDB.query(DBBasicNoteOpenHelper.NOTES_TABLE_NAME, DBBasicNoteOpenHelper.NOTES_TABLE_COLS,
                    DBBasicNoteOpenHelper.ID_COLUMN_NAME + " != ? AND " +
                    DBBasicNoteOpenHelper.NOTE_TYPE_COLUMN_NAME + " = ? AND " +
                    DBBasicNoteOpenHelper.IS_ENCRYPTED_COLUMN_NAME + " = ?", new String[]{String.valueOf(note.getId()), String.valueOf(note.getNoteType()), String.valueOf(BooleanUtils.toInt(note.isEncrypted()))},
                    null, null, DBBasicNoteOpenHelper.TITLE_COLUMN_NAME
            );

            for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                BasicNoteA newNote = noteFromCursor(c, mDTF);
                result.add(newNote);
            }

        } finally {
            if ((c !=null) && !c.isClosed())
                c.close();
        }

        return result;
    }

    public BasicNoteDataA fromNoteData(BasicNoteA note) {
        ArrayList<BasicNoteA> notes = new ArrayList<>();
        ArrayList<BasicNoteA> relatedNotes = queryRelatedNotes(note);

        //get note
        notes.add(note);

        return BasicNoteDataA.newInstance(null, notes, relatedNotes);
    }

    public void queryNoteDataItems(BasicNoteA note) {

        String orderString = DBBasicNoteOpenHelper.PRIORITY_COLUMN_NAME + " DESC, " + DBBasicNoteOpenHelper.ORDER_COLUMN_NAME;
        if (PreferenceManager.getDefaultSharedPreferences(mContext).getBoolean("pref_interface_checked_last", false))
            orderString = DBBasicNoteOpenHelper.CHECKED_COLUMN_NAME + " ASC, " + orderString;

        //clear items
        note.getItems().clear();

        //get items
        Cursor c = null;
        try {
            c = mDB.query(
                    DBBasicNoteOpenHelper.NOTE_ITEMS_TABLE_NAME, DBBasicNoteOpenHelper.NOTE_ITEMS_TABLE_COLS,
                    DBBasicNoteOpenHelper.NOTE_ID_COLUMN_NAME + " = ?", new String[] {String.valueOf(note.getId())}, null, null,
                    orderString
            );

            int itemCount = 0;
            int checkedItemCount = 0;

            for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                BasicNoteItemA newItem = noteItemFromCursor(c, mDTF);
                if (newItem.isChecked())
                    checkedItemCount ++;
                itemCount ++;
                note.getItems().add(newItem);
            }

            note.setItemCount(itemCount);
            note.setCheckedItemCount(checkedItemCount);

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
                        DBBasicNoteOpenHelper.NOTE_ID_COLUMN_NAME + " = ?", new String[]{String.valueOf(note.getId())}, null, null, null
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

    public void queryNoteDataValuesOrdered(BasicNoteA note, List<BasicNoteValueA> values) {
        //clear values
        values.clear();

        Cursor c = null;
        try {
            c = mDB.query(
                    DBBasicNoteOpenHelper.NOTE_VALUES_TABLE_NAME, new String[]{DBBasicNoteOpenHelper.NOTE_VALUES_TABLE_COLS[0], DBBasicNoteOpenHelper.NOTE_VALUES_TABLE_COLS[2]},
                    DBBasicNoteOpenHelper.NOTE_ID_COLUMN_NAME + " = ?", new String[]{String.valueOf(note.getId())}, null, null, DBBasicNoteOpenHelper.NOTE_VALUES_TABLE_COLS[2]
            );

            for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                values.add(BasicNoteValueA.newInstance(c.getLong(0), c.getString(1)));

            }

        } finally {
            if ((c != null) && !c.isClosed())
                c.close();
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
            cv.put(DBBasicNoteOpenHelper.NOTE_ITEMS_TABLE_COLS[6], BooleanUtils.toInt(checked));

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
        cv.put(DBBasicNoteOpenHelper.NOTES_TABLE_COLS[3], note.getNoteGroupId());
        cv.put(DBBasicNoteOpenHelper.NOTES_TABLE_COLS[4], note.getNoteType());
        cv.put(DBBasicNoteOpenHelper.NOTES_TABLE_COLS[5], note.getTitle());
        cv.put(DBBasicNoteOpenHelper.NOTES_TABLE_COLS[6], BooleanUtils.toInt(note.isEncrypted()));
        cv.put(DBBasicNoteOpenHelper.NOTES_TABLE_COLS[7], note.getEncryptedString());

        return mDB.insert(DBBasicNoteOpenHelper.NOTES_TABLE_NAME, null, cv);
    }

    public long deleteNote(BasicCommonNoteA note) {
        String[] noteIdArgs = new String[] {String.valueOf(note.getId())};

        //history
        mDB.delete(DBBasicNoteOpenHelper.NOTE_ITEMS_HISTORY_TABLE_NAME, DBBasicNoteOpenHelper.NOTE_ID_SELECTION_STRING, noteIdArgs);
        //values
        mDB.delete(DBBasicNoteOpenHelper.NOTE_VALUES_TABLE_NAME, DBBasicNoteOpenHelper.NOTE_ID_SELECTION_STRING, noteIdArgs);
        //items
        mDB.delete(DBBasicNoteOpenHelper.NOTE_ITEMS_TABLE_NAME, DBBasicNoteOpenHelper.NOTE_ID_SELECTION_STRING, noteIdArgs);

        //note
        return mDB.delete(DBBasicNoteOpenHelper.NOTES_TABLE_NAME, DBBasicNoteOpenHelper.NOTES_TABLE_COLS[0] + "=" + note.getId(), null);
    }

    public long updateNote(BasicNoteA note) {
        ContentValues cv = new ContentValues();
        cv.put(DBBasicNoteOpenHelper.NOTES_TABLE_COLS[1], System.currentTimeMillis());
        cv.put(DBBasicNoteOpenHelper.NOTES_TABLE_COLS[4], note.getNoteType());
        cv.put(DBBasicNoteOpenHelper.NOTES_TABLE_COLS[5], note.getTitle());
        cv.put(DBBasicNoteOpenHelper.NOTES_TABLE_COLS[6], BooleanUtils.toInt(note.isEncrypted()));
        cv.put(DBBasicNoteOpenHelper.NOTES_TABLE_COLS[7], note.getEncryptedString());

        return mDB.update(DBBasicNoteOpenHelper.NOTES_TABLE_NAME, cv, DBBasicNoteOpenHelper.NOTES_TABLE_COLS[0] + "=" + note.getId(), null);
    }

    public long updateNoteValueValue(BasicNoteValueA item) {
        ContentValues cv = new ContentValues();

        cv.put(DBBasicNoteOpenHelper.NOTE_VALUES_TABLE_COLS[2], item.getValue());

        return mDB.update(DBBasicNoteOpenHelper.NOTE_VALUES_TABLE_NAME, cv, DBBasicNoteOpenHelper.ID_COLUMN_NAME + " = ?", new String[] {String.valueOf(item.getId())});
    }

    public long insertNoteItem(BasicNoteA note, BasicNoteItemA item) {
        ContentValues cv = new ContentValues();

        cv.put(DBBasicNoteOpenHelper.NOTE_ITEMS_TABLE_COLS[1], System.currentTimeMillis());
        cv.put(DBBasicNoteOpenHelper.NOTES_TABLE_COLS[2], DBBasicNoteHelper.getInstance(mContext).getMaxOrderId(DBBasicNoteOpenHelper.NOTE_ITEMS_TABLE_NAME, note.getId()) + 1);
        cv.put(DBBasicNoteOpenHelper.NOTE_ITEMS_TABLE_COLS[3], note.getId());
        cv.put(DBBasicNoteOpenHelper.NOTE_ITEMS_TABLE_COLS[4], item.getName());
        cv.put(DBBasicNoteOpenHelper.NOTE_ITEMS_TABLE_COLS[5], item.getValue());
        cv.put(DBBasicNoteOpenHelper.NOTE_ITEMS_TABLE_COLS[6], item.isChecked());
        cv.put(DBBasicNoteOpenHelper.NOTE_ITEMS_TABLE_COLS[7], item.getPriority());

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
        cv.put(DBBasicNoteOpenHelper.NOTE_ITEMS_TABLE_COLS[6], !item.isChecked());

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
            if (item.isChecked()) {

                //add note values and history for not encrypted only
                if (!note.isEncrypted()) {
                    //insert value
                    if (note.getValues().add(item.getValue()))
                        insertNoteValue(note, BasicNoteValueA.newEditInstance(item.getValue()));

                    //insert history
                    insertNoteHistory(note, item.getValue());
                }

                //delete note
                deleteNoteItem(item);
            }
        }
    }

    public int moveNoteItemOther(BasicNoteItemA noteItem, BasicNoteA otherNote) {
        //DBManagementProvider dbManagementProvider = noteItem.getDBManagementProvider();
        //long maxOrderId = mDBHelper.getMaxOrderId(dbManagementProvider.getTableName(), dbManagementProvider.getOrderIdSelection(), dbManagementProvider.getOrderIdSelectionArgs());
        long maxOrderId = mDBHelper.getNoteMaxOrderId(otherNote.getId(), noteItem.getPriority());
        return updateNoteItemOther(noteItem, otherNote.getId(), maxOrderId + 1);
    }

    /**
     * Updates NoteItem to another note
     * @param note NoteItem
     * @param otherNoteId noteId to move
     * @param otherOrderId orderId to set
     */
    public int updateNoteItemOther(BasicEntityNoteA note, long otherNoteId, long otherOrderId) {
        ContentValues cv = new ContentValues();

        cv.put(DBBasicNoteOpenHelper.NOTE_ID_COLUMN_NAME, otherNoteId);
        cv.put(DBBasicNoteOpenHelper.ORDER_COLUMN_NAME, otherOrderId);

        return mDB.update(DBBasicNoteOpenHelper.NOTE_ITEMS_TABLE_NAME, cv, DBBasicNoteOpenHelper.ID_COLUMN_NAME + " = ?" , new String[] {String.valueOf(note.getId())});
    }

    public long insertNoteValue(BasicEntityNoteA note, BasicNoteValueA value) {
        ContentValues cv = new ContentValues();

        cv.put(DBBasicNoteOpenHelper.NOTE_VALUES_TABLE_COLS[1], note.getId());
        cv.put(DBBasicNoteOpenHelper.NOTE_VALUES_TABLE_COLS[2], value.getValue());

        return mDB.insert(DBBasicNoteOpenHelper.NOTE_VALUES_TABLE_NAME, null, cv);
    }

    public long insertNoteHistory(BasicNoteA note, String value) {
        ContentValues cv = new ContentValues();

        cv.put(DBBasicNoteOpenHelper.NOTE_ITEMS_HISTORY_TABLE_COLS[1], System.currentTimeMillis());
        cv.put(DBBasicNoteOpenHelper.NOTE_ITEMS_HISTORY_TABLE_COLS[2], note.getId());
        cv.put(DBBasicNoteOpenHelper.NOTE_ITEMS_HISTORY_TABLE_COLS[3], value);

        return mDB.insert(DBBasicNoteOpenHelper.NOTE_ITEMS_HISTORY_TABLE_NAME, null, cv);
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
