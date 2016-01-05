package ru.devtron.dagturism.viewholder;
/**
 * @author Elvira
 *
 */

import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import ru.devtron.dagturism.R;

public class RecyclerItemViewHolder extends RecyclerView.ViewHolder {
    public ViewPager pager;
    public TextView id;
    public TextView title;
    public TextView city;
    public FrameLayout frameLayout;


    public RecyclerItemViewHolder(View view) {
        super(view);
        this.pager = (ViewPager) view.findViewById(R.id.viewPagerForImages);
        this.city = (TextView) view.findViewById(R.id.cityTextView);
        this.id = (TextView) view.findViewById(R.id.id);
        this.title = (TextView) view.findViewById(R.id.namePlace);
        this.frameLayout = (FrameLayout) view.findViewById(R.id.frameLayout);


    }

}