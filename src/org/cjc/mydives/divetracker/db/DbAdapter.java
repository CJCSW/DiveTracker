package org.cjc.mydives.divetracker.db;

import android.content.Context;
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
	
	/**
	 * Constructor
	 * @param context Context
	 */
	public DbAdapter (Context context) {
		this.context = context;
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

}
