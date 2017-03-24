package com.ansen.gesture.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class GestureContentProvider extends ContentProvider {

    private static final int SLEEPGESTURE = 0;
    private static final int SLEEPGESTURES = 1;
    private GestureDB mGestureDB;
    private static UriMatcher mUriMatcher;

    static {
        mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        mUriMatcher.addURI("com.tomorrow_p.gesture", "sleepgesture", SLEEPGESTURE);
        mUriMatcher.addURI("com.tomorrow_p.gesture", "sleepgesture/#", SLEEPGESTURES);
    }

    @Override
    public boolean onCreate() {
        mGestureDB = new GestureDB(getContext());
        return true;
    }


    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = mGestureDB.getWritableDatabase();
        switch (mUriMatcher.match(uri)) {
            case SLEEPGESTURE:
                return db.query("gesture", projection, selection, selectionArgs, null, null, sortOrder);
            case SLEEPGESTURES:
                long id = ContentUris.parseId(uri);
                String where = "_id=" + id;
                if (selection != null && !"".equals(selection.trim())) {
                    where = selection + " and " + where;
                }
                return db.query("gesture", projection, where, selectionArgs, null, null, sortOrder);
            default:
                throw new IllegalArgumentException("Unkwon Uri:" + uri.toString());
        }
    }

    @Override
    public String getType(Uri uri) {
        switch (mUriMatcher.match(uri)) {
            case SLEEPGESTURE:
                return "vnd.android.cursor.dir/+AUTHORITY.sleepgestures";
            case SLEEPGESTURES:
                return "vnd.android.cursor.item/+AUTHORITY.sleepgesture";
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = mGestureDB.getWritableDatabase();
        switch (mUriMatcher.match(uri)) {
            case SLEEPGESTURE:
                long newId = db.insert("gesture", null, values);
                if (newId > 0) {
                    Uri insertUri = ContentUris.withAppendedId(uri, newId);
                    getContext().getContentResolver().notifyChange(uri, null);
                    return insertUri;
                }
            default:
                throw new IllegalArgumentException("Unkwon Uri:" + uri.toString());
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mGestureDB.getWritableDatabase();
        switch (mUriMatcher.match(uri)) {
            case SLEEPGESTURE:
                return db.delete("gesture", selection, selectionArgs);
            case SLEEPGESTURES:
                long id = ContentUris.parseId(uri);
                String where = "_id=" + id;
                if (selection != null && !"".equals(selection)) {
                    where = selection + " and " + where;
                }
                return db.delete("gesture", where, selectionArgs);
            default:
                throw new IllegalArgumentException("Unkwon Uri:" + uri.toString());
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mGestureDB.getWritableDatabase();
        switch (mUriMatcher.match(uri)) {
            case SLEEPGESTURE:
                return db.update("gesture", values, selection, selectionArgs);
            case SLEEPGESTURES:
                long id = ContentUris.parseId(uri);
                String where = "_id=" + id;
                if (selection != null && !"".equals(selection)) {
                    where = selection + " and " + where;
                }
                return db.update("gesture", values, where, selectionArgs);
            default:
                throw new IllegalArgumentException("Unkwon Uri:" + uri.toString());
        }
    }
}
