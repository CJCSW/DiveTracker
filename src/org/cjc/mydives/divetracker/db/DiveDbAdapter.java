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
import static org.cjc.mydives.divetracker.db.DiveConstants.FIELD_VISIBILITY;
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
	 * @param dive the dive to be created
	 * @return the _id of the new dive
	 */
	public long insert(Dive dive) {
		ContentValues values = createContentValues(dive);
		return insert(values);
		
	}
	
	/**
	 * Updates a new dive in the db
	 * @param dive the dive to be updated
	 * @return dive _id if success, -1 otherwise
	 */
	public boolean update(Dive dive) {
		ContentValues values = createContentValues(dive);
		return update(dive.get_id(), values);
	}
	
	private ContentValues createContentValues(Dive dive) {
		ContentValues values = new ContentValues();
		values.put(FIELD_NAME, dive.getName());
		values.put(FIELD_TIME_IN, dive.getTimeIn());
		values.put(FIELD_TIME_OUT, dive.getTimeOut());
		values.put(FIELD_DEPTH, dive.getDepth());
		values.put(FIELD_TEMP_AIR, dive.getTempAir());
		values.put(FIELD_TEMP_WATER, dive.getTempWater());
		values.put(FIELD_WATER_TYPE, dive.getWaterType());
		values.put(FIELD_RATING, dive.getRating());
		values.put(FIELD_LATITUDE, dive.getLatitude());
		values.put(FIELD_LONGITUDE, dive.getLongitude());
		values.put(FIELD_VISIBILITY, dive.getVisibility());
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
		d.setVisibility(c.getInt(c.getColumnIndex(FIELD_VISIBILITY)));
	}
}
