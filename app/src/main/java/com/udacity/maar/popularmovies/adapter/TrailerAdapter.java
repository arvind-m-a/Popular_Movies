package com.udacity.maar.popularmovies.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.udacity.maar.popularmovies.R;
import com.udacity.maar.popularmovies.Trailer;
import com.udacity.maar.popularmovies.util.StringConstants;

import java.util.List;

/**
 * Created by maar on 2/22/2016.
 */
public class TrailerAdapter extends BaseAdapter{
    private final Context context;
    private final List<Trailer> trailers;

    public TrailerAdapter(Context context, List<Trailer> trailers){
        this.context = context;
        this.trailers = trailers;
    }

    @Override
    public int getCount() {
        return trailers.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView trailerImage;
        TextView textView;
        if(convertView == null){
            LayoutInflater layoutInflater = ((Activity) context).getLayoutInflater();
            convertView = layoutInflater.inflate(R.layout.trailer_item, parent, false);
            trailerImage = (ImageView) convertView.findViewById(R.id.trailerImage);
            textView = (TextView) convertView.findViewById(R.id.trailer);
            textView.setText("Trailer "+ (position+1));

            if(trailers.get(position).getSite().equalsIgnoreCase("YouTube")){
                Picasso.with(context).load(StringConstants.YOU_TUBE_IMAGE_BASE_URL + trailers.get(position).getKey() + "/0.jpg").placeholder(R.raw.no_poster).error(R.raw.no_poster).into(trailerImage);
            }
        }
        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return trailers.get(position);
    }
}
