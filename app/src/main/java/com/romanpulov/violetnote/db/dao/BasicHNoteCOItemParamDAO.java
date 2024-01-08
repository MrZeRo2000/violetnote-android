package com.romanpulov.violetnote.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import androidx.annotation.NonNull;

import com.romanpulov.violetnote.db.tabledef.HNoteCOItemParamsTableDef;
import com.romanpulov.violetnote.model.BasicHNoteCOItemParam;

import java.util.ArrayList;
import java.util.List;

/**
 * BasicHNoteCOItemParamDAO DAO
 */
public final class BasicHNoteCOItemParamDAO extends AbstractDAO<BasicHNoteCOItemParam> {

    public BasicHNoteCOItemParamDAO(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public List<BasicHNoteCOItemParam> getAll() {
        final List<BasicHNoteCOItemParam> result = new ArrayList<>();

        readCursor(new CursorReaderHandler() {
            @Override
            public Cursor createCursor() {
                return mDB.query(
                        HNoteCOItemParamsTableDef.TABLE_NAME,
                        HNoteCOItemParamsTableDef.TABLE_COLS,
                        null,
                        null,
                        null,
                        null,
                        null
                );

            }

            @Override
            public void readFromCursor(Cursor c) {
                result.add(BasicHNoteCOItemParam.newInstance(
                        c.getLong(0),
                        c.getLong(1),
                        c.getLong(2),
                        c.getLong(3),
                        c.getString(4)
                ));
            }
        });

        return result;
    }

    @Override
    public long insert(@NonNull BasicHNoteCOItemParam object) {
        ContentValues cv = new ContentValues();

        cv.put(HNoteCOItemParamsTableDef.H_NOTE_CO_ITEM_ID_COLUMN_NAME, object.getHNoteCOItemId());
        cv.put(HNoteCOItemParamsTableDef.NOTE_ITEM_PARAM_TYPE_ID_COLUMN_NAME, object.getNoteItemParamValue().noteItemParamTypeId);
        cv.put(HNoteCOItemParamsTableDef.V_INT_COLUMN_NAME, object.getNoteItemParamValue().paramValue.vInt);
        cv.put(HNoteCOItemParamsTableDef.V_TEXT_COLUMN_NAME, object.getNoteItemParamValue().paramValue.vText);

        return mDB.insert(HNoteCOItemParamsTableDef.TABLE_NAME, null, cv);
    }

    @Override
    public long delete(@NonNull BasicHNoteCOItemParam object) {
        return internalDeleteById(HNoteCOItemParamsTableDef.TABLE_NAME, object.getId());
    }

    public void deleteByNoteCOItemId(long id) {
        internalDeleteById(HNoteCOItemParamsTableDef.TABLE_NAME, HNoteCOItemParamsTableDef.H_NOTE_CO_ITEM_ID_COLUMN_NAME, id);
    }
}
