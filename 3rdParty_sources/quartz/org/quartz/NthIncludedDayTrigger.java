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

package org.quartz;

import java.util.Date;

import org.quartz.Calendar;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Trigger;

/**
 * A trigger which fires on the N<SUP>th</SUP> day of every interval type 
 * ({@link #INTERVAL_TYPE_WEEKLY}, {@link #INTERVAL_TYPE_MONTHLY} or 
 * {@link #INTERVAL_TYPE_YEARLY}) that is <i>not</i> excluded by the associated
 * calendar. When determining what the N<SUP>th</SUP> day of the month or year
 * is, <CODE>NthIncludedDayTrigger</CODE> will skip excluded days on the 
 * associated calendar. This would commonly be used in an N<SUP>th</SUP> 
 * business day situation, in which the user wishes to fire a particular job on
 * the N<SUP>th</SUP> business day (i.e. the 5<SUP>th</SUP> business day of
 * every month). Each <CODE>NthIncludedDayTrigger</CODE> also has an associated
 * <CODE>fireAtTime</CODE> which indicates at what time of day the trigger is
 * to fire.
 * <P>
 * All <CODE>NthIncludedDayTrigger</CODE>s default to a monthly interval type
 * (fires on the N<SUP>th</SUP> day of every month) with N = 1 (first 
 * non-excluded day) and <CODE>fireAtTime</CODE> set to 12:00 PM (noon). These
 * values can be changed using the {@link #setN}, {@link #setIntervalType}, and
 * {@link #setFireAtTime} methods. Users may also want to note the 
 * {@link #setNextFireCutoffInterval} and {@link #getNextFireCutoffInterval}
 * methods.
 * <P>
 * Take, for example, the following calendar:
 * <P>
 * <PRE>
 *        July                  August                September
 * Su Mo Tu We Th Fr Sa   Su Mo Tu We Th Fr Sa   Su Mo Tu We Th Fr Sa
 *                 1  W       1  2  3  4  5  W                1  2  W
 *  W  H  5  6  7  8  W    W  8  9 10 11 12  W    W  H  6  7  8  9  W
 *  W 11 12 13 14 15  W    W 15 16 17 18 19  W    W 12 13 14 15 16  W
 *  W 18 19 20 21 22  W    W 22 23 24 25 26  W    W 19 20 21 22 23  W
 *  W 25 26 27 28 29  W    W 29 30 31             W 26 27 28 29 30
 *  W
 * </PRE>
 * <P>
 * Where W's represent weekend days, and H's represent holidays, all of which
 * are excluded on a calendar associated with an 
 * <CODE>NthIncludedDayTrigger</CODE> with <CODE>n=5</CODE> and 
 * <CODE>intervalType=INTERVAL_TYPE_MONTHLY</CODE>. In this case, the trigger 
 * would fire on the 8<SUP>th</SUP> of July (because of the July 4 holiday), 
 * the 5<SUP>th</SUP> of August, and the 8<SUP>th</SUP> of September (because 
 * of Labor Day).
 * 
 * @author  Aaron Craven
 */
public class NthIncludedDayTrigger extends Trigger {

    /**
     * Instructs the <CODE>Scheduler</CODE> that upon a mis-fire situation, the
     * <CODE>NthIncludedDayTrigger</CODE> wants to be fired now by the 
     * <CODE>Scheduler</CODE>
     */
    public static final int MISFIRE_INSTRUCTION_FIRE_ONCE_NOW = 1;

    /**
     * Instructs the <CODE>Scheduler</CODE> that upon a mis-fire situation, the
     * <CODE>NthIncludedDayTrigger</CODE> wants to have 
     * <CODE>nextFireTime</CODE> updated to the next time in the schedule after
     * the current time, but it does not want to be fired now.
     */
    public static final int MISFIRE_INSTRUCTION_DO_NOTHING = 2;

    /**
     * indicates a monthly trigger type (fires on the N<SUP>th</SUP> included
     * day of every month).
     */
    public static final int INTERVAL_TYPE_MONTHLY = 1;
    
    /**
     * indicates a yearly trigger type (fires on the N<SUP>th</SUP> included 
     * day of every year).
     */
    public static final int INTERVAL_TYPE_YEARLY = 2;
    
    /**
     * indicates a weekly trigger type (fires on the N<SUP>th</SUP> included
     * day of every week). When using this interval type, care must be taken
     * not to think of the value of <CODE>n</CODE> as an analog to 
     * <CODE>java.util.Calendar.DAY_OF_WEEK</CODE>. Such a comparison can only
     * be drawn when there are no calendars associated with the trigger. To 
     * illustrate, consider an <CODE>NthIncludedDayTrigger</CODE> with 
     * <CODE>n = 3</CODE> which is associated with a Calendar excluding
     * non-weekdays. The trigger would fire on the 3<SUP>rd</SUP> 
     * <I>included</I> day of the week, which would be 4<SUP>th</SUP> 
     * <I>actual</I> day of the week.
     */
    public static final int INTERVAL_TYPE_WEEKLY = 3;
    
    private Date startTime = new Date();
    private Date endTime;
    private Date previousFireTime;
    private Date nextFireTime;
    private Calendar calendar;
    
    private int n = 1;
    private int intervalType = INTERVAL_TYPE_MONTHLY;
    private int fireAtHour = 12;
    private int fireAtMinute = 0;
    private int nextFireCutoffInterval = 12;
    
    /**
     * Create an <CODE>NthIncludedDayTrigger</CODE> with no specified name,
     * group, or <CODE>JobDetail</CODE>. This will result initially in a
     * default monthly trigger that fires on the first day of every month at
     * 12:00 PM (<CODE>n</CODE>=1, 
     * <CODE>intervalType={@link #INTERVAL_TYPE_MONTHLY}</CODE>, 
     * <CODE>fireAtTime="12:00"</CODE>).
     * <P>
     * Note that <CODE>setName()</CODE>, <CODE>setGroup()</CODE>, 
     * <CODE>setJobName()</CODE>, and <CODE>setJobGroup()</CODE>, must be 
     * called before the <CODE>NthIncludedDayTrigger</CODE> can be placed into
     * a <CODE>Scheduler</CODE>.
     */
    public NthIncludedDayTrigger() {
        super();
    }

    /**
     * Create an <CODE>NthIncludedDayTrigger</CODE> with the given name and
     * group but no specified <CODE>JobDetail</CODE>. This will result 
     * initially in a default monthly trigger that fires on the first day of 
     * every month at 12:00 PM (<CODE>n</CODE>=1, 
     * <CODE>intervalType={@link #INTERVAL_TYPE_MONTHLY}</CODE>, 
     * <CODE>fireAtTime="12:00"</CODE>).
     * <P>
     * Note that <CODE>setJobName()</CODE> and <CODE>setJobGroup()</CODE> must
     * be called before the <CODE>NthIncludedDayTrigger</CODE> can be placed 
     * into a <CODE>Scheduler</CODE>.
     *  
     * @param name  the name for the <CODE>NthIncludedDayTrigger</CODE>
     * @param group the group for the <CODE>NthIncludedDayTrigger</CODE>
     */
    public NthIncludedDayTrigger(String name, String group) {
        super(name, group);
    }

    /**
     * Create an <CODE>NthIncludedDayTrigger</CODE> with the given name and
     * group and the specified <CODE>JobDetail</CODE>. This will result 
     * initially in a default monthly trigger that fires on the first day of
     * every month at 12:00 PM (<CODE>n</CODE>=1, 
     * <CODE>intervalType={@link #INTERVAL_TYPE_MONTHLY}</CODE>, 
     * <CODE>fireAtTime="12:00"</CODE>).
     * 
     * @param name     the name for the <CODE>NthIncludedDayTrigger</CODE>
     * @param group    the group for the <CODE>NthIncludedDayTrigger</CODE>
     * @param jobName  the name of the job to associate with the 
     *                 <CODE>NthIncludedDayTrigger</CODE>
     * @param jobGroup the group containing the job to associate with the 
     *                 <CODE>NthIncludedDayTrigger</CODE>
     */
    public NthIncludedDayTrigger(String name, String group, String jobName,
            String jobGroup) {
        super(name, group, jobName, jobGroup);
    }

    /**
     * Sets the day of the interval on which the 
     * <CODE>NthIncludedDayTrigger</CODE> should fire. If the N<SUP>th</SUP>
     * day of the interval does not exist (i.e. the 32<SUP>nd</SUP> of a 
     * month), the trigger simply will never fire. N may not be less than 1.
     * 
     * @param  n the day of the interval on which the trigger should fire.
     * @throws java.lang.IllegalArgumentException
     *         the value entered for N was not valid (probably less than or 
     *         equal to zero).
     * @see #getN()
     */
    public void setN(int n) {
        if (n > 0) {
            this.n = n;
        } else {
            throw new IllegalArgumentException("N must be greater than 0.");
        }
    }
    
    /**
     * Returns the day of the interval on which the 
     * <CODE>NthIncludedDayTrigger</CODE> should fire.
     * 
     * @return the value of <CODE>n</CODE>
     * @see #setN(int)
     */
    public int getN() {
        return this.n;
    }
    
    /**
     * Sets the interval type for the <CODE>NthIncludedDayTrigger</CODE>. If
     * {@link #INTERVAL_TYPE_MONTHLY}, the trigger will fire on the 
     * N<SUP>th</SUP> included day of every month. If 
     * {@link #INTERVAL_TYPE_YEARLY}, the trigger will fire on the 
     * N<SUP>th</SUP> included day of every year. If 
     * {@link #INTERVAL_TYPE_WEEKLY}, the trigger will fire on the 
     * N<SUP>th</SUP> included day of every week. 
     * 
     * @param  intervalType the interval type for the trigger
     * @throws java.lang.IllegalArgumentException
     *         the value of <CODE>intervalType</CODE> is not valid. Valid
     *         values are represented by the INTERVAL_TYPE_WEEKLY, 
     *         INTERVAL_TYPE_MONTHLY and INTERVAL_TYPE_YEARLY constants.
     * @see #getIntervalType()
     * @see #INTERVAL_TYPE_WEEKLY
     * @see #INTERVAL_TYPE_MONTHLY
     * @see #INTERVAL_TYPE_YEARLY
     */
    public void setIntervalType(int intervalType) {
        switch (intervalType) {
        case INTERVAL_TYPE_WEEKLY:
            this.intervalType = intervalType;
            break;
        case INTERVAL_TYPE_MONTHLY:
            this.intervalType = intervalType;
            break;
        case INTERVAL_TYPE_YEARLY:
            this.intervalType = intervalType;
            break;
        default:
            throw new IllegalArgumentException("Invalid Interval Type:" 
                                               + intervalType);
        }
    }
    
    /**
     * Returns the interval type for the <CODE>NthIncludedDayTrigger</CODE>.
     * 
     * @return the trigger's interval type
     * @see #setIntervalType(int)
     * @see #INTERVAL_TYPE_WEEKLY
     * @see #INTERVAL_TYPE_MONTHLY
     * @see #INTERVAL_TYPE_YEARLY
     */
    public int getIntervalType() {
        return this.intervalType;       
    }
    
    /**
     * Sets the fire time for the <CODE>NthIncludedDayTrigger</CODE>, which
     * should be represented as a string with the format &quot;HH:MM&quot;, 
     * with HH representing the 24-hour clock hour of the fire time. Hours can
     * be represented as either a one-digit or two-digit number.
     * 
     * @param  fireAtTime the time at which the trigger should fire
     * @throws java.lang.IllegalArgumentException
     *         the specified value for <CODE>fireAtTime</CODE> could not be 
     *         successfully parsed into a valid time of day.
     * @see #getFireAtTime()
     */
    public void setFireAtTime(String fireAtTime) {
        int fireHour = 12;
        int fireMinute = 0;
        String[] components;
        
        try {
            int i = fireAtTime.indexOf(":");
            fireHour = Integer.parseInt(fireAtTime.substring(0, i));
            fireMinute = Integer.parseInt(fireAtTime.substring(i+1));
        } catch (Exception e) {
            fireHour = 12;
            fireMinute = 0;
            throw new 
                IllegalArgumentException("Could not parse time expression: " 
                                         + e.getMessage());
        } finally {
            this.fireAtHour = fireHour;
            this.fireAtMinute = fireMinute;
        }
    }
    
    /**
     * Returns the fire time for the <CODE>NthIncludedDayTrigger</CODE> as a
     * string with the format &quot;HH:MM&quot;, with HH representing the 
     * 24-hour clock hour of the fire time.
     * 
     * @return the fire time for the trigger
     * @see #setFireAtTime(String)
     */
    public String getFireAtTime() {
        return this.fireAtHour + ":" + this.fireAtMinute;       
    }

    /**
     * Sets the <CODE>nextFireCutoffInterval</CODE> for the 
     * <CODE>NthIncludedDayTrigger</CODE>.
     * <P>
     * Because of the conceptual design of <CODE>NthIncludedDayTrigger</CODE>,
     * it is not always possible to decide with certainty that the trigger
     * will <I>never</I> fire again. Therefore, it will search for the next 
     * fire time up to a given cutoff. These cutoffs can be changed by using the
     * {@link #setNextFireCutoffInterval(int)} and 
     * {@link #getNextFireCutoffInterval()} methods. The default cutoff is 12
     * of the intervals specified by <CODE>{@link #getIntervalType()
     * intervalType}</CODE>.
     * <P>
     * In most cases, the default value of this setting (12) is sufficient (it
     * is highly unlikely, for example, that you will need to look at more than
     * 12 months of dates to ensure that your trigger will never fire again).  
     * However, this setting is included to allow for the rare exceptions where
     * this might not be true.
     * <P>
     * For example, if your trigger is associated with a calendar that excludes
     * a great many dates in the next 12 months, and hardly any following that,
     * it is possible (if <CODE>n</CODE> is large enough) that you could run 
     * into this situation.  
     * 
     * @param nextFireCutoffInterval the desired cutoff interval
     * @see #getNextFireCutoffInterval()
     * @see #getNextFireTime()
     */
    public void setNextFireCutoffInterval(int nextFireCutoffInterval) {
        this.nextFireCutoffInterval = nextFireCutoffInterval;
    }
    
    /**
     * Returns the <CODE>nextFireCutoffInterval</CODE> for the 
     * <CODE>NthIncludedDayTrigger</CODE>.
     * <P>
     * Because of the conceptual design of <CODE>NthIncludedDayTrigger</CODE>,
     * it is not always possible to decide with certainty that the trigger
     * will <I>never</I> fire again. Therefore, it will search for the next 
     * fire time up to a given cutoff. These cutoffs can be changed by using the
     * {@link #setNextFireCutoffInterval(int)} and 
     * {@link #getNextFireCutoffInterval()} methods. The default cutoff is 12
     * of the intervals specified by <CODE>{@link #getIntervalType()
     * intervalType}</CODE>.
     * 
     * @return the chosen cutoff interval
     * @see #setNextFireCutoffInterval(int)
     * @see #getNextFireTime()
     */
    public int getNextFireCutoffInterval() {
        return this.nextFireCutoffInterval;
    }
    
    /** 
     * Sets the date/time on which the trigger may begin firing. This defines
     * the initial boundary for trigger firings &mdash; the trigger will not
     * fire prior to this date and time. Defaults to the current date and time
     * when the <CODE>NthIncludedDayTrigger</CODE> is created.
     * 
     * @param  startTime the initial boundary for trigger firings
     * @throws java.lang.IllegalArgumentException
     *         the specified start time is after the current end time or is 
     *         null
     * @see #getStartTime()
     */
    public void setStartTime(Date startTime) {
        if (startTime == null) {
            throw new IllegalArgumentException("Start time may not be null");
        }
        if ((this.endTime != null) && endTime.before(startTime)) {
            throw new IllegalArgumentException("Start time must be before end time.");
        }
        this.startTime = startTime;
    }

    /**
     * Returns the date/time on which the trigger may begin firing. This 
     * defines the initial boundary for trigger firings &mdash; the trigger
     * will not fire prior to this date and time.
     * 
     * @return the initial date/time on which the trigger may begin firing
     * @see #setStartTime(Date)
     */
    public Date getStartTime() {
        return this.startTime;
    }

    /** 
     * Sets the date/time on which the trigger must stop firing. This defines
     * the final boundary for trigger firings &mdash; the trigger will not
     * fire after to this date and time. If this value is null, no end time
     * boundary is assumed, and the trigger can continue indefinitely.
     * 
     * @param  endTime the final boundary for trigger firings
     * @throws java.lang.IllegalArgumentException
     *         the specified end time is before the current start time
     * @see #getEndTime()
     */
    public void setEndTime(Date endTime) {
        if ((endTime != null) && endTime.before(startTime)) {
            throw new IllegalArgumentException("End time must be after start time.");
        }
        this.endTime = endTime;
    }

    /** 
     * Returns the date/time on which the trigger must stop firing. This 
     * defines the final boundary for trigger firings &mdash; the trigger will
     * not fire after to this date and time. If this value is null, no end time
     * boundary is assumed, and the trigger can continue indefinitely.
     * 
     * @return the date/time on which the trigger must stop firing
     * @see #setEndTime(Date)
     */
    public Date getEndTime() {
        return this.endTime;
    }

    /**
     * Returns the next time at which the <CODE>NthIncludedDayTrigger</CODE>
     * will fire. If the trigger will not fire again, <CODE>null</CODE> will be
     * returned. 
     * <P>
     * Because of the conceptual design of <CODE>NthIncludedDayTrigger</CODE>,
     * it is not always possible to decide with certainty that the trigger
     * will <I>never</I> fire again. Therefore, it will search for the next 
     * fire time up to a given cutoff. These cutoffs can be changed by using the
     * {@link #setNextFireCutoffInterval(int)} and 
     * {@link #getNextFireCutoffInterval()} methods. The default cutoff is 12
     * of the intervals specified by <CODE>{@link #getIntervalType()
     * intervalType}</CODE>.
     * <P>
     * The returned value is not guaranteed to be valid until after
     * the trigger has been added to the scheduler.
     * 
     * @return the next fire time for the trigger
     * @see #getNextFireCutoffInterval()
     * @see #setNextFireCutoffInterval(int)
     * @see #getFireTimeAfter(Date)
     */
    public Date getNextFireTime() {
        return this.nextFireTime;
    }

    /**
     * Returns the previous time at which the 
     * <CODE>NthIncludedDayTrigger</CODE> fired. If the trigger has not yet 
     * fired, <CODE>null</CODE> will be returned.
     * 
     * @return the previous fire time for the trigger
     */
    public Date getPreviousFireTime() {
        return this.previousFireTime;
    }

    /**
     * Returns the first time the <CODE>NthIncludedDayTrigger</CODE> will fire
     * after the specified date. 
     * <P> 
     * Because of the conceptual design of <CODE>NthIncludedDayTrigger</CODE>,
     * it is not always possible to decide with certainty that the trigger
     * will <I>never</I> fire again. Therefore, it will search for the next 
     * fire time up to a given cutoff. These cutoffs can be changed by using the
     * {@link #setNextFireCutoffInterval(int)} and 
     * {@link #getNextFireCutoffInterval()} methods. The default cutoff is 12
     * of the intervals specified by <CODE>{@link #getIntervalType()
     * intervalType}</CODE>.
     * <P>
     * Therefore, for triggers with <CODE>intervalType = 
     * {@link NthIncludedDayTrigger#INTERVAL_TYPE_WEEKLY 
     * INTERVAL_TYPE_WEEKLY}</CODE>, if the trigger will not fire within 12
     * weeks after the given date/time, <CODE>null</CODE> will be returned. For
     * triggers with <CODE>intervalType = 
     * {@link NthIncludedDayTrigger#INTERVAL_TYPE_MONTHLY
     * INTERVAL_TYPE_MONTHLY}</CODE>, if the trigger will not fire within 12 
     * months after the given date/time, <CODE>null</CODE> will be returned. 
     * For triggers with <CODE>intervalType = 
     * {@link NthIncludedDayTrigger#INTERVAL_TYPE_YEARLY 
     * INTERVAL_TYPE_YEARLY}</CODE>, if the trigger will not fire within 12
     * years after the given date/time, <CODE>null</CODE> will be returned.  In 
     * all cases, if the trigger will not fire before <CODE>endTime</CODE>, 
     * <CODE>null</CODE> will be returned.
     * 
     * @param  afterTime The time after which to find the nearest fire time.
     *                   This argument is treated as exclusive &mdash; that is,
     *                   if afterTime is a valid fire time for the trigger, it
     *                   will not be returned as the next fire time.
     * @return the first time the trigger will fire following the specified
     *         date
     */
    public Date getFireTimeAfter(Date afterTime) {
        if (afterTime == null) {
            afterTime = new Date();
        }
        
        if (afterTime.before(this.startTime)) {
            afterTime = new Date(startTime.getTime() - 1000l);
        }
        
        if (this.intervalType == INTERVAL_TYPE_WEEKLY) {
            return getWeeklyFireTimeAfter(afterTime);
        } else if (this.intervalType == INTERVAL_TYPE_MONTHLY) {
            return getMonthlyFireTimeAfter(afterTime);
        } else if (this.intervalType == INTERVAL_TYPE_YEARLY) {
            return getYearlyFireTimeAfter(afterTime);
        } else {
            return null;
        }
    }

    /**
     * Returns the last time the <CODE>NthIncludedDayTrigger</CODE> will fire.
     * If the trigger will not fire at any point between <CODE>startTime</CODE>
     * and <CODE>endTime</CODE>, <CODE>null</CODE> will be returned.
     * 
     * @return the last time the trigger will fire.
     */
    public Date getFinalFireTime() {
        Date finalTime = null;
        java.util.Calendar currCal = java.util.Calendar.getInstance();
        currCal.setTime(this.endTime);
        
        while ((finalTime == null) 
                && (this.startTime.before(currCal.getTime()))) {
            currCal.add(java.util.Calendar.DATE, -1);
            
            finalTime = getFireTimeAfter(currCal.getTime());
        }
        
        return finalTime;
    }

    /**
     * Called when the <CODE>Scheduler</CODE> has decided to 'fire' the trigger
     * (execute the associated <CODE>Job</CODE>), in order to give the 
     * <CODE>Trigger</CODE> a chance to update itself for its next triggering 
     * (if any).
     */
    public void triggered(Calendar calendar) {
        this.calendar = calendar;
        this.previousFireTime = this.nextFireTime;
        this.nextFireTime = getFireTimeAfter(this.nextFireTime);
    }
    
    /**
     * Called by the scheduler at the time a <CODE>Trigger</code> is first
     * added to the scheduler, in order to have the <CODE>Trigger</CODE>
     * compute its first fire time, based on any associated calendar.
     * <P>
     * After this method has been called, <CODE>getNextFireTime()</CODE>
     * should return a valid answer.
     * </p>
     * 
     * @return the first time at which the <CODE>Trigger</CODE> will be fired
     *         by the scheduler, which is also the same value 
     *         {@link #getNextFireTime()} will return (until after the first 
     *         firing of the <CODE>Trigger</CODE>).
     */
    public Date computeFirstFireTime(Calendar calendar) {       
        this.calendar = calendar;
        this.nextFireTime = 
            getFireTimeAfter(new Date(this.startTime.getTime() - 1000l));
        
        return this.nextFireTime;
    }

    /**
    * Called after the <CODE>Scheduler</CODE> has executed the 
    * <code>JobDetail</CODE> associated with the <CODE>Trigger</CODE> in order
    * to get the final instruction code from the trigger.
    * 
    * @param jobCtx the <CODE>JobExecutionContext</CODE> that was used by the
    *               <CODE>Job</CODE>'s <CODE>execute()</CODE> method.
    * @param result the <CODE>JobExecutionException</CODE> thrown by the
    *               <CODE>Job</CODE>, if any (may be <CODE>null</CODE>)
    * @return one of the Trigger.INSTRUCTION_XXX constants.
    */
    public int executionComplete(JobExecutionContext jobCtx,
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
    * Used by the <CODE>Scheduler</CODE> to determine whether or not it is
    * possible for this <CODE>Trigger</CODE> to fire again.
    * <P>
    * If the returned value is <CODE>false</CODE> then the 
    * <CODE>Scheduler</CODE> may remove the <CODE>Trigger</CODE> from the
    * <CODE>JobStore</CODE>
    * 
    * @return a boolean indicator of whether the trigger could potentially fire
    *         again
    */
    public boolean mayFireAgain() {
        return (getNextFireTime() == null);
    }

    /**
     * Indicates whether <CODE>misfireInstruction</CODE> is a valid misfire
     * instruction for this <CODE>Trigger</CODE>.
     * 
     * @return whether <CODE>misfireInstruction</CODE> is valid.
     */
    protected boolean validateMisfireInstruction(int misfireInstruction) {
        if ((misfireInstruction == MISFIRE_INSTRUCTION_SMART_POLICY) ||
                (misfireInstruction == MISFIRE_INSTRUCTION_DO_NOTHING) ||
                (misfireInstruction == MISFIRE_INSTRUCTION_FIRE_ONCE_NOW)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Updates the <CODE>NthIncludedDayTrigger</CODE>'s state based on the
     * MISFIRE_INSTRUCTION_XXX that was selected when the 
     * <CODE>NthIncludedDayTrigger</CODE> was created
     * <P>
     * If the misfire instruction is set to MISFIRE_INSTRUCTION_SMART_POLICY,
     * then the instruction will be interpreted as 
     * {@link #MISFIRE_INSTRUCTION_FIRE_ONCE_NOW}.
     * 
     * @param calendar a new or updated calendar to use for the trigger
     */
    public void updateAfterMisfire(Calendar calendar) {
        int instruction = getMisfireInstruction();
        
        this.calendar = calendar;
        
        if (instruction == MISFIRE_INSTRUCTION_SMART_POLICY) {
            instruction = MISFIRE_INSTRUCTION_FIRE_ONCE_NOW;
        }
        
        if (instruction == MISFIRE_INSTRUCTION_DO_NOTHING) {
            this.nextFireTime = getFireTimeAfter(new Date());
        } else if (instruction == MISFIRE_INSTRUCTION_FIRE_ONCE_NOW) {
            this.nextFireTime = new Date();
        }
    }

    /**
     * Updates the <CODE>NthIncludedDayTrigger</CODE>'s state based on the 
     * given new version of the associated <CODE>Calendar</CODE>. 
     * 
     * @param calendar         a new or updated calendar to use for the trigger
     * @param misfireThreshold the amount of time (in milliseconds) that must
     *                         be between &quot;now&quot; and the time the next
     *                         firing of the trigger is supposed to occur.
     */
    public void updateWithNewCalendar(Calendar calendar, 
            long misfireThreshold) {
        Date now = new Date();
        long diff;
        
        this.calendar = calendar;
        this.nextFireTime = getFireTimeAfter(this.previousFireTime);
        
        if ((this.nextFireTime != null) && (this.nextFireTime.before(now))) {
            diff = now.getTime() - this.nextFireTime.getTime();
            if (diff >= misfireThreshold) {
                this.nextFireTime = getFireTimeAfter(this.nextFireTime);
            }
        }
    }

    /**
     * Calculates the first time an <CODE>NthIncludedDayTrigger</CODE> with 
     * <CODE>intervalType = {@link #INTERVAL_TYPE_WEEKLY}</CODE> will fire 
     * after the specified date. See {@link #getNextFireTime} for more 
     * information.
     * 
     * @param afterDate The time after which to find the nearest fire time.
     *                  This argument is treated as exclusive &mdash; that is,
     *                  if afterTime is a valid fire time for the trigger, it
     *                  will not be returned as the next fire time.
     * @return the first time the trigger will fire following the specified
     *         date
     */
    private Date getWeeklyFireTimeAfter(Date afterDate) {
        int currN = 0;
        java.util.Calendar afterCal;
        java.util.Calendar currCal;
        int currWeek;
        int weekCount = 0;
        boolean gotOne = false;
        
        afterCal = java.util.Calendar.getInstance();
        afterCal.setTime(afterDate);
        
        currCal = java.util.Calendar.getInstance();
        currCal.set(afterCal.get(java.util.Calendar.YEAR),
                    afterCal.get(java.util.Calendar.MONTH), 
                    afterCal.get(java.util.Calendar.DAY_OF_MONTH));

        //move to the first day of the week (SUNDAY)
        currCal.add(java.util.Calendar.DAY_OF_MONTH, 
                (afterCal.get(java.util.Calendar.DAY_OF_WEEK) - 1) * -1);

        currCal.set(java.util.Calendar.HOUR_OF_DAY, this.fireAtHour);
        currCal.set(java.util.Calendar.MINUTE, this.fireAtMinute);
        currCal.set(java.util.Calendar.SECOND, 0);
        currCal.set(java.util.Calendar.MILLISECOND, 0);
        
        currWeek = currCal.get(java.util.Calendar.WEEK_OF_YEAR);
        
        while ((!gotOne) && (weekCount < this.nextFireCutoffInterval)) {
            while ((currN != this.n) && (weekCount < 12)) {
                //if we move into a new month, reset the current "n" counter
                if (currCal.get(java.util.Calendar.WEEK_OF_YEAR) != currWeek) {
                    currN = 0;
                    weekCount++;
                    currWeek = currCal.get(java.util.Calendar.WEEK_OF_YEAR);
                }
                
                //treating a null calendar as an all-inclusive calendar,
                // increment currN if the current date being tested is included
                // on the calendar
                if ((calendar == null) 
                        || (calendar.isTimeIncluded(currCal.getTime().getTime()))) {
                    currN++;
                }

                if (currN != this.n) {
                    currCal.add(java.util.Calendar.DATE, 1);
                }
                
                //if we pass endTime, drop out and return null.
                if ((this.endTime != null) 
                        && (currCal.getTime().after(this.endTime))) {
                    return null;
                }
            } 
            
            //We found an "n" or we've checked the requisite number of weeks.
            // If we've found an "n", is it the right one? -- that is, we could
            // be looking at an nth day PRIOR to afterDate
            if (currN == this.n) {
                if (afterDate.before(currCal.getTime())) {
                    gotOne = true;
                } else { //resume checking on the first day of the next week
                    currCal.add(java.util.Calendar.DAY_OF_MONTH, -1 * (currN - 1));
                    currCal.add(java.util.Calendar.DAY_OF_MONTH, 7);
                    currN = 0;
                }
            }
        }
        
        if (weekCount < this.nextFireCutoffInterval) {
            return currCal.getTime();           
        } else {
            return null;
        }
    }

    /**
     * Calculates the first time an <CODE>NthIncludedDayTrigger</CODE> with 
     * <CODE>intervalType = {@link #INTERVAL_TYPE_MONTHLY}</CODE> will fire 
     * after the specified date. See {@link #getNextFireTime} for more 
     * information.
     * 
     * @param afterDate The time after which to find the nearest fire time.
     *                  This argument is treated as exclusive &mdash; that is,
     *                  if afterTime is a valid fire time for the trigger, it
     *                  will not be returned as the next fire time.
     * @return the first time the trigger will fire following the specified
     *         date
     */
    private Date getMonthlyFireTimeAfter(Date afterDate) {
        int currN = 0;
        java.util.Calendar afterCal;
        java.util.Calendar currCal;
        int currMonth;
        int monthCount = 0;
        boolean gotOne = false;
        
        afterCal = java.util.Calendar.getInstance();
        afterCal.setTime(afterDate);
        
        currCal = java.util.Calendar.getInstance();
        currCal.set(afterCal.get(java.util.Calendar.YEAR),
                    afterCal.get(java.util.Calendar.MONTH), 1);
        currCal.set(java.util.Calendar.HOUR_OF_DAY, this.fireAtHour);
        currCal.set(java.util.Calendar.MINUTE, this.fireAtMinute);
        currCal.set(java.util.Calendar.SECOND, 0);
        currCal.set(java.util.Calendar.MILLISECOND, 0);
        
        currMonth = currCal.get(java.util.Calendar.MONTH);
        
        while ((!gotOne) && (monthCount < this.nextFireCutoffInterval)) {
            while ((currN != this.n) && (monthCount < 12)) {
                //if we move into a new month, reset the current "n" counter
                if (currCal.get(java.util.Calendar.MONTH) != currMonth) {
                    currN = 0;
                    monthCount++;
                    currMonth = currCal.get(java.util.Calendar.MONTH);
                }
                
                //treating a null calendar as an all-inclusive calendar,
                // increment currN if the current date being tested is included
                // on the calendar
                if ((calendar == null) 
                        || (calendar.isTimeIncluded(currCal.getTime().getTime()))) {
                    currN++;
                }

                if (currN != this.n) {
                    currCal.add(java.util.Calendar.DATE, 1);
                }
                
                //if we pass endTime, drop out and return null.
                if ((this.endTime != null) 
                        && (currCal.getTime().after(this.endTime))) {
                    return null;
                }
            } 
            
            //We found an "n" or we've checked the requisite number of months.
            // If we've found an "n", is it the right one? -- that is, we could
            // be looking at an nth day PRIOR to afterDate
            if (currN == this.n) {
                if (afterDate.before(currCal.getTime())) {
                    gotOne = true;
                } else { //resume checking on the first day of the next month
                    currCal.set(java.util.Calendar.DAY_OF_MONTH, 1);
                    currCal.add(java.util.Calendar.MONTH, 1);
                    currN = 0;
                }
            }
        }
        
        if (monthCount < this.nextFireCutoffInterval) {
            return currCal.getTime();           
        } else {
            return null;
        }
    }

    /**
     * Calculates the first time an <CODE>NthIncludedDayTrigger</CODE> with 
     * <CODE>intervalType = {@link #INTERVAL_TYPE_YEARLY}</CODE> will fire 
     * after the specified date. See {@link #getNextFireTime} for more 
     * information.
     * 
     * @param afterDate The time after which to find the nearest fire time.
     *                  This argument is treated as exclusive &mdash; that is,
     *                  if afterTime is a valid fire time for the trigger, it
     *                  will not be returned as the next fire time.
     * @return the first time the trigger will fire following the specified
     *         date
     */
    private Date getYearlyFireTimeAfter(Date afterDate) {
        int currN = 0;
        java.util.Calendar afterCal;
        java.util.Calendar currCal;
        int currYear;
        int yearCount = 0;
        boolean gotOne = false;
        
        afterCal = java.util.Calendar.getInstance();
        afterCal.setTime(afterDate);
        
        currCal = java.util.Calendar.getInstance();
        currCal.set(afterCal.get(java.util.Calendar.YEAR),
                    java.util.Calendar.JANUARY, 1);
        currCal.set(java.util.Calendar.HOUR_OF_DAY, this.fireAtHour);
        currCal.set(java.util.Calendar.MINUTE, this.fireAtMinute);
        currCal.set(java.util.Calendar.SECOND, 0);
        currCal.set(java.util.Calendar.MILLISECOND, 0);
        
        currYear = currCal.get(java.util.Calendar.YEAR);
        
        while ((!gotOne) && (yearCount < this.nextFireCutoffInterval)) {
            while ((currN != this.n) && (yearCount < 5)) {
                //if we move into a new year, reset the current "n" counter
                if (currCal.get(java.util.Calendar.YEAR) != currYear) {
                    currN = 0;
                    yearCount++;
                    currYear = currCal.get(java.util.Calendar.YEAR);
                }
                
                //treating a null calendar as an all-inclusive calendar,
                // increment currN if the current date being tested is included
                // on the calendar
                if ((calendar == null) 
                        || (calendar.isTimeIncluded(currCal.getTime().getTime()))) {
                    currN++;
                }

                if (currN != this.n) {
                    currCal.add(java.util.Calendar.DATE, 1);
                }
                
                //if we pass endTime, drop out and return null.
                if ((this.endTime != null) 
                        && (currCal.getTime().after(this.endTime))) {
                    return null;
                }
            } 
            
            //We found an "n" or we've checked the requisite number of years.
            // If we've found an "n", is it the right one? -- that is, we 
            // could be looking at an nth day PRIOR to afterDate
            if (currN == this.n) {
                if (afterDate.before(currCal.getTime())) {
                    gotOne = true;
                } else { //resume checking on the first day of the next year
                    currCal.set(java.util.Calendar.DAY_OF_MONTH, 1);
                    currCal.set(java.util.Calendar.MONTH, 
                                java.util.Calendar.JANUARY);
                    currCal.add(java.util.Calendar.YEAR, 1);
                    currN = 0;
                }
            }
        }
        
        if (yearCount < this.nextFireCutoffInterval) {
            return currCal.getTime();           
        } else {
            return null;
        }
    }
}