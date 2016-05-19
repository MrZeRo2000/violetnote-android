package com.romanpulov.violetnote;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.romanpulov.violetnote.RecyclerViewHelper.*;

import java.util.ArrayList;


/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class NoteFragment extends Fragment {

    private static void log(String message) {
        Log.d("NoteFragment", message);
    }


    private PassCategoryA mPassCategory;
    private ArrayList<PassNoteA> mPassNoteData;

    private OnListFragmentInteractionListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public NoteFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static NoteFragment newInstance(PassCategoryA passCategory, ArrayList<PassNoteA> passNoteData) {
        log("newInstance");
        NoteFragment fragment = new NoteFragment();
        Bundle args = new Bundle();
        args.putParcelable(NoteActivity.PASS_CATEGORY_ITEM, passCategory);
        args.putParcelableArrayList(NoteActivity.PASS_NOTE_DATA, passNoteData);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        log("onCreate getArguments = " + getArguments());

        if (getArguments() != null) {
            mPassCategory =getArguments().getParcelable(NoteActivity.PASS_CATEGORY_ITEM);
            mPassNoteData = getArguments().getParcelableArrayList(NoteActivity.PASS_NOTE_DATA);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        log("onCreateView arguments=" + getArguments());

        View view = inflater.inflate(R.layout.fragment_note_list, container, false);

        TextView headerTextView = (TextView)view.findViewById(R.id.headerTextView);
        headerTextView.setText(mPassCategory.getCategoryName());
        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.list);
        // Set the adapter
        Context context = view.getContext();
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(new NoteRecyclerViewAdapter(mPassNoteData, mListener));
        // add decoration
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST, R.drawable.divider_orange_black_gradient));

        return view;
    }


    @Override
    public void onAttach(android.app.Activity context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
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
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(PassNoteA item);
    }

    @Override
    public void onPause() {
        super.onPause();
        getArguments().putParcelable(NoteActivity.PASS_CATEGORY_ITEM, mPassCategory);
        getArguments().putParcelableArrayList(NoteActivity.PASS_NOTE_DATA, mPassNoteData);
    }
}
