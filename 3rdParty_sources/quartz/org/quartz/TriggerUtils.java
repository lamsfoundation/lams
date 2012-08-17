
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
package org.quartz;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.TimeZone;

/**
 * <p>
 * Convenience and utility methods for simplifying the construction and
 * configuration of <code>{@link Trigger}s</code>.
 * </p>
 * 
 * <p>
 * Please submit suggestions for additional convenience methods to either the
 * Quartz user forum or the developer's mail list at
 * <a href="http://www.sourceforge.net/projects/quartz">source forge</a>.
 * </p>
 * 
 * @see CronTrigger
 * @see SimpleTrigger
 * 
 * @author James House
 */
public class TriggerUtils {

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * 
     * Constants.
     * 
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    public static final int SUNDAY = 1;

    public static final int MONDAY = 2;

    public static final int TUESDAY = 3;

    public static final int WEDNESDAY = 4;

    public static final int THURSDAY = 5;

    public static final int FRIDAY = 6;

    public static final int SATURDAY = 7;

    public static final int LAST_DAY_OF_MONTH = -1;

    public static final long MILLISECONDS_IN_MINUTE = 60l * 1000l;

    public static final long MILLISECONDS_IN_HOUR = 60l * 60l * 1000l;

    public static final long SECONDS_IN_DAY = 24l * 60l * 60L;

    public static final long MILLISECONDS_IN_DAY = SECONDS_IN_DAY * 1000l;

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * 
     * Interface.
     * 
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    private static void validateDayOfWeek(int dayOfWeek) {
        if (dayOfWeek < SUNDAY || dayOfWeek > SATURDAY)
                throw new IllegalArgumentException("Invalid day of week.");
    }

    private static void validateHour(int hour) {
        if (hour < 0 || hour > 23)
                throw new IllegalArgumentException(
                        "Invalid hour (must be >= 0 and <= 23).");
    }

    private static void validateMinute(int minute) {
        if (minute < 0 || minute > 59)
                throw new IllegalArgumentException(
                        "Invalid minute (must be >= 0 and <= 59).");
    }

    private static void validateSecond(int second) {
        if (second < 0 || second > 59)
                throw new IllegalArgumentException(
                        "Invalid second (must be >= 0 and <= 59).");
    }

    private static void validateDayOfMonth(int day) {
        if ((day < 1 || day > 31) && day != LAST_DAY_OF_MONTH)
                throw new IllegalArgumentException("Invalid day of month.");
    }

    private static void validateMonth(int month) {
        if (month < 1 || month > 12)
                throw new IllegalArgumentException(
                        "Invalid month (must be >= 1 and <= 12.");
    }

    private static void validateYear(int year) {
        if (year < 1970 || year > 2099)
                throw new IllegalArgumentException(
                        "Invalid year (must be >= 1970 and <= 2099.");
    }

    /**
     * <p>
     * Set the given <code>Trigger</code>'s name to the given value, and its
     * group to the default group (<code>Scheduler.DEFAULT_GROUP</code>).
     * </p>
     * 
     * @param trig the tigger to change name to 
     * @param name the new trigger name
     */
    public static void setTriggerIdentity(Trigger trig, String name) {
        setTriggerIdentity(trig, name, Scheduler.DEFAULT_GROUP);
    }

    /**
     * <p>
     * Set the given <code>Trigger</code>'s name to the given value, and its
     * group to the given group.
     * </p>
     * 
     * @param trig the tigger to change name to 
     * @param name the new trigger name
     * @param group the new trigger group
     */
    public static void setTriggerIdentity(
            Trigger trig, String name, String group) {
        trig.setName(name);
        trig.setGroup(group);
    }

    /**
     * <p>
     * Make a trigger that will fire every day at the given time.
     * </p>
     * 
     * <p>
     * The generated trigger will not have its name, group,
     * or end-time set.  The Start time defaults to 'now'.
     * </p>
     * 
     * @param hour the hour (0-23) upon which to fire
     * @param minute the minute (0-59) upon which to fire
     * @return the new trigger
     */
    public static Trigger makeDailyTrigger(int hour, int minute) {
        validateHour(hour);
        validateMinute(minute);

        CronTrigger trig = new CronTrigger();

        try {
            trig.setCronExpression("0 " + minute + " " + hour + " ? * *");
        } catch (Exception ignore) {
            return null; /* never happens... */
        }

        trig.setStartTime(new Date());
        
        return trig;
    }

    /**
     * <p>
     * Make a trigger that will fire every day at the given time.
     * </p>
     * 
     * <p>
     * The generated trigger will not have its group or end-time set.  
     * The Start time defaults to 'now'.
     * </p>
     * 
     * @param trigName the trigger's name 
     * @param hour the hour (0-23) upon which to fire
     * @param minute the minute (0-59) upon which to fire
     * @return the newly created trigger
     */
    public static Trigger makeDailyTrigger(
            String trigName, int hour, int minute) {
        Trigger trig = makeDailyTrigger(hour, minute);
        trig.setName(trigName);
        return trig;
    }
    
    /**
     * <p>
     * Make a trigger that will fire every week at the given day and time.
     * </p>
     * 
     * <p>
     * The generated trigger will not have its name, group,
     * or end-time set.  The Start time defaults to 'now'.
     * </p>
     * 
     * @param dayOfWeek (1-7) the day of week upon which to fire
     * @param hour the hour (0-23) upon which to fire
     * @param minute the minute (0-59) upon which to fire
     * @return the new trigger
     * 
     * @see #SUNDAY
     * @see #MONDAY
     * @see #TUESDAY
     * @see #WEDNESDAY
     * @see #THURSDAY
     * @see #FRIDAY
     * @see #SATURDAY
     */
    public static Trigger makeWeeklyTrigger(
            int dayOfWeek, int hour, int minute) {
        validateDayOfWeek(dayOfWeek);
        validateHour(hour);
        validateMinute(minute);

        CronTrigger trig = new CronTrigger();

        try {
            trig.setCronExpression("0 " + minute + " " + hour + " ? * "
                    + dayOfWeek);
        } catch (Exception ignore) {
            return null; /* never happens... */
        }
        
        trig.setStartTime(new Date());

        return trig;
    }

    /**
     * <p>
     * Make a trigger that will fire every week at the given day and time.
     * </p>
     * 
     * <p>
     * The generated trigger will not have its group,
     * or end-time set.  The Start time defaults to 'now'.
     * </p>
     * 
     * @param trigName the trigger's name
     * @param dayOfWeek (1-7) the day of week upon which to fire
     * @param hour the hour (0-23) upon which to fire
     * @param minute the minute (0-59) upon which to fire
     * @return the newly created trigger
     * 
     * @see #SUNDAY
     * @see #MONDAY
     * @see #TUESDAY
     * @see #WEDNESDAY
     * @see #THURSDAY
     * @see #FRIDAY
     * @see #SATURDAY
     */
    public static Trigger makeWeeklyTrigger(
            String trigName, int dayOfWeek, int hour, int minute) {
        Trigger trig = makeWeeklyTrigger(dayOfWeek, hour, minute);
        trig.setName(trigName);
        return trig;
    }
    
    
    /**
     * <p>
     * Make a trigger that will fire every month at the given day and time.
     * </p>
     * 
     * <p>
     * The generated trigger will not have its name, group,
     * or end-time set.  The Start time defaults to 'now'.
     * </p>
     * 
     * <p>
     * If the day of the month specified does not occur in a given month, a
     * firing will not occur that month. (i.e. if dayOfMonth is specified as
     * 31, no firing will occur in the months of the year with fewer than 31
     * days).
     * </p>
     * 
     * @param dayOfMonth (1-31, or -1) the day of week upon which to fire
     * @param hour the hour (0-23) upon which to fire
     * @param minute the minute (0-59) upon which to fire
     * @return the newly created trigger
     */
    public static Trigger makeMonthlyTrigger(
            int dayOfMonth, int hour, int minute) {
        validateDayOfMonth(dayOfMonth);
        validateHour(hour);
        validateMinute(minute);

        CronTrigger trig = new CronTrigger();

        try {
            if (dayOfMonth != LAST_DAY_OF_MONTH) trig.setCronExpression("0 "
                    + minute + " " + hour + " " + dayOfMonth + " * ?");
            else
                trig.setCronExpression("0 " + minute + " " + hour + " L * ?");
        } catch (Exception ignore) {
            return null; /* never happens... */
        }
        
        trig.setStartTime(new Date());

        return trig;
    }

    /**
     * <p>
     * Make a trigger that will fire every month at the given day and time.
     * </p>
     * 
     * <p>
     * The generated trigger will not have its group,
     * or end-time set.  The Start time defaults to 'now'.
     * </p>
     * 
     * <p>
     * If the day of the month specified does not occur in a given month, a
     * firing will not occur that month. (i.e. if dayOfMonth is specified as
     * 31, no firing will occur in the months of the year with fewer than 31
     * days).
     * </p>
     * 
     * @param trigName the trigger's name
     * @param dayOfMonth (1-31, or -1) the day of week upon which to fire
     * @param hour the hour (0-23) upon which to fire
     * @param minute the minute (0-59) upon which to fire
     * @return the newly created trigger
     */
    public static Trigger makeMonthlyTrigger(
            String trigName, int dayOfMonth, int hour, int minute) {
        Trigger trig = makeMonthlyTrigger(dayOfMonth, hour, minute);
        trig.setName(trigName);
        return trig;
    }
    
    /*
     * <p> Make a trigger that will fire every N days at the given time. </p>
     * 
     * <p> TThe generated trigger will not have its name, group,
     * start-time and end-time set. </p>
     * 
     * @param hour the hour (0-23) upon which to fire @param minute the minute
     * (0-59) upon which to fire @param interval the number of days between
     * firings public static Trigger makeDailyTrigger(int interval, int hour,
     * int minute) {
     * 
     * SimpleTrigger trig = new SimpleTrigger();
     * 
     * MILLISECONDS_IN_DAY);
     * trig.setRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY);
     * 
     * return trig;
     *  }
     */

    /**
     * <p>
     * Make a trigger that will fire <code>repeatCount</code> times, waiting
     * <code>repeatInterval</code> milliseconds between each fire.
     * </p>
     * 
     * <p>
     * The generated trigger will not have its name, group,
     * or end-time set.  The Start time defaults to 'now'.
     * </p>
     *  
     * @param repeatCount the number of times to fire the trigger
     * @param repeatInterval the number of milliseconds to wait between fires
     * @return the newly created trigger
     */
    public static Trigger makeImmediateTrigger(
            int repeatCount, long repeatInterval) {
        SimpleTrigger trig = new SimpleTrigger();
        trig.setStartTime( new Date() );
        trig.setRepeatCount(repeatCount);
        trig.setRepeatInterval(repeatInterval);
        return trig;
    }    
    
    /**
     * <p>
     * Make a trigger that will fire <code>repeatCount</code> times, waiting
     * <code>repeatInterval</code> milliseconds between each fire.
     * </p>
     * 
     * <p>
     * The generated trigger will not have its name, group,
     * or end-time set.  The Start time defaults to 'now'.
     * </p>
     *
     * @param trigName the trigger's name 
     * @param repeatCount the number of times to fire the trigger
     * @param repeatInterval the number of milliseconds to wait between fires
     * @return the new trigger
     */
    public static Trigger makeImmediateTrigger(
            String trigName, int repeatCount, long repeatInterval) {
        Trigger trig = makeImmediateTrigger(repeatCount, repeatInterval);
        trig.setName(trigName);
        return trig;
    }    
    
    /**
     * <p>
     * Make a trigger that will fire every second, indefinitely.
     * </p>
     * 
     * <p>
     * The generated trigger will not have its name, group,
     * or end-time set.  The Start time defaults to 'now'.
     * </p>
     * @return the new trigger
     */
    public static Trigger makeSecondlyTrigger() {
        return makeSecondlyTrigger(1, SimpleTrigger.REPEAT_INDEFINITELY);
    }

    /**
     * <p>
     * Make a trigger that will fire every second, indefinitely.
     * </p>
     * 
     * <p>
     * The generated trigger will not have its group,
     * or end-time set.  The Start time defaults to 'now'.
     * </p>
     * 
     * @param trigName the trigger's name
     * @return the new trigger
     */
    public static Trigger makeSecondlyTrigger(String trigName) {
        return makeSecondlyTrigger(
                trigName, 1, SimpleTrigger.REPEAT_INDEFINITELY);
    }

    
    /**
     * <p>
     * Make a trigger that will fire every N seconds, indefinitely.
     * </p>
     * 
     * <p>
     * The generated trigger will not have its name, group,
     * or end-time set.  The Start time defaults to 'now'.
     * </p>
     * 
     * @param intervalInSeconds the number of seconds between firings
     * @return the new trigger
     */
    public static Trigger makeSecondlyTrigger(int intervalInSeconds) {
        return makeSecondlyTrigger(
                intervalInSeconds, SimpleTrigger.REPEAT_INDEFINITELY);
    }

    /**
     * <p>
     * Make a trigger that will fire every N seconds, with the given number of
     * repeats.
     * </p>
     * 
     * <p>
     * The generated trigger will not have its name, group,
     * or end-time set.  The Start time defaults to 'now'.
     * </p>
     * 
     * @param intervalInSeconds the number of seconds between firings
     * @param repeatCount the number of times to repeat the firing
     * @return the new trigger
     */
    public static Trigger makeSecondlyTrigger(
            int intervalInSeconds, int repeatCount) {
        SimpleTrigger trig = new SimpleTrigger();

        trig.setRepeatInterval(intervalInSeconds * 1000l);
        trig.setRepeatCount(repeatCount);

        return trig;
    }

    /**
     * <p>
     * Make a trigger that will fire every N seconds, with the given number of
     * repeats.
     * </p>
     * 
     * <p>
     * The generated trigger will not have its group,
     * or end-time set.  The Start time defaults to 'now'.
     * </p>
     * 
     * @param trigName the trigger's name
     * @param intervalInSeconds the number of seconds between firings
     * @param repeatCount the number of times to repeat the firing
     * @return the new trigger
     */
    public static Trigger makeSecondlyTrigger(
            String trigName, int intervalInSeconds, int repeatCount) {
        Trigger trig = makeSecondlyTrigger(intervalInSeconds, repeatCount);
        trig.setName(trigName);
        return trig;
    }

    /**
     * <p>
     * Make a trigger that will fire every minute, indefinitely.
     * </p>
     * 
     * <p>
     * The generated trigger will not have its name, group,
     * or end-time set.  The Start time defaults to 'now'.
     * </p>
     *  
     * @return the new trigger
     */
    public static Trigger makeMinutelyTrigger() {
        return makeMinutelyTrigger(1, SimpleTrigger.REPEAT_INDEFINITELY);
    }

    /**
     * <p>
     * Make a trigger that will fire every minute, indefinitely.
     * </p>
     * 
     * <p>
     * The generated trigger will not have its group,
     * or end-time set.  The Start time defaults to 'now'.
     * </p>
     * 
     * @param trigName the trigger's name 
     * @return the new trigger
     */
    public static Trigger makeMinutelyTrigger(String trigName) {
        return makeMinutelyTrigger(
                trigName, 1, SimpleTrigger.REPEAT_INDEFINITELY);
    }
    
    /**
     * <p>
     * Make a trigger that will fire every N minutes, indefinitely.
     * </p>
     * 
     * <p>
     * The generated trigger will not have its name, group,
     * or end-time set.  The Start time defaults to 'now'.
     * </p>
     * 
     * @param intervalInMinutes the number of minutes between firings
     * @return the new trigger
     */
    public static Trigger makeMinutelyTrigger(int intervalInMinutes) {
        return makeMinutelyTrigger(
                intervalInMinutes, SimpleTrigger.REPEAT_INDEFINITELY);
    }

    /**
     * <p>
     * Make a trigger that will fire every N minutes, with the given number of
     * repeats.
     * </p>
     * 
     * <p>
     * The generated trigger will not have its name, group,
     * or end-time set.  The Start time defaults to 'now'.
     * </p>
     * 
     * @param intervalInMinutes the number of minutes between firings
     * @param repeatCount the number of times to repeat the firing
     * @return the new trigger
     */
    public static Trigger makeMinutelyTrigger(
            int intervalInMinutes, int repeatCount) {
        SimpleTrigger trig = new SimpleTrigger();

        trig.setRepeatInterval(intervalInMinutes * MILLISECONDS_IN_MINUTE);
        trig.setRepeatCount(repeatCount);

        trig.setStartTime(new Date());
        
        return trig;
    }

    /**
     * <p>
     * Make a trigger that will fire every N minutes, with the given number of
     * repeats.
     * </p>
     * 
     * <p>
     * The generated trigger will not have its group,
     * or end-time set.  The Start time defaults to 'now'.
     * </p>
     * 
     * @param trigName the trigger's name
     * @param intervalInMinutes the number of minutes between firings
     * @param repeatCount the number of times to repeat the firing
     * @return the new trigger
     */
    public static Trigger makeMinutelyTrigger(
            String trigName, int intervalInMinutes, int repeatCount) {
        Trigger trig = makeMinutelyTrigger(intervalInMinutes, repeatCount);
        trig.setName(trigName);
        return trig;
    }

    /**
     * <p>
     * Make a trigger that will fire every hour, indefinitely.
     * </p>
     * 
     * <p>
     * The generated trigger will not have its name, group,
     * or end-time set.  The Start time defaults to 'now'.
     * </p>
     *  
     * @return the new trigger
     */
    public static Trigger makeHourlyTrigger() {
        return makeHourlyTrigger(1, SimpleTrigger.REPEAT_INDEFINITELY);
    }

    /**
     * <p>
     * Make a trigger that will fire every hour, indefinitely.
     * </p>
     * 
     * <p>
     * The generated trigger will not have its group,
     * or end-time set.  The Start time defaults to 'now'.
     * </p>
     * 
     * @param trigName the trigger's name
     * @return the new trigger
     */
    public static Trigger makeHourlyTrigger(String trigName) {
        return makeHourlyTrigger(
                trigName, 1, SimpleTrigger.REPEAT_INDEFINITELY);
    }

    /**
     * <p>
     * Make a trigger that will fire every N hours, indefinitely.
     * </p>
     * 
     * <p>
     * The generated trigger will not have its name, group,
     * or end-time set.  The Start time defaults to 'now'.
     * </p>
     * 
     * @param intervalInHours the number of hours between firings
     * @return the new trigger
     */
    public static Trigger makeHourlyTrigger(int intervalInHours) {
        return makeHourlyTrigger(
                intervalInHours, SimpleTrigger.REPEAT_INDEFINITELY);
    }

    /**
     * <p>
     * Make a trigger that will fire every N hours, with the given number of
     * repeats.
     * </p>
     * 
     * <p>
     * The generated trigger will not have its name, group,
     * or end-time set.  The Start time defaults to 'now'.
     * </p>
     * 
     * @param intervalInHours the number of hours between firings
     * @param repeatCount the number of times to repeat the firing
     * @return the new trigger
     */
    public static Trigger makeHourlyTrigger(
            int intervalInHours, int repeatCount) {
        SimpleTrigger trig = new SimpleTrigger();

        trig.setRepeatInterval(intervalInHours * MILLISECONDS_IN_HOUR);
        trig.setRepeatCount(repeatCount);

        trig.setStartTime(new Date());
        
        return trig;
    }

    /**
     * <p>
     * Make a trigger that will fire every N hours, with the given number of
     * repeats.
     * </p>
     * 
     * <p>
     * The generated trigger will not have its group,
     * or end-time set.  The Start time defaults to 'now'.
     * </p>
     * 
     * @param trigName the trigger's name
     * @param intervalInHours the number of hours between firings
     * @param repeatCount the number of times to repeat the firing
     * @return the new trigger
     */
    public static Trigger makeHourlyTrigger(
            String trigName, int intervalInHours, int repeatCount) {
        Trigger trig =makeHourlyTrigger(intervalInHours, repeatCount);
        trig.setName(trigName);
        return trig;
    }

    /**
     * <p>
     * Returns a date that is rounded to the next even hour above the given
     * date.
     * </p>
     * 
     * <p>
     * For example an input date with a time of 08:13:54 would result in a date
     * with the time of 09:00:00. If the date's time is in the 23rd hour, the
     * date's 'day' will be promoted, and the time will be set to 00:00:00.
     * </p>
     * 
     * @param date
     *          the Date to round, if <code>null</code> the current time will
     *          be used
     * @return the new rounded date
     */
    public static Date getEvenHourDate(Date date) {
        if (date == null) date = new Date();

        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.setLenient(true);

        c.set(Calendar.HOUR_OF_DAY, c.get(Calendar.HOUR_OF_DAY) + 1);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);

        return c.getTime();
    }

    /**
     * <p>
     * Returns a date that is rounded to the previous even hour below the given
     * date.
     * </p>
     * 
     * <p>
     * For example an input date with a time of 08:13:54 would result in a date
     * with the time of 08:00:00.
     * </p>
     * 
     * @param date
     *          the Date to round, if <code>null</code> the current time will
     *          be used
     * @return the new rounded date
     */
    public static Date getEvenHourDateBefore(Date date) {
        if (date == null) date = new Date();

        Calendar c = Calendar.getInstance();
        c.setTime(date);

        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);

        return c.getTime();
    }

    /**
     * <p>
     * Returns a date that is rounded to the next even minute above the given
     * date.
     * </p>
     * 
     * <p>
     * For example an input date with a time of 08:13:54 would result in a date
     * with the time of 08:14:00. If the date's time is in the 59th minute,
     * then the hour (and possibly the day) will be promoted.
     * </p>
     * 
     * @param date
     *          the Date to round, if <code>null</code> the current time will
     *          be used
     * @return the new rounded date
     */
    public static Date getEvenMinuteDate(Date date) {
        if (date == null) date = new Date();

        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.setLenient(true);

        c.set(Calendar.MINUTE, c.get(Calendar.MINUTE) + 1);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);

        return c.getTime();
    }

    /**
     * <p>
     * Returns a date that is rounded to the previous even minute below the 
     * given date.
     * </p>
     * 
     * <p>
     * For example an input date with a time of 08:13:54 would result in a date
     * with the time of 08:13:00.
     * </p>
     * 
     * @param date
     *          the Date to round, if <code>null</code> the current time will
     *          be used
     * @return the new rounded date
     */
    public static Date getEvenMinuteDateBefore(Date date) {
        if (date == null) date = new Date();

        Calendar c = Calendar.getInstance();
        c.setTime(date);

        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);

        return c.getTime();
    }

    /**
     * <p>
     * Returns a date that is rounded to the next even second above the given
     * date.
     * </p>
     * 
     * @param date
     *          the Date to round, if <code>null</code> the current time will
     *          be used
     * @return the new rounded date
     */
    public static Date getEvenSecondDate(Date date) {
        if (date == null) date = new Date();

        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.setLenient(true);

        c.set(Calendar.SECOND, c.get(Calendar.SECOND) + 1);
        c.set(Calendar.MILLISECOND, 0);

        return c.getTime();
    }

    /**
     * <p>
     * Returns a date that is rounded to the previous even second below the
     * given date.
     * </p>
     * 
     * <p>
     * For example an input date with a time of 08:13:54.341 would result in a
     * date with the time of 08:13:00.000.
     * </p>
     * 
     * @param date
     *          the Date to round, if <code>null</code> the current time will
     *          be used
     * @return the new rounded date
     */
    public static Date getEvenSecondDateBefore(Date date) {
        if (date == null) date = new Date();

        Calendar c = Calendar.getInstance();
        c.setTime(date);

        c.set(Calendar.MILLISECOND, 0);

        return c.getTime();
    }

    /**
     * <p>
     * Returns a date that is rounded to the next even multiple of the given
     * minute.
     * </p>
     * 
     * <p>
     * For example an input date with a time of 08:13:54, and an input
     * minute-base of 5 would result in a date with the time of 08:15:00. The
     * same input date with an input minute-base of 10 would result in a date
     * with the time of 08:20:00. But a date with the time 08:53:31 and an
     * input minute-base of 45 would result in 09:00:00, because the even-hour
     * is the next 'base' for 45-minute intervals.
     * </p>
     * 
     * <p>
     * More examples: <table>
     * <tr>
     * <th>Input Time</th>
     * <th>Minute-Base</th>
     * <th>Result Time</th>
     * </tr>
     * <tr>
     * <td>11:16:41</td>
     * <td>20</td>
     * <td>11:20:00</td>
     * </tr>
     * <tr>
     * <td>11:36:41</td>
     * <td>20</td>
     * <td>11:40:00</td>
     * </tr>
     * <tr>
     * <td>11:46:41</td>
     * <td>20</td>
     * <td>12:00:00</td>
     * </tr>
     * <tr>
     * <td>11:26:41</td>
     * <td>30</td>
     * <td>11:30:00</td>
     * </tr>
     * <tr>
     * <td>11:36:41</td>
     * <td>30</td>
     * <td>12:00:00</td>
     * </tr>
     * <td>11:16:41</td>
     * <td>17</td>
     * <td>11:17:00</td>
     * </tr>
     * </tr>
     * <td>11:17:41</td>
     * <td>17</td>
     * <td>11:34:00</td>
     * </tr>
     * </tr>
     * <td>11:52:41</td>
     * <td>17</td>
     * <td>12:00:00</td>
     * </tr>
     * </tr>
     * <td>11:52:41</td>
     * <td>5</td>
     * <td>11:55:00</td>
     * </tr>
     * </tr>
     * <td>11:57:41</td>
     * <td>5</td>
     * <td>12:00:00</td>
     * </tr>
     * </tr>
     * <td>11:17:41</td>
     * <td>0</td>
     * <td>12:00:00</td>
     * </tr>
     * </tr>
     * <td>11:17:41</td>
     * <td>1</td>
     * <td>11:08:00</td>
     * </tr>
     * </table>
     * </p>
     * 
     * @param date
     *          the Date to round, if <code>null</code> the current time will
     *          be used
     * @param minuteBase
     *          the base-minute to set the time on
     * @return the new rounded date
     * 
     * @see #getNextGivenSecondDate(Date, int)
     */
    public static Date getNextGivenMinuteDate(Date date, int minuteBase) {
        if (minuteBase < 0 || minuteBase > 59)
                throw new IllegalArgumentException(
                        "minuteBase must be >=0 and <= 59");

        if (date == null) date = new Date();

        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.setLenient(true);

        if (minuteBase == 0) {
            c.set(Calendar.HOUR_OF_DAY, c.get(Calendar.HOUR_OF_DAY) + 1);
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.SECOND, 0);
            c.set(Calendar.MILLISECOND, 0);

            return c.getTime();
        }

        int minute = c.get(Calendar.MINUTE);

        int arItr = minute / minuteBase;

        int nextMinuteOccurance = minuteBase * (arItr + 1);

        if (nextMinuteOccurance < 60) {
            c.set(Calendar.MINUTE, nextMinuteOccurance);
            c.set(Calendar.SECOND, 0);
            c.set(Calendar.MILLISECOND, 0);

            return c.getTime();
        } else {
            c.set(Calendar.HOUR_OF_DAY, c.get(Calendar.HOUR_OF_DAY) + 1);
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.SECOND, 0);
            c.set(Calendar.MILLISECOND, 0);

            return c.getTime();
        }
    }

    /**
     * <p>
     * Returns a date that is rounded to the next even multiple of the given
     * minute.
     * </p>
     * 
     * <p>
     * The rules for calculating the second are the same as those for
     * calculating the minute in the method 
     * <code>getNextGivenMinuteDate(..)<code>.
     * </p>
     *
     * @param date the Date to round, if <code>null</code> the current time will
     * be used
     * @param secondBase the base-second to set the time on
     * @return the new rounded date
     * 
     * @see #getNextGivenMinuteDate(Date, int)
     */
    public static Date getNextGivenSecondDate(Date date, int secondBase) {
        if (secondBase < 0 || secondBase > 59)
                throw new IllegalArgumentException(
                        "secondBase must be >=0 and <= 59");

        if (date == null) date = new Date();

        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.setLenient(true);

        if (secondBase == 0) {
            c.set(Calendar.MINUTE, c.get(Calendar.MINUTE) + 1);
            c.set(Calendar.SECOND, 0);
            c.set(Calendar.MILLISECOND, 0);

            return c.getTime();
        }

        int second = c.get(Calendar.SECOND);

        int arItr = second / secondBase;

        int nextSecondOccurance = secondBase * (arItr + 1);

        if (nextSecondOccurance < 60) {
            c.set(Calendar.SECOND, nextSecondOccurance);
            c.set(Calendar.MILLISECOND, 0);

            return c.getTime();
        } else {
            c.set(Calendar.MINUTE, c.get(Calendar.MINUTE) + 1);
            c.set(Calendar.SECOND, 0);
            c.set(Calendar.MILLISECOND, 0);

            return c.getTime();
        }
    }

    /**
     * <p>
     * Get a <code>Date</code> object that represents the given time, on
     * today's date.
     * </p>
     * 
     * @param second
     *          The value (0-59) to give the seconds field of the date
     * @param minute
     *          The value (0-59) to give the minutes field of the date
     * @param hour
     *          The value (0-23) to give the hours field of the date
     * @return the new date
     */
    public static Date getDateOf(int second, int minute, int hour) {
        validateSecond(second);
        validateMinute(minute);
        validateHour(hour);

        Date date = new Date();

        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.setLenient(true);

        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, second);
        c.set(Calendar.MILLISECOND, 0);

        return c.getTime();
    }

    /**
     * <p>
     * Get a <code>Date</code> object that represents the given time, on the
     * given date.
     * </p>
     * 
     * @param second
     *          The value (0-59) to give the seconds field of the date
     * @param minute
     *          The value (0-59) to give the minutes field of the date
     * @param hour
     *          The value (0-23) to give the hours field of the date
     * @param dayOfMonth
     *          The value (1-31) to give the day of month field of the date
     * @param month
     *          The value (1-12) to give the month field of the date
     * @return the new date
     */
    public static Date getDateOf(int second, int minute, int hour,
            int dayOfMonth, int month) {
        validateSecond(second);
        validateMinute(minute);
        validateHour(hour);
        validateDayOfMonth(dayOfMonth);
        validateMonth(month);

        Date date = new Date();

        Calendar c = Calendar.getInstance();
        c.setTime(date);

        c.set(Calendar.MONTH, month - 1);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, second);
        c.set(Calendar.MILLISECOND, 0);

        return c.getTime();
    }

    /**
     * <p>
     * Get a <code>Date</code> object that represents the given time, on the
     * given date.
     * </p>
     * 
     * @param second
     *          The value (0-59) to give the seconds field of the date
     * @param minute
     *          The value (0-59) to give the minutes field of the date
     * @param hour
     *          The value (0-23) to give the hours field of the date
     * @param dayOfMonth
     *          The value (1-31) to give the day of month field of the date
     * @param month
     *          The value (1-12) to give the month field of the date
     * @param year
     *          The value (1970-2099) to give the year field of the date
     * @return the new date
     */
    public static Date getDateOf(int second, int minute, int hour,
            int dayOfMonth, int month, int year) {
        validateSecond(second);
        validateMinute(minute);
        validateHour(hour);
        validateDayOfMonth(dayOfMonth);
        validateMonth(month);
        validateYear(year);

        Date date = new Date();

        Calendar c = Calendar.getInstance();
        c.setTime(date);

        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month - 1);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, second);
        c.set(Calendar.MILLISECOND, 0);

        return c.getTime();
    }

    /**
     * Returns a list of Dates that are the next fire times of a 
     * <code>Trigger</code>.
     * The input trigger will be cloned before any work is done, so you need
     * not worry about its state being altered by this method.
     * 
     * @param trigg
     *          The trigger upon which to do the work
     * @param cal
     *          The calendar to apply to the trigger's schedule
     * @param numTimes
     *          The number of next fire times to produce
     * @return List of java.util.Date objects
     */
    public static List computeFireTimes(Trigger trigg, org.quartz.Calendar cal,
            int numTimes) {
        LinkedList lst = new LinkedList();

        Trigger t = (Trigger) trigg.clone();

        if (t.getNextFireTime() == null) {
            t.computeFirstFireTime(cal);
        }

        for (int i = 0; i < numTimes; i++) {
            Date d = t.getNextFireTime();
            if (d != null) {
                lst.add(d);
                t.triggered(cal);
            } else
                break;
        }

        return java.util.Collections.unmodifiableList(lst);
    }

    /**
     * Returns a list of Dates that are the next fire times of a 
     * <code>Trigger</code>
     * that fall within the given date range. The input trigger will be cloned
     * before any work is done, so you need not worry about its state being
     * altered by this method.
     * 
     * <p>
     * NOTE: if this is a trigger that has previously fired within the given
     * date range, then firings which have already occured will not be listed
     * in the output List.
     * </p>
     * 
     * @param trigg
     *          The trigger upon which to do the work
     * @param cal
     *          The calendar to apply to the trigger's schedule
     * @param from
     *          The starting date at which to find fire times
     * @param to
     *          The ending date at which to stop finding fire times
     * @return List of java.util.Date objects
     */
    public static List computeFireTimesBetween(Trigger trigg,
            org.quartz.Calendar cal, Date from, Date to) {
        LinkedList lst = new LinkedList();

        Trigger t = (Trigger) trigg.clone();

        if (t.getNextFireTime() == null) {
            t.setStartTime(from);
            t.setEndTime(to);
            t.computeFirstFireTime(cal);
        }

        // TODO: this method could be more efficient by using logic specific
        //        to the type of trigger ...
        while (true) {
            Date d = t.getNextFireTime();
            if (d != null) {
                if (d.before(from)) {
                    t.triggered(cal);
                    continue;
                }
                if (d.after(to)) break;
                lst.add(d);
                t.triggered(cal);
            } else
                break;
        }

        return java.util.Collections.unmodifiableList(lst);
    }

    /**
     * Translate a date & time from a users timezone to the another
     * (probably server) timezone to assist in creating a simple trigger with 
     * the right date & time.
     * 
     * @param date the date to translate
     * @param src the original time-zone
     * @param dest the destination time-zone
     * @return the translated date
     */
    public static Date translateTime(Date date, TimeZone src, TimeZone dest) {

        Date newDate = new Date();

        int offset = (
                getOffset(date.getTime(), dest) - getOffset(date.getTime(), src)
        );

        newDate.setTime(date.getTime() - offset);

        return newDate;
    }
    
    /**
     * Gets the offset from UT for the given date in the given timezone, 
     * taking into account daylight savings.
     * 
     * <p>
     * Equivalent of TimeZone.getOffset(date) in JDK 1.4, but Quartz is trying
     * to support JDK 1.3.
     * </p>
     * 
     * @param date the date (in milliseconds) that is the base for the offset
     * @param tz the time-zone to calculate to offset to
     * @return the offset
     */
    public static int getOffset(long date, TimeZone tz) {
        
        if (tz.inDaylightTime(new Date(date))) {
            return tz.getRawOffset() + getDSTSavings(tz);
        }
        
        return tz.getRawOffset();
    }

    /**
     * <p>
     * Equivalent of TimeZone.getDSTSavings() in JDK 1.4, but Quartz is trying
     * to support JDK 1.3.
     * </p>
     * 
     * @param tz the target time-zone
     * @return the amount of saving time in milliseconds
     */
    public static int getDSTSavings(TimeZone tz) {
        
        if (tz.useDaylightTime()) {
            return 3600000;
        }
        return 0;
    }
}
