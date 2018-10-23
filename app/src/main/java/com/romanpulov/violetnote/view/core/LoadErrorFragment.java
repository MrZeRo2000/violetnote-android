package com.romanpulov.violetnote.view.core;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.romanpulov.violetnote.R;

/**
 * Fragment displaying load error in password protected activity
 * Created by rpulov on 13.09.2016.
 */
public class LoadErrorFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_load_error, container, false);
        Button reloadButton = view.findViewById(R.id.reload);
        reloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() instanceof PasswordActivity)
                    ((PasswordActivity)getActivity()).reload();
            }
        });

        return view;
    }
}
