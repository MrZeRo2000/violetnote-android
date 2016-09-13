package com.romanpulov.violetnote.view.action;

import android.content.Context;

import com.romanpulov.violetnote.db.DBNoteManager;

import java.util.List;

/**
 * Created by romanpulov on 13.09.2016.
 */
public class BasicNoteDataActionExecutor {
    private final Context mContext;
    private List<BasicNoteDataAction> mActionList;

    public BasicNoteDataActionExecutor(Context context) {
        mContext = context;
    }

    public void addBasicNoteDataAction(BasicNoteDataAction action) {
        mActionList.add(action);
    }

    public boolean execute() {
        DBNoteManager noteManager = new DBNoteManager(mContext);
        for (BasicNoteDataAction action : mActionList) {
            if (!action.execute(noteManager))
                return false;
        }
        return true;
    }
}
