package com.romanpulov.violetnote;

import android.os.Environment;

import com.romanpulov.violetnotecore.AESCrypt.AESCryptException;
import com.romanpulov.violetnotecore.AESCrypt.AESCryptService;
import com.romanpulov.violetnotecore.Model.PassData;
import com.romanpulov.violetnotecore.Processor.Exception.DataReadWriteException;
import com.romanpulov.violetnotecore.Processor.XMLPassDataReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by rpulov on 03.04.2016.
 */
public class Document {
    public static final String DEF_FILE_NAME = "/temp/1.vnf";

    private PassData mPassData;

    public PassData getPassData() {
        return mPassData;
    }

    private String mFileName;

    public String getFileName() {
        return mFileName;
    }

    private String mMasterPass;

    private void setFile(String fileName, String masterPass) {
        mFileName = fileName;
        mMasterPass = masterPass;
    }

    public void resetData() {
        mFileName = null;
        mMasterPass = null;
        mPassData = null;
    }

    public boolean loadPassData(String fileName, String masterPass) {
        if (masterPass == null) {
            resetData();
            return false;
        }

        try {
            File f = new File(Environment.getExternalStorageDirectory() + fileName);
            if (f.exists()) {
                InputStream input = AESCryptService.generateCryptInputStream(new FileInputStream(f), masterPass);
                mPassData = (new XMLPassDataReader()).readStream(input);
                setFile(fileName, masterPass);
                return true;
            } else
                return false;
        } catch(IOException | AESCryptException | DataReadWriteException e) {
            resetData();
            return false;
        }
    }

    public void setPassData(PassData passData) {
        mPassData = passData;
    }

    private Document() {

    }

    private static Document mOurInstance = new Document();

    public static Document getInstance() {
        return mOurInstance;
    }
}
