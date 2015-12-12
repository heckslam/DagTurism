package ru.devtron.dagturism.abstract_classes;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

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
import java.util.List;

import ru.devtron.dagturism.R;
import ru.devtron.dagturism.adapter.RecyclerAdapter;
import ru.devtron.dagturism.model.ModelPlace;

public abstract class AbstractTabFilterFragment extends Fragment {

    private String title;
    protected Context context;
    protected View view;

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

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new RecyclerAdapter(getContext(), listItemsList);
        adapter.clearAdapter();
        mRecyclerView.setAdapter(adapter);

        showPD();

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
                            JSONArray images = post.getJSONArray(TAG_IMAGES);
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
}
