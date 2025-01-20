package com.romanpulov.violetnote.view;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.databinding.FragmentBasicNoteNamedItemEditBinding;
import com.romanpulov.violetnote.model.BasicNoteItemA;

import java.util.Objects;

public class BasicNoteNamedItemEditFragment extends Fragment {
    private final static String TAG = BasicNoteNamedItemEditFragment.class.getSimpleName();

    public static final String RESULT_KEY = BasicNoteNamedItemEditFragment.class.getName() + "_RESULT_KEY";
    public static final String RESULT_VALUE_KEY = BasicNoteNamedItemEditFragment.class.getName() + "_RESULT_VALUE_KEY";

    private FragmentBasicNoteNamedItemEditBinding binding;

    public BasicNoteNamedItemEditFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentBasicNoteNamedItemEditBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final BasicNoteItemA editItem = BasicNoteNamedItemEditFragmentArgs.fromBundle(getArguments()).getEditItem();
        Log.d(TAG, "onViewCreated with edit item: " + editItem);

        if (editItem == null) {
            binding.nameEditText.requestFocus();
        } else {
            Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar())
                    .setTitle(getString(R.string.title_activity_basic_note_named_item_edit, editItem.getDisplayTitle()));

            binding.nameEditText.setText(editItem.getName());
            binding.valueEditText.setText(editItem.getValue());
        }

        binding.okButton.setOnClickListener(v -> {
            if (binding.nameEditText.getText().toString().trim().isEmpty()) {
                binding.nameEditText.setError(this.getString(R.string.error_field_not_empty));
            } else if (binding.valueEditText.getText().toString().trim().isEmpty()) {
                binding.valueEditText.setError(this.getString(R.string.error_field_not_empty));
            } else {
                BasicNoteItemA newItem = editItem == null ?
                        BasicNoteItemA.newNamedEditInstance(
                                binding.nameEditText.getText().toString().trim(),
                                binding.valueEditText.getText().toString().trim()) :
                        editItem
                                .withName(binding.nameEditText.getText().toString().trim())
                                .withValue(binding.valueEditText.getText().toString().trim());

                Bundle result = new Bundle();
                result.putParcelable(RESULT_VALUE_KEY, newItem);
                BasicNoteNamedItemEditFragment.this.getParentFragmentManager().setFragmentResult(RESULT_KEY, result);

                Navigation.findNavController(BasicNoteNamedItemEditFragment.this.requireView()).navigateUp();
            }
        });
    }
}