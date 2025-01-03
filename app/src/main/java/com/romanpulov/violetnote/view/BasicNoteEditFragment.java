package com.romanpulov.violetnote.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.databinding.FragmentBasicNoteEditBinding;
import com.romanpulov.violetnote.model.BasicNoteA;
import com.romanpulov.violetnote.model.BasicNoteGroupA;

import java.util.Objects;

public class BasicNoteEditFragment extends Fragment {
    private final static String TAG = BasicNoteEditFragment.class.getSimpleName();

    public static final String RESULT_KEY = BasicNoteEditFragment.class.getName() + "_RESULT_KEY";
    public static final String RESULT_VALUE_KEY = BasicNoteEditFragment.class.getName() + "_RESULT_VALUE_KEY";

    private FragmentBasicNoteEditBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentBasicNoteEditBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final BasicNoteGroupA noteGroup = Objects.requireNonNull(
                BasicNoteEditFragmentArgs.fromBundle(getArguments()).getNoteGroup());

        binding.titleEditText.requestFocus();

        binding.okButton.setOnClickListener(v -> {
            if (binding.titleEditText.getText().toString().trim().isEmpty()) {
                binding.titleEditText.setError(this.getString(R.string.error_field_not_empty));
            } else {
                BasicNoteA newItem = BasicNoteA.newEditInstance(
                        noteGroup.getId(),
                        binding.noteTypeChecked.isChecked() ? 0 : 1,
                        binding.titleEditText.getText().toString().trim(),
                        binding.isEncryptedCheckBox.isChecked(),
                        null
                );

                Bundle result = new Bundle();
                result.putParcelable(RESULT_VALUE_KEY, newItem);
                BasicNoteEditFragment.this.getParentFragmentManager().setFragmentResult(RESULT_KEY, result);

                Navigation.findNavController(BasicNoteEditFragment.this.requireView()).navigateUp();
            }
        });
    }
}
