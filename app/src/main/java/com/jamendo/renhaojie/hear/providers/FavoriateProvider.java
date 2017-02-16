package com.jamendo.renhaojie.hear.providers;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.jamendo.renhaojie.hear.databse.DbOpenHelper;

/**
 * Created by Ren Haojie on 2017/2/6.
 */

public class FavoriateProvider extends ContentProvider {
    private static final String AUTHORITY = "com.jamendo.renhaojie.hear.favoriteprovider";
    private static final String BASE_PATH = "favorites";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);

    private static final int FAVORITES = 1;
    private static final int FAVORITE_TRACKID = 2;

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(AUTHORITY, BASE_PATH, FAVORITES);
        uriMatcher.addURI(AUTHORITY, BASE_PATH + "/#", FAVORITE_TRACKID);
    }

    SQLiteDatabase mDatabase;

    @Override
    public boolean onCreate() {
        DbOpenHelper helper = new DbOpenHelper(this.getContext());
        mDatabase = helper.getWritableDatabase();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        if (uriMatcher.match(uri) == FAVORITE_TRACKID) {
            selection = DbOpenHelper.TABLE_TRACK_INFO + "." + DbOpenHelper.TRACK_ID + "=?";
        }
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(DbOpenHelper.TABLE_FAVORITE + " LEFT JOIN " + DbOpenHelper.TABLE_TRACK_INFO
                + " ON "
                + DbOpenHelper.TABLE_TRACK_INFO + "." + DbOpenHelper.TRACK_ID
                + "="
                + DbOpenHelper.TABLE_FAVORITE + "." + DbOpenHelper.TRACK_ID);
        Cursor cursor = builder.query(mDatabase,
                DbOpenHelper.FAV_ALL_COLUMNS,
                selection,
                new String[]{uri.getLastPathSegment()},//selectionArgs
                null,//groupby
                null,//having
                null//orderby
        );

        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        String id = values.getAsString(DbOpenHelper.TRACK_ID);
        ContentValues fav_values = new ContentValues();
        fav_values.put(DbOpenHelper.TRACK_ID, id);
        mDatabase.insert(DbOpenHelper.TABLE_FAVORITE, null, fav_values);
        Cursor track = mDatabase.query(DbOpenHelper.
                        TABLE_TRACK_INFO, new String[]{DbOpenHelper.TRACK_ID},
                DbOpenHelper.TRACK_ID + "=?",
                new String[]{id},
                null,
                null,
                null
        );
        if (track.getCount() == 0) {
            mDatabase.insert(DbOpenHelper.TABLE_TRACK_INFO, null, values);
        }
        return Uri.parse(BASE_PATH + "/" + id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        String trackId = uri.getLastPathSegment();
        String innerSelection = DbOpenHelper.TRACK_ID + "=?";
        return mDatabase.delete(DbOpenHelper.TABLE_FAVORITE,
                innerSelection,
                new String[]{trackId}
        );
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
