package ru.devtron.dagturism.adapter;


import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import ru.devtron.dagturism.Utils.MySingleton;
import ru.devtron.dagturism.OpenFullGalleryActivity;
import ru.devtron.dagturism.OpenPlaceActivity;
import ru.devtron.dagturism.R;
import ru.devtron.dagturism.model.ModelImages;

public class ImageGaleryRecyclerAdapter extends PagerAdapter {
    Context context;
    List<String> arrayImages;
    private ImageLoader mImageLoader;
    private ViewGroup layout;

    public static final int PAGER_PAGES = 10000;
    public static final int PAGER_PAGES_MIDDLE = PAGER_PAGES / 2;


    public ImageGaleryRecyclerAdapter(Context context, List<String> arrayImages){
        this.context = context;
        this.arrayImages = arrayImages;
    }



    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(context);

        layout = (ViewGroup) inflater.inflate(R.layout.recycler_item_image, container, false);
        position = position - PAGER_PAGES_MIDDLE;

        mImageLoader = MySingleton.getInstance(context).getImageLoader();


        int imageNumber = position % arrayImages.size();

        if (imageNumber == 0) {
            imageNumber = 0;
        } else if (imageNumber < 0) {
            imageNumber += arrayImages.size();
        }


        NetworkImageView imageView = (NetworkImageView) layout.findViewById(R.id.imageView5);
        imageView.setImageUrl(arrayImages.get(imageNumber), mImageLoader);

        if (context instanceof OpenPlaceActivity) {
            final int finalPosition = position;
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, OpenFullGalleryActivity.class);
                    ModelImages images = new ModelImages();
                    images.setImages(arrayImages);
                    intent.putExtra(ModelImages.class.getCanonicalName(), images);
                    intent.putExtra("position", finalPosition);
                    context.startActivity(intent);
                }
            });
        }

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
