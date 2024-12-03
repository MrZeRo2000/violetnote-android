package com.romanpulov.violetnote.view;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.romanpulov.violetnote.databinding.FragmentBasicNoteBinding;

public class BasicNoteGroupEditFragment extends Fragment {

    public BasicNoteGroupEditFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentBasicNoteBinding binding = FragmentBasicNoteBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }
}