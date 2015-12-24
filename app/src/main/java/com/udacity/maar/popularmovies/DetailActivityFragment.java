package com.udacity.maar.popularmovies;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toolbar;

import com.squareup.picasso.Picasso;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_detail, container, false);

        Movie clickedMovie = getActivity().getIntent().getExtras().getParcelable("movieParcel");
        ImageView posterImage = (ImageView) rootView.findViewById(R.id.poster_view);
        TextView releaseDate = (TextView) rootView.findViewById(R.id.release_date);
        TextView rating = (TextView) rootView.findViewById(R.id.rating);
        RatingBar ratingBar = (RatingBar) rootView.findViewById(R.id.ratingBar);
        TextView overview = (TextView) rootView.findViewById(R.id.over_view);

        String MOVIE_BASE_URL_W185="http://image.tmdb.org/t/p/w185/";

        ratingBar.setIsIndicator(true);
        ratingBar.setNumStars(5);

        if(clickedMovie!=null) {
            Picasso.with(getContext()).load(MOVIE_BASE_URL_W185 + clickedMovie.getPoster_path()).placeholder(R.raw.no_poster).error(R.raw.no_poster).into(posterImage);
            releaseDate.setText(clickedMovie.getRelease_date());
            rating.setText(clickedMovie.getVote_average().toString() + "/10");
            double movieRating = clickedMovie.getVote_average()/2.0;
            float movRating = (float) movieRating;
            ratingBar.setRating(movRating);
            overview.setText(clickedMovie.getOver_view());
        }
        return rootView;
    }
}
