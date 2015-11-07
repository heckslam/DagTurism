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

public class MapsFragment extends Fragment {
    public final static String ITEMS_COUNT_KEY = "PartThreeFragment$ItemsCount";
    private static final int LAYOUT = R.layout.fragment_maps;

    private RecyclerView recyclerView;

    public static MapsFragment createInstance(int itemsCount) {
        MapsFragment mapsFragment = new MapsFragment();
        Bundle args = new Bundle();
        args.putInt(ITEMS_COUNT_KEY, itemsCount);
        mapsFragment.setArguments(args);
        return mapsFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        recyclerView = (RecyclerView) inflater.inflate(LAYOUT, container, false);
        setupRecyclerView(recyclerView);
        return recyclerView;
    }
    private void setupRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
      //  RecyclerAdapter recyclerAdapter = new RecyclerAdapter(createItemList());
       // recyclerView.setAdapter(recyclerAdapter);
    }
    private List<String> createItemList() {
        List<String> itemList = new ArrayList<>();
        Bundle bundle = getArguments();
        if(bundle!=null) {
            int itemsCount = bundle.getInt(ITEMS_COUNT_KEY);
            for (int i = 0; i < itemsCount; i++) {
                itemList.add("Item " + i);
            }
        }
        return itemList;
    }
}




