package ru.devtron.dagturism.fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.devtron.dagturism.R;
import ru.devtron.dagturism.adapter.RecyclerAdapter;
import ru.devtron.dagturism.model.ModelPlace;

public class ArrayFragment extends Fragment {
    public final static String ITEMS_COUNT_KEY = "ArrayFragment$ItemsCount";
    private static final int LAYOUT = R.layout.fragment_array;

    public static final String TAG = "MyRecyclerList";
    private List<ModelPlace> listItemsList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private RecyclerAdapter adapter;
    private ProgressDialog progressDialog;

    RequestQueue queue;


    private final String getItemsUrl = "http://republic.tk/index.php/api/";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_ITEMS = "items";
    private static final String TAG_PID = "place_id";
    private static final String TAG_NAME = "place_name";
    private static final String TAG_URL = "place_desc";

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
        updateList();
        return v;


    }



    private void updateList () {

        queue = Volley.newRequestQueue(getContext());

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new RecyclerAdapter(getContext(), listItemsList);
        mRecyclerView.setAdapter(adapter);

        adapter.clearAdapter();


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, getItemsUrl, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    int success = response.getInt(TAG_SUCCESS);
                    if (success == 1) {

                        JSONArray places = response.getJSONArray(TAG_ITEMS);

                        for (int i = 0; i < places.length(); i++) {
                            JSONObject post = places.getJSONObject(i);

                            ModelPlace place = new ModelPlace();

                            place.setId(post.getInt("place_id"));
                            place.setTitle(post.getString("place_name"));
                            place.setCity(post.getString("place_city"));
                            place.setFirstImage(post.getString("images"));

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
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<>();
                parameters.put("key", "asdf");
                parameters.put("method", "getListView");
                return parameters;
            }
        };

        queue.add(jsonObjectRequest);
    }





    private void showPD() {
        if (progressDialog == null) {
            progressDialog= new ProgressDialog(getContext());
            progressDialog.setMessage("Загрузка...");
            progressDialog.setCancelable(false);
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
