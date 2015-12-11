package ru.devtron.dagturism.fragment;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.devtron.dagturism.R;
import ru.devtron.dagturism.adapter.RecyclerAdapter;
import ru.devtron.dagturism.model.ModelPlace;


public class PlacesFilteredFragment extends AbstractTabFilterFragment {

    private static final int LAYOUT = R.layout.fragment_places_filtered;


    public PlacesFilteredFragment() {
        // Required empty public constructor
    }


    public static PlacesFilteredFragment getInstance(String city, String rest){
        Bundle args = new Bundle();
        args.putString("City", city);
        args.putString("Rest", rest);
        PlacesFilteredFragment placesFilteredFragment = new PlacesFilteredFragment();
        placesFilteredFragment.setArguments(args);
        return placesFilteredFragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(LAYOUT, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewPlacesFiltered);
        noPlacesTextView = (TextView) view.findViewById(R.id.noPlaces);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);


        encodeSpaces();

        getItemsUrl = "http://republic.tk/api/listview/filter/" + encodeCity + "/" + encodeRest + "/1";

        updateList();
        return view;
    }




}
