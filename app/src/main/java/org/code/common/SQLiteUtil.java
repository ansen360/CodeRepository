package org.code.common;

import android.database.sqlite.SQLiteDatabase;

public class SQLiteUtil {
	
	/**
	 * 连接数据库/没有时创建
	 * @return
	 */
	public static SQLiteDatabase getSQLiteDB(String dburl){
		SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dburl, null);
		
		return db;
	}
	
	/**
	 * 关闭数据库连接
	 * @param db
	 */
	public static void closeSQLiteDB(SQLiteDatabase db){
		if(db != null && db.isOpen()){
			db.close();
		}
	}
	
}
