package com.udacity.maar.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by maar on 12/8/2015.
 */
public class Movie implements Parcelable {


    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public void setVote_average(Double vote_average) {
        this.vote_average = vote_average;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public void setOver_view(String over_view) {
        this.over_view = over_view;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private String poster_path;
   private String backdrop_path;
   private String release_date;
   private Double vote_average;
   private String original_title;
   private String over_view;
   private Long id;

    public boolean isVideo() {
        return video;
    }

    public void setVideo(boolean video) {
        this.video = video;
    }

    private boolean video;

    public Movie(Long id, String poster_path, String backdrop_path, String release_date, Double vote_average, String original_title, String over_view){
        this.id = id;
        this.backdrop_path = backdrop_path;
        this.release_date = release_date;
        this.vote_average = vote_average;
        this.original_title = original_title;
        this.over_view = over_view;
        this.poster_path = poster_path;
    }
    public Movie(){

    }

    private Movie(Parcel in){
        poster_path = in.readString();
        backdrop_path = in.readString();
        release_date = in.readString();
        vote_average = in.readDouble();
        original_title = in.readString();
        over_view = in.readString();
        id = in.readLong();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(poster_path);
        parcel.writeString(backdrop_path);
        parcel.writeString(release_date);
        parcel.writeDouble(vote_average);
        parcel.writeString(original_title);
        parcel.writeString(over_view);
        parcel.writeLong(id);
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>(){

        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public String getPoster_path() {
        return poster_path;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public String getRelease_date() {
        return release_date;
    }

    public Double getVote_average() {
        return vote_average;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public String getOver_view() {
        return over_view;
    }

    public Long getId() {
        return id;
    }
}
