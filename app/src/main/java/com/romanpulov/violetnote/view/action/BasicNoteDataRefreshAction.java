package com.romanpulov.violetnote.view.action;

import com.romanpulov.violetnote.db.DBNoteManager;
import com.romanpulov.violetnote.model.BasicNoteDataA;
import com.romanpulov.violetnote.model.BasicNoteItemA;
import com.romanpulov.violetnote.model.PassNoteItemCryptService;

/**
 * Created by romanpulov on 13.09.2016.
 */
public class BasicNoteDataRefreshAction extends BasicNoteDataAction {

    public BasicNoteDataRefreshAction(BasicNoteDataA basicNoteData) {
        super(basicNoteData);
    }

    @Override
    public boolean execute(DBNoteManager noteManager) {
        //retrieve data
        noteManager.queryNoteDataItems(mBasicNoteData.getNote());

        //decrypt
        if (mBasicNoteData.getNote().isEncrypted()) {
            for (BasicNoteItemA item : mBasicNoteData.getNote().getItems()) {
                if (!PassNoteItemCryptService.decryptBasicNoteItem(item, mBasicNoteData.getPassword()))
                    return false;
            }
        }
        return true;
    }
}
