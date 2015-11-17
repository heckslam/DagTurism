package ru.devtron.dagturism.fragment;
/**
 * @author Elvira
 *
 */

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import ru.devtron.dagturism.R;

public class RecyclerItemViewHolder extends RecyclerView.ViewHolder {
    /**
     *  в этом блоке описаны компоненты из которых состоит View */

    private final ImageView imageView;
    private final TextView textTitle;
    private final TextView textDescription;

    /**
     *
     * @param parent
     * @param itemImageView
     * @param itemTitleView
     * @param itemDescriptionView
     */
    public RecyclerItemViewHolder(final View parent, ImageView itemImageView, TextView itemTitleView, TextView itemDescriptionView) {
        super(parent);
        imageView = itemImageView;
        textTitle = itemTitleView;
        textDescription = itemDescriptionView;
    }

    /**
     * конструктор крторый инициализирует переменные типа RecyclerItemViewHolder
     * @param parent
     * @return
     */
    public static RecyclerItemViewHolder newInstance(View parent) {
        ImageView itemImageView = (ImageView) parent.findViewById(R.id.imageView);
        TextView itemTitleView = (TextView) parent.findViewById(R.id.textView);
        TextView itemDescriptionView = (TextView) parent.findViewById(R.id.textView2);

        return new RecyclerItemViewHolder(parent, itemImageView, itemTitleView, itemDescriptionView);
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
        textDescription.setText(textDescripionSet);
    }

    /**
     * сетер для инициализации картинки списка
     * @param textPathImageSet
     */
    public void setItemImage(CharSequence textPathImageSet) {
       // imageView.setText(textPathImageSet);
    }

}