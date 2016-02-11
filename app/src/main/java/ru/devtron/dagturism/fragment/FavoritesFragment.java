package ru.devtron.dagturism.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ru.devtron.dagturism.OpenPlaceActivity;
import ru.devtron.dagturism.R;
import ru.devtron.dagturism.abstract_classes.AbstractTabFragment;
import ru.devtron.dagturism.adapter.RecyclerAdapter;
import ru.devtron.dagturism.listener.ClickListener;
import ru.devtron.dagturism.listener.RecyclerClickListener;
import ru.devtron.dagturism.model.ModelImages;
import ru.devtron.dagturism.model.ModelPlace;


public class FavoritesFragment extends AbstractTabFragment {
    private static final int LAYOUT = R.layout.fragment_favorites;

    List<ModelPlace> listFavorites = new ArrayList<>();

    public FavoritesFragment() {
        // Required empty public constructor
    }

    public static FavoritesFragment getInstance(Context context){
        FavoritesFragment favoritesFragment = new FavoritesFragment();
        Bundle args = new Bundle();
        favoritesFragment.setArguments(args);
        favoritesFragment.setContext(context);
        favoritesFragment.setTitle(context.getString(R.string.tab_mine));
        return favoritesFragment;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("FavoriteList", (ArrayList<? extends Parcelable>) listFavorites);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(LAYOUT, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        setColumns();

        if (savedInstanceState!=null) {
            listFavorites.clear();
            listFavorites = savedInstanceState.getParcelableArrayList("FavoriteList");
            setAdapters();
        }

        mRecyclerView.addOnItemTouchListener(new RecyclerClickListener(context, mRecyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(context, OpenPlaceActivity.class);
                intent.putExtra(ModelPlace.class.getCanonicalName(), listFavorites.get(position));
                context.startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        // Make sure that we are currently visible
        if (this.isVisible()) {
            // If we are becoming visible, then...
            if (isVisibleToUser) {
                if (listFavorites.isEmpty()) {
                    loadFromDataBase();
                    setAdapters();
                }
                else {
                    listFavorites.clear();
                    loadFromDataBase();
                    setAdapters();
                }
            }
        }
    }

    private void setAdapters() {
        adapter = new RecyclerAdapter(context, listFavorites);
        mRecyclerView.setAdapter(adapter);
    }

    private void loadFromDataBase () {
        listFavorites = ModelPlace.listAll(ModelPlace.class);

        for (ModelPlace modelPlace : listFavorites) {
            List<ModelImages> images = ModelImages.find(ModelImages.class, "place_Id = ?", modelPlace.getPlaceId());
            List<String> listImages = new ArrayList<>();
            for (ModelImages images1 : images) {
                listImages.add(images1.getUrl());
            }

            modelPlace.setImages(listImages);
        }
    }
}




