package ru.devtron.dagturism.adapter;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;

import java.util.List;

import ru.devtron.dagturism.MySingleton;
import ru.devtron.dagturism.R;
import ru.devtron.dagturism.viewholder.RecyclerItemViewHolder;
import ru.devtron.dagturism.model.ModelPlace;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerItemViewHolder> {
    private List<ModelPlace> itemList;
    private Context mContext;
    private ImageLoader mImageLoader;
    private int focusedItem = 0;

    public RecyclerAdapter(Context context, List<ModelPlace> itemList) {
        this.itemList = itemList;
        this.mContext = context;
    }

    @Override
    public RecyclerItemViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, null);
        RecyclerItemViewHolder holder = new RecyclerItemViewHolder(v);

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(final RecyclerItemViewHolder recyclerItemViewHolder, int position) {
        ModelPlace modelPlaces = itemList.get(position);
        recyclerItemViewHolder.itemView.setSelected(focusedItem == position);

        recyclerItemViewHolder.getLayoutPosition();

        mImageLoader = MySingleton.getInstance(mContext).getImageLoader();
        try {
            recyclerItemViewHolder.id.setId(modelPlaces.getId());
            recyclerItemViewHolder.title.setText(modelPlaces.getTitle());
            recyclerItemViewHolder.city.setText(modelPlaces.getCity());
            recyclerItemViewHolder.firstImage.setImageUrl(modelPlaces.getFirstImage(), mImageLoader);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    public void clearAdapter () {
        itemList.clear();
    }

    @Override
    public int getItemCount() {
        return (null != itemList ? itemList.size() : 0 );
    }
}