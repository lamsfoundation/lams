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
 * This implementation of the Calendar may be used (you don't have to) as a
 * base class for more sophisticated one's. It merely implements the base
 * functionality required by each Calendar.
 * </p>
 * 
 * <p>
 * Regarded as base functionality is the treatment of base calendars. Base
 * calendar allow you to chain (stack) as much calendars as you may need. For
 * example to exclude weekends you may use WeeklyCalendar. In order to exclude
 * holidays as well you may define a WeeklyCalendar instance to be the base
 * calendar for HolidayCalendar instance.
 * </p>
 * 
 * @see org.quartz.Calendar
 * 
 * @author Juergen Donnerstag
 * @author James House
 */
public class BaseCalendar implements Calendar, Serializable {

    // <p>A optional base calendar.</p>
    private Calendar baseCalendar;

    private String description;

    /**
     * <p>
     * Default Constructor
     * </p>
     */
    public BaseCalendar() {
    }

    /**
     * <p>
     * Constructor
     * </p>
     */
    public BaseCalendar(Calendar baseCalendar) {
        setBaseCalendar(baseCalendar);
    }

    /**
     * <p>
     * Set a new base calendar or remove the existing one
     * </p>
     */
    public void setBaseCalendar(Calendar baseCalendar) {
        this.baseCalendar = baseCalendar;
    }

    /**
     * <p>
     * Get the base calendar. Will be null, if not set.
     * </p>
     */
    public Calendar getBaseCalendar() {
        return this.baseCalendar;
    }

    /**
     * <p>
     * Return the description given to the <code>Calendar</code> instance by
     * its creator (if any).
     * </p>
     * 
     * @return null if no description was set.
     */
    public String getDescription() {
        return description;
    }

    /**
     * <p>
     * Set a description for the <code>Calendar</code> instance - may be
     * useful for remembering/displaying the purpose of the calendar, though
     * the description has no meaning to Quartz.
     * </p>
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * <p>
     * Check if date/time represented by timeStamp is included. If included
     * return true. The implementation of BaseCalendar simply calls the base
     * calendars isTimeIncluded() method if base calendar is set.
     * </p>
     * 
     * @see org.quartz.Calendar#isTimeIncluded(long)
     */
    public boolean isTimeIncluded(long timeStamp) {

        if (timeStamp <= 0)
                throw new IllegalArgumentException(
                        "timeStamp must be greater 0");

        if (baseCalendar != null) {
            if (baseCalendar.isTimeIncluded(timeStamp) == false) { return false; }
        }

        return true;
    }

    /**
     * <p>
     * Determine the next time (in milliseconds) that is 'included' by the
     * Calendar after the given time. Return the original value if timeStamp is
     * included. Return 0 if all days are excluded.
     * </p>
     * 
     * @see org.quartz.Calendar#getNextIncludedTime(long)
     */
    public long getNextIncludedTime(long timeStamp) {

        if (timeStamp <= 0)
                throw new IllegalArgumentException(
                        "timeStamp must be greater 0");

        if (baseCalendar != null) { return baseCalendar
                .getNextIncludedTime(timeStamp); }

        return timeStamp;
    }

    /**
     * <p>
     * Utility method. Return the date of excludeDate. The time fraction will
     * be reset to 00.00:00.
     * </p>
     */
    static public Date buildHoliday(Date excludedDate) {
        java.util.Calendar cl = java.util.Calendar.getInstance();
        java.util.Calendar clEx = java.util.Calendar.getInstance();
        clEx.setTime(excludedDate);

        cl.setLenient(false);
        cl.clear();
        cl.set(clEx.get(java.util.Calendar.YEAR), clEx
                .get(java.util.Calendar.MONTH), clEx
                .get(java.util.Calendar.DATE));

        return cl.getTime();
    }

    /**
     * <p>
     * Utility method. Return just the date of excludeDate. The time fraction
     * will be reset to 00.00:00.
     * </p>
     */
    static public long buildHoliday(long timeStamp) {
        return buildHoliday(new Date(timeStamp)).getTime();
    }

    /**
     * <p>
     * Utility method. Return a java.util.Calendar for timeStamp.
     * </p>
     * 
     * @param timeStamp
     * @return Calendar
     */
    static public java.util.Calendar getJavaCalendar(long timeStamp) {
        java.util.Calendar cl = java.util.Calendar.getInstance();
        cl.setTime(new Date(timeStamp));
        return cl;
    }
}
