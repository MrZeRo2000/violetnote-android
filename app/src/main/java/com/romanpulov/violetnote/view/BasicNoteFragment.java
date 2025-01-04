package com.romanpulov.violetnote.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuHost;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.view.ActionMode;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.databinding.FragmentBasicNoteListBinding;
import com.romanpulov.violetnote.model.*;
import com.romanpulov.violetnote.view.action.*;
import com.romanpulov.violetnote.view.core.*;
import com.romanpulov.violetnote.view.helper.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BasicNoteFragment extends BasicCommonNoteFragment  {
    protected final static int MENU_GROUP_OTHER_ITEMS = Menu.FIRST + 1;

    private FragmentBasicNoteListBinding binding;
    private BasicNoteViewModel model;

    private BasicNoteRecycleViewAdapter mRecyclerViewAdapter;

    private InputActionHelper mInputActionHelper;

    private @NonNull BasicNoteGroupA getNoteGroup() {
        return Objects.requireNonNull(model.getBasicNoteGroup());
    }

    private @NonNull List<BasicNoteA> getNoteList() {
        return Objects.requireNonNull(model.getBasicNotes().getValue());
    }

    private void setupBottomToolbar() {
        mBottomToolbarHelper = BottomToolbarHelper.from(binding.fragmentToolbarBottom, this::processMoveMenuItemClick);
        requireActivity().getMenuInflater().inflate(R.menu.menu_listitem_bottom_move_actions, binding.fragmentToolbarBottom.getMenu());
        binding.fragmentToolbarBottom.setVisibility(View.GONE);
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

    public void onBasicNoteSelection(BasicNoteA item) {
        BasicNoteDataA noteData = model.createNoteDataFromNote(getNoteGroup(), item);
        Intent intent = switch (item.getNoteType()) {
            case BasicNoteA.NOTE_TYPE_CHECKED -> new Intent(requireActivity(), BasicNoteCheckedItemActivity.class);
            case BasicNoteA.NOTE_TYPE_NAMED -> new Intent(requireActivity(), BasicNoteNamedItemActivity.class);
            default -> null;
        };

        if (intent != null) {
            intent.putExtra(PasswordActivity.PASS_DATA, noteData);
            PasswordActivity.getPasswordValidityChecker().resetPeriod();
            startActivityForResult(intent, 0);
        }
    }

    @NonNull
    private List<BasicNoteA> getSelectedNotes() {
        return BasicEntityNoteSelectionPosA.getItemsByPositions(getNoteList(), mRecyclerViewSelector.getSelectedItems());
    }

    protected boolean processMoveMenuItemClick(MenuItem menuItem) {
        List<BasicNoteA> selectedNotes = getSelectedNotes();
        return internalProcessMoveMenuItemClick(menuItem, selectedNotes, model);
    }

    private void performDeleteAction(final ActionMode mode, final List<BasicNoteA> items) {
        AlertOkCancelSupportDialogFragment dialog = AlertOkCancelSupportDialogFragment.newAlertOkCancelDialog(
                getString(R.string.ui_question_are_you_sure));
        dialog.setOkButtonClickListener(dialog1 ->
            model.delete(items, new BasicUIFinishAction<>(mode)));

        dialog.show(getParentFragmentManager(), AlertOkCancelSupportDialogFragment.TAG);
    }

    private void performDuplicateAction(final ActionMode mode, final BasicNoteA item) {
        TextEditDialogBuilder textEditDialogBuilder = (new TextEditDialogBuilder(requireActivity(), getString(R.string.ui_note_value),
                null))
                .setNonEmptyErrorMessage(getString(R.string.error_field_not_empty))
                .setShowInput(true);

        final AlertDialog alertDialog = textEditDialogBuilder.execute();
        alertDialog.setTitle(R.string.ui_note_title);

        textEditDialogBuilder.setOnTextInputListener(text -> {
            //hide editor
            View focusedView = alertDialog.getCurrentFocus();
            InputManagerHelper.hideInput(focusedView);

            if (BasicNoteA.findByTitle(getNoteList(), text.trim()) == null) {
                model.duplicate(item, text.trim(), new BasicUIAddAction<>(mRecyclerView));
            } else {
                DisplayMessageHelper.displayInfoMessage(requireActivity(), getString(R.string.ui_error_note_already_exists, text));
            }
        });
    }

    private void performEditAction(String text) {
        List<BasicNoteA> selectedNotes = getSelectedNotes();
        BasicNoteA selectedNote;
        if ((selectedNotes.size() == 1) && (!text.equals((selectedNote = selectedNotes.get(0)).getTitle()))) {
            selectedNote.setTitle(text);
            model.edit(selectedNote, new BasicUIFinishAction<>(mRecyclerViewSelector.getActionMode()));
        }
    }

    private void performMoveToOtherNoteGroupAction(
            final ActionMode mode,
            @NonNull final List<BasicNoteA> items,
            @NonNull final BasicNoteGroupA otherNoteGroup) {
        String confirmationQuestion = getResources()
                .getQuantityString(R.plurals.ui_question_selected_note_move_to_other_note_group, items.size(), items.size(), otherNoteGroup.getDisplayTitle());
        AlertOkCancelSupportDialogFragment dialog = AlertOkCancelSupportDialogFragment.newAlertOkCancelDialog(confirmationQuestion);
        dialog.setOkButtonClickListener(dialog1 ->
            model.moveToOtherNoteGroup(items, otherNoteGroup, new BasicUIFinishAction<>(mode)));

        dialog.show(getParentFragmentManager(), AlertOkCancelSupportDialogFragment.TAG);
    }

    private void updateTitle(ActionMode mode) {
        mode.setTitle(DisplayTitleBuilder.buildItemsDisplayTitle(getActivity(), getNoteList(), mRecyclerViewSelector.getSelectedItems()));
    }

    public class ActionBarCallBack implements ActionMode.Callback {
        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            List<BasicNoteA> selectedNotes = getSelectedNotes();

            if (!selectedNotes.isEmpty()) {
                if ((item.getGroupId() == MENU_GROUP_OTHER_ITEMS) && (model.getRelatedNoteGroups().getValue() != null)) {
                    // move to other items
                    BasicNoteGroupA otherNoteGroup = model.getRelatedNoteGroups().getValue().get(item.getItemId());
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

        private void buildMoveToOtherGroupsSubMenu(Menu menu,  List<BasicNoteGroupA> relatedNoteGroups) {
            SubMenu subMenu = null;
            int order = 1;
            int relatedNoteGroupIndex = 0;

            for (BasicNoteGroupA noteGroupA : relatedNoteGroups) {
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

            model.getRelatedNoteGroups().observe(
                    BasicNoteFragment.this,
                    relatedNoteGroups ->
                        buildMoveToOtherGroupsSubMenu(menu, relatedNoteGroups));

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
        setupBottomToolbar();
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
        mInputActionHelper = new InputActionHelper(binding.addPanelInclude.getRoot());
        mInputActionHelper.setOnAddInteractionListener((actionType, text) -> {
            hideAddLayout();
            performEditAction(text);
        });

        MenuHost menuHost = requireActivity();
        menuHost.addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.menu_add_action, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.action_add) {
                    NavHostFragment.findNavController(BasicNoteFragment.this).navigate(
                            BasicNoteFragmentDirections.actionBasicNoteToBasicNoteEdit().setNoteGroup(getNoteGroup()));
                    return true;
                } else {
                    return false;
                }
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);

        model = new ViewModelProvider(this).get(BasicNoteViewModel.class);
        model.setBasicNoteGroup(BasicNoteFragmentArgs.fromBundle(getArguments()).getNoteGroup());

        final Observer<List<BasicNoteA>> notesObserver = newNotes -> {

            if (model.getBasicNotes().getValue() == null) {
                model.getBasicNotes().setValue(newNotes);

                mRecyclerViewAdapter = new BasicNoteRecycleViewAdapter(newNotes, new ActionBarCallBack(), this::onBasicNoteSelection);
                mRecyclerView.setAdapter(mRecyclerViewAdapter);
                mRecyclerViewSelector = mRecyclerViewAdapter.getRecyclerViewSelector();

                //restore selected items
                restoreSelectedItems(savedInstanceState, view);
            } else {
                if (mRecyclerViewAdapter == null) {
                    mRecyclerViewAdapter = new BasicNoteRecycleViewAdapter(newNotes, new ActionBarCallBack(), this::onBasicNoteSelection);
                    mRecyclerView.setAdapter(mRecyclerViewAdapter);
                    mRecyclerViewSelector = mRecyclerViewAdapter.getRecyclerViewSelector();
                } else {
                    mRecyclerViewAdapter.updateItems(newNotes);
                }

                UIAction<BasicNoteA> action = model.getAction();
                if (action != null) {
                    action.execute(newNotes);
                    model.resetAction();

                    DialogFragment dialogFragment = (DialogFragment)getParentFragmentManager().findFragmentByTag(AlertOkCancelSupportDialogFragment.TAG);
                    if (dialogFragment != null) {
                        dialogFragment.dismiss();
                    }
                }
            }
        };

        model.getBasicNotes().observe(this, notesObserver);

        getParentFragmentManager().setFragmentResultListener(
                BasicNoteEditFragment.RESULT_KEY, this, (requestKey, bundle) -> {
                   BasicNoteA item = bundle.getParcelable(BasicNoteEditFragment.RESULT_VALUE_KEY);
                    if (item != null) {
                        model.add(item, new BasicUIAddAction<>(mRecyclerView));
                    }
                });
    }

    public void hideAddLayout() {
        if (mInputActionHelper != null) {
            mInputActionHelper.hideLayout();
        }
    }

}
