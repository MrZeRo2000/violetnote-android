package com.romanpulov.violetnote.view;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.db.DBNoteManager;
import com.romanpulov.violetnote.model.BasicNoteValueA;
import com.romanpulov.violetnote.model.BasicNoteValueDataA;
import com.romanpulov.violetnote.view.core.BasicCommonNoteFragment;
import com.romanpulov.violetnote.view.core.RecyclerViewHelper;
import com.romanpulov.violetnote.view.helper.AddActionHelper;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnNoteValueFragmentInteractionListener}
 * interface.
 */
public class BasicNoteValueFragment extends BasicCommonNoteFragment {
    protected BasicNoteValueDataA mBasicNoteValueData;
    private AddActionHelper mAddActionHelper;

    @Override
    public void refreshList(DBNoteManager noteManager) {
        //noteManager.qu
        noteManager.queryNoteDataValuesOrdered(mBasicNoteValueData.getNote(), mBasicNoteValueData.getValues());
    }

    @Override
    public String getDBTableName() {
        return null;
    }

    private OnNoteValueFragmentInteractionListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public BasicNoteValueFragment() {
    }

    public static BasicNoteValueFragment newInstance(BasicNoteValueDataA basicNoteValueDataA) {
        BasicNoteValueFragment fragment = new BasicNoteValueFragment();

        Bundle args = new Bundle();
        args.putParcelable(BasicNoteValueDataA.class.getName(), basicNoteValueDataA);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mBasicNoteValueData = getArguments().getParcelable(BasicNoteValueDataA.class.getName());
        }
    }

    public void showAddLayout() {
        if (mAddActionHelper != null) {
            mAddActionHelper.showLayout();
        }
    }

    public class ActionBarCallBack implements ActionMode.Callback {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.menu_listitem_minimal_actions, menu);
            if (mRecyclerViewSelector.getSelectedItemPos() != -1)
                mode.setTitle(mBasicNoteValueData.getValues().get(mRecyclerViewSelector.getSelectedItemPos()).getValue());
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            int selectedItemPos = mRecyclerViewSelector.getSelectedItemPos();
            if (selectedItemPos != -1) {
                BasicNoteValueA value = mBasicNoteValueData.getValues().get(selectedItemPos);
                switch (item.getItemId()) {
                    case R.id.delete:
                        //performDeleteAction(mode, value);
                        break;
                    case R.id.edit:
                        break;
                }
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            if (mRecyclerViewSelector != null)
                mRecyclerViewSelector.destroyActionMode();
        }
    }

    private void performAddAction(BasicNoteValueA value) {
        DBNoteManager mNoteManager = new DBNoteManager(getActivity());
        if (mNoteManager.insertNoteValue(mBasicNoteValueData.getNote(), value) != -1) {
            // refresh list
            refreshList(mNoteManager);

            mRecyclerView.getAdapter().notifyDataSetChanged();

            //ensure added element is visible
            int newItemPos = - 1;
            for (int i = 0; i < mBasicNoteValueData.getValues().size(); i++) {
                if (mBasicNoteValueData.getValues().get(i).getValue().equals(value.getValue())) {
                    newItemPos = i;
                    break;
                }
            }
            if (newItemPos > -1)
                mRecyclerView.scrollToPosition(newItemPos);
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_basic_note_value_list, container, false);

        Context context = view.getContext();
        mRecyclerView = (RecyclerView) view.findViewById(R.id.list);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));

        BasicNoteValueRecyclerViewAdapter recyclerViewAdapter = new BasicNoteValueRecyclerViewAdapter(
                mBasicNoteValueData.getValues(), new ActionBarCallBack(), null
        );
        mRecyclerViewSelector = recyclerViewAdapter.getRecyclerViewSelector();
        mRecyclerView.setAdapter(recyclerViewAdapter);

        // add decoration
        mRecyclerView.addItemDecoration(new RecyclerViewHelper.DividerItemDecoration(getActivity(), RecyclerViewHelper.DividerItemDecoration.VERTICAL_LIST, R.drawable.divider_white_black_gradient));

        //add action panel
        mAddActionHelper = new AddActionHelper(view.findViewById(R.id.add_panel_include));
        mAddActionHelper.setOnAddInteractionListener(new AddActionHelper.OnAddInteractionListener() {
            @Override
            public void onAddFragmentInteraction(final String text) {
                performAddAction(BasicNoteValueA.newEditInstance(text));
            }
        });

        return view;
    }


    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        /*
        if (context instanceof OnNoteValueFragmentInteractionListener) {
            mListener = (OnNoteValueFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnNoteValueFragmentInteractionListener");
        }
        */
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnNoteValueFragmentInteractionListener {
        // TODO: Update argument type and name
        void onNoteValueClicked(BasicNoteValueA item, int adapterPosition);
    }
}
