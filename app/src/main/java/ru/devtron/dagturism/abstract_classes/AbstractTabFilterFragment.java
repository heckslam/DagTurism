package ru.devtron.dagturism.abstract_classes;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import ru.devtron.dagturism.OpenPlaceActivity;
import ru.devtron.dagturism.R;
import ru.devtron.dagturism.adapter.RecyclerAdapter;
import ru.devtron.dagturism.listener.ClickListener;
import ru.devtron.dagturism.listener.RecyclerClickListener;
import ru.devtron.dagturism.model.ModelPlace;

public abstract class AbstractTabFilterFragment extends Fragment {
    protected int success;
    private String title;
    protected Context context;
    protected View view;

    protected static final String STATE_PLACES_FILTERED = "state_places_filtered";
    protected static final String STATE_PLACES_EAT = "state_places_eat";
    protected static final String STATE_PLACES_SLEEP = "state_places_sleep";

    protected List<ModelPlace> listPlaces = new ArrayList<>();
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

    protected int numberOfColumns = 1;

    protected ProgressDialog progressDialog;
    protected TextView noPlacesTextView;

    protected String city, rest, getItemsUrl;
    protected String encodeCity, encodeRest = "";
    protected String[] splitedRest, splitedCity;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }



    protected void recyclerClickListener() {
        mRecyclerView.addOnItemTouchListener(new RecyclerClickListener(getContext(), mRecyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(getActivity(), OpenPlaceActivity.class);
                intent.putExtra(ModelPlace.class.getCanonicalName(), listPlaces.get(position));
                getActivity().startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    protected void showPD() {
        if (progressDialog == null) {
            progressDialog= new ProgressDialog(getContext());
            progressDialog.setMessage("Загрузка...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }
    }

    protected void hidePD() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    protected void setColumns() {
        if(this.getActivity().getResources().getConfiguration().orientation ==
                this.getActivity().getResources().getConfiguration().ORIENTATION_LANDSCAPE)
            numberOfColumns = 2;
        else numberOfColumns = 1;
        mRecyclerView.setLayoutManager(new GridLayoutManager
                (this.context,
                        numberOfColumns,
                        GridLayoutManager.VERTICAL, false));
    }


    protected void encodeSpaces() {
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
            System.out.println("Tried to encode URL. Bad luck, I guess");
        }
    }



    protected void updateList () {

        queue = Volley.newRequestQueue(getContext());

        adapter.clearAdapter();
        adapter = new RecyclerAdapter(getContext(), listPlaces);
        mRecyclerView.setAdapter(adapter);

        recyclerClickListener();

        showPD();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, getItemsUrl, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    success = response.getInt(TAG_SUCCESS);
                    if (success == 1) {

                        noPlacesTextView.setVisibility(View.GONE);
                        JSONArray places = response.getJSONArray(TAG_ITEMS);

                        hidePD();

                        for (int i = 0; i < places.length(); i++) {
                            JSONObject post = places.getJSONObject(i);
                            JSONArray images = post.getJSONArray(TAG_IMAGES);
                            List<String> arrayImages = new ArrayList<>();
                            for (int j = 0; j < images.length(); j++){
                                arrayImages.add(images.getString(j));

                            }

                            ModelPlace place = new ModelPlace();

                            place.setId(post.getString(TAG_PID));
                            place.setTitle(post.getString(TAG_NAME));
                            place.setCity(post.getString(TAG_CITY));
                            place.setImages(arrayImages);

                            listPlaces.add(place);
                        }

                    }

                    else {
                        hidePD();
                        noPlacesTextView.setVisibility(View.VISIBLE);
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
                noPlacesTextView.setVisibility(View.VISIBLE);
                System.out.println(error.getMessage());

                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    noPlacesTextView.setText(R.string.error_timeout);
                } else if (error instanceof AuthFailureError) {
                    noPlacesTextView.setText(R.string.error_auth);
                } else if (error instanceof ServerError) {
                    noPlacesTextView.setText(R.string.error_server);
                } else if (error instanceof NetworkError) {
                    noPlacesTextView.setText(R.string.no_network);
                } else if (error instanceof ParseError) {
                    noPlacesTextView.setText(R.string.error_server);
                }
                hidePD();
            }
        });

        queue.add(jsonObjectRequest);
    }
}


