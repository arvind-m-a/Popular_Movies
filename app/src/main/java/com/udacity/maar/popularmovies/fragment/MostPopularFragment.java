package com.udacity.maar.popularmovies.fragment;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.udacity.maar.popularmovies.GridViewAdapter;
import com.udacity.maar.popularmovies.Movie;
import com.udacity.maar.popularmovies.R;
import com.udacity.maar.popularmovies.util.CommonAsyncTask;
import com.udacity.maar.popularmovies.util.StringConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maar on 2/20/2016.
 */
public class MostPopularFragment extends Fragment implements SharedPreferences.OnSharedPreferenceChangeListener{
    private static final String LOG_TAG = MostPopularFragment.class.getSimpleName();
    private GridViewAdapter viewAdapter;
    private LinearLayoutManager layoutManager;
    private int pageCount = 1;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    private boolean loading = true;
    SharedPreferences sharedPreferences;
    List<Movie> movies = new ArrayList<>();
    public MostPopularFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(LOG_TAG, "inside OnCreate");
        setHasOptionsMenu(true);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        if (savedInstanceState!=null && !savedInstanceState.containsKey("movies")){
            movies = savedInstanceState.getParcelableArrayList("movies");
        }else{
            updateMovies();
        }
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    private void updateMovies() {
        Log.v(LOG_TAG, "inside UpdateMovies");
        new CommonAsyncTask(this, StringConstants.MOST_POPULAR_REQUEST).execute("1");
    }

    @Override
    public void onDestroy() {
        Log.v(LOG_TAG, "onDestroy");
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.v(LOG_TAG, "inside OnCreateView");
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        TextView noText = (TextView) view.findViewById(R.id.emptyTextView);
        recyclerView.setHasFixedSize(true);
        // The number of Columns
        if (Configuration.ORIENTATION_LANDSCAPE == getResources().getConfiguration().orientation) {
            layoutManager = new GridLayoutManager(this.getActivity(), 3);
        } else {
            layoutManager = new GridLayoutManager(this.getActivity(), 2);
        }
        recyclerView.setLayoutManager(layoutManager);
        viewAdapter = new GridViewAdapter(getActivity(), movies, this);
        recyclerView.setAdapter(viewAdapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    visibleItemCount = recyclerView.getChildCount();
                    totalItemCount = layoutManager.getItemCount();
                    pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();
                    if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                        if (loading && pageCount <= 40) {
                            loading = false;
                            Log.v("...", "Last Item Wow !");
                            pageCount++;
                            new CommonAsyncTask(MostPopularFragment.this, StringConstants.MOST_POPULAR_REQUEST).execute(Integer.toString(pageCount));

                        }
                    }
                }
            }
        });

        return view;
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        movies.clear();
        updateMovies();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_refresh){
            updateMovies();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("movies", (ArrayList<? extends Parcelable>) movies);
        super.onSaveInstanceState(outState);
    }

    public void onTaskCompleted(Object o){
        loading = true;
        if(o != null){
            movies.addAll((List<Movie>)o);
            viewAdapter.notifyDataSetChanged();
        }
        else{
            Toast.makeText(getActivity(), "Sorry, An error has occured", Toast.LENGTH_SHORT).show();
        }
    }
}
