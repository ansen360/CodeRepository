package com.ansen.provider;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Ansen on 2016/11/24 16:36.
 *
 * @E-mail: tomorrow_p@163.com
 * @Blog: http://blog.csdn.net/qq_25804863
 * @Github: https://github.com/ansen360
 * @PROJECT_NAME: CodeRepository
 * @PACKAGE_NAME: com.tomorrow_p.provider
 * @Description: TODO
 */
public class AppsManageDB extends SQLiteOpenHelper {

    private static final String NAME = "apps_manage.db";
    public static final String TABLE1 = "autostart";
    private static final int VERSION = 1;
    private static AppsManageDB sInstance;

    private AppsManageDB(Context context) {
        super(context, NAME, null, VERSION);
    }

    private AppsManageDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public AppsManageDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    public static synchronized AppsManageDB getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new AppsManageDB(context);
        }
        return sInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE1 + " (_id integer primary key autoincrement,app_name varchar(20),package_name varchar(20))");
//        db.execSQL("CREATE TABLE " + TABLE1 + " (_id integer primary key autoincrement,app_name varchar(20)," +
//                "package_name varchar(20),class_name varchar(20),keep_alive INTEGER NOT NULL DEFAULT 0," +
//                "autostart INTEGER NOT NULL DEFAULT 0,gesture_action varchar(20)," +
//                "gesture_status INTEGER NOT NULL DEFAULT 0)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
