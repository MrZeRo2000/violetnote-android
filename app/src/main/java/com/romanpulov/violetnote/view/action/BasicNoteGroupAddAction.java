package com.romanpulov.violetnote.view.action;

import androidx.annotation.NonNull;

import com.romanpulov.violetnote.db.manager.DBNoteManager;
import com.romanpulov.violetnote.model.BasicNoteGroupA;

import java.util.List;

public class BasicNoteGroupAddAction extends BasicAction<List<BasicNoteGroupA>> {

    public BasicNoteGroupAddAction(@NonNull List<BasicNoteGroupA> data){
        super(data);
    }

    @Override
    public boolean execute(DBNoteManager noteManager) {
        return mData.size() == 1 && noteManager.mBasicNoteGroupDAO.insert(mData.get(0)) != -1;
    }
}
