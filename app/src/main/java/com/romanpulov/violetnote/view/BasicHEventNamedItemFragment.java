package com.romanpulov.violetnote.view;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
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
import com.romanpulov.violetnote.databinding.FragmentBasicHEventNamedItemListBinding;
import com.romanpulov.violetnote.model.vm.BasicHEventNamedItemViewModel;
import com.romanpulov.violetnote.model.BasicHNoteItemA;
import com.romanpulov.violetnote.model.vm.PassUIStateViewModel;
import com.romanpulov.violetnote.view.core.RecyclerViewHelper;
import com.romanpulov.violetnote.view.helper.DisplayMessageHelper;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class BasicHEventNamedItemFragment extends Fragment {
    private static final String TAG = BasicHEventNamedItemFragment.class.getSimpleName();
    private static final int EXPIRATION_DELAY = 60;

    private FragmentBasicHEventNamedItemListBinding binding;
    BasicHEventNamedItemViewModel model;
    private PassUIStateViewModel passUIStateModel;

    BasicHEventNamedItemRecyclerViewAdapter mRecyclerViewAdapter;

    private final OnBackPressedCallback mBackPressedCallback = new OnBackPressedCallback(true /* enabled by default */) {
        @Override
        public void handleOnBackPressed() {
            // Handle the back button event
            model.setNoteItem(null);
            Navigation.findNavController(requireView()).navigateUp();
        }
    };


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
        requireActivity().getOnBackPressedDispatcher().addCallback(this, mBackPressedCallback);
    }

    @Override
    public void onDestroy() {
        mBackPressedCallback.remove();
        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentBasicHEventNamedItemListBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Context context = view.getContext();
        RecyclerView recyclerView = binding.list;
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        // add decoration
        recyclerView.addItemDecoration(
                new RecyclerViewHelper.DividerItemDecoration(getActivity(),
                        RecyclerViewHelper.DividerItemDecoration.VERTICAL_LIST,
                        R.drawable.divider_white_black_gradient));
        recyclerView.setAdapter(mRecyclerViewAdapter);

        model = new ViewModelProvider(this).get(BasicHEventNamedItemViewModel.class);

        model.setPassword(BasicHEventNamedItemFragmentArgs.fromBundle(getArguments()).getPassword());
        model.setBasicNote(BasicHEventNamedItemFragmentArgs.fromBundle(getArguments()).getNote());

        // ui state for encrypted
        if (model.getBasicNote().isEncrypted()) {
            passUIStateModel = new ViewModelProvider(this).get(PassUIStateViewModel.class);

            // set up expiration delay
            passUIStateModel.getExpireHelper().setExpirationDelay(EXPIRATION_DELAY);
            // observe expiration
            passUIStateModel.getExpireHelper().getDataExpired().observe(this, expired -> {
                Log.d(TAG, "Expiration changed to " + expired);
                if (expired) {
                    Navigation.findNavController(BasicHEventNamedItemFragment.this.requireView()).navigateUp();
                }
            });

            passUIStateModel.setUIState(PassUIStateViewModel.UI_STATE_LOADING);

            Observer<Integer> uiStateObserver = uiState -> {
                Log.d(TAG, "uiState: " + uiState);
                if (uiState == PassUIStateViewModel.UI_STATE_LOADING) {
                    binding.includeIndeterminateProgress.getRoot().setVisibility(View.VISIBLE);
                    binding.list.setVisibility(View.GONE);
                } else if (uiState == PassUIStateViewModel.UI_STATE_LOADED) {
                    passUIStateModel.getExpireHelper().initDataExpiration();

                    binding.includeIndeterminateProgress.getRoot().setVisibility(View.GONE);
                    binding.list.setVisibility(View.VISIBLE);
                }
            };
            passUIStateModel.getUIState().observe(this, uiStateObserver);
        }

        // start loading
        model.setNoteItem(BasicHEventNamedItemFragmentArgs.fromBundle(getArguments()).getItem());

        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).
                setTitle(getString(R.string.title_activity_basic_history_event_named_item,
                        model.getNoteItem().getName()));

        Observer<List<BasicHNoteItemA>> noteItemsObserver = newNoteItems -> {
            if (mRecyclerViewAdapter == null) {
                mRecyclerViewAdapter = new BasicHEventNamedItemRecyclerViewAdapter(context, newNoteItems);
                recyclerView.setAdapter(mRecyclerViewAdapter);
            }
            if (passUIStateModel != null) {
                String processError;
                if ((processError = model.getProcessError()) == null) {
                    passUIStateModel.setUIState(PassUIStateViewModel.UI_STATE_LOADED);
                } else {
                    Navigation.findNavController(requireView()).navigateUp();
                    DisplayMessageHelper.displayErrorMessage(requireActivity(), processError);
                }

                if (!newNoteItems.isEmpty()) {
                    passUIStateModel.getExpireHelper().prolongDataExpiration();
                }

            }
        };
        model.getBasicHNoteItems().observe(this, noteItemsObserver);
    }
}
