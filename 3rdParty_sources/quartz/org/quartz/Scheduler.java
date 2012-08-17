
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
import java.util.List;
import java.util.Set;

import org.quartz.spi.JobFactory;

/**
 * <p>
 * This is the main interface of a Quartz Scheduler.
 * </p>
 * 
 * <p>
 * A <code>Scheduler</code> maintains a registery of <code>{@link org.quartz.JobDetail}</code>
 * s and <code>{@link Trigger}</code>s. Once registered, the <code>Scheduler</code>
 * is responible for executing <code>Job</code> s when their associated
 * <code>Trigger</code> s fire (when their scheduled time arrives).
 * </p>
 * 
 * <p>
 * <code>Scheduler</code> instances are produced by a <code>{@link SchedulerFactory}</code>.
 * A scheduler that has already been created/initialized can be found and used
 * through the same factory that produced it. After a <code>Scheduler</code>
 * has been created, it is in "stand-by" mode, and must have its 
 * <code>start()</code> method called before it will fire any <code>Job</code>s.
 * </p>
 * 
 * <p>
 * <code>Job</code> s are to be created by the 'client program', by defining
 * a class that implements the <code>{@link org.quartz.Job}</code>
 * interface. <code>{@link JobDetail}</code> objects are then created (also
 * by the client) to define a individual instances of the <code>Job</code>.
 * <code>JobDetail</code> instances can then be registered with the <code>Scheduler</code>
 * via the <code>scheduleJob(JobDetail, Trigger)</code> or <code>addJob(JobDetail, boolean)</code>
 * method.
 * </p>
 * 
 * <p>
 * <code>Trigger</code> s can then be defined to fire individual <code>Job</code>
 * instances based on given schedules. <code>SimpleTrigger</code> s are most
 * useful for one-time firings, or firing at an exact moment in time, with N
 * repeats with a given delay between them. <code>CronTrigger</code> s allow
 * scheduling based on time of day, day of week, day of month, and month of
 * year.
 * </p>
 * 
 * <p>
 * <code>Job</code> s and <code>Trigger</code> s have a name and group
 * associated with them, which should uniquely identify them within a single
 * <code>{@link Scheduler}</code>. The 'group' feature may be useful for
 * creating logical groupings or categorizations of <code>Jobs</code> s and
 * <code>Triggers</code>s. If you don't have need for assigning a group to a
 * given <code>Jobs</code> of <code>Triggers</code>, then you can use the
 * <code>DEFAULT_GROUP</code> constant defined on this interface.
 * </p>
 * 
 * <p>
 * Stored <code>Job</code> s can also be 'manually' triggered through the use
 * of the <code>triggerJob(String jobName, String jobGroup)</code> function.
 * </p>
 * 
 * <p>
 * Client programs may also be interested in the 'listener' interfaces that are
 * available from Quartz. The <code>{@link JobListener}</code> interface
 * provides notifications of <code>Job</code> executions. The <code>{@link TriggerListener}</code>
 * interface provides notifications of <code>Trigger</code> firings. The
 * <code>{@link SchedulerListener}</code> interface provides notifications of
 * <code>Scheduler</code> events and errors.
 * </p>
 * 
 * <p>
 * The setup/configuration of a <code>Scheduler</code> instance is very
 * customizable. Please consult the documentation distributed with Quartz.
 * </p>
 * 
 * @see Job
 * @see JobDetail
 * @see Trigger
 * @see JobListener
 * @see TriggerListener
 * @see SchedulerListener
 * 
 * @author James House
 * @author Sharada Jambula
 */
public interface Scheduler {

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * 
     * Constants.
     * 
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    /**
     * <p>
     * A (possibly) usefull constant that can be used for specifying the group
     * that <code>Job</code> and <code>Trigger</code> instances belong to.
     * </p>
     */
    public static final String DEFAULT_GROUP = "DEFAULT";

    /**
     * <p>
     * A constant <code>Trigger</code> group name used internally by the
     * scheduler - clients should not use the value of this constant
     * ("MANUAL_TRIGGER") for thename of a <code>Trigger</code>'s group.
     * </p>
     */
    public static final String DEFAULT_MANUAL_TRIGGERS = "MANUAL_TRIGGER";

    /**
     * <p>
     * A constant <code>Trigger</code> group name used internally by the
     * scheduler - clients should not use the value of this constant
     * ("RECOVERING_JOBS") for thename of a <code>Trigger</code>'s group.
     * </p>
     */
    public static final String DEFAULT_RECOVERY_GROUP = "RECOVERING_JOBS";

    /**
     * <p>
     * A constant <code>Trigger</code> group name used internally by the
     * scheduler - clients should not use the value of this constant
     * ("FAILED_OVER_JOBS") for thename of a <code>Trigger</code>'s group.
     * </p>
     */
    public static final String DEFAULT_FAIL_OVER_GROUP = "FAILED_OVER_JOBS";

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * 
     * Interface.
     * 
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    /**
     * <p>
     * Returns the name of the <code>Scheduler</code>.
     * </p>
     */
    public String getSchedulerName() throws SchedulerException;

    /**
     * <p>
     * Returns the instance Id of the <code>Scheduler</code>.
     * </p>
     */
    public String getSchedulerInstanceId() throws SchedulerException;

    /**
     * <p>
     * Returns the <code>SchedulerContext</code> of the <code>Scheduler</code>.
     * </p>
     */
    public SchedulerContext getContext() throws SchedulerException;

    ///////////////////////////////////////////////////////////////////////////
    ///
    /// Schedululer State Management Methods
    ///
    ///////////////////////////////////////////////////////////////////////////

    /**
     * <p>
     * Starts the <code>Scheduler</code>'s threads that fire <code>{@link Trigger}s</code>.
     * When a scheduler is first created it is in "stand-by" mode, and will not
     * fire triggers.  The scheduler can also be put into stand-by mode by
     * calling the <code>standby()</code> method. 
     * </p>
     * 
     * <p>
     * The misfire/recovery process will be started, if it is the initial call
     * to this method on this scheduler instance.
     * </p>
     * 
     * @throws SchedulerException
     *           if <code>shutdown()</code> has been called, or there is an
     *           error within the <code>Scheduler</code>.
     * 
     * @see #standby
     * @see #shutdown
     */
    public void start() throws SchedulerException;

    /**
     * <p>
     * Temporarily halts the <code>Scheduler</code>'s firing of <code>{@link Trigger}s</code>.
     * </p>
     * 
     * <p>
     * When <code>start()</code> is called (to bring the scheduler out of 
     * stand-by mode), trigger misfire instructions will NOT be applied
     * during the execution of the <code>start()</code> method - any misfires 
     * will be detected immediately afterward (by the <code>JobStore</code>'s 
     * normal process).
     * </p>
     * 
     * <p>
     * The scheduler is not destroyed, and can be re-started at any time.
     * </p>
     * 
     * @see #start()
     * @see #pauseAll()
     */
    public void standby() throws SchedulerException;

    /**
     * @deprecated replaced by better-named standby() method.
     * @see #standby()
     */
    public void pause() throws SchedulerException;

    /**
     * <p>
     * Reports whether the <code>Scheduler</code> is in stand-by mode.
     * </p>
     * 
     * @see #standby()
     * @see #start()
     */
    public boolean isInStandbyMode() throws SchedulerException;

    /**
     * @deprecated
     * @see #isInStandbyMode() 
     */
    public boolean isPaused() throws SchedulerException;

    /**
     * <p>
     * Halts the <code>Scheduler</code>'s firing of <code>{@link Trigger}s</code>,
     * and cleans up all resources associated with the Scheduler. Equivalent to
     * <code>shutdown(false)</code>.
     * </p>
     * 
     * <p>
     * The scheduler cannot be re-started.
     * </p>
     * 
     * @see #shutdown(boolean)
     */
    public void shutdown() throws SchedulerException;

    /**
     * <p>
     * Halts the <code>Scheduler</code>'s firing of <code>{@link Trigger}s</code>,
     * and cleans up all resources associated with the Scheduler.
     * </p>
     * 
     * <p>
     * The scheduler cannot be re-started.
     * </p>
     * 
     * @param waitForJobsToComplete
     *          if <code>true</code> the scheduler will not allow this method
     *          to return until all currently executing jobs have completed.
     * 
     * @see #shutdown
     */
    public void shutdown(boolean waitForJobsToComplete)
            throws SchedulerException;

    /**
     * <p>
     * Reports whether the <code>Scheduler</code> has been shutdown.
     * </p>
     */
    public boolean isShutdown() throws SchedulerException;

    /**
     * <p>
     * Get a <code>SchedulerMetaData</code> object describiing the settings
     * and capabilities of the scheduler instance.
     * </p>
     * 
     * <p>
     * Note that the data returned is an 'instantaneous' snap-shot, and that as
     * soon as it's returned, the meta data values may be different.
     * </p>
     */
    public SchedulerMetaData getMetaData() throws SchedulerException;

    /**
     * <p>
     * Return a list of <code>JobExecutionContext</code> objects that
     * represent all currently executing Jobs.
     * </p>
     * 
     * <p>
     * Note that the list returned is an 'instantaneous' snap-shot, and that as
     * soon as it's returned, the true list of executing jobs may be different.
     * Also please read the doc associated with <code>JobExecutionContext</code>-
     * especially if you're using RMI.
     * </p>
     * 
     * @see JobExecutionContext
     */
    public List getCurrentlyExecutingJobs() throws SchedulerException;

    /**
     * <p>
     * Set the <code>JobFactory</code> that will be responsible for producing 
     * instances of <code>Job</code> classes.
     * </p>
     * 
     * <p>
     * JobFactories may be of use to those wishing to have their application
     * produce <code>Job</code> instances via some special mechanism, such as to
     * give the opertunity for dependency injection.
     * </p>
     * 
     * @see org.quart.spi.JobFactory
     * @throws SchedulerException
     */
    public void setJobFactory(JobFactory factory) throws SchedulerException;
    
    ///////////////////////////////////////////////////////////////////////////
    ///
    /// Scheduling-related Methods
    ///
    ///////////////////////////////////////////////////////////////////////////

    /**
     * <p>
     * Add the given <code>{@link org.quartz.JobDetail}</code> to the
     * Scheduler, and associate the given <code>{@link Trigger}</code> with
     * it.
     * </p>
     * 
     * <p>
     * If the given Trigger does not reference any <code>Job</code>, then it
     * will be set to reference the Job passed with it into this method.
     * </p>
     * 
     * @throws SchedulerException
     *           if the Job or Trigger cannot be added to the Scheduler, or
     *           there is an internal Scheduler error.
     */
    public Date scheduleJob(JobDetail jobDetail, Trigger trigger)
            throws SchedulerException;

    /**
     * <p>
     * Schedule the given <code>{@link org.quartz.Trigger}</code> with the
     * <code>Job</code> identified by the <code>Trigger</code>'s settings.
     * </p>
     * 
     * @throws SchedulerException
     *           if the indicated Job does not exist, or the Trigger cannot be
     *           added to the Scheduler, or there is an internal Scheduler
     *           error.
     */
    public Date scheduleJob(Trigger trigger) throws SchedulerException;

    /**
     * <p>
     * Remove the indicated <code>{@link Trigger}</code> from the scheduler.
     * </p>
     */
    public boolean unscheduleJob(String triggerName, String groupName)
            throws SchedulerException;

    /**
     * <p>
     * Remove (delete) the <code>{@link org.quartz.Trigger}</code> with the
     * given name, and store the new given one - which must be associated
     * with the same job (the new trigger must have the job name & group specified) 
     * - however, the new trigger need not have the same name as the old trigger.
     * </p>
     * 
     * @param triggerName
     *          The name of the <code>Trigger</code> to be replaced.
     * @param groupName
     *          The group name of the <code>Trigger</code> to be replaced.
     * @param newTrigger
     *          The new <code>Trigger</code> to be stored.
     * @return <code>null</code> if a <code>Trigger</code> with the given
     *         name & group was not found and removed from the store, otherwise
     *         the first fire time of the newly scheduled trigger.
     */
    public Date rescheduleJob(String triggerName,
            String groupName, Trigger newTrigger) throws SchedulerException;

    
    /**
     * <p>
     * Add the given <code>Job</code> to the Scheduler - with no associated
     * <code>Trigger</code>. The <code>Job</code> will be 'dormant' until
     * it is scheduled with a <code>Trigger</code>, or <code>Scheduler.triggerJob()</code>
     * is called for it.
     * </p>
     * 
     * <p>
     * The <code>Job</code> must by definition be 'durable', if it is not,
     * SchedulerException will be thrown.
     * </p>
     * 
     * @throws SchedulerException
     *           if there is an internal Scheduler error, or if the Job is not
     *           durable, or a Job with the same name already exists, and
     *           <code>replace</code> is <code>false</code>.
     */
    public void addJob(JobDetail jobDetail, boolean replace)
            throws SchedulerException;

    /**
     * <p>
     * Delete the identified <code>Job</code> from the Scheduler - and any
     * associated <code>Trigger</code>s.
     * </p>
     * 
     * @return true if the Job was found and deleted.
     * @throws SchedulerException
     *           if there is an internal Scheduler error.
     */
    public boolean deleteJob(String jobName, String groupName)
            throws SchedulerException;

    /**
     * <p>
     * Trigger the identified <code>{@link org.quartz.JobDetail}</code>
     * (execute it now) - the generated trigger will be non-volatile.
     * </p>
     */
    public void triggerJob(String jobName, String groupName)
            throws SchedulerException;

    /**
     * <p>
     * Trigger the identified <code>{@link org.quartz.JobDetail}</code>
     * (execute it now) - the generated trigger will be volatile.
     * </p>
     */
    public void triggerJobWithVolatileTrigger(String jobName, String groupName)
            throws SchedulerException;

    /**
     * <p>
     * Trigger the identified <code>{@link org.quartz.JobDetail}</code>
     * (execute it now) - the generated trigger will be non-volatile.
     * </p>
     * 
     * @param jobName the name of the Job to trigger
     * @param groupName the group name of the Job to trigger
     * @param data the (possibly <code>null</code>) JobDataMap to be 
     * associated with the trigger that fires the job immediately. 
     */
    public void triggerJob(String jobName, String groupName, JobDataMap data)
            throws SchedulerException;

    /**
     * <p>
     * Trigger the identified <code>{@link org.quartz.JobDetail}</code>
     * (execute it now) - the generated trigger will be volatile.
     * </p>
     * 
     * @param jobName the name of the Job to trigger
     * @param groupName the group name of the Job to trigger
     * @param data the (possibly <code>null</code>) JobDataMap to be 
     * associated with the trigger that fires the job immediately. 
     */
    public void triggerJobWithVolatileTrigger(String jobName, String groupName, JobDataMap data)
            throws SchedulerException;

    /**
     * <p>
     * Pause the <code>{@link org.quartz.JobDetail}</code> with the given
     * name - by pausing all of its current <code>Trigger</code>s.
     * </p>
     * 
     * @see #resumeJob(String, String)
     */
    public void pauseJob(String jobName, String groupName)
            throws SchedulerException;

    /**
     * <p>
     * Pause all of the <code>{@link org.quartz.JobDetail}s</code> in the
     * given group - by pausing all of their <code>Trigger</code>s.
     * </p>
     * 
     * <p>
     * The Scheduler will "remember" that the group is paused, and impose the
     * pause on any new jobs that are added to the group while the group is
     * paused.
     * </p>
     * 
     * @see #resumeJobGroup(String)
     */
    public void pauseJobGroup(String groupName) throws SchedulerException;

    /**
     * <p>
     * Pause the <code>{@link Trigger}</code> with the given name.
     * </p>
     * 
     * @see #resumeTrigger(String, String)
     */
    public void pauseTrigger(String triggerName, String groupName)
            throws SchedulerException;

    /**
     * <p>
     * Pause all of the <code>{@link Trigger}s</code> in the given group.
     * </p>
     * 
     * <p>
     * The Scheduler will "remember" that the group is paused, and impose the
     * pause on any new triggers that are added to the group while the group is
     * paused.
     * </p>
     * 
     * @see #resumeTriggerGroup(String)
     */
    public void pauseTriggerGroup(String groupName) throws SchedulerException;

    /**
     * <p>
     * Resume (un-pause) the <code>{@link org.quartz.JobDetail}</code> with
     * the given name.
     * </p>
     * 
     * <p>
     * If any of the <code>Job</code>'s<code>Trigger</code> s missed one
     * or more fire-times, then the <code>Trigger</code>'s misfire
     * instruction will be applied.
     * </p>
     * 
     * @see #pauseJob(String, String)
     */
    public void resumeJob(String jobName, String groupName)
            throws SchedulerException;

    /**
     * <p>
     * Resume (un-pause) all of the <code>{@link org.quartz.JobDetail}s</code>
     * in the given group.
     * </p>
     * 
     * <p>
     * If any of the <code>Job</code> s had <code>Trigger</code> s that
     * missed one or more fire-times, then the <code>Trigger</code>'s
     * misfire instruction will be applied.
     * </p>
     * 
     * @see #pauseJobGroup(String)
     */
    public void resumeJobGroup(String groupName) throws SchedulerException;

    /**
     * <p>
     * Resume (un-pause) the <code>{@link Trigger}</code> with the given
     * name.
     * </p>
     * 
     * <p>
     * If the <code>Trigger</code> missed one or more fire-times, then the
     * <code>Trigger</code>'s misfire instruction will be applied.
     * </p>
     * 
     * @see #pauseTrigger(String, String)
     */
    public void resumeTrigger(String triggerName, String groupName)
            throws SchedulerException;

    /**
     * <p>
     * Resume (un-pause) all of the <code>{@link Trigger}s</code> in the
     * given group.
     * </p>
     * 
     * <p>
     * If any <code>Trigger</code> missed one or more fire-times, then the
     * <code>Trigger</code>'s misfire instruction will be applied.
     * </p>
     * 
     * @see #pauseTriggerGroup(String)
     */
    public void resumeTriggerGroup(String groupName) throws SchedulerException;

    /**
     * <p>
     * Pause all triggers - similar to calling <code>pauseTriggerGroup(group)</code>
     * on every group, however, after using this method <code>resumeAll()</code> 
     * must be called to clear the scheduler's state of 'remembering' that all 
     * new triggers will be paused as they are added. 
     * </p>
     * 
     * <p>
     * When <code>resumeAll()</code> is called (to un-pause), trigger misfire
     * instructions WILL be applied.
     * </p>
     * 
     * @see #resumeAll()
     * @see #pauseTriggerGroup(String)
     * @see #standby()
     */
    public void pauseAll() throws SchedulerException;

    /**
     * <p>
     * Resume (un-pause) all triggers - similar to calling 
     * <code>resumeTriggerGroup(group)</code> on every group.
     * </p>
     * 
     * <p>
     * If any <code>Trigger</code> missed one or more fire-times, then the
     * <code>Trigger</code>'s misfire instruction will be applied.
     * </p>
     * 
     * @see #pauseAll()
     */
    public void resumeAll() throws SchedulerException;

    /**
     * <p>
     * Get the names of all known <code>{@link org.quartz.JobDetail}</code>
     * groups.
     * </p>
     */
    public String[] getJobGroupNames() throws SchedulerException;

    /**
     * <p>
     * Get the names of all the <code>{@link org.quartz.JobDetail}s</code>
     * in the given group.
     * </p>
     */
    public String[] getJobNames(String groupName) throws SchedulerException;

    /**
     * <p>
     * Get all <code>{@link Trigger}</code> s that are associated with the
     * identified <code>{@link org.quartz.JobDetail}</code>.
     * </p>
     */
    public Trigger[] getTriggersOfJob(String jobName, String groupName)
            throws SchedulerException;

    /**
     * <p>
     * Get the names of all known <code>{@link Trigger}</code> groups.
     * </p>
     */
    public String[] getTriggerGroupNames() throws SchedulerException;

    /**
     * <p>
     * Get the names of all the <code>{@link Trigger}s</code> in the given
     * group.
     * </p>
     */
    public String[] getTriggerNames(String groupName) throws SchedulerException;

    /**
     * <p>
     * Get the names of all <code>{@link Trigger}</code> groups that are paused.
     * </p>
     * 
     * @return
     * @throws SchedulerException
     */
    public Set getPausedTriggerGroups() throws SchedulerException;
    
    /**
     * <p>
     * Get the <code>{@link JobDetail}</code> for the <code>Job</code>
     * instance with the given name and group.
     * </p>
     */
    public JobDetail getJobDetail(String jobName, String jobGroup)
            throws SchedulerException;

    /**
     * <p>
     * Get the <code>{@link Trigger}</code> instance with the given name and
     * group.
     * </p>
     */
    public Trigger getTrigger(String triggerName, String triggerGroup)
            throws SchedulerException;

    /**
     * <p>
     * Get the current state of the identified <code>{@link Trigger}</code>.
     * </p>
     * 
     * @see Trigger#STATE_NORMAL
     * @see Trigger#STATE_PAUSED
     * @see Trigger#STATE_COMPLETE
     * @see Trigger#STATE_ERROR
     * @see Trigger#STATE_BLOCKED
     * @see Trigger#STATE_NONE
     */
    public int getTriggerState(String triggerName, String triggerGroup)
            throws SchedulerException;

    /**
     * <p>
     * Add (register) the given <code>Calendar</code> to the Scheduler.
     * </p>
     * 
     * @param updateTriggers whether or not to update existing triggers that
     * referenced the already existing calendar so that they are 'correct'
     * based on the new trigger. 
     * 
     *  
     * @throws SchedulerException
     *           if there is an internal Scheduler error, or a Calendar with
     *           the same name already exists, and <code>replace</code> is
     *           <code>false</code>.
     */
    public void addCalendar(String calName, Calendar calendar, boolean replace, boolean updateTriggers)
            throws SchedulerException;

    /**
     * <p>
     * Delete the identified <code>Calendar</code> from the Scheduler.
     * </p>
     * 
     * @return true if the Calendar was found and deleted.
     * @throws SchedulerException
     *           if there is an internal Scheduler error.
     */
    public boolean deleteCalendar(String calName) throws SchedulerException;

    /**
     * <p>
     * Get the <code>{@link Calendar}</code> instance with the given name.
     * </p>
     */
    public Calendar getCalendar(String calName) throws SchedulerException;

    /**
     * <p>
     * Get the names of all registered <code>{@link Calendar}s</code>.
     * </p>
     */
    public String[] getCalendarNames() throws SchedulerException;

    /**
     * <p>
     * Request the interruption of all currently executing instances of the 
     * identified <code>Job</code>, which must be an implementor of the 
     * <code>InterruptableJob</code> interface.
     * </p>
     * 
     * <p>
     * If more than one instance of the identified job is currently executing,
     * the <code>InterruptableJob#interrupt()</code> method will be called on
     * each instance.  However, there is a limitation that in the case that  
     * <code>interrupt()</code> on one instances throws an exception, all 
     * remaining  instances (that have not yet been interrupted) will not have 
     * their <code>interrupt()</code> method called.
     * </p>
     * 
     * <p>
     * If you wish to interrupt a specific instance of a job (when more than
     * one is executing) you can do so by calling 
     * <code>{@link #getCurrentlyExecutingJobs()}</code> to obtain a handle 
     * to the job instance, and then invoke <code>interrupt()</code> on it
     * yourself.
     * </p>
     * 
     * @param jobName
     * @param groupName
     * @return true is at least one instance of the identified job was found
     * and interrupted.
     * @throws UnableToInterruptJobException if the job does not implement
     * <code>InterruptableJob</code>, or there is an exception while 
     * interrupting the job.
     * @see InterruptableJob#interrupt()
     * @see #getCurrentlyExecutingJobs()
     */
    public boolean interrupt(String jobName, String groupName) throws UnableToInterruptJobException;
    
    ///////////////////////////////////////////////////////////////////////////
    ///
    /// Listener-related Methods
    ///
    ///////////////////////////////////////////////////////////////////////////

    /**
     * <p>
     * Add the given <code>{@link JobListener}</code> to the <code>Scheduler</code>'s
     * <i>global</i> list.
     * </p>
     * 
     * <p>
     * Listeners in the 'global' list receive notification of execution events
     * for ALL <code>{@link org.quartz.JobDetail}</code>s.
     * </p>
     */
    public void addGlobalJobListener(JobListener jobListener)
            throws SchedulerException;

    /**
     * <p>
     * Add the given <code>{@link JobListener}</code> to the <code>Scheduler</code>'s
     * list, of registered <code>JobListener</code>s.
     */
    public void addJobListener(JobListener jobListener)
            throws SchedulerException;

    /**
     * <p>
     * Remove the given <code>{@link JobListener}</code> from the <code>Scheduler</code>'s
     * list of <i>global</i> listeners.
     * </p>
     * 
     * @return true if the identifed listener was found in the list, and
     *         removed.
     */
    public boolean removeGlobalJobListener(JobListener jobListener)
            throws SchedulerException;

    /**
     * <p>
     * Remove the identifed <code>{@link JobListener}</code> from the <code>Scheduler</code>'s
     * list of registered listeners.
     * </p>
     * 
     * @return true if the identifed listener was found in the list, and
     *         removed.
     */
    public boolean removeJobListener(String name) throws SchedulerException;

    /**
     * <p>
     * Get a List containing all of the <code>{@link JobListener}</code> s in
     * the <code>Scheduler</code>'s<i>global</i> list.
     * </p>
     */
    public List getGlobalJobListeners() throws SchedulerException;

    /**
     * <p>
     * Get a Set containing the names of all the <i>non-global</i><code>{@link JobListener}</code>
     * s registered with the <code>Scheduler</code>.
     * </p>
     */
    public Set getJobListenerNames() throws SchedulerException;

    /**
     * <p>
     * Get the <i>non-global</i><code>{@link JobListener}</code> that has
     * the given name.
     * </p>
     */
    public JobListener getJobListener(String name) throws SchedulerException;

    /**
     * <p>
     * Add the given <code>{@link TriggerListener}</code> to the <code>Scheduler</code>'s
     * <i>global</i> list.
     * </p>
     * 
     * <p>
     * Listeners in the 'global' list receive notification of execution events
     * for ALL <code>{@link Trigger}</code>s.
     * </p>
     */
    public void addGlobalTriggerListener(TriggerListener triggerListener)
            throws SchedulerException;

    /**
     * <p>
     * Add the given <code>{@link TriggerListener}</code> to the <code>Scheduler</code>'s
     * list, of registered <code>TriggerListener</code>s.
     */
    public void addTriggerListener(TriggerListener triggerListener)
            throws SchedulerException;

    /**
     * <p>
     * Remove the given <code>{@link TriggerListener}</code> from the <code>Scheduler</code>'s
     * list of <i>global</i> listeners.
     * </p>
     * 
     * @return true if the identifed listener was found in the list, and
     *         removed.
     */
    public boolean removeGlobalTriggerListener(TriggerListener triggerListener)
            throws SchedulerException;

    /**
     * <p>
     * Remove the identifed <code>{@link TriggerListener}</code> from the
     * <code>Scheduler</code>'s list of registered listeners.
     * </p>
     * 
     * @return true if the identifed listener was found in the list, and
     *         removed.
     */
    public boolean removeTriggerListener(String name) throws SchedulerException;

    /**
     * <p>
     * Get a List containing all of the <code>{@link TriggerListener}</code>
     * s in the <code>Scheduler</code>'s<i>global</i> list.
     * </p>
     */
    public List getGlobalTriggerListeners() throws SchedulerException;

    /**
     * <p>
     * Get a Set containing the names of all the <i>non-global</i><code>{@link TriggerListener}</code>
     * s registered with the <code>Scheduler</code>.
     * </p>
     */
    public Set getTriggerListenerNames() throws SchedulerException;

    /**
     * <p>
     * Get the <i>non-global</i><code>{@link TriggerListener}</code> that
     * has the given name.
     * </p>
     */
    public TriggerListener getTriggerListener(String name)
            throws SchedulerException;

    /**
     * <p>
     * Register the given <code>{@link SchedulerListener}</code> with the
     * <code>Scheduler</code>.
     * </p>
     */
    public void addSchedulerListener(SchedulerListener schedulerListener)
            throws SchedulerException;

    /**
     * <p>
     * Remove the given <code>{@link SchedulerListener}</code> from the
     * <code>Scheduler</code>.
     * </p>
     * 
     * @return true if the identifed listener was found in the list, and
     *         removed.
     */
    public boolean removeSchedulerListener(SchedulerListener schedulerListener)
            throws SchedulerException;

    /**
     * <p>
     * Get a List containing all of the <code>{@link SchedulerListener}</code>
     * s registered with the <code>Scheduler</code>.
     * </p>
     */
    public List getSchedulerListeners() throws SchedulerException;


}
