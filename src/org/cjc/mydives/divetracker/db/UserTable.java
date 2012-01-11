package org.cjc.mydives.divetracker.db;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * User table maintenance methods
 * @author JuanCarlos
 *
 */
public class UserTable {
	// Database creation statement
	private static final String DATABASE_CREATE = "create table " + UserConstants.DB_TABLE + " "
			+ "(" + UserConstants.FIELD_ROWID + " integer primary key autoincrement, "
			+ UserConstants.FIELD_NAME + " text not null, "
			+ UserConstants.FIELD_SURNAME + " text not null, "
			+ UserConstants.FIELD_PROFILEPIC + " text not null)";
	
	public static void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
	}
	
	public static void onUpdate(SQLiteDatabase database, int oldVersion, int newVersion) {
		Log.w(UserTable.class.getName(), "Upgrading database from version " 
				+ oldVersion + " to version " + newVersion 
				+ " which will destroy all data");
		database.execSQL("DROP TABLE IF EXISTS " + UserConstants.DB_TABLE);
		onCreate(database);
	}
}
