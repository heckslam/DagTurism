package ru.devtron.dagturism.abstract_classes;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.melnykov.fab.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ru.devtron.dagturism.Utils.Constants;
import ru.devtron.dagturism.OpenPlaceActivity;
import ru.devtron.dagturism.R;
import ru.devtron.dagturism.adapter.RecyclerAdapter;
import ru.devtron.dagturism.dialog.SearchPlaceDialogFragment;
import ru.devtron.dagturism.listener.ClickListener;
import ru.devtron.dagturism.listener.RecyclerClickListener;
import ru.devtron.dagturism.model.ModelPlace;

public abstract class AbstractTabFragment extends Fragment {

    private String title;
    protected Context context;
    protected View view;
    protected FragmentManager fragmentManager;

    protected List<ModelPlace> listPlaces = new ArrayList<>();
    protected RecyclerView mRecyclerView;
    protected RecyclerAdapter adapter;

    protected int numberOfColumns = 1;




    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    protected void setClickListenerForCards() {
        adapter = new RecyclerAdapter(getContext(), listPlaces);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setHasFixedSize(true);

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


    protected void initFab() {
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


}
