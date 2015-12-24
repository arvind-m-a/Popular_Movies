package com.udacity.maar.popularmovies;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by maar on 12/8/2015.
 */
public class GridViewAdapter extends ArrayAdapter<Movie> {
    private static final String LOG_TAG = GridViewAdapter.class.getSimpleName();

    public GridViewAdapter(Activity context, List<Movie> movieList){
        super(context, 0 ,movieList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Movie movie = getItem(position);
        String MOVIE_BASE_URL_W185="http://image.tmdb.org/t/p/w185/";
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.movie_poster, parent, false);
        }

        ImageView posterView = (ImageView) convertView.findViewById(R.id.poster_image);
        TextView movieTitle = (TextView) convertView.findViewById(R.id.movie_title_rating);
        Picasso.with(getContext()).load(MOVIE_BASE_URL_W185 + movie.getPoster_path()).placeholder(R.raw.no_poster).error(R.raw.no_poster).into(posterView);
        movieTitle.setText(movie.getOriginal_title() + "\n" + movie.getVote_average().toString() + "/10");
        return convertView;
    }
}
