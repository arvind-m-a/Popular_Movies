package com.udacity.maar.popularmovies;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.support.v7.widget.ShareActionProvider;
import android.widget.Toolbar;

import com.squareup.picasso.Picasso;
import com.udacity.maar.popularmovies.adapter.DetailPageAdapter;
import com.udacity.maar.popularmovies.database.MoviesContract;
import com.udacity.maar.popularmovies.fragment.FavouriteFragment;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment implements View.OnClickListener{
    private static final String LOG_TAG = DetailActivityFragment.class.getSimpleName();
    Movie clickedMovie = null;
    private String fragmentType;
    private ShareActionProvider shareActionProvider;
    public DetailActivityFragment() {
    }

    public static DetailActivityFragment newInstance(Movie movie, String fragmentType){
        Log.v(LOG_TAG, "newInstance");
        DetailActivityFragment fragment = new DetailActivityFragment();
        Bundle args = new Bundle();
        args.putParcelable("movie", movie);
        args.putString("ft", fragmentType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if(getArguments() !=  null){
            clickedMovie = getArguments().getParcelable("movie");
            fragmentType = getArguments().getString("ft");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.v(LOG_TAG, "onCreateView");
        View rootView =  inflater.inflate(R.layout.fragment_detail, container, false);

        ImageView posterImage = (ImageView) rootView.findViewById(R.id.poster_view);
        TextView releaseDate = (TextView) rootView.findViewById(R.id.release_date);
        TextView rating = (TextView) rootView.findViewById(R.id.rating);
        RatingBar ratingBar = (RatingBar) rootView.findViewById(R.id.ratingBar);
       // TextView overview = (TextView) rootView.findViewById(R.id.over_view);
        ViewPager pager = (ViewPager) rootView.findViewById(R.id.detailPager);
        FloatingActionButton favBtn = (FloatingActionButton) rootView.findViewById(R.id.favBtn);
        if(fragmentType!=null && fragmentType.equals(FavouriteFragment.class.getSimpleName())){
            favBtn.setVisibility(View.GONE);
        }
        else{
            favBtn.setOnClickListener(this);
        }
        DetailPageAdapter adapter = new DetailPageAdapter(getActivity(), getChildFragmentManager(), clickedMovie);
        pager.setAdapter(adapter);
        String MOVIE_BASE_URL_W185="http://image.tmdb.org/t/p/w185/";

        ratingBar.setIsIndicator(true);
        ratingBar.setNumStars(5);

        if(clickedMovie!=null) {
            Picasso.with(getContext()).load(clickedMovie.getPoster_path()).placeholder(R.raw.no_poster).error(R.raw.no_poster).into(posterImage);
            releaseDate.setText(clickedMovie.getRelease_date());
            rating.setText(clickedMovie.getVote_average().toString() + "/10");
            double movieRating = clickedMovie.getVote_average()/2.0;
            float movRating = (float) movieRating;
            ratingBar.setRating(movRating);
           // overview.setText(clickedMovie.getOver_view());
        }
        return rootView;
    }

    @Override
    public void onClick(View v) {
        Log.v(LOG_TAG, "onClick");
        if(v.getId() == R.id.favBtn){
            Uri uri = MoviesContract.MovieEntry.CONTENT_URI.buildUpon().appendPath(clickedMovie.getId().toString()).build();
            Cursor movieCursor = getActivity().getContentResolver().query(uri, null, null, null, null);
            if(movieCursor!=null) {
                if (movieCursor.moveToNext() != true) {
                    ContentValues newValues = generateContentValues(clickedMovie);
                    Uri insertedUri = getActivity().getContentResolver().insert(MoviesContract.MovieEntry.CONTENT_URI, newValues);
                    long id = ContentUris.parseId(insertedUri);
                    if (id != -1) {
                        Snackbar.make(v, "Added to Favourites", Snackbar.LENGTH_LONG).show();
                    }
                } else {
                    Snackbar.make(v, "Already in Favourites", Snackbar.LENGTH_LONG).show();
                }
            }
        }
    }

    private ContentValues generateContentValues(Movie clickedMovie) {
        Log.v(LOG_TAG, "inside contentVal");
        ContentValues  contentValues = new ContentValues();
        contentValues.put(MoviesContract.MovieEntry.COLUMN_MOVIES_ID, clickedMovie.getId());
        contentValues.put(MoviesContract.MovieEntry.COLUMN_MOVIE_NAME, clickedMovie.getOriginal_title());
        contentValues.put(MoviesContract.MovieEntry.COLUMN_IMAGE_URL, clickedMovie.getPoster_path());
        contentValues.put(MoviesContract.MovieEntry.COLUMN_BACKDROP_PATH, clickedMovie.getBackdrop_path());
        contentValues.put(MoviesContract.MovieEntry.COLUMN_OVERVIEW, clickedMovie.getOver_view());
        contentValues.put(MoviesContract.MovieEntry.COLUMN_RATING, clickedMovie.getVote_average());
        contentValues.put(MoviesContract.MovieEntry.COLUMN_VIDEO, clickedMovie.isVideo());
        return contentValues;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_detail, menu);
        MenuItem menuItem = menu.findItem(R.id.action_share);
        shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        shareActionProvider.setShareIntent(null);
    }

    public void setShareIntent(String text){
        if(shareActionProvider != null){
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, text);
            shareActionProvider.setShareIntent(shareIntent);
        }
    }
}
