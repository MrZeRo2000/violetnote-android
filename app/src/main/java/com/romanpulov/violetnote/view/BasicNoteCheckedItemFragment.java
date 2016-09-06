package com.romanpulov.violetnote.view;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.db.DBNoteManager;
import com.romanpulov.violetnote.model.BasicNoteA;
import com.romanpulov.violetnote.model.BasicNoteDataA;
import com.romanpulov.violetnote.model.BasicNoteItemA;
import com.romanpulov.violetnote.view.core.AlertOkCancelDialogFragment;
import com.romanpulov.violetnote.view.core.PasswordActivity;
import com.romanpulov.violetnote.view.core.RecyclerViewHelper;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnBasicNoteItemFragmentInteractionListener}
 * interface.
 */
public class BasicNoteCheckedItemFragment extends Fragment {

    private BasicNoteDataA mBasicNoteData;

    private OnBasicNoteItemFragmentInteractionListener mListener;

    private AddActionHelper mAddActionHelper;

    private RecyclerView mRecyclerView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public BasicNoteCheckedItemFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_basic_note_checked_item_list, container, false);

        Context context = view.getContext();
        mRecyclerView = (RecyclerView) view.findViewById(R.id.list);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));

        mRecyclerView.setAdapter(new BasicNoteCheckedItemRecyclerViewAdapter(mBasicNoteData.getNote().getItems(),
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
        ));

        // add decoration
        mRecyclerView.addItemDecoration(new RecyclerViewHelper.DividerItemDecoration(getActivity(), RecyclerViewHelper.DividerItemDecoration.VERTICAL_LIST, R.drawable.divider_white_black_gradient));

        mAddActionHelper = new AddActionHelper(view.findViewById(R.id.add_panel_include));
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
                    mRecyclerView.getAdapter().notifyDataSetChanged();

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
