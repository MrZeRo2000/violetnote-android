package com.romanpulov.violetnote.view.action;

import com.romanpulov.violetnote.db.DBNoteManager;
import com.romanpulov.violetnote.model.BasicNoteDataA;
import com.romanpulov.violetnote.model.BasicNoteItemA;
import com.romanpulov.violetnote.model.PassNoteItemJSONCryptService;

/**
 * Created by romanpulov on 13.09.2016.
 */
public class BasicNoteDataItemAddAction extends BasicNoteDataItemAction {

    public BasicNoteDataItemAddAction(BasicNoteDataA basicNoteData, BasicNoteItemA item) {
        super(basicNoteData, item);
    }

    @Override
    public boolean execute(DBNoteManager noteManager) {
        //encrypt
        if (mBasicNoteData.getNote().isEncrypted()) {
            if (!PassNoteItemJSONCryptService.encryptBasicNoteItem(mItem, mBasicNoteData.getPassword()))
                return false;
        }

        //insert
        return noteManager.insertNoteItem(mBasicNoteData.getNote(), mItem) != -1;
    }
}
