package com.romanpulov.violetnote.view.action;

import com.romanpulov.violetnote.db.DBNoteManager;
import com.romanpulov.violetnote.model.BasicNoteDataA;
import com.romanpulov.violetnote.model.BasicNoteItemA;
import com.romanpulov.violetnote.model.PassNoteItemJSONCryptService;

import java.util.ArrayList;

/**
 * BasicNoteItemA add actopm
 * Created by romanpulov on 13.09.2016.
 */
public class BasicNoteDataItemAddAction extends BasicNoteDataItemAction {

    public BasicNoteDataItemAddAction(BasicNoteDataA basicNoteData, BasicNoteItemA item) {
        super(basicNoteData, new ArrayList<BasicNoteItemA>());
        mItems.add(item);
    }

    @Override
    public boolean execute(DBNoteManager noteManager) {
        //encrypt
        if (mBasicNoteData.getNote().isEncrypted()) {
            if (!PassNoteItemJSONCryptService.encryptBasicNoteItem(mItems.get(0), mBasicNoteData.getPassword()))
                return false;
        }

        //insert
        return noteManager.insertNoteItem(mBasicNoteData.getNote(), mItems.get(0)) != -1;
    }
}
