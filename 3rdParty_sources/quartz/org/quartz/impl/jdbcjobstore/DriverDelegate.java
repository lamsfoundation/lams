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
package org.quartz.impl.jdbcjobstore;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

import org.quartz.Calendar;
import org.quartz.CronTrigger;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.spi.ClassLoadHelper;
import org.quartz.utils.Key;
import org.quartz.utils.TriggerStatus;

/**
 * <p>
 * This is the base interface for all driver delegate classes.
 * </p>
 * 
 * <p>
 * This interface is very similar to the <code>{@link
 * org.quartz.spi.JobStore}</code>
 * interface except each method has an additional <code>{@link java.sql.Connection}</code>
 * parameter.
 * </p>
 * 
 * <p>
 * Unless a database driver has some <strong>extremely-DB-specific</strong>
 * requirements, any DriverDelegate implementation classes should extend the
 * <code>{@link org.quartz.impl.jdbcjobstore.StdJDBCDelegate}</code> class.
 * </p>
 * 
 * @author <a href="mailto:jeff@binaryfeed.org">Jeffrey Wescott</a>
 * @author James House
 */
public interface DriverDelegate {

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * 
     * Interface.
     * 
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    //---------------------------------------------------------------------------
    // startup / recovery
    //---------------------------------------------------------------------------
    /**
     * <p>
     * Update all triggers having one of the two given states, to the given new
     * state.
     * </p>
     * 
     * @param conn
     *          the DB Connection
     * @param newState
     *          the new state for the triggers
     * @param oldState1
     *          the first old state to update
     * @param oldState2
     *          the second old state to update
     * @return number of rows updated
     */
    public int updateTriggerStatesFromOtherStates(Connection conn,
            String newState, String oldState1, String oldState2)
            throws SQLException;

    /**
     * <p>
     * Get the names of all of the triggers that have misfired - according to
     * the given timestamp.
     * </p>
     * 
     * @param conn
     *          the DB Connection
     * @return an array of <code>{@link
     * org.quartz.utils.Key}</code> objects
     */
    public Key[] selectMisfiredTriggers(Connection conn, long ts)
            throws SQLException;

    /**
     * <p>
     * Get the names of all of the triggers in the given state that have
     * misfired - according to the given timestamp.
     * </p>
     * 
     * @param conn
     *          the DB Connection
     * @return an array of <code>{@link
     * org.quartz.utils.Key}</code> objects
     */
    public Key[] selectMisfiredTriggersInState(Connection conn, String state,
            long ts) throws SQLException;

    /**
     * <p>
     * Get the names of all of the triggers in the given group and state that
     * have misfired - according to the given timestamp.
     * </p>
     * 
     * @param conn
     *          the DB Connection
     * @return an array of <code>{@link
     * org.quartz.utils.Key}</code> objects
     */
    public Key[] selectMisfiredTriggersInGroupInState(Connection conn,
            String groupName, String state, long ts) throws SQLException;

    /**
     * <p>
     * Select all of the triggers for jobs that are requesting recovery. The
     * returned trigger objects will have unique "recoverXXX" trigger names and
     * will be in the <code>{@link
     * org.quartz.Scheduler}.DEFAULT_RECOVERY_GROUP</code>
     * trigger group.
     * </p>
     * 
     * <p>
     * In order to preserve the ordering of the triggers, the fire time will be
     * set from the <code>COL_FIRED_TIME</code> column in the <code>TABLE_FIRED_TRIGGERS</code>
     * table. The caller is responsible for calling <code>computeFirstFireTime</code>
     * on each returned trigger. It is also up to the caller to insert the
     * returned triggers to ensure that they are fired.
     * </p>
     * 
     * @param conn
     *          the DB Connection
     * @return an array of <code>{@link org.quartz.Trigger}</code> objects
     */
    public Trigger[] selectTriggersForRecoveringJobs(Connection conn)
            throws SQLException, IOException, ClassNotFoundException;

    /**
     * <p>
     * Delete all fired triggers.
     * </p>
     * 
     * @param conn
     *          the DB Connection
     * @return the number of rows deleted
     */
    public int deleteFiredTriggers(Connection conn) throws SQLException;

    /**
     * <p>
     * Delete all fired triggers of the given instance.
     * </p>
     * 
     * @param conn
     *          the DB Connection
     * @return the number of rows deleted
     */
    public int deleteFiredTriggers(Connection conn, String instanceId)
            throws SQLException;

    /**
     * <p>
     * Delete all volatile fired triggers.
     * </p>
     * 
     * @param conn
     *          the DB Connection
     * @return the number of rows deleted
     */
    public int deleteVolatileFiredTriggers(Connection conn) throws SQLException;

    /**
     * <p>
     * Get the names of all of the triggers that are volatile.
     * </p>
     * 
     * @param conn
     *          the DB Connection
     * @return an array of <code>{@link
     * org.quartz.utils.Key}</code> objects
     */
    public Key[] selectVolatileTriggers(Connection conn) throws SQLException;

    /**
     * <p>
     * Get the names of all of the jobs that are volatile.
     * </p>
     * 
     * @param conn
     *          the DB Connection
     * @return an array of <code>{@link
     * org.quartz.utils.Key}</code> objects
     */
    public Key[] selectVolatileJobs(Connection conn) throws SQLException;

    //---------------------------------------------------------------------------
    // jobs
    //---------------------------------------------------------------------------

    /**
     * <p>
     * Insert the job detail record.
     * </p>
     * 
     * @param conn
     *          the DB Connection
     * @param job
     *          the job to insert
     * @return number of rows inserted
     * @throws IOException
     *           if there were problems serializing the JobDataMap
     */
    public int insertJobDetail(Connection conn, JobDetail job)
            throws IOException, SQLException;

    /**
     * <p>
     * Update the job detail record.
     * </p>
     * 
     * @param conn
     *          the DB Connection
     * @param job
     *          the job to update
     * @return number of rows updated
     * @throws IOException
     *           if there were problems serializing the JobDataMap
     */
    public int updateJobDetail(Connection conn, JobDetail job)
            throws IOException, SQLException;

    /**
     * <p>
     * Get the names of all of the triggers associated with the given job.
     * </p>
     * 
     * @param conn
     *          the DB Connection
     * @param jobName
     *          the job name
     * @param groupName
     *          the job group
     * @return an array of <code>{@link
     * org.quartz.utils.Key}</code> objects
     */
    public Key[] selectTriggerNamesForJob(Connection conn, String jobName,
            String groupName) throws SQLException;

    /**
     * <p>
     * Delete all job listeners for the given job.
     * </p>
     * 
     * @param conn
     *          the DB Connection
     * @param jobName
     *          the name of the job
     * @param groupName
     *          the group containing the job
     * @return the number of rows deleted
     */
    public int deleteJobListeners(Connection conn, String jobName,
            String groupName) throws SQLException;

    /**
     * <p>
     * Delete the job detail record for the given job.
     * </p>
     * 
     * @param conn
     *          the DB Connection
     * @param jobName
     *          the name of the job
     * @param groupName
     *          the group containing the job
     * @return the number of rows deleted
     */
    public int deleteJobDetail(Connection conn, String jobName, String groupName)
            throws SQLException;

    /**
     * <p>
     * Check whether or not the given job is stateful.
     * </p>
     * 
     * @param conn
     *          the DB Connection
     * @param jobName
     *          the name of the job
     * @param groupName
     *          the group containing the job
     * @return true if the job exists and is stateful, false otherwise
     */
    public boolean isJobStateful(Connection conn, String jobName,
            String groupName) throws SQLException;

    /**
     * <p>
     * Check whether or not the given job exists.
     * </p>
     * 
     * @param conn
     *          the DB Connection
     * @param jobName
     *          the name of the job
     * @param groupName
     *          the group containing the job
     * @return true if the job exists, false otherwise
     */
    public boolean jobExists(Connection conn, String jobName, String groupName)
            throws SQLException;

    /**
     * <p>
     * Update the job data map for the given job.
     * </p>
     * 
     * @param conn
     *          the DB Connection
     * @param job
     *          the job to update
     * @return the number of rows updated
     * @throws IOException
     *           if there were problems serializing the JobDataMap
     */
    public int updateJobData(Connection conn, JobDetail job)
            throws IOException, SQLException;

    /**
     * <p>
     * Associate a listener with a job.
     * </p>
     * 
     * @param conn
     *          the DB Connection
     * @param job
     *          the job to associate with the listener
     * @param listener
     *          the listener to insert
     * @return the number of rows inserted
     */
    public int insertJobListener(Connection conn, JobDetail job, String listener)
            throws SQLException;

    /**
     * <p>
     * Get all of the listeners for a given job.
     * </p>
     * 
     * @param conn
     *          the DB Connection
     * @param jobName
     *          the job name whose listeners are wanted
     * @param groupName
     *          the group containing the job
     * @return array of <code>String</code> listener names
     */
    public String[] selectJobListeners(Connection conn, String jobName,
            String groupName) throws SQLException;

    /**
     * <p>
     * Select the JobDetail object for a given job name / group name.
     * </p>
     * 
     * @param conn
     *          the DB Connection
     * @param jobName
     *          the job name whose listeners are wanted
     * @param groupName
     *          the group containing the job
     * @return the populated JobDetail object
     * @throws ClassNotFoundException
     *           if a class found during deserialization cannot be found or if
     *           the job class could not be found
     * @throws IOException
     *           if deserialization causes an error
     */
    public JobDetail selectJobDetail(Connection conn, String jobName,
            String groupName, ClassLoadHelper loadHelper)
            throws ClassNotFoundException, IOException, SQLException;

    /**
     * <p>
     * Select the total number of jobs stored.
     * </p>
     * 
     * @param conn
     *          the DB Connection
     * @return the total number of jobs stored
     */
    public int selectNumJobs(Connection conn) throws SQLException;

    /**
     * <p>
     * Select all of the job group names that are stored.
     * </p>
     * 
     * @param conn
     *          the DB Connection
     * @return an array of <code>String</code> group names
     */
    public String[] selectJobGroups(Connection conn) throws SQLException;

    /**
     * <p>
     * Select all of the jobs contained in a given group.
     * </p>
     * 
     * @param conn
     *          the DB Connection
     * @param groupName
     *          the group containing the jobs
     * @return an array of <code>String</code> job names
     */
    public String[] selectJobsInGroup(Connection conn, String groupName)
            throws SQLException;

    //---------------------------------------------------------------------------
    // triggers
    //---------------------------------------------------------------------------

    /**
     * <p>
     * Insert the base trigger data.
     * </p>
     * 
     * @param conn
     *          the DB Connection
     * @param trigger
     *          the trigger to insert
     * @param state
     *          the state that the trigger should be stored in
     * @return the number of rows inserted
     */
    public int insertTrigger(Connection conn, Trigger trigger, String state,
            JobDetail jobDetail) throws SQLException, IOException;

    /**
     * <p>
     * Insert the simple trigger data.
     * </p>
     * 
     * @param conn
     *          the DB Connection
     * @param trigger
     *          the trigger to insert
     * @return the number of rows inserted
     */
    public int insertSimpleTrigger(Connection conn, SimpleTrigger trigger)
            throws SQLException;

    /**
     * <p>
     * Insert the blob trigger data.
     * </p>
     * 
     * @param conn
     *          the DB Connection
     * @param trigger
     *          the trigger to insert
     * @return the number of rows inserted
     */
    public int insertBlobTrigger(Connection conn, Trigger trigger)
            throws SQLException, IOException;

    /**
     * <p>
     * Insert the cron trigger data.
     * </p>
     * 
     * @param conn
     *          the DB Connection
     * @param trigger
     *          the trigger to insert
     * @return the number of rows inserted
     */
    public int insertCronTrigger(Connection conn, CronTrigger trigger)
            throws SQLException;

    /**
     * <p>
     * Update the base trigger data.
     * </p>
     * 
     * @param conn
     *          the DB Connection
     * @param trigger
     *          the trigger to insert
     * @param state
     *          the state that the trigger should be stored in
     * @return the number of rows updated
     */
    public int updateTrigger(Connection conn, Trigger trigger, String state,
            JobDetail jobDetail) throws SQLException, IOException;

    /**
     * <p>
     * Update the simple trigger data.
     * </p>
     * 
     * @param conn
     *          the DB Connection
     * @param trigger
     *          the trigger to insert
     * @return the number of rows updated
     */
    public int updateSimpleTrigger(Connection conn, SimpleTrigger trigger)
            throws SQLException;

    /**
     * <p>
     * Update the cron trigger data.
     * </p>
     * 
     * @param conn
     *          the DB Connection
     * @param trigger
     *          the trigger to insert
     * @return the number of rows updated
     */
    public int updateCronTrigger(Connection conn, CronTrigger trigger)
            throws SQLException;

    /**
     * <p>
     * Update the blob trigger data.
     * </p>
     * 
     * @param conn
     *          the DB Connection
     * @param trigger
     *          the trigger to insert
     * @return the number of rows updated
     */
    public int updateBlobTrigger(Connection conn, Trigger trigger)
            throws SQLException, IOException;

    /**
     * <p>
     * Check whether or not a trigger exists.
     * </p>
     * 
     * @param conn
     *          the DB Connection
     * @param triggerName
     *          the name of the trigger
     * @param groupName
     *          the group containing the trigger
     * @return the number of rows updated
     */
    public boolean triggerExists(Connection conn, String triggerName,
            String groupName) throws SQLException;

    /**
     * <p>
     * Update the state for a given trigger.
     * </p>
     * 
     * @param conn
     *          the DB Connection
     * @param triggerName
     *          the name of the trigger
     * @param groupName
     *          the group containing the trigger
     * @param state
     *          the new state for the trigger
     * @return the number of rows updated
     */
    public int updateTriggerState(Connection conn, String triggerName,
            String groupName, String state) throws SQLException;

    /**
     * <p>
     * Update the given trigger to the given new state, if it is in the given
     * old state.
     * </p>
     * 
     * @param conn
     *          the DB connection
     * @param triggerName
     *          the name of the trigger
     * @param groupName
     *          the group containing the trigger
     * @param newState
     *          the new state for the trigger
     * @param oldState
     *          the old state the trigger must be in
     * @return int the number of rows updated
     * @throws SQLException
     */
    public int updateTriggerStateFromOtherState(Connection conn,
            String triggerName, String groupName, String newState,
            String oldState) throws SQLException;

    /**
     * <p>
     * Update the given trigger to the given new state, if it is one of the
     * given old states.
     * </p>
     * 
     * @param conn
     *          the DB connection
     * @param triggerName
     *          the name of the trigger
     * @param groupName
     *          the group containing the trigger
     * @param newState
     *          the new state for the trigger
     * @param oldState1
     *          one of the old state the trigger must be in
     * @param oldState2
     *          one of the old state the trigger must be in
     * @param oldState3
     *          one of the old state the trigger must be in
     * @return int the number of rows updated
     * @throws SQLException
     */
    public int updateTriggerStateFromOtherStates(Connection conn,
            String triggerName, String groupName, String newState,
            String oldState1, String oldState2, String oldState3)
            throws SQLException;

    /**
     * <p>
     * Update the all triggers to the given new state, if they are in one of
     * the given old states AND its next fire time is before the given time.
     * </p>
     * 
     * @param conn
     *          the DB connection
     * @param newState
     *          the new state for the trigger
     * @param oldState1
     *          one of the old state the trigger must be in
     * @param oldState2
     *          one of the old state the trigger must be in
     * @param time
     *          the time before which the trigger's next fire time must be
     * @return int the number of rows updated
     * @throws SQLException
     */
    public int updateTriggerStateFromOtherStatesBeforeTime(Connection conn,
            String newState, String oldState1, String oldState2, long time)
            throws SQLException;

    /**
     * <p>
     * Update all triggers in the given group to the given new state, if they
     * are in one of the given old states.
     * </p>
     * 
     * @param conn
     *          the DB connection
     * @param groupName
     *          the group containing the trigger
     * @param newState
     *          the new state for the trigger
     * @param oldState1
     *          one of the old state the trigger must be in
     * @param oldState2
     *          one of the old state the trigger must be in
     * @param oldState3
     *          one of the old state the trigger must be in
     * @return int the number of rows updated
     * @throws SQLException
     */
    public int updateTriggerGroupStateFromOtherStates(Connection conn,
            String groupName, String newState, String oldState1,
            String oldState2, String oldState3) throws SQLException;

    /**
     * <p>
     * Update all of the triggers of the given group to the given new state, if
     * they are in the given old state.
     * </p>
     * 
     * @param conn
     *          the DB connection
     * @param groupName
     *          the group containing the triggers
     * @param newState
     *          the new state for the trigger group
     * @param oldState
     *          the old state the triggers must be in
     * @return int the number of rows updated
     * @throws SQLException
     */
    public int updateTriggerGroupStateFromOtherState(Connection conn,
            String groupName, String newState, String oldState)
            throws SQLException;

    /**
     * <p>
     * Update the states of all triggers associated with the given job.
     * </p>
     * 
     * @param conn
     *          the DB Connection
     * @param jobName
     *          the name of the job
     * @param groupName
     *          the group containing the job
     * @param state
     *          the new state for the triggers
     * @return the number of rows updated
     */
    public int updateTriggerStatesForJob(Connection conn, String jobName,
            String groupName, String state) throws SQLException;

    /**
     * <p>
     * Update the states of any triggers associated with the given job, that
     * are the given current state.
     * </p>
     * 
     * @param conn
     *          the DB Connection
     * @param jobName
     *          the name of the job
     * @param groupName
     *          the group containing the job
     * @param state
     *          the new state for the triggers
     * @param oldState
     *          the old state of the triggers
     * @return the number of rows updated
     */
    public int updateTriggerStatesForJobFromOtherState(Connection conn,
            String jobName, String groupName, String state, String oldState)
            throws SQLException;

    /**
     * <p>
     * Delete all of the listeners associated with a given trigger.
     * </p>
     * 
     * @param conn
     *          the DB Connection
     * @param triggerName
     *          the name of the trigger whose listeners will be deleted
     * @param groupName
     *          the name of the group containing the trigger
     * @return the number of rows deleted
     */
    public int deleteTriggerListeners(Connection conn, String triggerName,
            String groupName) throws SQLException;

    /**
     * <p>
     * Associate a listener with the given trigger.
     * </p>
     * 
     * @param conn
     *          the DB Connection
     * @param trigger
     *          the trigger
     * @param listener
     *          the name of the listener to associate with the trigger
     * @return the number of rows inserted
     */
    public int insertTriggerListener(Connection conn, Trigger trigger,
            String listener) throws SQLException;

    /**
     * <p>
     * Select the listeners associated with a given trigger.
     * </p>
     * 
     * @param conn
     *          the DB Connection
     * @param triggerName
     *          the name of the trigger
     * @param groupName
     *          the group containing the trigger
     * @return array of <code>String</code> trigger listener names
     */
    public String[] selectTriggerListeners(Connection conn, String triggerName,
            String groupName) throws SQLException;

    /**
     * <p>
     * Delete the simple trigger data for a trigger.
     * </p>
     * 
     * @param conn
     *          the DB Connection
     * @param triggerName
     *          the name of the trigger
     * @param groupName
     *          the group containing the trigger
     * @return the number of rows deleted
     */
    public int deleteSimpleTrigger(Connection conn, String triggerName,
            String groupName) throws SQLException;

    /**
     * <p>
     * Delete the BLOB trigger data for a trigger.
     * </p>
     * 
     * @param conn
     *          the DB Connection
     * @param triggerName
     *          the name of the trigger
     * @param groupName
     *          the group containing the trigger
     * @return the number of rows deleted
     */
    public int deleteBlobTrigger(Connection conn, String triggerName,
            String groupName) throws SQLException;

    /**
     * <p>
     * Delete the cron trigger data for a trigger.
     * </p>
     * 
     * @param conn
     *          the DB Connection
     * @param triggerName
     *          the name of the trigger
     * @param groupName
     *          the group containing the trigger
     * @return the number of rows deleted
     */
    public int deleteCronTrigger(Connection conn, String triggerName,
            String groupName) throws SQLException;

    /**
     * <p>
     * Delete the base trigger data for a trigger.
     * </p>
     * 
     * @param conn
     *          the DB Connection
     * @param triggerName
     *          the name of the trigger
     * @param groupName
     *          the group containing the trigger
     * @return the number of rows deleted
     */
    public int deleteTrigger(Connection conn, String triggerName,
            String groupName) throws SQLException;

    /**
     * <p>
     * Select the number of triggers associated with a given job.
     * </p>
     * 
     * @param conn
     *          the DB Connection
     * @param jobName
     *          the name of the job
     * @param groupName
     *          the group containing the job
     * @return the number of triggers for the given job
     */
    public int selectNumTriggersForJob(Connection conn, String jobName,
            String groupName) throws SQLException;

    /**
     * <p>
     * Select the job to which the trigger is associated.
     * </p>
     * 
     * @param conn
     *          the DB Connection
     * @param triggerName
     *          the name of the trigger
     * @param groupName
     *          the group containing the trigger
     * @return the <code>{@link org.quartz.JobDetail}</code> object
     *         associated with the given trigger
     */
    public JobDetail selectJobForTrigger(Connection conn, String triggerName,
            String groupName, ClassLoadHelper loadHelper) 
        throws ClassNotFoundException, SQLException;

    /**
     * <p>
     * Select the stateful jobs which are referenced by triggers in the given
     * trigger group.
     * </p>
     * 
     * @param conn
     *          the DB Connection
     * @param groupName
     *          the trigger group
     * @return a List of Keys to jobs.
     */
    public List selectStatefulJobsOfTriggerGroup(Connection conn,
            String groupName) throws SQLException;

    /**
     * <p>
     * Select the triggers for a job
     * </p>
     * 
     * @param conn
     *          the DB Connection
     * @param jobName
     *          the name of the trigger
     * @param groupName
     *          the group containing the trigger
     * @return an array of <code>(@link org.quartz.Trigger)</code> objects
     *         associated with a given job.
     * @throws SQLException
     */
    public Trigger[] selectTriggersForJob(Connection conn, String jobName,
            String groupName) throws SQLException, ClassNotFoundException,
            IOException;

    /**
     * <p>
     * Select the triggers for a calendar
     * </p>
     * 
     * @param conn
     *          the DB Connection
     * @param jobName
     *          the name of the trigger
     * @param groupName
     *          the group containing the trigger
     * @return an array of <code>(@link org.quartz.Trigger)</code> objects
     *         associated with a given job.
     * @throws SQLException
     */
    public Trigger[] selectTriggersForCalendar(Connection conn, String calName)
            throws SQLException, ClassNotFoundException, IOException;
    /**
     * <p>
     * Select a trigger.
     * </p>
     * 
     * @param conn
     *          the DB Connection
     * @param triggerName
     *          the name of the trigger
     * @param groupName
     *          the group containing the trigger
     * @return the <code>{@link org.quartz.Trigger}</code> object
     */
    public Trigger selectTrigger(Connection conn, String triggerName,
            String groupName) throws SQLException, ClassNotFoundException,
            IOException;

    /**
     * <p>
     * Select a trigger's JobDataMap.
     * </p>
     * 
     * @param conn
     *          the DB Connection
     * @param triggerName
     *          the name of the trigger
     * @param groupName
     *          the group containing the trigger
     * @return the <code>{@link org.quartz.JobDataMap}</code> of the Trigger,
     * never null, but possibly empty.
     */
    public JobDataMap selectTriggerJobDataMap(Connection conn, String triggerName,
            String groupName) throws SQLException, ClassNotFoundException,
            IOException;

    /**
     * <p>
     * Select a trigger' state value.
     * </p>
     * 
     * @param conn
     *          the DB Connection
     * @param triggerName
     *          the name of the trigger
     * @param groupName
     *          the group containing the trigger
     * @return the <code>{@link org.quartz.Trigger}</code> object
     */
    public String selectTriggerState(Connection conn, String triggerName,
            String groupName) throws SQLException;

    /**
     * <p>
     * Select a trigger' status (state & next fire time).
     * </p>
     * 
     * @param conn
     *          the DB Connection
     * @param triggerName
     *          the name of the trigger
     * @param groupName
     *          the group containing the trigger
     * @return a <code>TriggerStatus</code> object, or null
     */
    public TriggerStatus selectTriggerStatus(Connection conn,
            String triggerName, String groupName) throws SQLException;

    /**
     * <p>
     * Select the total number of triggers stored.
     * </p>
     * 
     * @param conn
     *          the DB Connection
     * @return the total number of triggers stored
     */
    public int selectNumTriggers(Connection conn) throws SQLException;

    /**
     * <p>
     * Select all of the trigger group names that are stored.
     * </p>
     * 
     * @param conn
     *          the DB Connection
     * @return an array of <code>String</code> group names
     */
    public String[] selectTriggerGroups(Connection conn) throws SQLException;

    /**
     * <p>
     * Select all of the triggers contained in a given group.
     * </p>
     * 
     * @param conn
     *          the DB Connection
     * @param groupName
     *          the group containing the triggers
     * @return an array of <code>String</code> trigger names
     */
    public String[] selectTriggersInGroup(Connection conn, String groupName)
            throws SQLException;

    /**
     * <p>
     * Select all of the triggers in a given state.
     * </p>
     * 
     * @param conn
     *          the DB Connection
     * @param state
     *          the state the triggers must be in
     * @return an array of trigger <code>Key</code> s
     */
    public Key[] selectTriggersInState(Connection conn, String state)
            throws SQLException;

    public int insertPausedTriggerGroup(Connection conn, String groupName)
            throws SQLException;

    public int deletePausedTriggerGroup(Connection conn, String groupName)
            throws SQLException;

    public int deleteAllPausedTriggerGroups(Connection conn)
            throws SQLException;

    public boolean isTriggerGroupPaused(Connection conn, String groupName)
            throws SQLException;

    public Set selectPausedTriggerGroups(Connection conn)
        throws SQLException;
    
    public boolean isExistingTriggerGroup(Connection conn, String groupName)
            throws SQLException;

    //---------------------------------------------------------------------------
    // calendars
    //---------------------------------------------------------------------------

    /**
     * <p>
     * Insert a new calendar.
     * </p>
     * 
     * @param conn
     *          the DB Connection
     * @param calendarName
     *          the name for the new calendar
     * @param calendar
     *          the calendar
     * @return the number of rows inserted
     * @throws IOException
     *           if there were problems serializing the calendar
     */
    public int insertCalendar(Connection conn, String calendarName,
            Calendar calendar) throws IOException, SQLException;

    /**
     * <p>
     * Update a calendar.
     * </p>
     * 
     * @param conn
     *          the DB Connection
     * @param calendarName
     *          the name for the new calendar
     * @param calendar
     *          the calendar
     * @return the number of rows updated
     * @throws IOException
     *           if there were problems serializing the calendar
     */
    public int updateCalendar(Connection conn, String calendarName,
            Calendar calendar) throws IOException, SQLException;

    /**
     * <p>
     * Check whether or not a calendar exists.
     * </p>
     * 
     * @param conn
     *          the DB Connection
     * @param calendarName
     *          the name of the calendar
     * @return true if the trigger exists, false otherwise
     */
    public boolean calendarExists(Connection conn, String calendarName)
            throws SQLException;

    /**
     * <p>
     * Select a calendar.
     * </p>
     * 
     * @param conn
     *          the DB Connection
     * @param calendarName
     *          the name of the calendar
     * @return the Calendar
     * @throws ClassNotFoundException
     *           if a class found during deserialization cannot be found be
     *           found
     * @throws IOException
     *           if there were problems deserializing the calendar
     */
    public Calendar selectCalendar(Connection conn, String calendarName)
            throws ClassNotFoundException, IOException, SQLException;

    /**
     * <p>
     * Check whether or not a calendar is referenced by any triggers.
     * </p>
     * 
     * @param conn
     *          the DB Connection
     * @param calendarName
     *          the name of the calendar
     * @return true if any triggers reference the calendar, false otherwise
     */
    public boolean calendarIsReferenced(Connection conn, String calendarName)
            throws SQLException;

    /**
     * <p>
     * Delete a calendar.
     * </p>
     * 
     * @param conn
     *          the DB Connection
     * @param calendarName
     *          the name of the trigger
     * @return the number of rows deleted
     */
    public int deleteCalendar(Connection conn, String calendarName)
            throws SQLException;

    /**
     * <p>
     * Select the total number of calendars stored.
     * </p>
     * 
     * @param conn
     *          the DB Connection
     * @return the total number of calendars stored
     */
    public int selectNumCalendars(Connection conn) throws SQLException;

    /**
     * <p>
     * Select all of the stored calendars.
     * </p>
     * 
     * @param conn
     *          the DB Connection
     * @return an array of <code>String</code> calendar names
     */
    public String[] selectCalendars(Connection conn) throws SQLException;

    //---------------------------------------------------------------------------
    // trigger firing
    //---------------------------------------------------------------------------

    /**
     * <p>
     * Select the next time that a trigger will be fired.
     * </p>
     * 
     * @param conn
     *          the DB Connection
     * @return the next fire time, or 0 if no trigger will be fired
     */
    public long selectNextFireTime(Connection conn) throws SQLException;

    /**
     * <p>
     * Select the trigger that will be fired at the given fire time.
     * </p>
     * 
     * @param conn
     *          the DB Connection
     * @param fireTime
     *          the time that the trigger will be fired
     * @return a <code>{@link org.quartz.utils.Key}</code> representing the
     *         trigger that will be fired at the given fire time, or null if no
     *         trigger will be fired at that time
     */
    public Key selectTriggerForFireTime(Connection conn, long fireTime)
            throws SQLException;

    /**
     * <p>
     * Insert a fired trigger.
     * </p>
     * 
     * @param conn
     *          the DB Connection
     * @param trigger
     *          the trigger
     * @param state
     *          the state that the trigger should be stored in
     * @return the number of rows inserted
     */
    public int insertFiredTrigger(Connection conn, Trigger trigger,
            String state, JobDetail jobDetail) throws SQLException;

    /**
     * <p>
     * Select the states of all fired-trigger records for a given trigger, or
     * trigger group if trigger name is <code>null</code>.
     * </p>
     * 
     * @return a List of FiredTriggerRecord objects.
     */
    public List selectFiredTriggerRecords(Connection conn, String triggerName,
            String groupName) throws SQLException;

    /**
     * <p>
     * Select the states of all fired-trigger records for a given job, or job
     * group if job name is <code>null</code>.
     * </p>
     * 
     * @return a List of FiredTriggerRecord objects.
     */
    public List selectFiredTriggerRecordsByJob(Connection conn, String jobName,
            String groupName) throws SQLException;

    /**
     * <p>
     * Select the states of all fired-trigger records for a given scheduler
     * instance.
     * </p>
     * 
     * @return a List of FiredTriggerRecord objects.
     */
    public List selectInstancesFiredTriggerRecords(Connection conn,
            String instanceName) throws SQLException;

    /**
     * <p>
     * Delete a fired trigger.
     * </p>
     * 
     * @param conn
     *          the DB Connection
     * @param entryId
     *          the fired trigger entry to delete
     * @return the number of rows deleted
     */
    public int deleteFiredTrigger(Connection conn, String entryId)
            throws SQLException;

    /**
     * <p>
     * Get the number instances of the identified job currently executing.
     * </p>
     * 
     * @param conn
     *          the DB Connection
     * @return the number instances of the identified job currently executing.
     */
    public int selectJobExecutionCount(Connection conn, String jobName,
            String jobGroup) throws SQLException;

    /**
     * <p>
     * Insert a scheduler-instance state record.
     * </p>
     * 
     * @param conn
     *          the DB Connection
     * @return the number of inserted rows.
     */
    public int insertSchedulerState(Connection conn, String instanceId,
            long checkInTime, long interval, String recoverer)
            throws SQLException;

    /**
     * <p>
     * Delete a scheduler-instance state record.
     * </p>
     * 
     * @param conn
     *          the DB Connection
     * @return the number of deleted rows.
     */
    public int deleteSchedulerState(Connection conn, String instanceId)
            throws SQLException;

    
    /**
     * <p>
     * Update a scheduler-instance state record.
     * </p>
     * 
     * @param conn
     *          the DB Connection
     * @return the number of updated rows.
     */
    public int updateSchedulerState(Connection conn, String instanceId, long checkInTime, String recoverer)
            throws SQLException;
    
    /**
     * <p>
     * A List of all current <code>SchedulerStateRecords</code>.
     * </p>
     * 
     * <p>
     * If instanceId is not null, then only the record for the identified
     * instance will be returned.
     * </p>
     * 
     * @param conn
     *          the DB Connection
     */
    public List selectSchedulerStateRecords(Connection conn, String instanceId) // TODO: this method would be more handy if it returned a map.
            throws SQLException;

}

// EOF
