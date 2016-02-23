package com.udacity.maar.popularmovies.util;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.udacity.maar.popularmovies.BuildConfig;
import com.udacity.maar.popularmovies.fragment.MostPopularFragment;
import com.udacity.maar.popularmovies.fragment.ReviewFragment;
import com.udacity.maar.popularmovies.fragment.TopRatedFragment;
import com.udacity.maar.popularmovies.fragment.TrailerFragment;
import com.udacity.maar.popularmovies.helper.JSONParserHelper;

import java.io.IOException;
import java.util.List;

import okhttp3.Response;

/**
 * Created by maar on 2/15/2016.
 */
public class CommonAsyncTask extends AsyncTask<String, Void, Object>{
    private Fragment fragment;
    private int req_type;

    public CommonAsyncTask(Fragment fragment, int req_type){
        this.fragment = fragment;
        this.req_type = req_type;
    }

    @Override
    protected Object doInBackground(String... strings) {
        String page = "1";
        if(strings!= null & strings.length > 0){
            page = strings[0];
        }

        //build the URI
        Uri.Builder builder = null;

        OkHttpClient client = new OkHttpClient();
        if(req_type == StringConstants.MOST_POPULAR_REQUEST){
            builder = Uri.parse(StringConstants.REQUEST_BASE_URL).buildUpon()
                    .appendQueryParameter("api_key", BuildConfig.MOVIE_DB_API_KEY)
                    .appendQueryParameter("page", page)
                    .appendQueryParameter("sort_by", "popularity.desc");
        }else if(req_type == StringConstants.TOP_RATED_REQUEST){
            builder = Uri.parse(StringConstants.REQUEST_BASE_URL).buildUpon()
                    .appendQueryParameter("api_key", BuildConfig.MOVIE_DB_API_KEY)
                    .appendQueryParameter("page", page)
                    .appendQueryParameter("vote_count.gte", "200")
                    .appendQueryParameter("sort_by", "vote_average.desc");
        }else if (req_type == StringConstants.VIDEO_TRAILER_REQUEST) {
            String movieId = fragment.getArguments().getString(TrailerFragment.MOVIE_ID);
            builder = Uri.parse(StringConstants.TRAILER_REVIEWS_BASE_URL).buildUpon()
                    .appendPath(movieId)
                    .appendPath("videos")
                    .appendQueryParameter("api_key", BuildConfig.MOVIE_DB_API_KEY);

        } else if (req_type == StringConstants.MOVIE_REVIEWS_REQUEST) {
            String movieId = fragment.getArguments().getString(TrailerFragment.MOVIE_ID);
            builder = Uri.parse(StringConstants.TRAILER_REVIEWS_BASE_URL).buildUpon()
                    .appendPath(movieId)
                    .appendPath("reviews")
                    .appendQueryParameter("api_key", BuildConfig.MOVIE_DB_API_KEY)
                    .appendQueryParameter("page", page);
        }
        Uri uri = builder.build();

        Request request = new Request.Builder()
                .url(uri.toString())
                .build();

        com.squareup.okhttp.Response response = null;
        List<Object> list = null;
        JSONParserHelper parser = new JSONParserHelper();
        try {
            response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                return null;
            }
            String json = response.body().string();
            if(req_type == StringConstants.MOST_POPULAR_REQUEST || req_type == StringConstants.TOP_RATED_REQUEST) {
                list = parser.parseMovieList(json);
            }else if(req_type == StringConstants.VIDEO_TRAILER_REQUEST) {
                list = parser.parseMovieTrailers(json);
            }else if(req_type == StringConstants.MOVIE_REVIEWS_REQUEST) {
                list = parser.parseMovieReviews(json);
            }

            System.out.println("Response "+ json);

        } catch (IOException e) {
            Log.e("CommonAsyncTask", "Exception", e);
        }
        return list;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        if(req_type == StringConstants.MOST_POPULAR_REQUEST){
            ((MostPopularFragment) fragment).onTaskCompleted(o);
        }
        if(req_type == StringConstants.TOP_RATED_REQUEST){
            ((TopRatedFragment) fragment).onTaskCompleted(o);
        }
        if(req_type == StringConstants.VIDEO_TRAILER_REQUEST){
            ((TrailerFragment) fragment).onTaskCompleted(o);
        }
        if(req_type == StringConstants.MOVIE_REVIEWS_REQUEST){
            ((ReviewFragment) fragment).onTaskCompleted(o);
        }
    }
}
