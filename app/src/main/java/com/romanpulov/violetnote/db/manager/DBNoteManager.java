package com.romanpulov.violetnote.db.manager;

import android.content.Context;

import com.romanpulov.violetnote.db.dao.BasicCommonNoteDAO;
import com.romanpulov.violetnote.db.dao.BasicNoteDAO;
import com.romanpulov.violetnote.db.dao.BasicNoteGroupDAO;
import com.romanpulov.violetnote.db.dao.BasicNoteHistoryDAO;
import com.romanpulov.violetnote.db.dao.BasicNoteItemDAO;
import com.romanpulov.violetnote.db.dao.BasicNoteValueDAO;

/**
 * BasicNoteA database operations
 * Created by romanpulov on 17.08.2016.
 */
public class DBNoteManager extends AbstractDBManager {
    public final BasicNoteDAO mBasicNoteDAO;
    public final BasicNoteItemDAO mBasicNoteItemDAO;
    public final BasicNoteValueDAO mBasicNoteValueDAO;
    public final BasicNoteHistoryDAO mBasicNoteHistoryDAO;
    public final BasicCommonNoteDAO mBasicCommonNoteDAO;
    public final BasicNoteGroupDAO mBasicNoteGroupDAO;

    public DBNoteManager(Context context) {
        mBasicNoteDAO = new BasicNoteDAO(context);
        mBasicNoteItemDAO = new BasicNoteItemDAO(context);
        mBasicNoteValueDAO = new BasicNoteValueDAO(context);
        mBasicNoteHistoryDAO = new BasicNoteHistoryDAO(context);
        mBasicCommonNoteDAO = new BasicCommonNoteDAO(context);
        mBasicNoteGroupDAO = new BasicNoteGroupDAO(context);
    }
}
