package ru.devtron.dagturism.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.devtron.dagturism.R;

public class arrayFragment extends Fragment {
    private static final int LAYOUT = R.layout.fragment_array;

    private View view;

    public static arrayFragment getInstance() {
        Bundle args = new Bundle();
        arrayFragment fragment = new arrayFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(LAYOUT, container, false);
        return view;
    }
}
