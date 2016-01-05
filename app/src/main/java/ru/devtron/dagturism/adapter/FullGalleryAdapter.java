package ru.devtron.dagturism.adapter;


import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

import java.util.List;

import ru.devtron.dagturism.Utils.MySingleton;
import ru.devtron.dagturism.R;
import ru.devtron.dagturism.listener.TouchImageView;

public class FullGalleryAdapter extends PagerAdapter {
    Context context;
    List<String> arrayImages;

    public FullGalleryAdapter(Context context, List<String> arrayImages){
        this.context = context;
        this.arrayImages = arrayImages;
    }



    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(context);
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.full_gallery_item, container, false);

        ImageLoader mImageLoader = MySingleton.getInstance(context).getImageLoader();


        final TouchImageView imageView = (TouchImageView) layout.findViewById(R.id.fullscreen_image);
        final ContentLoadingProgressBar progressBar = (ContentLoadingProgressBar) layout.findViewById(R.id.loading);

        progressBar.setVisibility(View.VISIBLE);

        mImageLoader.get(arrayImages.get(position), new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                imageView.setImageBitmap(response.getBitmap());
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
            }
        });




        container.addView(layout);
        return layout;
    }


    @Override
    public int getCount() {
        return arrayImages.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);
    }


}
