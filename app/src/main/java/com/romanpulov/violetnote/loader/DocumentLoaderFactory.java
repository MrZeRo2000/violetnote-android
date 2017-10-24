package com.romanpulov.violetnote.loader;

import android.content.Context;

import com.romanpulov.violetnote.view.preference.PreferenceRepository;

import java.lang.reflect.Constructor;

/**
 * Factory for DocumentLoader creation
 * Created by romanpulov on 16.06.2016.
 */
public class DocumentLoaderFactory {
    public static AbstractLoader fromType(Context context, int type) {
        switch(type) {
            case PreferenceRepository.SOURCE_TYPE_FILE:
                //return new DocumentFileLoader(context);
                return new DocumentLocalFileLoader(context);
            case PreferenceRepository.SOURCE_TYPE_DROPBOX:
                //return new DocumentDropboxFileLoader(context);
                return new DocumentDropboxFileLoader(context);
            default:
                return null;
        }
    }

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

    public static AbstractLoader fromClassName(Context context, String className) {
        Class<?> clazz;
        try {
            clazz = Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }

        Constructor<?> contextConstructor;
        try {
            contextConstructor = clazz.getConstructor(Context.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        try {
            return (AbstractLoader) contextConstructor.newInstance(context);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
