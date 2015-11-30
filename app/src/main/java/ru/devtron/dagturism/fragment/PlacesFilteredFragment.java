package ru.devtron.dagturism.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ru.devtron.dagturism.R;
import ru.devtron.dagturism.adapter.RecyclerAdapter;
import ru.devtron.dagturism.model.ModelPlace;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlacesFilteredFragment extends Fragment {
    public final static String ITEMS_COUNT_KEY = "PlacesFilteredFragment$ItemsCount";
    private static final int LAYOUT = R.layout.fragment_places_filtered;
    private RecyclerView recyclerView;
    private RecyclerAdapter recyclerAdapter;

    public static PlacesFilteredFragment createInstance(int itemsCount){
        Bundle args = new Bundle();
        args.putInt(ITEMS_COUNT_KEY, itemsCount);
        PlacesFilteredFragment placesFilteredFragment = new PlacesFilteredFragment();
        placesFilteredFragment.setArguments(args);
        return placesFilteredFragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(LAYOUT, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerViewPlacesFiltered);
        setupRecyclerView(recyclerView);
        return v;
    }

    public void setupRecyclerView(RecyclerView recyclerView){
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerAdapter = new RecyclerAdapter(createItemList());
        recyclerView.setAdapter(recyclerAdapter);
    }
    private List<ModelPlace> createItemList() {
        List<ModelPlace> itemList = new ArrayList<>();
        Bundle bundle = getArguments();
        if(bundle!=null) {
            int itemsCount = bundle.getInt(ITEMS_COUNT_KEY);
            for (int i = 0; i < itemsCount; i++) {
                itemList.add(new ModelPlace(i, "", "Саракум", "Дербент"));
            }
        }
        return itemList;

    }
}