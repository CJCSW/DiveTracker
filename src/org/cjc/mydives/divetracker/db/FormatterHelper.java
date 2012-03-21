package org.cjc.mydives.divetracker.db;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * This class is used to package values to and un-package values from the database.
 * @author Carlos
 *
 */
public final class FormatterHelper {

    // Database formats
    /*
    public static final String DATE_FORMAT_DB = "yyyyMMdd";
    public static final String TIME_FORMAT_DB = "HH:mm";
    public static final String DATETIME_FORMATDB = "yyyyMMddHHmm";
     */

    private static DateFormat df2scr = SimpleDateFormat.getDateInstance(SimpleDateFormat.MEDIUM); 	// Haciendo caso al locale
    private static DateFormat tf2scr = SimpleDateFormat.getTimeInstance(SimpleDateFormat.SHORT);    // Haciendo caso al locale

    /* Hide the default constructor. */
    private FormatterHelper() {}

    /**
     * Format a date string from the database into a string to be presented in the screen.
     * @param dateMillis the string with the date in the DB format.
     * @return a String with the date in the format to be presented in the screen.
     */
    public static String formatDate(long dateMillis) {
        return df2scr.format(new Date(dateMillis));
    }

    /**
     * Format a time string from the database into a string to be presented in the screen.
     * @param timeMillis the long with the time in milliseconds.
     * @return a String with the date in the format to be presented in the screen.
     */
    public static String formatTime(long timeMillis) {
        return tf2scr.format(new Date(timeMillis));
    }

    /**
     * Parse the value of a date from the screen.
     * @param dateStr the date to be parsed from the date string object
     * @return a Date with the date in the format {@link DATE_FORMAT_SCR}
     */
    public static long parseDate(String dateStr) {
    	long result;
        try {
            result = df2scr.parse(dateStr).getTime();
        } catch (ParseException e) {
            result = 0;
        }
        return result;
    }

    /**
     * Parse the value of a time from the screen.
     * @param timeStr the date to be parsed from the time string object
     * @return a Date with the time in the format {@link TIME_FORMAT_SCR}
     */
    public static long parseTime(String timeStr) {
    	long result;
        try {
            result = tf2scr.parse(timeStr).getTime();
        } catch (ParseException e) {
            result = 0;
        }
        return result;
    }
}
