package com.romanpulov.violetnote.view.action;

import com.romanpulov.violetnote.db.DBNoteManager;
import com.romanpulov.violetnote.model.BasicNoteDataA;
import com.romanpulov.violetnote.model.BasicNoteItemA;
import com.romanpulov.violetnote.model.PassNoteItemCryptService;

/**
 * Created by romanpulov on 13.09.2016.
 */
public class BasicNoteDataAddItemAction extends BasicNoteDataItemAction {

    public BasicNoteDataAddItemAction(BasicNoteDataA basicNoteData, BasicNoteItemA item) {
        super(basicNoteData, item);
    }

    @Override
    public boolean execute(DBNoteManager noteManager) {
        //encrypt
        if (mBasicNoteData.getNote().isEncrypted()) {
            if (!PassNoteItemCryptService.encryptBasicNoteItem(mItem, mBasicNoteData.getPassword()))
                return false;
        }

        //insert
        return noteManager.insertNoteItem(mBasicNoteData.getNote(), mItem) != -1;
    }
}
