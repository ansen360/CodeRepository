package org.code.gesture;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;


public class Utils {
    public static final String CONFIG = "config";
    public static final String IS_FIRST_RUN_KEY = "is_first_run";

    public static final Uri URI = Uri.parse("content://com.tomorrow_p.gesture/sleepgesture");

    public static boolean getConfig(Context context, String key, boolean def) {
        SharedPreferences sp = context.getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
        return sp.getBoolean(key, def);
    }

    public static void setConfig(Context context, String key, boolean value) {
        SharedPreferences sp = context.getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
        SharedPreferences.Editor localEditor = sp.edit();
        localEditor.putBoolean(key, value);
        localEditor.commit();
    }

    public static void insert(ContentResolver contentResolver, String gesture, String packageName, String className, int status) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("item", gesture);
        contentValues.put("pakname", packageName);
        contentValues.put("classname", className);
        contentValues.put("onoff", status);
        contentResolver.insert(URI, contentValues);
    }
    
    public static void updateByGesture(ContentResolver contentResolver,  ContentValues values,String gesture){
        contentResolver.update(URI, values, "item=?", new String[]{gesture});
    }
    
    public static String[] qureyByGesture(ContentResolver contentResolver,String gesture){
    	String where = "item=" + "'" + gesture + "'";
    	String [] values=new String[2];
    	 Cursor cursor = contentResolver.query(URI, null, where, null, null);
          if ((cursor != null) && (cursor.moveToFirst())) {
        	  values[0] = cursor.getString(cursor.getColumnIndex("pakname"));
        	  values[1]= cursor.getString(cursor.getColumnIndex("classname"));
              cursor.close();
          }
    	return values;
    }
}
