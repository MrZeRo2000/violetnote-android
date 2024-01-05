package com.romanpulov.violetnote.db.manager;

import android.content.Context;
import android.util.Log;

import com.romanpulov.violetnote.db.dao.BasicCommonNoteDAO;
import com.romanpulov.violetnote.db.dao.BasicNoteDAO;
import com.romanpulov.violetnote.db.dao.BasicNoteGroupDAO;
import com.romanpulov.violetnote.db.dao.BasicNoteHistoryDAO;
import com.romanpulov.violetnote.db.dao.BasicNoteItemDAO;
import com.romanpulov.violetnote.db.dao.BasicNoteItemParamTypeDAO;
import com.romanpulov.violetnote.db.dao.BasicNoteItemParamsDAO;
import com.romanpulov.violetnote.db.dao.BasicNoteValueDAO;

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
    public final BasicNoteGroupDAO mBasicNoteGroupDAO;

    public DBNoteManager(Context context) {
        mBasicNoteDAO = new BasicNoteDAO(context);
        mBasicNoteItemDAO = new BasicNoteItemDAO(context);
        mBasicNoteItemParamsDAO = new BasicNoteItemParamsDAO(context);
        mBasicNoteValueDAO = new BasicNoteValueDAO(context);
        mBasicNoteHistoryDAO = new BasicNoteHistoryDAO(context);
        mBasicNoteItemParamTypeDAO = new BasicNoteItemParamTypeDAO(context);
        mBasicCommonNoteDAO = new BasicCommonNoteDAO(context);
        mBasicNoteGroupDAO = new BasicNoteGroupDAO(context);
    }
}
