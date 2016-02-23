package com.udacity.maar.popularmovies.adapter;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.udacity.maar.popularmovies.fragment.FavouriteFragment;
import com.udacity.maar.popularmovies.fragment.MostPopularFragment;
import com.udacity.maar.popularmovies.fragment.TopRatedFragment;

/**
 * Created by maar on 2/20/2016.
 */
public class MainPageAdapter extends FragmentStatePagerAdapter {
    private Context mContext;

    public MainPageAdapter(FragmentManager fm, Context mContext){
        super(fm);
        this.mContext = mContext;
    }

    @Override
    public Fragment getItem(int position) {
        if(getPageTitle(position).equals("MOST POPULAR")){
            return new MostPopularFragment();
        }else if (getPageTitle(position).equals("TOP RATED")){
            return new TopRatedFragment();
        }else if(getPageTitle(position).equals("FAVOURITES")){
            return new FavouriteFragment();
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
                return "MOST POPULAR";
            case 1:
                return "TOP RATED";
            case 2:
                return "FAVOURITES";
        }
        return null;
    }
}
