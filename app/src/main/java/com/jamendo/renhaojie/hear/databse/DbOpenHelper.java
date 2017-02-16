package com.jamendo.renhaojie.hear.databse;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Ren Haojie on 2017/2/6.
 */

public class DbOpenHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "hear.db";
    private static final int DB_VERSION = 2;

    public static final String TABLE_TRACK_INFO = "track";

    public static final String TABLE_FAVORITE = "favorite";

    public static final String TABLE_DOWNLOAD = "download";

    public static final String TRACK_ID = "track_id";
    public static final String ALBUM_NAME = "album_id";
    public static final String TRACK_NAME = "track_name";
    public static final String TRACK_AUDIO = "track_audio";
    public static final String TRACK_IMAGE = "track_image";

    public static final String DLD_FILE_PATH = "file_path";

    public static final String[] FAV_ALL_COLUMNS = new String[]{TABLE_TRACK_INFO + "." + TRACK_ID, ALBUM_NAME, TRACK_NAME, TRACK_AUDIO, TRACK_IMAGE};
    public static final String[] DLD_ALL_COLUMNS = new String[]{TABLE_TRACK_INFO + "." + TRACK_ID, ALBUM_NAME, TRACK_NAME, TRACK_AUDIO, TRACK_IMAGE, DLD_FILE_PATH};

    private final String FAV_CREATE = "CREATE TABLE " + TABLE_FAVORITE + " (" +
            TRACK_ID + " TEXT " + ")";

    private final String TRACK_CREATE = "CREATE TABLE " + TABLE_TRACK_INFO + " (" +
            TRACK_ID + " TEXT, " +
            ALBUM_NAME + " TEXT, " +
            TRACK_NAME + " TEXT, " +
            TRACK_AUDIO + " TEXT, " +
            TRACK_IMAGE + " TEXT" + ")";

    private final String DLD_CREATE = "CREATE TABLE " + TABLE_DOWNLOAD + " (" +
            TRACK_ID + " TEXT, " +
            DLD_FILE_PATH + " TEXT" + ")";


    public static final String DLD_SELECTION = TABLE_TRACK_INFO + "." + TRACK_ID + "=?";

    public DbOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(FAV_CREATE);
        db.execSQL(TRACK_CREATE);
        db.execSQL(DLD_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITE);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRACK_INFO);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_DOWNLOAD);
            onCreate(db);
        }
    }
}
