package ru.devtron.dagturism.fragment;
/**
 * @author Elvira
 *
 */

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;


import com.android.volley.toolbox.NetworkImageView;

import ru.devtron.dagturism.R;

public class RecyclerItemViewHolder extends RecyclerView.ViewHolder {
    private static TextView itemCityView;
    /**
     *  в этом блоке описаны компоненты из которых состоит View */
    private final NetworkImageView imageView;
    private final TextView textTitle;
    private final TextView textCity;
    private final TextView url;

    /**
     *
     * @param parent
     * @param itemImageView
     * @param itemTitleView
     * @param itemDescriptionView
     */
    public RecyclerItemViewHolder(final View parent, NetworkImageView itemImageView, TextView itemTitleView, TextView itemDescriptionView, TextView url) {
        super(parent);
        imageView = itemImageView;
        textTitle = itemTitleView;
        textCity = itemDescriptionView;
        this.url = url;
    }





    /**
     * конструктор крторый инициализирует переменные типа RecyclerItemViewHolder
     * @param parent
     * @return
     */
    public static RecyclerItemViewHolder newInstance(View parent) {
        NetworkImageView itemImageView = (NetworkImageView) parent.findViewById(R.id.imageView);
        TextView itemTitleView = (TextView) parent.findViewById(R.id.namePlace);
        itemCityView = (TextView) parent.findViewById(R.id.cityTextView);
        TextView url = (TextView) parent.findViewById(R.id.url);


        return new RecyclerItemViewHolder(parent, itemImageView, itemTitleView, itemCityView, url);
    }



    /**
     * сетер для инициализации заголовка
     * @param textTitleSet
     */
    public void setItemTitle(CharSequence textTitleSet) {
        textTitle.setText(textTitleSet);
    }

    /**
     * сетер для инициализации описания в списке
     * @param textDescripionSet
     */
    public void setItemDecsription(CharSequence textDescripionSet) {
        textCity.setText(textDescripionSet);
    }

    /**
     * сетер для инициализации картинки списка
     * @param textPathImageSet
     */
    public void setItemImage(CharSequence textPathImageSet) {
       // imageView.setText(textPathImageSet);
    }

}