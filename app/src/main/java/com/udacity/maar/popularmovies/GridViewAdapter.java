package com.udacity.maar.popularmovies;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.udacity.maar.popularmovies.fragment.FavouriteFragment;

import java.util.List;

/**
 * Created by maar on 12/8/2015.
 */
public class GridViewAdapter extends RecyclerView.Adapter<GridViewAdapter.ViewHolder> {
    private static final String LOG_TAG = GridViewAdapter.class.getSimpleName();
    private Context mContext;
    private Fragment fragmentType;
    private List<Movie> movieList;
    public static final String DETAILFRAGMENT_TAG ="DFTAG";

   /* public GridViewAdapter(Activity context, List<Movie> movieList){
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
    }*/

    public GridViewAdapter(Context c, List<Movie> movieList, Fragment fragmentType){
        mContext = c;
        this.movieList = movieList;
        this.fragmentType = fragmentType;
    }


    @Override
    public GridViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_poster, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(GridViewAdapter.ViewHolder holder, int position) {
        holder.bindMovie(movieList.get(position));
    }

    @Override
    public int getItemCount() {
        return movieList.toArray().length;
    }
    class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView movImage;
        public TextView movieTitle, voteAverage;
        public CardView cardView;
        View view;
        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            cardView =(CardView) view.findViewById(R.id.movie_container);
            movieTitle = (TextView) view.findViewById(R.id.movie_title);
            voteAverage = (TextView) view.findViewById(R.id.movie_voting);
            movImage = (ImageView) view.findViewById(R.id.poster_image);
        }

        public void bindMovie(@NonNull final Movie movie){
            movieTitle.setText(movie.getOriginal_title());
            voteAverage.setText(movie.getVote_average().toString());

            Picasso.with(mContext).load(movie.getPoster_path()).placeholder(R.raw.no_poster).error(R.raw.no_poster).into(movImage);

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    if(((MainActivity) mContext).mTwoPane){
                        DetailActivityFragment detailFragment = DetailActivityFragment.newInstance(movie, fragmentType.getClass().getSimpleName());
                        ((MainActivity) mContext).getSupportFragmentManager().beginTransaction()
                                .replace(R.id.detailContainer, detailFragment, DETAILFRAGMENT_TAG)
                                .commit();

                    }else {
                        Intent intent = new Intent(mContext, MoviesDetailActivity.class);
                        bundle.putParcelable("movie", movie);
                        if (fragmentType.getClass().getSimpleName().equals(FavouriteFragment.class.getSimpleName())) {
                            bundle.putInt("ft", 3);
                        }
                        bundle.putString("fragmenttype", fragmentType.getClass().getSimpleName());
                        intent.putExtras(bundle);
                        fragmentType.startActivity(intent);
                    }

                }
            });
        }
    }
}
