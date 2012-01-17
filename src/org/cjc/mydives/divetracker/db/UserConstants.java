package org.cjc.mydives.divetracker.db;

/**
 * Constants for the User model class
 * @author JuanCarlos
 *
 */
public class UserConstants {
	public static final String DB_TABLE = "user";
	public static final String FIELD_ROWID = "_id";
	public static final String FIELD_NAME = "name";
	public static final String FIELD_SURNAME = "surname";
	public static final String FIELD_PROFILEPIC = "profilepic";

	public static String[] fields(){
		return new String[]{FIELD_ROWID, FIELD_NAME, FIELD_SURNAME, FIELD_PROFILEPIC};
	}
	
	// Database creation statement
	public static final String DATABASE_CREATE = "create table " + UserConstants.DB_TABLE + " "
			+ "(" + UserConstants.FIELD_ROWID + " integer primary key autoincrement, "
			+ UserConstants.FIELD_NAME + " text not null, "
			+ UserConstants.FIELD_SURNAME + " text not null, "
			+ UserConstants.FIELD_PROFILEPIC + " text not null)";
}
