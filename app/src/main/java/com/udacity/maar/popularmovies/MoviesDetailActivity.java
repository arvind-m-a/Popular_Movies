package com.udacity.maar.popularmovies;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.udacity.maar.popularmovies.fragment.FavouriteFragment;

/**
 * Created by maar on 2/21/2016.
 */
public class MoviesDetailActivity extends AppCompatActivity {
    private final String LOG_TAG = MoviesDetailActivity.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.v(LOG_TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Bundle bundle = getIntent().getExtras();
        Movie movie = bundle.getParcelable("movie");
        int ft = bundle.getInt("ft");
        String fragmentType;
        if(ft == 3){
            fragmentType = FavouriteFragment.class.getSimpleName();
        }
        else{
            fragmentType = "";
        }
        setTitle(movie.getOriginal_title());
        DetailActivityFragment detailActivityFragment = DetailActivityFragment.newInstance(movie, fragmentType);
        getSupportFragmentManager().beginTransaction().replace(R.id.detailContainer, detailActivityFragment).commit();
    }
}
