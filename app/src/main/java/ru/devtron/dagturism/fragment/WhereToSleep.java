package ru.devtron.dagturism.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.devtron.dagturism.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class WhereToSleep extends Fragment {

    public final static String ITEMS_COUNT_KEY = "WhereToSleep$ItemsCount";

    public static WhereToSleep createInstance(int itemsCount){
        Bundle args = new Bundle();
        args.putInt(ITEMS_COUNT_KEY, itemsCount);
        WhereToSleep whereToSleep = new WhereToSleep();
        whereToSleep.setArguments(args);
        return whereToSleep;
    }


    public WhereToSleep() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_where_to_sleep, container, false);
    }

}
