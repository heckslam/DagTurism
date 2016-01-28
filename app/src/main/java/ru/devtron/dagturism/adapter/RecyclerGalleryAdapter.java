package ru.devtron.dagturism.adapter;


import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

import java.util.List;

import ru.devtron.dagturism.Utils.MySingleton;
import ru.devtron.dagturism.OpenFullGalleryActivity;
import ru.devtron.dagturism.OpenPlaceActivity;
import ru.devtron.dagturism.R;
import ru.devtron.dagturism.model.ModelGallery;

public class RecyclerGalleryAdapter extends PagerAdapter {
    Context context;
    List<String> arrayImages;

    public static final int PAGER_PAGES = 10000;
    public static final int PAGER_PAGES_MIDDLE = PAGER_PAGES / 2;


    public RecyclerGalleryAdapter(Context context, List<String> arrayImages){
        this.context = context;
        this.arrayImages = arrayImages;
    }



    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(context);

        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.recycler_item_image, container, false);
        position = position - PAGER_PAGES_MIDDLE;

        ImageLoader mImageLoader = MySingleton.getInstance(context).getImageLoader();


        int imageNumber = position % arrayImages.size();

        if (imageNumber == 0) {
            imageNumber = 0;
        } else if (imageNumber < 0) {
            imageNumber += arrayImages.size();
        }


        final ImageView imageView = (ImageView) layout.findViewById(R.id.imageView5);
        final ContentLoadingProgressBar progressBar = (ContentLoadingProgressBar) layout.findViewById(R.id.loading);

        progressBar.setVisibility(View.VISIBLE);

        mImageLoader.get(arrayImages.get(imageNumber), new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                if (response.getBitmap() != null) {
                    imageView.setImageBitmap(response.getBitmap());
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
            }
        });


        if (context instanceof OpenPlaceActivity) {
            final int finalImageNumber = imageNumber;
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, OpenFullGalleryActivity.class);
                    ModelGallery images = new ModelGallery();
                    images.setImages(arrayImages);
                    intent.putExtra(ModelGallery.class.getCanonicalName(), images);
                    intent.putExtra("position", finalImageNumber);
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
