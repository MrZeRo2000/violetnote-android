package com.romanpulov.violetnote.dropboxchooser;

import android.app.Activity;
import android.os.Bundle;

import com.romanpulov.violetnote.chooser.AbstractChooseItem;
import com.romanpulov.violetnote.chooser.HrChooserFragment;
import com.romanpulov.library.dropbox.DropboxHelper;

/**
 * Fragment for shoosing file from Dropbox
 * Created by romanpulov on 02.07.2016.
 */
public class DropboxChooserFragment extends HrChooserFragment {

    @Override
    protected AbstractChooseItem getChooseItem() {
        Activity activity = getActivity();
        if (activity == null)
            throw new RuntimeException("No context for " + this);
        DropboxHelper dropBoxHelper = DropboxHelper.getInstance(activity.getApplication());
        return DropboxChooseItem.fromPath(dropBoxHelper.getClient(), DropboxChooseItem.getParentItemPath(mInitialPath));
    }

    public DropboxChooserFragment() {
        super();
        setRetainInstance(true);
    }

    @Override
    protected void requestChooseItem(AbstractChooseItem item) {
        startChooserUpdaterTask(item);
    }

    public static HrChooserFragment newInstance(String initialPath) {
        HrChooserFragment fragment = new DropboxChooserFragment();
        Bundle args = new Bundle();
        args.putString(HR_CHOOSER_INITIAL_PATH, initialPath);
        fragment.setArguments(args);
        return fragment;
    }

}
