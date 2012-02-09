package org.cjc.mydives.divetracker.db;

import static org.cjc.mydives.divetracker.db.DiveConstants.FIELDS;
import static org.cjc.mydives.divetracker.db.DiveConstants.FIELD_DEPTH;
import static org.cjc.mydives.divetracker.db.DiveConstants.FIELD_LATITUDE;
import static org.cjc.mydives.divetracker.db.DiveConstants.FIELD_LONGITUDE;
import static org.cjc.mydives.divetracker.db.DiveConstants.FIELD_NAME;
import static org.cjc.mydives.divetracker.db.DiveConstants.FIELD_RATING;
import static org.cjc.mydives.divetracker.db.DiveConstants.FIELD_ROWID;
import static org.cjc.mydives.divetracker.db.DiveConstants.FIELD_TEMP_AIR;
import static org.cjc.mydives.divetracker.db.DiveConstants.FIELD_TEMP_WATER;
import static org.cjc.mydives.divetracker.db.DiveConstants.FIELD_TIME_IN;
import static org.cjc.mydives.divetracker.db.DiveConstants.FIELD_TIME_OUT;
import static org.cjc.mydives.divetracker.db.DiveConstants.FIELD_WATER_TYPE;
import static org.cjc.mydives.divetracker.db.DiveConstants.TABLE_NAME;

import org.cjc.mydives.divetracker.entity.Dive;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

/**
 * Class representing the Dive DB table.
 * @author Carlos
 *
 */
public class DiveDbAdapter extends DbAdapter {

	public DiveDbAdapter(Context ctx) {
		super(ctx);
		init(TABLE_NAME, FIELDS);
	}

	/**
	 * Inserts a new dive in the db
	 * @param name the name of the dive (name of the place)
	 * @param timeIn the enter time of the dive
	 * @param timeOut the out time of the dive
	 * @param depth the maximum deep of the dive
	 * @param duration the duration (in seconds) of the dive
	 * @param tempAir the temperature of the air
	 * @param tempWater the temperature of the water
	 * @param waterType sweet or salty
	 * @param rating 0...5
	 * @param latitude GPS
	 * @param longitude GPS
	 * @return dive _id if success, -1 otherwise
	 */
	public long insert(String name, long timeIn, long timeOut, Double depth,
			Double tempAir, Double tempWater, String waterType,
			Integer rating, Double latitude, Double longitude) {
		ContentValues values = createContentValues(name, timeIn, timeOut, depth, 
				tempAir, tempWater, waterType, rating, latitude, longitude);
		return insert(values);
	}
	
	/**
	 * Inserts a new dive in the db
	 * @param rowId the dive identifier
	 * @param name the name of the dive (name of the place)
	 * @param timeIn the enter time of the dive
	 * @param timeOut the out time of the dive
	 * @param depth the maximum deep of the dive
	 * @param duration the duration (in seconds) of the dive
	 * @param tempAir the temperature of the air
	 * @param tempWater the temperature of the water
	 * @param waterType sweet or salty
	 * @param rating 0...5
	 * @param latitude GPS
	 * @param longitude GPS
	 * @return dive _id if success, -1 otherwise
	 */
	public boolean update(long rowId, String name, long timeIn, long timeOut, Double depth, 
			Double tempAir, Double tempWater, String waterType,
			Integer rating, Double latitude, Double longitude) {
		ContentValues values = createContentValues(name, timeIn, timeOut, depth, 
				tempAir, tempWater, waterType, rating, latitude, longitude);
		return update(rowId, values);
	}
	
	private ContentValues createContentValues(String name, long timeIn, long timeOut, Double depth,
			Double tempAir, Double tempWater, String waterType,
			Integer rating, Double latitude, Double longitude) {
		ContentValues values = new ContentValues();
		values.put(FIELD_NAME, name);
		values.put(FIELD_TIME_IN, timeIn);
		values.put(FIELD_TIME_OUT, timeOut);
		values.put(FIELD_DEPTH, depth);
		values.put(FIELD_TEMP_AIR, tempAir);
		values.put(FIELD_TEMP_WATER, tempWater);
		values.put(FIELD_WATER_TYPE, waterType);
		values.put(FIELD_RATING, rating);
		values.put(FIELD_LATITUDE, latitude);
		values.put(FIELD_LONGITUDE, longitude);
		return values;
	}

	/**
	 * Loads the info from the database to the Dive entity.
	 * @param c The cursor to the database instance
	 * @param d The empty Dive instance
	 */
	public void loadInstance(Cursor c, Dive d) {
		if (c.isBeforeFirst() || c.isAfterLast()) {
			return;
		}

		d.set_id(c.getInt(c.getColumnIndex(FIELD_ROWID)));
		d.setName(c.getString(c.getColumnIndex(FIELD_NAME)));
		d.setTimeIn(c.getLong(c.getColumnIndex(FIELD_TIME_IN)));
		d.setTimeOut(c.getLong(c.getColumnIndex(FIELD_TIME_OUT)));
		d.setDepth(c.getInt(c.getColumnIndex(FIELD_DEPTH)));
		d.setLatitude(c.getDouble(c.getColumnIndex(FIELD_LATITUDE)));
		d.setLongitude(c.getDouble(c.getColumnIndex(FIELD_LONGITUDE)));
		d.setTempAir(c.getInt(c.getColumnIndex(FIELD_TEMP_AIR)));
		d.setTempWater(c.getInt(c.getColumnIndex(FIELD_TEMP_WATER)));
		d.setWaterType(c.getInt(c.getColumnIndex(FIELD_WATER_TYPE)));
		d.setRating(c.getInt(c.getColumnIndex(FIELD_RATING)));
	}
}
