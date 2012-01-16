package org.cjc.mydives.divetracker.db;

/**
 * Constants for the Certification model class
 * @author JuanCarlos
 *
 */
public class CertificationConstants {
	public static final String DB_TABLE = "certification";
	public static final String FIELD_ROWID = "_id";
	public static final String FIELD_TYPE = "type";
	public static final String FIELD_DATE = "date";
	public static final String FIELD_NUMBER = "number";
	public static final String FIELD_ORGANIZATION = "organization";
	public static final String FIELD_INSTRUCTOR = "instructor";
	
	public static final String[] fields(){
		return new String[]{FIELD_ROWID, FIELD_TYPE, FIELD_DATE, FIELD_NUMBER, FIELD_ORGANIZATION, FIELD_INSTRUCTOR};
	}

}
