package com.udacity.maar.popularmovies.util;

/**
 * Created by maar on 2/16/2016.
 */
public interface StringConstants {
    String REQUEST_BASE_URL = "http://api.themoviedb.org/3/discover/movie";
    String TRAILER_REVIEWS_BASE_URL = "http://api.themoviedb.org/3/movie";
    String YOU_TUBE_IMAGE_BASE_URL = "http://i3.ytimg.com/vi/";
    String YOU_TUBE_BASE_URL = "http://www.youtube.com/watch?v=";

    String IMAGE_PATH_BASE_URL = "http://image.tmdb.org/t/p/w185/";
    String BACKDROP_IMAGE_PATH_BASE_URL = "http://image.tmdb.org/t/p/w342/";
    int MOST_POPULAR_REQUEST = 1;
    int TOP_RATED_REQUEST = 2;
    int VIDEO_TRAILER_REQUEST = 3;
    int MOVIE_REVIEWS_REQUEST = 4;

    String FRAGMENT_TYPE_FAVORITE = "FAVORITE";

}
