package com.romanpulov.violetnote.view;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.view.ActionMode;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.databinding.FragmentBasicNoteListBinding;
import com.romanpulov.violetnote.db.manager.DBNoteManager;
import com.romanpulov.violetnote.model.*;
import com.romanpulov.violetnote.view.action.BasicActionExecutor;
import com.romanpulov.violetnote.view.action.BasicNoteMoveToOtherNoteGroupAction;
import com.romanpulov.violetnote.view.action.BasicNoteRefreshAction;
import com.romanpulov.violetnote.view.core.TextEditDialogBuilder;
import com.romanpulov.violetnote.view.helper.*;
import com.romanpulov.violetnote.view.action.BasicItemsMoveAction;
import com.romanpulov.violetnote.view.action.BasicItemsMoveBottomAction;
import com.romanpulov.violetnote.view.action.BasicItemsMoveDownAction;
import com.romanpulov.violetnote.view.action.BasicItemsMoveTopAction;
import com.romanpulov.violetnote.view.action.BasicItemsMoveUpAction;
import com.romanpulov.violetnote.view.core.AlertOkCancelSupportDialogFragment;
import com.romanpulov.violetnote.view.core.BasicCommonNoteFragment;
import com.romanpulov.violetnote.view.core.RecyclerViewHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A placeholder fragment containing a simple view.
 */
public class BasicNoteFragment extends BasicCommonNoteFragment {
    protected final static int MENU_GROUP_OTHER_ITEMS = Menu.FIRST + 1;

    private FragmentBasicNoteListBinding binding;
    private BasicNoteViewModel model;

    private BasicNoteRecycleViewAdapter mRecyclerViewAdapter;

    private InputActionHelper mInputActionHelper;

    private List<BasicNoteGroupA> mRelatedNoteGroupList;
    private OnBasicNoteFragmentInteractionListener mListener;

    private @NonNull BasicNoteGroupA getNoteGroup() {
        return Objects.requireNonNull(model.getBasicNoteGroup());
    }

    private @NonNull List<BasicNoteA> getNoteList() {
        return Objects.requireNonNull(model.getBasicNotes().getValue());
    }

    public static BasicNoteFragment newInstance(BasicNoteGroupA noteGroup) {
        BasicNoteFragment fragment = new BasicNoteFragment();
        Bundle args = new Bundle();
        args.putParcelable(BasicNoteGroupA.BASIC_NOTE_GROUP_DATA, noteGroup);
        fragment.setArguments(args);
        //fragment.refreshList(noteManager);
        return fragment;
    }

    public BasicNoteFragment() {
    }

    private void setupBottomToolbarHelper() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            mBottomToolbarHelper = BottomToolbarHelper.fromContext(activity, this::processMoveMenuItemClick);
        }
    }

    @NonNull
    private List<BasicNoteA> getSelectedNotes() {
        return BasicEntityNoteSelectionPosA.getItemsByPositions(getNoteList(), mRecyclerViewSelector.getSelectedItems());
    }

    protected boolean processMoveMenuItemClick(MenuItem menuItem) {
        List<BasicNoteA> selectedNotes = getSelectedNotes();

        //int selectedItemPos = mRecyclerViewSelector.getSelectedItemPos();
        if (!selectedNotes.isEmpty()) {
            int itemId = menuItem.getItemId();
            if (itemId == R.id.move_up) {
                performMoveAction(new BasicItemsMoveUpAction<>(getNoteGroup(), selectedNotes), selectedNotes);
                return true;
            } else if (itemId == R.id.move_top) {
                performMoveAction(new BasicItemsMoveTopAction<>(getNoteGroup(), selectedNotes), selectedNotes);
                return true;
            } else if (itemId == R.id.move_down) {
                performMoveAction(new BasicItemsMoveDownAction<>(getNoteGroup(), selectedNotes), selectedNotes);
                return true;
            } else if (itemId == R.id.move_bottom) {
                performMoveAction(new BasicItemsMoveBottomAction<>(getNoteGroup(), selectedNotes), selectedNotes);
                return true;
            }
        }
        return false;
    }

    public void performAddAction(final BasicNoteA item) {
        /*
        DBNoteManager mNoteManager = new DBNoteManager(getActivity());
        if (mNoteManager.mBasicNoteDAO.insert(item) != -1) {
            // refresh list
            refreshList(mNoteManager);

            //scroll to bottom
            mRecyclerView.scrollToPosition(mNoteList.size() - 1);
        }

         */
    }

    private void performDeleteAction(final ActionMode mode, final List<BasicNoteA> items) {
        AlertOkCancelSupportDialogFragment dialog = AlertOkCancelSupportDialogFragment.newAlertOkCancelDialog(getString(R.string.ui_question_are_you_sure));
        dialog.setOkButtonClickListener(dialog1 -> {
            // delete item
            DBNoteManager mNoteManager = new DBNoteManager(getActivity());

            for (BasicNoteA item : items)
                mNoteManager.mBasicNoteDAO.delete(item);

            // refresh list
            //refreshList(mNoteManager);

            //finish action
            mode.finish();
        });

        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager != null)
            dialog.show(fragmentManager, null);
    }

    private void performDuplicateAction(final ActionMode mode, final BasicNoteA item) {
        TextEditDialogBuilder textEditDialogBuilder = (new TextEditDialogBuilder(getActivity(), getString(R.string.ui_note_value),
                null))
                .setNonEmptyErrorMessage(getString(R.string.error_field_not_empty))
                .setShowInput(true);

        final AlertDialog alertDialog = textEditDialogBuilder.execute();
        alertDialog.setTitle(R.string.ui_note_title);

        textEditDialogBuilder.setOnTextInputListener(text -> {
            //hide editor
            View focusedView = alertDialog.getCurrentFocus();
            InputManagerHelper.hideInput(focusedView);

            /*
            if (BasicNoteA.findByTitle(mNoteList, text) == null) {
                //add
                BasicNoteA newNote = ParcelableUtils.duplicateParcelableObject(item, BasicNoteA.CREATOR);
                newNote.setTitle(text);

                //persist
                DBNoteManager mNoteManager = new DBNoteManager(getActivity());

                //get items
                mNoteManager.mBasicNoteItemDAO.fillNoteDataItemsWithSummary(newNote);

                //get new id
                long newItemId = mNoteManager.mBasicNoteDAO.insert(newNote);
                if (newItemId != -1) {
                    //insert items ignoring any errors
                    for (BasicNoteItemA noteItem : newNote.getItems()) {
                        noteItem.setNoteId(newItemId);
                        mNoteManager.mBasicNoteItemDAO.insert(noteItem);
                    }

                    // refresh list
                    refreshList(mNoteManager);

                    //scroll to bottom
                    mRecyclerView.scrollToPosition(mNoteList.size() - 1);
                }

                // finish anyway
                mode.finish();
            } else {
                Activity activity = requireActivity();
                DisplayMessageHelper.displayInfoMessage(activity, activity.getString(R.string.ui_error_note_already_exists, text));
            }

             */
        });
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
            // BasicNoteFragment.this.refreshList(noteManager);

            //update list item
            int position = getNoteList().indexOf(selectedNote);
            if ((position != -1) && (mRecyclerView != null)) {
                RecyclerViewHelper.adapterNotifyItemChanged(mRecyclerView, position);
            }
        }
    }

    private void performMoveToOtherNoteGroupAction(final ActionMode mode, @NonNull final List<BasicNoteA> items, @NonNull final BasicNoteGroupA otherNoteGroup) {
        String confirmationQuestion = getResources()
                .getQuantityString(R.plurals.ui_question_selected_note_move_to_other_note_group, items.size(), items.size(), otherNoteGroup.getDisplayTitle());
        AlertOkCancelSupportDialogFragment dialog = AlertOkCancelSupportDialogFragment.newAlertOkCancelDialog(confirmationQuestion);
        dialog.setOkButtonClickListener(dialog1 -> {
            //executor
            BasicActionExecutor<List<BasicNoteA>> executor = new BasicActionExecutor<>(getContext(), getNoteList());

            //actions
            executor.addAction(getString(R.string.caption_processing), new BasicNoteMoveToOtherNoteGroupAction(items, otherNoteGroup));
            executor.addAction(getString(R.string.caption_loading), new BasicNoteRefreshAction(getNoteList(), getNoteGroup()));

            //on completed
            executor.setOnExecutionCompletedListener((data, result) -> {
                mode.finish();

                if (result) {
                    RecyclerViewHelper.adapterNotifyDataSetChanged(mRecyclerView);
                }

                if (mDialogFragment != null) {
                    mDialogFragment.dismiss();
                    mDialogFragment = null;
                }
            });

            //execute
            executor.execute();
        });

        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager != null)
            dialog.show(fragmentManager, null);

        mDialogFragment = dialog;
    }

    private void performMoveAction(BasicItemsMoveAction<?, BasicNoteA> action, List<BasicNoteA> items) {
        DBNoteManager noteManager = new DBNoteManager(getActivity());

        if (action.execute(noteManager)) {
            // refreshList(noteManager);

            BasicEntityNoteSelectionPosA selectionPos = new BasicEntityNoteSelectionPosA(getNoteList(), items);
            int selectionScrollPos = selectionPos.getDirectionPos(action.getDirection());

            if (selectionScrollPos != -1) {
                mRecyclerViewSelector.setSelectedItems(selectionPos.getSelectedItemsPositions());
                mRecyclerView.scrollToPosition(selectionScrollPos);
            }
        }
    }

    private void updateTitle(ActionMode mode) {
        mode.setTitle(DisplayTitleBuilder.buildItemsDisplayTitle(getActivity(), getNoteList(), mRecyclerViewSelector.getSelectedItems()));
    }

    public class ActionBarCallBack implements ActionMode.Callback {
        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            List<BasicNoteA> selectedNotes = getSelectedNotes();

            if (!selectedNotes.isEmpty()) {
                if ((item.getGroupId() == MENU_GROUP_OTHER_ITEMS) && (mRelatedNoteGroupList != null)) {
                    // move to other items
                    BasicNoteGroupA otherNoteGroup = mRelatedNoteGroupList.get(item.getItemId());
                    performMoveToOtherNoteGroupAction(mode, selectedNotes, otherNoteGroup);
                } else {
                    int itemId = item.getItemId();
                    if (itemId == R.id.delete) {
                        performDeleteAction(mode, selectedNotes);
                    } else if (itemId == R.id.edit) {
                        mInputActionHelper.showEditLayout(selectedNotes.get(0).getTitle());
                    } else if (itemId == R.id.duplicate) {
                        performDuplicateAction(mode, selectedNotes.get(0));
                    }
                }
            }
            return false;
        }

        private void buildMoveToOtherGroupsSubMenu(Menu menu) {
            DBNoteManager mNoteManager = new DBNoteManager(getContext());
            mRelatedNoteGroupList =  mNoteManager.mBasicNoteGroupDAO.getRelatedNoteGroupList(getNoteGroup());

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
            mode.getMenuInflater().inflate(R.menu.menu_edit_delete_duplicate_actions, menu);

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

        private void setSingleSelectedMenusVisibility(@NonNull Iterable<MenuItem> menuItems) {
            for (MenuItem menuItem : menuItems) {
                menuItem.setVisible(mRecyclerViewSelector.isSelectedSingle());
            }
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            hideAddLayout();

            List<MenuItem> singleMenuItems = new ArrayList<>();

            MenuItem editMenuItem = menu.findItem(R.id.edit);
            if (editMenuItem != null) {
                singleMenuItems.add(editMenuItem);
            }

            MenuItem duplicateMenuItem = menu.findItem(R.id.duplicate);
            if (duplicateMenuItem != null) {
                singleMenuItems.add(duplicateMenuItem);
            }

            setSingleSelectedMenusVisibility(singleMenuItems);

            if (mBottomToolbarHelper == null) {
                setupBottomToolbarHelper();
            }

            if (mBottomToolbarHelper != null) {
                mBottomToolbarHelper.showLayout(mRecyclerViewSelector.getSelectedItems().size(), getNoteList().size());
            }

            List<BasicNoteA> selectedNotes = getSelectedNotes();
            if (selectedNotes.size() == 1) {
                mRecyclerView.scrollToPosition(getNoteList().indexOf(selectedNotes.get(0)));
            }

            updateTitle(mode);
            return false;
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentBasicNoteListBinding.inflate(getLayoutInflater());
        setupBottomToolbarHelper();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Context context = view.getContext();
        mRecyclerView = binding.list;
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        // add decoration
        mRecyclerView.addItemDecoration(new RecyclerViewHelper.DividerItemDecoration(
                getActivity(),
                RecyclerViewHelper.DividerItemDecoration.VERTICAL_LIST,
                R.drawable.divider_white_black_gradient));

        mRecyclerView.setAdapter(mRecyclerViewAdapter);

        //add action panel
        mInputActionHelper = new InputActionHelper(view.findViewById(R.id.add_panel_include));
        mInputActionHelper.setOnAddInteractionListener((actionType, text) -> {
            hideAddLayout();

            performEditAction(text);

            mRecyclerViewSelector.finishActionMode();
        });

        model = new ViewModelProvider(this).get(BasicNoteViewModel.class);
        model.setBasicNoteGroup(BasicNoteFragmentArgs.fromBundle(getArguments()).getNoteGroup());

        final Observer<List<BasicNoteA>> notesObserver = newNotes -> {

            if (model.getBasicNotes().getValue() == null) {
                model.getBasicNotes().setValue(newNotes);

                mRecyclerViewAdapter = new BasicNoteRecycleViewAdapter(newNotes, new ActionBarCallBack(), mListener);
                mRecyclerView.setAdapter(mRecyclerViewAdapter);
                mRecyclerViewSelector = mRecyclerViewAdapter.getRecyclerViewSelector();

                //restore selected items
                restoreSelectedItems(savedInstanceState, view);
            } else {
                if (mRecyclerViewAdapter == null) {
                    mRecyclerViewAdapter = new BasicNoteRecycleViewAdapter(newNotes, new ActionBarCallBack(), mListener);
                    mRecyclerView.setAdapter(mRecyclerViewAdapter);
                    mRecyclerViewSelector = mRecyclerViewAdapter.getRecyclerViewSelector();
                } else {
                    DiffUtil.DiffResult result = DiffUtilHelper.getEntityListDiffResult(this::getNoteList, newNotes);
                    mRecyclerViewAdapter.setItems(newNotes);
                    result.dispatchUpdatesTo(mRecyclerViewAdapter);
                }
            }
        };

        model.getBasicNotes().observe(this, notesObserver);


    }

    public void hideAddLayout() {
        if (mInputActionHelper != null) {
            mInputActionHelper.hideLayout();
        }
    }

    public interface OnBasicNoteFragmentInteractionListener {
        void onBasicNoteFragmentInteraction(BasicNoteA item);
    }
}
