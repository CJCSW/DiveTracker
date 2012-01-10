package org.cjc.mydives.divetracker.db;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * User table
 * @author JuanCarlos
 *
 */
public class UserTable {
	// Database creation statement
	private static final String DATABASE_CREATE = "create table user "
			+ "(_id integer primary key autoincrement, "
			+ "name text not null, "
			+ "surname text not null, "
			+ "profilepic text not null)";
	
	public static void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
	}
	
	public static void onUpdate(SQLiteDatabase database, int oldVersion, int newVersion) {
		Log.w(UserTable.class.getName(), "Upgrading database from version " 
				+ oldVersion + " to version " + newVersion 
				+ " which will destroy all data");
		database.execSQL("DROP TABLE IF EXISTS todo");
		onCreate(database);
	}
}
