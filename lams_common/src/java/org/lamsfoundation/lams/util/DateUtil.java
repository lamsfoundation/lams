/***************************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ***********************************************************************/

package org.lamsfoundation.lams.util;

import java.sql.Timestamp;
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

    /**
     * Convert your local time to Universal Time Coordinator.
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

        return new Date(gmtLocal.getTime());
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
}
