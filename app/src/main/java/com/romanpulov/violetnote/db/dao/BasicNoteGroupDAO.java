package com.romanpulov.violetnote.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.romanpulov.violetnote.db.DBRawQueryRepository;
import com.romanpulov.violetnote.db.tabledef.DBCommonDef;
import com.romanpulov.violetnote.db.tabledef.NoteGroupsTableDef;
import com.romanpulov.violetnote.model.BasicNoteGroupA;

import java.util.ArrayList;
import java.util.List;

public class BasicNoteGroupDAO extends AbstractDAO<BasicNoteGroupA> {

    private static BasicNoteGroupA fromCursor(@NonNull Cursor c) {
        return BasicNoteGroupA.newInstance(
                c.getLong(0),
                c.getLong(1),
                c.getString(2),
                c.getLong(3),
                c.getLong(4),
                c.getString(5)
        );
    }

    private static BasicNoteGroupA fromCursorWithTotals(@NonNull Cursor c) {
        return BasicNoteGroupA.newInstanceWithTotals(
                c.getLong(0),
                c.getLong(1),
                c.getString(2),
                c.getLong(3),
                c.getLong(4),
                c.getString(5),
                c.getLong(6),
                c.getLong(7),
                c.getLong(8)
        );
    }

    public BasicNoteGroupDAO(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public List<BasicNoteGroupA> getAll() {
        final List<BasicNoteGroupA> result = new ArrayList<>();

        readCursor(new CursorReaderHandler() {
            @Override
            public Cursor createCursor() {
                return mDB.query(
                        NoteGroupsTableDef.TABLE_NAME,
                        NoteGroupsTableDef.TABLE_COLS,
                        null,
                        null,
                        null,
                        null,
                        NoteGroupsTableDef.NOTE_GROUP_TYPE_COLUMN_NAME + ", " + DBCommonDef.ORDER_COLUMN_NAME
                );
            }

            @Override
            public void readFromCursor(Cursor c) {
                result.add(fromCursor(c));
            }
        });

        return result;
    }

    public List<BasicNoteGroupA> getAllWithTotals(boolean excludePasswordGroup) {
        final List<BasicNoteGroupA> result = new ArrayList<>();

        String predicate = excludePasswordGroup ? "g.note_group_type != 1" : "1 = 1";

        readCursor(new CursorReaderHandler() {
            @Override
            public Cursor createCursor() {
                return mDB.rawQuery(
                        DBRawQueryRepository.NOTE_GROUPS_WITH_TOTALS_WITH_CHECKED.replace(
                                DBRawQueryRepository.PREDICATE_PLACEHOLDER,
                                predicate
                        ),
                        null
                );
            }

            @Override
            public void readFromCursor(Cursor c) {
                result.add(fromCursorWithTotals(c));
            }
        });

        return result;
    }

    @NonNull
    public List<BasicNoteGroupA> getByGroupType(final long groupType) {
        final List<BasicNoteGroupA> result = new ArrayList<>();

        readCursor(new CursorReaderHandler() {
            @Override
            public Cursor createCursor() {
                return mDB.query(
                        NoteGroupsTableDef.TABLE_NAME,
                        NoteGroupsTableDef.TABLE_COLS,
                        NoteGroupsTableDef.NOTE_GROUP_TYPE_COLUMN_NAME + " = ?",
                        new String[]{String.valueOf(groupType)},
                        null,
                        null,
                        DBCommonDef.ORDER_COLUMN_NAME
                );
            }

            @Override
            public void readFromCursor(Cursor c) {
                result.add(fromCursor(c));
            }
        });

        return result;
    }

    @NonNull
    public List<BasicNoteGroupA> getRelatedNoteGroupList(@NonNull final BasicNoteGroupA noteGroup) {
        final List<BasicNoteGroupA> result = new ArrayList<>();

        readCursor(new CursorReaderHandler() {
            @Override
            public Cursor createCursor() {
                return mDB.query(
                        NoteGroupsTableDef.TABLE_NAME,
                        NoteGroupsTableDef.TABLE_COLS,
                        NoteGroupsTableDef.NOTE_GROUP_TYPE_COLUMN_NAME + " = ? AND " + DBCommonDef.ID_COLUMN_NAME + " != ?",
                        new String[]{String.valueOf(noteGroup.getGroupType()), String.valueOf(noteGroup.getId())},
                        null,
                        null,
                        DBCommonDef.ORDER_COLUMN_NAME
                );
            }

            @Override
            public void readFromCursor(Cursor c) {
                result.add(fromCursor(c));
            }
        });

        return result;
    }

    public void fillByGroupType(long groupType, @NonNull List<BasicNoteGroupA> noteGroupList) {
        final List<BasicNoteGroupA> newNoteGroupList = getByGroupType(groupType);
        noteGroupList.clear();
        noteGroupList.addAll(newNoteGroupList);
    }

    @Nullable
    public BasicNoteGroupA getById(final long id) {
        final BasicNoteGroupA[] result = new BasicNoteGroupA[1];

        readCursor(new CursorReaderHandler() {
            @Override
            public Cursor createCursor() {
                return mDB.query(
                        NoteGroupsTableDef.TABLE_NAME,
                        NoteGroupsTableDef.TABLE_COLS,
                        DBCommonDef.ID_COLUMN_NAME + "=?",
                        new String[] {String.valueOf(id)},
                        null,
                        null,
                        null
                );
            }

            @Override
            public void readFromCursor(Cursor c) {
                result[0] = fromCursor(c);
            }
        });

        return result[0];
    }

    @Override
    public long insert(@NonNull BasicNoteGroupA noteGroup) {
        ContentValues cv = new ContentValues();

        cv.put(NoteGroupsTableDef.NOTE_GROUP_TYPE_COLUMN_NAME, noteGroup.getGroupType());
        cv.put(NoteGroupsTableDef.NOTE_GROUP_NAME_COLUMN_NAME, noteGroup.getGroupName());
        cv.put(NoteGroupsTableDef.NOTE_GROUP_ICON_COLUMN_NAME, noteGroup.getGroupIcon());
        cv.put(DBCommonDef.ORDER_COLUMN_NAME, mDBHelper.getMaxOrderId (
                NoteGroupsTableDef.TABLE_NAME,
                NoteGroupsTableDef.NOTE_GROUP_TYPE_COLUMN_NAME + " = ?",
                new String[]{String.valueOf(noteGroup.getGroupType())}) + 1
        );
        cv.put(NoteGroupsTableDef.NOTE_GROUP_DISPLAY_OPTIONS, noteGroup.getDisplayOptions().toJSONString());

        return mDB.insert(NoteGroupsTableDef.TABLE_NAME, null, cv);
    }

    @Override
    public long delete(@NonNull BasicNoteGroupA noteGroup) {
        return internalDeleteById(NoteGroupsTableDef.TABLE_NAME, noteGroup.getId());
    }

    @Override
    public long update(@NonNull BasicNoteGroupA noteGroup) {
        ContentValues cv = new ContentValues();

        cv.put(NoteGroupsTableDef.NOTE_GROUP_TYPE_COLUMN_NAME, noteGroup.getGroupType());
        cv.put(NoteGroupsTableDef.NOTE_GROUP_NAME_COLUMN_NAME, noteGroup.getGroupName());
        cv.put(NoteGroupsTableDef.NOTE_GROUP_ICON_COLUMN_NAME, noteGroup.getGroupIcon());
        cv.put(DBCommonDef.ORDER_COLUMN_NAME, noteGroup.getOrderId());
        cv.put(NoteGroupsTableDef.NOTE_GROUP_DISPLAY_OPTIONS, noteGroup.getDisplayOptions().toJSONString());

        return mDB.update(NoteGroupsTableDef.TABLE_NAME, cv, DBCommonDef.ID_COLUMN_NAME + " = ?" , new String[]{String.valueOf(noteGroup.getId())});
    }
}
