package ru.devtron.dagturism;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
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

import java.util.ArrayList;
import java.util.List;

import ru.devtron.dagturism.Utils.Constants;
import ru.devtron.dagturism.Utils.NetworkUtil;
import ru.devtron.dagturism.abstract_classes.AbstractMethodsActivity;
import ru.devtron.dagturism.adapter.RecyclerAdapter;
import ru.devtron.dagturism.listener.ClickListener;
import ru.devtron.dagturism.listener.RecyclerClickListener;
import ru.devtron.dagturism.model.ModelPlace;

/**
 * Активность категорий
 *
 * @created 10.10.2015
 * @version $Revision 738 $
 * @author AlievRuslan
 * since 0.0.1
 */

public class ViewListPlacesActivity extends AbstractMethodsActivity {
    private List<ModelPlace> listPlaces = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private RecyclerAdapter adapter;

    private static final String getItemsUrl = "http://republic.tk/api/listview/";
    private static final String STATE_PLACES = "state_places";
    private String getCategoryUrl;

    private TextView textVolleyError;
    private ProgressBar progressBar;

    private int success = 0;

    protected RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        settingTheme();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_list_places);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        textVolleyError = (TextView) findViewById(R.id.textVolleyError);
        initToolbar();
        initNavigationView();
        setColumns();

        String selectedItem = String.valueOf(getIntent().getIntExtra("selectedItem", 1));
        getCategoryUrl = getItemsUrl  + selectedItem;

        if (savedInstanceState!=null) {
            progressBar.setVisibility(View.GONE);
            listPlaces = savedInstanceState.getParcelableArrayList(STATE_PLACES);
        }
        else {
            listPlaces.clear();
            updateList();
        }
        setClickListenerForCards();


    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (toolbar != null) {
            toolbar.setTitleTextColor(Color.WHITE);
            setSupportActionBar(toolbar);
            toolbar.setTitle(R.string.app_name);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setColumns() {
        int numberOfColumns = 1;
        if(getResources().getConfiguration().orientation ==
                getResources().getConfiguration().ORIENTATION_LANDSCAPE)
            numberOfColumns = 2;
        else numberOfColumns = 1;
        mRecyclerView.setLayoutManager(new GridLayoutManager
                (this, numberOfColumns,
                        GridLayoutManager.VERTICAL, false));
    }

    protected void setClickListenerForCards() {
        adapter = new RecyclerAdapter(this, listPlaces);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.addOnItemTouchListener(new RecyclerClickListener(this, mRecyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(ViewListPlacesActivity.this, OpenPlaceActivity.class);
                intent.putExtra(ModelPlace.class.getCanonicalName(), listPlaces.get(position));
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    protected void updateList () {
        progressBar.setVisibility(View.VISIBLE);
        queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, getCategoryUrl, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    success = response.getInt(TAG_SUCCESS);
                    if (success == 1) {

                        String title = Constants.NA;
                        String id = Constants.NA;
                        String city = Constants.NA;

                        textVolleyError.setVisibility(View.GONE);

                        JSONArray places = response.getJSONArray(TAG_ITEMS);



                        for (int i = 0; i < places.length(); i++) {
                            JSONObject currentPlace = places.getJSONObject(i);
                            JSONArray images = currentPlace.getJSONArray(TAG_IMAGES);
                            List<String> arrayImages = new ArrayList<>();
                            for (int j = 0; j < images.length(); j++){
                                arrayImages.add(images.getString(j));
                            }

                            ModelPlace place = new ModelPlace();

                            id = currentPlace.getString(TAG_PID);
                            title = currentPlace.getString(TAG_NAME);
                            city = currentPlace.getString(TAG_CITY);


                            place.setId(id);
                            place.setTitle(title);
                            place.setCity(city);
                            place.setImages(arrayImages);

                            listPlaces.add(place);
                        }
                    }

                    progressBar.setVisibility(View.GONE);
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }

                adapter.notifyDataSetChanged();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                textVolleyError.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    textVolleyError.setText(R.string.error_timeout);
                } else if (error instanceof AuthFailureError) {
                    textVolleyError.setText(R.string.error_auth);
                } else if (error instanceof ServerError) {
                    textVolleyError.setText(R.string.error_server);
                } else if (error instanceof NetworkError) {
                    textVolleyError.setText(R.string.no_network);
                } else if (error instanceof ParseError) {
                    textVolleyError.setText(R.string.error_server);
                }
            }
        });

        queue.add(jsonObjectRequest);

    }



}
