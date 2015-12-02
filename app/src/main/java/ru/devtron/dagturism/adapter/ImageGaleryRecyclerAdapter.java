package ru.devtron.dagturism.adapter;


import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import ru.devtron.dagturism.MySingleton;
import ru.devtron.dagturism.R;

public class ImageGaleryRecyclerAdapter extends PagerAdapter {
    Context context;
    List<String> arrayImages;
    private ImageLoader mImageLoader;


    public ImageGaleryRecyclerAdapter(Context context, List<String> arrayImages){
        this.context = context;
        this.arrayImages = arrayImages;
    }



    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(context);

        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.recycler_item_image, container, false);



        mImageLoader = MySingleton.getInstance(context).getImageLoader();

        NetworkImageView imageView = (NetworkImageView) layout.findViewById(R.id.imageView5);
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
        return view == ((View)object);
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }


}
