package com.romanpulov.violetnote.view;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.view.core.PasswordActivity;
import com.romanpulov.violetnote.view.core.RecyclerViewHelper;
import com.romanpulov.violetnote.model.PassDataA;


public class SearchResultFragment extends Fragment {
    private static final String SEARCH_TEXT_DISPLAY_FORMAT = " ..%s..";

    private PassDataA mPassDataA;
    private String mSearchText;

    private String getDisplaySearchText() {
        return String.format(SEARCH_TEXT_DISPLAY_FORMAT, mSearchText);
    }

    private OnPassNoteItemInteractionListener mListener;

    public SearchResultFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param passDataA Pass Data.
     * @return A new instance of fragment SearchResultFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchResultFragment newInstance(PassDataA passDataA, String searchText) {
        SearchResultFragment fragment = new SearchResultFragment();
        Bundle args = new Bundle();
        args.putParcelable(PasswordActivity.PASS_DATA, passDataA);
        args.putString(SearchResultActivity.SEARCH_TEXT, searchText);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPassDataA = getArguments().getParcelable(PasswordActivity.PASS_DATA);
            mSearchText = getArguments().getString(SearchResultActivity.SEARCH_TEXT);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_search_result, container, false);

        View view = inflater.inflate(R.layout.fragment_note_list, container, false);

        TextView headerTextView = view.findViewById(R.id.headerTextView);
        headerTextView.setText(getDisplaySearchText());
        headerTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_search ,0, 0, 0);
        RecyclerView recyclerView = view.findViewById(R.id.list);
        // Set the adapter
        Context context = view.getContext();
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(new NoteRecyclerViewAdapter(mPassDataA.getPassNoteData(), mListener));
        // add decoration
        recyclerView.addItemDecoration(new RecyclerViewHelper.DividerItemDecoration(getActivity(), RecyclerViewHelper.DividerItemDecoration.VERTICAL_LIST, R.drawable.divider_orange_black_gradient));

        return view;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnPassNoteItemInteractionListener) {
            mListener = (OnPassNoteItemInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
