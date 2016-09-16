package com.romanpulov.violetnote.view.action;

import com.romanpulov.violetnote.db.DBNoteManager;
import com.romanpulov.violetnote.model.BasicNoteDataA;
import com.romanpulov.violetnote.model.BasicNoteItemA;
import com.romanpulov.violetnote.model.PassNoteItemCryptService;

/**
 * Created by romanpulov on 13.09.2016.
 */
public class BasicNoteDataRefreshAction extends BasicNoteDataAction {
    private final String mPassword;

    public BasicNoteDataRefreshAction(BasicNoteDataA basicNoteData) {
        super(basicNoteData);
        mPassword = basicNoteData.getPassword();
    }

    public BasicNoteDataRefreshAction(BasicNoteDataA basicNoteData, String password) {
        super(basicNoteData);
        mPassword = password;
    }

    @Override
    public boolean execute(DBNoteManager noteManager) {
        //retrieve data
        noteManager.queryNoteDataItems(mBasicNoteData.getNote());

        //decrypt
        if (mBasicNoteData.getNote().isEncrypted()) {
            for (BasicNoteItemA item : mBasicNoteData.getNote().getItems()) {
                if (!PassNoteItemCryptService.decryptBasicNoteItem(item, mPassword))
                    return false;
            }
        }
        return true;
    }
}
