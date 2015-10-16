package ru.devtron.dagturism.adapterListView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import ru.devtron.dagturism.R;
import ru.devtron.dagturism.pojo.Sight;

public class SightAdapter extends BaseAdapter{
    private List<Sight> list;
    private LayoutInflater layoutInflater;

    public SightAdapter(Context context, List<Sight> list) {
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


        Sight sight = (Sight) getSight(position);
        TextView textTitle = (TextView) view.findViewById(R.id.textView);
        TextView textDescription = (TextView) view.findViewById(R.id.textView2);
        textTitle.setText(sight.getTitle());
        textDescription.setText(sight.getDescrition());
        return view;
    }
    private Sight getSight(int position){
        return (Sight) getItem(position);
    }
}
