package ru.devtron.dagturism.fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import ru.devtron.dagturism.R;
import ru.devtron.dagturism.adapterSight.SightAdapter;
import ru.devtron.dagturism.pojo.Sight;
public class ArrayFragment extends Fragment{
    public final static String ITEMS_COUNT_KEY = "ArrayFragment$ItemsCount";
    private static final int LAYOUT = R.layout.fragment_array;
    private RecyclerView recyclerView;
    private View view;
    private ListView listView;
    public static ArrayFragment createInstance(int itemsCount){
        Bundle args = new Bundle();
        args.putInt(ITEMS_COUNT_KEY, itemsCount);
        ArrayFragment arrayFragment = new ArrayFragment();
        arrayFragment.setArguments(args); //fgfg
        return arrayFragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        recyclerView = (RecyclerView) inflater.inflate(LAYOUT, container, false);
        setupRecyclerView(recyclerView);
        return recyclerView;

    }
    public void setupRecyclerView(RecyclerView recyclerView){
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        RecyclerAdapter recyclerAdapter = new RecyclerAdapter(createItemList());
        recyclerView.setAdapter(recyclerAdapter);
    }
    private List<Sight> createItemList() {
        List<Sight> itemList = new ArrayList<>();
        Bundle bundle = getArguments();
        if(bundle!=null) {
            int itemsCount = bundle.getInt(ITEMS_COUNT_KEY);
            for (int i = 0; i < itemsCount; i++) {
                itemList.add(new Sight(i, "", "Саракум", "Нарын-кала - древняя, доарабская крепость в нагорной части Дербента, соединеная с Каспийским морем двойными стенами, призваннимы перекрывать т. н. Каспийские ворота в"));
                //itemList.add("Item " + i);
            }
        }
        return itemList;
    }
    /*private List<Sight> initData(){

        List<Sight> list = new ArrayList<>();
        list.add(new Sight(0, "", "Саракум", "Нарын-кала - древняя, доарабская крепость в нагорной части Дербента, соединеная с Каспийским морем двойными стенами, призваннимы перекрывать т. н. Каспийские ворота в"));
        list.add(new Sight(0, "", "Нарын-кала", "Нарын-кала - древняя, доарабская крепость в нагорной части Дербента, соединеная с Каспийским морем двойными стенами, призваннимы перекрывать т. н. Каспийские ворота в"));
        list.add(new Sight(0, "", "Заголовок 3", "Нарын-кала - древняя, доарабская крепость в нагорной части Дербента, соединеная с Каспийским морем двойными стенами, призваннимы перекрывать т. н. Каспийские ворота в"));
        list.add(new Sight(0, "", "Заголовок 4", "Нарын-кала - древняя, доарабская крепость в нагорной части Дербента, соединеная с Каспийским морем двойными стенами, призваннимы перекрывать т. н. Каспийские ворота в"));
        list.add(new Sight(0, "", "Заголовок 5", "Нарын-кала - древняя, доарабская крепость в нагорной части Дербента, соединеная с Каспийским морем двойными стенами, призваннимы перекрывать т. н. Каспийские ворота в"));
        list.add(new Sight(0, "", "Заголовок 6", "Нарын-кала - древняя, доарабская крепость в нагорной части Дербента, соединеная с Каспийским морем двойными стенами, призваннимы перекрывать т. н. Каспийские ворота в"));
        list.add(new Sight(0, "", "Заголовок 7", "Нарын-кала - древняя, доарабская крепость в нагорной части Дербента, соединеная с Каспийским морем двойными стенами, призваннимы перекрывать т. н. Каспийские ворота в"));
        list.add(new Sight(0, "", "Заголовок 8", "Нарын-кала - древняя, доарабская крепость в нагорной части Дербента, соединеная с Каспийским морем двойными стенами, призваннимы перекрывать т. н. Каспийские ворота в"));
        list.add(new Sight(0, "", "Заголовок 9", "Нарын-кала - древняя, доарабская крепость в нагорной части Дербента, соединеная с Каспийским морем двойными стенами, призваннимы перекрывать т. н. Каспийские ворота в"));
        list.add(new Sight(0, "", "Заголовок 10", "Нарын-кала - древняя, доарабская крепость в нагорной части Дербента, соединеная с Каспийским морем двойными стенами, призваннимы перекрывать т. н. Каспийские ворота в"));
        list.add(new Sight(0, "", "Заголовок 11", "Нарын-кала - древняя, доарабская крепость в нагорной части Дербента, соединеная с Каспийским морем двойными стенами, призваннимы перекрывать т. н. Каспийские ворота в"));
        list.add(new Sight(0, "", "Заголовок 12", "Нарын-кала - древняя, доарабская крепость в нагорной части Дербента, соединеная с Каспийским морем двойными стенами, призваннимы перекрывать т. н. Каспийские ворота в"));
        list.add(new Sight(0, "", "Заголовок 13", "Нарын-кала - древняя, доарабская крепость в нагорной части Дербента, соединеная с Каспийским морем двойными стенами, призваннимы перекрывать т. н. Каспийские ворота в"));
        list.add(new Sight(0, "", "Заголовок 14", "Нарын-кала - древняя, доарабская крепость в нагорной части Дербента, соединеная с Каспийским морем двойными стенами, призваннимы перекрывать т. н. Каспийские ворота в"));
        list.add(new Sight(0, "", "Заголовок 15", "Нарын-кала - древняя, доарабская крепость в нагорной части Дербента, соединеная с Каспийским морем двойными стенами, призваннимы перекрывать т. н. Каспийские ворота в"));
        list.add(new Sight(0, "", "Заголовок 16", "Нарын-кала - древняя, доарабская крепость в нагорной части Дербента, соединеная с Каспийским морем двойными стенами, призваннимы перекрывать т. н. Каспийские ворота в"));
        return list;
    }*/
}
