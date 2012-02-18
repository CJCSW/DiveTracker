package org.cjc.mydives.divetracker.db;

import static org.cjc.mydives.divetracker.db.UserConstants.DB_TABLE;
import static org.cjc.mydives.divetracker.db.UserConstants.fields;

import org.cjc.mydives.divetracker.entity.Equipment;
import org.cjc.mydives.divetracker.entity.User;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

/**
 * User database adapter
 * @author JuanCarlos
 *
 */
public class UserDbAdapter extends DbAdapter {

	private Context context;
	
	/**
	 * Constructor
	 * @param context Context
	 */
	public UserDbAdapter (Context context) {
		super(context);
		this.context = context;
		init(DB_TABLE, fields());
	}
	
	/**
	 * Creates a new User
	 * @param name User name
	 * @param surname User surname
	 * @param profilePic User profile picture
	 * @return User id if successfully created, -1 otherwise
	 */
	public long create(User user) {
		long user_id = insert(createContentValues(user));
		
		// Associate default equipment list to user profile
		EquipmentDbAdapter equipmentDbAdapter = new EquipmentDbAdapter(context);
		equipmentDbAdapter.open();
		equipmentDbAdapter.create(new Equipment(user_id, null, "Mask", true));
		equipmentDbAdapter.create(new Equipment(user_id, null, "BCD", true));
		equipmentDbAdapter.create(new Equipment(user_id, null, "Regulator", true));
		equipmentDbAdapter.create(new Equipment(user_id, null, "Fins", true));
		equipmentDbAdapter.create(new Equipment(user_id, null, "Wet suit", false));
		equipmentDbAdapter.create(new Equipment(user_id, null, "Dry suit", false));
		equipmentDbAdapter.create(new Equipment(user_id, null, "Compass", false));
		equipmentDbAdapter.create(new Equipment(user_id, null, "Dive computer", false));
		equipmentDbAdapter.create(new Equipment(user_id, null, "Torch", false));
		equipmentDbAdapter.create(new Equipment(user_id, null, "Camera", false));
		equipmentDbAdapter.create(new Equipment(user_id, null, "Buoy", true));
		equipmentDbAdapter.close();
		
		return user_id;
	}
	
	/**
	 * Updates an existing User
	 * @param rowId User Id
	 * @param name User name
	 * @param surname User surname
	 * @param profilePic User profile picture
	 * @return True if successfully updated, false otherwise
	 */
	public boolean update(User user) {
		return update(user.get_id(), createContentValues(user));
	}
	
	/**
	 * Deletes an existing User
	 * @param rowId User Id
	 * @return True if successfully deleted, false otherwise
	 */
	public boolean delete(User user) {
		return super.delete(user.get_id());
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
	private ContentValues createContentValues(User user) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(UserConstants.FIELD_NAME, user.getName());
		contentValues.put(UserConstants.FIELD_SURNAME, user.getSurname());
		contentValues.put(UserConstants.FIELD_PROFILEPIC, user.getProfilepic());
		return contentValues;
	}
}
