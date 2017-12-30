package com.tzutalin.dlibtest.db;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import com.tzutalin.dlibtest.db.ImageDatabaseHelper;

/**
 * Created by liuqing on 4/17/17.For store the image path,id,vec
 */

public class ImageProvider extends ContentProvider {
    private static final String TAG = "ImageProvider";
    private SQLiteOpenHelper mOpenHelper;
    private static final UriMatcher URI_MATCHER =
            new UriMatcher(UriMatcher.NO_MATCH);

    @Override
    public boolean onCreate() {
        mOpenHelper = ImageDatabaseHelper.getInstance(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection,
                        String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        Cursor cursor = null;
        cursor = db.query(ImageDatabaseHelper.TABLE_IMAGE, projection, selection, null,
                null, null, sortOrder, null);
        return cursor;
    }


    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        return 0;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
                SQLiteDatabase db = mOpenHelper.getWritableDatabase();
                long rowId = db.insert(ImageDatabaseHelper.TABLE_IMAGE, null, values);
                return Uri.parse(uri + "/" + rowId);
    }

    @Override
    public int delete(Uri uri, String selection,
                      String[] selectionArgs) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        return 0;
    }

    @Override
    public String getType(Uri uri) {
        return "imageprovider";
    }


}