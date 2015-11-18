package ru.devtron.dagturism.fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ru.devtron.dagturism.R;
import ru.devtron.dagturism.model.ModelPlace;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<ModelPlace> mItemList;

    public RecyclerAdapter(List<ModelPlace> itemList) {
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
        ModelPlace modelPlace =  getSight(position);
        ModelPlace itemText = mItemList.get(position);
        holder.setItemTitle(itemText.getTitle());
        holder.setItemDecsription(itemText.getDescription());
        holder.setItemImage(itemText.getImg());

    }

    @Override
    public int getItemCount() {
        return mItemList == null ? 0 : mItemList.size();
    }
    public Object getItem(int position) {
        return mItemList.get(position);
    }
    private ModelPlace getSight(int position){
        return (ModelPlace) getItem(position);
    }
}