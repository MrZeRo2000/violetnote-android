package com.romanpulov.violetnote.view;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.view.core.PasswordActivity;
import com.romanpulov.violetnote.view.core.RecyclerViewHelper.*;
import com.romanpulov.violetnote.model.PassCategoryA;
import com.romanpulov.violetnote.model.PassDataA;
import com.romanpulov.violetnote.view.helper.SearchActionHelper;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnPassCategoryInteractionListener}
 * interface.
 */
public class CategoryFragment extends Fragment {

    private OnPassCategoryInteractionListener mListener;
    private OnSearchInteractionListener mSearchListener;
    private SearchActionHelper mSearchActionHelper;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public CategoryFragment() {

    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static CategoryFragment newInstance(PassDataA passDataA) {
        CategoryFragment fragment = new CategoryFragment();
        Bundle args = new Bundle();
        args.putParcelable(PasswordActivity.PASS_DATA, passDataA);
        fragment.setArguments(args);
        return fragment;
    }

    public void showSearchLayout() {
        if (mSearchActionHelper != null)
            mSearchActionHelper.showLayout();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category_list, container, false);
        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.list);

        // Set the adapter
        Context context = view.getContext();
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        final PassDataA passDataA = this.getArguments().getParcelable(PasswordActivity.PASS_DATA);
        if ((passDataA != null) && (passDataA.getPassCategoryData() != null))
            recyclerView.setAdapter(new CategoryRecyclerViewAdapter(passDataA.getPassCategoryData(), mListener));

        // add decoration
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST, R.drawable.divider_white_black_gradient));

        // setup search action helper
        mSearchActionHelper = new SearchActionHelper(view, SearchActionHelper.DISPLAY_TYPE_SYSTEM_USER);
        mSearchActionHelper.setOnSearchInteractionListener(mSearchListener);
        if (passDataA != null)
            mSearchActionHelper.setAutoCompleteList(passDataA.getSearchValues(true, true));
        mSearchActionHelper.setOnSearchConditionChangedListeber(new SearchActionHelper.OnSearchConditionChangedListener() {
            @Override
            public void onSearchConditionChanged(boolean isSearchSystem, boolean isSearchUser) {
                if (passDataA != null)
                    mSearchActionHelper.setAutoCompleteList(passDataA.getSearchValues(isSearchSystem, isSearchUser));
            }
        });

        return view;
    }

    @Override
    public void onPause() {
        if (mSearchActionHelper != null)
            mSearchActionHelper.hideLayout();
        super.onPause();
    }

    @Override
    public void onAttach(android.app.Activity context) {
        super.onAttach(context);
        if (context instanceof OnPassCategoryInteractionListener) {
            mListener = (OnPassCategoryInteractionListener) context;
        }
        else {
            throw new RuntimeException(context.toString()
                    + " must implement OnPassNoteItemInteractionListener");
        }
        if (context instanceof OnSearchInteractionListener) {
            mSearchListener = (OnSearchInteractionListener) context;
        }
        else {
            throw new RuntimeException(context.toString()
                    + " must implement OnSearchInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnPassCategoryInteractionListener {
        void onPassCategorySelection(PassCategoryA item);
    }
}
