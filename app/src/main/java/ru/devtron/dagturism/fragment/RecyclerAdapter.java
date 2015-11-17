package ru.devtron.dagturism.fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ru.devtron.dagturism.R;
import ru.devtron.dagturism.pojo.Sight;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Sight> mItemList;

    public RecyclerAdapter(List<Sight> itemList) {
        mItemList = itemList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
        return RecyclerItemViewHolder.newInstance(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        RecyclerItemViewHolder holder = (RecyclerItemViewHolder) viewHolder;
        Sight sight =  getSight(position);
        Sight itemText = mItemList.get(position);
        holder.setItemTitle(itemText.getTitle());
        holder.setItemDecsription(itemText.getDescrition());
        holder.setItemImage(itemText.getImg());

    }

    @Override
    public int getItemCount() {
        return mItemList == null ? 0 : mItemList.size();
    }
    public Object getItem(int position) {
        return mItemList.get(position);
    }
    private Sight getSight(int position){
        return (Sight) getItem(position);
    }
}