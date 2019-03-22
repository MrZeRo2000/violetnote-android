package com.romanpulov.violetnote.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.LongSparseArray;

import com.romanpulov.violetnote.db.DateTimeFormatter;
import com.romanpulov.violetnote.db.tabledef.DBCommonDef;
import com.romanpulov.violetnote.db.tabledef.NoteItemsTableDef;
import com.romanpulov.violetnote.model.BasicNoteA;
import com.romanpulov.violetnote.model.BasicNoteItemA;
import com.romanpulov.violetnote.model.BasicNoteItemParams;
import com.romanpulov.violetnote.model.BasicNoteSummary;
import com.romanpulov.violetnote.model.BooleanUtils;
import com.romanpulov.violetnote.view.preference.PreferenceRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * BasicNoteItem DAO
 */
public final class BasicNoteItemDAO extends AbstractBasicNoteItemDAO<BasicNoteItemA> {

    public BasicNoteItemDAO(Context context){
        super(context);
    }

    @NonNull
    public static BasicNoteItemA noteItemFromCursor(@NonNull Cursor c, @NonNull DateTimeFormatter dtf) {
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

    public List<BasicNoteItemA> getByNote(final BasicNoteA note) {
        final List<BasicNoteItemA> result = new ArrayList<>();

        readCursor(new CursorReaderHandler() {
            @Override
            public Cursor createCursor() {
                return mDB.query(
                        NoteItemsTableDef.TABLE_NAME,
                        NoteItemsTableDef.TABLE_COLS,
                        DBCommonDef.NOTE_ID_COLUMN_NAME + " = ?",
                        new String[] {String.valueOf(note.getId())},
                        null,
                        null,
                        null
                );
            }

            @Override
            public void readFromCursor(Cursor c) {
                result.add(noteItemFromCursor(c, mDTF));
            }
        });

        return result;
    }

    public BasicNoteItemA getById(final long id) {
        final BasicNoteItemA[] result = new BasicNoteItemA[1];

        readCursor(new CursorReaderHandler() {
            @Override
            public Cursor createCursor() {
                return mDB.query(
                        NoteItemsTableDef.TABLE_NAME,
                        NoteItemsTableDef.TABLE_COLS,
                        DBCommonDef.ID_COLUMN_NAME + "=?",
                        new String[]{String.valueOf(id)},
                        null,
                        null,
                        null
                );
            }

            @Override
            public void readFromCursor(Cursor c) {
                result[0] = noteItemFromCursor(c, mDTF);
            }
        });

        return result[0];
    }

    public void fillNoteDataItemsWithSummary(final BasicNoteA note) {
        final List<BasicNoteItemA> items = new ArrayList<>();

        String orderString = DBCommonDef.PRIORITY_COLUMN_NAME + " DESC, " + DBCommonDef.ORDER_COLUMN_NAME;
        if (PreferenceManager.getDefaultSharedPreferences(mContext).getBoolean(PreferenceRepository.PREF_KEY_INTERFACE_CHECKED_LAST, false))
            orderString = DBCommonDef.CHECKED_COLUMN_NAME + " ASC, " + orderString;

        final String queryOrderString = orderString;
        final LongSparseArray<BasicNoteItemParams> paramsList = getBasicNoteItemParamsDAO().getByNote(note);

        final BasicNoteSummary calcSummary = BasicNoteSummary.createEmpty();

        readCursor(new CursorReaderHandler() {
            @Override
            public Cursor createCursor() {
                return mDB.query(
                        NoteItemsTableDef.TABLE_NAME,
                        NoteItemsTableDef.TABLE_COLS,
                        DBCommonDef.NOTE_ID_COLUMN_NAME + " = ?",
                        new String[] {String.valueOf(note.getId())},
                        null,
                        null,
                        queryOrderString
                );
            }

            @Override
            public void readFromCursor(Cursor c) {
                BasicNoteItemA newItem = noteItemFromCursor(c, mDTF);

                BasicNoteItemParams params = paramsList.get(newItem.getId());
                newItem.setNoteItemParams(params);
                if (params != null) {
                    calcSummary.appendParams(params.toLongList());
                }

                if (newItem.isChecked()) {
                    calcSummary.addCheckedItemCount();
                }
                calcSummary.addItemCount();
                items.add(newItem);
            }
        });

        note.setItems(items);
        note.setSummary(calcSummary);
    }

    public long updateChecked(BasicNoteItemA item, boolean checked) {
        ContentValues cv = new ContentValues();

        cv.put(NoteItemsTableDef.LAST_MODIFIED_COLUMN_NAME, System.currentTimeMillis());
        cv.put(NoteItemsTableDef.CHECKED_COLUMN_NAME, BooleanUtils.toInt(checked));

        return mDB.update(NoteItemsTableDef.TABLE_NAME, cv, DBCommonDef.ID_COLUMN_NAME + " = ?" , new String[] {String.valueOf(item.getId())});
    }

    public long updateCheckedList(List<BasicNoteItemA> items, boolean checked) {
        long result = 0;
        for (BasicNoteItemA item : items) {
            result += updateChecked(item, checked);
        }

        return result;
    }

    public long updateNameValue(BasicNoteItemA item) {
        ContentValues cv = new ContentValues();

        cv.put(NoteItemsTableDef.LAST_MODIFIED_COLUMN_NAME, System.currentTimeMillis());
        if (item.getName() != null)
            cv.put(NoteItemsTableDef.NAME_COLUMN_NAME, item.getName());
        cv.put(NoteItemsTableDef.VALUE_COLUMN_NAME, item.getValue());

        long result = mDB.update(NoteItemsTableDef.TABLE_NAME, cv, DBCommonDef.ID_COLUMN_NAME + " = ?" , new String[] {String.valueOf(item.getId())});

        getBasicNoteItemParamsDAO().deleteByNoteItem(item);
        getBasicNoteItemParamsDAO().insertWithId(item.getId(), item.getNoteItemParams());

        if (result > 0) {
            getBasicHNoteItemDAO().saveEvent(item);
        }
        return result;
    }

    /**
     * Updates NoteItem to another note
     * @param note NoteItem
     * @param otherNoteId noteId to move
     * @param otherOrderId orderId to set
     */
    private int updateToOtherNote(@NonNull BasicNoteItemA note, long otherNoteId, long otherOrderId) {
        ContentValues cv = new ContentValues();

        cv.put(DBCommonDef.NOTE_ID_COLUMN_NAME, otherNoteId);
        cv.put(DBCommonDef.ORDER_COLUMN_NAME, otherOrderId);

        return mDB.update(NoteItemsTableDef.TABLE_NAME, cv, DBCommonDef.ID_COLUMN_NAME + " = ?" , new String[] {String.valueOf(note.getId())});
    }

    public int moveToOtherNote(@NonNull BasicNoteItemA noteItem, @NonNull BasicNoteA otherNote) {
        long maxOrderId = mDBHelper.getNoteMaxOrderId(otherNote.getId(), noteItem.getPriority());
        return updateToOtherNote(noteItem, otherNote.getId(), maxOrderId + 1);
    }

    public long insertWithNote(@NonNull BasicNoteA note, @NonNull BasicNoteItemA item) {
        item.setNoteId(note.getId());
        return insert(item);
    }

    @Override
    public long insert(@NonNull BasicNoteItemA item) {
        ContentValues cv = new ContentValues();

        cv.put(NoteItemsTableDef.LAST_MODIFIED_COLUMN_NAME, System.currentTimeMillis());
        cv.put(NoteItemsTableDef.ORDER_COLUMN_NAME, mDBHelper.getMaxNoteOrderId(NoteItemsTableDef.TABLE_NAME, item.getNoteId()) + 1);
        cv.put(NoteItemsTableDef.NOTE_ID_COLUMN_NAME, item.getNoteId());
        cv.put(NoteItemsTableDef.NAME_COLUMN_NAME, item.getName());
        cv.put(NoteItemsTableDef.VALUE_COLUMN_NAME, item.getValue());
        cv.put(NoteItemsTableDef.CHECKED_COLUMN_NAME, item.isChecked());
        cv.put(NoteItemsTableDef.PRIORITY_COLUMN_NAME, item.getPriority());

        long newRowId = mDB.insert(NoteItemsTableDef.TABLE_NAME, null, cv);

        if (newRowId > 0) {
            getBasicNoteItemParamsDAO().insertWithId(newRowId, item.getNoteItemParams());

            item.setId(newRowId);
            getBasicHNoteItemDAO().saveEvent(item);
        }

        return newRowId;
    }

    @Override
    public long delete(@NonNull BasicNoteItemA object) {
        getBasicNoteItemParamsDAO().deleteByNoteItem(object);
        getBasicHNoteItemDAO().deleteEvent(object);
        return internalDeleteById(NoteItemsTableDef.TABLE_NAME, object.getId());
    }
}
