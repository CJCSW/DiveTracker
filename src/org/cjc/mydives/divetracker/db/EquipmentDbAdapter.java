/**
 * 
 */
package org.cjc.mydives.divetracker.db;

import static org.cjc.mydives.divetracker.db.EquipmentConstants.DB_TABLE;
import static org.cjc.mydives.divetracker.db.EquipmentConstants.fields;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

/**
 * Equipment database adapter
 * @author JuanCarlos
 *
 */
public class EquipmentDbAdapter extends DbAdapter {

	/**
	 * Constructor
	 * @param context
	 */
	public EquipmentDbAdapter(Context context) {
		super(context);
		init(DB_TABLE, fields());
	}
	
	/**
	 * Creates a new Equipment item
	 * @param user_id User Id if this equipment item is associated to the user profile
	 * @param dive_id Dive Id if this equipment item is associated to a particular dive
	 * @param name Name of the equipment item
	 * @param active Whether this equipment item is active/used or not
	 * @return
	 */
	public long create(Long user_id, Long dive_id, String name, long active){
		return insert(createContentValues(user_id, dive_id, name, active));
	}
	
	/**
	 * Updates an existing Equipment item
	 * @param rowId Equipment item Id
	 * @param user_id User Id if this equipment item is associated to the user profile
	 * @param dive_id Dive Id if this equipment item is associated to the user profile
	 * @param name Name of the equipment item
	 * @param active Whether this equipment item is active/used or not
	 * @return
	 */
	public boolean update(long rowId, Long user_id, Long dive_id, String name, long active) {
		return update(rowId, createContentValues(user_id, dive_id, name, active));
	}
	
	/**
	 * Deletes an existing Equipment item
	 * @param rowId Equipment item Id
	 * @return True if successfully deleted, false otherwise
	 */
	public boolean delete(long rowId) {
		return super.delete(rowId);
	}
	
	/**
	 * Gets an existing Equipment item by Id
	 * @param rowId Equipment item Id
	 * @return Cursor resulting from the query
	 */
	public Cursor fetchById(long rowId) {
		return super.fetchById(rowId);
	}

	/**
	 * Returns all occurrences of Equipment
	 * @return Cursor resulting from the query
	 */
	public Cursor fetchAll() {
		return super.fetchAll();
	}

	/**
	 * Returns all occurrences of Equipment related to the User profile
	 * @return Cursor resulting from the query
	 */
	public Cursor fecthAllByUser(Long user_id) {
		return db.query(DB_TABLE, fields(), EquipmentConstants.FIELD_USERID + " = " + user_id, null, null, null, null);
	}
	
	/**
	 * Returns all occurrences of Equipment related to a given Dive
	 * @return Cursor resulting from the query
	 */
	public Cursor fetchAllByDive(Long dive_id) {
		return db.query(DB_TABLE, fields(), EquipmentConstants.FIELD_DIVEID + " = " + dive_id, null, null, null, null);
	}
	
	/**
	 * Helper method used to encapsulate attribute values for the Equipment entity
	 * @param user_id User Id if this equipment item is associated to the user profile
	 * @param dive_id Dive Id if this equipment item is associated to a particular dive
	 * @param name Name of the equipment item
	 * @param active Whether this equipment item is active/used or not
	 * @return
	 */
	private ContentValues createContentValues(Long user_id, Long dive_id, String name, long active) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(EquipmentConstants.FIELD_USERID, user_id);
		contentValues.put(EquipmentConstants.FIELD_DIVEID, dive_id);
		contentValues.put(EquipmentConstants.FIELD_NAME, name);
		contentValues.put(EquipmentConstants.FIELD_ACTIVE, active);
		return contentValues;
	}
}
