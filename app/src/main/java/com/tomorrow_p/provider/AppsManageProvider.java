package com.tomorrow_p.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by Ansen on 2016/11/25 10:18.
 *
 * @E-mail: tomorrow_p@163.com
 * @Blog: http://blog.csdn.net/qq_25804863
 * @Github: https://github.com/ansen360
 * @PROJECT_NAME: CodeRepository
 * @PACKAGE_NAME: com.tomorrow_p.provider
 * @Description: TODO
 */
public class AppsManageProvider extends ContentProvider {

    private SQLiteDatabase db;
    private static UriMatcher mUriMatcher;
    private AppsManageDB mAppsManageDB;
    private static final String AUTHORITY = "com.tomorrow_p.apps";
    private static final int APPS = 0;
    private static final int GESTURE = 1;
    private static final int KEEP_ALIVE = 2;
    private static final int KEEP_ALIVE_ID = 3;
    private static final int AUTOSTART = 4;
    private static final int AUTOSTART_ID = 5;

    static {
        mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        mUriMatcher.addURI(AUTHORITY, "apps", APPS);
        mUriMatcher.addURI(AUTHORITY, "gesture/#", GESTURE);
        mUriMatcher.addURI(AUTHORITY, "keep_alive", KEEP_ALIVE);
        mUriMatcher.addURI(AUTHORITY, "keep_alive/#", KEEP_ALIVE_ID);
        mUriMatcher.addURI(AUTHORITY, "autostart", AUTOSTART);
        mUriMatcher.addURI(AUTHORITY, "autostart/#", AUTOSTART_ID);
    }

    @Override
    public boolean onCreate() {
         mAppsManageDB = AppsManageDB.getInstance(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        db = mAppsManageDB.getWritableDatabase();
        String where;
        switch (mUriMatcher.match(uri)) {
            case AUTOSTART:
                return db.query(AppsManageDB.TABLE1, projection, selection, selectionArgs, null, null, sortOrder);
            case GESTURE:
            case AUTOSTART_ID:
                long id = ContentUris.parseId(uri);
                where = "_id=" + id;
                if (selection != null && !"".equals(selection.trim())) {
                    where = selection + " and " + where;
                }
                break;
            default:
                throw new IllegalArgumentException("Unkwon Uri:" + uri.toString());
        }
        return db.query(AppsManageDB.TABLE1, projection, where, selectionArgs, null, null, sortOrder);
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (mUriMatcher.match(uri)) {
            case APPS:
                return "vnd.android.cursor.dir/+AUTHORITY.sleepgestures";
            case GESTURE:
                return "vnd.android.cursor.item/+AUTHORITY.sleepgesture";
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        db = mAppsManageDB.getWritableDatabase();
        switch (mUriMatcher.match(uri)) {
            case AUTOSTART:
                long rowId = db.insert(AppsManageDB.TABLE1, null, values);
                if (rowId > 0) {//判断插入是否执行成功
                    Uri insertedUserUri = ContentUris.withAppendedId(uri, rowId);
                    getContext().getContentResolver().notifyChange(insertedUserUri, null);
                    return insertedUserUri;
                } else {
                    return uri;
                }
            case APPS:
                long newId = db.insert(AppsManageDB.TABLE1, null, values);
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
        db = mAppsManageDB.getWritableDatabase();
        switch (mUriMatcher.match(uri)) {
            case AUTOSTART:
                return db.delete(AppsManageDB.TABLE1, selection, selectionArgs);
            case GESTURE:
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
        db = mAppsManageDB.getWritableDatabase();
        switch (mUriMatcher.match(uri)) {
            case AUTOSTART:
                return db.update(AppsManageDB.TABLE1, values, selection, selectionArgs);
            case GESTURE:
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
