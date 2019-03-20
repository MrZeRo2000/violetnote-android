package com.romanpulov.violetnote.view.action;

import android.support.annotation.NonNull;

import com.romanpulov.violetnote.db.manager.DBNoteManager;
import com.romanpulov.violetnote.model.BasicNoteGroupA;

import java.util.List;

public class BasicNoteGroupEditAction extends BasicAction<List<BasicNoteGroupA>>  {

    public BasicNoteGroupEditAction(@NonNull List<BasicNoteGroupA> data){
        super(data);
    }

    @Override
    public boolean execute(DBNoteManager noteManager) {
        return mData.size() == 1 && noteManager.mBasicNoteGroupDAO.update(mData.get(0)) != -1;
    }
}
