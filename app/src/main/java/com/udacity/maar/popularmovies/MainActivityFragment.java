package com.udacity.maar.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    private GridViewAdapter gridViewAdapter;
    private ArrayList<Movie> movies;
    private ArrayList<Movie> movieArrayList;
    SharedPreferences sharedPreferences;
    private final String LOG_TAG = MainActivityFragment.class.getSimpleName();
    public MainActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(getActivity());
        if(savedInstanceState != null && !savedInstanceState.containsKey("movies")){
            Log.v(LOG_TAG, "inside saved state");
            movieArrayList = savedInstanceState.getParcelableArrayList("movies");
        }
        else{
            updateMovies();
        }
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_refresh){
            Log.v(LOG_TAG, "inside refresh");
            updateMovies();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.v(LOG_TAG, "inside save instance state");
        outState.putParcelableArrayList("movies", movieArrayList);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        updateMovies();
    }

    @Override
    public void onDestroy() {
        Log.v(LOG_TAG, "onDestroy");
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        movies = new ArrayList<>();
        gridViewAdapter = new GridViewAdapter(getActivity(), movies);
        View rootView =  inflater.inflate(R.layout.fragment_main, container, false);

        GridView gridView = (GridView) rootView.findViewById(R.id.movie_posters_grid);
        gridView.setAdapter(gridViewAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie clickedMovie = gridViewAdapter.getItem(position);
              Intent intent = new Intent(getContext(), DetailActivity.class).putExtra("movieParcel", clickedMovie);
                startActivity(intent);
            }
        });

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void updateMovies() {
        FetchMoviesTask moviesTask = new FetchMoviesTask();
        String sort_setting = sharedPreferences.getString(getString(R.string.pref_sort_key), getString(R.string.pref_sort_popularity));
        moviesTask.execute(sort_setting);
    }

    public class FetchMoviesTask extends AsyncTask<String, Void, List<Movie>>{
        private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();


        @Override
        protected List<Movie> doInBackground(String... params) {
            if(params.length == 0){
                return null;
            }
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String movieJsonStr = null;


            try {
                // Construct the URL for the MovieDB API
                final String MOVIE_BASE_URL = "http://api.themoviedb.org/3/discover/movie?";
                final String SORT_BY = "sort_by";
                final String APP_KEY_PARAM = "api_key";
                final String VOTE_COUNT = "vote_count.gte";
                final String voteCount = "200";
                Uri builtUri;
                if(params[0].contains("vote")) {
                    Log.v(LOG_TAG, "inside rating query");
                    builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon().appendQueryParameter(SORT_BY, params[0]).appendQueryParameter(VOTE_COUNT, voteCount).appendQueryParameter(APP_KEY_PARAM, BuildConfig.MOVIE_DB_API_KEY).build();
                }
                else{
                    builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon().appendQueryParameter(SORT_BY, params[0]).appendQueryParameter(APP_KEY_PARAM, BuildConfig.MOVIE_DB_API_KEY).build();
                }

                URL url = new URL(builtUri.toString());

                // Create the request to movieDBAPI, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                movieJsonStr = buffer.toString();
                Log.v(LOG_TAG, movieJsonStr);
                return fetchMovieData(movieJsonStr);
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the movie data, there's no point in attemping
                // to parse it.
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }




        }

        @Override
        protected void onPostExecute(List<Movie> movies) {
            if(movies!=null){
                gridViewAdapter.clear();
                gridViewAdapter.addAll(movies);
            }
        }

        private List<Movie> fetchMovieData(String movieJsonStr) {
            Log.v(LOG_TAG, "Inside FetchMovieData");
            List<Movie> movies = new ArrayList<>();
            try {
                JSONObject jsonObject = new JSONObject(movieJsonStr);
                JSONArray jsonArray = jsonObject.getJSONArray("results");
                for(int i = 0 ; i < jsonArray.length(); i++){
                    JSONObject tmp = jsonArray.getJSONObject(i);
                    String posterUrl = tmp.getString("poster_path");
                    String backDrop_path = tmp.getString("backdrop_path");
                    String overView = tmp.getString("overview");
                    String releaseDate = tmp.getString("release_date");
                    Long id = tmp.getLong("id");
                    String originalTitle = tmp.getString("original_title");
                    Double vote_average = tmp.getDouble("vote_average");
                    Log.v(LOG_TAG, "original title is "+ originalTitle);
                   movies.add(i, new Movie(id,posterUrl,backDrop_path,releaseDate,vote_average,originalTitle,overView));
                }
            }catch (JSONException e){
                e.printStackTrace();
            }finally {

                return movies;
            }

        }
    }
}
