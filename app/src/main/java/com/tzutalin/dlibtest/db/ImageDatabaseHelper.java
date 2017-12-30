package com.tzutalin.dlibtest.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import timber.log.Timber;
/**
 * Created by liuqing on 4/17/17.For store the image path,id,vec
 */
public class ImageDatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "ImageDatabaseHelper";
    static final String DATABASE_NAME = "imageprovider.db";
    static final int DATABASE_VERSION = 1;
    static final String TABLE_IMAGE = "image";
    public static final String IMAGE_ID = "image_id";
    public static final String IMAGE_PATH = "image_path";
    public static final String IMAGE_VEC = "image_vec";
    public static final Uri IMAGE_TABLE_URI = Uri.parse("content://imageprovider/image");
    private Context mContext = null;
    private static ImageDatabaseHelper sInstance = null;

    private ImageDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    /* package */
    static synchronized ImageDatabaseHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new ImageDatabaseHelper(context);
        }
        return sInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createImageTable(db);
    }

    private void createImageTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_IMAGE + " (" +
                IMAGE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                IMAGE_PATH + "  TEXT," +
                IMAGE_VEC + " TEXT" + " )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int currentVersion) {
        Timber.tag(TAG).d("Upgrading database from version " + oldVersion
                + " to " + currentVersion + ".");
    }

    @Override
    public synchronized SQLiteDatabase getWritableDatabase() {
        SQLiteDatabase db = super.getWritableDatabase();
        return db;
    }

}