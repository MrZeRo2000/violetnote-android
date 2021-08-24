package com.romanpulov.violetnote.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import com.romanpulov.violetnote.databinding.FragmentPassDataCategoryBinding;
import com.romanpulov.violetnote.model.PassDataViewModel;
import com.romanpulov.violetnote.view.helper.InputManagerHelper;

public class PassDataCategoryFragment extends Fragment {

    private FragmentPassDataCategoryBinding binding;
    private PassDataViewModel model;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentPassDataCategoryBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        model = new ViewModelProvider(requireActivity()).get(PassDataViewModel.class);
        model.getPassData().observe(getViewLifecycleOwner(), passDataA -> {
            Toast.makeText(getContext(), "PassData observed:" + (passDataA == null ? "null" : passDataA.toString()), Toast.LENGTH_SHORT).show();
            if ((passDataA == null) && (model.getLoadErrorText() != null)) {
                binding.includePasswordInput.getRoot().setVisibility(View.VISIBLE);
                binding.includeIndeterminateProgress.getRoot().setVisibility(View.GONE);
            }
        });

        binding.includePasswordInput.editTextPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((v.getText().length() > 0) && (actionId == EditorInfo.IME_ACTION_GO)) {
                    Toast.makeText(getContext(), "Go:" + v.getText(), Toast.LENGTH_SHORT).show();
                    model.setPassword(v.getText().toString());
                    InputManagerHelper.hideInput(v);
                    binding.includePasswordInput.getRoot().setVisibility(View.GONE);
                    binding.includeIndeterminateProgress.getRoot().setVisibility(View.VISIBLE);
                    model.loadPassData();
                    return true;
                }
                return false;
            }
        });

        ActionBar actionBar = ((AppCompatActivity)requireActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        InputManagerHelper.focusAndShowDelayed(binding.includePasswordInput.editTextPassword);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}