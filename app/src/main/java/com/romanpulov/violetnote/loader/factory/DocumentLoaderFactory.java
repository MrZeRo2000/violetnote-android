package com.romanpulov.violetnote.loader.factory;

import com.romanpulov.library.common.loader.core.AbstractContextLoader;
import com.romanpulov.violetnote.loader.document.DocumentDropboxFileLoader;
import com.romanpulov.violetnote.loader.document.DocumentLocalFileLoader;
import com.romanpulov.violetnote.loader.document.DocumentOneDriveFileLoader;
import com.romanpulov.violetnote.loader.document.DocumentUriFileLoader;
import com.romanpulov.violetnote.loader.onedrive.OneDriveFileLoader;
import com.romanpulov.violetnote.view.preference.PreferenceRepository;

/**
 * Document loader factory class
 * Created by romanpulov on 17.11.2017.
 */

public class DocumentLoaderFactory {
    public static Class<? extends AbstractContextLoader> classFromType(int type) {
        switch (type) {
            case PreferenceRepository.SOURCE_TYPE_FILE:
                //return DocumentLocalFileLoader.class;
                return DocumentUriFileLoader.class;
            case PreferenceRepository.SOURCE_TYPE_DROPBOX:
                return DocumentDropboxFileLoader.class;
            case PreferenceRepository.SOURCE_TYPE_ONEDRIVE:
                return DocumentOneDriveFileLoader.class;
            default:
                return null;
        }
    }
}
