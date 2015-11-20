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
 * Фрагмент который загружается в MainActivity  при нажатии на таб Карты и загружает данные в него
 * @created 10.10.2015
 * @version $Revision 738 $
 * @author Эльвира Темирханова
 * since 0.0.1
 */

public class MapsFragment extends Fragment {
    public final static String ITEMS_COUNT_KEY = "PartThreeFragment$ItemsCount";
    private static final int LAYOUT = R.layout.fragment_maps;

    private RecyclerView recyclerView;
    private ru.devtron.dagturism.adapter.RecyclerAdapter recyclerAdapter;

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
        View v = inflater.inflate(LAYOUT, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        setupRecyclerView(recyclerView);
        return recyclerView;
    }
    private void setupRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerAdapter = new RecyclerAdapter(createItemList());
        recyclerView.setAdapter(recyclerAdapter);

    }
    private List<ModelPlace> createItemList() {
        List<ModelPlace> itemList = new ArrayList<>();
        Bundle bundle = getArguments();
        if(bundle!=null) {
            int itemsCount = bundle.getInt(ITEMS_COUNT_KEY);
            for (int i = 0; i < 10; i++) {
                itemList.add(new ModelPlace(i, "", "Элементы в мои", "Нарын-кала - древняя, в моив моив моив моив моив моив моив моив моив моив моив моив моив моив моив моив моив моив моив моив моив моив моив моив моив моив моив моив моив моив моив моив моив моив мои перекрывать т. н. Каспийские ворота в"));
            }
        }
        return itemList;
    }
}




