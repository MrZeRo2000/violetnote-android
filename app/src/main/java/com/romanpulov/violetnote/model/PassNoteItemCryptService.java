package com.romanpulov.violetnote.model;

import com.romanpulov.violetnotecore.AESCrypt.AESCryptService;

/**
 * Created by rpulov on 12.09.2016.
 */
public class PassNoteItemCryptService {

    public static boolean encryptBasicNoteItem(BasicNoteItemA item, String password) {
        try {
            //name
            if ((item.getName() != null) && (!item.getName().isEmpty()))
                item.setName(AESCryptService.encryptString(item.getName(), password));
            //value
            if ((item.getValue() != null) && (!item.getValue().isEmpty()))
                item.setValue(AESCryptService.encryptString(item.getValue(), password));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean decryptBasicNoteItem(BasicNoteItemA item, String password) {
        try {
            //name
            if ((item.getName() != null) && (!item.getName().isEmpty()))
                item.setName(AESCryptService.decryptString(item.getName(), password));
            //value
            if ((item.getValue() != null) && (!item.getValue().isEmpty()))
                item.setValue(AESCryptService.decryptString(item.getValue(), password));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
