package org.cjc.mydives.divetracker.db;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This class is used to package values to and un-package values from the database.
 * @author Carlos
 *
 */
public abstract class FormatterHelper {
	
	// Database formats
	public static final String DATE_FORMAT_DB = "yyyyMMdd";
	public static final String TIME_FORMAT_DB = "HHmm";
	public static final String DATETIME_FORMATDB = "yyyyMMddHHmm";

	// Screen formats
	public static final String DATE_FORMAT_SCR = "dd/MM/yyyy";
	public static final String TIME_FORMAT_SCR = "HH:mm";
	
	private static SimpleDateFormat df2db = new SimpleDateFormat(DATE_FORMAT_DB);
	private static SimpleDateFormat tf2db = new SimpleDateFormat(TIME_FORMAT_DB);
	private static SimpleDateFormat dtf2db = new SimpleDateFormat(DATETIME_FORMATDB);

	private static SimpleDateFormat df2scr = new SimpleDateFormat(DATE_FORMAT_SCR);	
	private static SimpleDateFormat tf2scr = new SimpleDateFormat(TIME_FORMAT_SCR);
	
	/* Hide the default constructor. */
	private FormatterHelper() {}

	/**
	 * Format a date string from the database into a string to be presented in the screen.
	 * @param date the string with the date in the DB format.
	 * @return a String with the date in the format to be presented in the screen.
	 */
	public static String db2ScrDateFormat(String date) {
		return df2scr.format(unPackDate(date));
	}

	/**
	 * Format a time string from the database into a string to be presented in the screen.
	 * @param time the string with the time in the DB format.
	 * @return a String with the date in the format to be presented in the screen.
	 */
	public static String db2ScrTimeFormat(String time) {
		return tf2scr.format(unPackTime(time));
	}

	/**
	 * Parse the value of a date from the screen.
	 * @param dateStr the date to be parsed from the date string object
	 * @return a Date with the date in the format {@link DATE_FORMAT_SCR}
	 */
	public static Date parseScrDate(String dateStr) {
		try {
			return df2scr.parse(dateStr);
		} catch (ParseException e) {
			return null;
		}
	}
	
	/**
	 * Parse the value of a time from the screen.
	 * @param timeStr the date to be parsed from the time string object
	 * @return a Date with the time in the format {@link TIME_FORMAT_SCR}
	 */
	public static Date parseScrTime(String timeStr) {
		try {
			return tf2scr.parse(timeStr);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * Converts the value of a date for the screen.
	 * @param date the date to be formatted for the screen
	 * @return a string with the date in the format {@link DATE_FORMAT_SCR}
	 */
	public static String packDate4Scr(Date date) {
			return df2scr.format(date);
	}
	
	/**
	 * Converts the value of a time for the screen.
	 * @param time the time to be formatted for the screen
	 * @return a string with the time in the format {@link TIME_FORMAT_SCR}
	 */
	public static String packTime4Scr(Date time) {
			return tf2scr.format(time);
	}

	/**
	 * Packs the value of a date into a String.
	 * @param date the date to be packed
	 * @return a string with the date in the format {@link DATE_FORMAT}
	 */
	public static String packDate(Date date) {
		if (date == null) {
			return null;
		}
		return df2db.format(date);
	}
	
	/**
	 * Returns a Date object for the passes date string.
	 * @param dateStr a string with a date in the format {@link DATE_FORMAT}
	 * @return The date object if it can be parsed or null.
	 */
	public static Date unPackDate(String dateStr) {
		try {
			return df2db.parse(dateStr);
		} catch (ParseException e) {
			return null;
		}
	}
	
	/**
	 * Packs the value of a time into a String.
	 * @param time the time to be packed from the date object
	 * @return a string with the time in the format {@link TIME_FORMAT}
	 */
	public static String packTime(Date time) {
		if (time == null) {
			return null;
		}
		return tf2db.format(time);
	}
	
	/**
	 * Returns a Date object for the passes time string.
	 * @param timeStr a string with a time in the format {@link TIME_FORMAT}
	 * @return The date object with the time if it can be parsed or null.
	 */
	public static Date unPackTime(String timeStr) {
		try {
			return tf2db.parse(timeStr);
		} catch (ParseException e) {
			return null;
		}
	}
	
	/**
	 * Packs the value of a dateTme into a String.
	 * @param dateTime the DateTime to be packed
	 * @return a string with the date in the format {@link DATETIME_FORMAT}
	 */
	public static String packDateTime(Date dateTime) {
		return dtf2db.format(dateTime);
	}
	
	/**
	 * Returns a Date object with the datetime for the passes datetime string.
	 * @param dateTimeStr a string with a dateTime in the format {@link DATETIME_FORMAT}
	 * @return The date object if it can be parsed or null.
	 */
	public static Date unPackDateTime(String dateTimeStr) {
		try {
			return df2db.parse(dateTimeStr);
		} catch (ParseException e) {
			return null;
		}
	}
}
