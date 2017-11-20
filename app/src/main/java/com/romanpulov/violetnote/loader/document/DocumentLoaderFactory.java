package com.romanpulov.violetnote.loader.document;

import com.romanpulov.violetnote.loader.core.AbstractLoader;
import com.romanpulov.violetnote.view.preference.PreferenceRepository;

/**
 * Created by romanpulov on 17.11.2017.
 */

public class DocumentLoaderFactory {
    public static Class<? extends AbstractLoader> classFromType(int type) {
        switch (type) {
            case PreferenceRepository.SOURCE_TYPE_FILE:
                return DocumentLocalFileLoader.class;
            case PreferenceRepository.SOURCE_TYPE_DROPBOX:
                return DocumentDropboxFileLoader.class;
            default:
                return null;
        }
    }
}
