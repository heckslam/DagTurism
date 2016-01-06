package ru.devtron.dagturism.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ru.devtron.dagturism.R;
import ru.devtron.dagturism.model.WaylineModel;
import ru.devtron.dagturism.customview.WaylineView;
import ru.devtron.dagturism.viewholder.WaylineViewHolder;

/**
 * Created by user on 03.01.2016.
 */
public class WaylineAdapter extends RecyclerView.Adapter<WaylineViewHolder> {

    private List<WaylineModel> mFeedList;

    public WaylineAdapter(List<WaylineModel> feedList) {
        mFeedList = feedList;
    }

    @Override
    public int getItemViewType(int position) {
        return WaylineView.getTimeLineViewType(position, getItemCount());
    }

    @Override
    public WaylineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.item_timeline, null);
        return new WaylineViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(WaylineViewHolder holder, int position) {

        WaylineModel waylineModel = mFeedList.get(position);

        holder.name.setText(waylineModel.getPointCaption());

    }

    @Override
    public int getItemCount() {
        return (mFeedList!=null? mFeedList.size():0);
    }

}