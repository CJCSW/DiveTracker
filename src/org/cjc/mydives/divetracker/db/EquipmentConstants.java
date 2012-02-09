/**
 * 
 */
package org.cjc.mydives.divetracker.db;

/**
 * Constants for the Equipment model class
 * @author JuanCarlos
 *
 */
public class EquipmentConstants {
	public static final String DB_TABLE = "Equipment";
	public static final String FIELD_ROWID = "_id";
	public static final String FIELD_USERID = "user_id";
	public static final String FIELD_DIVEID = "dive_id";
	public static final String FIELD_NAME = "name";
	public static final String FIELD_ACTIVE = "active";

	public static String[] fields(){
		return new String[]{FIELD_ROWID, FIELD_USERID, FIELD_DIVEID, FIELD_NAME, FIELD_ACTIVE};
	}

	// Database creation statement
	public static final String DATABASE_CREATE = "create table " + DB_TABLE + " "
			+ "(" + FIELD_ROWID + " integer primary key autoincrement, "
			+ FIELD_USERID + " integer null, "
			+ FIELD_DIVEID + " integer null, "
			+ FIELD_NAME + " text not null, "
			+ FIELD_ACTIVE + " integer not null)";
}
