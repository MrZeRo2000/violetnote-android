package com.romanpulov.violetnote.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import androidx.annotation.NonNull;
import android.util.LongSparseArray;

import com.romanpulov.violetnote.db.DBRawQueryRepository;
import com.romanpulov.violetnote.db.tabledef.DBCommonDef;
import com.romanpulov.violetnote.db.tabledef.NoteItemParamsTableDef;
import com.romanpulov.violetnote.model.BasicNoteA;
import com.romanpulov.violetnote.model.BasicNoteItemA;
import com.romanpulov.violetnote.model.BasicNoteItemParams;
import com.romanpulov.violetnote.model.vo.BasicParamValueA;

/**
 * BasicNoteItemParams DAO
 */
public final class BasicNoteItemParamsDAO extends AbstractDAO<BasicNoteItemParams> {

    public BasicNoteItemParamsDAO(Context context) {
        super(context);
    }

    public LongSparseArray<BasicNoteItemParams> getByNote(final BasicNoteA note) {
        final LongSparseArray<BasicNoteItemParams> result = new LongSparseArray<>();

        readCursor(new CursorReaderHandler() {
            @Override
            public Cursor createCursor() {
                return mDB.rawQuery(DBRawQueryRepository.NOTE_ITEMS_PARAMS, new String[]{String.valueOf(note.getId())});
            }

            @Override
            public void readFromCursor(Cursor c) {
                long noteId = c.getLong(0);
                long noteItemParamTypeId = c.getLong(1);
                BasicParamValueA value = BasicParamValueA.newInstance(
                        c.getLong(2), c.getString(3)
                );

                BasicNoteItemParams params = result.get(noteId);
                if (params == null) {
                    result.append(noteId, BasicNoteItemParams.fromValue(noteItemParamTypeId, value));
                } else {
                    params.append(noteItemParamTypeId, value);
                }
            }
        });

        return result;
    }

    public long insertWithId(long noteItemId, @NonNull BasicNoteItemParams object) {
        long result = 0;

        for (int i = 0; i < object.size(); i++) {
            long noteItemParamTypeId = object.keyAt(i);
            result += insertNoteItemParam(noteItemId, noteItemParamTypeId, object.get(noteItemParamTypeId));
        }

        return result;
    }

    private long insertNoteItemParam(long noteItemId, long paramTypeId, @NonNull BasicParamValueA paramValue) {
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

    public long deleteByNoteItem(@NonNull BasicNoteItemA noteItem) {
        return internalDeleteById(NoteItemParamsTableDef.TABLE_NAME, DBCommonDef.NOTE_ITEM_ID_COLUMN_NAME, noteItem.getId());
    }
}
