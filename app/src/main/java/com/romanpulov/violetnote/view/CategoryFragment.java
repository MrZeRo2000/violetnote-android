package com.romanpulov.violetnote.view;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.view.core.PasswordActivity;
import com.romanpulov.violetnote.view.core.RecyclerViewHelper.*;
import com.romanpulov.violetnote.model.PassCategoryA;
import com.romanpulov.violetnote.model.PassDataA;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class CategoryFragment extends Fragment {

    private OnListFragmentInteractionListener mListener;
    private OnSearchFragmentInteractionListener mSearchListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public CategoryFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static CategoryFragment newInstance(PassDataA passDataA) {
        Log.d("CategoryFragment", "newInstance");
        CategoryFragment fragment = new CategoryFragment();
        Bundle args = new Bundle();
        args.putParcelable(PasswordActivity.PASS_DATA, passDataA);
        fragment.setArguments(args);
        return fragment;
    }

    public void showSearchLayout() {
        View view = getView();
        if (view != null) {
            View searchView = view.findViewById(R.id.search_layout);
            if (searchView != null) {
                searchView.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category_list, container, false);
        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.list);

        // Set the adapter
        Context context = view.getContext();
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        PassDataA passDataA = this.getArguments().getParcelable(PasswordActivity.PASS_DATA);
        if ((passDataA != null) && (passDataA.getPassCategoryData() != null))
            recyclerView.setAdapter(new CategoryRecyclerViewAdapter(getActivity(), passDataA.getPassCategoryData(), mListener));
        // add decoration
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST, R.drawable.divider_white_black_gradient));

        final View searchView = view.findViewById(R.id.search_layout);

        Button searchCancelButton = (Button)view.findViewById(R.id.cancel_button);
        searchCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchView.setVisibility(View.GONE);
            }
        });

        final EditText searchEditText = (EditText)view.findViewById(R.id.search_edit_text);
        searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    searchView.setVisibility(View.GONE);
                    mSearchListener.onSearchFragmentInteraction(textView.getText().toString());
                    //clear search text for future
                    searchEditText.setText(null);
                }
                return false;
            }
        });

        return view;
    }


    @Override
    public void onAttach(android.app.Activity context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        }
        else {
            throw new RuntimeException(context.toString()
                    + " must implement OnPassNoteItemInteractionListener");
        }
        if (context instanceof OnSearchFragmentInteractionListener) {
            mSearchListener = (OnSearchFragmentInteractionListener) context;
        }
        else {
            throw new RuntimeException(context.toString()
                    + " must implement OnSearchFragmentInteractionListener");
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(PassCategoryA item);
    }

    public interface OnSearchFragmentInteractionListener {
        void onSearchFragmentInteraction(String searchText);
    }
}
