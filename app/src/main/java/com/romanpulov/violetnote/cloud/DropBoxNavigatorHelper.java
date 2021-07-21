package com.romanpulov.violetnote.cloud;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.FolderMetadata;
import com.dropbox.core.v2.files.Metadata;
import com.romanpulov.library.dropbox.DropboxHelper;
import com.romanpulov.violetnote.picker.HrPickerItem;
import com.romanpulov.violetnote.picker.HrPickerNavigationProcessor;
import com.romanpulov.violetnote.picker.HrPickerNavigator;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class DropBoxNavigatorHelper implements HrPickerNavigator {

    private Executor mExecutor;

    private final AtomicBoolean mNavigating = new AtomicBoolean(false);

    private static int getItemTypeFromMetaData(Metadata metaData) {
        if (metaData == null) {
            return HrPickerItem.ITEM_TYPE_FOLDER;
        }
        else if (metaData instanceof FileMetadata) {
            return HrPickerItem.ITEM_TYPE_FILE;
        }
        else if (metaData instanceof FolderMetadata) {
            return HrPickerItem.ITEM_TYPE_FOLDER;
        }
        else {
            return HrPickerItem.ITEM_TYPE_UNKNOWN;
        }
    }

    private static String getNavigationPath(String path) {
        return path.equals("/") ? "" : path;
    }

    @Override
    synchronized public void onNavigate(final Context context, final String path, final HrPickerNavigationProcessor processor) {
        if (!mNavigating.get()) {
            mNavigating.set(true);

            if (mExecutor == null) {
                mExecutor = Executors.newSingleThreadExecutor();
            }

            mExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        final List<HrPickerItem> items = new ArrayList<>();

                        for (Metadata m : DropboxHelper
                                .getInstance(context)
                                .getClient()
                                .files()
                                .listFolder(getNavigationPath(path))
                                .getEntries()) {
                            items.add(HrPickerItem.createItem(getItemTypeFromMetaData(m), m.getName()));
                        }

                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                // Code here will run in UI thread
                                processor.onNavigationSuccess(path, items);
                            }
                        });


                    } catch (final Exception e) {

                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                // Code here will run in UI thread
                                processor.onNavigationFailure(path, e.getMessage());
                            }
                        });

                        e.printStackTrace();
                    } finally {
                        mNavigating.set(false);
                    }
                }
            });
        }
    }
}
