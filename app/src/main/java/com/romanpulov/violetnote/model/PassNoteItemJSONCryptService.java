package com.romanpulov.violetnote.model;

import com.romanpulov.violetnotecore.AESCrypt.AESCryptService;

import org.json.JSONObject;

/**
 * Created by rpulov on 17.10.2016.
 */

public class PassNoteItemJSONCryptService {
    private static final String JSON_NAME = "Name";
    private static final String JSON_VALUE = "Value";

    public static boolean encryptBasicNoteItem(BasicNoteItemA item, String password) {
        try {
            JSONObject jsonObject = new JSONObject();
            if ((item.getName() != null) && (!item.getName().isEmpty()))
                jsonObject.put(JSON_NAME, item.getName());

            if ((item.getValue() != null) && (!item.getValue().isEmpty()))
                jsonObject.put(JSON_VALUE, item.getValue());

            String encryptedJSON = AESCryptService.encryptString(jsonObject.toString(), password);
            item.setName("");
            item.setValue(encryptedJSON);

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean decryptBasicNoteItem(BasicNoteItemA item, String password) {
        try {
            String encryptedJSON = item.getValue();
            JSONObject jsonObject = new JSONObject(AESCryptService.decryptString(encryptedJSON, password));

            item.setName(jsonObject.optString(JSON_NAME));
            item.setValue(jsonObject.optString(JSON_VALUE));

            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
