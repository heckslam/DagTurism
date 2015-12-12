package ru.devtron.dagturism.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ru.devtron.dagturism.R;
import ru.devtron.dagturism.abstract_classes.AbstractTabFilterFragment;


public class WhereToEat extends AbstractTabFilterFragment {

    private static final int LAYOUT = R.layout.fragment_where_to_eat;

    public WhereToEat() {
        // Required empty public constructor
    }

    public static WhereToEat getInstance(String city, String rest){
        Bundle args = new Bundle();
        args.putString("City", city);
        args.putString("Rest", rest);
        WhereToEat whereToEat = new WhereToEat();
        whereToEat.setArguments(args);
        return whereToEat;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(LAYOUT, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewWhereToEat);
        noPlacesTextView = (TextView) view.findViewById(R.id.noPlaces);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);

        encodeSpaces();

        getItemsUrl = "http://republic.tk/api/listview/filter/" + encodeCity + "/" + encodeRest + "/2";

        updateList();

        return view;
    }




}
