package ru.devtron.dagturism.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "favoritesDB";


    public static final String TABLE_PLACES = "places";
    public static final String TABLE_IMAGES = "images";
    public static final String TABLE_POINTS = "points";

    public static final String KEY_ID = "_id";
    public static final String KEY_PLACE_ID = "place_id";
    public static final String KEY_TITLE = "title";
    public static final String KEY_CITY = "city";
    public static final String KEY_LAT = "lat";
    public static final String KEY_LNG = "lng";
    public static final String KEY_DESCRIPTION = "desc";

    public static final String KEY_IMAGE_URL = "url";

    public static final String KEY_POINT_LAT = "point_latitude";
    public static final String KEY_POINT_LNG = "point_longitude";
    public static final String KEY_POINT_NUMBER = "point_number";
    public static final String KEY_POINT_CAPTION = "point_caption";
    public static final String KEY_POINTS_PRICE = "points_price";


    private static final String CREATE_TABLE_PLACES = "CREATE TABLE "
            + TABLE_PLACES + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_PLACE_ID
            + " TEXT," + KEY_TITLE + " TEXT," + KEY_CITY + " TEXT," + KEY_LAT + " TEXT,"
            + KEY_LNG + " TEXT," + KEY_DESCRIPTION + " TEXT"  + ")";

    private static final String CREATE_TABLE_IMAGES = "CREATE TABLE "
            + TABLE_IMAGES + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_PLACE_ID + " TEXT,"
            + KEY_IMAGE_URL + " TEXT" + ")";

    private static final String CREATE_TABLE_POINTS = "CREATE TABLE "
            + TABLE_POINTS + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_PLACE_ID + " TEXT,"
            + KEY_POINT_CAPTION + " TEXT," + KEY_POINT_NUMBER + " TEXT,"
            + KEY_POINT_LAT + " TEXT," +  KEY_POINT_LNG + " TEXT," + KEY_POINTS_PRICE + " TEXT" + ")";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_PLACES);
        db.execSQL(CREATE_TABLE_IMAGES);
        db.execSQL(CREATE_TABLE_POINTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLACES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_IMAGES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_POINTS);
        onCreate(db);
    }
}
