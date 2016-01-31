package ru.devtron.dagturism.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ru.devtron.dagturism.R;
import ru.devtron.dagturism.model.ModelNearPlace;

/**
 * Created by user on 30.01.2016.
 */
public class RecyclerAdapterEatSleep extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<ModelNearPlace> modelPlaces = new ArrayList<>();
    private Context mContext;

    public RecyclerAdapterEatSleep(Context context, List<ModelNearPlace> modelPlaces) {
        this.modelPlaces = modelPlaces;
        this.mContext = context;
    }

    public ModelNearPlace getItem(int position) {
        return modelPlaces.get(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.model_cafe, parent, false);
        TextView placeName = (TextView) v.findViewById(R.id.namePlace);
        TextView distance = (TextView) v.findViewById(R.id.distance);

        return new CafeViewHolder(v, placeName, distance);
}

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ModelNearPlace place = modelPlaces.get(position);
        holder.itemView.setEnabled(true);
        CafeViewHolder cafeViewHolder = (CafeViewHolder) holder;

        cafeViewHolder.title.setText(place.getTitle());
        cafeViewHolder.distance.setText(String.valueOf(place.getDistance() + " метров"));
    }

    @Override
    public int getItemCount() {
        return modelPlaces.size();
    }

    private class CafeViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView distance;
        public CafeViewHolder(View itemView, TextView title, TextView distance) {
            super(itemView);
            this.title = title;
            this.distance = distance;
        }
    }
}
