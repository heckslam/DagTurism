package ru.devtron.dagturism.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import ru.devtron.dagturism.NetworkUtil;
import ru.devtron.dagturism.R;
import ru.devtron.dagturism.abstract_classes.AbstractTabFragment;

public class PopularFragment extends AbstractTabFragment {
    private static final int LAYOUT = R.layout.fragment_popular;

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

        if (savedInstanceState!=null) {
            progressBar.setVisibility(View.GONE);
            listPlaces = savedInstanceState.getParcelableArrayList(STATE_PLACES);
            setClickListenerForCards();
        }

        else {
            networkStateReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if (NetworkUtil.getConnectivityStatusString(context)) {
                        listPlaces.clear();
                        updateList();
                    }

                }
            };
            IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
            context.registerReceiver(networkStateReceiver, filter);
        }


        return view;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if(networkStateReceiver != null) {
            context.unregisterReceiver(networkStateReceiver);
        }
    }


}
