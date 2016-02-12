package ru.devtron.dagturism.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ru.devtron.dagturism.R;
import ru.devtron.dagturism.abstract_classes.AbstractTabFragment;
import ru.devtron.dagturism.model.ModelPlace;

/**
 * A simple {@link Fragment} subclass.
 */
public class RootFragment extends AbstractTabFragment {
    private String title;
    private Context context;

    private static final String STATE_PLACES = "state_places";

    private List<ModelPlace> arrayPlaces = new ArrayList<>();

    public RootFragment() {
        // Required empty public constructor
        setHasOptionsMenu(true);
    }

    public static RootFragment getInstance(Context context){
        Bundle args = new Bundle();
        RootFragment rootFragment = new RootFragment();
        rootFragment.setArguments(args);
        rootFragment.setContext(context);
        rootFragment.setTitle(context.getString(R.string.tab_popular));

        return rootFragment;
    }

    public void setContext(Context context) {
        this.context = context;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_root, container, false);


        if (savedInstanceState!=null) {
            arrayPlaces = savedInstanceState.getParcelableArrayList(STATE_PLACES);
        }

        FragmentTransaction transaction = getFragmentManager()
                .beginTransaction();
		/*
		 * When this container fragment is created, we fill it with our first
		 * "real" fragment
		 */


        transaction.replace(R.id.root_frame, PopularFragment.getInstance(context, arrayPlaces));
        transaction.commit();

        return view;
    }




}
