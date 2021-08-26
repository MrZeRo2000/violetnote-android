package com.romanpulov.violetnote.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.view.core.PasswordActivity;
import com.romanpulov.violetnote.view.core.RecyclerViewHelper.*;
import com.romanpulov.violetnote.model.PassDataA;
import com.romanpulov.violetnote.view.helper.SearchActionHelper;


public class NoteFragment extends Fragment {

    private static void log(String message) {
        Log.d("NoteFragment", message);
    }

    private PassDataA mPassDataA;
    private SearchActionHelper mSearchActionHelper;

    private OnPassNoteItemInteractionListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public NoteFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static NoteFragment newInstance(PassDataA passDataA) {
        log("newInstance");
        NoteFragment fragment = new NoteFragment();
        Bundle args = new Bundle();
        args.putParcelable(PasswordActivity.PASS_DATA, passDataA);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        log("onCreate getArguments = " + getArguments());

        if (getArguments() != null) {
            mPassDataA = getArguments().getParcelable(PasswordActivity.PASS_DATA);
        }
    }

    public void showSearchLayout() {
        if (mSearchActionHelper != null)
            mSearchActionHelper.showLayout();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        log("onCreateView arguments=" + getArguments());

        View view = inflater.inflate(R.layout.fragment_note_list, container, false);

        TextView headerTextView = view.findViewById(R.id.headerTextView);
        headerTextView.setText(mPassDataA.getPassCategoryData().get(0).getCategoryName());
        RecyclerView recyclerView = view.findViewById(R.id.list);
        // Set the adapter
        Context context = view.getContext();
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(new NoteRecyclerViewAdapter(mPassDataA.getPassNoteData(), mListener));

        // add decoration
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST, R.drawable.divider_orange_black_gradient));

        // setup search action helper
        mSearchActionHelper = new SearchActionHelper(view, SearchActionHelper.DISPLAY_TYPE_SYSTEM_USER);
        mSearchActionHelper.setAutoCompleteList(mPassDataA.getSearchValues(true, true));
        mSearchActionHelper.setOnSearchInteractionListener(new OnSearchInteractionListener() {
            @Override
            public void onSearchFragmentInteraction(String searchText, boolean isSearchSystem, boolean isSearchUser) {
                Intent intent = new Intent(getActivity(), SearchResultActivity.class);
                intent.putExtra(PasswordActivity.PASS_DATA, PassDataA.newSearchInstance(mPassDataA, searchText, isSearchSystem, isSearchUser));
                PasswordActivity.getPasswordValidityChecker().startPeriod();
                intent.putExtra(SearchResultActivity.SEARCH_TEXT, searchText);
                startActivityForResult(intent, 0);
            }
        });
        mSearchActionHelper.setOnSearchConditionChangedListener(new SearchActionHelper.OnSearchConditionChangedListener() {
            @Override
            public void onSearchConditionChanged(boolean isSearchSystem, boolean isSearchUser) {
                mSearchActionHelper.setAutoCompleteList(mPassDataA.getSearchValues(isSearchSystem, isSearchUser));
            }
        });

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnPassNoteItemInteractionListener) {
            mListener = (OnPassNoteItemInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnPassNoteItemInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
