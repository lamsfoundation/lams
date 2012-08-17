/* 
 * Copyright 2004-2005 OpenSymphony 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not 
 * use this file except in compliance with the License. You may obtain a copy 
 * of the License at 
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0 
 *   
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT 
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the 
 * License for the specific language governing permissions and limitations 
 * under the License.
 * 
 */

/*
 * Previously Copyright (c) 2001-2004 James House
 * and Juergen Donnerstag (c) 2002, EDS 2002
 */

package org.quartz.impl.calendar;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import org.quartz.Calendar;

/**
 * <p>
 * This implementation of the Calendar excludes a set of days of the year. You
 * may use it to exclude bank holidays which are on the same date every year.
 * </p>
 * 
 * @see org.quartz.Calendar
 * @see org.quartz.impl.calendar.BaseCalendar
 * 
 * @author Juergen Donnerstag
 */
public class AnnualCalendar extends BaseCalendar implements Calendar,
        Serializable {

    private ArrayList excludeDays = new ArrayList();

    // true, if excludeDays is sorted
    private boolean dataSorted = false;

    /**
     * <p>
     * Constructor
     * </p>
     */
    public AnnualCalendar() {
        super();
    }

    /**
     * <p>
     * Constructor
     * </p>
     */
    public AnnualCalendar(Calendar baseCalendar) {
        super(baseCalendar);
    }

    /**
     * <p>
     * Get the array which defines the exclude-value of each day of month
     * </p>
     */
    public ArrayList getDaysExcluded() {
        return excludeDays;
    }

    /**
     * <p>
     * Return true, if day is defined to be exluded.
     * </p>
     */
    public boolean isDayExcluded(java.util.Calendar day) {
        if (day == null)
                throw new IllegalArgumentException(
                        "Parameter day must not be null");

        int dmonth = day.get(java.util.Calendar.MONTH);
        int dday = day.get(java.util.Calendar.DAY_OF_MONTH);

        if (dataSorted == false) {
            Collections.sort(excludeDays, new CalendarComparator());
            dataSorted = true;
        }

        Iterator iter = excludeDays.iterator();
        while (iter.hasNext()) {
            java.util.Calendar cl = (java.util.Calendar) iter.next();

            // remember, the list is sorted
            if (dmonth < cl.get(java.util.Calendar.MONTH)) return false;

            if (dday != cl.get(java.util.Calendar.DAY_OF_MONTH)) continue;

            if (dmonth != cl.get(java.util.Calendar.MONTH)) continue;

            return true;
        }

        return false;
    }

    /**
     * <p>
     * Redefine the array of days excluded. The array must of size greater or
     * equal 31.
     * </p>
     */
    public void setDaysExcluded(ArrayList days) {
        if (days == null) excludeDays = new ArrayList();

        excludeDays = days;
        dataSorted = false;
    }

    /**
     * <p>
     * Redefine a certain day to be excluded (true) or included (false).
     * </p>
     */
    public void setDayExcluded(java.util.Calendar day, boolean exclude) {
        if (isDayExcluded(day)) return;

        excludeDays.add(day);
        dataSorted = false;
    }

    /**
     * <p>
     * Determine whether the given time (in milliseconds) is 'included' by the
     * Calendar.
     * </p>
     * 
     * <p>
     * Note that this Calendar is only has full-day precision.
     * </p>
     */
    public boolean isTimeIncluded(long timeStamp) {
        // Test the base calendar first. Only if the base calendar not already
        // excludes the time/date, continue evaluating this calendar instance.
        if (super.isTimeIncluded(timeStamp) == false) { return false; }

        java.util.Calendar day = getJavaCalendar(timeStamp);

        return !(isDayExcluded(day));
    }

    /**
     * <p>
     * Determine the next time (in milliseconds) that is 'included' by the
     * Calendar after the given time. Return the original value if timeStamp is
     * included. Return 0 if all days are excluded.
     * </p>
     * 
     * <p>
     * Note that this Calendar is only has full-day precision.
     * </p>
     */
    public long getNextIncludedTime(long timeStamp) {
        // Call base calendar implementation first
        long baseTime = super.getNextIncludedTime(timeStamp);
        if ((baseTime > 0) && (baseTime > timeStamp)) timeStamp = baseTime;

        // Get timestamp for 00:00:00
        long newTimeStamp = buildHoliday(timeStamp);

        java.util.Calendar day = getJavaCalendar(newTimeStamp);
        if (isDayExcluded(day) == false) return timeStamp; // return the
                                                           // original value

        while (isDayExcluded(day) == true) {
            day.add(java.util.Calendar.DATE, 1);
        }

        return day.getTime().getTime();
    }
}

class CalendarComparator implements Comparator
{
    
    public CalendarComparator() {
        
    }

    /** 
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    public int compare(Object arg0, Object arg1) {
        java.util.Calendar c1 = (java.util.Calendar) arg0;
        java.util.Calendar c2 = (java.util.Calendar) arg1;
        
        if(c1.before(c2))
            return -1;
        else if(c1.after(c2))
            return 1;
        else
            return 0;
    }
}
