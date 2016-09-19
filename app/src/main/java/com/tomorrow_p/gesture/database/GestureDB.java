package com.tomorrow_p.gesture.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class GestureDB extends SQLiteOpenHelper {

    private static final String name = "gesture.db";
    private static final int version = 1;

    GestureDB(Context context) {
        super(context, name, null, version);
    }

    public GestureDB(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE gesture (_id integer primary key autoincrement,item varchar(20),pakname varchar(20),classname varchar(20),onoff INTEGER(12))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS gesture");
        onCreate(db);
    }

}
