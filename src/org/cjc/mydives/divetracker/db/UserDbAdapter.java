package org.cjc.mydives.divetracker.db;

import static org.cjc.mydives.divetracker.db.UserConstants.DB_TABLE;
import static org.cjc.mydives.divetracker.db.UserConstants.fields;
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
		init(DB_TABLE, fields());
	}
	
	/**
	 * Creates a new User
	 * @param name User name
	 * @param surname User surname
	 * @param profilePic User profile picture
	 * @return User id if successfully created, -1 otherwise
	 */
	public long create(String name, String surname, String profilepic) {
		ContentValues userContentValues = createContentValues(name, surname, profilepic);
		return insert(userContentValues);
	}
	
	/**
	 * Updates an existing User
	 * @param rowId User Id
	 * @param name User name
	 * @param surname User surname
	 * @param profilePic User profile picture
	 * @return True if successfully updated, false otherwise
	 */
	public boolean update(long rowId, String name, String surname, String profilepic) {
		ContentValues userContentValues = createContentValues(name, surname, profilepic);
		return update(rowId, userContentValues);
	}
	
	/**
	 * Deletes an existing User
	 * @param rowId User Id
	 * @return True if successfully deleted, false otherwise
	 */
	public boolean delete(long rowId) {
		return super.delete(rowId);
	}
	
	/**
	 * Gets an existing User by Id
	 * @param rowId User Id
	 * @return Cursor resulting from the query
	 */
	public Cursor fetchById(long rowId) {
		return super.fetchById(rowId);
	}

	/**
	 * Returns all occurrences of User
	 * @return Cursor resulting from the query
	 */
	public Cursor fetchAll() {
		return super.fetchAll();
	}
	
	/**
	 * Helper method used to encapsulate attribute values for the User entity
	 * @param name User name
	 * @param surname User surname
	 * @param profilePic User's profile picture
	 * @return ContentValues instance with all attributes of the User
	 */
	private ContentValues createContentValues(String name, String surname, String profilepic) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(UserConstants.FIELD_NAME, name);
		contentValues.put(UserConstants.FIELD_SURNAME, surname);
		contentValues.put(UserConstants.FIELD_PROFILEPIC, profilepic);
		return contentValues;
	}
}
