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


public class WhereToEat extends Fragment {

    private final static String ITEMS_COUNT_KEY = "WhereToEat$ItemsCount";

    private static final int LAYOUT = R.layout.fragment_where_to_eat;

    private List<ModelPlace> listItemsList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private RecyclerAdapter adapter;
    private ProgressDialog progressDialog;
    private TextView noPlacesTextView;

    private String city;
    private String rest;
    private String encodeCity = "";
    private String encodeRest = "";
    private String[] splitedRest;
    private String[] splitedCity;

    private RequestQueue queue;

    private String getItemsUrl;

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_ITEMS = "items";
    private static final String TAG_PID = "place_id";
    private static final String TAG_NAME = "place_name";
    private static final String TAG_CITY = "place_city";

    public WhereToEat() {
        // Required empty public constructor
    }

    public static WhereToEat createInstance(int itemsCount, String city, String rest){
        Bundle args = new Bundle();
        args.putInt(ITEMS_COUNT_KEY, itemsCount);
        args.putString("City", city);
        args.putString("Rest", rest);
        WhereToEat whereToEat = new WhereToEat();
        whereToEat.setArguments(args);
        return whereToEat;
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(LAYOUT, container, false);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.recyclerViewWhereToEat);
        noPlacesTextView = (TextView) v.findViewById(R.id.noPlaces);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);

        city = this.getArguments().getString("City");
        rest = this.getArguments().getString("Rest");

        encodeCity = "";
        encodeRest = "";

        try {
            if (rest != null && city != null) {
                splitedRest = rest.split(" ");
                splitedCity = city.split(" ");

                for (int i = 0; i < splitedCity.length; i++) {
                    if (i != splitedCity.length - 1){
                        encodeCity = encodeCity + URLEncoder.encode(splitedCity[i], "utf-8") + "%20";
                    }
                    else encodeCity = encodeCity + URLEncoder.encode(splitedCity[i], "utf-8");
                }

                for (int i = 0; i < splitedRest.length; i++) {
                    if (i != splitedRest.length - 1){
                        encodeRest = encodeRest + URLEncoder.encode(splitedRest[i], "utf-8") + "%20";
                    }
                    else encodeRest = encodeRest + URLEncoder.encode(splitedRest[i], "utf-8");
                }

            }

        }
        catch (Exception e) {
            System.out.println("Я молодец");
        }

        updateList();
        return v;
    }


    private void updateList () {

        queue = Volley.newRequestQueue(getContext());

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new RecyclerAdapter(getContext(), listItemsList);
        mRecyclerView.setAdapter(adapter);

        adapter.clearAdapter();

        showPD();

        getItemsUrl = "http://republic.tk/api/listview/filter/" + encodeCity + "/" + encodeRest + "/2";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, getItemsUrl, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    int success = response.getInt(TAG_SUCCESS);
                    if (success == 1) {

                        JSONArray places = response.getJSONArray(TAG_ITEMS);

                        hidePD();

                        for (int i = 0; i < places.length(); i++) {
                            JSONObject post = places.getJSONObject(i);
                            JSONArray images = post.getJSONArray("images");
                            List<String> arrayImages = new ArrayList<>();
                            for (int j = 0; j < images.length(); j++){
                                arrayImages.add(images.getString(j));

                            }

                            ModelPlace place = new ModelPlace();

                            place.setId(post.getInt(TAG_PID));
                            place.setTitle(post.getString(TAG_NAME));
                            place.setCity(post.getString(TAG_CITY));
                            place.setImages(arrayImages);

                            listItemsList.add(place);



                        }

                    }


                    else {
                        hidePD();
                        adapter.clearAdapter();
                        listItemsList.clear();
                        noPlacesTextView.setText(R.string.no_places_filtered);
                    }
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }

                adapter.notifyDataSetChanged();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.getMessage());
                hidePD();
            }
        });

        queue.add(jsonObjectRequest);
    }

    private void showPD() {
        if (progressDialog == null) {
            progressDialog= new ProgressDialog(getContext());
            progressDialog.setMessage("Загрузка...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }
    }

    private void hidePD() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

}
