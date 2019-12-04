package com.romanpulov.violetnote.onedrivechooser;

import android.app.Activity;
import android.os.Bundle;

import com.onedrive.sdk.core.ClientException;
import com.onedrive.sdk.extensions.Item;
import com.romanpulov.violetnote.chooser.AbstractChooseItem;
import com.romanpulov.violetnote.chooser.HrChooserFragment;
import com.romanpulov.library.onedrive.OneDriveHelper;

import java.io.File;

import static com.romanpulov.library.onedrive.OneDriveHelper.ROOT_PATH;

public class OneDriveChooserFragment extends HrChooserFragment {
    private final OneDriveHelper mOneDriveHelper = OneDriveHelper.getInstance();
    {
        mOneDriveHelper.setOnOneDriveItemListener(new OneDriveHelper.OnOneDriveItemListener() {
            @Override
            public void onItemSuccess(Item item) {
                updateChooseItem(fillChooseItem(OneDriveChooseItem.fromOneDriveItem(item)));
            }

            @Override
            public void onItemFailure(ClientException ex) {
                updateChooseItem(fillChooseItem(OneDriveChooseItem.createErrorItem(ex.getMessage())));
            }
        });
    }

    @Override
    protected AbstractChooseItem getChooseItem() {
        return null;
    }

    @Override
    protected void requestChooseItem(AbstractChooseItem item) {
        Activity activity = getActivity();
        if (activity != null){
            setProgress();

            String path;
            if (item == null) {
                path = ROOT_PATH;
            } else if (item.getItemPath().equals(ROOT_PATH)) {
                path = ROOT_PATH;
            } else {
                path = ROOT_PATH + File.separator + item.getItemPath();
            }

            mOneDriveHelper.listItems(activity, path);
        }
    }

    public OneDriveChooserFragment() {
        super();
        setRetainInstance(true);
    }

    public static HrChooserFragment newInstance(String initialPath) {
        HrChooserFragment fragment = new OneDriveChooserFragment();
        Bundle args = new Bundle();
        args.putString(HR_CHOOSER_INITIAL_PATH, initialPath);
        fragment.setArguments(args);
        return fragment;
    }
}
