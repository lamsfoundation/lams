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
/* $$Id$$ */
package org.lamsfoundation.lams.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;


/**
 * Date utility class that helps the conversion between time
 * 
 * @author Jacky Fang
 * @since  2005-4-14
 * @version 1.1
 * 
 */
public class DateUtil
{

	public static final String WDDX_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";
	public static final String LAMS_FLASH_FORMAT = "dd/M/yyyy h:mm a";
	private static final DateFormat JSON_DATE_OUTPUT_FORMATTER = new SimpleDateFormat("d MMMM yyyy h:mm:ss a");

	/**
     * Convert your local time to Universal Time Coordinator.
     * TODO conversion is not working properly. The returned Date object still 
     * contain server local timezone rather than GMT time zone.
     * @param time your local time
     * @return the date UTC time which is the same as GMT.
     */
    public static Date convertToUTC(Date time)
    {
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
     * Convert from UTC time to your local time. <b>Note: </b>it is your 
     * responsibility to pass in the correct UTC date.
     * 
     * @param localTimeZone your local time zone.
     * @param UTCDate the utc date.
     * @return your local date time.
     */
    public static Date convertFromUTCToLocal(TimeZone localTimeZone,Date UTCDate)
    {        
        Calendar canlendar = new GregorianCalendar(localTimeZone); 
        long offset  = canlendar.get(Calendar.ZONE_OFFSET)+canlendar.get(Calendar.DST_OFFSET);

        return new Date(UTCDate.getTime()+offset);
    }
    
    /**
     * Convert from local time to your client (time zone) time.
     * 
     * @param targetTimeZone time zone converting to.
     * @param date date to convert.
     * @return your time zone date time.
     */
    public static Date convertToTimeZoneFromDefault(TimeZone targetTimeZone, Date date) {
    	TimeZone defaultTz = TimeZone.getDefault();
    	Integer rawOffset = new Integer(defaultTz.getOffset(date.getTime()) - targetTimeZone.getOffset(date.getTime()));
    	
    	return new Date(date.getTime() - rawOffset);
    }
    
    /**
     * Convert from client (time zone) time to your local time.
     * 
     * @param targetTimeZone time zone converting from.
     * @param date date to convert.
     * @return your local date time.
     */
    public static Date convertFromTimeZoneToDefault(TimeZone targetTimeZone, Date date) {
    	TimeZone defaultTz = TimeZone.getDefault();
    	Integer rawOffset = new Integer(defaultTz.getOffset(date.getTime()) - targetTimeZone.getOffset(date.getTime()));
    	
    	return new Date(date.getTime() + rawOffset);
    }
    
    /**
     * Convert from String formatted date to a Date. Tries the following
     * date formats:
     *  (WDDX) YYYY-MM-ddTHH:mm:ss
     * 
     * @param dateString the date as a string
     * @return converted Date
     * @throws ParseException
     */
    public static Date convertFromString(String dateString) throws ParseException
    {  
    	if ( dateString == null )
    		return null;
    	
    	// Replace this implementation with commons.lang.time.DateUtils.parseDate()
    	// if/when we upgrade to commons 2.1
        SimpleDateFormat parser = null;
        String[] parseFormats = new String[] {WDDX_FORMAT};
        for (int i = 0; i < parseFormats.length; i++) {
            if (i == 0) {
                parser = new SimpleDateFormat(parseFormats[0]);
            } else {
                parser.applyPattern(parseFormats[i]);
            }
            try {
            	Date date = parser.parse(dateString);
            	if ( date != null ) {
            		return date;
            	}
            } catch ( ParseException p )  {
				// okay no good, try the next one...
            }
        }
        throw new ParseException("Unable to parse date "+dateString,0);
    }

    /**
     * Convert from String formatted date to a Date given the supplied
     * SimpleDateFormat pattern
     * 
     * @param dateString the date as a string
     * @param dateFormat SimpleDateFormat pattern
     * @return converted Date
     * @throws ParseException
     */
    public static Date convertFromString(String dateString, String dateFormat) throws ParseException
    {
    	if ( dateString == null )
    		return null;
    	
        SimpleDateFormat parser = new SimpleDateFormat(dateFormat);
        return parser.parse(dateString);
    }
    
    /**
     * Convert from String formatted date to a Date. Tries the following
     * date format: DD/MM/YYYY hh:mm a
     * 
     * This is the format used by Flash to send the data/time for 
     * scheduling a lesson. It has a matching custom date formatter in Flash
     * 
     * @param dateString the date as a string
     * @return converted Date
     * @throws ParseException
     */
    public static Date convertFromLAMSFlashFormat(String dateString) throws ParseException
    {  
    	return convertFromString(dateString, LAMS_FLASH_FORMAT);
    }

    
    /**
     *  Convert a date to a String for sending to the client via JSON. 
     */
    public static String convertToStringForJSON(Date date) {
	return JSON_DATE_OUTPUT_FORMATTER.format(date);
    }

}
