package org.cjc.mydives.divetracker.db;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import static org.cjc.mydives.divetracker.db.DiveConstants.DATABASE_CREATE;
import static org.cjc.mydives.divetracker.db.DiveConstants.TABLE_NAME;

public class DiveTable {
	public static void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
	}
	
	public static void onUpdate(SQLiteDatabase database, int oldVersion, int newVersion) {
		Log.w(UserTable.class.getName(), "Upgrading database from version " 
				+ oldVersion + " to version " + newVersion 
				+ " which will destroy all data");
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		onCreate(database);
	}

}
