package com.romanpulov.violetnote.model;

import com.romanpulov.violetnotecore.AESCrypt.AESCryptService;

/**
 * Created by rpulov on 12.09.2016.
 */
public class PassNoteItemCryptService {
    public static boolean encryptBasicNoteCheckedItem(BasicNoteItemA item, String password) {
        try {
            item.setValue(AESCryptService.encryptString(item.getValue(), password));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
