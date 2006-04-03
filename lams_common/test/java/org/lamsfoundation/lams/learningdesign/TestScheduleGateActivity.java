/***************************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 * =============================================================
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
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
/* $$Id$$ */
package org.lamsfoundation.lams.learningdesign;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.apache.log4j.Logger;

import junit.framework.TestCase;


/**
 * 
 * @author Jacky Fang
 * @since  2005-4-14
 * @version
 * 
 */
public class TestScheduleGateActivity extends TestCase
{
    //---------------------------------------------------------------------
    // Instance variables
    //---------------------------------------------------------------------
	private static Logger log = Logger.getLogger(TestScheduleGateActivity.class);
	
    private ScheduleGateActivity gateByTimeOffset;
    private ScheduleGateActivity gateByDateTime;
    private static final Long START_TIME_OFFSET = new Long(30);
    private static final Long END_TIME_OFFSET = new Long(60);
    private Date lessonStartTime;
    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception
    {
        super.setUp();
        gateByTimeOffset = new ScheduleGateActivity();
        gateByTimeOffset.setGateStartTimeOffset(START_TIME_OFFSET);
        gateByTimeOffset.setGateEndTimeOffset(END_TIME_OFFSET);
        
        //String tzid[] = TimeZone.getAvailableIDs();
        gateByDateTime = new ScheduleGateActivity();
        
        //convert current system time to utc
        Calendar now = new GregorianCalendar();

        now.setTime(now.getTime());
        gateByDateTime.setGateStartDateTime(now.getTime());
        
        now.add(Calendar.MINUTE,END_TIME_OFFSET.intValue());
        gateByDateTime.setGateEndDateTime(now.getTime());
        
        //setup a lesson start time.
        Calendar lessonStart = new GregorianCalendar(2005,
                                                     Calendar.APRIL,
                                                     20,
                                                     9,
                                                     50);
        lessonStartTime = lessonStart.getTime();
        log.info("new lessonStartTime--"+lessonStartTime);
    }

    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception
    {
        super.tearDown();
    }

    /**
     * Constructor for TestScheduleGateActivity.
     * @param arg0
     */
    public TestScheduleGateActivity(String arg0)
    {
        super(arg0);
    }

    public void testGetLessonGateOpenTime()
    {
        assertNotNull("verify real open time by offset",gateByTimeOffset.getLessonGateOpenTime(lessonStartTime));
        log.info("gate open time offset -- "+gateByTimeOffset.getGateStartTimeOffset());
        log.info("Real gate open time by offset --"+gateByTimeOffset.getLessonGateOpenTime(lessonStartTime));
        log.info("gate open time by offset, calculated open time --"+gateByTimeOffset.getGateStartDateTime());
        
        assertNotNull("verify real open time by date time",gateByDateTime.getLessonGateOpenTime(lessonStartTime));
        //log.info("gate open date time(UTC) -- "+gateByDateTime.getGateStartDateTime());
        //log.info("Just for testing:"+this.formatUTCTime(gateByDateTime.getGateStartDateTime()));
        log.info("Real gate open time by date time -- "+gateByDateTime.getLessonGateOpenTime(lessonStartTime));
    }

    public void testGetLessonGateCloseTime()
    {
        assertNotNull("verify real open time",gateByTimeOffset.getLessonGateCloseTime(lessonStartTime));
        log.info("gate close time offset -- "+gateByTimeOffset.getGateEndTimeOffset());
        log.info("gate close time by offset --"+gateByTimeOffset.getLessonGateCloseTime(lessonStartTime));
        log.info("gate close time by offset, calculated close time --"+gateByTimeOffset.getGateEndDateTime());

        
        assertNotNull("verify real close time by date time",gateByDateTime.getLessonGateCloseTime(lessonStartTime));
        //log.info("gate close date time(UTC) -- "+gateByDateTime.getGateEndDateTime());
        log.info("Real gate close time by date time -- "+gateByDateTime.getLessonGateCloseTime(lessonStartTime));
    }

    public void testGetLessonCloseTimeWithLongOffset()
    {
        Calendar cal = new GregorianCalendar();
        cal.setTime(gateByDateTime.getGateEndDateTime());
        cal.add(Calendar.MINUTE,END_TIME_OFFSET.intValue()*24);
        gateByDateTime.setGateEndDateTime(cal.getTime());
        
        assertNotNull("verify real close time by date time",gateByDateTime.getLessonGateCloseTime(lessonStartTime));
        
        log.info("gate close date time(UTC) -- "+gateByDateTime.getGateEndDateTime());
        log.info("Real gate close time by date time -- "+gateByDateTime.getLessonGateCloseTime(lessonStartTime));
       
    }
    
    private String formatUTCTime(Date time)
    {
        TimeZone gmt = TimeZone.getTimeZone("Etc/GMT");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss z");
        sdf.setTimeZone(gmt);
        String str = sdf.format(time);
        return str;
    }
}
