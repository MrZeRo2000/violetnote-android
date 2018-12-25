package com.romanpulov.violetnote.view.action;

import com.romanpulov.violetnote.db.DBNoteManager;
import com.romanpulov.violetnote.model.BasicNoteDataA;
import com.romanpulov.violetnote.model.BasicNoteItemA;
import com.romanpulov.violetnote.model.PassNoteItemJSONCryptService;

/**
 * BasicNoteDataA refresh action
 * Created by romanpulov on 13.09.2016.
 */
public class BasicNoteDataRefreshAction extends BasicNoteDataAction {
    private final String mPassword;
    private boolean mRequireValues = false;

    public BasicNoteDataRefreshAction(BasicNoteDataA basicNoteData) {
        super(basicNoteData);
        mPassword = basicNoteData.getPassword();
    }

    public BasicNoteDataRefreshAction(BasicNoteDataA basicNoteData, String password) {
        super(basicNoteData);
        mPassword = password;
    }

    public BasicNoteDataRefreshAction requireValues() {
        mRequireValues = true;
        return this;
    }

    @Override
    public boolean execute(DBNoteManager noteManager) {
        //retrieve data
        noteManager.mBasicNoteItemDAO.fillNoteDataItemsWithSummary(mBasicNoteData.getNote());
        if (mRequireValues)
            noteManager.queryNoteDataValues(mBasicNoteData.getNote());

        //decrypt
        if (mBasicNoteData.getNote().isEncrypted()) {
            for (BasicNoteItemA item : mBasicNoteData.getNote().getItems()) {
                if (!PassNoteItemJSONCryptService.decryptBasicNoteItem(item, mPassword))
                    return false;
            }
        }
        mBasicNoteData.setPassword(mPassword);
        return true;
    }
}
