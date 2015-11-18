package ru.devtron.dagturism.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import ru.devtron.dagturism.R;
import ru.devtron.dagturism.model.ModelPlace;

/**
 * Адаптер который позволяет читать даннные из элементов типа ModelPlace из загружать их в соответствующие элементы в листвиь(TextView и тп)
 *
 * @created 10.10.2015
 * @version $Revision 738 $
 * @author Эльвира Темирханова
 * since 0.0.1
 */

public class ModelPlaceAdapter extends BaseAdapter {
    private List<ModelPlace> list;

    private LayoutInflater layoutInflater;


    public ModelPlaceAdapter(Context context, List<ModelPlace> list) {
        this.list = list;
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.item_sight_layout, parent, false);
        }


        ModelPlace modelPlace =  getModelPlace(position);
        TextView textTitle = (TextView) view.findViewById(R.id.textView);
        TextView textDescription = (TextView) view.findViewById(R.id.textView2);
        textTitle.setText(modelPlace.getTitle());
        textDescription.setText(modelPlace.getDescription());
        return view;
    }//jhkhjkvhj
    private ModelPlace getModelPlace(int position){
        return (ModelPlace) getItem(position);
    }
}
