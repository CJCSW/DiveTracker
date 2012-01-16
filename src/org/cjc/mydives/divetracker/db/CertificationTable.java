package org.cjc.mydives.divetracker.db;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Certification table maintenance methods
 * @author JuanCarlos
 *
 */
public class CertificationTable {
	// Database creation statement
	private static final String DATABASE_CREATE = "create table " + CertificationConstants.DB_TABLE + " "
			+ "(" + CertificationConstants.FIELD_ROWID + " integer primary key autoincrement, "
			+ CertificationConstants.FIELD_TYPE + " text not null, "
			+ CertificationConstants.FIELD_DATE + " numeric not null, "
			+ CertificationConstants.FIELD_NUMBER + " text not null, "
			+ CertificationConstants.FIELD_ORGANIZATION + " text not null, "
			+ CertificationConstants.FIELD_INSTRUCTOR + " text null)";
	
	public static void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
	}
	
	public static void onUpdate(SQLiteDatabase database, int oldVersion, int newVersion) {
		Log.w(UserTable.class.getName(), "Upgrading database from version " 
				+ oldVersion + " to version " + newVersion 
				+ " which will destroy all data");
		database.execSQL("DROP TABLE IF EXISTS " + CertificationConstants.DB_TABLE);
		onCreate(database);
	}
}
