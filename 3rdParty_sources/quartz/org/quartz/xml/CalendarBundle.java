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
package org.quartz.xml;

import org.quartz.Calendar;

/**
 * Wraps a <code>Calendar</code>.
 * 
 * @author <a href="mailto:bonhamcm@thirdeyeconsulting.com">Chris Bonham</a>
 */
public class CalendarBundle implements Calendar {
    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * 
     * Data members.
     * 
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */
     
    protected String calendarName;

    protected String className;
    
    protected Calendar calendar;
    
    protected boolean replace;

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * 
     * Constructors.
     * 
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    public CalendarBundle() {
    }

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * 
     * Interface.
     * 
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    public String getCalendarName() {
        return calendarName;
    }

    public void setCalendarName(String calendarName) {
        this.calendarName = calendarName;
    }
    
    public String getClassName() {
        return className;
    }

    public void setClassName(String className)
        throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        this.className = className;
        createCalendar();
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }
    
    public boolean getReplace() {
        return replace;
    }
    
    public void setReplace(boolean replace) {
        this.replace = replace;
    }
    
    public Calendar getBaseCalendar() {
        return calendar.getBaseCalendar();
    }
    
    public void setBaseCalendar(Calendar baseCalendar) {
        if (baseCalendar instanceof CalendarBundle) {
            baseCalendar = ((CalendarBundle)baseCalendar).getCalendar();
        }
        calendar.setBaseCalendar(baseCalendar);
    }
    
    public String getDescription() {
        return calendar.getDescription();
    }

    public void setDescription(String description) {
        calendar.setDescription(description);
    }
    
    public boolean isTimeIncluded(long timeStamp) {
        return calendar.isTimeIncluded(timeStamp);
    }

    public long getNextIncludedTime(long timeStamp) {
        return calendar.getNextIncludedTime(timeStamp);
    }
    
    protected void createCalendar()
        throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        Class clazz = Thread.currentThread().getContextClassLoader().loadClass(getClassName());
        setCalendar((Calendar)clazz.newInstance());
    }
}
