package ru.devtron.dagturism.fragment;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.android.volley.RequestQueue;

import java.util.ArrayList;
import java.util.List;

import ru.devtron.dagturism.adapter.RecyclerAdapter;
import ru.devtron.dagturism.model.ModelPlace;

public abstract class AbstractTabFragment extends Fragment {

    private String title;
    protected Context context;
    protected View view;
    protected FragmentManager fragmentManager;

    protected List<ModelPlace> listItemsList = new ArrayList<>();
    protected RecyclerView mRecyclerView;
    protected RecyclerAdapter adapter;

    protected RequestQueue queue;

    // JSON Node names
    protected static final String TAG_SUCCESS = "success";
    protected static final String TAG_ITEMS = "items";
    protected static final String TAG_PID = "place_id";
    protected static final String TAG_NAME = "place_name";
    protected static final String TAG_CITY = "place_city";
    protected static final String TAG_IMAGES = "images";



    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
