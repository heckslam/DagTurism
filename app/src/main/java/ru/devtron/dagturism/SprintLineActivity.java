package ru.devtron.dagturism;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ru.devtron.dagturism.adapter.WaylineAdapter;
import ru.devtron.dagturism.listener.ClickListener;
import ru.devtron.dagturism.listener.RecyclerClickListener;
import ru.devtron.dagturism.model.WaylineModel;

public class SprintLineActivity extends AppCompatActivity {
    private List<WaylineModel> mDataList = new ArrayList<>();
    TextView finalPrice;
    RecyclerView mRecyclerView;
    private String price;

    private static final String STATE_SPRINT = "state_sprint";

    SharedPreferences sp;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(STATE_SPRINT, (ArrayList<? extends Parcelable>) mDataList);
    }

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
        finalPrice = (TextView) findViewById(R.id.finalPrice);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        initToolbar();


        if (savedInstanceState!=null) {
            mDataList = savedInstanceState.getParcelableArrayList(STATE_SPRINT);
            RecyclerViewSettings();
        }

        else {
            mDataList = getIntent().getParcelableArrayListExtra("mDataList");
            price = getIntent().getStringExtra("finalPrice");
            RecyclerViewSettings();
        }

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

    private void setText(String price) {
        if (price.length()>1) {
            finalPrice.setText(getResources().getString(R.string.finalPrice) + " " + price + "р");
            finalPrice.setVisibility(View.VISIBLE);
        }
    }

    private void RecyclerViewSettings() {
        WaylineAdapter adapter = new WaylineAdapter(mDataList);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setHasFixedSize(true);

        setText(price);


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
                Intent intent = new Intent(SprintLineActivity.this, OpenMapActivity.class);
                intent.putExtra("lat", mDataList.get(position).getPointLat());
                intent.putExtra("lng", mDataList.get(position).getPointLng());
                intent.putExtra("title", getResources().getString(R.string.howtogo));
                startActivity(intent);
            }
        }));
    }


}
