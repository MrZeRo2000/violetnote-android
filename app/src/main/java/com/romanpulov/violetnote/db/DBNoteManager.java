package com.romanpulov.violetnote.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.romanpulov.violetnote.db.dao.BasicNoteDAO;
import com.romanpulov.violetnote.db.dao.BasicNoteItemDAO;
import com.romanpulov.violetnote.db.dao.BasicNoteItemParamsDAO;
import com.romanpulov.violetnote.db.tabledef.DBCommonDef;
import com.romanpulov.violetnote.db.tabledef.NoteItemParamTypesTableDef;
import com.romanpulov.violetnote.db.tabledef.NoteItemParamsTableDef;
import com.romanpulov.violetnote.db.tabledef.NoteItemsHistoryTableDef;
import com.romanpulov.violetnote.db.tabledef.NoteItemsTableDef;
import com.romanpulov.violetnote.db.tabledef.NoteValuesTableDef;
import com.romanpulov.violetnote.db.tabledef.NotesTableDef;
import com.romanpulov.violetnote.model.BasicEntityNoteA;
import com.romanpulov.violetnote.model.BasicNoteA;
import com.romanpulov.violetnote.model.BasicNoteDataA;
import com.romanpulov.violetnote.model.BasicNoteItemA;
import com.romanpulov.violetnote.model.BasicNoteItemParams;
import com.romanpulov.violetnote.model.BasicNoteValueA;
import com.romanpulov.violetnote.model.vo.BasicParamValueA;
import com.romanpulov.violetnote.model.BooleanUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * BasicNoteA database operations
 * Created by romanpulov on 17.08.2016.
 */
public class DBNoteManager extends BasicCommonNoteManager {
    private static final String TAG = "DBNoteManager";
    private static void log(String message) {
        Log.d(TAG, message);
    }

    private final DBHManager mDBHManager;
    public final BasicNoteDAO mBasicNoteDAO;
    public final BasicNoteItemDAO mBasicNoteItemDAO;
    public final BasicNoteItemParamsDAO mBasicNoteItemParamsDAO;

    public DBNoteManager(Context context) {
        super(context);
        mDBHManager = new DBHManager(context);
        mBasicNoteDAO = new BasicNoteDAO(context);
        mBasicNoteItemDAO = new BasicNoteItemDAO(context);
        mBasicNoteItemParamsDAO = new BasicNoteItemParamsDAO(context);
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

    public BasicNoteDataA fromNoteData(BasicNoteA note) {
        ArrayList<BasicNoteA> notes = new ArrayList<>();
        List<BasicNoteA> relatedNotes = mBasicNoteDAO.getRelatedNotes(note);

        //get note
        notes.add(note);

        return BasicNoteDataA.newInstance(null, notes, relatedNotes);
    }

    /*
   public void queryNoteDataItems(BasicNoteA note) {

        String orderString = DBCommonDef.PRIORITY_COLUMN_NAME + " DESC, " + DBCommonDef.ORDER_COLUMN_NAME;
        if (PreferenceManager.getDefaultSharedPreferences(mContext).getBoolean("pref_interface_checked_last", false))
            orderString = DBCommonDef.CHECKED_COLUMN_NAME + " ASC, " + orderString;

        //clear items
        note.getItems().clear();
        LongSparseArray<BasicNoteItemParams> paramsList = mBasicNoteItemParamsDAO.getByNote(note);
        long priceNoteParamTypeId = getPriceNoteParamTypeId();

        long totalPrice = 0L;
        int itemCount = 0;
        int checkedItemCount = 0;

        //get items
        Cursor c = null;
        try {
            c = mDB.query(
                    NoteItemsTableDef.TABLE_NAME, NoteItemsTableDef.TABLE_COLS,
                    DBCommonDef.NOTE_ID_COLUMN_NAME + " = ?", new String[] {String.valueOf(note.getId())}, null, null,
                    orderString
            );


            for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                BasicNoteItemA newItem = noteItemFromCursor(c, mDTF);

                BasicNoteItemParams params = paramsList.get(newItem.getId());
                newItem.setNoteItemParams(params);
                if (params != null) {
                    BasicParamValueA priceParamValue = params.get(priceNoteParamTypeId);
                    if (priceParamValue != null) {
                        totalPrice += priceParamValue.vInt;
                    }
                }

                if (newItem.isChecked())
                    checkedItemCount ++;
                itemCount ++;
                note.getItems().add(newItem);
            }

            note.setItemCount(itemCount);
            note.setCheckedItemCount(checkedItemCount);
            note.setTotalPrice(totalPrice);

        } finally {
            if ((c !=null) && !c.isClosed())
                c.close();
        }
    }
    */

    public void queryNoteDataValues(BasicNoteA note) {
        //clear values
        note.getValues().clear();

        if (!note.isEncrypted()) {
            //get values
            Cursor c = null;
            try {
                c = mDB.query(
                        NoteValuesTableDef.TABLE_NAME, new String[]{NoteValuesTableDef.VALUE_COLUMN_NAME},
                        DBCommonDef.NOTE_ID_COLUMN_NAME + " = ?", new String[]{String.valueOf(note.getId())}, null, null, null
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
                    NoteValuesTableDef.TABLE_NAME, new String[]{DBCommonDef.ID_COLUMN_NAME, NoteValuesTableDef.VALUE_COLUMN_NAME},
                    DBCommonDef.NOTE_ID_COLUMN_NAME + " = ?", new String[]{String.valueOf(note.getId())}, null, null, NoteValuesTableDef.VALUE_COLUMN_NAME
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
        List<BasicNoteA> newNotes = mBasicNoteDAO.getTotals();
        notes.clear();
        notes.addAll(newNotes);
    }

    public void updateNoteDataChecked(BasicNoteDataA noteData, boolean checked) {
        for (BasicNoteItemA item : noteData.getNoteList().get(0).getItems()) {
            ContentValues cv = new ContentValues();

            cv.put(NoteItemsTableDef.LAST_MODIFIED_COLUMN_NAME, System.currentTimeMillis());
            cv.put(NoteItemsTableDef.CHECKED_COLUMN_NAME, BooleanUtils.toInt(checked));

            mDB.update(NoteItemsTableDef.TABLE_NAME, cv, DBCommonDef.ID_COLUMN_NAME + " = ?" , new String[] {String.valueOf(item.getId())});
        }
    }

    public long insertNote(BasicNoteA note) {
        ContentValues cv = new ContentValues();

        cv.put(NotesTableDef.LAST_MODIFIED_COLUMN_NAME, System.currentTimeMillis());
        cv.put(NotesTableDef.ORDER_COLUMN_NAME, DBBasicNoteHelper.getInstance(mContext).getMaxOrderId(NotesTableDef.TABLE_NAME, 0) + 1);
        cv.put(NotesTableDef.GROUP_ID_COLUMN_NAME, note.getNoteGroupId());
        cv.put(NotesTableDef.NOTE_TYPE_COLUMN_NAME, note.getNoteType());
        cv.put(NotesTableDef.TITLE_COLUMN_NAME, note.getTitle());
        cv.put(NotesTableDef.IS_ENCRYPTED_COLUMN_NAME, BooleanUtils.toInt(note.isEncrypted()));
        cv.put(NotesTableDef.ENCRYPTED_STRING_COLUMN_NAME, note.getEncryptedString());

        return mDB.insert(NotesTableDef.TABLE_NAME, null, cv);
    }

    public long deleteNote(BasicNoteA note) {
        String[] noteIdArgs = new String[] {String.valueOf(note.getId())};

        //history
        mDB.delete(NoteItemsHistoryTableDef.TABLE_NAME, DBCommonDef.NOTE_ID_SELECTION_STRING, noteIdArgs);
        //values
        mDB.delete(NoteValuesTableDef.TABLE_NAME, DBCommonDef.NOTE_ID_SELECTION_STRING, noteIdArgs);
        //items
        mBasicNoteItemDAO.fillNoteDataItemsWithSummary(note);
        for (BasicNoteItemA noteItem : note.getItems()) {
            deleteNoteItem(noteItem);
        }
        //h
        mDBHManager.basicHNoteCOItemDAO.deleteEvent(note);

        //note
        return mDB.delete(NotesTableDef.TABLE_NAME, DBCommonDef.ID_COLUMN_NAME + "=" + note.getId(), null);
    }

    public long updateNote(BasicNoteA note) {
        ContentValues cv = new ContentValues();
        cv.put(NotesTableDef.LAST_MODIFIED_COLUMN_NAME, System.currentTimeMillis());
        cv.put(NotesTableDef.NOTE_TYPE_COLUMN_NAME, note.getNoteType());
        cv.put(NotesTableDef.TITLE_COLUMN_NAME, note.getTitle());
        cv.put(NotesTableDef.IS_ENCRYPTED_COLUMN_NAME, BooleanUtils.toInt(note.isEncrypted()));
        cv.put(NotesTableDef.ENCRYPTED_STRING_COLUMN_NAME, note.getEncryptedString());

        return mDB.update(NotesTableDef.TABLE_NAME, cv, DBCommonDef.ID_COLUMN_NAME + "=" + note.getId(), null);
    }

    public long updateNoteValueValue(BasicNoteValueA item) {
        ContentValues cv = new ContentValues();

        cv.put(NoteValuesTableDef.VALUE_COLUMN_NAME, item.getValue());

        return mDB.update(NoteValuesTableDef.TABLE_NAME, cv, DBCommonDef.ID_COLUMN_NAME + " = ?", new String[] {String.valueOf(item.getId())});
    }

    public long insertNoteItem(BasicNoteA note, BasicNoteItemA item) {
        ContentValues cv = new ContentValues();

        cv.put(NoteItemsTableDef.LAST_MODIFIED_COLUMN_NAME, System.currentTimeMillis());
        cv.put(NoteItemsTableDef.ORDER_COLUMN_NAME, DBBasicNoteHelper.getInstance(mContext).getMaxOrderId(NoteItemsTableDef.TABLE_NAME, note.getId()) + 1);
        cv.put(NoteItemsTableDef.NOTE_ID_COLUMN_NAME, note.getId());
        cv.put(NoteItemsTableDef.NAME_COLUMN_NAME, item.getName());
        cv.put(NoteItemsTableDef.VALUE_COLUMN_NAME, item.getValue());
        cv.put(NoteItemsTableDef.CHECKED_COLUMN_NAME, item.isChecked());
        cv.put(NoteItemsTableDef.PRIORITY_COLUMN_NAME, item.getPriority());

        long newRowId = mDB.insert(NoteItemsTableDef.TABLE_NAME, null, cv);

        if (newRowId > 0) {
            insertNoteItemParams(newRowId, item.getNoteItemParams());

            item.setId(newRowId);
            mDBHManager.basicHNoteItemDAO.saveEvent(item);
        }
/*
        if ((newRowId > 0) && (item.getParamPrice() > 0)) {
            insertNoteItemParam(newRowId, getPriceNoteParamTypeId(), item.getNoteItemParams().get(getPriceNoteParamTypeId()));
        }

        if (newRowId > 0) {
            item.setId(newRowId);
            mDBHManager.saveNoteItemsEvent(item);
        }
*/

        return newRowId;
    }

    public void insertNoteItemParams(long noteItemId, BasicNoteItemParams noteItemParams) {
        for (int i = 0; i < noteItemParams.size(); i++) {
            long noteItemParamTypeId = noteItemParams.keyAt(i);
            insertNoteItemParam(noteItemId, noteItemParamTypeId, noteItemParams.get(noteItemParamTypeId));
        }
    }

    public long insertNoteItemParam(long noteItemId, long paramTypeId, BasicParamValueA paramValue) {
        if ((paramValue.vInt == 0) && ((paramValue.vText == null) || (paramValue.vText.isEmpty())))
            return 0;

        ContentValues cv = new ContentValues();

        cv.put(DBCommonDef.NOTE_ITEM_ID_COLUMN_NAME, noteItemId);
        cv.put(NoteItemParamsTableDef.NOTE_ITEM_PARAM_TYPE_ID_COLUMN_NAME, paramTypeId);
        if (paramValue.vInt != 0)
            cv.put(NoteItemParamsTableDef.V_INT_COLUMN_NAME, paramValue.vInt);
        if ((paramValue.vText != null) && (!paramValue.vText.isEmpty()))
            cv.put(NoteItemParamsTableDef.V_TEXT_COLUMN_NAME, paramValue.vText);

        return mDB.insert(NoteItemParamsTableDef.TABLE_NAME, null, cv);
    }

    public long deleteNoteItemParam(BasicNoteItemA item) {
        return mDB.delete(NoteItemParamsTableDef.TABLE_NAME, DBCommonDef.NOTE_ITEM_ID_COLUMN_NAME + "=?", new String[] {String.valueOf(item.getId())});
    }

    public long deleteNoteItem(BasicNoteItemA item) {
        deleteNoteItemParam(item);
        //mDBHManager.deleteHNoteItem(item);
        mDBHManager.basicHNoteItemDAO.deleteEvent(item);
        return deleteEntityNote(NoteItemsTableDef.TABLE_NAME, item);
    }

    public long deleteEntityNote(String tableName, BasicEntityNoteA item) {
        return deleteById(tableName, item.getId());
    }

    public long checkNoteItem(BasicNoteItemA item) {
        ContentValues cv = new ContentValues();

        cv.put(NoteItemsTableDef.LAST_MODIFIED_COLUMN_NAME, System.currentTimeMillis());
        cv.put(NoteItemsTableDef.CHECKED_COLUMN_NAME, !item.isChecked());

        return mDB.update(NoteItemsTableDef.TABLE_NAME, cv, DBCommonDef.ID_COLUMN_NAME + " = ?" , new String[] {String.valueOf(item.getId())});
    }

    public long updateNoteItemNameValue(BasicNoteItemA item) {
        ContentValues cv = new ContentValues();

        cv.put(NoteItemsTableDef.LAST_MODIFIED_COLUMN_NAME, System.currentTimeMillis());
        if (item.getName() != null)
            cv.put(NoteItemsTableDef.NAME_COLUMN_NAME, item.getName());
        cv.put(NoteItemsTableDef.VALUE_COLUMN_NAME, item.getValue());

        deleteNoteItemParam(item);
        insertNoteItemParams(item.getId(), item.getNoteItemParams());

        long result = mDB.update(NoteItemsTableDef.TABLE_NAME, cv, DBCommonDef.ID_COLUMN_NAME + " = ?" , new String[] {String.valueOf(item.getId())});

        if (result > 0) {
            mDBHManager.basicHNoteItemDAO.saveEvent(item);
        }
        return result;
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

                    //insert h
                    mDBHManager.basicHNoteCOItemDAO.saveEvent(note, item);
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

        cv.put(DBCommonDef.NOTE_ID_COLUMN_NAME, otherNoteId);
        cv.put(DBCommonDef.ORDER_COLUMN_NAME, otherOrderId);

        return mDB.update(NoteItemsTableDef.TABLE_NAME, cv, DBCommonDef.ID_COLUMN_NAME + " = ?" , new String[] {String.valueOf(note.getId())});
    }

    public long insertNoteValue(BasicEntityNoteA note, BasicNoteValueA value) {
        ContentValues cv = new ContentValues();

        cv.put(NoteValuesTableDef.NOTE_ID_COLUMN_NAME, note.getId());
        cv.put(NoteValuesTableDef.VALUE_COLUMN_NAME, value.getValue());

        return mDB.insert(NoteValuesTableDef.TABLE_NAME, null, cv);
    }

    public long insertNoteHistory(BasicNoteA note, String value) {
        ContentValues cv = new ContentValues();

        cv.put(NoteItemsHistoryTableDef.LAST_MODIFIED_COLUMN_NAME, System.currentTimeMillis());
        cv.put(NoteItemsHistoryTableDef.NOTE_ID_COLUMN_NAME, note.getId());
        cv.put(NoteItemsHistoryTableDef.VALUE_COLUMN_NAME, value);

        return mDB.insert(NoteItemsHistoryTableDef.TABLE_NAME, null, cv);
    }

    public BasicNoteItemA getNoteItem(long id) {
        Cursor c = null;
        try {
            c = mDB.query(
                    NoteItemsTableDef.TABLE_NAME, NoteItemsTableDef.TABLE_COLS,
                    DBCommonDef.ID_COLUMN_NAME + "=?", new String[]{String.valueOf(id)}, null, null, null
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

    public Map<String, Long> getNoteParamTypesMap() {
        final Map<String, Long> result = new HashMap<>();

        readCursor(new CursorReaderHandler() {
            @Override
            public Cursor createCursor() {
                return mDB.query(
                        NoteItemParamTypesTableDef.TABLE_NAME, NoteItemParamTypesTableDef.TABLE_COLS,
                        null, null, null, null, null
                );
            }

            @Override
            public void readFromCursor(Cursor c) {
                result.put(c.getString(1), c.getLong(0));
            }
        });

        return result;
    }

    private long getPriceNoteParamTypeId() {
        return DBBasicNoteHelper.getInstance(mContext).getDBDictionaryCache().getPriceNoteParamTypeId();
    }
}
