package com.romanpulov.violetnote.dropboxchooser;

import android.os.Bundle;

import com.romanpulov.violetnote.chooser.ChooseItem;
import com.romanpulov.violetnote.chooser.HrChooserFragment;
import com.romanpulov.violetnote.dropbox.DropBoxHelper;

/**
 * Created by romanpulov on 02.07.2016.
 */
public class DropboxChooserFragment extends HrChooserFragment {
    @Override
    protected ChooseItem getChooseItem() {
        DropBoxHelper dropBoxHelper = DropBoxHelper.getInstance(getActivity().getApplication());
        return DropboxChooseItem.fromPath(dropBoxHelper.getClient(), DropboxChooseItem.getParentItemPath(mInitialPath));
    }

    public static HrChooserFragment newInstance(String initialPath) {
        HrChooserFragment fragment = new DropboxChooserFragment();
        fragment.setFillMode(HR_MODE_ASYNC);
        Bundle args = new Bundle();
        args.putString(HR_CHOOSER_INITIAL_PATH, initialPath);
        fragment.setArguments(args);
        return fragment;
    }

}
