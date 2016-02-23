package com.udacity.maar.popularmovies.fragment;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.udacity.maar.popularmovies.DetailActivityFragment;
import com.udacity.maar.popularmovies.GridViewAdapter;
import com.udacity.maar.popularmovies.MainActivity;
import com.udacity.maar.popularmovies.Movie;
import com.udacity.maar.popularmovies.R;
import com.udacity.maar.popularmovies.database.MoviesContract;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maar on 2/20/2016.
 */
public class FavouriteFragment extends Fragment {
    private RecyclerView recyclerView;
    private GridViewAdapter viewAdapter;
    private LinearLayoutManager layoutManager;
    List<Movie> movies;
    SharedPreferences sharedPreferences;
    TextView emptyView;
    private static final String LOG_TAG = FavouriteFragment.class.getSimpleName();
    private static final String DETAILFRAGMENT_TAG ="DFTAG";

    public FavouriteFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(LOG_TAG, "inside onCreate");
        setHasOptionsMenu(true);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        if(savedInstanceState!=null && !savedInstanceState.containsKey("movies")){
            movies = savedInstanceState.getParcelableArrayList("movies");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.v(LOG_TAG, "onCreateView");
        movies = new ArrayList<>();
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        emptyView = (TextView) view.findViewById(R.id.emptyTextView);
        recyclerView.setHasFixedSize(true);


        if(Configuration.ORIENTATION_LANDSCAPE == getResources().getConfiguration().orientation){
            layoutManager = new GridLayoutManager(this.getActivity(),3);
        }else{
            layoutManager = new GridLayoutManager(this.getActivity(),2);
        }
        recyclerView.setLayoutManager(layoutManager);
        viewAdapter = new GridViewAdapter(getActivity(), movies, this);
        recyclerView.setAdapter(viewAdapter);

        if(movies==null || movies.size()==0){
            Cursor cursor = getActivity().getContentResolver().query(MoviesContract.MovieEntry.CONTENT_URI, null, null, null, null);
            getMoviesFromCursor(cursor);
            if(movies.size()==0){
                emptyView.setText("No Favourites Added");
                emptyView.setVisibility(View.VISIBLE);
                if(((MainActivity) getActivity()).mTwoPane){
                    Fragment fragment = ((MainActivity) getActivity()).getSupportFragmentManager().findFragmentByTag(DETAILFRAGMENT_TAG);
                    ((MainActivity) getActivity()).getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                }
            }
            else {
                viewAdapter.notifyDataSetChanged();
                if (((MainActivity) getActivity()).mTwoPane) {
                    ((MainActivity) getActivity()).getSupportFragmentManager().beginTransaction()
                            .replace(R.id.detailContainer, DetailActivityFragment.newInstance(movies.get(0), FavouriteFragment.class.getSimpleName()), DETAILFRAGMENT_TAG)
                            .commit();
                }
            }
        }
        return view;
    }

    private void getMoviesFromCursor(Cursor cursor) {
        if(movies!=null){
            movies.clear();
        }
        else{
            movies = new ArrayList<>();
        }
        if(cursor!=null) {
            while (cursor.moveToNext()) {
                Movie movie = new Movie();
                movie.setId(cursor.getLong(cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_MOVIES_ID)));
                movie.setOriginal_title(cursor.getString(cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_MOVIE_NAME)));
                movie.setPoster_path(cursor.getString(cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_IMAGE_URL)));
                movie.setBackdrop_path(cursor.getString(cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_BACKDROP_PATH)));
                movie.setOver_view(cursor.getString(cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_OVERVIEW)));
                movie.setRelease_date(cursor.getString(cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_RELEASE_DATE)));
                movie.setVote_average(cursor.getDouble(cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_RATING)));
                movies.add(movie);
            }
        }
    }
}
