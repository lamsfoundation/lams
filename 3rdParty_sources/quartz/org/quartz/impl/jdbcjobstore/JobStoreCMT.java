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

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

import org.quartz.Calendar;
import org.quartz.JobDetail;
import org.quartz.JobPersistenceException;
import org.quartz.ObjectAlreadyExistsException;
import org.quartz.SchedulerConfigException;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.core.SchedulingContext;
import org.quartz.spi.ClassLoadHelper;
import org.quartz.spi.SchedulerSignaler;
import org.quartz.spi.TriggerFiredBundle;
import org.quartz.utils.DBConnectionManager;

/**
 * <p>
 * <code>JobStoreCMT</code> is meant to be used in an application-server
 * environment that provides container-managed-transactions. No commit /
 * rollback will be1 handled by this class.
 * </p>
 * 
 * <p>
 * If you need commit / rollback, use <code>{@link
 * org.quartz.impl.jdbcjobstore.JobStoreTX}</code>
 * instead.
 * </p>
 * 
 * @author <a href="mailto:jeff@binaryfeed.org">Jeffrey Wescott</a>
 * @author James House
 * @author Srinivas Venkatarangaiah
 *  
 */
public class JobStoreCMT extends JobStoreSupport {

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * 
     * Data members.
     * 
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    protected String nonManagedTxDsName;

    // Great name huh?
    protected boolean dontSetNonManagedTXConnectionAutoCommitFalse = false;

    
    protected boolean setTxIsolationLevelReadCommitted = false;
    
    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * 
     * Interface.
     * 
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    /**
     * <p>
     * Set the name of the <code>DataSource</code> that should be used for
     * performing database functions.
     * </p>
     */
    public void setNonManagedTXDataSource(String nonManagedTxDsName) {
        this.nonManagedTxDsName = nonManagedTxDsName;
    }

    /**
     * <p>
     * Get the name of the <code>DataSource</code> that should be used for
     * performing database functions.
     * </p>
     */
    public String getNonManagedTXDataSource() {
        return nonManagedTxDsName;
    }

    public boolean isDontSetNonManagedTXConnectionAutoCommitFalse() {
        return dontSetNonManagedTXConnectionAutoCommitFalse;
    }

    /**
     * Don't call set autocommit(false) on connections obtained from the
     * DataSource. This can be helpfull in a few situations, such as if you
     * have a driver that complains if it is called when it is already off.
     * 
     * @param b
     */
    public void setDontSetNonManagedTXConnectionAutoCommitFalse(boolean b) {
        dontSetNonManagedTXConnectionAutoCommitFalse = b;
    }


    public boolean isTxIsolationLevelReadCommitted() {
        return setTxIsolationLevelReadCommitted;
    }

    /**
     * Set the transaction isolation level of DB connections to sequential.
     * 
     * @param b
     */
    public void setTxIsolationLevelReadCommitted(boolean b) {
        setTxIsolationLevelReadCommitted = b;
    }
    

    public void initialize(ClassLoadHelper loadHelper,
            SchedulerSignaler signaler) throws SchedulerConfigException {

        if (nonManagedTxDsName == null)
                throw new SchedulerConfigException(
                        "Non-ManagedTX DataSource name not set!");

        setUseDBLocks(true); // *must* use DB locks with CMT...

        super.initialize(loadHelper, signaler);

        getLog().info("JobStoreCMT initialized.");
    }
    
    public void shutdown() {

        super.shutdown();
        
        try {
            DBConnectionManager.getInstance().shutdown(getNonManagedTXDataSource());
        } catch (SQLException sqle) {
            getLog().warn("Database connection shutdown unsuccessful.", sqle);
        }
    }
    

    //---------------------------------------------------------------------------
    // JobStoreSupport methods
    //---------------------------------------------------------------------------

    /**
     * <p>
     * Recover any failed or misfired jobs and clean up the data store as
     * appropriate.
     * </p>
     * 
     * @throws JobPersistenceException
     *           if jobs could not be recovered
     */
    protected void recoverJobs() throws JobPersistenceException {
        Connection conn = null;

        boolean transOwner = false;

        try {
            conn = getNonManagedTXConnection();

            getLockHandler().obtainLock(conn, LOCK_TRIGGER_ACCESS);
            transOwner = true;
            //getLockHandler().obtainLock(conn, LOCK_JOB_ACCESS);

            recoverJobs(conn);

            conn.commit();
        } catch (JobPersistenceException e) {
            rollbackConnection(conn);
            throw e;
        } catch (Exception e) {
            rollbackConnection(conn);
            throw new JobPersistenceException("Error recovering jobs: "
                    + e.getMessage(), e);
        } finally {
        	try {
                releaseLock(conn, LOCK_TRIGGER_ACCESS, transOwner);
        	} finally {
                closeConnection(conn);
        	}
        }
    }

    protected void cleanVolatileTriggerAndJobs() throws JobPersistenceException {
        Connection conn = null;

        boolean transOwner = false;

        try {
            conn = getNonManagedTXConnection();

            getLockHandler().obtainLock(conn, LOCK_TRIGGER_ACCESS);
            transOwner = true;
            //getLockHandler().obtainLock(conn, LOCK_JOB_ACCESS);

            cleanVolatileTriggerAndJobs(conn);

            conn.commit();
        } catch (JobPersistenceException e) {
            rollbackConnection(conn);
            throw e;
        } catch (Exception e) {
            rollbackConnection(conn);
            throw new JobPersistenceException("Error cleaning volatile data: "
                    + e.getMessage(), e);
        } finally {
        	try {
                releaseLock(conn, LOCK_TRIGGER_ACCESS, transOwner);
        	} finally {
                closeConnection(conn);
        	}
        }
    }

    //---------------------------------------------------------------------------
    // job / trigger storage methods
    //---------------------------------------------------------------------------

    /**
     * <p>
     * Store the given <code>{@link org.quartz.JobDetail}</code> and <code>{@link org.quartz.Trigger}</code>.
     * </p>
     * 
     * @param newJob
     *          The <code>JobDetail</code> to be stored.
     * @param newTrigger
     *          The <code>Trigger</code> to be stored.
     * @throws ObjectAlreadyExistsException
     *           if a <code>Job</code> with the same name/group already
     *           exists.
     */
    public void storeJobAndTrigger(SchedulingContext ctxt, JobDetail newJob,
            Trigger newTrigger) throws ObjectAlreadyExistsException,
            JobPersistenceException {
        Connection conn = getConnection();
        boolean transOwner = false;
        try {
            if(isLockOnInsert()) {
                getLockHandler().obtainLock(conn, LOCK_TRIGGER_ACCESS);
                transOwner = true;
                //getLockHandler().obtainLock(conn, LOCK_JOB_ACCESS);
            }

            if (newJob.isVolatile() && !newTrigger.isVolatile()) {
                JobPersistenceException jpe = new JobPersistenceException(
                        "Cannot associate non-volatile "
                                + "trigger with a volatile job!");
                jpe.setErrorCode(SchedulerException.ERR_CLIENT_ERROR);
                throw jpe;
            }

            storeJob(conn, ctxt, newJob, false);
            storeTrigger(conn, ctxt, newTrigger, newJob, false,
                    Constants.STATE_WAITING, false, false);
        } finally {
        	try {
                releaseLock(conn, LOCK_TRIGGER_ACCESS, transOwner);
        	} finally {
                closeConnection(conn);
        	}
        }
    }

    /**
     * <p>
     * Store the given <code>{@link org.quartz.JobDetail}</code>.
     * </p>
     * 
     * @param newJob
     *          The <code>JobDetail</code> to be stored.
     * @param replaceExisting
     *          If <code>true</code>, any <code>Job</code> existing in the
     *          <code>JobStore</code> with the same name & group should be
     *          over-written.
     * @throws ObjectAlreadyExistsException
     *           if a <code>Job</code> with the same name/group already
     *           exists, and replaceExisting is set to false.
     */
    public void storeJob(SchedulingContext ctxt, JobDetail newJob,
            boolean replaceExisting) throws ObjectAlreadyExistsException,
            JobPersistenceException {
        Connection conn = getConnection();
        boolean transOwner = false;
        try {
            if(isLockOnInsert() || replaceExisting) {
                getLockHandler().obtainLock(conn, LOCK_TRIGGER_ACCESS);
                transOwner = true;
                //getLockHandler().obtainLock(conn, LOCK_JOB_ACCESS);
            }
            
            storeJob(conn, ctxt, newJob, replaceExisting);
        } finally {
        	try {
                releaseLock(conn, LOCK_TRIGGER_ACCESS, transOwner);
        	} finally {
                closeConnection(conn);
        	}
        }
    }

    /**
     * <p>
     * Remove (delete) the <code>{@link org.quartz.Job}</code> with the given
     * name, and any <code>{@link org.quartz.Trigger}</code> s that reference
     * it.
     * </p>
     * 
     * <p>
     * If removal of the <code>Job</code> results in an empty group, the
     * group should be removed from the <code>JobStore</code>'s list of
     * known group names.
     * </p>
     * 
     * @param jobName
     *          The name of the <code>Job</code> to be removed.
     * @param groupName
     *          The group name of the <code>Job</code> to be removed.
     * @return <code>true</code> if a <code>Job</code> with the given name &
     *         group was found and removed from the store.
     */
    public boolean removeJob(SchedulingContext ctxt, String jobName,
            String groupName) throws JobPersistenceException {
        Connection conn = getConnection();
        boolean transOwner = false;
        try {
            getLockHandler().obtainLock(conn, LOCK_TRIGGER_ACCESS);
            transOwner = true;
            //getLockHandler().obtainLock(conn, LOCK_JOB_ACCESS);

            return removeJob(conn, ctxt, jobName, groupName, true);
        } finally {
        	try {
                releaseLock(conn, LOCK_TRIGGER_ACCESS, transOwner);
        	} finally {
                closeConnection(conn);
        	}
        }
    }

    /**
     * <p>
     * Retrieve the <code>{@link org.quartz.JobDetail}</code> for the given
     * <code>{@link org.quartz.Job}</code>.
     * </p>
     * 
     * @param jobName
     *          The name of the <code>Job</code> to be retrieved.
     * @param groupName
     *          The group name of the <code>Job</code> to be retrieved.
     * @return The desired <code>Job</code>, or null if there is no match.
     */
    public JobDetail retrieveJob(SchedulingContext ctxt, String jobName,
            String groupName) throws JobPersistenceException {
        Connection conn = getConnection();
        try {
            // no locks necessary for read...
            return retrieveJob(conn, ctxt, jobName, groupName);
        } finally {
            closeConnection(conn);
        }
    }

    /**
     * <p>
     * Store the given <code>{@link org.quartz.Trigger}</code>.
     * </p>
     * 
     * @param newTrigger
     *          The <code>Trigger</code> to be stored.
     * @param replaceExisting
     *          If <code>true</code>, any <code>Trigger</code> existing in
     *          the <code>JobStore</code> with the same name & group should
     *          be over-written.
     * @throws ObjectAlreadyExistsException
     *           if a <code>Trigger</code> with the same name/group already
     *           exists, and replaceExisting is set to false.
     */
    public void storeTrigger(SchedulingContext ctxt, Trigger newTrigger,
            boolean replaceExisting) throws ObjectAlreadyExistsException,
            JobPersistenceException {
        Connection conn = getConnection();
        boolean transOwner = false;
        try {
            if(isLockOnInsert() || replaceExisting) {
                getLockHandler().obtainLock(conn, LOCK_TRIGGER_ACCESS);
                transOwner = true;
            }

            storeTrigger(conn, ctxt, newTrigger, null, replaceExisting,
                    STATE_WAITING, false, false);
        } finally {
        	try {
                releaseLock(conn, LOCK_TRIGGER_ACCESS, transOwner);
        	} finally {
                closeConnection(conn);
        	}
        }
    }

    /**
     * <p>
     * Remove (delete) the <code>{@link org.quartz.Trigger}</code> with the
     * given name.
     * </p>
     * 
     * <p>
     * If removal of the <code>Trigger</code> results in an empty group, the
     * group should be removed from the <code>JobStore</code>'s list of
     * known group names.
     * </p>
     * 
     * <p>
     * If removal of the <code>Trigger</code> results in an 'orphaned' <code>Job</code>
     * that is not 'durable', then the <code>Job</code> should be deleted
     * also.
     * </p>
     * 
     * @param triggerName
     *          The name of the <code>Trigger</code> to be removed.
     * @param groupName
     *          The group name of the <code>Trigger</code> to be removed.
     * @return <code>true</code> if a <code>Trigger</code> with the given
     *         name & group was found and removed from the store.
     */
    public boolean removeTrigger(SchedulingContext ctxt, String triggerName,
            String groupName) throws JobPersistenceException {
        Connection conn = getConnection();
        boolean transOwner = false;
        try {
            getLockHandler().obtainLock(conn, LOCK_TRIGGER_ACCESS);
            transOwner = true;

            return removeTrigger(conn, ctxt, triggerName, groupName);
        } finally {
        	try {
                releaseLock(conn, LOCK_TRIGGER_ACCESS, transOwner);
        	} finally {
                closeConnection(conn);
        	}
        }
    }


    /** 
     * @see org.quartz.spi.JobStore#replaceTrigger(org.quartz.core.SchedulingContext, java.lang.String, java.lang.String, org.quartz.Trigger)
     */
    public boolean replaceTrigger(SchedulingContext ctxt, String triggerName, String groupName, Trigger newTrigger) throws JobPersistenceException {
        Connection conn = getConnection();
        boolean transOwner = false;
        try {
            getLockHandler().obtainLock(conn, LOCK_TRIGGER_ACCESS);
            transOwner = true;

            return replaceTrigger(conn, ctxt, triggerName, groupName, newTrigger);
        } finally {
        	try {
                releaseLock(conn, LOCK_TRIGGER_ACCESS, transOwner);
        	} finally {
                closeConnection(conn);
        	}
        }
    }
    
    /**
     * <p>
     * Retrieve the given <code>{@link org.quartz.Trigger}</code>.
     * </p>
     * 
     * @param triggerName
     *          The name of the <code>Trigger</code> to be retrieved.
     * @param groupName
     *          The group name of the <code>Trigger</code> to be retrieved.
     * @return The desired <code>Trigger</code>, or null if there is no
     *         match.
     */
    public Trigger retrieveTrigger(SchedulingContext ctxt, String triggerName,
            String groupName) throws JobPersistenceException {
        Connection conn = getConnection();
        try {
            // no locks necessary for read...
            return retrieveTrigger(conn, ctxt, triggerName, groupName);
        } finally {
            closeConnection(conn);
        }
    }

    /**
     * <p>
     * Store the given <code>{@link org.quartz.Calendar}</code>.
     * </p>
     * 
     * @param calName
     *          The name of the calendar.
     * @param calendar
     *          The <code>Calendar</code> to be stored.
     * @param replaceExisting
     *          If <code>true</code>, any <code>Calendar</code> existing
     *          in the <code>JobStore</code> with the same name & group
     *          should be over-written.
     * @throws ObjectAlreadyExistsException
     *           if a <code>Calendar</code> with the same name already
     *           exists, and replaceExisting is set to false.
     */
    public void storeCalendar(SchedulingContext ctxt, String calName,
            Calendar calendar, boolean replaceExisting, boolean updateTriggers)
            throws ObjectAlreadyExistsException, JobPersistenceException {
        Connection conn = getConnection();
        boolean lockOwner = false;
        try {
            if(isLockOnInsert() || updateTriggers) { 
                getLockHandler().obtainLock(conn, LOCK_TRIGGER_ACCESS);
                lockOwner = true;
            }
            
            storeCalendar(conn, ctxt, calName, calendar, replaceExisting, updateTriggers);
        } finally {
        	try {
                releaseLock(conn, LOCK_TRIGGER_ACCESS, lockOwner);
        	} finally {
                closeConnection(conn);
        	}
        }
    }

    /**
     * <p>
     * Remove (delete) the <code>{@link org.quartz.Calendar}</code> with the
     * given name.
     * </p>
     * 
     * <p>
     * If removal of the <code>Calendar</code> would result in
     * <code.Trigger</code>s pointing to non-existent calendars, then a
     * <code>JobPersistenceException</code> will be thrown.</p>
     *       *
     * @param calName The name of the <code>Calendar</code> to be removed.
     * @return <code>true</code> if a <code>Calendar</code> with the given name
     * was found and removed from the store.
     */
    public boolean removeCalendar(SchedulingContext ctxt, String calName)
            throws JobPersistenceException {
        Connection conn = getConnection();
        boolean lockOwner = false;
        try {
            getLockHandler().obtainLock(conn, LOCK_CALENDAR_ACCESS);
            lockOwner = true;

            return removeCalendar(conn, ctxt, calName);
        } finally {
        	try {
                releaseLock(conn, LOCK_TRIGGER_ACCESS, lockOwner);
        	} finally {
                closeConnection(conn);
        	}
        }
    }

    /**
     * <p>
     * Retrieve the given <code>{@link org.quartz.Trigger}</code>.
     * </p>
     * 
     * @param calName
     *          The name of the <code>Calendar</code> to be retrieved.
     * @return The desired <code>Calendar</code>, or null if there is no
     *         match.
     */
    public Calendar retrieveCalendar(SchedulingContext ctxt, String calName)
            throws JobPersistenceException {
        Connection conn = getConnection();
        try {
            // no locks necessary for read...
            return retrieveCalendar(conn, ctxt, calName);
        } finally {
            closeConnection(conn);
        }
    }

    //---------------------------------------------------------------------------
    // informational methods
    //---------------------------------------------------------------------------

    /**
     * <p>
     * Get the number of <code>{@link org.quartz.Job}</code> s that are
     * stored in the <code>JobStore</code>.
     * </p>
     */
    public int getNumberOfJobs(SchedulingContext ctxt)
            throws JobPersistenceException {
        Connection conn = getConnection();
        try {
            // no locks necessary for read...
            return getNumberOfJobs(conn, ctxt);
        } finally {
            closeConnection(conn);
        }
    }

    /**
     * <p>
     * Get the number of <code>{@link org.quartz.Trigger}</code> s that are
     * stored in the <code>JobsStore</code>.
     * </p>
     */
    public int getNumberOfTriggers(SchedulingContext ctxt)
            throws JobPersistenceException {
        Connection conn = getConnection();
        try {
            // no locks necessary for read...
            return getNumberOfTriggers(conn, ctxt);
        } finally {
            closeConnection(conn);
        }
    }

    /**
     * <p>
     * Get the number of <code>{@link org.quartz.Calendar}</code> s that are
     * stored in the <code>JobsStore</code>.
     * </p>
     */
    public int getNumberOfCalendars(SchedulingContext ctxt)
            throws JobPersistenceException {
        Connection conn = getConnection();
        try {
            // no locks necessary for read...
            return getNumberOfCalendars(conn, ctxt);
        } finally {
            closeConnection(conn);
        }
    }

    public Set getPausedTriggerGroups(SchedulingContext ctxt) 
    throws JobPersistenceException {

        Connection conn = getConnection();
        try {
            // no locks necessary for read...
            Set groups = getPausedTriggerGroups(conn, ctxt);
            return groups;
        } finally {
            closeConnection(conn);
        }
    }
    
    /**
     * <p>
     * Get the names of all of the <code>{@link org.quartz.Job}</code> s that
     * have the given group name.
     * </p>
     * 
     * <p>
     * If there are no jobs in the given group name, the result should be a
     * zero-length array (not <code>null</code>).
     * </p>
     */
    public String[] getJobNames(SchedulingContext ctxt, String groupName)
            throws JobPersistenceException {
        Connection conn = getConnection();
        try {
            // no locks necessary for read...
            return getJobNames(conn, ctxt, groupName);
        } finally {
            closeConnection(conn);
        }
    }

    /**
     * <p>
     * Get the names of all of the <code>{@link org.quartz.Trigger}</code> s
     * that have the given group name.
     * </p>
     * 
     * <p>
     * If there are no triggers in the given group name, the result should be a
     * zero-length array (not <code>null</code>).
     * </p>
     */
    public String[] getTriggerNames(SchedulingContext ctxt, String groupName)
            throws JobPersistenceException {
        Connection conn = getConnection();
        try {
            // no locks necessary for read...
            return getTriggerNames(conn, ctxt, groupName);
        } finally {
            closeConnection(conn);
        }
    }

    /**
     * <p>
     * Get the names of all of the <code>{@link org.quartz.Job}</code>
     * groups.
     * </p>
     * 
     * <p>
     * If there are no known group names, the result should be a zero-length
     * array (not <code>null</code>).
     * </p>
     */
    public String[] getJobGroupNames(SchedulingContext ctxt)
            throws JobPersistenceException {
        Connection conn = getConnection();
        try {
            // no locks necessary for read...
            return getJobGroupNames(conn, ctxt);
        } finally {
            closeConnection(conn);
        }
    }

    /**
     * <p>
     * Get the names of all of the <code>{@link org.quartz.Trigger}</code>
     * groups.
     * </p>
     * 
     * <p>
     * If there are no known group names, the result should be a zero-length
     * array (not <code>null</code>).
     * </p>
     */
    public String[] getTriggerGroupNames(SchedulingContext ctxt)
            throws JobPersistenceException {
        Connection conn = getConnection();
        try {
            // no locks necessary for read...
            return getTriggerGroupNames(conn, ctxt);
        } finally {
            closeConnection(conn);
        }
    }

    /**
     * <p>
     * Get the names of all of the <code>{@link org.quartz.Calendar}</code> s
     * in the <code>JobStore</code>.
     * </p>
     * 
     * <p>
     * If there are no Calendars in the given group name, the result should be
     * a zero-length array (not <code>null</code>).
     * </p>
     */
    public String[] getCalendarNames(SchedulingContext ctxt)
            throws JobPersistenceException {
        Connection conn = getConnection();
        try {
            // no locks necessary for read...
            return getCalendarNames(conn, ctxt);
        } finally {
            closeConnection(conn);
        }
    }

    /**
     * <p>
     * Get all of the Triggers that are associated to the given Job.
     * </p>
     * 
     * <p>
     * If there are no matches, a zero-length array should be returned.
     * </p>
     */
    public Trigger[] getTriggersForJob(SchedulingContext ctxt, String jobName,
            String groupName) throws JobPersistenceException {
        Connection conn = getConnection();
        try {
            // no locks necessary for read...
            return getTriggersForJob(conn, ctxt, jobName, groupName);
        } finally {
            closeConnection(conn);
        }
    }

    /**
     * <p>
     * Get the current state of the identified <code>{@link Trigger}</code>.
     * </p>
     * 
     * @see Trigger#STATE_NORMAL
     * @see Trigger#STATE_PAUSED
     * @see Trigger#STATE_COMPLETE
     * @see Trigger#STATE_ERROR
     * @see Trigger#STATE_NONE
     */
    public int getTriggerState(SchedulingContext ctxt, String triggerName,
            String groupName) throws JobPersistenceException {
        Connection conn = getConnection();
        try {
            // no locks necessary for read...
            return getTriggerState(conn, ctxt, triggerName, groupName);
        } finally {
            closeConnection(conn);
        }
    }

    //---------------------------------------------------------------------------
    // trigger state manipulation methods
    //---------------------------------------------------------------------------

    /**
     * <p>
     * Pause the <code>{@link org.quartz.Trigger}</code> with the given name.
     * </p>
     * 
     * @see #resumeTrigger(SchedulingContext, String, String)
     */
    public void pauseTrigger(SchedulingContext ctxt, String triggerName,
            String groupName) throws JobPersistenceException {
        Connection conn = getConnection();
        boolean transOwner = false;
        try {
            getLockHandler().obtainLock(conn, LOCK_TRIGGER_ACCESS);
            transOwner = true;
            //getLockHandler().obtainLock(conn, LOCK_JOB_ACCESS);

            pauseTrigger(conn, ctxt, triggerName, groupName);
        } finally {
        	try {
                releaseLock(conn, LOCK_TRIGGER_ACCESS, transOwner);
        	} finally {
                closeConnection(conn);
        	}
        }
    }

    /**
     * <p>
     * Pause all of the <code>{@link org.quartz.Trigger}s</code> in the
     * given group.
     * </p>
     * 
     * @see #resumeTriggerGroup(SchedulingContext, String)
     */
    public void pauseTriggerGroup(SchedulingContext ctxt, String groupName)
            throws JobPersistenceException {
        Connection conn = getConnection();
        boolean transOwner = false;
        try {
            getLockHandler().obtainLock(conn, LOCK_TRIGGER_ACCESS);
            transOwner = true;
            //getLockHandler().obtainLock(conn, LOCK_JOB_ACCESS);

            pauseTriggerGroup(conn, ctxt, groupName);
        } finally {
        	try {
                releaseLock(conn, LOCK_TRIGGER_ACCESS, transOwner);
        	} finally {
                closeConnection(conn);
        	}
        }
    }

    /**
     * <p>
     * Pause the <code>{@link org.quartz.Job}</code> with the given name - by
     * pausing all of its current <code>Trigger</code>s.
     * </p>
     * 
     * @see #resumeJob(SchedulingContext, String, String)
     */
    public void pauseJob(SchedulingContext ctxt, String jobName,
            String groupName) throws JobPersistenceException {
        Connection conn = getConnection();
        boolean transOwner = false;
        try {
            getLockHandler().obtainLock(conn, LOCK_TRIGGER_ACCESS);
            transOwner = true;
            //getLockHandler().obtainLock(conn, LOCK_JOB_ACCESS);

            Trigger[] triggers = getTriggersForJob(conn, ctxt, jobName,
                    groupName);
            for (int j = 0; j < triggers.length; j++) {
                pauseTrigger(conn, ctxt, triggers[j].getName(), triggers[j]
                                                                         .getGroup());
            }
        } finally {
        	try {
                releaseLock(conn, LOCK_TRIGGER_ACCESS, transOwner);
        	} finally {
                closeConnection(conn);
        	}
        }
    }

    /**
     * <p>
     * Pause all of the <code>{@link org.quartz.Job}s</code> in the given
     * group - by pausing all of their <code>Trigger</code>s.
     * </p>
     * 
     * @see #resumeJobGroup(SchedulingContext, String)
     */
    public void pauseJobGroup(SchedulingContext ctxt, String groupName)
            throws JobPersistenceException {
        Connection conn = getConnection();
        boolean transOwner = false;
        try {
            getLockHandler().obtainLock(conn, LOCK_TRIGGER_ACCESS);
            transOwner = true;
            //getLockHandler().obtainLock(conn, LOCK_JOB_ACCESS);

            String[] jobNames = getJobNames(conn, ctxt, groupName);

            for (int i = 0; i < jobNames.length; i++) {
                Trigger[] triggers = getTriggersForJob(conn, ctxt, jobNames[i],
                        groupName);
                for (int j = 0; j < triggers.length; j++) {
                    pauseTrigger(conn, ctxt, triggers[j].getName(), triggers[j]
                                                                             .getGroup());
                }
            }
        } finally {
        	try {
                releaseLock(conn, LOCK_TRIGGER_ACCESS, transOwner);
        	} finally {
                closeConnection(conn);
        	}
        }
    }

    /**
     * <p>
     * Resume (un-pause) the <code>{@link org.quartz.Trigger}</code> with the
     * given name.
     * </p>
     * 
     * <p>
     * If the <code>Trigger</code> missed one or more fire-times, then the
     * <code>Trigger</code>'s misfire instruction will be applied.
     * </p>
     * 
     * @see #pauseTrigger(SchedulingContext, String, String)
     */
    public void resumeTrigger(SchedulingContext ctxt, String triggerName,
            String groupName) throws JobPersistenceException {
        Connection conn = getConnection();
        boolean transOwner = false;
        try {
            getLockHandler().obtainLock(conn, LOCK_TRIGGER_ACCESS);
            transOwner = true;
            //getLockHandler().obtainLock(conn, LOCK_JOB_ACCESS);

            resumeTrigger(conn, ctxt, triggerName, groupName);
        } finally {
        	try {
                releaseLock(conn, LOCK_TRIGGER_ACCESS, transOwner);
        	} finally {
                closeConnection(conn);
        	}
        }
    }

    /**
     * <p>
     * Resume (un-pause) all of the <code>{@link org.quartz.Trigger}s</code>
     * in the given group.
     * </p>
     * 
     * <p>
     * If any <code>Trigger</code> missed one or more fire-times, then the
     * <code>Trigger</code>'s misfire instruction will be applied.
     * </p>
     * 
     * @see #pauseTriggerGroup(SchedulingContext, String)
     */
    public void resumeTriggerGroup(SchedulingContext ctxt, String groupName)
            throws JobPersistenceException {
        Connection conn = getConnection();
        boolean transOwner = false;
        try {
            getLockHandler().obtainLock(conn, LOCK_TRIGGER_ACCESS);
            transOwner = true;
            //getLockHandler().obtainLock(conn, LOCK_JOB_ACCESS);

            resumeTriggerGroup(conn, ctxt, groupName);
        } finally {
        	try {
                releaseLock(conn, LOCK_TRIGGER_ACCESS, transOwner);
        	} finally {
                closeConnection(conn);
        	}
        }
    }

    /**
     * <p>
     * Resume (un-pause) the <code>{@link org.quartz.Job}</code> with the
     * given name.
     * </p>
     * 
     * <p>
     * If any of the <code>Job</code>'s<code>Trigger</code> s missed one
     * or more fire-times, then the <code>Trigger</code>'s misfire
     * instruction will be applied.
     * </p>
     * 
     * @see #pauseJob(SchedulingContext, String, String)
     */
    public void resumeJob(SchedulingContext ctxt, String jobName,
            String groupName) throws JobPersistenceException {
        Connection conn = getConnection();
        boolean transOwner = false;
        try {
            getLockHandler().obtainLock(conn, LOCK_TRIGGER_ACCESS);
            transOwner = true;
            //getLockHandler().obtainLock(conn, LOCK_JOB_ACCESS);

            Trigger[] triggers = getTriggersForJob(conn, ctxt, jobName,
                    groupName);
            for (int j = 0; j < triggers.length; j++) {
                resumeTrigger(conn, ctxt, triggers[j].getName(), triggers[j]
                                                                          .getGroup());
            }
        } finally {
        	try {
                releaseLock(conn, LOCK_TRIGGER_ACCESS, transOwner);
        	} finally {
                closeConnection(conn);
        	}
        }
    }

    /**
     * <p>
     * Resume (un-pause) all of the <code>{@link org.quartz.Job}s</code> in
     * the given group.
     * </p>
     * 
     * <p>
     * If any of the <code>Job</code> s had <code>Trigger</code> s that
     * missed one or more fire-times, then the <code>Trigger</code>'s
     * misfire instruction will be applied.
     * </p>
     * 
     * @see #pauseJobGroup(SchedulingContext, String)
     */
    public void resumeJobGroup(SchedulingContext ctxt, String groupName)
            throws JobPersistenceException {
        Connection conn = getConnection();
        boolean transOwner = false;
        try {
            getLockHandler().obtainLock(conn, LOCK_TRIGGER_ACCESS);
            transOwner = true;
            //getLockHandler().obtainLock(conn, LOCK_JOB_ACCESS);

            String[] jobNames = getJobNames(conn, ctxt, groupName);

            for (int i = 0; i < jobNames.length; i++) {
                Trigger[] triggers = getTriggersForJob(conn, ctxt, jobNames[i],
                        groupName);
                for (int j = 0; j < triggers.length; j++) {
                    resumeTrigger(conn, ctxt, triggers[j].getName(),
                            triggers[j].getGroup());
                }
            }
        } finally {
        	try {
                releaseLock(conn, LOCK_TRIGGER_ACCESS, transOwner);
        	} finally {
                closeConnection(conn);
        	}
        }
    }

    /**
     * <p>
     * Pause all triggers - equivalent of calling <code>pauseTriggerGroup(group)</code>
     * on every group.
     * </p>
     * 
     * <p>
     * When <code>resumeAll()</code> is called (to un-pause), trigger misfire
     * instructions WILL be applied.
     * </p>
     * 
     * @see #resumeAll(SchedulingContext)
     * @see #pauseTriggerGroup(SchedulingContext, String)
     */
    public void pauseAll(SchedulingContext ctxt) throws JobPersistenceException {
        Connection conn = getConnection();
        boolean transOwner = false;
        try {
            getLockHandler().obtainLock(conn, LOCK_TRIGGER_ACCESS);
            transOwner = true;
            //getLockHandler().obtainLock(conn, LOCK_JOB_ACCESS);

            pauseAll(conn, ctxt);
        } finally {
        	try {
                releaseLock(conn, LOCK_TRIGGER_ACCESS, transOwner);
        	} finally {
                closeConnection(conn);
        	}
        }
    }

    /**
     * <p>
     * Resume (un-pause) all triggers - equivalent of calling <code>resumeTriggerGroup(group)</code>
     * on every group.
     * </p>
     * 
     * <p>
     * If any <code>Trigger</code> missed one or more fire-times, then the
     * <code>Trigger</code>'s misfire instruction will be applied.
     * </p>
     * 
     * @see #pauseAll(SchedulingContext)
     */
    public void resumeAll(SchedulingContext ctxt)
            throws JobPersistenceException {
        Connection conn = getConnection();
        boolean transOwner = false;
        try {
            getLockHandler().obtainLock(conn, LOCK_TRIGGER_ACCESS);
            transOwner = true;
            //getLockHandler().obtainLock(conn, LOCK_JOB_ACCESS);

            resumeAll(conn, ctxt);
        } finally {
        	try {
                releaseLock(conn, LOCK_TRIGGER_ACCESS, transOwner);
        	} finally {
                closeConnection(conn);
        	}
        }
    }

    //---------------------------------------------------------------------------
    // trigger firing methods
    //---------------------------------------------------------------------------

    /**
     * <p>
     * Get a handle to the next trigger to be fired, and mark it as 'reserved'
     * by the calling scheduler.
     * </p>
     * 
     * @see #releaseAcquiredTrigger(SchedulingContext, Trigger)
     */
    public Trigger acquireNextTrigger(SchedulingContext ctxt, long noLaterThan)
            throws JobPersistenceException {
        Connection conn = null;
        boolean transOwner = false;

        try {
            conn = getNonManagedTXConnection();

            getLockHandler().obtainLock(conn, LOCK_TRIGGER_ACCESS);
            transOwner = true;
            //getLockHandler().obtainLock(conn, LOCK_JOB_ACCESS);

            Trigger trigger = acquireNextTrigger(conn, ctxt, noLaterThan);

            conn.commit();
            return trigger;
        } catch (JobPersistenceException e) {
            rollbackConnection(conn);
            throw e;
        } catch (Exception e) {
            rollbackConnection(conn);
            throw new JobPersistenceException(
                    "Error acquiring next firable trigger: " + e.getMessage(),
                    e);
        } finally {
        	try {
                releaseLock(conn, LOCK_TRIGGER_ACCESS, transOwner);
        	} finally {
                closeConnection(conn);
        	}
        }
    }

    /**
     * <p>
     * Inform the <code>JobStore</code> that the scheduler no longer plans to
     * fire the given <code>Trigger</code>, that it had previously acquired
     * (reserved).
     * </p>
     */
    public void releaseAcquiredTrigger(SchedulingContext ctxt, Trigger trigger)
            throws JobPersistenceException {
        Connection conn = null;
        boolean transOwner = false;

        try {
            conn = getNonManagedTXConnection();

            getLockHandler().obtainLock(conn, LOCK_TRIGGER_ACCESS);
            transOwner = true;
            //getLockHandler().obtainLock(conn, LOCK_JOB_ACCESS);

            releaseAcquiredTrigger(conn, ctxt, trigger);
            conn.commit();
        } catch (JobPersistenceException e) {
            rollbackConnection(conn);
            throw e;
        } catch (Exception e) {
            rollbackConnection(conn);
            throw new JobPersistenceException(
                    "Error releasing acquired trigger: " + e.getMessage(), e);
        } finally {
        	try {
                releaseLock(conn, LOCK_TRIGGER_ACCESS, transOwner);
        	} finally {
                closeConnection(conn);
        	}
        }
    }

    /**
     * <p>
     * Inform the <code>JobStore</code> that the scheduler is now firing the
     * given <code>Trigger</code> (executing its associated <code>Job</code>),
     * that it had previously acquired (reserved).
     * </p>
     * 
     * @return null if the trigger or it's job or calendar no longer exist, or
     *         if the trigger was not successfully put into the 'executing'
     *         state.
     */
    public TriggerFiredBundle triggerFired(SchedulingContext ctxt,
            Trigger trigger) throws JobPersistenceException {
        Connection conn = null;
        boolean transOwner = false;

        try {
            conn = getNonManagedTXConnection();

            getLockHandler().obtainLock(conn, LOCK_TRIGGER_ACCESS);
            transOwner = true;
            //getLockHandler().obtainLock(conn, LOCK_JOB_ACCESS);

            TriggerFiredBundle tfb = null;
            JobPersistenceException err = null;
            try {
                tfb = triggerFired(conn, ctxt, trigger);
            } catch (JobPersistenceException jpe) {
                if (jpe.getErrorCode() != SchedulerException.ERR_PERSISTENCE_JOB_DOES_NOT_EXIST)
                        throw jpe;
                err = jpe;
            }

            if (err != null) throw err;

            conn.commit();
            return tfb;
        } catch (JobPersistenceException e) {
            rollbackConnection(conn);
            throw e;
        } catch (Exception e) {
            rollbackConnection(conn);
            throw new JobPersistenceException("TX failure: " + e.getMessage(),
                    e);
        } finally {
        	try {
                releaseLock(conn, LOCK_TRIGGER_ACCESS, transOwner);
        	} finally {
                closeConnection(conn);
        	}
        }
    }

    /**
     * <p>
     * Inform the <code>JobStore</code> that the scheduler has completed the
     * firing of the given <code>Trigger</code> (and the execution its
     * associated <code>Job</code>), and that the <code>{@link org.quartz.JobDataMap}</code>
     * in the given <code>JobDetail</code> should be updated if the <code>Job</code>
     * is stateful.
     * </p>
     */
    public void triggeredJobComplete(SchedulingContext ctxt, Trigger trigger,
            JobDetail jobDetail, int triggerInstCode)
            throws JobPersistenceException {
        Connection conn = null;
        boolean transOwner = false;

        try {
            conn = getNonManagedTXConnection();
            getLockHandler().obtainLock(conn, LOCK_TRIGGER_ACCESS);
            transOwner = true;
            //getLockHandler().obtainLock(conn, LOCK_JOB_ACCESS);

            triggeredJobComplete(conn, ctxt, trigger, jobDetail,
                    triggerInstCode);

            conn.commit();
        } catch (JobPersistenceException e) {
            rollbackConnection(conn);
            throw e;
        } catch (Exception e) {
            rollbackConnection(conn);
            throw new JobPersistenceException("TX failure: " + e.getMessage(),
                    e);
        } finally {
        	try {
                releaseLock(conn, LOCK_TRIGGER_ACCESS, transOwner);
        	} finally {
                closeConnection(conn);
        	}
        }
    }

    protected boolean doRecoverMisfires() throws JobPersistenceException {
        Connection conn = null;
        boolean transOwner = false;
        boolean moreToDo = false;

        try {
            conn = getNonManagedTXConnection();

            getLockHandler().obtainLock(conn, LOCK_TRIGGER_ACCESS);
            transOwner = true;

            try {
                moreToDo = recoverMisfiredJobs(conn, false);
            } catch (Exception e) {
                throw new JobPersistenceException(e.getMessage(), e);
            }

            conn.commit();

            return moreToDo;
        } catch (JobPersistenceException e) {
            rollbackConnection(conn);
            throw e;
        } catch (Exception e) {
            rollbackConnection(conn);
            throw new JobPersistenceException("TX failure: " + e.getMessage(),
                    e);
        } finally {
        	try {
                releaseLock(conn, LOCK_TRIGGER_ACCESS, transOwner);
        	} finally {
                closeConnection(conn);
        	}
        }

    }

    protected boolean doCheckin() throws JobPersistenceException {
        Connection conn = null;

        boolean transOwner = false;
        boolean transStateOwner = false;
        boolean recovered = false;

        try {
            conn = getNonManagedTXConnection();

            // Other than the first time, always checkin first to make sure there is 
            // work to be done before we aquire / the lock (since that is expensive, 
            // and is almost never necessary)
            List failedRecords = (firstCheckIn) ? null : clusterCheckIn(conn);
            
            if (firstCheckIn || (failedRecords.size() > 0)) {
                getLockHandler().obtainLock(conn, LOCK_STATE_ACCESS);
                transStateOwner = true;
                
                // Now that we own the lock, make sure we still have work to do. 
                // The first time through, we also need to make sure we update/create our state record
                failedRecords = (firstCheckIn) ? clusterCheckIn(conn) : findFailedInstances(conn);
    
                if (failedRecords.size() > 0) {
                    getLockHandler().obtainLock(conn, LOCK_TRIGGER_ACCESS);
                    //getLockHandler().obtainLock(conn, LOCK_JOB_ACCESS);
                    transOwner = true;
    
                    clusterRecover(conn, failedRecords);
                    recovered = true;
                }
            }
            conn.commit();
        } catch (JobPersistenceException e) {
            rollbackConnection(conn);
            throw e;
        } catch (Exception e) {
            rollbackConnection(conn);
            throw new JobPersistenceException("TX failure: " + e.getMessage(),
                    e);
        } finally {
        	try {
        		releaseLock(conn, LOCK_TRIGGER_ACCESS, transOwner);
        	} finally {
            	try {
            		releaseLock(conn, LOCK_STATE_ACCESS, transStateOwner);
            	} finally {
            		closeConnection(conn);
            	}
        	}
        }

        firstCheckIn = false;

        return recovered;
    }

    //---------------------------------------------------------------------------
    // private helpers
    //---------------------------------------------------------------------------


    protected Connection getNonManagedTXConnection()
            throws JobPersistenceException {
        try {
            Connection conn = DBConnectionManager.getInstance().getConnection(
                    getNonManagedTXDataSource());

            if (conn == null) { throw new SQLException(
                    "Could not get connection from DataSource '"
                            + getNonManagedTXDataSource() + "'"); }

            try {
	            if (!isDontSetNonManagedTXConnectionAutoCommitFalse())
	                    conn.setAutoCommit(false);
	
	            if (isTxIsolationLevelReadCommitted())
	                conn.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
            } catch (SQLException ingore) {
            } catch (Exception e) {
            	if(conn != null)
            		try { conn.close(); } catch(Throwable tt) {}
            		throw new JobPersistenceException(
            				"Failure setting up connection.", e);
            }

            return conn;
        } catch (SQLException sqle) {
            throw new JobPersistenceException(
                    "Failed to obtain DB connection from data source '"
                            + getNonManagedTXDataSource() + "': "
                            + sqle.toString(), sqle);
        } catch (Exception e) {
            throw new JobPersistenceException(
                    "Failed to obtain DB connection from data source '"
                            + getNonManagedTXDataSource() + "': "
                            + e.toString(), e,
                    JobPersistenceException.ERR_PERSISTENCE_CRITICAL_FAILURE);
        }
    }

}

// EOF
