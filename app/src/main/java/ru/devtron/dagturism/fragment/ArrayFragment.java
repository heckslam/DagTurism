package ru.devtron.dagturism.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.melnykov.fab.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ru.devtron.dagturism.R;
import ru.devtron.dagturism.adapter.RecyclerAdapter;
import ru.devtron.dagturism.dialog.SearchPlaceDialogFragment;
import ru.devtron.dagturism.model.ModelPlace;

public class ArrayFragment extends Fragment  {
    private final static String ITEMS_COUNT_KEY = "ArrayFragment$ItemsCount";
    private static final int LAYOUT = R.layout.fragment_array;

    FragmentManager fragmentManager;

    private static final String TAG = "MyRecyclerList";
    private List<ModelPlace> listItemsList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private RecyclerAdapter adapter;

    private RequestQueue queue;

    private static final String getItemsUrl = "http://republic.tk/api/listview/";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_ITEMS = "items";
    private static final String TAG_PID = "place_id";
    private static final String TAG_NAME = "place_name";
    private static final String TAG_CITY = "place_city";


    public ArrayFragment() {
        // Required empty public constructor
    }

    public static ArrayFragment createInstance(int itemsCount){
        Bundle args = new Bundle();
        args.putInt(ITEMS_COUNT_KEY, itemsCount);
        ArrayFragment arrayFragment = new ArrayFragment();
        arrayFragment.setArguments(args);
        return arrayFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(LAYOUT, container, false);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);

        fragmentManager = getActivity().getSupportFragmentManager();

        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment searchPlaceDialogFragment = new SearchPlaceDialogFragment();
                searchPlaceDialogFragment.show(fragmentManager, "SearchPlaceDialogFragment");
            }
        });

        fab.attachToRecyclerView(mRecyclerView);

        updateList();

        return v;
    }


    private void updateList () {

        queue = Volley.newRequestQueue(getContext());

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new RecyclerAdapter(getContext(), listItemsList);
        mRecyclerView.setAdapter(adapter);

        adapter.clearAdapter();


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, getItemsUrl, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    int success = response.getInt(TAG_SUCCESS);
                    if (success == 1) {

                        JSONArray places = response.getJSONArray(TAG_ITEMS);


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
            }
        });

        queue.add(jsonObjectRequest);
    }

}
