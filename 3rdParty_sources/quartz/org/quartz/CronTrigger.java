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

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;


/**
 * <p>
 * A concrete <code>{@link Trigger}</code> that is used to fire a <code>{@link org.quartz.JobDetail}</code>
 * at given moments in time, defined with Unix 'cron-like' definitions.
 * </p>
 * 
 * <p>
 * For those unfamiliar with "cron", this means being able to create a firing
 * schedule such as: "At 8:00am every Monday through Friday" or "At 1:30am
 * every last Friday of the month".
 * </p>
 * 
 * <p>
 * The format of a "Cron-Expression" string is documented on the 
 * {@link org.quartz.CronExpression} class.
 * </p>
 * 
 * <p>
 * Here are some full examples: <br><table cellspacing="8">
 * <tr>
 * <th align="left">Expression</th>
 * <th align="left">&nbsp;</th>
 * <th align="left">Meaning</th>
 * </tr>
 * <tr>
 * <td align="left"><code>"0 0 12 * * ?"</code></td>
 * <td align="left">&nbsp;</th>
 * <td align="left"><code>Fire at 12pm (noon) every day</code></td>
 * </tr>
 * <tr>
 * <td align="left"><code>"0 15 10 ? * *"</code></td>
 * <td align="left">&nbsp;</th>
 * <td align="left"><code>Fire at 10:15am every day</code></td>
 * </tr>
 * <tr>
 * <td align="left"><code>"0 15 10 * * ?"</code></td>
 * <td align="left">&nbsp;</th>
 * <td align="left"><code>Fire at 10:15am every day</code></td>
 * </tr>
 * <tr>
 * <td align="left"><code>"0 15 10 * * ? *"</code></td>
 * <td align="left">&nbsp;</th>
 * <td align="left"><code>Fire at 10:15am every day</code></td>
 * </tr>
 * <tr>
 * <td align="left"><code>"0 15 10 * * ? 2005"</code></td>
 * <td align="left">&nbsp;</th>
 * <td align="left"><code>Fire at 10:15am every day during the year 2005</code>
 * </td>
 * </tr>
 * <tr>
 * <td align="left"><code>"0 * 14 * * ?"</code></td>
 * <td align="left">&nbsp;</th>
 * <td align="left"><code>Fire every minute starting at 2pm and ending at 2:59pm, every day</code>
 * </td>
 * </tr>
 * <tr>
 * <td align="left"><code>"0 0/5 14 * * ?"</code></td>
 * <td align="left">&nbsp;</th>
 * <td align="left"><code>Fire every 5 minutes starting at 2pm and ending at 2:55pm, every day</code>
 * </td>
 * </tr>
 * <tr>
 * <td align="left"><code>"0 0/5 14,18 * * ?"</code></td>
 * <td align="left">&nbsp;</th>
 * <td align="left"><code>Fire every 5 minutes starting at 2pm and ending at 2:55pm, AND fire every 5 minutes starting at 6pm and ending at 6:55pm, every day</code>
 * </td>
 * </tr>
 * <tr>
 * <td align="left"><code>"0 0-5 14 * * ?"</code></td>
 * <td align="left">&nbsp;</th>
 * <td align="left"><code>Fire every minute starting at 2pm and ending at 2:05pm, every day</code>
 * </td>
 * </tr>
 * <tr>
 * <td align="left"><code>"0 10,44 14 ? 3 WED"</code></td>
 * <td align="left">&nbsp;</th>
 * <td align="left"><code>Fire at 2:10pm and at 2:44pm every Wednesday in the month of March.</code>
 * </td>
 * </tr>
 * <tr>
 * <td align="left"><code>"0 15 10 ? * MON-FRI"</code></td>
 * <td align="left">&nbsp;</th>
 * <td align="left"><code>Fire at 10:15am every Monday, Tuesday, Wednesday, Thursday and Friday</code>
 * </td>
 * </tr>
 * <tr>
 * <td align="left"><code>"0 15 10 15 * ?"</code></td>
 * <td align="left">&nbsp;</th>
 * <td align="left"><code>Fire at 10:15am on the 15th day of every month</code>
 * </td>
 * </tr>
 * <tr>
 * <td align="left"><code>"0 15 10 L * ?"</code></td>
 * <td align="left">&nbsp;</th>
 * <td align="left"><code>Fire at 10:15am on the last day of every month</code>
 * </td>
 * </tr>
 * <tr>
 * <td align="left"><code>"0 15 10 ? * 6L"</code></td>
 * <td align="left">&nbsp;</th>
 * <td align="left"><code>Fire at 10:15am on the last Friday of every month</code>
 * </td>
 * </tr>
 * <tr>
 * <td align="left"><code>"0 15 10 ? * 6L"</code></td>
 * <td align="left">&nbsp;</th>
 * <td align="left"><code>Fire at 10:15am on the last Friday of every month</code>
 * </td>
 * </tr>
 * <tr>
 * <td align="left"><code>"0 15 10 ? * 6L 2002-2005"</code></td>
 * <td align="left">&nbsp;</th>
 * <td align="left"><code>Fire at 10:15am on every last friday of every month during the years 2002, 2003, 2004 and 2005</code>
 * </td>
 * </tr>
 * <tr>
 * <td align="left"><code>"0 15 10 ? * 6#3"</code></td>
 * <td align="left">&nbsp;</th>
 * <td align="left"><code>Fire at 10:15am on the third Friday of every month</code>
 * </td>
 * </tr>
 * </table>
 * </p>
 * 
 * <p>
 * Pay attention to the effects of '?' and '*' in the day-of-week and
 * day-of-month fields!
 * </p>
 * 
 * <p>
 * <b>NOTES:</b>
 * <ul>
 * <li>Support for specifying both a day-of-week and a day-of-month value is
 * not complete (you'll need to use the '?' character in on of these fields).
 * </li>
 * <li>Be careful when setting fire times between mid-night and 1:00 AM -
 * "daylight savings" can cause a skip or a repeat depending on whether the
 * time moves back or jumps forward.</li>
 * </ul>
 * </p>
 * 
 * @see Trigger
 * @see SimpleTrigger
 * @see TriggerUtils
 * 
 * @author Sharada Jambula, James House
 * @author Contributions from Mads Henderson
 */
public class CronTrigger extends Trigger {

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * 
     * Constants.
     * 
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    /**
     * <p>
     * Instructs the <code>{@link Scheduler}</code> that upon a mis-fire
     * situation, the <code>{@link CronTrigger}</code> wants to be fired now
     * by <code>Scheduler</code>.
     * </p>
     */
    public static final int MISFIRE_INSTRUCTION_FIRE_ONCE_NOW = 1;

    /**
     * <p>
     * Instructs the <code>{@link Scheduler}</code> that upon a mis-fire
     * situation, the <code>{@link CronTrigger}</code> wants to have it's
     * next-fire-time updated to the next time in the schedule after the
     * current time (taking into account any associated <code>{@link Calendar}</code>,
     * but it does not want to be fired now.
     * </p>
     */
    public static final int MISFIRE_INSTRUCTION_DO_NOTHING = 2;

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * 
     * Data members.
     * 
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    private CronExpression cronEx = null;
    private Date startTime = null;
    private Date endTime = null;
    private Date nextFireTime = null;
    private Date previousFireTime = null;
    private transient TimeZone timeZone = null;

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * 
     * Constructors.
     * 
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    /**
     * <p>
     * Create a <code>CronTrigger</code> with no settings.
     * </p>
     * 
     * <p>
     * The start-time will also be set to the current time, and the time zone
     * will be set the the system's default time zone.
     * </p>
     */
    public CronTrigger() {
        super();
        setStartTime(new Date());
        setTimeZone(TimeZone.getDefault());
    }

    /**
     * <p>
     * Create a <code>CronTrigger</code> with the given name and group.
     * </p>
     * 
     * <p>
     * The start-time will also be set to the current time, and the time zone
     * will be set the the system's default time zone.
     * </p>
     */
    public CronTrigger(String name, String group) {
        super(name, group);
        setStartTime(new Date());
        setTimeZone(TimeZone.getDefault());
    }

    /**
     * <p>
     * Create a <code>CronTrigger</code> with the given name, group and
     * expression.
     * </p>
     * 
     * <p>
     * The start-time will also be set to the current time, and the time zone
     * will be set the the system's default time zone.
     * </p>
     */
    public CronTrigger(String name, String group, String cronExpression)
            throws ParseException {
        super(name, group);

        setCronExpression(cronExpression);

        setStartTime(new Date());
        setTimeZone(TimeZone.getDefault());
    }

    /**
     * <p>
     * Create a <code>CronTrigger</code> with the given name and group, and
     * associated with the identified <code>{@link org.quartz.JobDetail}</code>.
     * </p>
     * 
     * <p>
     * The start-time will also be set to the current time, and the time zone
     * will be set the the system's default time zone.
     * </p>
     */
    public CronTrigger(String name, String group, String jobName,
            String jobGroup) {
        super(name, group, jobName, jobGroup);
        setStartTime(new Date());
        setTimeZone(TimeZone.getDefault());
    }

    /**
     * <p>
     * Create a <code>CronTrigger</code> with the given name and group,
     * associated with the identified <code>{@link org.quartz.JobDetail}</code>,
     * and with the given "cron" expression.
     * </p>
     * 
     * <p>
     * The start-time will also be set to the current time, and the time zone
     * will be set the the system's default time zone.
     * </p>
     */
    public CronTrigger(String name, String group, String jobName,
            String jobGroup, String cronExpression) throws ParseException {
        this(name, group, jobName, jobGroup, null, null, cronExpression,
                TimeZone.getDefault());
    }

    /**
     * <p>
     * Create a <code>CronTrigger</code> with the given name and group,
     * associated with the identified <code>{@link org.quartz.JobDetail}</code>,
     * and with the given "cron" expression resolved with respect to the <code>TimeZone</code>.
     * </p>
     */
    public CronTrigger(String name, String group, String jobName,
            String jobGroup, String cronExpression, TimeZone timeZone)
            throws ParseException {
        this(name, group, jobName, jobGroup, null, null, cronExpression,
                timeZone);
    }

    /**
     * <p>
     * Create a <code>CronTrigger</code> that will occur at the given time,
     * until the given end time.
     * </p>
     * 
     * <p>
     * If null, the start-time will also be set to the current time, the time
     * zone will be set the the system's default.
     * </p>
     * 
     * @param startTime
     *          A <code>Date</code> set to the time for the <code>Trigger</code>
     *          to fire.
     * @param endTime
     *          A <code>Date</code> set to the time for the <code>Trigger</code>
     *          to quit repeat firing.
     */
    public CronTrigger(String name, String group, String jobName,
            String jobGroup, Date startTime, Date endTime, String cronExpression)
            throws ParseException {
        super(name, group, jobName, jobGroup);

        setCronExpression(cronExpression);

        if (startTime == null) startTime = new Date();
        setStartTime(startTime);
        if (endTime != null) setEndTime(endTime);
        setTimeZone(TimeZone.getDefault());

    }

    /**
     * <p>
     * Create a <code>CronTrigger</code> with fire time dictated by the
     * <code>cronExpression</code> resolved with respect to the specified
     * <code>timeZone</code> occuring from the <code>startTime</code> until
     * the given <code>endTime</code>.
     * </p>
     * 
     * <p>
     * If null, the start-time will also be set to the current time. If null,
     * the time zone will be set to the system's default.
     * </p>
     * 
     * @param name
     *          of the <code>Trigger</code>
     * @param group
     *          of the <code>Trigger</code>
     * @param jobName,
     *          name of the <code>{@link org.quartz.JobDetail}</code>
     *          executed on firetime
     * @param jobGroup,
     *          group of the <code>{@link org.quartz.JobDetail}</code>
     *          executed on firetime
     * @param startTime
     *          A <code>Date</code> set to the earliest time for the <code>Trigger</code>
     *          to start firing.
     * @param endTime
     *          A <code>Date</code> set to the time for the <code>Trigger</code>
     *          to quit repeat firing.
     * @param cronExpression,
     *          A cron expression dictating the firing sequence of the <code>Trigger</code>
     * @param timeZone,
     *          Specifies for which time zone the <code>cronExpression</code>
     *          should be interprted, i.e. the expression 0 0 10 * * ?, is
     *          resolved to 10:00 am in this time zone.
     * @throws ParseException
     *           if the <code>cronExpression</code> is invalid.
     */
    public CronTrigger(String name, String group, String jobName,
            String jobGroup, Date startTime, Date endTime,
            String cronExpression, TimeZone timeZone) throws ParseException {
        super(name, group, jobName, jobGroup);

        setCronExpression(cronExpression);

        if (startTime == null) startTime = new Date();
        setStartTime(startTime);
        if (endTime != null) setEndTime(endTime);
        if (timeZone == null) {
            setTimeZone(TimeZone.getDefault());
        } else {
            setTimeZone(timeZone);
        }
    }

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * 
     * Interface.
     * 
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */
    
    public Object clone() {
    	CronTrigger copy = (CronTrigger) super.clone();
        copy.setCronExpression((CronExpression)cronEx.clone());
        return copy;
    }

    public void setCronExpression(String cronExpression) throws ParseException {
        this.cronEx = new CronExpression(cronExpression);
        this.cronEx.setTimeZone(getTimeZone());
    }

    public String getCronExpression() {
        return cronEx == null ? null : cronEx.getCronExpression();
    }

    public void setCronExpression(CronExpression cronExpression) {
    	this.cronEx = cronExpression;
    	this.timeZone = cronExpression.getTimeZone();
    }
    
    /**
     * <p>
     * Get the time at which the <code>CronTrigger</code> should occur.
     * </p>
     */
    public Date getStartTime() {
        return this.startTime;
    }

    public void setStartTime(Date startTime) {
        if (startTime == null)
                throw new IllegalArgumentException("Start time cannot be null");

        Date eTime = getEndTime();
        if (eTime != null && startTime != null && eTime.before(startTime))
            throw new IllegalArgumentException(
            "End time cannot be before start time");
        
        // round off millisecond...
        // Note timeZone is not needed here as parameter for
        // Calendar.getInstance(),
        // since time zone is implicit when using a Date in the setTime method.
        Calendar cl = Calendar.getInstance();
        cl.setTime(startTime);
        cl.set(Calendar.MILLISECOND, 0);

        this.startTime = cl.getTime();
    }

    /**
     * <p>
     * Get the time at which the <code>CronTrigger</code> should quit
     * repeating - even if repeastCount isn't yet satisfied.
     * </p>
     * 
     * @see #getFinalFireTime()
     */
    public Date getEndTime() {
        return this.endTime;
    }

    public void setEndTime(Date endTime) {
        Date sTime = getStartTime();
        if (sTime != null && endTime != null && sTime.after(endTime))
                throw new IllegalArgumentException(
                        "End time cannot be before start time");

        this.endTime = endTime;
    }

    /**
     * <p>
     * Returns the next time at which the <code>CronTrigger</code> will fire.
     * If the trigger will not fire again, <code>null</code> will be
     * returned. The value returned is not guaranteed to be valid until after
     * the <code>Trigger</code> has been added to the scheduler.
     * </p>
     */
    public Date getNextFireTime() {
        return this.nextFireTime;
    }

    /**
     * <p>
     * Returns the previous time at which the <code>CronTrigger</code> will
     * fire. If the trigger has not yet fired, <code>null</code> will be
     * returned.
     */
    public Date getPreviousFireTime() {
        return this.previousFireTime;
    }

    /**
     * <p>
     * Sets the next time at which the <code>CronTrigger</code> will fire.
     * <b>This method should not be invoked by client code.</b>
     * </p>
     */
    public void setNextFireTime(Date nextFireTime) {
        this.nextFireTime = nextFireTime;
    }

    /**
     * <p>
     * Set the previous time at which the <code>SimpleTrigger</code> fired.
     * </p>
     * 
     * <p>
     * <b>This method should not be invoked by client code.</b>
     * </p>
     */
    public void setPreviousFireTime(Date previousFireTime) {
        this.previousFireTime = previousFireTime;
    }

    /**
     * <p>
     * Returns the time zone for which the <code>cronExpression</code> of
     * this <code>CronTrigger</code> will be resolved.
     * </p>
     */
    public TimeZone getTimeZone() {
    	
    	if(cronEx != null) return cronEx.getTimeZone();
    	
        if (timeZone == null) timeZone = TimeZone.getDefault();
        return timeZone;
    }

    /**
     * <p>
     * Sets the time zone for which the <code>cronExpression</code> of this
     * <code>CronTrigger</code> will be resolved.
     * </p>
     */
    public void setTimeZone(TimeZone timeZone) {
    	if(cronEx != null) cronEx.setTimeZone(timeZone);
    	this.timeZone = timeZone;
    }

    /**
     * <p>
     * Returns the next time at which the <code>CronTrigger</code> will fire,
     * after the given time. If the trigger will not fire after the given time,
     * <code>null</code> will be returned.
     * </p>
     * 
     * <p>
     * Note that the date returned is NOT validated against the related
     * org.quartz.Calendar (if any)
     * </p>
     */
    public Date getFireTimeAfter(Date afterTime) {
        if (afterTime == null) afterTime = new Date();

        if (startTime.after(afterTime))
                afterTime = new Date(startTime.getTime() - 1000l);

        Date pot = getTimeAfter(afterTime);
        if (endTime != null && pot != null && pot.after(endTime)) return null;

        return pot;
    }

    /**
     * <p>
     * Returns the final time at which the <code>CronTrigger</code> will
     * fire.
     * </p>
     * 
     * <p>
     * Note that the return time *may* be in the past. and the date returned is
     * not validated against org.quartz.calendar
     * </p>
     */
    public Date getFinalFireTime() {
        if (this.endTime != null) return getTimeBefore(this.endTime);
        else
            return null;
    }

    /**
     * <p>
     * Determines whether or not the <code>CronTrigger</code> will occur
     * again.
     * </p>
     */
    public boolean mayFireAgain() {
        return (getNextFireTime() != null);
    }

    protected boolean validateMisfireInstruction(int misfireInstruction) {
        if (misfireInstruction < MISFIRE_INSTRUCTION_SMART_POLICY)
                return false;

        if (misfireInstruction > MISFIRE_INSTRUCTION_DO_NOTHING) return false;

        return true;
    }

    /**
     * <p>
     * Updates the <code>CronTrigger</code>'s state based on the
     * MISFIRE_INSTRUCTION_XXX that was selected when the <code>SimpleTrigger</code>
     * was created.
     * </p>
     * 
     * <p>
     * If the misfire instruction is set to MISFIRE_INSTRUCTION_SMART_POLICY,
     * then the following scheme will be used: <br>
     * <ul>
     * <li>The instruction will be interpreted as <code>MISFIRE_INSTRUCTION_FIRE_ONCE_NOW</code>
     * </ul>
     * </p>
     */
    public void updateAfterMisfire(org.quartz.Calendar cal) {
        int instr = getMisfireInstruction();

        if (instr == MISFIRE_INSTRUCTION_SMART_POLICY)
                instr = MISFIRE_INSTRUCTION_FIRE_ONCE_NOW;

        if (instr == MISFIRE_INSTRUCTION_DO_NOTHING) {
            Date newFireTime = getFireTimeAfter(new Date());
            while (newFireTime != null && cal != null
                    && !cal.isTimeIncluded(newFireTime.getTime())) {
                newFireTime = getFireTimeAfter(newFireTime);
            }
            setNextFireTime(newFireTime);
        } else if (instr == MISFIRE_INSTRUCTION_FIRE_ONCE_NOW) {
            setNextFireTime(new Date());
        }
    }

    /**
     * <p>
     * Determines whether the date and (optionally) time of the given Calendar 
     * instance falls on a scheduled fire-time of this trigger.
     * </p>
     * 
     * <p>
     * Equivalent to calling <code>willFireOn(cal, false)</code>.
     * </p>
     * 
     * @param test the date to compare
     * 
     * @see #willFireOn(Calendar, boolean)
     */
    public boolean willFireOn(Calendar test) {
        return willFireOn(test, false);
    }
    
    /**
     * <p>
     * Determines whether the date and (optionally) time of the given Calendar 
     * instance falls on a scheduled fire-time of this trigger.
     * </p>
     * 
     * <p>
     * Note that the value returned is NOT validated against the related
     * org.quartz.Calendar (if any)
     * </p>
     * 
     * @param test the date to compare
     * @param dayOnly if set to true, the method will only determine if the
     * trigger will fire during the day represented by the given Calendar
     * (hours, minutes and seconds will be ignored).
     * @see #willFireOn(Calendar)
     */
    public boolean willFireOn(Calendar test, boolean dayOnly) {

    	test = (Calendar) test.clone();
    	
        test.set(Calendar.MILLISECOND, 0); // don't compare millis.
        
        if(dayOnly) {
            test.set(Calendar.HOUR, 0); 
            test.set(Calendar.MINUTE, 0); 
            test.set(Calendar.SECOND, 0); 
        }
        
        Date testTime = test.getTime();
        
        Date fta = getFireTimeAfter(new Date(test.getTime().getTime() - 1000));

        Calendar p = Calendar.getInstance(test.getTimeZone());
        p.setTime(fta);
        
        int year = p.get(Calendar.YEAR);
        int month = p.get(Calendar.MONTH);
        int day = p.get(Calendar.DATE);
        
        if(dayOnly) {
            return (year == test.get(Calendar.YEAR) 
                    && month == test.get(Calendar.MONTH) 
                    && day == test.get(Calendar.DATE));
        }
        
        while(fta.before(testTime)) {
            fta = getFireTimeAfter(fta);
        }
        
        if(fta.equals(testTime))
            return true;

        return false;
    }

    /**
     * <p>
     * Called after the <code>{@link Scheduler}</code> has executed the
     * <code>{@link org.quartz.JobDetail}</code> associated with the <code>Trigger</code>
     * in order to get the final instruction code from the trigger.
     * </p>
     * 
     * @param context
     *          is the <code>JobExecutionContext</code> that was used by the
     *          <code>Job</code>'s<code>execute(xx)</code> method.
     * @param result
     *          is the <code>JobExecutionException</code> thrown by the
     *          <code>Job</code>, if any (may be null).
     * @return one of the Trigger.INSTRUCTION_XXX constants.
     * 
     * @see #INSTRUCTION_NOOP
     * @see #INSTRUCTION_RE_EXECUTE_JOB
     * @see #INSTRUCTION_DELETE_TRIGGER
     * @see #INSTRUCTION_SET_TRIGGER_COMPLETE
     * @see #triggered(Calendar)
     */
    public int executionComplete(JobExecutionContext context,
            JobExecutionException result) {
        if (result != null && result.refireImmediately())
                return INSTRUCTION_RE_EXECUTE_JOB;

        if (result != null && result.unscheduleFiringTrigger())
                return INSTRUCTION_SET_TRIGGER_COMPLETE;

        if (result != null && result.unscheduleAllTriggers())
                return INSTRUCTION_SET_ALL_JOB_TRIGGERS_COMPLETE;

        if (!mayFireAgain()) return INSTRUCTION_DELETE_TRIGGER;

        return INSTRUCTION_NOOP;
    }

    /**
     * <p>
     * Called when the <code>{@link Scheduler}</code> has decided to 'fire'
     * the trigger (execute the associated <code>Job</code>), in order to
     * give the <code>Trigger</code> a chance to update itself for its next
     * triggering (if any).
     * </p>
     * 
     * @see #executionComplete(JobExecutionContext, JobExecutionException)
     */
    public void triggered(org.quartz.Calendar calendar) {
        previousFireTime = nextFireTime;
        nextFireTime = getFireTimeAfter(nextFireTime);

        while (nextFireTime != null && calendar != null
                && !calendar.isTimeIncluded(nextFireTime.getTime())) {
            nextFireTime = getFireTimeAfter(nextFireTime);
        }
    }

    /**
     *  
     * @see org.quartz.Trigger#updateWithNewCalendar(org.quartz.Calendar, long)
     */
    public void updateWithNewCalendar(org.quartz.Calendar calendar, long misfireThreshold)
    {
        nextFireTime = getFireTimeAfter(previousFireTime);
        
        Date now = new Date();
        do {
            while (nextFireTime != null && calendar != null
                    && !calendar.isTimeIncluded(nextFireTime.getTime())) {
                nextFireTime = getFireTimeAfter(nextFireTime);
            }
            
            if(nextFireTime != null && nextFireTime.before(now)) {
                long diff = now.getTime() - nextFireTime.getTime();
                if(diff >= misfireThreshold) {
                    nextFireTime = getFireTimeAfter(nextFireTime);
                    continue;
                }
            }
        }while(false);
    }

    /**
     * <p>
     * Called by the scheduler at the time a <code>Trigger</code> is first
     * added to the scheduler, in order to have the <code>Trigger</code>
     * compute its first fire time, based on any associated calendar.
     * </p>
     * 
     * <p>
     * After this method has been called, <code>getNextFireTime()</code>
     * should return a valid answer.
     * </p>
     * 
     * @return the first time at which the <code>Trigger</code> will be fired
     *         by the scheduler, which is also the same value <code>getNextFireTime()</code>
     *         will return (until after the first firing of the <code>Trigger</code>).
     *         </p>
     */
    public Date computeFirstFireTime(org.quartz.Calendar calendar) {
        nextFireTime = getFireTimeAfter(new Date(startTime.getTime() - 1000l));

        while (nextFireTime != null && calendar != null
                && !calendar.isTimeIncluded(nextFireTime.getTime())) {
            nextFireTime = getFireTimeAfter(nextFireTime);
        }

        return nextFireTime;
    }

    public String getExpressionSummary() {
    	return cronEx == null ? null : cronEx.getExpressionSummary();
    }

    ////////////////////////////////////////////////////////////////////////////
    //
    // Computation Functions
    //
    ////////////////////////////////////////////////////////////////////////////

    protected Date getTimeAfter(Date afterTime) {
    	return cronEx.getTimeAfter(afterTime);
    }

    protected Date getTimeBefore(Date endTime) 
    {
        return null;
    }

    public static void main(String[] args) // TODO: remove method after good
            // unit testing
            throws Exception {

            String expr = "15 10 0/4 * * ?";
            if(args != null && args.length > 0 && args[0] != null)
              expr = args[0];
        
            CronTrigger ct = new CronTrigger("t", "g", "j", "g", new Date(), null, expr);
            ct.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
            System.err.println(ct.getExpressionSummary());
            System.err.println("tz=" + ct.getTimeZone().getID());
            System.err.println();
        
            java.util.List times = TriggerUtils.computeFireTimes(ct, null, 25);
        
            for (int i = 0; i < times.size(); i++) {
              System.err.println("firetime = " + times.get(i));
            }
            
            Calendar tt = Calendar.getInstance();
            tt.set(Calendar.DATE, 17);
            tt.set(Calendar.MONTH, 5 - 1);
            tt.set(Calendar.HOUR, 11);
            tt.set(Calendar.MINUTE, 0);
            tt.set(Calendar.SECOND, 7);
            
            System.err.println("\nWill fire on: " + tt.getTime() + " -- " + ct.willFireOn(tt, false));
            
          
//            CRON Expression: 0 0 9 * * ?
//
//                    TimeZone.getDefault().getDisplayName() = Central African Time
//                    TimeZone.getDefault().getID() = Africa/Harare            
        //
////        System.err.println();
////        System.err.println();
////        System.err.println();
////        System.err.println("Daylight test:");
////
////        CronTrigger trigger = new CronTrigger();
////
////        TimeZone timeZone = TimeZone.getTimeZone("America/Los_Angeles");
////        //    TimeZone timeZone = TimeZone.getDefault();
////
////        trigger.setTimeZone(timeZone);
////        trigger.setCronExpression("0 0 1 ? 4 *");
////
////        Date start = new Date(1056319200000L);
////        Date end = new Date(1087682399000L);
////
////        trigger.setStartTime(start);
////        trigger.setEndTime(end);
////
////        Date next = new Date(1056232800000L);
////        while (next != null) {
////            next = trigger.getFireTimeAfter(next);
////            if (next != null) {
////                Calendar cal = Calendar.getInstance();
////                cal.setTimeZone(timeZone);
////                cal.setTime(next);
////                System.err.println(cal.get(Calendar.MONTH) + "/"
////                        + cal.get(Calendar.DATE) + "/" + cal.get(Calendar.YEAR)
////                        + " - " + cal.get(Calendar.HOUR_OF_DAY) + ":"
////                        + cal.get(Calendar.MINUTE));
////            }
////        }
////
////        System.err.println();
////        System.err.println();
////        System.err.println();
////        System.err.println("Midnight test:");
////
////        trigger = new CronTrigger();
////
////        timeZone = TimeZone.getTimeZone("Asia/Jerusalem");
////        //    timeZone = TimeZone.getTimeZone("America/Los_Angeles");
////        //    TimeZone timeZone = TimeZone.getDefault();
////
////        trigger.setTimeZone(timeZone);
////        trigger.setCronExpression("0 /15 * ? 4 *");
////
////        start = new Date(1056319200000L);
////        end = new Date(1087682399000L);
////
////        trigger.setStartTime(start);
////        trigger.setEndTime(end);
////
////        next = new Date(1056232800000L);
////        while (next != null) {
////            next = trigger.getFireTimeAfter(next);
////            if (next != null) {
////                Calendar cal = Calendar.getInstance();
////                cal.setTimeZone(timeZone);
////                cal.setTime(next);
////                System.err.println(cal.get(Calendar.MONTH) + "/"
////                        + cal.get(Calendar.DATE) + "/" + cal.get(Calendar.YEAR)
////                        + " - " + cal.get(Calendar.HOUR_OF_DAY) + ":"
////                        + cal.get(Calendar.MINUTE));
////            }
////        }

    }
}

