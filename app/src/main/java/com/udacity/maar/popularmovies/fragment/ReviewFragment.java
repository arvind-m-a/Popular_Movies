package com.udacity.maar.popularmovies.fragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.widget.Toast;

import com.udacity.maar.popularmovies.Review;
import com.udacity.maar.popularmovies.adapter.ReviewAdapter;
import com.udacity.maar.popularmovies.util.CommonAsyncTask;
import com.udacity.maar.popularmovies.util.StringConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maar on 2/21/2016.
 */
public class ReviewFragment extends ListFragment {
    private static final String LOG_TAG = ReviewFragment.class.getSimpleName();
    private String param;
    private List<Review> reviewList = new ArrayList<>();
    private ReviewAdapter reviewAdapter;

    public static ReviewFragment newInstance(String param){
        Log.v(LOG_TAG, "newInstance");
        ReviewFragment fragment = new ReviewFragment();
        Bundle bundle = new Bundle();
        bundle.putString("movie_id", param);
        fragment.setArguments(bundle);
        return fragment;
    }

    public ReviewFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.v(LOG_TAG, "onCreate");
        super.onCreate(savedInstanceState);
        if(getArguments()!=null){
            param = getArguments().getString("movie_id");
            CommonAsyncTask asyncTask = new CommonAsyncTask(this, StringConstants.MOVIE_REVIEWS_REQUEST);
            asyncTask.execute();
        }

        reviewAdapter = new ReviewAdapter(getActivity(), reviewList);
        setListAdapter(reviewAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        getListView().setDivider(new ColorDrawable(Color.BLACK));
        getListView().setDividerHeight(1);
    }

    public void onTaskCompleted(Object object){
        if(object!=null){
            reviewList.addAll((List<Review>) object);
            reviewAdapter.notifyDataSetChanged();
            if(reviewList.size() == 0){
                setEmptyText("No Reviews Found");
            }
        }
        else{
            Toast.makeText(getActivity(), "Sorry, Error Occured", Toast.LENGTH_SHORT).show();
        }
    }
}
