package com.udacity.maar.popularmovies.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import com.udacity.maar.popularmovies.Movie;
import com.udacity.maar.popularmovies.database.DBHelper;
import com.udacity.maar.popularmovies.database.MoviesContract;

/**
 * Created by maar on 2/18/2016.
 */
public class MovieProvider extends ContentProvider {
    private static final String LOG_TAG = MovieProvider.class.getSimpleName();
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private DBHelper dbHelper;
    static final int MOVIE = 100;
    static final int MOVIE_WITH_ID = 101;

    private static final String sMovieIDSelection = MoviesContract.MovieEntry.TABLE_NAME + "." + MoviesContract.MovieEntry.COLUMN_MOVIES_ID + " = ? ";

    private static UriMatcher buildUriMatcher() {
        Log.v(LOG_TAG, "buildUriMatcher");
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MoviesContract.CONTENT_AUTHORITY;
        matcher.addURI(authority,MoviesContract.PATH_MOVIES, MOVIE);
        matcher.addURI(authority, MoviesContract.PATH_MOVIES + "/#", MOVIE_WITH_ID);
        return matcher;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        Log.v(LOG_TAG, "getType");
        //we use the uri matcher to determine the type of query.
        final int match = sUriMatcher.match(uri);
        switch (match){
            case MOVIE:
                return MoviesContract.MovieEntry.CONTENT_TYPE;
            case MOVIE_WITH_ID:
                return MoviesContract.MovieEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown Uri:"+uri);
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        Log.v(LOG_TAG, "update");
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;
        switch (match){
            case MOVIE:
                rowsUpdated = db.update(MoviesContract.MovieEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri:"+uri);
        }
        if(rowsUpdated!=0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Log.v(LOG_TAG, "insert");
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        Uri returnUri;
        switch (match){
            case MOVIE:{
                long _id = db.insert(MoviesContract.MovieEntry.TABLE_NAME, null, values);
                returnUri = MoviesContract.MovieEntry.buildMoviesUri(_id);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown URi:"+uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public boolean onCreate() {
        Log.v(LOG_TAG, "create");
        dbHelper = DBHelper.getInstance(getContext());
        return true;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        Log.v(LOG_TAG, "delete");
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        int rowsDeleted;

        if(selection == null)selection = "1";
        switch (match){
            case MOVIE:
                rowsDeleted = db.delete(MoviesContract.MovieEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri:"+uri);
        }
        if(rowsDeleted!=0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Log.v(LOG_TAG, "query");
        Cursor retCursor;
        int match = sUriMatcher.match(uri);
        switch (match){
            case MOVIE_WITH_ID:{
                retCursor = getMoviebyID(uri, projection, sortOrder);
                break;
            }
            case MOVIE:{
                retCursor = dbHelper.getReadableDatabase().query(
                        MoviesContract.MovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown Uri:"+uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    private Cursor getMoviebyID(Uri uri, String[] projection, String sortOrder) {
        Log.v(LOG_TAG, "movieID");
        String movieId = MoviesContract.MovieEntry.getMovieIdFromUri(uri);
        String[] selectionArgs = new String[] {movieId};
        String selection = sMovieIDSelection;

        return dbHelper.getReadableDatabase().query(
                MoviesContract.MovieEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }
}
