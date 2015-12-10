package ru.devtron.dagturism.viewholder;
/**
 * @author Elvira
 *
 */

import android.content.ClipData;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.toolbox.NetworkImageView;

import ru.devtron.dagturism.OpenPlaceActivity;
import ru.devtron.dagturism.R;
import ru.devtron.dagturism.model.ModelPlace;

public class RecyclerItemViewHolder extends RecyclerView.ViewHolder {
    public ViewPager pager;
    public TextView id;
    public TextView title;
    public TextView city;
    public RelativeLayout relativeLayout;


    public RecyclerItemViewHolder(View view) {
        super(view);
        this.pager = (ViewPager) view.findViewById(R.id.viewPagerForImages);
        this.city = (TextView) view.findViewById(R.id.cityTextView);
        this.id = (TextView) view.findViewById(R.id.id);
        this.title = (TextView) view.findViewById(R.id.namePlace);
        this.relativeLayout = (RelativeLayout) view.findViewById(R.id.relativeLayout);
        view.setClickable(true);


    }

}