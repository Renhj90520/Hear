package com.jamendo.renhaojie.hear.providers;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.jamendo.renhaojie.hear.databse.DbOpenHelper;

/**
 * Created by Ren Haojie on 2017/2/7.
 */

public class DownLoadProvider extends ContentProvider {

    private static final String AUTHORITY = "com.jamendo.renhaojie.hear.downloadprovider";
    private static final String BASE_PATH = "downloads";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);

    private static final int DOWNLOADS = 1;
    private static final int DOWNLOAD_TRACKID = 2;

    private static final UriMatcher mMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        mMatcher.addURI(AUTHORITY, BASE_PATH, DOWNLOADS);
        mMatcher.addURI(AUTHORITY, BASE_PATH + "/#", DOWNLOAD_TRACKID);
    }

    SQLiteDatabase mDatabase;

    @Override
    public boolean onCreate() {
        DbOpenHelper helper = new DbOpenHelper(getContext());
        mDatabase = helper.getReadableDatabase();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        if (mMatcher.match(uri) == DOWNLOAD_TRACKID) {
            selection = DbOpenHelper.TABLE_TRACK_INFO + "." + DbOpenHelper.TRACK_ID + "=?";
        }
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(DbOpenHelper.TABLE_DOWNLOAD + " LEFT JOIN " + DbOpenHelper.TABLE_TRACK_INFO
                + " ON "
                + DbOpenHelper.TABLE_TRACK_INFO + "." + DbOpenHelper.TRACK_ID
                + "="
                + DbOpenHelper.TABLE_DOWNLOAD + "." + DbOpenHelper.TRACK_ID);
        Cursor cursor = builder.query(mDatabase,
                DbOpenHelper.DLD_ALL_COLUMNS,
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
        ContentValues dld_values = new ContentValues();
        dld_values.put(DbOpenHelper.TRACK_ID, id);
        dld_values.put(DbOpenHelper.DLD_FILE_PATH, values.getAsString(DbOpenHelper.DLD_FILE_PATH));

        values.remove(DbOpenHelper.DLD_FILE_PATH);
        mDatabase.insert(DbOpenHelper.TABLE_DOWNLOAD, null, dld_values);
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
        return mDatabase.delete(DbOpenHelper.TABLE_DOWNLOAD, DbOpenHelper.TRACK_ID + "=?", new String[]{uri.getLastPathSegment()});
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
