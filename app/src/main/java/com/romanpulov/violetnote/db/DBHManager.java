package com.romanpulov.violetnote.db;

import android.content.Context;

import com.romanpulov.violetnote.db.dao.BasicHEventDAO;
import com.romanpulov.violetnote.db.dao.BasicHEventTypeDAO;
import com.romanpulov.violetnote.db.dao.BasicHNoteCOItemDAO;
import com.romanpulov.violetnote.db.dao.BasicHNoteCOItemParamDAO;
import com.romanpulov.violetnote.db.dao.BasicHNoteItemDAO;

public final class DBHManager extends AbstractDBManager {
    public final BasicHEventTypeDAO basicHEventTypeDAO;
    public final BasicHEventDAO basicHEventDAO;
    public final BasicHNoteItemDAO basicHNoteItemDAO;
    public final BasicHNoteCOItemDAO basicHNoteCOItemDAO;
    public final BasicHNoteCOItemParamDAO basicHNoteCOItemParamDAO;

    public DBHManager(Context context) {
        super(context);
        basicHEventTypeDAO = new BasicHEventTypeDAO(context);
        basicHEventDAO = new BasicHEventDAO(context);
        basicHNoteItemDAO = new BasicHNoteItemDAO(context);
        basicHNoteCOItemDAO = new BasicHNoteCOItemDAO(context);
        basicHNoteCOItemParamDAO = new BasicHNoteCOItemParamDAO(context);
    }
}
