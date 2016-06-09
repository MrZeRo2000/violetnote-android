package com.romanpulov.violetnote;

import android.content.Context;

import com.romanpulov.violetnotecore.AESCrypt.AESCryptException;
import com.romanpulov.violetnotecore.AESCrypt.AESCryptService;
import com.romanpulov.violetnotecore.Model.PassCategory;
import com.romanpulov.violetnotecore.Model.PassData;
import com.romanpulov.violetnotecore.Model.PassNote;
import com.romanpulov.violetnotecore.Processor.Exception.DataReadWriteException;
import com.romanpulov.violetnotecore.Processor.XMLPassDataReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rpulov on 03.04.2016.
 */
public class Document {
    private static final String DOCUMENT_FILE_NAME = "document.vnf";

    private static Document mInstance;


    public static Document getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new Document(context);
        }
        return mInstance;
    }

    private Context mContext;

    private List<String> mLoadErrorList = new ArrayList<>();

    public List<String> getLoadErrorList() {
        return mLoadErrorList;
    }

    public String getFileName() {
        return mContext.getCacheDir() + DOCUMENT_FILE_NAME;
    }

    public PassDataA loadPassDataA(String fileName, String masterPass) {
        mLoadErrorList.clear();

        if (masterPass == null) {
            return null;
        }

        try {
            File f = new File(fileName);
            if (f.exists()) {
                InputStream input = AESCryptService.generateCryptInputStream(new FileInputStream(f), masterPass);
                PassData pd =  (new XMLPassDataReader()).readStream(input);
                if (pd != null)
                    return PassDataA.newInstance(masterPass, pd);
                else
                    return null;
            } else
                throw new FileNotFoundException();
        }
        catch(FileNotFoundException e) {
            mLoadErrorList.add(mContext.getResources().getString(R.string.error_file_not_found));
            return null;
        }
        catch (IOException e) {
            mLoadErrorList.add(mContext.getResources().getString(R.string.error_io));
            return null;
        }
        catch (AESCryptException e) {
            mLoadErrorList.add(mContext.getResources().getString(R.string.error_crypt));
            return null;
        }
        catch(DataReadWriteException e) {
            mLoadErrorList.add(mContext.getResources().getString(R.string.error_read));
            return null;
        }
    }

    public static PassDataA loadSamplePassData() {
        PassData pd = new PassData();

        PassCategory pc = new PassCategory("Category 1");
        pd.getPassCategoryList().add(pc);

        PassNote pn = new PassNote(pc, "System 1", "User 1", "Password 1", null, null, null);
        pd.getPassNoteList().add(pn);
        pn = new PassNote(pc, "System 12", "User 12", "Password 12", null, null, null);
        pd.getPassNoteList().add(pn);
        pn = new PassNote(pc, "System 13", "User 13", "Password 13", null, null, null);
        pd.getPassNoteList().add(pn);

        pc = new PassCategory("Category 2");
        pd.getPassCategoryList().add(pc);

        pn = new PassNote(pc, "System 2", "User 2", "Password 2", null, null, null);
        pd.getPassNoteList().add(pn);

        for (int i = 3; i < 30; i ++) {
            pd.getPassCategoryList().add(new PassCategory("Category " + i));
        }

        return PassDataA.newInstance(null, pd);
    }

    private Document(Context context) {
        mContext = context;
    }
}
