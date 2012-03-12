package org.cjc.mydives.divetracker.db;

import static org.cjc.mydives.divetracker.db.CertificationConstants.DB_TABLE;
import static org.cjc.mydives.divetracker.db.CertificationConstants.fields;

import org.cjc.mydives.divetracker.entity.Certification;

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
	public long create(Certification certification) {
		return insert(createContentValues(certification));
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
	public boolean update(Certification certification) {
		return update(certification.get_id(), createContentValues(certification));
	}
	
	/**
	 * Deletes an existing Certification
	 * @param rowId Certification Id
	 * @return True if successfully deleted, false otherwise
	 */
	public boolean delete(Certification certification) {
		return super.delete(certification.get_id());
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

	
	private ContentValues createContentValues(Certification certification) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(CertificationConstants.FIELD_TYPE, certification.getType());
		contentValues.put(CertificationConstants.FIELD_DATE, certification.getDate());
		contentValues.put(CertificationConstants.FIELD_NUMBER, certification.getNumber());
		contentValues.put(CertificationConstants.FIELD_ORGANIZATION, certification.getOrganization());
		contentValues.put(CertificationConstants.FIELD_INSTRUCTOR, certification.getInstructor());
		return contentValues;
	}
}
