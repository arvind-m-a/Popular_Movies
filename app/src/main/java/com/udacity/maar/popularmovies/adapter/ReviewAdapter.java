package com.udacity.maar.popularmovies.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.udacity.maar.popularmovies.R;
import com.udacity.maar.popularmovies.Review;

import java.util.List;

/**
 * Created by maar on 2/22/2016.
 */
public class ReviewAdapter extends BaseAdapter {
    private final Context mContext;
    private final List<Review> reviews;


    public ReviewAdapter(Context context, List<Review> reviews){
        this.mContext = context;
        this.reviews = reviews;
    }
    @Override
    public int getCount() {
        return reviews.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView review = null;
        TextView author = null;
        if(convertView == null){
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(R.layout.review_item, parent, false);
            review = (TextView) convertView.findViewById(R.id.reviewContent);
            author = (TextView) convertView.findViewById(R.id.author);
            review.setText(reviews.get(position).getContent());
            author.setText(reviews.get(position).getAuthor());
        }


        return convertView;
    }

    @Override
    public Object getItem(int position) {
        return reviews.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
