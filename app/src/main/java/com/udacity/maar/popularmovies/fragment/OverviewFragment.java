package com.udacity.maar.popularmovies.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.udacity.maar.popularmovies.R;

/**
 * Created by maar on 2/22/2016.
 */
public class OverviewFragment extends Fragment {
private static final String LOG_TAG = OverviewFragment.class.getSimpleName();
    private String param;

    public static OverviewFragment newInstance(String param){
        OverviewFragment fragment = new OverviewFragment();
        Bundle bundle = new Bundle();
        bundle.putString("param", param);
        fragment.setArguments(bundle);
        return fragment;
    }

    public OverviewFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(LOG_TAG, "onCreate");
        if(getArguments()!=null){
            param = getArguments().getString("param");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.v(LOG_TAG, "onCreateView");
        View view = inflater.inflate(R.layout.overview_item, container, false);
        TextView overView = (TextView) view.findViewById(R.id.movie_overview);
        if (param != null && !param.equals("")) {
            overView.setText(param);
        } else {
            overView.setText("No Overview Found");
        }
        return view;
    }


}
