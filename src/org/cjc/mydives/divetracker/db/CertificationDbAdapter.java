package org.cjc.mydives.divetracker.db;

import java.sql.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

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
		return db.insert(CertificationConstants.DB_TABLE, null, contentValues);
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
		return db.update(CertificationConstants.DB_TABLE, contentValues, CertificationConstants.FIELD_ROWID + "= " + rowId, null) > 0;
	}
	
	/**
	 * Deletes an existing Certification
	 * @param rowId Certification Id
	 * @return True if successfully deleted, false otherwise
	 */
	public boolean delete(long rowId) {
		return db.delete(CertificationConstants.DB_TABLE, CertificationConstants.FIELD_ROWID + " = " + rowId, null) > 0;
	}
	
	/**
	 * Gets an existing Certification by Id
	 * @param rowId Certification Id
	 * @return Cursor resulting from the query
	 */
	public Cursor fetchById(long rowId) {
		Cursor cursor = db.query(CertificationConstants.DB_TABLE, CertificationConstants.fields(), CertificationConstants.FIELD_ROWID + " = " + rowId, null, null, null, null);
		if (cursor != null) {
			cursor.moveToFirst();
		}
		return cursor;
	}

	/**
	 * Returns all occurrences of Certification
	 * @return Cursor resulting from the query
	 */
	public Cursor fetchAll() {
		Cursor cursor = db.query(CertificationConstants.DB_TABLE, CertificationConstants.fields(), null, null, null, null, null);
		return cursor;
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
