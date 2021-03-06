package ru.devtron.dagturism.fragment;


import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import ru.devtron.dagturism.R;
import ru.devtron.dagturism.abstract_classes.AbstractTabFilterFragment;
import ru.devtron.dagturism.adapter.RecyclerAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class WhereToSleep extends AbstractTabFilterFragment {

    private static final int LAYOUT = R.layout.fragment_where_to_sleep;

    public WhereToSleep() {
        // Required empty public constructor
    }

    public static WhereToSleep getInstance(Context context, String city, String rest){
        Bundle args = new Bundle();
        args.putString("City", city);
        args.putString("Rest", rest);
        WhereToSleep whereToSleep = new WhereToSleep();
        whereToSleep.setArguments(args);
        whereToSleep.setContext(context);
        whereToSleep.setTitle(context.getString(R.string.tab_where_sleep));

        return whereToSleep;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(STATE_PLACES_SLEEP, (ArrayList<? extends Parcelable>) listPlaces);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(LAYOUT, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewWhereToSleep);
        noPlacesTextView = (TextView) view.findViewById(R.id.noPlaces);

        setColumns();

        encodeSpaces();

        getItemsUrl = "http://republic.tk/api/listview/filter/" + encodeCity + "/" + encodeRest + "/3";

        if (savedInstanceState!=null) {
            listPlaces = savedInstanceState.getParcelableArrayList(STATE_PLACES_SLEEP);
            recyclerClickListener();
            if (listPlaces.size() < 1) {
                noPlacesTextView.setVisibility(View.VISIBLE);
                noPlacesTextView.setText(R.string.no_places_filtered);
            }
        }

        else {
            listPlaces.clear();
            updateList();
        }



        return view;
    }


}


