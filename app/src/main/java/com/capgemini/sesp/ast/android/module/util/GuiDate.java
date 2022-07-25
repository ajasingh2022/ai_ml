package com.capgemini.sesp.ast.android.module.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * This class is mainly intended to be used in tables, to be able to handle
 * customized toString and null dates.
 * 
 * @author avallda
 */
public final class GuiDate extends Date {
	private static final long serialVersionUID = -4294532030971873040L;

	public static final String DATE_FORMAT = "yyyy-MM-dd";
	public static final String TIME_FORMAT = "HH:mm:ss (z)";
	public static final String TIME_MINUTES_FORMAT = "HH:mm";
	public static final String DATE_TIME_FORMAT = DATE_FORMAT + " " + TIME_FORMAT;

	private static String dateTimeFormat = GuiDate.DATE_TIME_FORMAT;
	private static String dateFormat = GuiDate.DATE_FORMAT;
	private static String timeFormat = GuiDate.TIME_FORMAT;

	private static final GuiDate NULL_DATE_FUTURE = new GuiDate(System.currentTimeMillis() + 6311520000000l); // now
																												// +
																												// 200
																												// years
	private static final GuiDate NULL_DATE_PAST = new GuiDate(System.currentTimeMillis() - 6311520000000l); // now
																											// -
																											// 200
																											// years
	private TimeZone timeZone;

	private static SimpleDateFormat getFormatter(Boolean showTime, TimeZone timeZone) {
		SimpleDateFormat smd;
		if (showTime == null || showTime) {
			smd = new SimpleDateFormat(getDateTimeFormat());
		} else {
			smd = new SimpleDateFormat(getDateFormat());
		}
		if (timeZone != null) {
			smd.setTimeZone(timeZone);
		}
		return smd;
	}

	private static SimpleDateFormat getTimeFormatter(TimeZone timeZone) {
		SimpleDateFormat smd = new SimpleDateFormat(getTimeFormat());
		if (timeZone != null)
			smd.setTimeZone(timeZone);
		return smd;
	}

	public static String formatDate(Date date) {
		return getFormatter(null, null).format(date);
	}

	public static String formatDate(Date date, boolean showTime) {
		return getFormatter(showTime, null).format(date);
	}

	public static String formatDate(Date date, boolean showTime, TimeZone timeZone) {
		return getFormatter(showTime, timeZone).format(date);
	}

	public static String formatTime(Date date) {
		return getTimeFormatter(null).format(date);
	}

	public static String formatTime(Date date, TimeZone timeZone) {
		return getTimeFormatter(timeZone).format(date);
	}

	public static String formatDate(Date date, String format) {
		return new SimpleDateFormat(format).format(date);
	}

	public static String formatDate(Date date, String format, TimeZone timeZone) {
		SimpleDateFormat smd = new SimpleDateFormat(format);
		if (timeZone != null)
			smd.setTimeZone(timeZone);
		return smd.format(date);
	}

	public static String formatSameDateAndTime(Date fromDate, Date toDate) {
		return formatSameDateAndTime(fromDate, toDate, TIME_MINUTES_FORMAT, null);
	}

	public static String formatSameDateAndTime(Date fromDate, Date toDate, TimeZone timeZone) {
		return formatSameDateAndTime(fromDate, toDate, TIME_MINUTES_FORMAT, timeZone);
	}

	public static String formatSameDateAndTime(Date fromDate, Date toDate, String timeFormat, TimeZone timeZone) {
		String fromDateString = formatDate(fromDate, false, timeZone);
		String toDateString = formatDate(toDate, false, timeZone);

		if (fromDateString.equals(toDateString)) {
			return fromDateString + " " + formatDate(fromDate, timeFormat, timeZone) + " - " + formatDate(toDate, timeFormat, timeZone);
		} else {
			return formatDate(fromDate, true) + " - " + formatDate(toDate, true);
		}
	}

	public static GuiDate parseDate(String date) {
		try {
			return new GuiDate(getFormatter(null, null).parse(date));
		} catch (ParseException e) {
			return null;
		}
	}

	public static GuiDate toGuiDateUsersTimesZone(Date date, boolean showTime) {
		if (date instanceof GuiDate) {
			return (GuiDate) date;
		} else if (date != null) {
			return new GuiDate(date, showTime, SessionState.getInstance().getUsersTimeZone());
		} else {
			return GuiDate.getNullDate(true);
		}
	}
	
	public static GuiDate toGuiDate(Date date) {
		return toGuiDate(date, true, null);
	}

	public static GuiDate toGuiDate(Date date, boolean showTime, TimeZone timeZone) {
		if (date instanceof GuiDate) {
			return (GuiDate) date;
		} else if (date != null) {
			return new GuiDate(date, showTime, timeZone);
		} else {
			return GuiDate.getNullDate(true);
		}
	}

	/**
	 * Convenient method for getting dates to fill tables with.
	 * 
	 * @param date
	 *            The date object. If null, a special null instance is returned.
	 * @param showTime
	 *            If <code>date</code> is null
	 * @return Always returns a non-null <code>GuiDate</code> object.
	 */
	public static GuiDate toGuiDate(Date date, boolean showTime) {
		return toGuiDate(date, showTime, null);
	}

	// public static GuiDate toGuiDate(RspDateDay date) {
	// return toGuiDate((date == null ? null : date.getLocalDate()), false);
	// }

	/**
	 * @return A special static far-in-the-(future/past)-date with toString()
	 *         always returning "".
	 */
	public static GuiDate getNullDate(boolean futureDate) {
		if (futureDate) {
			return NULL_DATE_FUTURE;
		} else {
			return NULL_DATE_PAST;
		}
	}

	private final boolean showTime;

	public static GuiDate getNow() {
		return new GuiDate();
	}

	private GuiDate() {
		this(System.currentTimeMillis());
	}

	private GuiDate(Date date) {
		this(date.getTime());
	}

	private GuiDate(Date date, boolean showTime, TimeZone timeZone) {
		this(date.getTime(), showTime, timeZone);
	}

	private GuiDate(long timeInMillis) {
		this(timeInMillis, true, null);
	}

	private GuiDate(long timeInMillis, boolean showTime, TimeZone timeZone) {
		super(timeInMillis);
		this.showTime = showTime;
		this.timeZone = timeZone;
	}

	@Override
	public String toString() {
		if (this != NULL_DATE_FUTURE && this != NULL_DATE_PAST) {
			return GuiDate.formatDate(this, showTime, timeZone);
		} else {
			return "";
		}
	}

	public String getRelativeTime() {
		return getRelativeTime(new Date(), this);
	}

	private static String getRelativeTime(Date refDate, Date relDate) {
		long rel = (relDate.getTime() - refDate.getTime());
		String suffix = (rel < 0 ? "AGO" : "REMAINING");

		long absrel = Math.abs(rel);
		if (absrel < 1000) {
			return "NOW";
		}

		String output = millisAsString(absrel);

		if (!output.equals("A_VERY_LONG_TIME")) {
			return output + " " + suffix;
		} else {
			if (rel > 0) {
				return "WAY_FAR_IN_THE_FUTURE" + "...";
			} else {
				return "IN_THE_STONEAGE_OR_SOMETHING" + "...";
			}
		}
	}

	public static String millisAsString(long length) {
		long absLength = Math.abs(length);
		double[] levels = { 1000, 60, 60, 24, 7, 4.348214286, 12, 10 };
		String[] unitsSingular = { "MILLISECOND", "SECOND", "MINUTE", "HOUR", "DAY", "WEEK", "MONTH", "YEAR" };
		String[] unitsPlural = { "MILLISECONDS", "SECONDS", "MINUTES", "HOURS", "DAYS", "WEEKS", "MONTHS", "YEARS" };

		final int MAX_DEPTH = 2;
		int depth = 0;
		String output = "";
		double baseLevel = 1.0;
		double currentLevel = baseLevel;
		for (int i = 0; i < levels.length; i++) {
			if (absLength < currentLevel * levels[i]) {
				int x = (int) (absLength / currentLevel);
				output += x + " " + (x == 1 ? unitsSingular[i] : unitsPlural[i]) + " ";
				depth += 1;
				if (depth < MAX_DEPTH && i > 0) { // restart the loop once again
													// without the first unit
					// output += " and ";
					i = -1;
					absLength -= x * currentLevel;
					currentLevel = baseLevel;
					continue;
				} else {
					return output.trim();
				}
			}
			currentLevel *= levels[i];
		}
		return "A_VERY_LONG_TIME";
	}

	/**
	 * @return the dateTimeFormat
	 */
	public static String getDateTimeFormat() {
		return dateTimeFormat;
	}

	/**
	 * @param aDateTimeFormat
	 *            the dateTimeFormat to set
	 */
	public static void setDateTimeFormat(String aDateTimeFormat) {
		dateTimeFormat = aDateTimeFormat;
	}

	/**
	 * @return the dateFormat
	 */
	public static String getDateFormat() {
		return dateFormat;
	}

	/**
	 * @param aDateFormat
	 *            the dateFormat to set
	 */
	public static void setDateFormat(String aDateFormat) {
		dateFormat = aDateFormat;
	}

	/**
	 * @return the timeFormat
	 */
	public static String getTimeFormat() {
		return timeFormat;
	}

	/**
	 * @param aTimeFormat
	 *            the timeFormat to set
	 */
	public static void setTimeFormat(String aTimeFormat) {
		timeFormat = aTimeFormat;
	}
}
