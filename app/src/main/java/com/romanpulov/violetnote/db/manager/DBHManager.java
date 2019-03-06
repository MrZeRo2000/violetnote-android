package com.romanpulov.violetnote.db.manager;

import android.content.Context;

import com.romanpulov.violetnote.db.dao.BasicHEventDAO;
import com.romanpulov.violetnote.db.dao.BasicHEventTypeDAO;
import com.romanpulov.violetnote.db.dao.BasicHNoteCOItemDAO;
import com.romanpulov.violetnote.db.dao.BasicHNoteCOItemParamDAO;
import com.romanpulov.violetnote.db.dao.BasicHNoteItemDAO;

public final class DBHManager extends AbstractDBManager {
    public final BasicHEventTypeDAO mBasicHEventTypeDAO;
    public final BasicHEventDAO mBasicHEventDAO;
    public final BasicHNoteItemDAO mBasicHNoteItemDAO;
    public final BasicHNoteCOItemDAO mBasicHNoteCOItemDAO;
    public final BasicHNoteCOItemParamDAO mBasicHNoteCOItemParamDAO;

    public DBHManager(Context context) {
        super(context);
        mBasicHEventTypeDAO = new BasicHEventTypeDAO(context);
        mBasicHEventDAO = new BasicHEventDAO(context);
        mBasicHNoteItemDAO = new BasicHNoteItemDAO(context);
        mBasicHNoteCOItemDAO = new BasicHNoteCOItemDAO(context);
        mBasicHNoteCOItemParamDAO = new BasicHNoteCOItemParamDAO(context);
    }
}
