package ru.devtron.dagturism.adapter;


import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import ru.devtron.dagturism.MySingleton;
import ru.devtron.dagturism.R;

public class ImageGaleryRecyclerAdapter extends PagerAdapter {
    Context context;
    List<String> arrayImages;
    private ImageLoader mImageLoader;

    public static final int PAGER_PAGES = 10000;
    public static final int PAGER_PAGES_MIDDLE = PAGER_PAGES / 2;


    public ImageGaleryRecyclerAdapter(Context context, List<String> arrayImages){
        this.context = context;
        this.arrayImages = arrayImages;
    }



    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(context);

        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.recycler_item_image, container, false);

        mImageLoader = MySingleton.getInstance(context).getImageLoader();

        position = position - PAGER_PAGES_MIDDLE;
        int imageNumber = position % arrayImages.size();

        if (imageNumber == 0) {
            imageNumber = 0;
        } else if (imageNumber < 0) {
            imageNumber += arrayImages.size();
        }


        NetworkImageView imageView = (NetworkImageView) layout.findViewById(R.id.imageView5);
        imageView.setImageUrl(arrayImages.get(imageNumber), mImageLoader);

        container.addView(layout);
        return layout;

    }


    @Override
    public int getCount() {
        int count;
        if (arrayImages == null || arrayImages.isEmpty()) {
            count = 0;
        } else if (arrayImages.size() == 1) {
            count = 1;
        } else {
            count = PAGER_PAGES;
        }
        return count;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((FrameLayout)object);
    }


}
