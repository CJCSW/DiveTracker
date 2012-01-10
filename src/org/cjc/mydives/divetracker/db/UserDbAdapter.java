/**
 * 
 */
package org.cjc.mydives.divetracker.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * User database adapter
 * @author JuanCarlos
 *
 */
public class UserDbAdapter {

	// Table fields
	public static final String KEY_ROWID = "_id";
	public static final String KEY_NAME = "name";
	public static final String KEY_SURNAME = "surname";
	public static final String KEY_PROFILEPIC = "profilepic";
	private static final String DB_TABLE = "user";
	private Context context;
	private SQLiteDatabase db;
	private DbHelper dbHelper;
	
	/**
	 * Constructor
	 * @param context Context
	 */
	public UserDbAdapter (Context context) {
		this.context = context;
	}
	
	/**
	 * Opens the database for write operations
	 * @return Database adapter for the User entity
	 * @throws SQLException if failed to create a new database helper or to get a writable database 
	 */
	public UserDbAdapter open() throws SQLException{
		dbHelper = new DbHelper(context);
		dbHelper.getWritableDatabase();
		return this;
	}
	
	public void close() {
		dbHelper.close();
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
		contentValues.put(KEY_NAME, name);
		contentValues.put(KEY_SURNAME, surname);
		contentValues.put(KEY_PROFILEPIC, profilePic);
		return contentValues;
	}
}
