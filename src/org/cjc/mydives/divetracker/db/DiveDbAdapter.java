package org.cjc.mydives.divetracker.db;

import java.util.Date;

import android.content.ContentValues;
import android.content.Context;

import static org.cjc.mydives.divetracker.db.DiveConstants.*;

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
	 * @param enterDate the enter date of the dive
	 * @param enterTime the enter time of the dive
	 * @param maxDeep the maximum deep of the dive
	 * @param duration the duration (in seconds) of the dive
	 * @param tempAir the temperature of the air
	 * @param tempWater the temperature of the water
	 * @param waterType sweet or salty
	 * @param rating 0...5
	 * @return dive _id if success, -1 otherwise
	 */
	public long insert(String name, Date enterDate, Date enterTime, Integer maxDeep,
			Integer duration, Double tempAir, Double tempWater, String waterType,
			Integer rating) {
		ContentValues values = createContentValues(name, enterDate, enterTime, maxDeep, 
				duration, tempAir, tempWater, waterType, rating);
		return insert(values);
	}
	
	/**
	 * Inserts a new dive in the db
	 * @param rowId the dive identifier
	 * @param name the name of the dive (name of the place)
	 * @param enterDate the enter date of the dive
	 * @param enterTime the enter time of the dive
	 * @param maxDeep the maximum deep of the dive
	 * @param duration the duration (in seconds) of the dive
	 * @param tempAir the temperature of the air
	 * @param tempWater the temperature of the water
	 * @param waterType sweet or salty
	 * @param rating 0...5
	 * @return dive _id if success, -1 otherwise
	 */
	public boolean update(long rowId, String name, Date enterDate, Date enterTime, Integer maxDeep, 
			Integer duration, Double tempAir, Double tempWater, String waterType,
			Integer rating) {
		ContentValues values = createContentValues(name, enterDate, enterTime, maxDeep, 
				duration, tempAir, tempWater, waterType, rating);
		return update(rowId, values);
	}
	
	private ContentValues createContentValues(String name, Date enterDate, Date enterTime, Integer maxDeep,
			Integer duration, Double tempAir, Double tempWater, String waterType,
			Integer rating) {
		ContentValues values = new ContentValues();
		values.put(FIELD_NAME, name);
		values.put(FIELD_ENTERDATE, FormatterHelper.packDate(enterDate));
		values.put(FIELD_ENTERTIME, FormatterHelper.packTime(enterTime));
		values.put(FIELD_DURATION, duration);
		values.put(FIELD_TEMP_AIR, tempAir);
		values.put(FIELD_TEMP_WATER, tempWater);
		values.put(FIELD_WATER_TYPE, waterType);
		values.put(FIELD_RATING, rating);
		return values;
	}
}
