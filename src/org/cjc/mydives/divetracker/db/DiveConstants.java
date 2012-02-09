package org.cjc.mydives.divetracker.db;

public class DiveConstants {
	public static final String TABLE_NAME = "dive";
	/* Fields */
	public static final String FIELD_ROWID = "_id";
	public static final String FIELD_NAME  = "name";
	public static final String FIELD_TIME_IN   = "timeIn";
	public static final String FIELD_TIME_OUT  = "timeOut";
	public static final String FIELD_DEPTH  = "depth";			// In meters
	public static final String FIELD_TEMP_AIR   = "temp_air";	// In C
	public static final String FIELD_TEMP_WATER = "temp_water";	// In C
	public static final String FIELD_WATER_TYPE = "water_type";	// [freshwater, saltwater]
	public static final String FIELD_RATING     = "rating";		// 0 - 5
	public static final String FIELD_LATITUDE   = "latitude";	// double
	public static final String FIELD_LONGITUDE  = "longitude";	// double
	
	/* Field list */
	public static final String[] FIELDS = new String[] {
		FIELD_ROWID, FIELD_NAME, FIELD_TIME_IN, FIELD_TIME_OUT,
		FIELD_DEPTH, FIELD_TEMP_AIR, FIELD_TEMP_WATER,
		FIELD_WATER_TYPE, FIELD_RATING, FIELD_LATITUDE, FIELD_LONGITUDE
	};
	
	/* Queries */
	public static final String DATABASE_CREATE = 
			"create table " + TABLE_NAME
			+ " (" + FIELD_ROWID + " integer primary key autoincrement,"
			+ FIELD_NAME + " text null,"
			+ FIELD_TIME_IN + " integer not null,"
			+ FIELD_TIME_OUT + " integer null,"
			+ FIELD_DEPTH + " real null,"
			+ FIELD_TEMP_AIR + " real null,"
			+ FIELD_TEMP_WATER + " real null,"
			+ FIELD_WATER_TYPE + " text null,"
			+ FIELD_RATING + " integer null,"
			+ FIELD_LATITUDE + " real null,"
			+ FIELD_LONGITUDE + " real null"
			+ ");";
}
