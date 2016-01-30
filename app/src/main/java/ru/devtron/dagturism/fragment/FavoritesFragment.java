package ru.devtron.dagturism.fragment;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import ru.devtron.dagturism.db.DBHelper;
import ru.devtron.dagturism.listener.ClickListener;
import ru.devtron.dagturism.listener.RecyclerClickListener;
import ru.devtron.dagturism.model.ModelPlace;


public class FavoritesFragment extends AbstractTabFragment {
    private static final int LAYOUT = R.layout.fragment_favorites;
    SQLiteDatabase database;

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
    }

    private void loadFromDataBase () {
        DBHelper dbHelper = new DBHelper(getContext());
        database = dbHelper.getReadableDatabase();

        Cursor cursorPlace = database.query(DBHelper.TABLE_PLACES, null, null, null, null, null, null);
        if(cursorPlace.moveToFirst()){
            do {
                ModelPlace place = new ModelPlace();
                String place_id = cursorPlace.getString(cursorPlace.getColumnIndex(DBHelper.KEY_PLACE_ID));
                place.setId(place_id);
                String title = cursorPlace.getString(cursorPlace.getColumnIndex(DBHelper.KEY_TITLE));
                place.setTitle(title);
                String city = cursorPlace.getString(cursorPlace.getColumnIndex(DBHelper.KEY_CITY));
                place.setCity(city);

                String selectImagesQuery = "SELECT  * FROM " + DBHelper.TABLE_IMAGES + " WHERE "
                        + DBHelper.KEY_PLACE_ID + " = " + place_id;

                Cursor cursorImages = database.rawQuery(selectImagesQuery, null);
                List<String> listImages = new ArrayList<>();
                if (cursorImages.moveToFirst()) {
                    do {
                        String image = cursorImages.getString((cursorImages.getColumnIndex(DBHelper.KEY_IMAGE_URL)));
                        listImages.add(image);
                    }
                    while (cursorImages.moveToNext());

                }
                cursorImages.close();

                place.setImages(listImages);

                listFavorites.add(place);
            }
            while (cursorPlace.moveToNext());
        }
        cursorPlace.close();
        database.close();
    }
}




