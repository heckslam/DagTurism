package ru.devtron.dagturism;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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

import ru.devtron.dagturism.listener.ClickListener;
import ru.devtron.dagturism.listener.RecyclerClickListener;
import ru.devtron.dagturism.adapter.WaylineAdapter;
import ru.devtron.dagturism.model.WaylineModel;

public class SprintLineActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private double pointLat, pointLng;
    private String pointCaption;
    private List<WaylineModel> mDataList = new ArrayList<>();
    private WaylineModel waylineModel;
    private TextView textVolleyError;
    private WaylineAdapter adapter;
    private ProgressBar progressBar;

    private int id;
    private int success = 0;
    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_POINTS = "points";
    private static final String TAG_LAT = "point_latitude";
    private static final String TAG_LNG = "point_longitude";
    private static final String TAG_CAPTION = "point_caption";
    private static final String TAG_ITEM = "item";

    SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        String selectedTheme = sp.getString("selectedTheme", "1");
        int selectedThemeValue = Integer.parseInt(selectedTheme);
        switch (selectedThemeValue) {
            case 1:
                setTheme(R.style.AppDefault);
                break;
            case 2:
                setTheme(R.style.AppOrange);
                break;
            case 3:
                setTheme(R.style.AppPurple);
                break;
            case 4:
                setTheme(R.style.AppGrey);
                break;
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sprint_line);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);

        if (savedInstanceState!=null) {
            progressBar.setVisibility(View.GONE);
        }

        id = getIntent().getIntExtra("id", 0);
        initToolbar();

        textVolleyError = (TextView) findViewById(R.id.textVolleyError);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        updateItem();

        adapter = new WaylineAdapter(mDataList);
        mRecyclerView.setAdapter(adapter);

        mRecyclerView.addOnItemTouchListener(new RecyclerClickListener(this, mRecyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(SprintLineActivity.this, OpenMapActivity.class);
                intent.putExtra("lat", mDataList.get(position).getPointLat());
                intent.putExtra("lng", mDataList.get(position).getPointLng());
                intent.putExtra("title", getResources().getString(R.string.howtogo));
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (toolbar != null) {
            toolbar.setTitle(R.string.howtogo);
            toolbar.setTitleTextColor(Color.WHITE);
            setSupportActionBar(toolbar);
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
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


    private void updateItem () {
        progressBar.setVisibility(View.VISIBLE);
        String getItemsUrl = "http://republic.tk/api/place/";

        getItemsUrl = getItemsUrl + id;

        Log.d("url123", getItemsUrl);

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, getItemsUrl, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    success = response.getInt(TAG_SUCCESS);
                    if (success == 1) {


                        JSONObject place = response.getJSONObject(TAG_ITEM);
                        JSONArray points = place.getJSONArray(TAG_POINTS);

                        for (int i = points.length() - 1; i >= 0; i--) {
                            JSONObject currentPoint = points.getJSONObject(i);
                            waylineModel = new WaylineModel();

                            pointLat = currentPoint.getDouble(TAG_LAT);
                            pointLng = currentPoint.getDouble(TAG_LNG);
                            pointCaption = currentPoint.getString(TAG_CAPTION);

                            waylineModel.setPointLat(pointLat);
                            waylineModel.setPointLng(pointLng);
                            waylineModel.setPointCaption(pointCaption);


                            mDataList.add(waylineModel);
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
