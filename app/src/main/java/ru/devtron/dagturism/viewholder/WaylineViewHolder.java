package ru.devtron.dagturism.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import ru.devtron.dagturism.R;
import ru.devtron.dagturism.timeline.WaylineView;

/**
 * Created by user on 03.01.2016.
 */
public class WaylineViewHolder extends RecyclerView.ViewHolder {
    public TextView name;
    public WaylineView mWaylineView;

    public WaylineViewHolder(View itemView, int viewType) {
        super(itemView);
        name = (TextView) itemView.findViewById(R.id.tx_name);
        mWaylineView = (WaylineView) itemView.findViewById(R.id.time_marker);
        mWaylineView.initLine(viewType);
    }

}