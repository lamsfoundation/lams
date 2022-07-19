/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2.0
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

package org.lamsfoundation.lams.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.time.FastDateFormat;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;

/**
 * Date utility class that helps the conversion between time
 *
 * @author Jacky Fang
 * @since 2005-4-14
 * @version 1.1
 *
 */
public class DateUtil {

    public static final String EXPORT_LD_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";
    public static final String SCHEDULE_LESSON_FORMAT = "dd/M/yyyy h:mm a";
    public static final String ISO8601_FORMAT = "yyyy-MM-dd'T'HH:mmZ";
    public static final String PRETTY_FORMAT = "d MMMM yyyy h:mm:ss a";

    private static final DateFormat shortDateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT,
	    DateFormat.SHORT);

    private static final FastDateFormat dateFormatterTimeAgo = FastDateFormat.getInstance(DateUtil.ISO8601_FORMAT,
	    TimeZone.getTimeZone("GMT"), null);

    /**
     * Convert your local time to Universal Time Coordinator. TODO conversion is not working properly. The returned Date
     * object still contain server local timezone rather than GMT time zone.
     *
     * @param time
     *            your local time
     * @return the date UTC time which is the same as GMT.
     */
    public static Date convertToUTC(Date time) {
	TimeZone gmt = TimeZone.getTimeZone("Etc/GMT");
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
	sdf.setTimeZone(gmt);
	String str = sdf.format(time);
	Timestamp gmtLocal = Timestamp.valueOf(str);

	Calendar gmtCanlendar = new GregorianCalendar(gmt);
	gmtCanlendar.setTimeInMillis(gmtLocal.getTime());

	return gmtCanlendar.getTime();
    }

    /**
     * Convert from UTC time to your local time. <b>Note: </b>it is your responsibility to pass in the correct UTC date.
     *
     * @param localTimeZone
     *            your local time zone.
     * @param UTCDate
     *            the utc date.
     * @return your local date time.
     */
    public static Date convertFromUTCToLocal(TimeZone localTimeZone, Date UTCDate) {
	Calendar canlendar = new GregorianCalendar(localTimeZone);
	long offset = canlendar.get(Calendar.ZONE_OFFSET) + canlendar.get(Calendar.DST_OFFSET);

	return new Date(UTCDate.getTime() + offset);
    }

    /**
     * Convert from local time to your client (time zone) time.
     *
     * @param targetTimeZone
     *            time zone converting to.
     * @param date
     *            date to convert.
     * @return your time zone date time.
     */
    public static Date convertToTimeZoneFromDefault(TimeZone targetTimeZone, Date date) {
	TimeZone defaultTz = TimeZone.getDefault();
	int rawOffset = defaultTz.getOffset(date.getTime()) - targetTimeZone.getOffset(date.getTime());

	return new Date(date.getTime() - rawOffset);
    }

    /**
     * Convert from client (time zone) time to your local time.
     *
     * @param targetTimeZone
     *            time zone converting from.
     * @param date
     *            date to convert.
     * @return your local date time.
     */
    public static Date convertFromTimeZoneToDefault(TimeZone targetTimeZone, Date date) {
	TimeZone defaultTz = TimeZone.getDefault();
	int rawOffset = defaultTz.getOffset(date.getTime()) - targetTimeZone.getOffset(date.getTime());

	return new Date(date.getTime() + rawOffset);
    }

    /**
     * Convert from String formatted date to a Date. Tries the following date formats: (WDDX) YYYY-MM-ddTHH:mm:ss
     *
     * @param dateString
     *            the date as a string
     * @return converted Date
     * @throws ParseException
     */
    public static Date convertFromString(String dateString) throws ParseException {
	if (dateString == null) {
	    return null;
	}

	// Replace this implementation with commons.lang.time.DateUtils.parseDate()
	// if/when we upgrade to commons 2.1
	SimpleDateFormat parser = null;
	String[] parseFormats = new String[] { EXPORT_LD_FORMAT };
	for (int i = 0; i < parseFormats.length; i++) {
	    if (i == 0) {
		parser = new SimpleDateFormat(parseFormats[0]);
	    } else {
		parser.applyPattern(parseFormats[i]);
	    }
	    try {
		Date date = parser.parse(dateString);
		if (date != null) {
		    return date;
		}
	    } catch (ParseException p) {
		// okay no good, try the next one...
	    }
	}
	throw new ParseException("Unable to parse date " + dateString, 0);
    }

    /**
     * Convert from String formatted date to a Date given the supplied SimpleDateFormat pattern
     *
     * @param dateString
     *            the date as a string
     * @param dateFormat
     *            SimpleDateFormat pattern
     * @return converted Date
     * @throws ParseException
     */
    public static Date convertFromString(String dateString, String dateFormat) throws ParseException {
	if (dateString == null) {
	    return null;
	}

	SimpleDateFormat parser = new SimpleDateFormat(dateFormat);
	return parser.parse(dateString);
    }

    public static final int TYPE_BOTH = 1;
    public static final int TYPE_DATE = 2;
    public static final int TYPE_TIME = 3;

    /**
     * Equivalent of <LAMS:Date value="value"/>. Use for processing a date to send to the client via JSON. Locale comes
     * from request.getLocale(); Same as calling convertToStringForJSON(value, DateFormat.MEDIUM, TYPE_BOTH, locale)
     *
     * @param value
     * @param locale
     * @return
     */
    public static String convertToStringForJSON(Date value, Locale locale) {
	return DateUtil.convertToStringForJSON(value, DateFormat.MEDIUM, TYPE_BOTH, locale);
    }

    /**
     * Equivalent of <LAMS:Date value="value" style="short|full|medium" type="date|time|both"/>. Use for processing a
     * date to send to the client via JSON. Locale comes from request.getLocale();
     *
     * @param value
     * @param style
     *            DateFormat.MEDIUM, DateFormat.SHORT, DateFormat.FULL
     * @param type
     *            TYPE_BOTH (both data and time), TYPE_DATE or TYPE_TIME
     * @param locale
     * @return
     */
    public static String convertToStringForJSON(Date value, Integer style, Locale locale) {
	return DateUtil.convertToStringForJSON(value, style, TYPE_BOTH, locale);
    }

    /**
     * Equivalent of <LAMS:Date value="value" type="date|time|both"/>. Use for processing a date to send to the client
     * via JSON. Locale comes from request.getLocale();
     *
     * @param value
     * @param type
     *            TYPE_BOTH (both data and time), TYPE_DATE or TYPE_TIME
     * @param locale
     * @return
     */
    public static String convertToStringForJSON(Date value, Integer style, Integer type, Locale locale) {

	HttpSession ss = SessionManager.getSession();
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	TimeZone tz = user.getTimeZone();

	int dateStyle, timeStyle;
	switch (style) {
	    case DateFormat.SHORT:
		dateStyle = DateFormat.SHORT;
		timeStyle = DateFormat.SHORT;
		break;
	    case DateFormat.FULL:
		dateStyle = DateFormat.LONG;
		timeStyle = DateFormat.FULL;
		break;
	    default:
		dateStyle = DateFormat.LONG;
		timeStyle = DateFormat.MEDIUM;
	}

	DateFormat df = null;
	switch (type) {
	    case TYPE_DATE:
		df = DateFormat.getDateInstance(dateStyle, locale);
		break;
	    case TYPE_TIME:
		df = DateFormat.getTimeInstance(timeStyle, locale);
		break;
	    default:
		df = DateFormat.getDateTimeInstance(dateStyle, timeStyle, locale);
	}

	if (tz != null) {
	    df.setTimeZone(tz);
	}

	return df.format(value);
    }

    /**
     * Convert a date to the ISO08601 format needed for Timeago. Used to return dates through JSON.
     */
    public static String convertToStringForTimeagoJSON(Date value) {
	return dateFormatterTimeAgo.format(value);

    }

    /**
     * A shared function to convert milliseconds into a readable string
     *
     * @param timeInMillis
     * @return
     */
    public static String convertTimeToString(Long timeInMillis) {
	StringBuilder sb = new StringBuilder();
	if (timeInMillis != null && timeInMillis >= 1000) {
	    long totalTimeInSeconds = timeInMillis / 1000;

	    long seconds = (totalTimeInSeconds >= 60 ? totalTimeInSeconds % 60 : totalTimeInSeconds);
	    long minutes = (totalTimeInSeconds = (totalTimeInSeconds / 60)) >= 60 ? totalTimeInSeconds % 60
		    : totalTimeInSeconds;
	    long hours = (totalTimeInSeconds = (totalTimeInSeconds / 60)) >= 24 ? totalTimeInSeconds % 24
		    : totalTimeInSeconds;
	    long days = (totalTimeInSeconds = (totalTimeInSeconds / 24));

	    if (days != 0) {
		sb.append("" + days + "d, ");
	    }
	    if (hours != 0) {
		sb.append("" + hours + "h, ");
	    }
	    if (minutes != 0) {
		sb.append("" + minutes + "m, ");
	    }
	    if (seconds != 0) {
		sb.append("" + seconds + "s");
	    }
	}

	if (sb.length() > 0) {
	    return sb.toString();
	} else {
	    return null;
	}
    }

    /**
     * A shared function to convert date into a readable string
     *
     * @param date
     *            to format
     * @return formatted date
     */
    public static String convertToString(Date date, DateFormat format) {
	if (date != null) {
	    DateFormat usedFormat = format == null ? shortDateFormat : format;
	    return usedFormat.format(date);
	}
	return null;
    }
}
