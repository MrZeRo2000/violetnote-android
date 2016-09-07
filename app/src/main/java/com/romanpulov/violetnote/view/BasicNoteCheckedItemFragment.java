package com.romanpulov.violetnote.view;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.db.DBBasicNoteOpenHelper;
import com.romanpulov.violetnote.db.DBNoteManager;
import com.romanpulov.violetnote.model.BasicCommonNoteA;
import com.romanpulov.violetnote.model.BasicNoteA;
import com.romanpulov.violetnote.model.BasicNoteDataA;
import com.romanpulov.violetnote.model.BasicNoteItemA;
import com.romanpulov.violetnote.view.core.AlertOkCancelDialogFragment;
import com.romanpulov.violetnote.view.core.PasswordActivity;
import com.romanpulov.violetnote.view.core.RecyclerViewHelper;
import com.romanpulov.violetnote.view.core.TextInputDialog;

public class BasicNoteCheckedItemFragment extends Fragment {

    private BasicNoteDataA mBasicNoteData;

    private OnBasicNoteItemFragmentInteractionListener mListener;

    private AddActionHelper mAddActionHelper;

    private RecyclerView mRecyclerView;
    private BasicNoteCheckedItemRecyclerViewAdapter mRecyclerViewAdapter;
    private RecyclerViewHelper.RecyclerViewSelector mRecyclerViewSelector;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public BasicNoteCheckedItemFragment() {
    }

    public static BasicNoteCheckedItemFragment newInstance(BasicNoteDataA basicNoteDataA) {
        BasicNoteCheckedItemFragment fragment = new BasicNoteCheckedItemFragment();
        Bundle args = new Bundle();
        args.putParcelable(PasswordActivity.PASS_DATA, basicNoteDataA);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mBasicNoteData = getArguments().getParcelable(PasswordActivity.PASS_DATA);
        }
    }

    private abstract class ActionExecutor {
        protected DBNoteManager mNoteManager;

        public ActionExecutor() {
            mNoteManager = new DBNoteManager(getActivity());
        }

        protected abstract boolean execute(final ActionMode mode, final BasicNoteItemA item);

        protected int executeAndReturnNewPos(final ActionMode mode, final BasicNoteItemA item) {
            if (execute(mode, item)) {
                // refresh list
                mNoteManager.queryNoteDataItems(mBasicNoteData.getNote());

                // find and return new pos of the node
                return BasicCommonNoteA.getNotePosWithId(mBasicNoteData.getNote().getItems(), item.getId());
            } else
                return -1;
        }
    }

    private class DeleteActionExecutor extends ActionExecutor {
        @Override
        protected boolean execute(final ActionMode mode, final BasicNoteItemA item) {
            AlertOkCancelDialogFragment dialog = AlertOkCancelDialogFragment.newAlertOkCancelDialog(getString(R.string.ui_question_are_you_sure));
            dialog.setOkButtonClickListener(new AlertOkCancelDialogFragment.OnClickListener() {
                @Override
                public void OnClick(DialogFragment dialog) {
                    // delete item
                    DBNoteManager mNoteManager = new DBNoteManager(getActivity());
                    mNoteManager.deleteNoteItem(item);

                    // refresh list
                    mNoteManager.queryNoteDataItems(mBasicNoteData.getNote());

                    //finish action
                    mode.finish();
                }
            });
            dialog.show(getFragmentManager(), null);

            return true;
        }
    }

    private class EditActionExecutor extends ActionExecutor {
        @Override
        protected boolean execute(final ActionMode mode, final BasicNoteItemA item) {
            final String oldTitle = item.getValue();
            TextInputDialog dialog = new TextInputDialog(getActivity(), getResources().getString(R.string.ui_note_title));
            dialog.setText(oldTitle);
            dialog.setNonEmptyErrorMessage(getString(R.string.error_field_not_empty));
            dialog.setOnTextInputListener(new TextInputDialog.OnTextInputListener() {
                @Override
                public void onTextInput(String text) {
                    if (text != null) {
                        if (!text.equals(oldTitle)) {
                            // prepare and update item
                            item.setValue(text);
                            DBNoteManager mNoteManager = new DBNoteManager(getActivity());
                            mNoteManager.updateNoteItemValue(item);

                            // refresh list
                            mNoteManager.queryNoteDataItems(mBasicNoteData.getNote());
                        }
                        // finish anyway
                        mode.finish();
                    }
                }
            });
            dialog.show();
            return true;
        }
    }

    private class MoveUpActionExecutor extends ActionExecutor {
        @Override
        protected boolean execute(ActionMode mode, BasicNoteItemA item) {
            return mNoteManager.moveUp(DBBasicNoteOpenHelper.NOTE_ITEMS_TABLE_NAME, item);
        }
    }

    private class MoveTopActionExecutor extends ActionExecutor {
        @Override
        protected boolean execute(ActionMode mode, BasicNoteItemA item) {
            return mNoteManager.moveTop(DBBasicNoteOpenHelper.NOTE_ITEMS_TABLE_NAME, item);
        }
    }

    private class MoveDownActionExecutor extends ActionExecutor {
        @Override
        protected boolean execute(ActionMode mode, BasicNoteItemA item) {
            return mNoteManager.moveDown(DBBasicNoteOpenHelper.NOTE_ITEMS_TABLE_NAME, item);
        }
    }

    private class MoveBottomActionExecutor extends ActionExecutor {
        @Override
        protected boolean execute(ActionMode mode, BasicNoteItemA item) {
            return mNoteManager.moveBottom(DBBasicNoteOpenHelper.NOTE_ITEMS_TABLE_NAME, item);
        }
    }

    private void performMoveAction(ActionExecutor executor, BasicNoteItemA item) {
        int notePos = executor.executeAndReturnNewPos(null, item);
        if (notePos != -1) {
            mRecyclerViewSelector.setSelectedView(null, notePos);
            mRecyclerView.scrollToPosition(notePos);
        }
    }

    public class ActionBarCallBack implements ActionMode.Callback {
        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            int selectedItemPos = mRecyclerViewSelector.getSelectedItemPos();
            if (selectedItemPos != -1) {
                BasicNoteItemA selectedItem = mBasicNoteData.getNote().getItems().get(selectedItemPos);
                switch (item.getItemId()) {
                    case R.id.delete:
                        (new DeleteActionExecutor()).execute(mode, selectedItem);
                        break;
                    case R.id.edit:
                        (new EditActionExecutor()).execute(mode, selectedItem);
                        break;
                    case R.id.move_up:
                        performMoveAction(new MoveUpActionExecutor(), selectedItem);
                        break;
                    case R.id.move_top:
                        performMoveAction(new MoveTopActionExecutor(), selectedItem);
                        break;
                    case R.id.move_down:
                        performMoveAction(new MoveDownActionExecutor(), selectedItem);
                        break;
                    case R.id.move_bottom:
                        performMoveAction(new MoveBottomActionExecutor(), selectedItem);
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
                mRecyclerViewSelector.finishActionMode();
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_basic_note_checked_item_list, container, false);

        Context context = view.getContext();
        mRecyclerView = (RecyclerView) view.findViewById(R.id.list);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));

        mRecyclerViewAdapter = new BasicNoteCheckedItemRecyclerViewAdapter(mBasicNoteData.getNote().getItems(), new ActionBarCallBack(),
                new OnBasicNoteItemFragmentInteractionListener() {
                    @Override
                    public void onBasicNoteItemFragmentInteraction(BasicNoteItemA item, int position) {
                        DBNoteManager manager = new DBNoteManager(getActivity());
                        //update item
                        manager.checkNoteItem(item);
                        //ensure item is updated and reload
                        BasicNoteItemA updatedItem = manager.getNoteItem(item.getId());
                        item.updateChecked(updatedItem);

                        mRecyclerView.getAdapter().notifyItemChanged(position);
                    }
                }
        );
        mRecyclerViewSelector = mRecyclerViewAdapter.getRecyclerViewSelector();
        mRecyclerView.setAdapter(mRecyclerViewAdapter);

        // add decoration
        mRecyclerView.addItemDecoration(new RecyclerViewHelper.DividerItemDecoration(getActivity(), RecyclerViewHelper.DividerItemDecoration.VERTICAL_LIST, R.drawable.divider_white_black_gradient));

        mAddActionHelper = new AddActionHelper(view.findViewById(R.id.add_panel_include), mBasicNoteData.getNote().getValues());
        mAddActionHelper.setOnAddInteractionListener(new AddActionHelper.OnAddInteractionListener() {
            @Override
            public void onAddFragmentInteraction(String text) {
                DBNoteManager manager = new DBNoteManager(getActivity());

                manager.insertNoteItem(mBasicNoteData.getNote(), BasicNoteItemA.newCheckedEditInstance(text));

                manager.queryNoteDataItems(mBasicNoteData.getNote());

                //recyclerView.getAdapter().notifyDataSetChanged();
                mRecyclerView.scrollToPosition(mBasicNoteData.getNote().getItems().size() - 1);
            }
        });

        return view;
    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void showAddLayout() {
        if (mAddActionHelper != null) {
            mAddActionHelper.showLayout();
        }
    }

    public void updateNoteDataChecked(boolean checked) {
        DBNoteManager noteManager = new DBNoteManager(getActivity());

        noteManager.updateNoteDataChecked(mBasicNoteData, checked);
        noteManager.queryNoteDataItems(mBasicNoteData.getNote());

        mRecyclerView.getAdapter().notifyDataSetChanged();
    }

    public void checkOut() {
        int checkedCount = mBasicNoteData.getCheckedCount();
        if (checkedCount > 0) {
            String queryString = String.format(getString(R.string.ui_question_are_you_sure_checkout_items), new Integer[] {checkedCount});
            AlertOkCancelDialogFragment dialog = AlertOkCancelDialogFragment.newAlertOkCancelDialog(queryString);
            dialog.setOkButtonClickListener(new AlertOkCancelDialogFragment.OnClickListener() {
                @Override
                public void OnClick(DialogFragment dialog) {
                    DBNoteManager noteManager = new DBNoteManager(getActivity());

                    noteManager.checkOut(mBasicNoteData.getNote());

                    noteManager.queryNoteDataItems(mBasicNoteData.getNote());
                    noteManager.queryNoteDataValues(mBasicNoteData.getNote());

                    //update main list
                    mRecyclerView.getAdapter().notifyDataSetChanged();
                    //update autocomplete
                    if (mAddActionHelper != null)
                        mAddActionHelper.setAutoCompleteList(mBasicNoteData.getNote().getValues());
                }
            });
            dialog.show(getFragmentManager(), null);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public interface OnBasicNoteItemFragmentInteractionListener {
        void onBasicNoteItemFragmentInteraction(BasicNoteItemA item, int position);
    }
}
