package ru.devtron.dagturism.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.melnykov.fab.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import ru.devtron.dagturism.Constants;
import ru.devtron.dagturism.NetworkUtil;
import ru.devtron.dagturism.R;
import ru.devtron.dagturism.abstract_classes.AbstractTabFragment;
import ru.devtron.dagturism.adapter.RecyclerAdapter;
import ru.devtron.dagturism.dialog.SearchPlaceDialogFragment;
import ru.devtron.dagturism.model.ModelPlace;

public class PopularFragment extends AbstractTabFragment {
    private static final int LAYOUT = R.layout.fragment_array;

    private static final String getItemsUrl = "http://republic.tk/api/listview/";
    private static final String STATE_PLACES = "state_places";

    BroadcastReceiver networkStateReceiver;

    private TextView textVolleyError;

    private int success = 0;


    public PopularFragment() {
        // Required empty public constructor
    }

    public static PopularFragment getInstance(Context context){
        Bundle args = new Bundle();
        PopularFragment popularFragment = new PopularFragment();
        popularFragment.setArguments(args);
        popularFragment.setContext(context);
        popularFragment.setTitle(context.getString(R.string.tab_popular));
        return popularFragment;
    }

    public void setContext(Context context) {
        this.context = context;
    }

 /*   @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(STATE_PLACES, (ArrayList<? extends Parcelable>) listPlaces);
    }*/

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(LAYOUT, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        textVolleyError = (TextView) view.findViewById(R.id.textVolleyError);


        /*networkStateReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                checkConnection(NetworkUtil.getConnectivityStatusString(context));
            }
        };
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        context.registerReceiver(networkStateReceiver, filter);*/


        fragmentManager = getActivity().getSupportFragmentManager();

        initFab();

        updateList();

        return view;
    }




/*
    @Override
    public void onDestroy() {
        super.onDestroy();
        if(networkStateReceiver != null) {
            context.unregisterReceiver(networkStateReceiver);
        }
    }
*/

    private void initFab() {
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment searchPlaceDialogFragment = new SearchPlaceDialogFragment();
                searchPlaceDialogFragment.show(fragmentManager, "SearchPlaceDialogFragment");
            }
        });

        fab.attachToRecyclerView(mRecyclerView);
    }

    private void checkConnection(boolean IsConnected) {
        /*if (IsConnected) {
            updateList();
        }
        else  {
            Toast toast = Toast.makeText(context,
                    R.string.no_network, Toast.LENGTH_LONG);
            toast.show();
        }*/
    }


    private void updateList () {

        queue = Volley.newRequestQueue(getContext());

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new RecyclerAdapter(getContext(), listPlaces);
        mRecyclerView.setAdapter(adapter);

//        adapter.clearAdapter();


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, getItemsUrl, new Response.Listener<JSONObject>() {
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
                System.out.println(error.getMessage());

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
