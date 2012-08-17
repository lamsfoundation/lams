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
import java.util.Date;

import org.quartz.Calendar;

/**
 * <p>
 * This implementation of the Calendar excludes a set of days of the month. You
 * may use it to exclude every 1. of each month for example. But you may define
 * any day of a month.
 * </p>
 * 
 * @see org.quartz.Calendar
 * @see org.quartz.impl.calendar.BaseCalendar
 * 
 * @author Juergen Donnerstag
 */
public class MonthlyCalendar extends BaseCalendar implements Calendar,
        Serializable {

    // An array to store a months days which are to be excluded.
    // java.util.Calendar.get( ) as index.
    private boolean[] excludeDays = new boolean[31];

    // Will be set to true, if all week days are excluded
    private boolean excludeAll = false;

    /**
     * <p>
     * Constructor
     * </p>
     */
    public MonthlyCalendar() {
        super();
        init();
    }

    /**
     * <p>
     * Constructor
     * </p>
     */
    public MonthlyCalendar(Calendar baseCalendar) {
        super(baseCalendar);
        init();
    }

    /**
     * <p>
     * Initialize internal variables
     * </p>
     */
    private void init() {
        // all days are included by default
        excludeAll = areAllDaysExcluded();
    }

    /**
     * <p>
     * Get the array which defines the exclude-value of each day of month
     * </p>
     */
    public boolean[] getDaysExcluded() {
        return excludeDays;
    }

    /**
     * <p>
     * Return true, if mday is defined to be exluded.
     * </p>
     */
    public boolean isDayExcluded(int day) {
        return excludeDays[day - 1];
    }

    /**
     * <p>
     * Redefine the array of days excluded. The array must of size greater or
     * equal 31.
     * </p>
     */
    public void setDaysExcluded(boolean[] days) {
        if (days == null) return;

        excludeDays = days;
        excludeAll = areAllDaysExcluded();
    }

    /**
     * <p>
     * Redefine a certain day of the month to be excluded (true) or included
     * (false).
     * </p>
     */
    public void setDayExcluded(int day, boolean exclude) {
        excludeDays[day] = exclude;
        excludeAll = areAllDaysExcluded();
    }

    /**
     * <p>
     * Check if all days are excluded. That is no day is included.
     * </p>
     * 
     * @return boolean
     */
    public boolean areAllDaysExcluded() {
        for (int i = 1; i <= 31; i++) {
            if (isDayExcluded(i) == false) return false;
        }

        return true;
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
        if (excludeAll == true) return false;

        // Test the base calendar first. Only if the base calendar not already
        // excludes the time/date, continue evaluating this calendar instance.
        if (super.isTimeIncluded(timeStamp) == false) { return false; }

        java.util.Calendar cl = java.util.Calendar.getInstance();
        cl.setTime(new Date(timeStamp));
        int day = cl.get(java.util.Calendar.DAY_OF_MONTH);

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
        if (excludeAll == true) return 0;

        // Call base calendar implementation first
        long baseTime = super.getNextIncludedTime(timeStamp);
        if ((baseTime > 0) && (baseTime > timeStamp)) timeStamp = baseTime;

        // Get timestamp for 00:00:00
        long newTimeStamp = buildHoliday(timeStamp);

        java.util.Calendar cl = getJavaCalendar(newTimeStamp);
        int day = cl.get(java.util.Calendar.DAY_OF_MONTH);

        if (!isDayExcluded(day)) return timeStamp; // return the original value

        while (isDayExcluded(day) == true) {
            cl.add(java.util.Calendar.DATE, 1);
            day = cl.get(java.util.Calendar.DAY_OF_WEEK);
        }

        return cl.getTime().getTime();
    }
}
