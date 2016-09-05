package com.romanpulov.violetnote.view;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.model.BasicNoteDataA;
import com.romanpulov.violetnote.model.BasicNoteItemA;
import com.romanpulov.violetnote.view.core.PasswordActivity;
import com.romanpulov.violetnote.view.dummy.DummyContent.DummyItem;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnBasicNoteItemFragmentInteractionListener}
 * interface.
 */
public class BasicNoteCheckedItemFragment extends Fragment {

    private BasicNoteDataA mBasicNoteData;

    private OnBasicNoteItemFragmentInteractionListener mListener;

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

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;

            recyclerView.setLayoutManager(new LinearLayoutManager(context));

            recyclerView.setAdapter(new BasicNoteCheckedItemRecyclerViewAdapter(mBasicNoteData.getNoteList().get(0).getItems(), mListener));
        }
        return view;
    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        if (context instanceof OnBasicNoteItemFragmentInteractionListener) {
            mListener = (OnBasicNoteItemFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnBasicNoteItemFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnBasicNoteItemFragmentInteractionListener {
        void onBasicNoteItemFragmentInteraction(BasicNoteItemA item);
    }
}
