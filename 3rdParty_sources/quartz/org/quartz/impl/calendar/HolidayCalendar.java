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
 */
package org.quartz.impl.calendar;

import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.SortedSet;
import java.util.TreeSet;

import org.quartz.Calendar;

/**
 * <p>
 * This implementation of the Calendar stores a list of holidays (full days
 * that are excluded from scheduling).
 * </p>
 * 
 * <p>
 * The implementation DOES take the year into consideration, so if you want to
 * exclude July 4th for the next 10 years, you need to add 10 entries to the
 * exclude list.
 * </p>
 * 
 * @author Sharada Jambula
 * @author Juergen Donnerstag
 */
public class HolidayCalendar extends BaseCalendar implements Calendar,
        Serializable {

    // A sorted set to store the holidays
    private TreeSet dates = new TreeSet();

    /**
     * Constructor
     */
    public HolidayCalendar() {
    }

    /**
     * Constructor
     */
    public HolidayCalendar(Calendar baseCalendar) {
        setBaseCalendar(baseCalendar);
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
        if (super.isTimeIncluded(timeStamp) == false) return false;

        Date lookFor = buildHoliday(new Date(timeStamp));

        return !(dates.contains(lookFor));
    }

    /**
     * <p>
     * Determine the next time (in milliseconds) that is 'included' by the
     * Calendar after the given time.
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
        while (isTimeIncluded(day.getTime().getTime()) == false) {
            day.add(java.util.Calendar.DATE, 1);
        }

        return day.getTime().getTime();
    }

    /**
     * <p>
     * Add the given Date to the list of excluded days. Only the month, day and
     * year of the returned dates are significant.
     * </p>
     */
    public void addExcludedDate(Date excludedDate) {
        Date date = buildHoliday(excludedDate);
        /*
         * System.err.println( "HolidayCalendar.add(): date=" +
         * excludedDate.toLocaleString());
         */
        this.dates.add(date);
    }

    public void removeExcludedDate(Date dateToRemove) {
        Date date = buildHoliday(dateToRemove);
        dates.remove(date);
    }

    /**
     * <p>
     * Returns a <code>SortedSet</code> of Dates representing the excluded
     * days. Only the month, day and year of the returned dates are
     * significant.
     * </p>
     */
    public SortedSet getExcludedDates() {
        return Collections.unmodifiableSortedSet(dates);
    }
}
