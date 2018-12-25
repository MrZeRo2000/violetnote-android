package com.romanpulov.violetnote.db.dao;

import android.content.Context;
import android.database.Cursor;
import android.util.LongSparseArray;

import com.romanpulov.violetnote.db.DBRawQueryRepository;
import com.romanpulov.violetnote.model.BasicNoteA;
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

}
