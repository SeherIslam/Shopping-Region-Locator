package com.seher.shoppingregionlocator.ui.RateUs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.seher.shoppingregionlocator.R;

public class RateUsFragment extends Fragment {

    private RateUsViewModel rateUsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        rateUsViewModel =
                new ViewModelProvider(this).get(RateUsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_rateus, container, false);
        final TextView textView = root.findViewById(R.id.text_slideshow);
        rateUsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}