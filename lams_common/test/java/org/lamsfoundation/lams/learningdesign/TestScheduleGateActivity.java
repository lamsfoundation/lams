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

package org.lamsfoundation.lams.learningdesign;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.util.DateUtil;

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

        now.setTime(DateUtil.convertToUTC(now.getTime()));
        gateByDateTime.setGateStartDateTime(now.getTime());
        
        now.add(Calendar.MINUTE,END_TIME_OFFSET.intValue());
        gateByDateTime.setGateEndDateTime(now.getTime());
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

    public void testGetRealGateOpenTime()
    {
        assertNotNull("verify real open time by offset",gateByTimeOffset.getRealGateOpenTime());
        log.info("gate open time offset -- "+gateByTimeOffset.getGateStartTimeOffset());
        log.info("Real gate open time by offset --"+gateByTimeOffset.getRealGateOpenTime());
        
        assertNotNull("verify real open time by date time",gateByDateTime.getRealGateOpenTime());
        log.info("gate open date time(UTC) -- "+gateByDateTime.getGateStartDateTime());
        log.info("Real gate open time by date time -- "+gateByDateTime.getRealGateOpenTime());
    }

    public void testGetRealGateCloseTime()
    {
        assertNotNull("verify real open time",gateByTimeOffset.getRealGateCloseTime());
        log.info("gate close time offset -- "+gateByTimeOffset.getGateEndTimeOffset());
        log.info("gate close time by offset --"+gateByTimeOffset.getRealGateCloseTime());
        
        assertNotNull("verify real close time by date time",gateByDateTime.getRealGateCloseTime());
        log.info("gate close date time(UTC) -- "+gateByDateTime.getGateEndDateTime());
        log.info("Real gate close time by date time -- "+gateByDateTime.getRealGateCloseTime());
    }

    public void testGetRealCloseTimeWithLongOffset()
    {
        Calendar cal = new GregorianCalendar();
        cal.setTime(gateByDateTime.getGateEndDateTime());
        cal.add(Calendar.MINUTE,END_TIME_OFFSET.intValue()*24);
        gateByDateTime.setGateEndDateTime(cal.getTime());
        
        assertNotNull("verify real close time by date time",gateByDateTime.getRealGateCloseTime());
        
        log.info("gate close date time(UTC) -- "+gateByDateTime.getGateEndDateTime());
        log.info("Real gate close time by date time -- "+gateByDateTime.getRealGateCloseTime());
       
    }
}
