/**
 * 
 */
package org.cjc.mydives.divetracker.db;

import static org.cjc.mydives.divetracker.db.EquipmentConstants.DATABASE_CREATE;
import static org.cjc.mydives.divetracker.db.EquipmentConstants.DB_TABLE;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Equipment table maintenance methods
 * @author JuanCarlos
 *
 */
public class EquipmentTable {
	
	public static void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
	}
	
	public static void onUpdate(SQLiteDatabase database, int oldVersion, int newVersion) {
		Log.w(UserTable.class.getName(), "Upgrading database from version " 
				+ oldVersion + " to version " + newVersion 
				+ " which will destroy all data");
		database.execSQL("DROP TABLE IF EXISTS " + DB_TABLE);
		onCreate(database);
	}

}
