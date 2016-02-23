package com.udacity.maar.popularmovies.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.udacity.maar.popularmovies.DetailActivityFragment;
import com.udacity.maar.popularmovies.Trailer;
import com.udacity.maar.popularmovies.adapter.TrailerAdapter;
import com.udacity.maar.popularmovies.database.MoviesContract;
import com.udacity.maar.popularmovies.util.CommonAsyncTask;
import com.udacity.maar.popularmovies.util.StringConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maar on 2/21/2016.
 */
public class TrailerFragment extends ListFragment {
    private static final String LOG_TAG = TrailerFragment.class.getSimpleName();
    public static final String MOVIE_ID = "movie_id";
    private TrailerAdapter trailerAdapter;
    private List<Trailer> trailerList = new ArrayList<>();
    private String param;

    public static TrailerFragment newInstance(String id){
        Log.v(LOG_TAG, "newInstance");
        TrailerFragment fragment = new TrailerFragment();
        Bundle bundle = new Bundle();
        bundle.putString(MOVIE_ID, id);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.v(LOG_TAG, "onCreate");
        super.onCreate(savedInstanceState);
        if(getArguments()!=null){
            CommonAsyncTask asyncTask = new CommonAsyncTask(this, StringConstants.VIDEO_TRAILER_REQUEST);
            asyncTask.execute();
        }
        trailerAdapter = new TrailerAdapter(getActivity(), trailerList);
        setListAdapter(trailerAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        getListView().setDivider(new ColorDrawable(Color.BLACK));
        getListView().setDividerHeight(1);
    }

    public TrailerFragment(){

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Trailer trailer = trailerList.get(position);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:"+ trailer.getKey()));
        if(intent.resolveActivity(getActivity().getPackageManager())!=null){
            startActivity(intent);
        }
        else{
            Intent YTIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(StringConstants.YOU_TUBE_BASE_URL + trailer.getKey()));
            startActivity(YTIntent);
        }
    }

    public void onTaskCompleted(Object o){
        if(o!=null){
            trailerList.addAll((List<Trailer>) o);
            trailerAdapter.notifyDataSetChanged();
            if(trailerList.size()==0){
                setEmptyText("No Trailers Found");
            }
            else{
                ((DetailActivityFragment)getParentFragment()).setShareIntent("Watch the Trailer - " + StringConstants.YOU_TUBE_BASE_URL + trailerList.get(0).getKey());
            }
        }else{
            Toast.makeText(getActivity(), "Sorry, Error Occured", Toast.LENGTH_SHORT).show();
        }
    }
}
