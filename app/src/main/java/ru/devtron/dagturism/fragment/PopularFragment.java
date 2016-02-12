package ru.devtron.dagturism.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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

import ru.devtron.dagturism.R;
import ru.devtron.dagturism.Utils.Constants;
import ru.devtron.dagturism.Utils.NetworkUtil;
import ru.devtron.dagturism.abstract_classes.AbstractTabFragment;
import ru.devtron.dagturism.model.ModelPlace;

public class PopularFragment extends AbstractTabFragment {
    private static final int LAYOUT = R.layout.fragment_popular;

    private BroadcastReceiver networkStateReceiver;

    private TextView textVolleyError;

    private static final String STATE_PLACES = "state_places";

    private ProgressBar progressBar;

    private int success = 0;
    private int sizePlaces = 0;

    private static final String getItemsUrl = "http://republic.tk/api/listview/";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_ITEMS = "items";
    private static final String TAG_PID = "place_id";
    private static final String TAG_NAME = "place_name";
    private static final String TAG_CITY = "place_city";
    private static final String TAG_IMAGES = "images";
    private static final String TAG_LOCATION = "location";

    public PopularFragment() {
        // Required empty public constructor
    }

    public static PopularFragment getInstance(Context context, List<ModelPlace> list){
        Bundle args = new Bundle();
        PopularFragment popularFragment = new PopularFragment();
        args.putParcelableArrayList("listPlaces", (ArrayList<? extends Parcelable>) list);
        popularFragment.setArguments(args);
        popularFragment.setContext(context);
        popularFragment.setTitle(context.getString(R.string.tab_popular));
        return popularFragment;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(STATE_PLACES, (ArrayList<? extends Parcelable>) listPlaces);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(LAYOUT, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        progressBar = (ProgressBar) view.findViewById(R.id.progressbar);
        textVolleyError = (TextView) view.findViewById(R.id.textVolleyError);

        fragmentManager = getActivity().getSupportFragmentManager();
        initFab();

        setColumns();

        Bundle bundle = this.getArguments();

        if (savedInstanceState!=null) {
            progressBar.setVisibility(View.GONE);
            listPlaces = savedInstanceState.getParcelableArrayList(STATE_PLACES);
            setClickListenerForCards();
        }

        else if (bundle != null) {
            listPlaces = bundle.getParcelableArrayList("listPlaces");
            if (listPlaces != null) {
                sizePlaces = listPlaces.size();
                setClickListenerForCards();
            }
        }

        if (sizePlaces < 1){
            networkStateReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if (NetworkUtil.getConnectivityStatusString(context)) {
                        updateList();
                    }

                }
            };
            IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
            context.registerReceiver(networkStateReceiver, filter);
            setClickListenerForCards();
        }


        setHasOptionsMenu(true);
        return view;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.view_map_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mapmenu:
                FragmentTransaction trans = getFragmentManager()
                        .beginTransaction();
                trans.replace(R.id.root_frame, MapFragment.getInstance(context, listPlaces));
                trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                trans.commit();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(networkStateReceiver != null) {
            context.unregisterReceiver(networkStateReceiver);
        }
    }

    private void updateList () {
        progressBar.setVisibility(View.VISIBLE);
        RequestQueue queue = Volley.newRequestQueue(getContext());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, getItemsUrl, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    success = response.getInt(TAG_SUCCESS);

                    if (success == 1) {
                        textVolleyError.setVisibility(View.GONE);

                        JSONArray places = response.getJSONArray(TAG_ITEMS);

                        for (int i = 0; i < places.length(); i++) {
                            JSONObject currentPlace = places.getJSONObject(i);
                            JSONObject location = currentPlace.getJSONObject(TAG_LOCATION);
                            JSONArray images = currentPlace.getJSONArray(TAG_IMAGES);
                            List<String> arrayImages = new ArrayList<>();
                            for (int j = 0; j < images.length(); j++){
                                arrayImages.add(images.getString(j));
                            }

                            ModelPlace place = new ModelPlace();
                            place.setPlaceId(currentPlace.getString(TAG_PID));
                            place.setTitle(currentPlace.getString(TAG_NAME));
                            place.setCity(currentPlace.getString(TAG_CITY));
                            place.setImages(arrayImages);

                            place.setLat(location.getDouble(Constants.TAG_LAT));
                            place.setLng(location.getDouble(Constants.TAG_LNG));

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
