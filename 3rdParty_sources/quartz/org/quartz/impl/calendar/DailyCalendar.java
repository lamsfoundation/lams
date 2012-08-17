package org.quartz.impl.calendar;

import java.text.NumberFormat;
import java.util.Calendar;

/**
 * This implementation of the Calendar excludes (or includes - see below) a 
 * specified time range each day. For example, you could use this calendar to 
 * exclude business hours (8AM - 5PM) every day. Each <CODE>DailyCalendar</CODE>
 * only allows a single time range to be specified, and that time range may not
 * cross daily boundaries (i.e. you cannot specify a time range from 8PM - 5AM).
 * If the property <CODE>invertTimeRange</CODE> is <CODE>false</CODE> (default), 
 * the time range defines a range of times in which triggers are not allowed to
 * fire. If <CODE>invertTimeRange</CODE> is <CODE>true</CODE>, the time range
 * is inverted &ndash; that is, all times <I>outside</I> the defined time range
 * are excluded.
 * <P>
 * Note when using <CODE>DailyCalendar</CODE>, it behaves on the same principals
 * as, for example, {@link org.quartz.impl.calendar.WeeklyCalendar 
 * WeeklyCalendar}. <CODE>WeeklyCalendar</CODE> defines a set of days that are
 * excluded <I>every week</I>. Likewise, <CODE>DailyCalendar</CODE> defines a 
 * set of times that are excluded <I>every day</I>.
 * 
 * @author Mike Funk, Aaron Craven
 * @version $Revision$ $Date$
 */
public class DailyCalendar extends BaseCalendar {
    private static final String invalidHourOfDay = "Invalid hour of day: ";
    private static final String invalidMinute = "Invalid minute: ";
    private static final String invalidSecond = "Invalid second: ";
    private static final String invalidMillis = "Invalid millis: ";
    private static final String invalidTimeRange = "Invalid time range: ";
    private static final String separator = " - ";
    private static final long oneMillis = 1;
    private static final String colon = ":";

    private String name;
    private int rangeStartingHourOfDay;
    private int rangeStartingMinute;
    private int rangeStartingSecond;
    private int rangeStartingMillis;
    private int rangeEndingHourOfDay;
    private int rangeEndingMinute;
    private int rangeEndingSecond;
    private int rangeEndingMillis;
    
    private boolean invertTimeRange = false;

    /**
     * Create a <CODE>DailyCalendar</CODE> with a time range defined by the
     * specified strings and no <CODE>baseCalendar</CODE>. 
     * <CODE>rangeStartingTime</CODE> and <CODE>rangeEndingTime</CODE>
     * must be in the format &quot;HH:MM[:SS[:mmm]]&quot; where:
     * <UL><LI>HH is the hour of the specified time. The hour should be
     *         specified using military (24-hour) time and must be in the range
     *         0 to 23.</LI>
     *     <LI>MM is the minute of the specified time and must be in the range
     *         0 to 59.</LI>
     *     <LI>SS is the second of the specified time and must be in the range
     *         0 to 59.</LI>
     *     <LI>mmm is the millisecond of the specified time and must be in the
     *         range 0 to 999.</LI>
     *     <LI>items enclosed in brackets ('[', ']') are optional.</LI>
     *     <LI>The time range starting time must be before the time range ending
     *         time. Note this means that a time range may not cross daily 
     *         boundaries (10PM - 2AM)</LI>  
     * </UL>
     *  
     * @param name              the name for the <CODE>DailyCalendar</CODE>
     * @param rangeStartingTime a String representing the starting time for the
     *                          time range
     * @param rangeEndingTime   a String representing the ending time for the
     *                          the time range
     */
    public DailyCalendar(String name,
                         String rangeStartingTime,
                         String rangeEndingTime) {
        super();
        this.name = name;
        setTimeRange(rangeStartingTime, rangeEndingTime);
    }

    /**
     * Create a <CODE>DailyCalendar</CODE> with a time range defined by the
     * specified strings and the specified <CODE>baseCalendar</CODE>. 
     * <CODE>rangeStartingTime</CODE> and <CODE>rangeEndingTime</CODE>
     * must be in the format &quot;HH:MM[:SS[:mmm]]&quot; where:
     * <UL><LI>HH is the hour of the specified time. The hour should be
     *         specified using military (24-hour) time and must be in the range
     *         0 to 23.</LI>
     *     <LI>MM is the minute of the specified time and must be in the range
     *         0 to 59.</LI>
     *     <LI>SS is the second of the specified time and must be in the range
     *         0 to 59.</LI>
     *     <LI>mmm is the millisecond of the specified time and must be in the
     *         range 0 to 999.</LI>
     *     <LI>items enclosed in brackets ('[', ']') are optional.</LI>
     *     <LI>The time range starting time must be before the time range ending
     *         time. Note this means that a time range may not cross daily 
     *         boundaries (10PM - 2AM)</LI>  
     * </UL>
     * 
     * @param name              the name for the <CODE>DailyCalendar</CODE>
     * @param baseCalendar      the base calendar for this calendar instance
     *                          &ndash; see {@link BaseCalendar} for more
     *                          information on base calendar functionality
     * @param rangeStartingTime a String representing the starting time for the
     *                          time range
     * @param rangeEndingTime   a String representing the ending time for the
     *                          time range
     */
    public DailyCalendar(String name,
                         org.quartz.Calendar baseCalendar,
                         String rangeStartingTime,
                         String rangeEndingTime) {
        super(baseCalendar);
        this.name = name;
        setTimeRange(rangeStartingTime, rangeEndingTime);
    }

    /**
     * Create a <CODE>DailyCalendar</CODE> with a time range defined by the
     * specified values and no <CODE>baseCalendar</CODE>. Values are subject to
     * the following validations:
     * <UL><LI>Hours must be in the range 0-23 and are expressed using military
     *         (24-hour) time.</LI>
     *     <LI>Minutes must be in the range 0-59</LI>
     *     <LI>Seconds must be in the range 0-59</LI>
     *     <LI>Milliseconds must be in the range 0-999</LI>
     *     <LI>The time range starting time must be before the time range ending
     *         time. Note this means that a time range may not cross daily 
     *         boundaries (10PM - 2AM)</LI>  
     * </UL>
     * 
     * @param name                   the name for the <CODE>DailyCalendar</CODE>
     * @param rangeStartingHourOfDay the hour of the start of the time range
     * @param rangeStartingMinute    the minute of the start of the time range
     * @param rangeStartingSecond    the second of the start of the time range
     * @param rangeStartingMillis    the millisecond of the start of the time 
     *                               range
     * @param rangeEndingHourOfDay   the hour of the end of the time range
     * @param rangeEndingMinute      the minute of the end of the time range
     * @param rangeEndingSecond      the second of the end of the time range
     * @param rangeEndingMillis      the millisecond of the start of the time 
     *                               range
     */
    public DailyCalendar(String name,
                         int rangeStartingHourOfDay,
                         int rangeStartingMinute,
                         int rangeStartingSecond,
                         int rangeStartingMillis,
                         int rangeEndingHourOfDay,
                         int rangeEndingMinute,
                         int rangeEndingSecond,
                         int rangeEndingMillis) {
        super();
        this.name = name;
        setTimeRange(rangeStartingHourOfDay,
                     rangeStartingMinute,
                     rangeStartingSecond,
                     rangeStartingMillis,
                     rangeEndingHourOfDay,
                     rangeEndingMinute,
                     rangeEndingSecond,
                     rangeEndingMillis);
    }
    
    /**
     * Create a <CODE>DailyCalendar</CODE> with a time range defined by the
     * specified values and the specified <CODE>baseCalendar</CODE>. Values are
     * subject to the following validations:
     * <UL><LI>Hours must be in the range 0-23 and are expressed using military
     *         (24-hour) time.</LI>
     *     <LI>Minutes must be in the range 0-59</LI>
     *     <LI>Seconds must be in the range 0-59</LI>
     *     <LI>Milliseconds must be in the range 0-999</LI>
     *     <LI>The time range starting time must be before the time range ending
     *         time. Note this means that a time range may not cross daily
     *         boundaries (10PM - 2AM)</LI>  
     * </UL> 
     * 
     * @param name                      the name for the 
     *                                  <CODE>DailyCalendar</CODE>
     * @param baseCalendar              the base calendar for this calendar
     *                                  instance &ndash; see 
     *                                  {@link BaseCalendar} for more 
     *                                  information on base calendar 
     *                                  functionality
     * @param rangeStartingHourOfDay the hour of the start of the time range
     * @param rangeStartingMinute    the minute of the start of the time range
     * @param rangeStartingSecond    the second of the start of the time range
     * @param rangeStartingMillis    the millisecond of the start of the time 
     *                               range
     * @param rangeEndingHourOfDay   the hour of the end of the time range
     * @param rangeEndingMinute      the minute of the end of the time range
     * @param rangeEndingSecond      the second of the end of the time range
     * @param rangeEndingMillis      the millisecond of the start of the time 
     *                               range
     */
    public DailyCalendar(String name,
                         org.quartz.Calendar baseCalendar,
                         int rangeStartingHourOfDay,
                         int rangeStartingMinute,
                         int rangeStartingSecond,
                         int rangeStartingMillis,
                         int rangeEndingHourOfDay,
                         int rangeEndingMinute,
                         int rangeEndingSecond,
                         int rangeEndingMillis) {
        super(baseCalendar);
        this.name = name;
        setTimeRange(rangeStartingHourOfDay,
                     rangeStartingMinute,
                     rangeStartingSecond,
                     rangeStartingMillis,
                     rangeEndingHourOfDay,
                     rangeEndingMinute,
                     rangeEndingSecond,
                     rangeEndingMillis);
    }

    /**
     * Create a <CODE>DailyCalendar</CODE> with a time range defined by the
     * specified <CODE>java.util.Calendar</CODE>s and no 
     * <CODE>baseCalendar</CODE>. The Calendars are subject to the following
     * considerations:
     * <UL><LI>Only the time-of-day fields of the specified Calendars will be
     *         used (the date fields will be ignored)</LI>
     *     <LI>The starting time must be before the ending time of the defined
     *         time range. Note this means that a time range may not cross
     *         daily boundaries (10PM - 2AM). <I>(because only time fields are
     *         are used, it is possible for two Calendars to represent a valid
     *         time range and 
     *         <CODE>rangeStartingCalendar.after(rangeEndingCalendar) == 
     *         true</CODE>)</I></LI>  
     * </UL> 
     * 
     * @param name                  the name for the <CODE>DailyCalendar</CODE>
     * @param rangeStartingCalendar a java.util.Calendar representing the 
     *                              starting time for the time range
     * @param rangeEndingCalendar   a java.util.Calendar representing the ending
     *                              time for the time range
     */
    public DailyCalendar(String name,
                         Calendar rangeStartingCalendar,
                         Calendar rangeEndingCalendar) {
        super();
        this.name = name;
        setTimeRange(rangeStartingCalendar, rangeEndingCalendar);
    }

    /**
     * Create a <CODE>DailyCalendar</CODE> with a time range defined by the
     * specified <CODE>java.util.Calendar</CODE>s and the specified 
     * <CODE>baseCalendar</CODE>. The Calendars are subject to the following
     * considerations:
     * <UL><LI>Only the time-of-day fields of the specified Calendars will be
     *         used (the date fields will be ignored)</LI>
     *     <LI>The starting time must be before the ending time of the defined
     *         time range. Note this means that a time range may not cross
     *         daily boundaries (10PM - 2AM). <I>(because only time fields are
     *         are used, it is possible for two Calendars to represent a valid
     *         time range and 
     *         <CODE>rangeStartingCalendar.after(rangeEndingCalendar) == 
     *         true</CODE>)</I></LI>  
     * </UL> 
     * 
     * @param name                  the name for the <CODE>DailyCalendar</CODE>
     * @param baseCalendar          the base calendar for this calendar instance
     *                              &ndash; see {@link BaseCalendar} for more 
     *                              information on base calendar functionality
     * @param rangeStartingCalendar a java.util.Calendar representing the 
     *                              starting time for the time range
     * @param rangeEndingCalendar   a java.util.Calendar representing the ending
     *                              time for the time range
     */
    public DailyCalendar(String name,
                         org.quartz.Calendar baseCalendar,
                         Calendar rangeStartingCalendar,
                         Calendar rangeEndingCalendar) {
        super(baseCalendar);
        this.name = name;
        setTimeRange(rangeStartingCalendar, rangeEndingCalendar);
    }

    /**
     * Create a <CODE>DailyCalendar</CODE> with a time range defined by the
     * specified values and no <CODE>baseCalendar</CODE>. The values are 
     * subject to the following considerations:
     * <UL><LI>Only the time-of-day portion of the specified values will be
     *         used</LI>
     *     <LI>The starting time must be before the ending time of the defined
     *         time range. Note this means that a time range may not cross
     *         daily boundaries (10PM - 2AM). <I>(because only time value are
     *         are used, it is possible for the two values to represent a valid
     *         time range and <CODE>rangeStartingTime &gt; 
     *         rangeEndingTime</CODE>)</I></LI>  
     * </UL> 
     * 
     * @param name                      the name for the 
     *                                  <CODE>DailyCalendar</CODE>
     * @param rangeStartingTimeInMillis a long representing the starting time 
     *                                  for the time range
     * @param rangeEndingTimeInMillis   a long representing the ending time for
     *                                  the time range
     */
    public DailyCalendar(String name,
                         long rangeStartingTimeInMillis,
                         long rangeEndingTimeInMillis) {
        super();
        this.name = name;
        setTimeRange(rangeStartingTimeInMillis, 
        		     rangeEndingTimeInMillis);
    }

    /**
     * Create a <CODE>DailyCalendar</CODE> with a time range defined by the
     * specified values and the specified <CODE>baseCalendar</CODE>. The values
     * are subject to the following considerations:
     * <UL><LI>Only the time-of-day portion of the specified values will be
     *         used</LI>
     *     <LI>The starting time must be before the ending time of the defined
     *         time range. Note this means that a time range may not cross
     *         daily boundaries (10PM - 2AM). <I>(because only time value are
     *         are used, it is possible for the two values to represent a valid
     *         time range and <CODE>rangeStartingTime &gt; 
     *         rangeEndingTime</CODE>)</I></LI>  
     * </UL> 
     * 
     * @param name                      the name for the 
     *                                  <CODE>DailyCalendar</CODE>
     * @param baseCalendar              the base calendar for this calendar
     *                                  instance &ndash; see {@link 
     *                                  BaseCalendar} for more information on 
     *                                  base calendar functionality
     * @param rangeStartingTimeInMillis a long representing the starting time 
     *                                  for the time range
     * @param rangeEndingTimeInMillis   a long representing the ending time for
     *                                  the time range
     */
    public DailyCalendar(String name,
                         org.quartz.Calendar baseCalendar,
                         long rangeStartingTimeInMillis,
                         long rangeEndingTimeInMillis) {
        super(baseCalendar);
        this.name = name;
        setTimeRange(rangeStartingTimeInMillis,
        		     rangeEndingTimeInMillis);
    }

    /**
     * Returns the name of the <CODE>DailyCalendar</CODE>
     * 
     * @return the name of the <CODE>DailyCalendar</CODE>
     */
    public String getName() {
        return name;
    }

    /**
     * Determines whether the given time (in milliseconds) is 'included' by the
     * <CODE>BaseCalendar</CODE>
     * 
     * @param timeInMillis the date/time to test
     * @return a boolean indicating whether the specified time is 'included' by
     *         the <CODE>BaseCalendar</CODE>
     */
    public boolean isTimeIncluded(long timeInMillis) {        
        if ((getBaseCalendar() != null) && 
        		(getBaseCalendar().isTimeIncluded(timeInMillis) == false)) {
        	return false;
        }
        
        long startOfDayInMillis = getStartOfDayInMillis(timeInMillis);
        long endOfDayInMillis = getEndOfDayInMillis(timeInMillis);
        long timeRangeStartingTimeInMillis = 
        	getTimeRangeStartingTimeInMillis(timeInMillis);
        long timeRangeEndingTimeInMillis = 
        	getTimeRangeEndingTimeInMillis(timeInMillis);
        if (!invertTimeRange) {
	        if ((timeInMillis > startOfDayInMillis && 
	        		timeInMillis < timeRangeStartingTimeInMillis) ||
	            (timeInMillis > timeRangeEndingTimeInMillis && 
	            	timeInMillis < endOfDayInMillis)) {
	        	
	        		return true;
	        } else {
	        	return false;
	        }
        } else {
        	if ((timeInMillis >= timeRangeStartingTimeInMillis) &&
        			(timeInMillis <= timeRangeEndingTimeInMillis)) {
        		return true;
        	} else {
        		return false;
        	}
        }
    }

    /**
     * Determines the next time included by the <CODE>DailyCalendar</CODE>
     * after the specified time.
     * 
     * @param timeInMillis the initial date/time after which to find an 
     *                     included time
     * @return the time in milliseconds representing the next time included
     *         after the specified time.
     */
    public long getNextIncludedTime(long timeInMillis) {
        long nextIncludedTime = timeInMillis + oneMillis;
        
        while (!isTimeIncluded(nextIncludedTime)) {
        	if (!invertTimeRange) {
	        	//If the time is in a range excluded by this calendar, we can
	        	// move to the end of the excluded time range and continue 
	        	// testing from there. Otherwise, if nextIncludedTime is 
        		// excluded by the baseCalendar, ask it the next time it 
        		// includes and begin testing from there. Failing this, add one
        		// millisecond and continue testing.
	        	if ((nextIncludedTime >= 
	        			getTimeRangeStartingTimeInMillis(nextIncludedTime)) && 
					(nextIncludedTime <= 
						getTimeRangeEndingTimeInMillis(nextIncludedTime))) {
	        		
	        		nextIncludedTime = 
	        			getTimeRangeEndingTimeInMillis(nextIncludedTime) + 
							oneMillis;
	        	} else if ((getBaseCalendar() != null) && 
	        			(!getBaseCalendar().isTimeIncluded(nextIncludedTime))){
	        		nextIncludedTime = 
	        			getBaseCalendar().getNextIncludedTime(nextIncludedTime);
	        	} else {
	        		nextIncludedTime++;
	        	}
        	} else {
	        	//If the time is in a range excluded by this calendar, we can
	        	// move to the end of the excluded time range and continue 
	        	// testing from there. Otherwise, if nextIncludedTime is 
        		// excluded by the baseCalendar, ask it the next time it 
        		// includes and begin testing from there. Failing this, add one
        		// millisecond and continue testing.
        		if (nextIncludedTime < 
        				getTimeRangeStartingTimeInMillis(nextIncludedTime)) {
        			nextIncludedTime = 
        				getTimeRangeStartingTimeInMillis(nextIncludedTime);
        		} else if (nextIncludedTime > 
        				getTimeRangeEndingTimeInMillis(nextIncludedTime)) {
        			//(move to start of next day)
        			nextIncludedTime = getEndOfDayInMillis(nextIncludedTime);
        			nextIncludedTime += 1l; 
        		} else if ((getBaseCalendar() != null) && 
	        			(!getBaseCalendar().isTimeIncluded(nextIncludedTime))){
	        		nextIncludedTime = 
	        			getBaseCalendar().getNextIncludedTime(nextIncludedTime);
	        	} else {
	        		nextIncludedTime++;
	        	}
        	}
        }
        
        return nextIncludedTime;
    }

    /**
     * Returns the start time of the time range (in milliseconds) of the day 
     * specified in <CODE>timeInMillis</CODE>
     * 
     * @param timeInMillis a time containing the desired date for the starting
     *                     time of the time range.
     * @return a date/time (in milliseconds) representing the start time of the
     *         time range for the specified date.
     */
    public long getTimeRangeStartingTimeInMillis(long timeInMillis) {
        Calendar rangeStartingTime = Calendar.getInstance();
        rangeStartingTime.setTimeInMillis(timeInMillis);
        rangeStartingTime.set(Calendar.HOUR_OF_DAY, rangeStartingHourOfDay);
        rangeStartingTime.set(Calendar.MINUTE, rangeStartingMinute);
        rangeStartingTime.set(Calendar.SECOND, rangeStartingSecond);
        rangeStartingTime.set(Calendar.MILLISECOND, rangeStartingMillis);
        return rangeStartingTime.getTimeInMillis();
    }

    /**
     * Returns the end time of the time range (in milliseconds) of the day
     * specified in <CODE>timeInMillis</CODE>
     * 
     * @param timeInMillis a time containing the desired date for the ending
     *                     time of the time range.
     * @return a date/time (in milliseconds) representing the end time of the
     *         time range for the specified date.
     */
    public long getTimeRangeEndingTimeInMillis(long timeInMillis) {
        Calendar rangeEndingTime = Calendar.getInstance();
        rangeEndingTime.setTimeInMillis(timeInMillis);
        rangeEndingTime.set(Calendar.HOUR_OF_DAY, rangeEndingHourOfDay);
        rangeEndingTime.set(Calendar.MINUTE, rangeEndingMinute);
        rangeEndingTime.set(Calendar.SECOND, rangeEndingSecond);
        rangeEndingTime.set(Calendar.MILLISECOND, rangeEndingMillis);
        return rangeEndingTime.getTimeInMillis();
    }

    /**
     * Indicates whether the time range represents an inverted time range (see
     * class description).
     * 
     * @return a boolean indicating whether the time range is inverted
     */
    public boolean getInvertTimeRange() {
    	return invertTimeRange;
    }
    
    /**
     * Indicates whether the time range represents an inverted time range (see
     * class description).
     * 
     * @param flag the new value for the <CODE>invertTimeRange</CODE> flag.
     */
    public void setInvertTimeRange(boolean flag) {
    	this.invertTimeRange = flag;
    }
    
    /**
     * Returns a string representing the properties of the 
     * <CODE>DailyCalendar</CODE>
     * 
     * @return the properteis of the DailyCalendar in a String format
     */
    public String toString() {
        long todayInMillis = Calendar.getInstance().getTimeInMillis();
        NumberFormat numberFormatter = NumberFormat.getNumberInstance();
        numberFormatter.setMaximumFractionDigits(0);
        numberFormatter.setMinimumIntegerDigits(2);
        StringBuffer buffer = new StringBuffer();
        buffer.append(getName());
        buffer.append(": base calendar: [");
        if (getBaseCalendar() != null) {
        	buffer.append(getBaseCalendar().toString());
        } else {
        	buffer.append("null");
        }
        buffer.append("], time range: '");
        buffer.append(numberFormatter.format(rangeStartingHourOfDay));
        buffer.append(":");
        buffer.append(numberFormatter.format(rangeStartingMinute));
        buffer.append(":");
        buffer.append(numberFormatter.format(rangeStartingSecond));
        buffer.append(":");
        numberFormatter.setMinimumIntegerDigits(3);
        buffer.append(numberFormatter.format(rangeStartingMillis));
        numberFormatter.setMinimumIntegerDigits(2);
        buffer.append(" - ");
        buffer.append(numberFormatter.format(rangeEndingHourOfDay));
        buffer.append(":");
        buffer.append(numberFormatter.format(rangeEndingMinute));
        buffer.append(":");
        buffer.append(numberFormatter.format(rangeEndingSecond));
        buffer.append(":");
        numberFormatter.setMinimumIntegerDigits(3);
        buffer.append(numberFormatter.format(rangeEndingMillis));
        buffer.append("', inverted: " + 
        		Boolean.toString(invertTimeRange) + "]");
        return buffer.toString();
    }
    
    /**
     * Sets the time range for the <CODE>DailyCalendar</CODE> to the times 
     * represented in the specified Strings. 
     * 
     * @param rangeStartingTimeString a String representing the start time of 
     *                                the time range
     * @param rangeEndingTimeString   a String representing the end time of the
     *                                excluded time range
     */
    private void setTimeRange(String rangeStartingTimeString,
                              String rangeEndingTimeString) {
    	String[] rangeStartingTime;
    	int rangeStartingHourOfDay;
    	int rangeStartingMinute;
    	int rangeStartingSecond;
    	int rangeStartingMillis;
    	
    	String[] rangeEndingTime;
    	int rangeEndingHourOfDay;
    	int rangeEndingMinute;
    	int rangeEndingSecond;
    	int rangeEndingMillis;
    	
    	rangeStartingTime = rangeStartingTimeString.split(colon);
        
        if ((rangeStartingTime.length < 2) || (rangeStartingTime.length > 4)) {
        	throw new IllegalArgumentException("Invalid time string '" + 
        			rangeStartingTimeString + "'");
        }
        
        rangeStartingHourOfDay = Integer.parseInt(rangeStartingTime[0]);
        rangeStartingMinute = Integer.parseInt(rangeStartingTime[1]);
        if (rangeStartingTime.length > 2) {
        	rangeStartingSecond = Integer.parseInt(rangeStartingTime[2]);
        } else {
        	rangeStartingSecond = 0;
        }
        if (rangeStartingTime.length == 4) {
	        rangeStartingMillis = Integer.parseInt(rangeStartingTime[3]);
        } else {
        	rangeStartingMillis = 0;
        }
        
        rangeEndingTime = rangeEndingTimeString.split(colon);

        if ((rangeEndingTime.length < 2) || (rangeEndingTime.length > 4)) {
        	throw new IllegalArgumentException("Invalid time string '" + 
        			rangeEndingTimeString + "'");
        }
        
        rangeEndingHourOfDay = Integer.parseInt(rangeEndingTime[0]);
        rangeEndingMinute = Integer.parseInt(rangeEndingTime[1]);
        if (rangeEndingTime.length > 2) {
        	rangeEndingSecond = Integer.parseInt(rangeEndingTime[2]);
        } else {
        	rangeEndingSecond = 0;
        }
        if (rangeEndingTime.length == 4) {
        	rangeEndingMillis = Integer.parseInt(rangeEndingTime[3]);
        } else {
        	rangeEndingMillis = 0;
        }
        
        setTimeRange(rangeStartingHourOfDay,
                     rangeStartingMinute,
                     rangeStartingSecond,
                     rangeStartingMillis,
                     rangeEndingHourOfDay,
                     rangeEndingMinute,
                     rangeEndingSecond,
                     rangeEndingMillis);
    }

    /**
     * Sets the time range for the <CODE>DailyCalendar</CODE> to the times
     * represented in the specified values.  
     * 
     * @param rangeStartingHourOfDay the hour of the start of the time range
     * @param rangeStartingMinute    the minute of the start of the time range
     * @param rangeStartingSecond    the second of the start of the time range
     * @param rangeStartingMillis    the millisecond of the start of the time
     *                               range
     * @param rangeEndingHourOfDay   the hour of the end of the time range
     * @param rangeEndingMinute      the minute of the end of the time range
     * @param rangeEndingSecond      the second of the end of the time range
     * @param rangeEndingMillis      the millisecond of the start of the time 
     *                               range
     */
    private void setTimeRange(int rangeStartingHourOfDay,
                              int rangeStartingMinute,
                              int rangeStartingSecond,
                              int rangeStartingMillis,
                              int rangeEndingHourOfDay,
                              int rangeEndingMinute,
                              int rangeEndingSecond,
                              int rangeEndingMillis) {
        validate(rangeStartingHourOfDay,
                 rangeStartingMinute,
                 rangeStartingSecond,
                 rangeStartingMillis);
        
        validate(rangeEndingHourOfDay,
                 rangeEndingMinute,
                 rangeEndingSecond,
                 rangeEndingMillis);
        
        Calendar startCal = Calendar.getInstance();
        startCal.set(Calendar.HOUR_OF_DAY, rangeStartingHourOfDay);
        startCal.set(Calendar.MINUTE, rangeStartingMinute);
        startCal.set(Calendar.SECOND, rangeStartingSecond);
        startCal.set(Calendar.MILLISECOND, rangeStartingMillis);
        
        Calendar endCal = Calendar.getInstance();
        endCal.set(Calendar.HOUR_OF_DAY, rangeEndingHourOfDay);
        endCal.set(Calendar.MINUTE, rangeEndingMinute);
        endCal.set(Calendar.SECOND, rangeEndingSecond);
        endCal.set(Calendar.MILLISECOND, rangeEndingMillis);
        
        if (!startCal.before(endCal)) {
            throw new IllegalArgumentException(invalidTimeRange +
            		rangeStartingHourOfDay + ":" +
            		rangeStartingMinute + ":" +
            		rangeStartingSecond + ":" +
            		rangeStartingMillis + separator +
            		rangeEndingHourOfDay + ":" +
            		rangeEndingMinute + ":" +
            		rangeEndingSecond + ":" +
            		rangeEndingMillis);
        }
        
        this.rangeStartingHourOfDay = rangeStartingHourOfDay;
        this.rangeStartingMinute = rangeStartingMinute;
        this.rangeStartingSecond = rangeStartingSecond;
        this.rangeStartingMillis = rangeStartingMillis;
        this.rangeEndingHourOfDay = rangeEndingHourOfDay;
        this.rangeEndingMinute = rangeEndingMinute;
        this.rangeEndingSecond = rangeEndingSecond;
        this.rangeEndingMillis = rangeEndingMillis;
    }
    
    /**
     * Sets the time range for the <CODE>DailyCalendar</CODE> to the times
     * represented in the specified <CODE>java.util.Calendar</CODE>s. 
     * 
     * @param rangeStartingCalendar a Calendar containing the start time for
     *                              the <CODE>DailyCalendar</CODE>
     * @param rangeEndingCalendar   a Calendar containing the end time for
     *                              the <CODE>DailyCalendar</CODE>
     */
    private void setTimeRange(Calendar rangeStartingCalendar,
    		                  Calendar rangeEndingCalendar) {
        setTimeRange(
                rangeStartingCalendar.get(Calendar.HOUR_OF_DAY),
                rangeStartingCalendar.get(Calendar.MINUTE),
                rangeStartingCalendar.get(Calendar.SECOND),
                rangeStartingCalendar.get(Calendar.MILLISECOND),
                rangeEndingCalendar.get(Calendar.HOUR_OF_DAY),
                rangeEndingCalendar.get(Calendar.MINUTE),
                rangeEndingCalendar.get(Calendar.SECOND),
                rangeEndingCalendar.get(Calendar.MILLISECOND));
    }
    
    /**
     * Sets the time range for the <CODE>DailyCalendar</CODE> to the times
     * represented in the specified values. 
     * 
     * @param rangeStartingTime the starting time (in milliseconds) for the
     *                          time range
     * @param rangeEndingTime   the ending time (in milliseconds) for the time
     *                          range
     */
    private void setTimeRange(long rangeStartingTime, 
    		                  long rangeEndingTime) {
    	Calendar startCal = Calendar.getInstance();
    	Calendar endCal = Calendar.getInstance();
    	startCal.setTimeInMillis(rangeStartingTime);
    	endCal.setTimeInMillis(rangeEndingTime);
    	
    	setTimeRange(startCal, endCal);
    }
    
    /**
     * Returns the start of the given day in milliseconds
     * 
     * @param timeInMillis a time containing the desired date for the 
     *                     start-of-day time.
     * @return the start of the given day in milliseconds
     */
    private long getStartOfDayInMillis(long timeInMillis) {
        Calendar startOfDay = Calendar.getInstance();
        startOfDay.setTimeInMillis(timeInMillis);
        startOfDay.set(Calendar.HOUR_OF_DAY, 0);
        startOfDay.set(Calendar.MINUTE, 0);
        startOfDay.set(Calendar.SECOND, 0);
        startOfDay.set(Calendar.MILLISECOND, 0);
        return startOfDay.getTimeInMillis();
    }

    /**
     * Returns the end of the given day in milliseconds
     * 
     * @param timeInMillis a time containing the desired date for the 
     *                     end-of-day time.
     * @return the end of the given day in milliseconds
     */
    private long getEndOfDayInMillis(long timeInMillis) {
        Calendar endOfDay = Calendar.getInstance();
        endOfDay.setTimeInMillis(timeInMillis);
        endOfDay.set(Calendar.HOUR_OF_DAY, 23);
        endOfDay.set(Calendar.MINUTE, 59);
        endOfDay.set(Calendar.SECOND, 59);
        endOfDay.set(Calendar.MILLISECOND, 999);
        return endOfDay.getTimeInMillis();
    }
    
    /**
     * Checks the specified values for validity as a set of time values.
     * 
     * @param hourOfDay the hour of the time to check (in military (24-hour)
     *                  time)
     * @param minute    the minute of the time to check
     * @param second    the second of the time to check
     * @param millis    the millisecond of the time to check
     */
    private void validate(int hourOfDay, int minute, int second, int millis) {
        if (hourOfDay < 0 || hourOfDay > 23) {
            throw new IllegalArgumentException(invalidHourOfDay + hourOfDay);
        }
        if (minute < 0 || minute > 59) {
            throw new IllegalArgumentException(invalidMinute + minute);
        }
        if (second < 0 || second > 59) {
            throw new IllegalArgumentException(invalidSecond + second);
        }
        if (millis < 0 || millis > 999) {
            throw new IllegalArgumentException(invalidMillis + millis);
        }
    }
}