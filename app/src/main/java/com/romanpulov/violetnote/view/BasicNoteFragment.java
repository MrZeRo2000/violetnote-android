package com.romanpulov.violetnote.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.db.manager.DBNoteManager;
import com.romanpulov.violetnote.model.BasicEntityNoteSelectionPosA;
import com.romanpulov.violetnote.model.BasicNoteA;
import com.romanpulov.violetnote.model.BasicNoteGroupA;
import com.romanpulov.violetnote.view.action.BasicActionExecutor;
import com.romanpulov.violetnote.view.action.BasicNoteMoveToOtherNoteGroupAction;
import com.romanpulov.violetnote.view.action.BasicNoteRefreshAction;
import com.romanpulov.violetnote.view.helper.BottomToolbarHelper;
import com.romanpulov.violetnote.view.helper.DisplayTitleBuilder;
import com.romanpulov.violetnote.view.action.BasicItemsMoveAction;
import com.romanpulov.violetnote.view.action.BasicItemsMoveBottomAction;
import com.romanpulov.violetnote.view.action.BasicItemsMoveDownAction;
import com.romanpulov.violetnote.view.action.BasicItemsMoveTopAction;
import com.romanpulov.violetnote.view.action.BasicItemsMoveUpAction;
import com.romanpulov.violetnote.view.core.AlertOkCancelSupportDialogFragment;
import com.romanpulov.violetnote.view.core.BasicCommonNoteFragment;
import com.romanpulov.violetnote.view.core.RecyclerViewHelper;
import com.romanpulov.violetnote.view.helper.InputActionHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class BasicNoteFragment extends BasicCommonNoteFragment {
    private static void log(String message) {
        Log.d("BasicNoteFragment", message);
    }
    protected final static int MENU_GROUP_OTHER_ITEMS = Menu.FIRST + 1;

    private InputActionHelper mInputActionHelper;

    private BasicNoteGroupA mNoteGroup;
    private List<BasicNoteGroupA> mRelatedNoteGroupList;
    private OnBasicNoteFragmentInteractionListener mListener;

    private final List<BasicNoteA> mNoteList = new ArrayList<>();

    public static BasicNoteFragment newInstance(DBNoteManager noteManager, BasicNoteGroupA noteGroup) {
        BasicNoteFragment fragment = new BasicNoteFragment();
        Bundle args = new Bundle();
        args.putParcelable(BasicNoteGroupA.BASIC_NOTE_GROUP_DATA, noteGroup);
        fragment.setArguments(args);
        //fragment.refreshList(noteManager);
        return fragment;
    }

    public BasicNoteFragment() {
    }

    @Override
    public void refreshList(DBNoteManager noteManager) {
        noteManager.mBasicNoteDAO.fillNotesByGroup(mNoteGroup, mNoteList);
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

    @NonNull
    private List<BasicNoteA> getSelectedNotes() {
        return BasicEntityNoteSelectionPosA.getItemsByPositions(mNoteList, mRecyclerViewSelector.getSelectedItems());
    }

    protected boolean processMoveMenuItemClick(MenuItem menuItem) {
        List<BasicNoteA> selectedNotes = getSelectedNotes();

        //int selectedItemPos = mRecyclerViewSelector.getSelectedItemPos();
        if (selectedNotes.size() > 0) {
            switch (menuItem.getItemId()) {
                case R.id.move_up:
                    performMoveAction(new BasicItemsMoveUpAction<>(mNoteGroup, selectedNotes), selectedNotes);
                    return true;
                case R.id.move_top:
                    performMoveAction(new BasicItemsMoveTopAction<>(mNoteGroup, selectedNotes), selectedNotes);
                    return true;
                case R.id.move_down:
                    performMoveAction(new BasicItemsMoveDownAction<>(mNoteGroup, selectedNotes), selectedNotes);
                    return true;
                case R.id.move_bottom:
                    performMoveAction(new BasicItemsMoveBottomAction<>(mNoteGroup, selectedNotes), selectedNotes);
                    return true;
            }
        }
        return false;
    }

    public void performAddAction(final BasicNoteA item) {
        DBNoteManager mNoteManager = new DBNoteManager(getActivity());
        if (mNoteManager.mBasicNoteDAO.insert(item) != -1) {
            // refresh list
            refreshList(mNoteManager);

            //scroll to bottom
            mRecyclerView.scrollToPosition(mNoteList.size() - 1);
        }
    }

    private void performDeleteAction(final ActionMode mode, final List<BasicNoteA> items) {
        AlertOkCancelSupportDialogFragment dialog = AlertOkCancelSupportDialogFragment.newAlertOkCancelDialog(getString(R.string.ui_question_are_you_sure));
        dialog.setOkButtonClickListener(new AlertOkCancelSupportDialogFragment.OnClickListener() {
            @Override
            public void OnClick(DialogFragment dialog) {
                // delete item
                DBNoteManager mNoteManager = new DBNoteManager(getActivity());

                for (BasicNoteA item : items)
                    mNoteManager.mBasicNoteDAO.delete(item);

                // refresh list
                refreshList(mNoteManager);

                //finish action
                mode.finish();
            }
        });

        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager != null)
            dialog.show(fragmentManager, null);
    }

    private void performEditAction(String text) {
        List<BasicNoteA> selectedNotes = getSelectedNotes();
        BasicNoteA selectedNote;
        if ((selectedNotes.size() == 1) && (!text.equals((selectedNote = selectedNotes.get(0)).getTitle()))) {
            //change
            selectedNote.setTitle(text);

            //update
            DBNoteManager noteManager = new DBNoteManager(getContext());
            noteManager.mBasicNoteDAO.update(selectedNote);

            //refresh list
            BasicNoteFragment.this.refreshList(noteManager);

            //update list item
            int position = mNoteList.indexOf(selectedNote);
            if ((position != -1) && (mRecyclerView != null)) {
                RecyclerViewHelper.adapterNotifyItemChanged(mRecyclerView, position);
            }
        }
    }

    /*
    private void performEditAction(final ActionMode mode, final BasicNoteA item) {
        (new TextEditDialogBuilder(getActivity(), getString(R.string.ui_note_title), item.getTitle()))
                .setNonEmptyErrorMessage(getString(R.string.error_field_not_empty))
                .setOnTextInputListener(new TextInputDialog.OnTextInputListener() {
                    @Override
                    public void onTextInput(String text) {
                        if (!text.equals(item.getTitle())) {
                            //change
                            item.setTitle(text);

                            //update database
                            DBNoteManager noteManager = new DBNoteManager(getActivity());
                            noteManager.mBasicNoteDAO.update(item);

                            //refresh list
                            BasicNoteFragment.this.refreshList(noteManager);
                        }
                        // finish anyway
                        mode.finish();
                    }
                })
                .execute();
    }
    */

    private void performMoveToOtherNoteGroupAction(final ActionMode mode, @NonNull final List<BasicNoteA> items, @NonNull final BasicNoteGroupA otherNoteGroup) {
        String confirmationQuestion = getString(R.string.ui_question_selected_note_move_to_other_note_group, items.size(), otherNoteGroup.getDisplayTitle());
        AlertOkCancelSupportDialogFragment dialog = AlertOkCancelSupportDialogFragment.newAlertOkCancelDialog(confirmationQuestion);
        dialog.setOkButtonClickListener(new AlertOkCancelSupportDialogFragment.OnClickListener() {
            @Override
            public void OnClick(DialogFragment dialog) {
                //executor
                BasicActionExecutor<List<BasicNoteA>> executor = new BasicActionExecutor<>(getContext(), mNoteList);

                //actions
                executor.addAction(getString(R.string.caption_processing), new BasicNoteMoveToOtherNoteGroupAction(items, otherNoteGroup));
                executor.addAction(getString(R.string.caption_loading), new BasicNoteRefreshAction(mNoteList, mNoteGroup));

                //on completed
                executor.setOnExecutionCompletedListener(new BasicActionExecutor.OnExecutionCompletedListener<List<BasicNoteA>>() {
                    @Override
                    public void onExecutionCompleted(List<BasicNoteA> data, boolean result) {
                        mode.finish();

                        if (result) {
                            RecyclerViewHelper.adapterNotifyDataSetChanged(mRecyclerView);
                        }

                        if (mDialogFragment != null) {
                            mDialogFragment.dismiss();
                            mDialogFragment = null;
                        }
                    }
                });

                //execute
                executor.execute();
            }
        });

        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager != null)
            dialog.show(fragmentManager, null);

        mDialogFragment = dialog;
    }

    private void performMoveAction(BasicItemsMoveAction<?, BasicNoteA> action, List<BasicNoteA> items) {
        DBNoteManager noteManager = new DBNoteManager(getActivity());

        if (action.execute(noteManager)) {
            refreshList(noteManager);

            BasicEntityNoteSelectionPosA selectionPos = new BasicEntityNoteSelectionPosA(mNoteList, items);
            int selectionScrollPos = selectionPos.getDirectionPos(action.getDirection());

            if (selectionScrollPos != -1) {
                mRecyclerViewSelector.setSelectedItems(selectionPos.getSelectedItemsPositions());
                mRecyclerView.scrollToPosition(selectionScrollPos);
            }
        }
    }

    private void updateTitle(ActionMode mode) {
        mode.setTitle(DisplayTitleBuilder.buildItemsDisplayTitle(getActivity(), mNoteList, mRecyclerViewSelector.getSelectedItems()));
    }

    public class ActionBarCallBack implements ActionMode.Callback {
        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            List<BasicNoteA> selectedNotes = getSelectedNotes();

            if (selectedNotes.size() > 0) {
                if ((item.getGroupId() == MENU_GROUP_OTHER_ITEMS) && (mRelatedNoteGroupList != null)) {
                    // move to other items
                    BasicNoteGroupA otherNoteGroup = mRelatedNoteGroupList.get(item.getItemId());
                    performMoveToOtherNoteGroupAction(mode, selectedNotes, otherNoteGroup);
                } else {
                    switch (item.getItemId()) {
                        case R.id.delete:
                            performDeleteAction(mode, selectedNotes);
                            break;
                        case R.id.edit:
                            final BasicNoteA selectedNote = selectedNotes.get(0);
                            mInputActionHelper.showLayout(selectedNote.getTitle(), InputActionHelper.INPUT_ACTION_TYPE_EDIT);
                            break;
                    }
                }
            }
            return false;
        }

        private void buildMoveToOtherGroupsSubMenu(Menu menu) {
            DBNoteManager mNoteManager = new DBNoteManager(getContext());
            mRelatedNoteGroupList =  mNoteManager.mBasicNoteGroupDAO.getRelatedNoteGroupList(mNoteGroup);

            SubMenu subMenu = null;
            int order = 1;
            int relatedNoteGroupIndex = 0;

            for (BasicNoteGroupA noteGroupA : mRelatedNoteGroupList) {
                if (subMenu == null) {
                    subMenu = menu.addSubMenu(Menu.NONE, Menu.NONE, order++, getString(R.string.action_move_other));
                    subMenu.getItem().setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
                    subMenu.clearHeader();
                }

                subMenu.add(MENU_GROUP_OTHER_ITEMS, relatedNoteGroupIndex ++, Menu.NONE, noteGroupA.getDisplayTitle());
            }
        }

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.menu_edit_delete_actions, menu);

            buildMoveToOtherGroupsSubMenu(menu);

            if (mInputActionHelper != null) {
                mInputActionHelper.hideLayout();
            }

            if (mRecyclerViewSelector.isSelected()) {
                updateTitle(mode);
            }

            return true;
        }


        @Override
        public void onDestroyActionMode(ActionMode mode) {
            hideAddLayout();

            if (mBottomToolbarHelper != null) {
                mBottomToolbarHelper.hideLayout();
            }
            if (mRecyclerViewSelector != null)
                mRecyclerViewSelector.destroyActionMode();
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            hideAddLayout();

            log("onPrepareActionMode");
            //menu.getItem(R.menu.)
            MenuItem menuItem = menu.findItem(R.id.edit);
            if (menuItem != null)
                menuItem.setVisible(mRecyclerViewSelector.isSelectedSingle());

            if (mBottomToolbarHelper == null) {
                setupBottomToolbarHelper();
            }

            if (mBottomToolbarHelper != null) {
                mBottomToolbarHelper.showLayout(mRecyclerViewSelector.getSelectedItems().size(), mNoteList.size());
            }

            updateTitle(mode);
            return false;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arguments = getArguments();
        if (arguments != null) {
            mNoteGroup = arguments.getParcelable(BasicNoteGroupA.BASIC_NOTE_GROUP_DATA);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_basic_note_list, container, false);
        mRecyclerView = view.findViewById(R.id.list);

        refreshList(new DBNoteManager(view.getContext()));

        // Set the adapter
        Context context = view.getContext();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));

        BasicNoteRecycleViewAdapter recyclerViewAdapter = new BasicNoteRecycleViewAdapter(mNoteList, new ActionBarCallBack(), mListener);
        mRecyclerViewSelector = recyclerViewAdapter.getRecyclerViewSelector();
        mRecyclerView.setAdapter(recyclerViewAdapter);

        //restore selected items
        restoreSelectedItems(savedInstanceState, view);

        // add decoration
        mRecyclerView.addItemDecoration(new RecyclerViewHelper.DividerItemDecoration(getActivity(), RecyclerViewHelper.DividerItemDecoration.VERTICAL_LIST, R.drawable.divider_white_black_gradient));

        //add action panel
        mInputActionHelper = new InputActionHelper(view.findViewById(R.id.add_panel_include));
        mInputActionHelper.setOnAddInteractionListener(new InputActionHelper.OnAddInteractionListener() {
            @Override
            public void onAddFragmentInteraction(int actionType, String text) {
                hideAddLayout();

                performEditAction(text);

                mRecyclerViewSelector.finishActionMode();
            }
        });

        return view;
    }

    public void hideAddLayout() {
        if (mInputActionHelper != null) {
            mInputActionHelper.hideLayout();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnBasicNoteFragmentInteractionListener) {
            mListener = (OnBasicNoteFragmentInteractionListener) context;
        }
        else {
            throw new RuntimeException(context.toString()
                    + " must implement OnBasicNoteFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnBasicNoteFragmentInteractionListener {
        void onBasicNoteFragmentInteraction(BasicNoteA item);
    }
}
