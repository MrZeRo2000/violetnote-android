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

    public static boolean encryptBasicNoteNamedItem(BasicNoteItemA item, String password) {
        try {
            item.setName(AESCryptService.encryptString(item.getName(), password));
            item.setValue(AESCryptService.encryptString(item.getValue(), password));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean decryptBasicNoteCheckedItem(BasicNoteItemA item, String password) {
        try {
            item.setValue(AESCryptService.decryptString(item.getValue(), password));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean decryptBasicNoteNamedItem(BasicNoteItemA item, String password) {
        try {
            item.setName(AESCryptService.decryptString(item.getName(), password));
            item.setValue(AESCryptService.decryptString(item.getValue(), password));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
