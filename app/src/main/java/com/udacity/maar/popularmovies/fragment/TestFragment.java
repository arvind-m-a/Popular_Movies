package com.udacity.maar.popularmovies.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.udacity.maar.popularmovies.R;
import com.udacity.maar.popularmovies.adapter.MainPageAdapter;


/**
 * Created by maar on 2/20/2016.
 */
public class TestFragment extends Fragment {
    private static final String LOG_TAG = TestFragment.class.getSimpleName();
    public TestFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(LOG_TAG, "inOnCreate");
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.v(LOG_TAG, "inside oncreateView");
        View rootView = inflater.inflate(R.layout.fragment_test, container, false);
        ViewPager viewPager = (ViewPager) rootView.findViewById(R.id.main_pager);
        MainPageAdapter adapter = new MainPageAdapter(getChildFragmentManager(), getActivity());
        viewPager.setAdapter(adapter);
        return rootView;
    }
}
