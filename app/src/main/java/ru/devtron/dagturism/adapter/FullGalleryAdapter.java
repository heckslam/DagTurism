package ru.devtron.dagturism.adapter;


import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.toolbox.ImageLoader;

import java.util.List;

import ru.devtron.dagturism.Utils.MySingleton;
import ru.devtron.dagturism.R;
import ru.devtron.dagturism.listener.NetworkImageView;

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


        NetworkImageView imageView = (NetworkImageView) layout.findViewById(R.id.fullscreen_image);
        imageView.setImageUrl(arrayImages.get(position), mImageLoader);

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
