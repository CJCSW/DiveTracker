package org.cjc.mydives.divetracker.db;

import android.content.ContentValues;
import android.content.Context;

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
		init(UserConstants.DB_TABLE, UserConstants.fields());
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
	public boolean update(long rowId, String name, String surname, byte[] profilePic) {
		ContentValues userContentValues = createContentValues(name, surname, profilePic);
		return update(rowId, userContentValues);
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
