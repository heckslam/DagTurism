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
public class WhereToEat extends Fragment {

    public final static String ITEMS_COUNT_KEY = "WhereToEat$ItemsCount";

    public static WhereToEat createInstance(int itemsCount){
        Bundle args = new Bundle();
        args.putInt(ITEMS_COUNT_KEY, itemsCount);
        WhereToEat whereToEat = new WhereToEat();
        whereToEat.setArguments(args);
        return whereToEat;
    }


    public WhereToEat() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_where_to_eat, container, false);
    }

}
