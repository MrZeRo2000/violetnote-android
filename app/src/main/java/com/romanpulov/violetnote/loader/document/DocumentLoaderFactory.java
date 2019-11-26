package com.romanpulov.violetnote.loader.document;

import com.romanpulov.library.common.loader.core.AbstractContextLoader;
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
                return DocumentLocalFileLoader.class;
            case PreferenceRepository.SOURCE_TYPE_DROPBOX:
                return DocumentDropboxFileLoader.class;
            case PreferenceRepository.SOURCE_TYPE_ONEDRIVE:
                return DocumentOneDriveFileLoader.class;
            default:
                return null;
        }
    }
}
