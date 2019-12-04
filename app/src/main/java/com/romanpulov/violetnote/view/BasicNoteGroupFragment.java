package com.romanpulov.violetnote.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.view.ActionMode;
import androidx.appcompat.widget.ActionMenuView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.db.manager.DBNoteManager;
import com.romanpulov.violetnote.model.BasicEntityNoteSelectionPosA;
import com.romanpulov.violetnote.model.BasicNoteGroupA;
import com.romanpulov.violetnote.view.action.BasicActionExecutor;
import com.romanpulov.violetnote.view.action.BasicItemsMoveAction;
import com.romanpulov.violetnote.view.action.BasicItemsMoveBottomAction;
import com.romanpulov.violetnote.view.action.BasicItemsMoveDownAction;
import com.romanpulov.violetnote.view.action.BasicItemsMoveTopAction;
import com.romanpulov.violetnote.view.action.BasicItemsMoveUpAction;
import com.romanpulov.violetnote.view.action.BasicNoteGroupAddAction;
import com.romanpulov.violetnote.view.action.BasicNoteGroupDeleteAction;
import com.romanpulov.violetnote.view.action.BasicNoteGroupEditAction;
import com.romanpulov.violetnote.view.action.BasicNoteGroupRefreshAction;
import com.romanpulov.violetnote.view.core.AlertOkCancelSupportDialogFragment;
import com.romanpulov.violetnote.view.core.BasicCommonNoteFragment;
import com.romanpulov.violetnote.view.core.RecyclerViewHelper;
import com.romanpulov.violetnote.view.helper.BottomToolbarHelper;
import com.romanpulov.violetnote.view.helper.DisplayTitleBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BasicNoteGroupFragment extends BasicCommonNoteFragment {
    public static final int ACTIVITY_REQUEST_INSERT = 0;
    public static final int ACTIVITY_REQUEST_EDIT = 1;

    private final List<BasicNoteGroupA> mBasicNoteGroupList = new ArrayList<>();
    private OnBasicNoteGroupFragmentInteractionListener mListener;
    private DBNoteManager mDBNoteManager;

    public static BasicNoteGroupFragment newInstance() {
        return new BasicNoteGroupFragment();
    }

    public BasicNoteGroupFragment() {

    }

    @Override
    public void refreshList(DBNoteManager noteManager) {
        (new BasicNoteGroupRefreshAction(mBasicNoteGroupList)).execute(noteManager);

        if (mRecyclerView != null)
            RecyclerViewHelper.adapterNotifyDataSetChanged(mRecyclerView);
    }

    private void setupBottomToolbarHelper() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            mBottomToolbarHelper = BottomToolbarHelper.fromContext(activity, new ActionMenuView.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    return processMoveMenuItemClick(menuItem);
                }
            });
        }
    }

    protected boolean processMoveMenuItemClick(MenuItem menuItem) {
        List<BasicNoteGroupA> selectedNoteItems = BasicEntityNoteSelectionPosA.getItemsByPositions(mBasicNoteGroupList, mRecyclerViewSelector.getSelectedItems());

        if (selectedNoteItems.size() > 0) {
            switch (menuItem.getItemId()) {
                case R.id.move_up:
                    performMoveAction(new BasicItemsMoveUpAction<>(mBasicNoteGroupList, selectedNoteItems), selectedNoteItems);
                    return true;
                case R.id.move_top:
                    performMoveAction(new BasicItemsMoveTopAction<>(mBasicNoteGroupList, selectedNoteItems), selectedNoteItems);
                    return true;
                case R.id.move_down:
                    performMoveAction(new BasicItemsMoveDownAction<>(mBasicNoteGroupList, selectedNoteItems), selectedNoteItems);
                    return true;
                case R.id.move_bottom:
                    performMoveAction(new BasicItemsMoveBottomAction<>(mBasicNoteGroupList, selectedNoteItems), selectedNoteItems);
                    return true;
                default:
                    return false;
            }
        } else
            return false;
    }

    protected void performMoveAction(final BasicItemsMoveAction<List<BasicNoteGroupA>, BasicNoteGroupA> action, final List<BasicNoteGroupA> items) {
        //executor
        BasicActionExecutor<List<BasicNoteGroupA>> executor = new BasicActionExecutor<>(getContext(), items);

        //actions
        executor.addAction(getString(R.string.caption_processing), action);
        executor.addAction(getString(R.string.caption_loading), new BasicNoteGroupRefreshAction(mBasicNoteGroupList));

        //on complete
        executor.setOnExecutionCompletedListener(new BasicActionExecutor.OnExecutionCompletedListener<List<BasicNoteGroupA>>() {
            @Override
            public void onExecutionCompleted(List<BasicNoteGroupA> data, boolean result) {
                BasicEntityNoteSelectionPosA selectionPos = new BasicEntityNoteSelectionPosA(mBasicNoteGroupList, items);
                int selectionScrollPos = selectionPos.getDirectionPos(action.getDirection());

                if (selectionScrollPos != -1) {
                    mRecyclerViewSelector.setSelectedItems(selectionPos.getSelectedItemsPositions());
                    mRecyclerView.scrollToPosition(selectionScrollPos);
                }
            }
        });

        //execute
        executor.execute();
    }

    public void performAddAction(@NonNull final BasicNoteGroupA item) {
        final List<BasicNoteGroupA> items = Collections.singletonList(item);
        BasicActionExecutor<List<BasicNoteGroupA>> executor = new BasicActionExecutor<>(getContext(), items);
        executor.addAction(getString(R.string.caption_processing), new BasicNoteGroupAddAction(items));
        executor.addAction(getString(R.string.caption_loading), new BasicNoteGroupRefreshAction(mBasicNoteGroupList));
        executor.setOnExecutionCompletedListener(new BasicActionExecutor.OnExecutionCompletedListener<List<BasicNoteGroupA>>() {
            @Override
            public void onExecutionCompleted(List<BasicNoteGroupA> data, boolean result) {
                if (result) {
                    RecyclerViewHelper.adapterNotifyDataSetChanged(mRecyclerView);

                    int position = mBasicNoteGroupList.size() - 1;

                    if (position > -1)
                        mRecyclerView.scrollToPosition(position);

                }
            }
        });
        executor.execute();
    }

    private void performDeleteAction(@NonNull final ActionMode mode, @NonNull final List<BasicNoteGroupA> items) {
        // initial check
        if (items.size() != 1) {
            return;
        }

        BasicNoteGroupA item = items.get(0);

        AlertOkCancelSupportDialogFragment dialog;

        // check notes
        if (mDBNoteManager.mBasicNoteDAO.getByGroup(item).size() > 0) {
            dialog = AlertOkCancelSupportDialogFragment.newAlertOkInfoDialog(getString(R.string.ui_error_group_delete_contain_notes, item.getDisplayTitle()));
        } else {
            dialog = AlertOkCancelSupportDialogFragment.newAlertOkCancelDialog(getString(R.string.ui_question_delete_item_are_you_sure, item.getDisplayTitle()));
            dialog.setOkButtonClickListener(new AlertOkCancelSupportDialogFragment.OnClickListener() {
                @Override
                public void OnClick(DialogFragment dialog) {
                    BasicActionExecutor<List<BasicNoteGroupA>> executor = new BasicActionExecutor<>(getContext(), items);
                    executor.addAction(getString(R.string.caption_processing), new BasicNoteGroupDeleteAction(mBasicNoteGroupList, items));
                    executor.addAction(getString(R.string.caption_loading), new BasicNoteGroupRefreshAction(mBasicNoteGroupList));
                    executor.setOnExecutionCompletedListener(new BasicActionExecutor.OnExecutionCompletedListener<List<BasicNoteGroupA>>() {
                        @Override
                        public void onExecutionCompleted(List<BasicNoteGroupA> data, boolean result) {
                            if (result) {
                                mode.finish();
                            }
                            if (mDialogFragment != null) {
                                mDialogFragment.dismiss();
                                mDialogFragment = null;
                            }

                        }
                    });
                    executor.execute();
                }
            });
        }

        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager != null)
            dialog.show(fragmentManager, null);

        mDialogFragment = dialog;
    }

    private void performEditAction(@NonNull final ActionMode mode, @NonNull final List<BasicNoteGroupA> items) {
        if (items.size() == 1) {
            BasicNoteGroupA item = items.get(0);
            Intent intent = new Intent(getContext(), BasicNoteGroupEditActivity.class);
            intent.putExtra(BasicNoteGroupA.BASIC_NOTE_GROUP_DATA, item);
            startActivityForResult(intent, ACTIVITY_REQUEST_EDIT);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        final BasicNoteGroupA noteGroup;
        if ((requestCode == BasicNoteGroupFragment.ACTIVITY_REQUEST_EDIT) && (resultCode == Activity.RESULT_OK) && (data != null) && ((noteGroup = data.getParcelableExtra(BasicNoteGroupA.BASIC_NOTE_GROUP_DATA)) != null)) {
            List<BasicNoteGroupA> items = Collections.singletonList(noteGroup);

            //executor
            BasicActionExecutor<List<BasicNoteGroupA>> executor = new BasicActionExecutor<>(getContext(), items);

            //actions
            executor.addAction(getString(R.string.caption_processing), new BasicNoteGroupEditAction(items));
            executor.addAction(getString(R.string.caption_loading), new BasicNoteGroupRefreshAction(mBasicNoteGroupList));

            //on complete
            executor.setOnExecutionCompletedListener(new BasicActionExecutor.OnExecutionCompletedListener<List<BasicNoteGroupA>>() {
                @Override
                public void onExecutionCompleted(List<BasicNoteGroupA> data, boolean result) {
                    if (result) {
                        int position = mBasicNoteGroupList.indexOf(noteGroup);
                        if (mRecyclerView != null) {
                            RecyclerViewHelper.adapterNotifyItemChanged(mRecyclerView, position);
                        }

                        updateTitle(mRecyclerViewSelector.getActionMode());
                    }
                }
            });

            //execute
            executor.execute();
        }
    }

    private void updateTitle(ActionMode mode) {
        if ((mRecyclerViewSelector != null) && (mRecyclerViewSelector.isSelected())) {
            mode.setTitle(DisplayTitleBuilder.buildItemsDisplayTitle(getContext(), mBasicNoteGroupList, mRecyclerViewSelector.getSelectedItems()));
        }
    }

    public class ActionBarCallBack implements ActionMode.Callback {
        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            List<BasicNoteGroupA> selectedNoteItems = BasicEntityNoteSelectionPosA.getItemsByPositions(mBasicNoteGroupList, mRecyclerViewSelector.getSelectedItems());

            //int selectedItemPos = mRecyclerViewSelector.getSelectedItemPos();
            if (selectedNoteItems.size() == 1) {
                switch (item.getItemId()) {
                    case R.id.delete:
                        performDeleteAction(mode, selectedNoteItems);
                        break;
                    case R.id.edit:
                        performEditAction(mode, selectedNoteItems);
                        break;
                    case R.id.move_up:
                        //performMoveAction(new BasicItemsMoveUpAction<BasicNoteA>(), selectedNoteItems);
                        break;
                    case R.id.move_top:
                        //performMoveAction(new BasicItemsMoveTopAction<BasicNoteA>(), selectedNoteItems);
                        break;
                    case R.id.move_down:
                        //performMoveAction(new BasicItemsMoveDownAction<BasicNoteA>(), selectedNoteItems);
                        break;
                    case R.id.move_bottom:
                        //performMoveAction(new BasicItemsMoveBottomAction<BasicNoteA>(), selectedNoteItems);
                        break;
                }
            }
            return false;
        }

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.menu_edit_delete_actions, menu);
            updateTitle(mode);
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            if (mBottomToolbarHelper != null) {
                mBottomToolbarHelper.hideLayout();
            }
            if (mRecyclerViewSelector != null)
                mRecyclerViewSelector.destroyActionMode();
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            //menu.getItem(R.menu.)
            MenuItem menuItem = menu.findItem(R.id.edit);
            if (menuItem != null)
                menuItem.setVisible(mRecyclerViewSelector.isSelectedSingle());

            if (mBottomToolbarHelper == null) {
                setupBottomToolbarHelper();
            }

            if (mBottomToolbarHelper != null) {
                mBottomToolbarHelper.showLayout(mRecyclerViewSelector.getSelectedItems().size(), mBasicNoteGroupList.size());
            }

            updateTitle(mode);
            return false;
        }
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_recycler_view_list, container, false);

        Context context = view.getContext();

        mRecyclerView = (RecyclerView) view;
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));

        mDBNoteManager = new DBNoteManager(context);
        refreshList(mDBNoteManager);

        BasicNoteGroupItemRecyclerViewAdapter recyclerViewAdapter = new BasicNoteGroupItemRecyclerViewAdapter(mBasicNoteGroupList, new ActionBarCallBack());
        mRecyclerView.setAdapter(recyclerViewAdapter);

        mRecyclerViewSelector = recyclerViewAdapter.getRecyclerViewSelector();

        //restore selected items
        restoreSelectedItems(savedInstanceState, view);

        // add decoration
        mRecyclerView.addItemDecoration(new RecyclerViewHelper.DividerItemDecoration(getActivity(), RecyclerViewHelper.DividerItemDecoration.VERTICAL_LIST, R.drawable.divider_white_black_gradient));

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnBasicNoteGroupFragmentInteractionListener) {
            mListener = (OnBasicNoteGroupFragmentInteractionListener) context;
        }
        else {
            throw new RuntimeException(context.toString()
                    + " must implement OnBasicNoteGroupFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnBasicNoteGroupFragmentInteractionListener {
        void onBasicNoteGroupFragmentInteraction(BasicNoteGroupA item);
    }
}
