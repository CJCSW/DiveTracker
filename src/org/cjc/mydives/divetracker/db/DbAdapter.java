package org.cjc.mydives.divetracker.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * Generic database adapter
 * @author JuanCarlos
 *
 */
public class DbAdapter {
	
	protected Context context;
	protected SQLiteDatabase db;
	protected DbHelper dbHelper;
	
	private String tableName;
	private String[] fields;
	
	/**
	 * Constructor
	 * @param context Context
	 */
	public DbAdapter (Context context) {
		this.context = context;
	}
	
	/**
	 * Initializes the parameters of this adapter.
	 * @param tableName the name of the database table.
	 * @param fields the fields in the database table.
	 */
	protected void init(String tableName, String[] fields) {
		this.tableName = tableName;
		this.fields = fields;
	}
	
	/**
	 * Opens the database for write operations
	 * @return Database adapter
	 * @throws SQLException if failed to create a new database helper or to get a writable database 
	 */
	public DbAdapter open() throws SQLException{
		dbHelper = new DbHelper(context);
		db = dbHelper.getWritableDatabase();
		return this;
	}
	
	public void close() {
		dbHelper.close();
	}

	/**
	 * Inserts a row in the database.
	 * @param tableName the name of the table.
	 * @param initialValues
	 * @return
	 */
	public long insert(ContentValues initialValues) {
		return db.insert(tableName, null, initialValues);		
	}

	/**
	 * Updates a row in the database.
	 * @param rowId the identifier of the row to be updated.
	 * @param values the values to update.
	 * @return true if the row has been updated, false otherwise.
	 */
	public boolean update(long rowId, ContentValues values) {
		return db.update(tableName, values, fields[0] + "=" + String.valueOf(rowId), null) > 0;
	}

	/**
	 * Deletes a row in the database.
	 * @param rowId the identifier of the row to be deleted.
	 * @return true if the row has been deleted, false otherwise.
	 */
	public boolean delete(long rowId) {
		return db.delete(tableName, fields[0] + " = " + String.valueOf(rowId), null) > 0;		
	}

	/**
	 * Gets an existing row by Id
	 * @param rowId the Id of the row
	 * @return Cursor resulting from the query
	 */
	public Cursor fetchById(long rowId) {
		Cursor cursor = db.query(tableName, fields, fields[0] + " = " + rowId, null, null, null, null);
		if (cursor != null) {
			cursor.moveToFirst();
		}
		return cursor;
	}

	/**
	 * Fetch all the rows in the DB
	 */
	public Cursor fetchAll() {
		return db.query(tableName, fields, null, null, null, null, null);
	}
}
