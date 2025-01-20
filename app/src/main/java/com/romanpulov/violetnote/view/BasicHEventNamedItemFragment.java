package com.romanpulov.violetnote.view;

import android.content.Context;
import android.os.Bundle;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.databinding.ViewRecyclerViewListBinding;
import com.romanpulov.violetnote.model.BasicHEventNamedItemViewModel;
import com.romanpulov.violetnote.model.BasicHNoteItemA;
import com.romanpulov.violetnote.view.core.RecyclerViewHelper;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class BasicHEventNamedItemFragment extends Fragment {

    private ViewRecyclerViewListBinding binding;
    BasicHEventNamedItemViewModel model;

    BasicHEventNamedItemRecyclerViewAdapter mRecyclerViewAdapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public BasicHEventNamedItemFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        // This callback is only called when MyFragment is at least started
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                model.setNoteItem(null);
                Navigation.findNavController(BasicHEventNamedItemFragment.this.requireView()).navigateUp();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ViewRecyclerViewListBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Context context = view.getContext();
        RecyclerView recyclerView = binding.getRoot();
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        // add decoration
        recyclerView.addItemDecoration(
                new RecyclerViewHelper.DividerItemDecoration(getActivity(),
                        RecyclerViewHelper.DividerItemDecoration.VERTICAL_LIST,
                        R.drawable.divider_white_black_gradient));
        recyclerView.setAdapter(mRecyclerViewAdapter);

        model = new ViewModelProvider(this).get(BasicHEventNamedItemViewModel.class);
        model.setNoteItem(BasicHEventNamedItemFragmentArgs.fromBundle(getArguments()).getItem());

        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).
                setTitle(getString(R.string.title_activity_basic_history_event_named_item,
                        model.getNoteItem().getName()));

        Observer<List<BasicHNoteItemA>> noteItemsObserver = newNoteItems -> {
            if (mRecyclerViewAdapter == null) {
                mRecyclerViewAdapter = new BasicHEventNamedItemRecyclerViewAdapter(context, newNoteItems);
                recyclerView.setAdapter(mRecyclerViewAdapter);
            }
        };
        model.getBasicHNoteItems().observe(this, noteItemsObserver);
    }
}
