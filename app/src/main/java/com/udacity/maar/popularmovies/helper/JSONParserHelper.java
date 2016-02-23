package com.udacity.maar.popularmovies.helper;

import com.udacity.maar.popularmovies.Movie;
import com.udacity.maar.popularmovies.Review;
import com.udacity.maar.popularmovies.Trailer;
import com.udacity.maar.popularmovies.util.StringConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maar on 2/17/2016.
 */
public class JSONParserHelper {

    public List<Object> parseMovieList(String json){
        List<Object> movieList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("results");
            for(int i =0;i<jsonArray.length();i++){
                Movie movie = new Movie();
                JSONObject object = jsonArray.getJSONObject(i);
                movie.setId(Long.parseLong(object.getString("id")));
                movie.setPoster_path(StringConstants.IMAGE_PATH_BASE_URL + object.get("poster_path"));
                movie.setOver_view(object.getString("overview"));
                movie.setRelease_date(object.getString("release_date"));
                movie.setOriginal_title(object.getString("original_title"));
                //movie.setLanguage(object.getString("original_language"));
                movie.setBackdrop_path(StringConstants.BACKDROP_IMAGE_PATH_BASE_URL + object.getString("backdrop_path"));
               // movie.setPopularity(object.getString("popularity"));
               // movie.setVote_average(object.getString("vote_count"));
                movie.setVideo(object.getBoolean("video"));
                movie.setVote_average(Double.parseDouble(object.getString("vote_average")));
                movieList.add(movie);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return movieList;
    }

    public List<Object> parseMovieTrailers(String json){
        List<Object> trailerList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("results");
            for(int i =0;i<jsonArray.length();i++){
                Trailer trailer = new Trailer();
                JSONObject object = jsonArray.getJSONObject(i);
                trailer.setId(object.getString("id"));
                trailer.setKey(object.getString("key"));
                trailer.setName(object.getString("name"));
                trailer.setSite(object.getString("site"));
                trailerList.add(trailer);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return trailerList;
    }

    public List<Object> parseMovieReviews(String json){
        List<Object> reviewList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("results");
            for(int i =0;i<jsonArray.length();i++){
                Review review = new Review();
                JSONObject object = jsonArray.getJSONObject(i);
                review.setId(object.getString("id"));
                review.setAuthor(object.getString("author"));
                review.setContent(object.getString("content"));
                reviewList.add(review);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return reviewList;
    }
}
