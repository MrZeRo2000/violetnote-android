package com.romanpulov.violetnote.view;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.model.BasicNoteDataA;
import com.romanpulov.violetnote.model.BasicNoteItemA;
import com.romanpulov.violetnote.view.action.BasicNoteDataActionExecutor;
import com.romanpulov.violetnote.view.action.BasicNoteDataItemEditNameValueAction;
import com.romanpulov.violetnote.view.action.BasicNoteDataRefreshAction;
import com.romanpulov.violetnote.view.action.BasicNoteMoveBottomAction;
import com.romanpulov.violetnote.view.action.BasicNoteMoveDownAction;
import com.romanpulov.violetnote.view.action.BasicNoteMoveTopAction;
import com.romanpulov.violetnote.view.action.BasicNoteMoveUpAction;
import com.romanpulov.violetnote.view.core.NameValueInputDialog;
import com.romanpulov.violetnote.view.core.PasswordActivity;
import com.romanpulov.violetnote.view.core.RecyclerViewHelper;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class BasicNoteNamedItemFragment extends BasicNoteItemFragment {

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public BasicNoteNamedItemFragment() {
    }

    public static BasicNoteNamedItemFragment newInstance(BasicNoteDataA basicNoteDataA) {
        BasicNoteNamedItemFragment fragment = new BasicNoteNamedItemFragment();
        Bundle args = new Bundle();
        args.putParcelable(PasswordActivity.PASS_DATA, basicNoteDataA);
        fragment.setArguments(args);
        return fragment;
    }

    public void performAddAction() {
        NameValueInputDialog dialog = new NameValueInputDialog(getActivity(), getString(R.string.ui_name_value_title));
        dialog.setOnNameValueInputListener(new NameValueInputDialog.OnNameValueInputListener() {
            @Override
            public void onNameValueInput(String name, String value) {
                if ((name != null) && (value != null)) {
                    performAddAction(BasicNoteItemA.newNamedEditInstance(name, value));
                }
            }
        });
        dialog.show();
        mEditorDialog = dialog.getAlertDialog();
    }

    private void performEditAction(final ActionMode mode, final BasicNoteItemA item) {
        NameValueInputDialog dialog = new NameValueInputDialog(getActivity(), getString(R.string.ui_name_value_title));
        dialog.setNameValue(item.getName(), item.getValue());
        dialog.setOnNameValueInputListener(new NameValueInputDialog.OnNameValueInputListener() {
            @Override
            public void onNameValueInput(String name, String value) {
                if (!name.equals(item.getName()) || !value.equals(item.getValue())) {
                    //change
                    item.setName(name);
                    item.setValue(value);

                    BasicNoteDataActionExecutor executor = new BasicNoteDataActionExecutor(getActivity());
                    executor.addAction(getString(R.string.caption_processing), new BasicNoteDataItemEditNameValueAction(mBasicNoteData, item));
                    executor.addAction(getString(R.string.caption_loading), new BasicNoteDataRefreshAction(mBasicNoteData));
                    executor.setOnExecutionCompletedListener(new BasicNoteDataActionExecutor.OnExecutionCompletedListener() {
                        @Override
                        public void onExecutionCompleted(boolean result) {
                            // finish anyway
                            mode.finish();
                            //clear editor reference
                            mEditorDialog.dismiss();
                            mEditorDialog = null;
                        }
                    });
                    executor.execute(mBasicNoteData.getNote().isEncrypted());
                }
                // finish anyway
                mode.finish();
                //
                mEditorDialog = null;
            }
        });
        dialog.show();
        mEditorDialog = dialog.getAlertDialog();
    }

    public class ActionBarCallBack implements ActionMode.Callback {
        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            int selectedItemPos = mRecyclerViewSelector.getSelectedItemPos();
            if (selectedItemPos != -1) {
                BasicNoteItemA selectedItem = mBasicNoteData.getNote().getItems().get(selectedItemPos);
                switch (item.getItemId()) {
                    case R.id.delete:
                        performDeleteAction(mode, selectedItem);
                        break;
                    case R.id.edit:
                        performEditAction(mode, selectedItem);
                        break;
                    case R.id.move_up:
                        performMoveAction(new BasicNoteMoveUpAction<BasicNoteItemA>(BasicNoteNamedItemFragment.this), selectedItem);
                        break;
                    case R.id.move_top:
                        performMoveAction(new BasicNoteMoveTopAction<BasicNoteItemA>(BasicNoteNamedItemFragment.this), selectedItem);
                        break;
                    case R.id.move_down:
                        performMoveAction(new BasicNoteMoveDownAction<BasicNoteItemA>(BasicNoteNamedItemFragment.this), selectedItem);
                        break;
                    case R.id.move_bottom:
                        performMoveAction(new BasicNoteMoveBottomAction<BasicNoteItemA>(BasicNoteNamedItemFragment.this), selectedItem);
                        break;
                }
            }
            return false;
        }

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.menu_listitem_generic_actions, menu);
            if (mRecyclerViewSelector.getSelectedItemPos() != -1)
                mode.setTitle( mBasicNoteData.getNote().getItems().get(mRecyclerViewSelector.getSelectedItemPos()).getValue());
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            if (mRecyclerViewSelector != null)
                mRecyclerViewSelector.destroyActionMode();
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_basic_note_named_item_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            mRecyclerView = (RecyclerView) view;

            mRecyclerView.setLayoutManager(new LinearLayoutManager(context));

            BasicNoteNamedItemRecyclerViewAdapter recyclerViewAdapter = new BasicNoteNamedItemRecyclerViewAdapter(mBasicNoteData.getNote().getItems(), new ActionBarCallBack(),
                    new OnBasicNoteItemFragmentInteractionListener() {
                        @Override
                        public void onBasicNoteItemFragmentInteraction(BasicNoteItemA item, int position) {
                            // no action currently required
                            //placeholder for future
                        }
                    }
            );
            mRecyclerViewSelector = recyclerViewAdapter.getRecyclerViewSelector();
            mRecyclerView.setAdapter(recyclerViewAdapter);

            // add decoration
            mRecyclerView.addItemDecoration(new RecyclerViewHelper.DividerItemDecoration(getActivity(), RecyclerViewHelper.DividerItemDecoration.VERTICAL_LIST, R.drawable.divider_white_black_gradient));
        }
        return view;
    }
}
