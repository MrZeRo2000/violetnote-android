package com.romanpulov.violetnote.db.dao;

import android.content.Context;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.LongSparseArray;

import com.romanpulov.violetnote.db.DateTimeFormatter;
import com.romanpulov.violetnote.db.tabledef.DBCommonDef;
import com.romanpulov.violetnote.db.tabledef.NoteItemsTableDef;
import com.romanpulov.violetnote.model.BasicNoteA;
import com.romanpulov.violetnote.model.BasicNoteItemA;
import com.romanpulov.violetnote.model.BasicNoteItemParams;
import com.romanpulov.violetnote.model.BooleanUtils;
import com.romanpulov.violetnote.model.vo.BasicParamValueA;

import java.util.Date;
import java.util.List;

/**
 * BasicNoteItem DAO
 */
public final class BasicNoteItemDAO extends AbstractDAO<BasicNoteItemA> {

    public BasicNoteItemDAO(Context context){
        super(context);
    }

    private BasicNoteItemParamsDAO mBasicNoteItemParamsDAO;
    private BasicNoteItemParamsDAO getBasicNoteItemParamsDAO() {
        return mBasicNoteItemParamsDAO == null ? (mBasicNoteItemParamsDAO = new BasicNoteItemParamsDAO(mContext)) : mBasicNoteItemParamsDAO;
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

    public void fillNoteDataItems(final BasicNoteA note) {
        //clear items
        note.getItems().clear();

        String orderString = DBCommonDef.PRIORITY_COLUMN_NAME + " DESC, " + DBCommonDef.ORDER_COLUMN_NAME;
        if (PreferenceManager.getDefaultSharedPreferences(mContext).getBoolean("pref_interface_checked_last", false))
            orderString = DBCommonDef.CHECKED_COLUMN_NAME + " ASC, " + orderString;

        final String queryOrderString = orderString;
        final LongSparseArray<BasicNoteItemParams> paramsList = getBasicNoteItemParamsDAO().getByNote(note);
        final long priceNoteParamTypeId = mDBHelper.getDBDictionaryCache().getPriceNoteParamTypeId();

        class CalcSummary {
            long totalPrice = 0L;
            int itemCount = 0;
            int checkedItemCount = 0;
        }

        final CalcSummary calcSummary = new CalcSummary();

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
                    BasicParamValueA priceParamValue = params.get(priceNoteParamTypeId);
                    if (priceParamValue != null) {
                        calcSummary.totalPrice += priceParamValue.vInt;
                    }
                }

                if (newItem.isChecked()) {
                    calcSummary.checkedItemCount++;
                }
                calcSummary.itemCount ++;
                note.getItems().add(newItem);

            }
        });

        note.setItemCount(calcSummary.itemCount);
        note.setCheckedItemCount(calcSummary.checkedItemCount);
        note.setTotalPrice(calcSummary.totalPrice);

    }
}
