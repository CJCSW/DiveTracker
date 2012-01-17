package org.cjc.mydives.divetracker.db;

import java.sql.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import static org.cjc.mydives.divetracker.db.CertificationConstants.*;

/**
 * Certification database adapter
 * @author JuanCarlos
 *
 */
public class CertificationDbAdapter extends DbAdapter {
	/**
	 * Constructor
	 * @param context Context
	 */
	public CertificationDbAdapter (Context context) {
		super(context);
		init(DB_TABLE, fields());
	}
	
	/**
	 * Creates a new Certification
	 * @param type Type
	 * @param date Date of certification
	 * @param number Certification No
	 * @param organization Organization name
	 * @param instructor Instructor name
	 * @return
	 */
	public long create(String type, Date date, String number, String organization, String instructor) {
		ContentValues contentValues = createContentValues(type, date, number, organization, instructor);
		return insert(contentValues);
	}
	
	/**
	 * Updates an existing Certification
	 * @param rowId Certification
	 * @param type Type
	 * @param date Date of certification
	 * @param number Certification No
	 * @param organization Organization name
	 * @param instructor Instructor name
	 * @return True if successfully updated, false otherwise
	 */
	public boolean update(long rowId, String type, Date date, String number, String organization, String instructor) {
		ContentValues contentValues = createContentValues(type, date, number, organization, instructor);
		return update(rowId, contentValues);
	}
	
	/**
	 * Deletes an existing Certification
	 * @param rowId Certification Id
	 * @return True if successfully deleted, false otherwise
	 */
	public boolean delete(long rowId) {
		return super.delete(rowId);
	}
	
	/**
	 * Gets an existing Certification by Id
	 * @param rowId Certification Id
	 * @return Cursor resulting from the query
	 */
	public Cursor fetchById(long rowId) {
		return super.fetchById(rowId);
	}

	/**
	 * Returns all occurrences of Certification
	 * @return Cursor resulting from the query
	 */
	public Cursor fetchAll() {
		return super.fetchAll();
	}

	
	private ContentValues createContentValues(String type, Date date, String number, String organization, String instructor) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(CertificationConstants.FIELD_TYPE, type);
		contentValues.put(CertificationConstants.FIELD_DATE, date.getTime());
		contentValues.put(CertificationConstants.FIELD_NUMBER, number);
		contentValues.put(CertificationConstants.FIELD_ORGANIZATION, organization);
		contentValues.put(CertificationConstants.FIELD_INSTRUCTOR, instructor);
		return contentValues;
	}
}
