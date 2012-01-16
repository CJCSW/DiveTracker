package org.cjc.mydives.divetracker.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

/**
 * User database adapter
 * @author JuanCarlos
 *
 */
public class UserDbAdapter extends DbAdapter {

	/**
	 * Constructor
	 * @param context Context
	 */
	public UserDbAdapter (Context context) {
		super(context);
	}
	
	/**
	 * Creates a new User
	 * @param name User name
	 * @param surname User surname
	 * @param profilePic User profile picture
	 * @return User id if successfully created, -1 otherwise
	 */
	public long create(String name, String surname, byte[] profilePic) {
		ContentValues userContentValues = createContentValues(name, surname, profilePic);
		return db.insert(UserConstants.DB_TABLE, null, userContentValues);
	}
	
	/**
	 * Updates an existing User
	 * @param rowId User Id
	 * @param name User name
	 * @param surname User surname
	 * @param profilePic User profile picture
	 * @return True if successfully updated, false otherwise
	 */
	public boolean update(long rowId, String name, String surname, byte[] profilePic) {
		ContentValues userContentValues = createContentValues(name, surname, profilePic);
		return db.update(UserConstants.DB_TABLE, userContentValues, UserConstants.FIELD_ROWID + " = " + rowId, null) > 0;
	}
	
	/**
	 * Deletes an existing User
	 * @param rowId User Id
	 * @return True if successfully deleted, false otherwise
	 */
	public boolean delete(long rowId) {
		return db.delete(UserConstants.DB_TABLE, UserConstants.FIELD_ROWID + " = " + rowId, null) > 0;
	}
	
	/**
	 * Gets an existing User by Id
	 * @param rowId User Id
	 * @return Cursor resulting from the query
	 */
	public Cursor fetchById(long rowId) {
		Cursor cursor = db.query(UserConstants.DB_TABLE, UserConstants.fields(), UserConstants.FIELD_ROWID + " = " + rowId, null, null, null, null);
		if (cursor != null) {
			cursor.moveToFirst();
		}
		return cursor;
	}

	/**
	 * Returns all occurrences of User
	 * @return Cursor resulting from the query
	 */
	public Cursor fetchAll() {
		Cursor cursor = db.query(UserConstants.DB_TABLE, UserConstants.fields(), null, null, null, null, null);
		return cursor;
	}
	
	/**
	 * Helper method used to encapsulate attribute values for the User entity
	 * @param name User name
	 * @param surname User surname
	 * @param profilePic User's profile picture
	 * @return ContentValues instance with all attributes of the User
	 */
	private ContentValues createContentValues(String name, String surname, byte[] profilePic) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(UserConstants.FIELD_NAME, name);
		contentValues.put(UserConstants.FIELD_SURNAME, surname);
		contentValues.put(UserConstants.FIELD_PROFILEPIC, profilePic);
		return contentValues;
	}
}
