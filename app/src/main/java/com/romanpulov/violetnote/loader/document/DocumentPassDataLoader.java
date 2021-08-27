package com.romanpulov.violetnote.loader.document;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.model.PassDataA;
import com.romanpulov.violetnotecore.AESCrypt.AESCryptException;
import com.romanpulov.violetnotecore.Model.PassCategory2;
import com.romanpulov.violetnotecore.Model.PassData2;
import com.romanpulov.violetnotecore.Model.PassNote2;
import com.romanpulov.violetnotecore.Processor.Exception.DataReadWriteException;
import com.romanpulov.violetnotecore.Service.PassData2ReaderServiceV2;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rpulov on 03.04.2016.
 * Document PassData loader loader class
 */
public class DocumentPassDataLoader {
    public static final String DOCUMENT_FILE_NAME = "document.vnf";

    /**
     * Returns document file name by context
     * @param context Context to get file name
     * @return document file name
     */
    public static String getDocumentFileName(Context context) {
        return context.getFilesDir() + File.separator + DOCUMENT_FILE_NAME;
    }

    /**
     * Returns document file if it exists, otherwise null
     * @param context Context to get file name
     * @return file if it exists
     */
    @Nullable
    public static File getDocumentFile(Context context) {
        File file = new File(getDocumentFileName(context));
        return file.exists() ? file : null;
    }

    public static DocumentPassDataLoader newInstance(Context context) {
        return new DocumentPassDataLoader(context.getApplicationContext());
    }

    public static class DocumentPassDataLoadResult {
        private final PassDataA mPassData;
        private final String mErrorMessage;

        public PassDataA getPassData() {
            return mPassData;
        }

        public String getErrorMessage() {
            return mErrorMessage;
        }

        public DocumentPassDataLoadResult(PassDataA mPassData, String mErrorMessage) {
            this.mPassData = mPassData;
            this.mErrorMessage = mErrorMessage;
        }

        public static DocumentPassDataLoadResult fromErrorMessage(String errorMessage) {
            return new DocumentPassDataLoadResult(null, errorMessage);
        }

        public static DocumentPassDataLoadResult fromPassData(PassDataA passData) {
            return new DocumentPassDataLoadResult(passData, null);
        }
    }

    @NonNull
    public static DocumentPassDataLoadResult loadPassData(Context context, String password) {

        if (password == null) {
            return DocumentPassDataLoadResult.fromErrorMessage(context.getResources().getString(R.string.error_empty_password));
        }

        File file = getDocumentFile(context);
        if (file == null) {
            return DocumentPassDataLoadResult.fromErrorMessage(context.getResources().getString(R.string.error_file_not_found));
        }

        try {
            try (InputStream inputStream = new FileInputStream(file)) {
                PassData2 pd = PassData2ReaderServiceV2.fromStream(inputStream, password);
                if (pd != null)
                    return DocumentPassDataLoadResult.fromPassData(PassDataA.newInstance(password, pd));
                else
                    return DocumentPassDataLoadResult.fromErrorMessage(context.getResources().getString(R.string.error_read));
            }
        }
        catch(FileNotFoundException e) {
            return DocumentPassDataLoadResult.fromErrorMessage(context.getResources().getString(R.string.error_file_not_found));
        }
        catch (IOException e) {
            return DocumentPassDataLoadResult.fromErrorMessage(context.getResources().getString(R.string.error_io));
        }
        catch (AESCryptException e) {
            return DocumentPassDataLoadResult.fromErrorMessage(context.getResources().getString(R.string.error_crypt));
        }
        catch(DataReadWriteException e) {
            return DocumentPassDataLoadResult.fromErrorMessage(context.getResources().getString(R.string.error_read));
        }
    }

    private final Context mContext;

    private final List<String> mLoadErrorList = new ArrayList<>();

    public List<String> getLoadErrorList() {
        return mLoadErrorList;
    }

    public PassDataA loadPassDataA(String fileName, String masterPass) {
        mLoadErrorList.clear();

        if (masterPass == null) {
            return null;
        }

        try {
            File f = new File(fileName);
            if (f.exists()) {
                try (InputStream inputStream = new FileInputStream(f)) {
                    PassData2 pd = PassData2ReaderServiceV2.fromStream(inputStream, masterPass);
                    if (pd != null)
                        return PassDataA.newInstance(masterPass, pd);
                    else
                        return null;
                }
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
        PassData2 pd = new PassData2();
        pd.setCategoryList(new ArrayList<PassCategory2>());

        PassCategory2 pc = new PassCategory2("Category 1");
        pc.setNoteList(new ArrayList<PassNote2>());

        pd.getCategoryList().add(pc);

        PassNote2 pn = new PassNote2( "System 1", "User 1", "Password 1", null, null, null, null);
        pc.getNoteList().add(pn);
        pn = new PassNote2( "System 12", "User 12", "Password 12", null, null, null, null);
        pc.getNoteList().add(pn);
        pn = new PassNote2("System 13", "User 13", "Password 13", null, null, null, null);
        pc.getNoteList().add(pn);

        pc = new PassCategory2("Category 2");
        pc.setNoteList(new ArrayList<PassNote2>());

        pd.getCategoryList().add(pc);

        pn = new PassNote2("System 2", "User 2", "Password 2", null, null, null, null);
        pc.getNoteList().add(pn);

        for (int i = 3; i < 30; i ++) {
            PassCategory2 passCategory2 = new PassCategory2("Category " + i);
            passCategory2.setNoteList(new ArrayList<PassNote2>());
            pd.getCategoryList().add(passCategory2);
        }

        return PassDataA.newInstance(null, pd);
    }

    private DocumentPassDataLoader(Context context) {
        mContext = context;
    }
}
