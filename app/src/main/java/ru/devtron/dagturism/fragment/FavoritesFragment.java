package ru.devtron.dagturism.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
 * Фрагмент который загружается в MainActivity  при нажатии на таб Карты и загружает данные в него
 * @created 10.10.2015
 * @version $Revision 738 $
 * @author Эльвира Темирханова
 * since 0.0.1
 */

public class FavoritesFragment extends AbstractTabFragment {
    private static final int LAYOUT = R.layout.fragment_maps;

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


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(LAYOUT, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        setupRecyclerView(mRecyclerView);

        return view;
    }
    private void setupRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new RecyclerAdapter(getContext(), createItemList());
        recyclerView.setAdapter(adapter);


    }
    private List<ModelPlace> createItemList() {
        List<ModelPlace> itemList = new ArrayList<>();
        Bundle bundle = getArguments();
        /*if(bundle!=null) {
            int itemsCount = bundle.getInt(ITEMS_COUNT_KEY);
            for (int i = 0; i < 10; i++) {
                itemList.add(new ModelPlace(i, "Элементы в мои", "Нарын-кала", ""));
            }
        }*/
        return itemList;
    }
}




