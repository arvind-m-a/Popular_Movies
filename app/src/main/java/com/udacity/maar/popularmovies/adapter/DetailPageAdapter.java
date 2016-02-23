package com.udacity.maar.popularmovies.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.udacity.maar.popularmovies.Movie;
import com.udacity.maar.popularmovies.fragment.OverviewFragment;
import com.udacity.maar.popularmovies.fragment.ReviewFragment;
import com.udacity.maar.popularmovies.fragment.TrailerFragment;

/**
 * Created by maar on 2/21/2016.
 */
public class DetailPageAdapter extends FragmentStatePagerAdapter {
    private Context context;
    private Movie movie;

    public DetailPageAdapter(Context context, FragmentManager fm, Movie movie){
        super(fm);
        this.context = context;
        this.movie = movie;
    }

    @Override
    public Fragment getItem(int position) {
        if(getPageTitle(position).equals("OVERVIEW")){
            return OverviewFragment.newInstance(movie.getOver_view());
        }
        else if(getPageTitle(position).equals("REVIEWS")){
         return ReviewFragment.newInstance(movie.getId().toString());
        }else if(getPageTitle(position).equals("TRAILERS")){
            return TrailerFragment.newInstance(movie.getId().toString());
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "OVERVIEW";
            case 1:
                return "REVIEWS";
            case 2:
                return "TRAILERS";
        }
        return null;
    }
}
