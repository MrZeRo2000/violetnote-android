package com.romanpulov.violetnote.model.service;

import com.romanpulov.violetnote.model.BasicNoteItemA;
import com.romanpulov.violetnotecore.Service.StringCryptService;

import org.json.JSONObject;

/**
 * BasicNoteItemA encrypt and decrypt
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

            String encryptedJSON = StringCryptService.encryptStringAES128(jsonObject.toString(), password);
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
            JSONObject jsonObject = new JSONObject(StringCryptService.decryptStringAES128(encryptedJSON, password));

            item.setName(jsonObject.optString(JSON_NAME));
            item.setValue(jsonObject.optString(JSON_VALUE));

            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
