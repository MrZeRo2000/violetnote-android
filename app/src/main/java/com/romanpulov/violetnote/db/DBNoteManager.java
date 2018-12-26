package com.romanpulov.violetnote.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.romanpulov.violetnote.db.dao.BasicCommonNoteDAO;
import com.romanpulov.violetnote.db.dao.BasicNoteDAO;
import com.romanpulov.violetnote.db.dao.BasicNoteHistoryDAO;
import com.romanpulov.violetnote.db.dao.BasicNoteItemDAO;
import com.romanpulov.violetnote.db.dao.BasicNoteItemParamTypeDAO;
import com.romanpulov.violetnote.db.dao.BasicNoteItemParamsDAO;
import com.romanpulov.violetnote.db.dao.BasicNoteValueDAO;
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
public class DBNoteManager extends AbstractDBManager {
    private static final String TAG = "DBNoteManager";
    private static void log(String message) {
        Log.d(TAG, message);
    }

    public final BasicNoteDAO mBasicNoteDAO;
    public final BasicNoteItemDAO mBasicNoteItemDAO;
    public final BasicNoteItemParamsDAO mBasicNoteItemParamsDAO;
    public final BasicNoteValueDAO mBasicNoteValueDAO;
    public final BasicNoteHistoryDAO mBasicNoteHistoryDAO;
    public final BasicNoteItemParamTypeDAO mBasicNoteItemParamTypeDAO;
    public final BasicCommonNoteDAO mBasicCommonNoteDAO;

    public DBNoteManager(Context context) {
        super(context);
        mBasicNoteDAO = new BasicNoteDAO(context);
        mBasicNoteItemDAO = new BasicNoteItemDAO(context);
        mBasicNoteItemParamsDAO = new BasicNoteItemParamsDAO(context);
        mBasicNoteValueDAO = new BasicNoteValueDAO(context);
        mBasicNoteHistoryDAO = new BasicNoteHistoryDAO(context);
        mBasicNoteItemParamTypeDAO = new BasicNoteItemParamTypeDAO(context);
        mBasicCommonNoteDAO = new BasicCommonNoteDAO(context);
    }
}
