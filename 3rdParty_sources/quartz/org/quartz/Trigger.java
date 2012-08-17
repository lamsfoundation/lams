
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

import java.util.Date;
import java.util.LinkedList;


/**
 * <p>
 * The base abstract class to be extended by all <code>Trigger</code>s.
 * </p>
 * 
 * <p>
 * <code>Triggers</code> s have a name and group associated with them, which
 * should uniquely identify them within a single <code>{@link Scheduler}</code>.
 * </p>
 * 
 * <p>
 * <code>Trigger</code>s are the 'mechanism' by which <code>Job</code> s
 * are scheduled. Many <code>Trigger</code> s can point to the same <code>Job</code>,
 * but a single <code>Trigger</code> can only point to one <code>Job</code>.
 * </p>
 * 
 * <p>
 * Triggers can 'send' parameters/data to <code>Job</code>s by placing contents
 * into the <code>JobDataMap</code> on the <code>Trigger</code>.
 * </p>
 * 
 * @see SimpleTrigger
 * @see CronTrigger
 * @see NthIncludedDayTrigger
 * @see TriggerUtils
 * @see JobDataMap
 * @see JobExecutionContext
 * 
 * @author James House
 * @author Sharada Jambula
 */
public abstract class Trigger implements java.io.Serializable, Cloneable,
        Comparable {

    private static final long serialVersionUID = -3904243490805975570L;
    
    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * 
     * Constants.
     * 
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    /**
     * <p>
     * Instructs the <code>{@link Scheduler}</code> that the <code>{@link Trigger}</code>
     * has no further instructions.
     * </p>
     */
    public static final int INSTRUCTION_NOOP = 0;

    /**
     * <p>
     * Instructs the <code>{@link Scheduler}</code> that the <code>{@link Trigger}</code>
     * wants the <code>{@link org.quartz.JobDetail}</code> to re-execute
     * immediately. If not in a 'RECOVERING' or 'FAILED_OVER' situation, the
     * execution context will be re-used (giving the <code>Job</code> the
     * abilitiy to 'see' anything placed in the context by its last execution).
     * </p>
     */
    public static final int INSTRUCTION_RE_EXECUTE_JOB = 1;

    /**
     * <p>
     * Instructs the <code>{@link Scheduler}</code> that the <code>{@link Trigger}</code>
     * should be put in the <code>COMPLETE</code> state.
     * </p>
     */
    public static final int INSTRUCTION_SET_TRIGGER_COMPLETE = 2;

    /**
     * <p>
     * Instructs the <code>{@link Scheduler}</code> that the <code>{@link Trigger}</code>
     * wants itself deleted.
     * </p>
     */
    public static final int INSTRUCTION_DELETE_TRIGGER = 3;

    /**
     * <p>
     * Instructs the <code>{@link Scheduler}</code> that all <code>Trigger</code>
     * s referencing the same <code>{@link org.quartz.JobDetail}</code> as
     * this one should be put in the <code>COMPLETE</code> state.
     * </p>
     */
    public static final int INSTRUCTION_SET_ALL_JOB_TRIGGERS_COMPLETE = 4;

    /**
     * <p>
     * Instructs the <code>{@link Scheduler}</code> that all <code>Trigger</code>
     * s referencing the same <code>{@link org.quartz.JobDetail}</code> as
     * this one should be put in the <code>ERROR</code> state.
     * </p>
     */
    public static final int INSTRUCTION_SET_TRIGGER_ERROR = 5;

    /**
     * <p>
     * Instructs the <code>{@link Scheduler}</code> that the <code>Trigger</code>
     * should be put in the <code>ERROR</code> state.
     * </p>
     */
    public static final int INSTRUCTION_SET_ALL_JOB_TRIGGERS_ERROR = 6;

    /**
     * <p>
     * Instructs the <code>{@link Scheduler}</code> that upon a mis-fire
     * situation, the <code>updateAfterMisfire()</code> method will be called
     * on the <code>Trigger</code> to determine the mis-fire instruction.
     * </p>
     * 
     * <p>
     * In order to see if this instruction fits your needs, you should look at
     * the documentation for the <code>getSmartMisfirePolicy()</code> method
     * on the particular <code>Trigger</code> implementation you are using.
     * </p>
     */
    public static final int MISFIRE_INSTRUCTION_SMART_POLICY = 0;

    /**
     * <p>
     * Indicates that the <code>Trigger</code> is in the "normal" state.
     * </p>
     */
    public final static int STATE_NORMAL = 0;

    /**
     * <p>
     * Indicates that the <code>Trigger</code> is in the "paused" state.
     * </p>
     */
    public final static int STATE_PAUSED = 1;

    /**
     * <p>
     * Indicates that the <code>Trigger</code> is in the "complete" state.
     * </p>
     * 
     * <p>
     * "Complete" indicates that the trigger has not remaining fire-times in
     * its schedule.
     * </p>
     */
    public final static int STATE_COMPLETE = 2;

    /**
     * <p>
     * Indicates that the <code>Trigger</code> is in the "error" state.
     * </p>
     * 
     * <p>
     * A <code>Trigger</code> arrives at the error state when the scheduler
     * attempts to fire it, but cannot due to an error creating and executing
     * its related job. Often this is due to the <code>Job</code>'s
     * class not existing in the classpath.
     * </p>
     * 
     * <p>
     * When the trigger is in the error state, the scheduler will make no
     * attempts to fire it.
     * </p>
     */
    public final static int STATE_ERROR = 3;


    /**
     * <p>
     * Indicates that the <code>Trigger</code> is in the "blocked" state.
     * </p>
     * 
     * <p>
     * A <code>Trigger</code> arrives at the blocked state when the job that
     * it is associated with is a <code>StatefulJob</code> and it is 
     * currently executing.
     * </p>
     *
     * @see StatefulJob 
     */
    public final static int STATE_BLOCKED = 4;

    /**
     * <p>
     * Indicates that the <code>Trigger</code> does not exist.
     * </p>
     */
    public final static int STATE_NONE = -1;

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * 
     * Data members.
     * 
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    private String name;

    private String group = Scheduler.DEFAULT_GROUP;

    private String jobName;

    private String jobGroup = Scheduler.DEFAULT_GROUP;

    private String description;
    
    private JobDataMap jobDataMap;

    private boolean volatility = false;

    private String calendarName = null;

    private String fireInstanceId = null;

    private int misfireInstruction = MISFIRE_INSTRUCTION_SMART_POLICY;

    private LinkedList triggerListeners = new LinkedList();

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * 
     * Constructors.
     * 
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    /**
     * <p>
     * Create a <code>Trigger</code> with no specified name, group, or <code>{@link org.quartz.JobDetail}</code>.
     * </p>
     * 
     * <p>
     * Note that the {@link #setName(String)},{@link #setGroup(String)}and
     * the {@link #setJobName(String)}and {@link #setJobGroup(String)}methods
     * must be called before the <code>Trigger</code> can be placed into a
     * {@link Scheduler}.
     * </p>
     */
    public Trigger() {
        // do nothing...
    }

    /**
     * <p>
     * Create a <code>Trigger</code> with the given name, and group.
     * </p>
     * 
     * <p>
     * Note that the {@link #setJobName(String)}and
     * {@link #setJobGroup(String)}methods must be called before the <code>Trigger</code>
     * can be placed into a {@link Scheduler}.
     * </p>
     * 
     * @param group if <code>null</code>, Scheduler.DEFAULT_GROUP will be used.
     * 
     * @exception IllegalArgumentException
     *              if name is null or empty, or the group is an empty string.
     */
    public Trigger(String name, String group) {
        setName(name);
        setGroup(group);
    }

    /**
     * <p>
     * Create a <code>Trigger</code> with the given name, and group.
     * </p>
     * 
     * @param group if <code>null</code>, Scheduler.DEFAULT_GROUP will be used.
     * 
     * @exception IllegalArgumentException
     *              if name is null or empty, or the group is an empty string.
     */
    public Trigger(String name, String group, String jobName, String jobGroup) {
        setName(name);
        setGroup(group);
        setJobName(jobName);
        setJobGroup(jobGroup);
    }

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * 
     * Interface.
     * 
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    /**
     * <p>
     * Get the name of this <code>Trigger</code>.
     * </p>
     */
    public String getName() {
        return name;
    }

    /**
     * <p>
     * Set the name of this <code>Trigger</code>.
     * </p>
     * 
     * @exception IllegalArgumentException
     *              if name is null or empty.
     */
    public void setName(String name) {
        if (name == null || name.trim().length() == 0)
                throw new IllegalArgumentException(
                        "Trigger name cannot be null or empty.");

        this.name = name;
    }

    /**
     * <p>
     * Get the group of this <code>Trigger</code>.
     * </p>
     */
    public String getGroup() {
        return group;
    }

    /**
     * <p>
     * Set the name of this <code>Trigger</code>. 
     * </p>
     * 
     * @param group if <code>null</code>, Scheduler.DEFAULT_GROUP will be used.
     * 
     * @exception IllegalArgumentException
     *              if group is an empty string.
     */
    public void setGroup(String group) {
        if (group != null && group.trim().length() == 0)
                throw new IllegalArgumentException(
                        "Group name cannot be an empty string.");

        if(group == null)
            group = Scheduler.DEFAULT_GROUP;
        
        this.group = group;
    }

    /**
     * <p>
     * Get the name of the associated <code>{@link org.quartz.JobDetail}</code>.
     * </p>
     */
    public String getJobName() {
        return jobName;
    }

    /**
     * <p>
     * Set the name of the associated <code>{@link org.quartz.JobDetail}</code>.
     * </p>
     * 
     * @exception IllegalArgumentException
     *              if jobName is null or empty.
     */
    public void setJobName(String jobName) {
        if (jobName == null || jobName.trim().length() == 0)
                throw new IllegalArgumentException(
                        "Job name cannot be null or empty.");

        this.jobName = jobName;
    }

    /**
     * <p>
     * Get the name of the associated <code>{@link org.quartz.JobDetail}</code>'s
     * group.
     * </p>
     */
    public String getJobGroup() {
        return jobGroup;
    }

    /**
     * <p>
     * Set the name of the associated <code>{@link org.quartz.JobDetail}</code>'s
     * group.
     * </p>
     * 
     * @param group if <code>null</code>, Scheduler.DEFAULT_GROUP will be used.
     * 
     * @exception IllegalArgumentException
     *              if group is an empty string.
     */
    public void setJobGroup(String jobGroup) {
        if (jobGroup != null && jobGroup.trim().length() == 0)
                throw new IllegalArgumentException(
                        "Group name cannot be null or empty.");

        if(jobGroup == null)
            jobGroup = Scheduler.DEFAULT_GROUP;
        
        this.jobGroup = jobGroup;
    }

    /**
     * <p>
     * Returns the 'full name' of the <code>Trigger</code> in the format
     * "group.name".
     * </p>
     */
    public String getFullName() {
        return group + "." + name;
    }

    /**
     * <p>
     * Returns the 'full name' of the <code>Job</code> that the <code>Trigger</code>
     * points to, in the format "group.name".
     * </p>
     */
    public String getFullJobName() {
        return jobGroup + "." + jobName;
    }

    /**
     * <p>
     * Return the description given to the <code>Trigger</code> instance by
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
     * Set a description for the <code>Trigger</code> instance - may be
     * useful for remembering/displaying the purpose of the trigger, though the
     * description has no meaning to Quartz.
     * </p>
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * <p>
     * Set whether or not the <code>Trigger</code> should be persisted in the
     * <code>{@link org.quartz.spi.JobStore}</code> for re-use after program
     * restarts.
     * </p>
     */
    public void setVolatility(boolean volatility) {
        this.volatility = volatility;
    }

    /**
     * <p>
     * Associate the <code>{@link Calendar}</code> with the given name with
     * this Trigger.
     * </p>
     * 
     * @param calendarName
     *          use <code>null</code> to dis-associate a Calendar.
     */
    public void setCalendarName(String calendarName) {
        this.calendarName = calendarName;
    }

    /**
     * <p>
     * Get the name of the <code>{@link Calendar}</code> associated with this
     * Trigger.
     * </p>
     * 
     * @return <code>null</code> if there is no associated Calendar.
     */
    public String getCalendarName() {
        return calendarName;
    }

    /**
     * <p>
     * Get the <code>JobDataMap</code> that is associated with the 
     * <code>Trigger</code>.
     * </p>
     * 
     * <p>
     * Changes made to this map during job execution are not re-persisted, and
     * in fact typically result in an <code>IllegalStateException</code>.
     * </p>
     */
    public JobDataMap getJobDataMap() {
        if (jobDataMap == null) jobDataMap = new JobDataMap();
        return jobDataMap;
    }


    /**
     * <p>
     * Set the <code>JobDataMap</code> to be associated with the 
     * <code>Trigger</code>.
     * </p>
     */
    public void setJobDataMap(JobDataMap jobDataMap) {
        this.jobDataMap = jobDataMap;
    }
    
    /**
     * <p>
     * Whether or not the <code>Trigger</code> should be persisted in the
     * <code>{@link org.quartz.spi.JobStore}</code> for re-use after program
     * restarts.
     * </p>
     * 
     * <p>
     * If not explicitly set, the default value is <code>false</code>.
     * </p>
     * 
     * @return <code>true</code> if the <code>Trigger</code> should be
     *         garbage collected along with the <code>{@link Scheduler}</code>.
     */
    public boolean isVolatile() {
        return volatility;
    }

    /**
     * <p>
     * Add the specified name of a <code>{@link TriggerListener}</code> to
     * the end of the <code>Trigger</code>'s list of listeners.
     * </p>
     */
    public void addTriggerListener(String name) {
        triggerListeners.add(name);
    }

    /**
     * <p>
     * Remove the specified name of a <code>{@link TriggerListener}</code>
     * from the <code>Trigger</code>'s list of listeners.
     * </p>
     * 
     * @return true if the given name was found in the list, and removed
     */
    public boolean removeTriggerListener(String name) {
        return triggerListeners.remove(name);
    }

    /**
     * <p>
     * Returns an array of <code>String</code> s containing the names of all
     * <code>{@link TriggerListener}</code> assigned to the <code>Trigger</code>,
     * in the order in which they should be notified.
     * </p>
     */
    public String[] getTriggerListenerNames() {
        String[] outNames = new String[triggerListeners.size()];
        return (String[]) triggerListeners.toArray(outNames);
    }

    /**
     * <p>
     * This method should not be used by the Quartz client.
     * </p>
     * 
     * <p>
     * Called when the <code>{@link Scheduler}</code> has decided to 'fire'
     * the trigger (execute the associated <code>Job</code>), in order to
     * give the <code>Trigger</code> a chance to update itself for its next
     * triggering (if any).
     * </p>
     * 
     * @see #executionComplete(JobExecutionContext, JobExecutionException)
     */
    public abstract void triggered(Calendar calendar);

    /**
     * <p>
     * This method should not be used by the Quartz client.
     * </p>
     * 
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
    public abstract Date computeFirstFireTime(Calendar calendar);

    /**
     * <p>
     * This method should not be used by the Quartz client.
     * </p>
     * 
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
    public abstract int executionComplete(JobExecutionContext context,
            JobExecutionException result);

    /**
     * <p>
     * Used by the <code>{@link Scheduler}</code> to determine whether or not
     * it is possible for this <code>Trigger</code> to fire again.
     * </p>
     * 
     * <p>
     * If the returned value is <code>false</code> then the <code>Scheduler</code>
     * may remove the <code>Trigger</code> from the <code>{@link org.quartz.spi.JobStore}</code>.
     * </p>
     */
    public abstract boolean mayFireAgain();

    /**
     * <p>
     * Get the time at which the <code>Trigger</code> should occur.
     * </p>
     */
    public abstract Date getStartTime();

    public abstract void setStartTime(Date startTime); 
    
    public abstract void setEndTime(Date endTime); 
    
    /**
     * <p>
     * Get the time at which the <code>Trigger</code> should quit repeating -
     * even if an assigned 'repeatCount' isn't yet satisfied.
     * </p>
     * 
     * @see #getFinalFireTime()
     */
    public abstract Date getEndTime();

    /**
     * <p>
     * Returns the next time at which the <code>Trigger</code> will fire. If
     * the trigger will not fire again, <code>null</code> will be returned.
     * The value returned is not guaranteed to be valid until after the <code>Trigger</code>
     * has been added to the scheduler.
     * </p>
     */
    public abstract Date getNextFireTime();

    /**
     * <p>
     * Returns the previous time at which the <code>Trigger</code> will fire.
     * If the trigger has not yet fired, <code>null</code> will be returned.
     */
    public abstract Date getPreviousFireTime();

    /**
     * <p>
     * Returns the next time at which the <code>Trigger</code> will fire,
     * after the given time. If the trigger will not fire after the given time,
     * <code>null</code> will be returned.
     * </p>
     */
    public abstract Date getFireTimeAfter(Date afterTime);

    /**
     * <p>
     * Returns the last time at which the <code>Trigger</code> will fire, if
     * the Trigger will repeat indefinitely, null will be returned.
     * </p>
     * 
     * <p>
     * Note that the return time *may* be in the past.
     * </p>
     */
    public abstract Date getFinalFireTime();

    /**
     * <p>
     * Set the instruction the <code>Scheduler</code> should be given for
     * handling misfire situations for this <code>Trigger</code>- the
     * concrete <code>Trigger</code> type that you are using will have
     * defined a set of additional <code>MISFIRE_INSTRUCTION_XXX</code>
     * constants that may be passed to this method.
     * </p>
     * 
     * <p>
     * If not explicitly set, the default value is <code>MISFIRE_INSTRUCTION_SMART_POLICY</code>.
     * </p>
     * 
     * @see #MISFIRE_INSTRUCTION_SMART_POLICY
     * @see #updateAfterMisfire(Calendar)
     * @see SimpleTrigger
     * @see CronTrigger
     */
    public void setMisfireInstruction(int misfireInstruction) {
        if (!validateMisfireInstruction(misfireInstruction))
                throw new IllegalArgumentException(
                        "The misfire instruction code is invalid for this type of trigger.");
        this.misfireInstruction = misfireInstruction;
    }

    protected abstract boolean validateMisfireInstruction(int misfireInstruction);

    /**
     * <p>
     * Get the instruction the <code>Scheduler</code> should be given for
     * handling misfire situations for this <code>Trigger</code>- the
     * concrete <code>Trigger</code> type that you are using will have
     * defined a set of additional <code>MISFIRE_INSTRUCTION_XXX</code>
     * constants that may be passed to this method.
     * </p>
     * 
     * <p>
     * If not explicitly set, the default value is <code>MISFIRE_INSTRUCTION_SMART_POLICY</code>.
     * </p>
     * 
     * @see #MISFIRE_INSTRUCTION_SMART_POLICY
     * @see #updateAfterMisfire(Calendar)
     * @see SimpleTrigger
     * @see CronTrigger
     */
    public int getMisfireInstruction() {
        return misfireInstruction;
    }

    /**
     * <p>
     * This method should not be used by the Quartz client.
     * </p>
     * 
     * <p>
     * To be implemented by the concrete classes that extend this class.
     * </p>
     * 
     * <p>
     * The implementation should update the <code>Trigger</code>'s state
     * based on the MISFIRE_INSTRUCTION_XXX that was selected when the <code>Trigger</code>
     * was created.
     * </p>
     */
    public abstract void updateAfterMisfire(Calendar cal);

    /**
     * <p>
     * This method should not be used by the Quartz client.
     * </p>
     * 
     * <p>
     * To be implemented by the concrete class.
     * </p>
     * 
     * <p>
     * The implementation should update the <code>Trigger</code>'s state
     * based on the given new version of the associated <code>Calendar</code>
     * (the state should be updated so that it's next fire time is appropriate
     * given the Calendar's new settings). 
     * </p>
     * 
     * @param cal
     */
    public abstract void updateWithNewCalendar(Calendar cal, long misfireThreshold);

    /**
     * <p>
     * Validates whether the properties of the <code>JobDetail</code> are
     * valid for submission into a <code>Scheduler</code>.
     * 
     * @throws IllegalStateException
     *           if a required property (such as Name, Group, Class) is not
     *           set.
     */
    public void validate() throws SchedulerException {
        if (name == null)
                throw new SchedulerException("Trigger's name cannot be null",
                        SchedulerException.ERR_CLIENT_ERROR);

        if (group == null)
                throw new SchedulerException("Trigger's group cannot be null",
                        SchedulerException.ERR_CLIENT_ERROR);

        if (jobName == null)
                throw new SchedulerException(
                        "Trigger's related Job's name cannot be null",
                        SchedulerException.ERR_CLIENT_ERROR);

        if (jobGroup == null)
                throw new SchedulerException(
                        "Trigger's related Job's group cannot be null",
                        SchedulerException.ERR_CLIENT_ERROR);
    }

    /**
     * <p>
     * This method should not be used by the Quartz client.
     * </p>
     * 
     * <p>
     * Usable by <code>{@link org.quartz.spi.JobStore}</code>
     * implementations, in order to facilitate 'recognizing' instances of fired
     * <code>Trigger</code> s as their jobs complete execution.
     * </p>
     * 
     *  
     */
    public void setFireInstanceId(String id) {
        this.fireInstanceId = id;
    }

    /**
     * <p>
     * This method should not be used by the Quartz client.
     * </p>
     */
    public String getFireInstanceId() {
        return fireInstanceId;
    }

    /**
     * <p>
     * Return a simple string representation of this object.
     * </p>
     */
    public String toString() {
        return "Trigger '" + getFullName() + "':  triggerClass: '"
                + getClass().getName() + " isVolatile: " + isVolatile()
                + " calendar: '" + getCalendarName() + "' misfireInstruction: "
                + getMisfireInstruction() + " nextFireTime: " + getNextFireTime();
    }

    /**
     * <p>
     * Compare the next fire time of this <code>Trigger</code> to that of
     * another.
     * </p>
     */
    public int compareTo(Object obj) {
        Trigger other = (Trigger) obj;

        Date myTime = getNextFireTime();
        Date otherTime = other.getNextFireTime();

        if (myTime == null && otherTime == null) return 0;

        if (myTime == null) return 1;

        if (otherTime == null) return -1;

        if(myTime.before(otherTime))
            return -1;

        if(myTime.after(otherTime))
            return 1;
        
        return 0;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof Trigger)) return false;

        Trigger other = (Trigger) obj;

        if (!other.getName().equals(getName())) return false;
        if (!other.getGroup().equals(getGroup())) return false;

        return true;
    }
    
    
    public int hashCode() {
        return getFullName().hashCode(); 
    }
    
    public Object clone() {
        Trigger copy;
        try {
            copy = (Trigger) super.clone();
        } catch (CloneNotSupportedException ex) {
            throw new IncompatibleClassChangeError("Not Cloneable.");
        }
        return copy;
    }

}
