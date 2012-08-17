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
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Calendar;
import org.quartz.CronTrigger;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobPersistenceException;
import org.quartz.ObjectAlreadyExistsException;
import org.quartz.Scheduler;
import org.quartz.SchedulerConfigException;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.core.SchedulingContext;
import org.quartz.spi.ClassLoadHelper;
import org.quartz.spi.JobStore;
import org.quartz.spi.SchedulerSignaler;
import org.quartz.spi.TriggerFiredBundle;
import org.quartz.utils.DBConnectionManager;
import org.quartz.utils.Key;
import org.quartz.utils.TriggerStatus;


/**
 * <p>
 * Contains base functionality for JDBC-based JobStore implementations.
 * </p>
 * 
 * @author <a href="mailto:jeff@binaryfeed.org">Jeffrey Wescott</a>
 * @author James House
 */
public abstract class JobStoreSupport implements JobStore, Constants {

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * 
     * Constants.
     * 
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    protected static String LOCK_TRIGGER_ACCESS = "TRIGGER_ACCESS";

    protected static String LOCK_JOB_ACCESS = "JOB_ACCESS";

    protected static String LOCK_CALENDAR_ACCESS = "CALENDAR_ACCESS";

    protected static String LOCK_STATE_ACCESS = "STATE_ACCESS";

    protected static String LOCK_MISFIRE_ACCESS = "MISFIRE_ACCESS";

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * 
     * Data members.
     * 
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    protected String dsName;

    protected String tablePrefix = DEFAULT_TABLE_PREFIX;

    protected boolean useProperties = false;

    protected String instanceId;

    protected String instanceName;
    
    protected String delegateClassName;
    protected Class delegateClass = StdJDBCDelegate.class;

    protected HashMap calendarCache = new HashMap();

    private DriverDelegate delegate;

    private long misfireThreshold = 60000L; // one minute

    private boolean dontSetAutoCommitFalse = false;

    private boolean isClustered = false;

    private boolean useDBLocks = false;
    
    private boolean lockOnInsert = true;

    private Semaphore lockHandler = null; // set in initialize() method...

    private String selectWithLockSQL = null;

    private long clusterCheckinInterval = 7500L;

    private ClusterManager clusterManagementThread = null;

    private MisfireHandler misfireHandler = null;

    private ClassLoadHelper classLoadHelper;

    private SchedulerSignaler signaler;

    protected int maxToRecoverAtATime = 20;
    
    private boolean setTxIsolationLevelSequential = false;
    
    private long dbRetryInterval = 10000;
    
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
    public void setDataSource(String dsName) {
        this.dsName = dsName;
    }

    /**
     * <p>
     * Get the name of the <code>DataSource</code> that should be used for
     * performing database functions.
     * </p>
     */
    public String getDataSource() {
        return dsName;
    }

    /**
     * <p>
     * Set the prefix that should be pre-pended to all table names.
     * </p>
     */
    public void setTablePrefix(String prefix) {
        if (prefix == null) prefix = "";

        this.tablePrefix = prefix;
    }

    /**
     * <p>
     * Get the prefix that should be pre-pended to all table names.
     * </p>
     */
    public String getTablePrefix() {
        return tablePrefix;
    }

    /**
     * <p>
     * Set whether String-only properties will be handled in JobDataMaps.
     * </p>
     */
    public void setUseProperties(String useProp) {
        if (useProp == null) useProp = "false";

        this.useProperties = Boolean.valueOf(useProp).booleanValue();
    }

    /**
     * <p>
     * Get whether String-only properties will be handled in JobDataMaps.
     * </p>
     */
    public boolean canUseProperties() {
        return useProperties;
    }

    /**
     * <p>
     * Set the instance Id of the Scheduler (must be unique within a cluster).
     * </p>
     */
    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    /**
     * <p>
     * Get the instance Id of the Scheduler (must be unique within a cluster).
     * </p>
     */
    public String getInstanceId() {

        return instanceId;
    }

    /**
     * <p>
     * Set the instance Id of the Scheduler (must be unique within a cluster).
     * </p>
     */
    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    /**
     * <p>
     * Get the instance Id of the Scheduler (must be unique within a cluster).
     * </p>
     */
    public String getInstanceName() {

        return instanceName;
    }

    /**
     * <p>
     * Set whether this instance is part of a cluster.
     * </p>
     */
    public void setIsClustered(boolean isClustered) {
        this.isClustered = isClustered;
    }

    /**
     * <p>
     * Get whether this instance is part of a cluster.
     * </p>
     */
    public boolean isClustered() {
        return isClustered;
    }

    /**
     * <p>
     * Get the frequency (in milliseconds) at which this instance "checks-in"
     * with the other instances of the cluster. -- Affects the rate of
     * detecting failed instances.
     * </p>
     */
    public long getClusterCheckinInterval() {
        return clusterCheckinInterval;
    }

    /**
     * <p>
     * Set the frequency (in milliseconds) at which this instance "checks-in"
     * with the other instances of the cluster. -- Affects the rate of
     * detecting failed instances.
     * </p>
     */
    public void setClusterCheckinInterval(long l) {
        clusterCheckinInterval = l;
    }

    /**
     * <p>
     * Get the maximum number of misfired triggers that the misfire handling
     * thread will try to recover at one time (within one transaction).  The
     * default is 20.
     * </p>
     */
    public int getMaxMisfiresToHandleAtATime() {
        return maxToRecoverAtATime;
    }

    /**
     * <p>
     * Set the maximum number of misfired triggers that the misfire handling
     * thread will try to recover at one time (within one transaction).  The
     * default is 20.
     * </p>
     */
    public void setMaxMisfiresToHandleAtATime(int maxToRecoverAtATime) {
        this.maxToRecoverAtATime = maxToRecoverAtATime;
    }

    /**
     * @return Returns the dbRetryInterval.
     */
    public long getDbRetryInterval() {
        return dbRetryInterval;
    }
    /**
     * @param dbRetryInterval The dbRetryInterval to set.
     */
    public void setDbRetryInterval(long dbRetryInterval) {
        this.dbRetryInterval = dbRetryInterval;
    }
    
    /**
     * <p>
     * Set whether this instance should use database-based thread
     * synchronization.
     * </p>
     */
    public void setUseDBLocks(boolean useDBLocks) {
        this.useDBLocks = useDBLocks;
    }

    /**
     * <p>
     * Get whether this instance should use database-based thread
     * synchronization.
     * </p>
     */
    public boolean getUseDBLocks() {
        return useDBLocks;
    }

    public boolean isLockOnInsert() {
        return lockOnInsert;
    }
    
    /**
     * Whether or not to obtain locks when inserting new jobs/triggers.  
     * Defaults to <code>true</code>, which is safest - some db's (such as 
     * MS SQLServer) seem to require this to avoid deadlocks under high load,
     * while others seem to do fine without.  
     * 
     * <p>Setting this property to <code>false</code> will provide a 
     * significant performance increase during the addition of new jobs 
     * and triggers.</p>
     * 
     * @param lockOnInsert
     */
    public void setLockOnInsert(boolean lockOnInsert) {
        this.lockOnInsert = lockOnInsert;
    }
    
    public long getMisfireThreshold() {
        return misfireThreshold;
    }

    /**
     * The the number of milliseconds by which a trigger must have missed its
     * next-fire-time, in order for it to be considered "misfired" and thus
     * have its misfire instruction applied.
     * 
     * @param misfireThreshold
     */
    public void setMisfireThreshold(long misfireThreshold) {
        if (misfireThreshold < 1)
                throw new IllegalArgumentException(
                        "Misfirethreashold must be larger than 0");
        this.misfireThreshold = misfireThreshold;
    }

    public boolean isDontSetAutoCommitFalse() {
        return dontSetAutoCommitFalse;
    }

    /**
     * Don't call set autocommit(false) on connections obtained from the
     * DataSource. This can be helpfull in a few situations, such as if you
     * have a driver that complains if it is called when it is already off.
     * 
     * @param b
     */
    public void setDontSetAutoCommitFalse(boolean b) {
        dontSetAutoCommitFalse = b;
    }

    public boolean isTxIsolationLevelSerializable() {
      return setTxIsolationLevelSequential;
    }

    /**
     * Set the transaction isolation level of DB connections to sequential.
     * 
     * @param b
     */
    public void setTxIsolationLevelSerializable(boolean b) {
      setTxIsolationLevelSequential = b;
    }

    
    /**
     * <p>
     * Set the JDBC driver delegate class.
     * </p>
     * 
     * @param delegateClassName
     *          the delegate class name
     */
    public void setDriverDelegateClass(String delegateClassName)
            throws InvalidConfigurationException {
        this.delegateClassName = delegateClassName;
    }

    /**
     * <p>
     * Get the JDBC driver delegate class name.
     * </p>
     * 
     * @return the delegate class name
     */
    public String getDriverDelegateClass() {
        return delegateClassName;
    }

    public String getSelectWithLockSQL() {
        return selectWithLockSQL;
    }

    /**
     * <p>
     * set the SQL statement to use to select and lock a row in the "locks"
     * table.
     * </p>
     * 
     * @see StdRowLockSemaphore
     */
    public void setSelectWithLockSQL(String string) {
        selectWithLockSQL = string;
    }

    protected ClassLoadHelper getClassLoadHelper() {
        return classLoadHelper;
    }

    //---------------------------------------------------------------------------
    // interface methods
    //---------------------------------------------------------------------------

    Log getLog() {
        return LogFactory.getLog(getClass());
    }

    /**
     * <p>
     * Called by the QuartzScheduler before the <code>JobStore</code> is
     * used, in order to give the it a chance to initialize.
     * </p>
     */
    public void initialize(ClassLoadHelper loadHelper,
            SchedulerSignaler signaler) throws SchedulerConfigException {

        if (dsName == null) { throw new SchedulerConfigException(
                "DataSource name not set."); }

        classLoadHelper = loadHelper;
        this.signaler = signaler;

        if (!getUseDBLocks() && !isClustered()) {
            getLog()
                    .info(
                            "Using thread monitor-based data access locking (synchronization).");
            lockHandler = new SimpleSemaphore();
        } else {
            getLog()
                    .info(
                            "Using db table-based data access locking (synchronization).");
            lockHandler = new StdRowLockSemaphore(getTablePrefix(),
                    getSelectWithLockSQL());
        }

        if (!isClustered()) {
            try {
                cleanVolatileTriggerAndJobs();
            } catch (SchedulerException se) {
                throw new SchedulerConfigException(
                        "Failure occured during job recovery.", se);
            }
        }
    }

    /**
     * @see org.quartz.spi.JobStore#schedulerStarted()
     */
    public void schedulerStarted() throws SchedulerException {

        if (isClustered()) {
            clusterManagementThread = new ClusterManager(this);
            clusterManagementThread.initialize();
        }
        else {
            try {
                recoverJobs();
            } catch (SchedulerException se) {
                throw new SchedulerConfigException(
                        "Failure occured during job recovery.", se);
            }
        }

        misfireHandler = new MisfireHandler(this);
        misfireHandler.initialize();
    }
    
    /**
     * <p>
     * Called by the QuartzScheduler to inform the <code>JobStore</code> that
     * it should free up all of it's resources because the scheduler is
     * shutting down.
     * </p>
     */
    public void shutdown() {
        if (clusterManagementThread != null)
                clusterManagementThread.shutdown();

        if (misfireHandler != null) misfireHandler.shutdown();
        
        try {
            DBConnectionManager.getInstance().shutdown(getDataSource());
        } catch (SQLException sqle) {
            getLog().warn("Database connection shutdown unsuccessful.", sqle);
        }        
    }

    public boolean supportsPersistence() {
        return true;
    }

    //---------------------------------------------------------------------------
    // helper methods for subclasses
    //---------------------------------------------------------------------------

    

    protected Connection getConnection() throws JobPersistenceException {
        try {
            Connection conn = DBConnectionManager.getInstance().getConnection(
                    getDataSource());

            if (conn == null) { throw new SQLException(
                    "Could not get connection from DataSource '"
                    + getDataSource() + "'"); }

            try {
                if (!isDontSetAutoCommitFalse()) conn.setAutoCommit(false);

                if(isTxIsolationLevelSerializable())
                  conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
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
                    + getDataSource() + "': " + sqle.toString(), sqle);
        } catch (Exception e) {
            throw new JobPersistenceException(
                    "Failed to obtain DB connection from data source '"
                    + getDataSource() + "': " + e.toString(), e,
                    JobPersistenceException.ERR_PERSISTENCE_CRITICAL_FAILURE);
        }
    }
    
    protected void releaseLock(Connection conn, String lockName, boolean doIt) {
      if (doIt && conn != null) {
        try {
          getLockHandler().releaseLock(conn, lockName);
        } catch (LockException le) {
          getLog().error("Error returning lock: " + le.getMessage(),
              le);
        }
      }
    }
    
    /**
     * <p>
     * Removes all volatile data
     * </p>
     * 
     * @throws JobPersistenceException
     *           if jobs could not be recovered
     */
    protected abstract void cleanVolatileTriggerAndJobs()
            throws JobPersistenceException;

    /**
     * <p>
     * Removes all volatile data.
     * </p>
     * 
     * @throws JobPersistenceException
     *           if jobs could not be recovered
     */
    protected void cleanVolatileTriggerAndJobs(Connection conn)
            throws JobPersistenceException {
        try {
            // find volatile jobs & triggers...
            Key[] volatileTriggers = getDelegate().selectVolatileTriggers(conn);
            Key[] volatileJobs = getDelegate().selectVolatileJobs(conn);

            for (int i = 0; i < volatileTriggers.length; i++) {
                removeTrigger(conn, null, volatileTriggers[i].getName(),
                        volatileTriggers[i].getGroup());
            }
            getLog().info(
                    "Removed " + volatileTriggers.length
                            + " Volatile Trigger(s).");

            for (int i = 0; i < volatileJobs.length; i++) {
                removeJob(conn, null, volatileJobs[i].getName(),
                        volatileJobs[i].getGroup(), true);
            }
            getLog().info(
                    "Removed " + volatileJobs.length + " Volatile Job(s).");

            // clean up any fired trigger entries
            getDelegate().deleteVolatileFiredTriggers(conn);

        } catch (Exception e) {
            throw new JobPersistenceException("Couldn't clean volatile data: "
                    + e.getMessage(), e);
        }
    }

    /**
     * <p>
     * Will recover any failed or misfired jobs and clean up the data store as
     * appropriate.
     * </p>
     * 
     * @throws JobPersistenceException
     *           if jobs could not be recovered
     */
    protected abstract void recoverJobs() throws JobPersistenceException;

    /**
     * <p>
     * Will recover any failed or misfired jobs and clean up the data store as
     * appropriate.
     * </p>
     * 
     * @throws JobPersistenceException
     *           if jobs could not be recovered
     */
    protected void recoverJobs(Connection conn) throws JobPersistenceException {
        try {
            // update inconsistent job states
            int rows = getDelegate().updateTriggerStatesFromOtherStates(conn,
                    STATE_WAITING, STATE_ACQUIRED, STATE_BLOCKED);

            rows += getDelegate().updateTriggerStatesFromOtherStates(conn,
                        STATE_PAUSED, STATE_PAUSED_BLOCKED, STATE_PAUSED_BLOCKED);
            
            getLog().info(
                    "Freed " + rows
                            + " triggers from 'acquired' / 'blocked' state.");

            // clean up misfired jobs
            getDelegate().updateTriggerStateFromOtherStatesBeforeTime(conn,
                    STATE_MISFIRED, STATE_WAITING, STATE_WAITING,
                    getMisfireTime()); // only waiting
            recoverMisfiredJobs(conn, true);
            
            // recover jobs marked for recovery that were not fully executed
            Trigger[] recoveringJobTriggers = getDelegate()
                    .selectTriggersForRecoveringJobs(conn);
            getLog()
                    .info(
                            "Recovering "
                                    + recoveringJobTriggers.length
                                    + " jobs that were in-progress at the time of the last shut-down.");

            for (int i = 0; i < recoveringJobTriggers.length; ++i) {
                if (jobExists(conn, recoveringJobTriggers[i].getJobName(),
                        recoveringJobTriggers[i].getJobGroup())) {
                    recoveringJobTriggers[i].computeFirstFireTime(null);
                    storeTrigger(conn, null, recoveringJobTriggers[i], null, false,
                            STATE_WAITING, false, true);
                }
            }
            getLog().info("Recovery complete.");

            // remove lingering 'complete' triggers...
            Key[] ct = getDelegate().selectTriggersInState(conn, STATE_COMPLETE);
            for(int i=0; ct != null && i < ct.length; i++)
              removeTrigger(conn, null, ct[i].getName(), ct[i].getGroup());
            getLog().info(
                "Removed " + ct.length
                + " 'complete' triggers.");
            
            // clean up any fired trigger entries
            int n = getDelegate().deleteFiredTriggers(conn);
            getLog().info("Removed " + n + " stale fired job entries.");
        } catch (Exception e) {
            throw new JobPersistenceException("Couldn't recover jobs: "
                    + e.getMessage(), e);
        }
    }

    protected long getMisfireTime() {
        long misfireTime = System.currentTimeMillis();
        if (getMisfireThreshold() > 0) misfireTime -= getMisfireThreshold();

        return misfireTime;
    }

    private int lastRecoverCount = 0;

    protected boolean recoverMisfiredJobs(Connection conn, boolean recovering)
            throws JobPersistenceException, NoSuchDelegateException,
            SQLException, ClassNotFoundException, IOException {

        Key[] misfiredTriggers = getDelegate().selectTriggersInState(conn,
                STATE_MISFIRED);

        if (misfiredTriggers.length > 0
                && misfiredTriggers.length > getMaxMisfiresToHandleAtATime()) getLog()
                .info(
                        "Handling "
                                + getMaxMisfiresToHandleAtATime()
                                + " of "
                                + misfiredTriggers.length
                                + " triggers that missed their scheduled fire-time.");
        else if (misfiredTriggers.length > 0) getLog().info(
                "Handling " + misfiredTriggers.length
                        + " triggers that missed their scheduled fire-time.");
        else
            getLog().debug(
                    "Found 0 triggers that missed their scheduled fire-time.");

        lastRecoverCount = misfiredTriggers.length;

        for (int i = 0; i < misfiredTriggers.length && i < getMaxMisfiresToHandleAtATime(); i++) {
            Trigger trig = getDelegate().selectTrigger(conn,
                    misfiredTriggers[i].getName(),
                    misfiredTriggers[i].getGroup());

            if (trig == null) continue;

            Calendar cal = null;
            if (trig.getCalendarName() != null)
                    cal = retrieveCalendar(conn, null, trig.getCalendarName());

            String[] listeners = getDelegate().selectTriggerListeners(conn,
                trig.getName(), trig.getGroup());
            for (int l = 0; l < listeners.length; ++l) {
              trig.addTriggerListener(listeners[l]);
            }
            
            signaler.notifyTriggerListenersMisfired(trig);

            trig.updateAfterMisfire(cal);

            if (trig.getNextFireTime() == null) 
                storeTrigger(conn, null, trig, null, true, STATE_COMPLETE, 
                        false, recovering);
            else
                storeTrigger(conn, null, trig, null, true, STATE_WAITING, 
                        false, recovering);
        }

        if (misfiredTriggers.length > getMaxMisfiresToHandleAtATime()) return true;

        return false;
    }

    protected boolean updateMisfiredTrigger(Connection conn,
            SchedulingContext ctxt, String triggerName, String groupName,
            String newStateIfNotComplete, boolean forceState) // TODO: probably
            // get rid of
            // this
            throws JobPersistenceException {
        try {

            Trigger trig = getDelegate().selectTrigger(conn, triggerName,
                    groupName);

            long misfireTime = System.currentTimeMillis();
            if (getMisfireThreshold() > 0)
                    misfireTime -= getMisfireThreshold();

            if (trig.getNextFireTime().getTime() > misfireTime) return false;

            Calendar cal = null;
            if (trig.getCalendarName() != null)
                    cal = retrieveCalendar(conn, ctxt, trig.getCalendarName());

            signaler.notifyTriggerListenersMisfired(trig);

            trig.updateAfterMisfire(cal);

            if (trig.getNextFireTime() == null) storeTrigger(conn, ctxt, trig,
                    null, true, STATE_COMPLETE, forceState, false);
            else {
                storeTrigger(conn, ctxt, trig, null, true, newStateIfNotComplete,
                        forceState, false);
            }
            
            return true;

        } catch (Exception e) {
            throw new JobPersistenceException(
                    "Couldn't update misfired trigger '" + groupName + "."
                            + triggerName + "': " + e.getMessage(), e);
        }
    }

    /**
     * <p>
     * Insert or update a job.
     * </p>
     */
    protected void storeJob(Connection conn, SchedulingContext ctxt,
            JobDetail newJob, boolean replaceExisting)
            throws ObjectAlreadyExistsException, JobPersistenceException {
        if (newJob.isVolatile() && isClustered())
                getLog()
                        .info(
                                "note: volatile jobs are effectively non-volatile in a clustered environment.");

        boolean existingJob = jobExists(conn, newJob.getName(), newJob
                .getGroup());
        try {
            if (existingJob) {
                if (!replaceExisting) { throw new ObjectAlreadyExistsException(
                        newJob); }
                getDelegate().updateJobDetail(conn, newJob);
            } else {
                getDelegate().insertJobDetail(conn, newJob);
            }
        } catch (IOException e) {
            throw new JobPersistenceException("Couldn't store job: "
                    + e.getMessage(), e);
        } catch (SQLException e) {
            throw new JobPersistenceException("Couldn't store job: "
                    + e.getMessage(), e);
        }
    }

    /**
     * <p>
     * Check existence of a given job.
     * </p>
     */
    protected boolean jobExists(Connection conn, String jobName,
            String groupName) throws JobPersistenceException {
        try {
            return getDelegate().jobExists(conn, jobName, groupName);
        } catch (SQLException e) {
            throw new JobPersistenceException(
                    "Couldn't determine job existence (" + groupName + "."
                            + jobName + "): " + e.getMessage(), e);
        }
    }

    /**
     * <p>
     * Insert or update a trigger.
     * </p>
     */
    protected void storeTrigger(Connection conn, SchedulingContext ctxt,
            Trigger newTrigger, JobDetail job, boolean replaceExisting, String state,
            boolean forceState, boolean recovering)
            throws ObjectAlreadyExistsException, JobPersistenceException {
        if (newTrigger.isVolatile() && isClustered())
                getLog()
                        .info(
                                "note: volatile triggers are effectively non-volatile in a clustered environment.");

        boolean existingTrigger = triggerExists(conn, newTrigger.getName(),
                newTrigger.getGroup());

        try {

            boolean shouldBepaused = false;

            if (!forceState) {
                shouldBepaused = getDelegate().isTriggerGroupPaused(
                        conn, newTrigger.getGroup());

                if(!shouldBepaused) {
                    shouldBepaused = getDelegate().isTriggerGroupPaused(conn,
                            ALL_GROUPS_PAUSED);

                    if (shouldBepaused)
                            getDelegate().insertPausedTriggerGroup(conn,
                                    newTrigger.getGroup());
                }

                if (shouldBepaused && (state.equals(STATE_WAITING) || state.equals(STATE_ACQUIRED)))
                        state = STATE_PAUSED;
            }

            if(job == null) {
                job = getDelegate().selectJobDetail(conn,
                    newTrigger.getJobName(), newTrigger.getJobGroup(),
                    getClassLoadHelper());
            }
            if (job == null)
                    throw new JobPersistenceException("The job ("
                            + newTrigger.getFullJobName()
                            + ") referenced by the trigger does not exist.");
            if (job.isVolatile() && !newTrigger.isVolatile())
                    throw new JobPersistenceException(
                            "It does not make sense to "
                                    + "associate a non-volatile Trigger with a volatile Job!");

            if (job.isStateful() && !recovering) { 
                    String bstate = getNewStatusForTrigger(conn, ctxt, job.getName(), job
                            .getGroup());
                    if(STATE_BLOCKED.equals(bstate) && STATE_WAITING.equals(state))
                        state = STATE_BLOCKED;
                    if(STATE_BLOCKED.equals(bstate) && STATE_PAUSED.equals(state))
                        state = STATE_PAUSED_BLOCKED;
            }
            if (existingTrigger) {
                if (!replaceExisting) { throw new ObjectAlreadyExistsException(
                        newTrigger); }
                if (newTrigger instanceof SimpleTrigger) {
                    getDelegate().updateSimpleTrigger(conn,
                            (SimpleTrigger) newTrigger);
                } else if (newTrigger instanceof CronTrigger) {
                    getDelegate().updateCronTrigger(conn,
                            (CronTrigger) newTrigger);
                } else {
                    getDelegate().updateBlobTrigger(conn, newTrigger);
                }
                getDelegate().updateTrigger(conn, newTrigger, state, job);
            } else {
                getDelegate().insertTrigger(conn, newTrigger, state, job);
                if (newTrigger instanceof SimpleTrigger) {
                    getDelegate().insertSimpleTrigger(conn,
                            (SimpleTrigger) newTrigger);
                } else if (newTrigger instanceof CronTrigger) {
                    getDelegate().insertCronTrigger(conn,
                            (CronTrigger) newTrigger);
                } else {
                    getDelegate().insertBlobTrigger(conn, newTrigger);
                }
            }
        } catch (Exception e) {
            throw new JobPersistenceException("Couldn't store trigger: "
                    + e.getMessage(), e);
        }
    }

    /**
     * <p>
     * Check existence of a given trigger.
     * </p>
     */
    protected boolean triggerExists(Connection conn, String triggerName,
            String groupName) throws JobPersistenceException {
        try {
            return getDelegate().triggerExists(conn, triggerName, groupName);
        } catch (SQLException e) {
            throw new JobPersistenceException(
                    "Couldn't determine trigger existence (" + groupName + "."
                            + triggerName + "): " + e.getMessage(), e);
        }
    }

    protected boolean removeJob(Connection conn, SchedulingContext ctxt,
            String jobName, String groupName, boolean activeDeleteSafe)
            throws JobPersistenceException {

        try {
            Key[] jobTriggers = getDelegate().selectTriggerNamesForJob(conn,
                    jobName, groupName);
            for (int i = 0; i < jobTriggers.length; ++i) {
                getDelegate().deleteSimpleTrigger(conn,
                        jobTriggers[i].getName(), jobTriggers[i].getGroup());
                getDelegate().deleteCronTrigger(conn, jobTriggers[i].getName(),
                        jobTriggers[i].getGroup());
                getDelegate().deleteBlobTrigger(conn, jobTriggers[i].getName(),
                        jobTriggers[i].getGroup());
                getDelegate().deleteTriggerListeners(conn,
                        jobTriggers[i].getName(), jobTriggers[i].getGroup());
                getDelegate().deleteTrigger(conn, jobTriggers[i].getName(),
                        jobTriggers[i].getGroup());
            }

            getDelegate().deleteJobListeners(conn, jobName, groupName);

            if (getDelegate().deleteJobDetail(conn, jobName, groupName) > 0) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            throw new JobPersistenceException("Couldn't remove job: "
                    + e.getMessage(), e);
        }
    }

    protected JobDetail retrieveJob(Connection conn, SchedulingContext ctxt,
            String jobName, String groupName) throws JobPersistenceException {
        try {
            JobDetail job = getDelegate().selectJobDetail(conn, jobName,
                    groupName, getClassLoadHelper());
            String[] listeners = getDelegate().selectJobListeners(conn,
                    jobName, groupName);
            for (int i = 0; i < listeners.length; ++i) {
                job.addJobListener(listeners[i]);
            }

            return job;
        } catch (ClassNotFoundException e) {
            throw new JobPersistenceException(
                    "Couldn't retrieve job because a required class was not found: "
                            + e.getMessage(), e,
                    SchedulerException.ERR_PERSISTENCE_JOB_DOES_NOT_EXIST);
        } catch (IOException e) {
            throw new JobPersistenceException(
                    "Couldn't retrieve job because the BLOB couldn't be deserialized: "
                            + e.getMessage(), e,
                    SchedulerException.ERR_PERSISTENCE_JOB_DOES_NOT_EXIST);
        } catch (SQLException e) {
            throw new JobPersistenceException("Couldn't retrieve job: "
                    + e.getMessage(), e);
        }
    }

    protected boolean removeTrigger(Connection conn, SchedulingContext ctxt,
            String triggerName, String groupName)
            throws JobPersistenceException {
        boolean removedTrigger = false;
        try {
            // this must be called before we delete the trigger, obviously
            JobDetail job = getDelegate().selectJobForTrigger(conn,
                    triggerName, groupName, getClassLoadHelper());

            getDelegate().deleteSimpleTrigger(conn, triggerName, groupName);
            getDelegate().deleteCronTrigger(conn, triggerName, groupName);
            getDelegate().deleteBlobTrigger(conn, triggerName, groupName);
            getDelegate().deleteTriggerListeners(conn, triggerName, groupName);
            removedTrigger = (getDelegate().deleteTrigger(conn, triggerName,
                    groupName) > 0);

            if (null != job && !job.isDurable()) {
                int numTriggers = getDelegate().selectNumTriggersForJob(conn,
                        job.getName(), job.getGroup());
                if (numTriggers == 0) {
                    removeJob(conn, ctxt, job.getName(), job.getGroup(), true);
                }
            }
        } catch (ClassNotFoundException e) {
            throw new JobPersistenceException("Couldn't remove trigger: "
                    + e.getMessage(), e);
        } catch (SQLException e) {
            throw new JobPersistenceException("Couldn't remove trigger: "
                    + e.getMessage(), e);
        }

        return removedTrigger;
    }

    protected boolean replaceTrigger(Connection conn, SchedulingContext ctxt,
            String triggerName, String groupName, Trigger newTrigger)
    throws JobPersistenceException {
        boolean removedTrigger = false;
        try {
            // this must be called before we delete the trigger, obviously
            JobDetail job = getDelegate().selectJobForTrigger(conn,
                    triggerName, groupName, getClassLoadHelper());

            if(job == null)
                return false;
            
            if(!newTrigger.getJobName().equals(job.getName()) || 
               !newTrigger.getJobGroup().equals(job.getGroup()))
                throw new JobPersistenceException("New trigger is not related to the same job as the old trigger.");
            
            getDelegate().deleteSimpleTrigger(conn, triggerName, groupName);
            getDelegate().deleteCronTrigger(conn, triggerName, groupName);
            getDelegate().deleteBlobTrigger(conn, triggerName, groupName);
            getDelegate().deleteTriggerListeners(conn, triggerName, groupName);
            removedTrigger = (getDelegate().deleteTrigger(conn, triggerName,
                    groupName) > 0);
            
            storeTrigger(conn, ctxt, newTrigger, job, false, STATE_WAITING, false, false);

        } catch (ClassNotFoundException e) {
            throw new JobPersistenceException("Couldn't remove trigger: "
                    + e.getMessage(), e);
        } catch (SQLException e) {
            throw new JobPersistenceException("Couldn't remove trigger: "
                    + e.getMessage(), e);
        }

        return removedTrigger;
    }

    protected Trigger retrieveTrigger(Connection conn, SchedulingContext ctxt,
            String triggerName, String groupName)
            throws JobPersistenceException {
        try {
            Trigger trigger = getDelegate().selectTrigger(conn, triggerName,
                    groupName);
            if (trigger == null) return null;
            String[] listeners = getDelegate().selectTriggerListeners(conn,
                    triggerName, groupName);
            for (int i = 0; i < listeners.length; ++i) {
                trigger.addTriggerListener(listeners[i]);
            }

            return trigger;
        } catch (Exception e) {
            throw new JobPersistenceException("Couldn't retrieve trigger: "
                    + e.getMessage(), e);
        }
    }

    public int getTriggerState(Connection conn, SchedulingContext ctxt,
            String triggerName, String groupName)
            throws JobPersistenceException {
        try {
            String ts = getDelegate().selectTriggerState(conn, triggerName,
                    groupName);

            if (ts == null) return Trigger.STATE_NONE;

            if (ts.equals(STATE_DELETED)) return Trigger.STATE_NONE;

            if (ts.equals(STATE_COMPLETE)) return Trigger.STATE_COMPLETE;

            if (ts.equals(STATE_PAUSED)) return Trigger.STATE_PAUSED;

            if (ts.equals(STATE_PAUSED_BLOCKED)) return Trigger.STATE_PAUSED;

            if (ts.equals(STATE_ERROR)) return Trigger.STATE_ERROR;

            if (ts.equals(STATE_BLOCKED)) return Trigger.STATE_BLOCKED;

            return Trigger.STATE_NORMAL;

        } catch (SQLException e) {
            throw new JobPersistenceException(
                    "Couldn't determine state of trigger (" + groupName + "."
                            + triggerName + "): " + e.getMessage(), e);
        }
    }

    protected void storeCalendar(Connection conn, SchedulingContext ctxt,
            String calName, Calendar calendar, boolean replaceExisting, boolean updateTriggers)
            throws ObjectAlreadyExistsException, JobPersistenceException {
        try {
            boolean existingCal = calendarExists(conn, calName);
            if (existingCal && !replaceExisting) { throw new ObjectAlreadyExistsException(
                    "Calendar with name '" + calName + "' already exists."); }

            if (existingCal) {
                if (getDelegate().updateCalendar(conn, calName, calendar) < 1) { throw new JobPersistenceException(
                        "Couldn't store calendar.  Update failed."); }
                
                if(updateTriggers) {
                    Trigger[] trigs = getDelegate().selectTriggersForCalendar(conn, calName);
                    
                    for(int i=0; i < trigs.length; i++) {
                        trigs[i].updateWithNewCalendar(calendar, getMisfireThreshold());
                        storeTrigger(conn, ctxt, trigs[i], null, true, STATE_WAITING, false, false);
                    }
                }
            } else {
                if (getDelegate().insertCalendar(conn, calName, calendar) < 1) { throw new JobPersistenceException(
                        "Couldn't store calendar.  Insert failed."); }
            }

            calendarCache.put(calName, calendar); // lazy-cache

        } catch (IOException e) {
            throw new JobPersistenceException(
                    "Couldn't store calendar because the BLOB couldn't be serialized: "
                            + e.getMessage(), e);
        } catch (ClassNotFoundException e) {
            throw new JobPersistenceException("Couldn't store calendar: "
                    + e.getMessage(), e);
        }catch (SQLException e) {
            throw new JobPersistenceException("Couldn't store calendar: "
                    + e.getMessage(), e);
        }
    }
    
    protected boolean calendarExists(Connection conn, String calName)
            throws JobPersistenceException {
        try {
            return getDelegate().calendarExists(conn, calName);
        } catch (SQLException e) {
            throw new JobPersistenceException(
                    "Couldn't determine calendar existence (" + calName + "): "
                            + e.getMessage(), e);
        }
    }

    protected boolean removeCalendar(Connection conn, SchedulingContext ctxt,
            String calName) throws JobPersistenceException {
        try {
            if (getDelegate().calendarIsReferenced(conn, calName)) { throw new JobPersistenceException(
                    "Calender cannot be removed if it referenced by a trigger!"); }

            calendarCache.remove(calName);

            return (getDelegate().deleteCalendar(conn, calName) > 0);
        } catch (SQLException e) {
            throw new JobPersistenceException("Couldn't remove calendar: "
                    + e.getMessage(), e);
        }
    }

    protected Calendar retrieveCalendar(Connection conn,
            SchedulingContext ctxt, String calName)
            throws JobPersistenceException {
        // all calendars are persistent, but we lazy-cache them during run
        // time...
        Calendar cal = (Calendar) calendarCache.get(calName);
        if (cal != null) return cal;

        try {
            cal = getDelegate().selectCalendar(conn, calName);
            calendarCache.put(calName, cal); // lazy-cache...
            return cal;
        } catch (ClassNotFoundException e) {
            throw new JobPersistenceException(
                    "Couldn't retrieve calendar because a required class was not found: "
                            + e.getMessage(), e);
        } catch (IOException e) {
            throw new JobPersistenceException(
                    "Couldn't retrieve calendar because the BLOB couldn't be deserialized: "
                            + e.getMessage(), e);
        } catch (SQLException e) {
            throw new JobPersistenceException("Couldn't retrieve calendar: "
                    + e.getMessage(), e);
        }
    }

    protected int getNumberOfJobs(Connection conn, SchedulingContext ctxt)
            throws JobPersistenceException {
        try {
            return getDelegate().selectNumJobs(conn);
        } catch (SQLException e) {
            throw new JobPersistenceException(
                    "Couldn't obtain number of jobs: " + e.getMessage(), e);
        }
    }

    protected int getNumberOfTriggers(Connection conn, SchedulingContext ctxt)
            throws JobPersistenceException {
        try {
            return getDelegate().selectNumTriggers(conn);
        } catch (SQLException e) {
            throw new JobPersistenceException(
                    "Couldn't obtain number of triggers: " + e.getMessage(), e);
        }
    }

    protected int getNumberOfCalendars(Connection conn, SchedulingContext ctxt)
            throws JobPersistenceException {
        try {
            return getDelegate().selectNumCalendars(conn);
        } catch (SQLException e) {
            throw new JobPersistenceException(
                    "Couldn't obtain number of calendars: " + e.getMessage(), e);
        }
    }

    protected String[] getJobNames(Connection conn, SchedulingContext ctxt,
            String groupName) throws JobPersistenceException {
        String[] jobNames = null;

        try {
            jobNames = getDelegate().selectJobsInGroup(conn, groupName);
        } catch (SQLException e) {
            throw new JobPersistenceException("Couldn't obtain job names: "
                    + e.getMessage(), e);
        }

        return jobNames;
    }

    protected String[] getTriggerNames(Connection conn, SchedulingContext ctxt,
            String groupName) throws JobPersistenceException {

        String[] trigNames = null;

        try {
            trigNames = getDelegate().selectTriggersInGroup(conn, groupName);
        } catch (SQLException e) {
            throw new JobPersistenceException("Couldn't obtain trigger names: "
                    + e.getMessage(), e);
        }

        return trigNames;
    }

    protected String[] getJobGroupNames(Connection conn, SchedulingContext ctxt)
            throws JobPersistenceException {

        String[] groupNames = null;

        try {
            groupNames = getDelegate().selectJobGroups(conn);
        } catch (SQLException e) {
            throw new JobPersistenceException("Couldn't obtain job groups: "
                    + e.getMessage(), e);
        }

        return groupNames;
    }

    protected String[] getTriggerGroupNames(Connection conn,
            SchedulingContext ctxt) throws JobPersistenceException {

        String[] groupNames = null;

        try {
            groupNames = getDelegate().selectTriggerGroups(conn);
        } catch (SQLException e) {
            throw new JobPersistenceException(
                    "Couldn't obtain trigger groups: " + e.getMessage(), e);
        }

        return groupNames;
    }

    protected String[] getCalendarNames(Connection conn, SchedulingContext ctxt)
            throws JobPersistenceException {
        try {
            return getDelegate().selectCalendars(conn);
        } catch (SQLException e) {
            throw new JobPersistenceException(
                    "Couldn't obtain trigger groups: " + e.getMessage(), e);
        }
    }

    protected Trigger[] getTriggersForJob(Connection conn,
            SchedulingContext ctxt, String jobName, String groupName)
            throws JobPersistenceException {
        Trigger[] array = null;

        try {
            array = getDelegate()
                    .selectTriggersForJob(conn, jobName, groupName);
        } catch (Exception e) {
            throw new JobPersistenceException(
                    "Couldn't obtain triggers for job: " + e.getMessage(), e);
        }

        return array;
    }

    /**
     * <p>
     * Pause the <code>{@link org.quartz.Trigger}</code> with the given name.
     * </p>
     * 
     * @see #resumeTrigger(Connection, SchedulingContext, String, String)
     */
    public void pauseTrigger(Connection conn, SchedulingContext ctxt,
            String triggerName, String groupName)
            throws JobPersistenceException {

        try {
            String oldState = getDelegate().selectTriggerState(conn,
                    triggerName, groupName);

            if (oldState.equals(STATE_WAITING)
                    || oldState.equals(STATE_ACQUIRED)) {

                getDelegate().updateTriggerState(conn, triggerName,
                        groupName, STATE_PAUSED);
            }
            else if (oldState.equals(STATE_BLOCKED)) {
                getDelegate().updateTriggerState(conn, triggerName,
                        groupName, STATE_PAUSED_BLOCKED);
            }
        } catch (SQLException e) {
            throw new JobPersistenceException("Couldn't pause trigger '"
                    + groupName + "." + triggerName + "': " + e.getMessage(), e);
        }
    }

    protected String getStatusForResumedTrigger(Connection conn,
            SchedulingContext ctxt, TriggerStatus status)
            throws JobPersistenceException {

        try {
            String newState = STATE_WAITING;

            List lst = getDelegate()
                    .selectFiredTriggerRecordsByJob(conn,
                            status.getJobKey().getName(),
                            status.getJobKey().getGroup());

            if (lst.size() > 0) {
                FiredTriggerRecord rec = (FiredTriggerRecord) lst.get(0);
                if (rec.isJobIsStateful()) // TODO: worry about
                        // failed/recovering/volatile job
                        // states?
                        newState = STATE_BLOCKED;
            }

            return newState;

        } catch (SQLException e) {
            throw new JobPersistenceException(
                    "Couldn't determine new state in order to resume trigger '"
                            + status.getKey().getGroup() + "."
                            + status.getKey().getName() + "': "
                            + e.getMessage(), e);
        }

    }

    protected String getNewStatusForTrigger(Connection conn,
            SchedulingContext ctxt, String jobName, String groupName)
            throws JobPersistenceException {

        try {
            String newState = STATE_WAITING;

            List lst = getDelegate().selectFiredTriggerRecordsByJob(conn,
                    jobName, groupName);

            if (lst.size() > 0) {
                FiredTriggerRecord rec = (FiredTriggerRecord) lst.get(0);
                if (rec.isJobIsStateful()) // TODO: worry about
                        // failed/recovering/volatile job
                        // states?
                        newState = STATE_BLOCKED;
            }

            return newState;

        } catch (SQLException e) {
            throw new JobPersistenceException(
                    "Couldn't determine state for new trigger: "
                            + e.getMessage(), e);
        }

    }

    /*
     * private List findTriggersToBeBlocked(Connection conn, SchedulingContext
     * ctxt, String groupName) throws JobPersistenceException {
     * 
     * try { List blockList = new LinkedList();
     * 
     * List affectingJobs =
     * getDelegate().selectStatefulJobsOfTriggerGroup(conn, groupName);
     * 
     * Iterator itr = affectingJobs.iterator(); while(itr.hasNext()) { Key
     * jobKey = (Key) itr.next();
     * 
     * List lst = getDelegate().selectFiredTriggerRecordsByJob(conn,
     * jobKey.getName(), jobKey.getGroup());
     * 
     * This logic is BROKEN...
     * 
     * if(lst.size() > 0) { FiredTriggerRecord rec =
     * (FiredTriggerRecord)lst.get(0); if(rec.isJobIsStateful()) // TODO: worry
     * about failed/recovering/volatile job states? blockList.add(
     * rec.getTriggerKey() ); } }
     * 
     * 
     * return blockList; } catch (SQLException e) { throw new
     * JobPersistenceException ("Couldn't determine states of resumed triggers
     * in group '" + groupName + "': " + e.getMessage(), e); } }
     */

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
     * @see #pauseTrigger(Connection, SchedulingContext, String, String)
     */
    public void resumeTrigger(Connection conn, SchedulingContext ctxt,
            String triggerName, String groupName)
            throws JobPersistenceException {
        try {

            TriggerStatus status = getDelegate().selectTriggerStatus(conn,
                    triggerName, groupName);

            if (status == null || status.getNextFireTime() == null) return;

            boolean blocked = false;
            if(STATE_PAUSED_BLOCKED.equals(status.getStatus()))
                blocked = true;

            String newState = getStatusForResumedTrigger(conn, ctxt, status);

            boolean misfired = false;

            if (status.getNextFireTime().before(new Date())) {
              misfired = updateMisfiredTrigger(conn, ctxt, triggerName, groupName,
                  newState, true);
            }

            if(!misfired) {
                if(blocked)
                    getDelegate().updateTriggerStateFromOtherState(conn,
                            triggerName, groupName, newState, STATE_PAUSED_BLOCKED);
                else
                    getDelegate().updateTriggerStateFromOtherState(conn,
                            triggerName, groupName, newState, STATE_PAUSED);
            } 

        } catch (SQLException e) {
            throw new JobPersistenceException("Couldn't resume trigger '"
                    + groupName + "." + triggerName + "': " + e.getMessage(), e);
        }
    }

    /**
     * <p>
     * Pause all of the <code>{@link org.quartz.Trigger}s</code> in the
     * given group.
     * </p>
     * 
     * @see #resumeTriggerGroup(Connection, SchedulingContext, String)
     */
    public void pauseTriggerGroup(Connection conn, SchedulingContext ctxt,
            String groupName) throws JobPersistenceException {

        try {

            getDelegate().updateTriggerGroupStateFromOtherStates(
                    conn, groupName, STATE_PAUSED, STATE_ACQUIRED,
                    STATE_WAITING, STATE_WAITING);

            getDelegate().updateTriggerGroupStateFromOtherState(
                    conn, groupName, STATE_PAUSED_BLOCKED, STATE_BLOCKED);
            
            if (!getDelegate().isTriggerGroupPaused(conn, groupName)) {
                getDelegate().insertPausedTriggerGroup(conn, groupName);
            }

        } catch (SQLException e) {
            throw new JobPersistenceException("Couldn't pause trigger group '"
                    + groupName + "': " + e.getMessage(), e);
        }
    }

    /**
     * <p>
     * Pause all of the <code>{@link org.quartz.Trigger}s</code> in the
     * given group.
     * </p>
     * 
     * @see #resumeTriggerGroup(Connection, SchedulingContext, String)
     */
    public Set getPausedTriggerGroups(Connection conn, SchedulingContext ctxt) 
        throws JobPersistenceException {

        try {
            return getDelegate().selectPausedTriggerGroups(conn);
        } catch (SQLException e) {
            throw new JobPersistenceException(
                    "Couldn't determine paused trigger groups: " + e.getMessage(), e);
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
     * @see #pauseTriggerGroup(Connection, SchedulingContext, String)
     */
    public void resumeTriggerGroup(Connection conn, SchedulingContext ctxt,
            String groupName) throws JobPersistenceException {

        try {

            getDelegate().deletePausedTriggerGroup(conn, groupName);

            String[] trigNames = getDelegate().selectTriggersInGroup(conn,
                    groupName);

            for (int i = 0; i < trigNames.length; i++) {
                resumeTrigger(conn, ctxt, trigNames[i], groupName);
            }

            // TODO: find an efficient way to resume triggers (better than the
            // above)... logic below is broken because of
            // findTriggersToBeBlocked()
            /*
             * int res =
             * getDelegate().updateTriggerGroupStateFromOtherState(conn,
             * groupName, STATE_WAITING, STATE_PAUSED);
             * 
             * if(res > 0) {
             * 
             * long misfireTime = System.currentTimeMillis();
             * if(getMisfireThreshold() > 0) misfireTime -=
             * getMisfireThreshold();
             * 
             * Key[] misfires =
             * getDelegate().selectMisfiredTriggersInGroupInState(conn,
             * groupName, STATE_WAITING, misfireTime);
             * 
             * List blockedTriggers = findTriggersToBeBlocked(conn, ctxt,
             * groupName);
             * 
             * Iterator itr = blockedTriggers.iterator(); while(itr.hasNext()) {
             * Key key = (Key)itr.next();
             * getDelegate().updateTriggerState(conn, key.getName(),
             * key.getGroup(), STATE_BLOCKED); }
             * 
             * for(int i=0; i < misfires.length; i++) {               String
             * newState = STATE_WAITING;
             * if(blockedTriggers.contains(misfires[i])) newState =
             * STATE_BLOCKED; updateMisfiredTrigger(conn, ctxt,
             * misfires[i].getName(), misfires[i].getGroup(), newState, true); } }
             */

        } catch (SQLException e) {
            throw new JobPersistenceException("Couldn't pause trigger group '"
                    + groupName + "': " + e.getMessage(), e);
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
    public void pauseAll(Connection conn, SchedulingContext ctxt)
            throws JobPersistenceException {

        String[] names = getTriggerGroupNames(conn, ctxt);

        for (int i = 0; i < names.length; i++) {
            pauseTriggerGroup(conn, ctxt, names[i]);
        }

        try {
            if (!getDelegate().isTriggerGroupPaused(conn, ALL_GROUPS_PAUSED)) {
                getDelegate().insertPausedTriggerGroup(conn, ALL_GROUPS_PAUSED);
            }

        } catch (SQLException e) {
            throw new JobPersistenceException(
                    "Couldn't pause all trigger groups: " + e.getMessage(), e);
        }

    }

    /**
     * protected
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
    public void resumeAll(Connection conn, SchedulingContext ctxt)
            throws JobPersistenceException {

        String[] names = getTriggerGroupNames(conn, ctxt);

        for (int i = 0; i < names.length; i++) {
            resumeTriggerGroup(conn, ctxt, names[i]);
        }

        try {
            getDelegate().deletePausedTriggerGroup(conn, ALL_GROUPS_PAUSED);
        } catch (SQLException e) {
            throw new JobPersistenceException(
                    "Couldn't resume all trigger groups: " + e.getMessage(), e);
        }

    }

    private static long ftrCtr = System.currentTimeMillis();

    protected synchronized String getFiredTriggerRecordId() {
        return getInstanceId() + ftrCtr++;
    }

    // TODO: this really ought to return something like a FiredTriggerBundle,
    // so that the fireInstanceId doesn't have to be on the trigger...
    protected Trigger acquireNextTrigger(Connection conn, SchedulingContext ctxt, long noLaterThan)
            throws JobPersistenceException {
        Trigger nextTrigger = null;

        boolean acquiredOne = false;

        do {
            try {
                getDelegate().updateTriggerStateFromOtherStatesBeforeTime(conn,
                        STATE_MISFIRED, STATE_WAITING, STATE_WAITING,
                        getMisfireTime()); // only waiting

                long nextFireTime = getDelegate().selectNextFireTime(conn);

                if (nextFireTime == 0 || nextFireTime > noLaterThan) 
                    return null;

                Key triggerKey = null;
                do {
                    triggerKey = getDelegate().selectTriggerForFireTime(conn,
                            nextFireTime);
                    if (null != triggerKey) {

                        int res = getDelegate()
                                .updateTriggerStateFromOtherState(conn,
                                        triggerKey.getName(),
                                        triggerKey.getGroup(), STATE_ACQUIRED,
                                        STATE_WAITING);

                        if (res <= 0) continue;

                        nextTrigger = retrieveTrigger(conn, ctxt, triggerKey
                                .getName(), triggerKey.getGroup());

                        if(nextTrigger == null) continue;
                        
                        nextTrigger
                                .setFireInstanceId(getFiredTriggerRecordId());
                        getDelegate().insertFiredTrigger(conn, nextTrigger,
                                STATE_ACQUIRED, null);

                        acquiredOne = true;
                    }
                } while (triggerKey != null && !acquiredOne);
            } catch (Exception e) {
                throw new JobPersistenceException(
                        "Couldn't acquire next trigger: " + e.getMessage(), e);
            }

        } while (!acquiredOne);

        return nextTrigger;
    }

    protected void releaseAcquiredTrigger(Connection conn,
            SchedulingContext ctxt, Trigger trigger)
            throws JobPersistenceException {
        try {
            getDelegate().updateTriggerStateFromOtherState(conn,
                    trigger.getName(), trigger.getGroup(), STATE_WAITING,
                    STATE_ACQUIRED);
            getDelegate().deleteFiredTrigger(conn, trigger.getFireInstanceId());
        } catch (SQLException e) {
            throw new JobPersistenceException(
                    "Couldn't release acquired trigger: " + e.getMessage(), e);
        }
    }

    protected TriggerFiredBundle triggerFired(Connection conn,
            SchedulingContext ctxt, Trigger trigger)
            throws JobPersistenceException {
        JobDetail job = null;
        Calendar cal = null;

        // Make sure trigger wasn't deleted, paused, or completed...
        try { // if trigger was deleted, state will be STATE_DELETED
            String state = getDelegate().selectTriggerState(conn,
                    trigger.getName(), trigger.getGroup());
            if (!state.equals(STATE_ACQUIRED)) return null;
        } catch (SQLException e) {
            throw new JobPersistenceException("Couldn't select trigger state: "
                    + e.getMessage(), e);
        }

        try {
            job = retrieveJob(conn, ctxt, trigger.getJobName(), trigger
                    .getJobGroup());
            if (job == null) { return null; }
        } catch (JobPersistenceException jpe) {
            try {
                getLog().error("Error retrieving job, setting trigger state to ERROR.", jpe);
                getDelegate().updateTriggerState(conn, trigger.getName(),
                        trigger.getGroup(), STATE_ERROR);
            } catch (SQLException sqle) {
                getLog().error("Unable to set trigger state to ERROR.", sqle);
            }
            throw jpe;
        }

        if (trigger.getCalendarName() != null) {
            cal = retrieveCalendar(conn, ctxt, trigger.getCalendarName());
            if (cal == null) { return null; }
        }

        try {
            getDelegate().deleteFiredTrigger(conn, trigger.getFireInstanceId());
            getDelegate().insertFiredTrigger(conn, trigger, STATE_EXECUTING,
                    job);
        } catch (SQLException e) {
            throw new JobPersistenceException("Couldn't insert fired trigger: "
                    + e.getMessage(), e);
        }

        Date prevFireTime = trigger.getPreviousFireTime();

        // call triggered - to update the trigger's next-fire-time state...
        trigger.triggered(cal);

        String state = STATE_WAITING;
        boolean force = true;
        
        if (job.isStateful()) {
            state = STATE_BLOCKED;
            force = false;
            try {
                getDelegate().updateTriggerStatesForJobFromOtherState(conn, job.getName(),
                        job.getGroup(), STATE_BLOCKED, STATE_WAITING);
                getDelegate().updateTriggerStatesForJobFromOtherState(conn, job.getName(),
                        job.getGroup(), STATE_BLOCKED, STATE_ACQUIRED);
                getDelegate().updateTriggerStatesForJobFromOtherState(conn, job.getName(),
                        job.getGroup(), STATE_PAUSED_BLOCKED, STATE_PAUSED);
            } catch (SQLException e) {
                throw new JobPersistenceException(
                        "Couldn't update states of blocked triggers: "
                                + e.getMessage(), e);
            }
        } 
            
        if (trigger.getNextFireTime() == null) {
                state = STATE_COMPLETE;
                force = true;
        }

        storeTrigger(conn, ctxt, trigger, job, true, state, force, false);

        job.getJobDataMap().clearDirtyFlag();

        return new TriggerFiredBundle(job, trigger, cal, trigger.getGroup()
                .equals(Scheduler.DEFAULT_RECOVERY_GROUP), new Date(), trigger
                .getPreviousFireTime(), prevFireTime, trigger.getNextFireTime());
    }

    protected void triggeredJobComplete(Connection conn,
            SchedulingContext ctxt, Trigger trigger, JobDetail jobDetail,
            int triggerInstCode) throws JobPersistenceException {
        try {
            if (triggerInstCode == Trigger.INSTRUCTION_DELETE_TRIGGER) {
                if(trigger.getNextFireTime() == null) { 
                    // double check for possible reschedule within job 
                    // execution, which would cancel the need to delete...
                    TriggerStatus stat = getDelegate().selectTriggerStatus(
                            conn, trigger.getName(), trigger.getGroup());
                    if(stat != null && stat.getNextFireTime() == null) {
                        removeTrigger(conn, ctxt, trigger.getName(), trigger.getGroup());
                    }
                }
                else{
                    removeTrigger(conn, ctxt, trigger.getName(), trigger.getGroup());
                }
            } else if (triggerInstCode == Trigger.INSTRUCTION_SET_TRIGGER_COMPLETE) {
                getDelegate().updateTriggerState(conn, trigger.getName(),
                        trigger.getGroup(), STATE_COMPLETE);
            } else if (triggerInstCode == Trigger.INSTRUCTION_SET_TRIGGER_ERROR) {
                getLog().info("Trigger " + trigger.getFullName() + " set to ERROR state.");
                getDelegate().updateTriggerState(conn, trigger.getName(),
                        trigger.getGroup(), STATE_ERROR);
            } else if (triggerInstCode == Trigger.INSTRUCTION_SET_ALL_JOB_TRIGGERS_COMPLETE) {
                getDelegate().updateTriggerStatesForJob(conn,
                        trigger.getJobName(), trigger.getJobGroup(),
                        STATE_COMPLETE);
            } else if (triggerInstCode == Trigger.INSTRUCTION_SET_ALL_JOB_TRIGGERS_ERROR) {
                getLog().info("All triggers of Job " + 
                        trigger.getFullJobName() + " set to ERROR state.");
                getDelegate().updateTriggerStatesForJob(conn,
                        trigger.getJobName(), trigger.getJobGroup(),
                        STATE_ERROR);
            }

            if (jobDetail.isStateful()) {
                getDelegate().updateTriggerStatesForJobFromOtherState(conn,
                        jobDetail.getName(), jobDetail.getGroup(),
                        STATE_WAITING, STATE_BLOCKED);

                getDelegate().updateTriggerStatesForJobFromOtherState(conn,
                        jobDetail.getName(), jobDetail.getGroup(),
                        STATE_PAUSED, STATE_PAUSED_BLOCKED);

                try {
                    if (jobDetail.getJobDataMap().isDirty()) {
                            getDelegate().updateJobData(conn, jobDetail);
                    }
                } catch (IOException e) {
                    throw new JobPersistenceException(
                            "Couldn't serialize job data: " + e.getMessage(), e);
                } catch (SQLException e) {
                    throw new JobPersistenceException(
                            "Couldn't update job data: " + e.getMessage(), e);
                }
            }
        } catch (SQLException e) {
            throw new JobPersistenceException(
                    "Couldn't update trigger state(s): " + e.getMessage(), e);
        }

        try {
            getDelegate().deleteFiredTrigger(conn, trigger.getFireInstanceId());
        } catch (SQLException e) {
            throw new JobPersistenceException("Couldn't delete fired trigger: "
                    + e.getMessage(), e);
        }
    }

    /**
     * <P>
     * Get the driver delegate for DB operations.
     * </p>
     */
    protected DriverDelegate getDelegate() throws NoSuchDelegateException {
        if (null == delegate) {
            try {
                if(delegateClassName != null)
                    delegateClass = 
                        getClassLoadHelper().loadClass(delegateClassName);
                
                Constructor ctor = null;
                Object[] ctorParams = null;
                if (canUseProperties()) {
                    Class[] ctorParamTypes = new Class[]{Log.class,
                            String.class, String.class, Boolean.class};
                    ctor = delegateClass.getConstructor(ctorParamTypes);
                    ctorParams = new Object[]{getLog(), tablePrefix,
                            instanceId, new Boolean(canUseProperties())};
                } else {
                    Class[] ctorParamTypes = new Class[]{Log.class,
                            String.class, String.class};
                    ctor = delegateClass.getConstructor(ctorParamTypes);
                    ctorParams = new Object[]{getLog(), tablePrefix, instanceId};
                }

                delegate = (DriverDelegate) ctor.newInstance(ctorParams);
            } catch (NoSuchMethodException e) {
                throw new NoSuchDelegateException(
                        "Couldn't find delegate constructor: " + e.getMessage());
            } catch (InstantiationException e) {
                throw new NoSuchDelegateException("Couldn't create delegate: "
                        + e.getMessage());
            } catch (IllegalAccessException e) {
                throw new NoSuchDelegateException("Couldn't create delegate: "
                        + e.getMessage());
            } catch (InvocationTargetException e) {
                throw new NoSuchDelegateException("Couldn't create delegate: "
                        + e.getMessage());
            } catch (ClassNotFoundException e) {
                throw new NoSuchDelegateException("Couldn't load delegate class: "
                        + e.getMessage());
            }
        }

        return delegate;
    }

    protected Semaphore getLockHandler() {
        return lockHandler;
    }

    //---------------------------------------------------------------------------
    // Management methods
    //---------------------------------------------------------------------------

    protected abstract boolean doRecoverMisfires()
            throws JobPersistenceException;

    protected void signalSchedulingChange() {
        signaler.signalSchedulingChange();
    }

    //---------------------------------------------------------------------------
    // Cluster management methods
    //---------------------------------------------------------------------------

    protected abstract boolean doCheckin() throws JobPersistenceException;

    protected boolean firstCheckIn = true;

    protected long lastCheckin = System.currentTimeMillis();

    /**
     * Get a list of all scheduler instances in the cluster that may have failed.
     * This includes this scheduler if it has no recoverer and is checking for the
     * first time.
     */
    protected List findFailedInstances(Connection conn)
            throws JobPersistenceException {
    
        List failedInstances = new LinkedList();
        boolean selfFailed = false;
        
        long timeNow = System.currentTimeMillis();
        
        try {
            List states = getDelegate().selectSchedulerStateRecords(conn, null);
            // build map of states by Id...
            HashMap statesById = new HashMap();
            Iterator itr = states.iterator();
            while (itr.hasNext()) {
                SchedulerStateRecord rec = (SchedulerStateRecord) itr.next();
            	statesById.put(rec.getSchedulerInstanceId(), rec);
            }        

            itr = states.iterator();
            while (itr.hasNext()) {
                SchedulerStateRecord rec = (SchedulerStateRecord) itr.next();
        
                // find own record...
                if (rec.getSchedulerInstanceId().equals(getInstanceId())) {
                    if (firstCheckIn) {
                        if (rec.getRecoverer() == null) {
                            failedInstances.add(rec);
                        } else {
                            // make sure the recoverer hasn't died itself!
                            SchedulerStateRecord recOrec = (SchedulerStateRecord)statesById.get(rec.getRecoverer());
                            
                            long failedIfAfter = (recOrec == null) ? timeNow : calcFailedIfAfter(recOrec);

                            // if it has failed, then let's become the recoverer
                            if( failedIfAfter < timeNow || recOrec == null) {
                                failedInstances.add(rec);
                            }
                        }
                    }
                    // TODO: revisit when handle self-failed-out impled (see TODO in clusterCheckIn() below)
                    // else if (rec.getRecoverer() != null) {
                    //     selfFailed = true;
                    // }
                } else {
                    // find failed instances...
                    long failedIfAfter = calcFailedIfAfter(rec);
        
                    if (rec.getRecoverer() == null) {
                       if (failedIfAfter < timeNow) {
                           failedInstances.add(rec);
                       }
                    } else {
                        // make sure the recoverer hasn't died itself!
                        SchedulerStateRecord recOrec = (SchedulerStateRecord)statesById.get(rec.getRecoverer());

                        failedIfAfter = (recOrec == null) ? timeNow : calcFailedIfAfter(recOrec);

                        // if it has failed, then let's become the recoverer
                        if (failedIfAfter < timeNow || recOrec == null) {
                            failedInstances.add(rec);
                        }
                    }
                }
            }
        } catch (Exception e) {
            lastCheckin = System.currentTimeMillis();
            throw new JobPersistenceException("Failure identifying failed instances when checking-in: "
                    + e.getMessage(), e);
        }
    
        return failedInstances;
    }
    
    protected long calcFailedIfAfter(SchedulerStateRecord rec) {
    	return rec.getCheckinTimestamp() +
	        Math.max(rec.getCheckinInterval(), 
	        		(System.currentTimeMillis() - lastCheckin)) +
	        7500L;
    }
    
    protected List clusterCheckIn(Connection conn)
            throws JobPersistenceException {

        List failedInstances = findFailedInstances(conn);
        
        try {
            // TODO: handle self-failed-out

            // check in...
            lastCheckin = System.currentTimeMillis();
            if(getDelegate().updateSchedulerState(conn, getInstanceId(), lastCheckin, null) == 0) {
                getDelegate().insertSchedulerState(conn, getInstanceId(),
                        lastCheckin, getClusterCheckinInterval(), null);
            }
            
        } catch (Exception e) {
            throw new JobPersistenceException("Failure updating scheduler state when checking-in: "
                    + e.getMessage(), e);
        }

        return failedInstances;
    }

    protected void clusterRecover(Connection conn, List failedInstances)
            throws JobPersistenceException {

        if (failedInstances.size() > 0) {

            long recoverIds = System.currentTimeMillis();

            logWarnIfNonZero(failedInstances.size(),
                    "ClusterManager: detected " + failedInstances.size()
                            + " failed or restarted instances.");
            try {
                Iterator itr = failedInstances.iterator();
                while (itr.hasNext()) {
                    SchedulerStateRecord rec = (SchedulerStateRecord) itr
                            .next();

                    getLog().info(
                            "ClusterManager: Scanning for instance \""
                                    + rec.getSchedulerInstanceId()
                                    + "\"'s failed in-progress jobs.");

                    List firedTriggerRecs = getDelegate()
                            .selectInstancesFiredTriggerRecords(conn,
                                    rec.getSchedulerInstanceId());

                    int acquiredCount = 0;
                    int recoveredCount = 0;
                    int otherCount = 0;

                    Iterator ftItr = firedTriggerRecs.iterator();
                    while (ftItr.hasNext()) {
                        FiredTriggerRecord ftRec = (FiredTriggerRecord) ftItr
                                .next();

                        Key tKey = ftRec.getTriggerKey();
                        Key jKey = ftRec.getJobKey();

                        // release blocked triggers..
                        if (ftRec.getFireInstanceState().equals(STATE_BLOCKED)) {
                            getDelegate()
                                    .updateTriggerStatesForJobFromOtherState(
                                            conn, jKey.getName(),
                                            jKey.getGroup(), STATE_WAITING,
                                            STATE_BLOCKED);
                        }
                        if (ftRec.getFireInstanceState().equals(STATE_PAUSED_BLOCKED)) {
                            getDelegate()
                                    .updateTriggerStatesForJobFromOtherState(
                                            conn, jKey.getName(),
                                            jKey.getGroup(), STATE_PAUSED,
                                            STATE_PAUSED_BLOCKED);
                        }

                        // release acquired triggers..
                        if (ftRec.getFireInstanceState().equals(STATE_ACQUIRED)) {
                            getDelegate().updateTriggerStateFromOtherState(
                                    conn, tKey.getName(), tKey.getGroup(),
                                    STATE_WAITING, STATE_ACQUIRED);
                            acquiredCount++;
                        }// handle jobs marked for recovery that were not fully
                        // executed..
                        else if (ftRec.isJobRequestsRecovery()) {
                            if (jobExists(conn, jKey.getName(), jKey.getGroup())) {
                                SimpleTrigger rcvryTrig = new SimpleTrigger(
                                        "recover_"
                                                + rec.getSchedulerInstanceId()
                                                + "_"
                                                + String.valueOf(recoverIds++),
                                        Scheduler.DEFAULT_RECOVERY_GROUP,
                                        new Date(ftRec.getFireTimestamp()));
                                rcvryTrig.setJobName(jKey.getName());
                                rcvryTrig.setJobGroup(jKey.getGroup());
                                rcvryTrig
                                        .setMisfireInstruction(SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);
                                JobDataMap jd = getDelegate().selectTriggerJobDataMap(conn, tKey.getName(), tKey.getGroup());
                                jd.put("QRTZ_FAILED_JOB_ORIG_TRIGGER_NAME", tKey.getName());
                                jd.put("QRTZ_FAILED_JOB_ORIG_TRIGGER_GROUP", tKey.getGroup());
                                jd.put("QRTZ_FAILED_JOB_ORIG_TRIGGER_FIRETIME_IN_MILLISECONDS_AS_STRING", String.valueOf(ftRec.getFireTimestamp()));
                                rcvryTrig.setJobDataMap(jd);

                                rcvryTrig.computeFirstFireTime(null);
                                storeTrigger(conn, null, rcvryTrig, null, false,
                                        STATE_WAITING, false, true);
                                recoveredCount++;
                            } else {
                                getLog()
                                        .warn(
                                                "ClusterManager: failed job '"
                                                        + jKey
                                                        + "' no longer exists, cannot schedule recovery.");
                                otherCount++;
                            }
                        } else {
                            otherCount++;
                        }

                        // free up stateful job's triggers
                        if (ftRec.isJobIsStateful()) {
                            getDelegate()
                                .updateTriggerStatesForJobFromOtherState(
                                        conn, jKey.getName(),
                                        jKey.getGroup(), STATE_WAITING,
                                        STATE_BLOCKED);
                            getDelegate()
                                .updateTriggerStatesForJobFromOtherState(
                                        conn, jKey.getName(),
                                        jKey.getGroup(), STATE_PAUSED,
                                        STATE_PAUSED_BLOCKED);
                        }
                    }

                    getDelegate().deleteFiredTriggers(conn,
                            rec.getSchedulerInstanceId());

                    logWarnIfNonZero(acquiredCount,
                            "ClusterManager: ......Freed " + acquiredCount
                                    + " acquired trigger(s).");
                    logWarnIfNonZero(recoveredCount,
                            "ClusterManager: ......Scheduled " + recoveredCount
                                    + " recoverable job(s) for recovery.");
                    logWarnIfNonZero(otherCount,
                            "ClusterManager: ......Cleaned-up " + otherCount
                                    + " other failed job(s).");

                    getDelegate().deleteSchedulerState(conn,
                            rec.getSchedulerInstanceId());

                    // update record to show that recovery was handled
                    if (rec.getSchedulerInstanceId().equals(getInstanceId())) {
                        getDelegate().insertSchedulerState(conn,
                                rec.getSchedulerInstanceId(), System.currentTimeMillis(),
                                rec.getCheckinInterval(), null);
                    }

                }
            } catch (Exception e) {
                throw new JobPersistenceException("Failure recovering jobs: "
                        + e.getMessage(), e);
            }
        }
    }

    protected void logWarnIfNonZero(int val, String warning) {
        if (val > 0) getLog().info(warning);
        else
            getLog().debug(warning);
    }

    /**
     * Closes the supplied connection
     *
     * @param conn (Optional)
     * @throws JobPersistenceException thrown if a SQLException occurs when the
     * connection is closed
     */
    protected void closeConnection(Connection conn)
        throws JobPersistenceException {

        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                throw new JobPersistenceException(
                    "Couldn't close jdbc connection. "+e.getMessage(), e);
            }
        }
    }

    /**
     * Rollback the supplied connection
     *
     * @param conn (Optional)
     * @throws JobPersistenceException thrown if a SQLException occurs when the
     * connection is rolled back
     */
    protected void rollbackConnection(Connection conn)
        throws JobPersistenceException {

        if (conn != null) {
            try {
                conn.rollback();
            } catch (SQLException e) {
                throw new JobPersistenceException(
                    "Couldn't rollback jdbc connection. "+e.getMessage(), e);
            }
        }
    }
    /**
     * Commit the supplied connection
     *
     * @param conn (Optional)
     * @throws JobPersistenceException thrown if a SQLException occurs when the
     * connection is committed
     */
    protected void commitConnection(Connection conn)
        throws JobPersistenceException {

        if (conn != null) {
            try {
                conn.commit();
            } catch (SQLException e) {
                throw new JobPersistenceException(
                    "Couldn't commit jdbc connection. "+e.getMessage(), e);
            }
        }
    }

    /////////////////////////////////////////////////////////////////////////////
    //
    // ClusterManager Thread
    //
    /////////////////////////////////////////////////////////////////////////////

    class ClusterManager extends Thread {

        private boolean shutdown = false;

        private JobStoreSupport js;

        private int numFails = 0;
        
        ClusterManager(JobStoreSupport js) {
            this.js = js;
            this.setPriority(Thread.NORM_PRIORITY + 2);
            this.setName("QuartzScheduler_" + instanceName + "-" + instanceId + "_ClusterManager");
        }

        public void initialize() {
            this.manage();
            this.start();
        }

        public void shutdown() {
            shutdown = true;
            this.interrupt();
        }

        private boolean manage() {
            boolean res = false;
            try {

                res = js.doCheckin();

                numFails = 0;
                getLog().debug("ClusterManager: Check-in complete.");
            } catch (Exception e) {
                if(numFails % 4 == 0)
                    getLog().error(
                        "ClusterManager: Error managing cluster: "
                                + e.getMessage(), e);
                numFails++;
            }
            return res;
        }

        public void run() {
            while (!shutdown) {

                if (!shutdown) {
                    long timeToSleep = getClusterCheckinInterval();
                    long transpiredTime = (System.currentTimeMillis() - lastCheckin);
                    timeToSleep = timeToSleep - transpiredTime;
                    if (timeToSleep <= 0) timeToSleep = 100L;

                    if(numFails > 0) timeToSleep = Math.max(getDbRetryInterval(), timeToSleep);
                    
                    try {
                        Thread.sleep(timeToSleep);
                    } catch (Exception ignore) {
                    }
                }

                if (!shutdown && this.manage()) signalSchedulingChange();

            }//while !shutdown
        }
    }

    /////////////////////////////////////////////////////////////////////////////
    //
    // MisfireHandler Thread
    //
    /////////////////////////////////////////////////////////////////////////////

    class MisfireHandler extends Thread {

        private boolean shutdown = false;

        private JobStoreSupport js;
        
        private int numFails = 0;
        

        MisfireHandler(JobStoreSupport js) {
            this.js = js;
            this.setName("QuartzScheduler_" + instanceName + "-" + instanceId + "_MisfireHandler");
        }

        public void initialize() {
            //this.manage();
            this.start();
        }

        public void shutdown() {
            shutdown = true;
            this.interrupt();
        }

        private boolean manage() {
            try {
                getLog().debug("MisfireHandler: scanning for misfires...");

                boolean res = js.doRecoverMisfires();
                numFails = 0;
                return res;
            } catch (Exception e) {
                if(numFails % 4 == 0)
                    getLog().error(
                        "MisfireHandler: Error handling misfires: "
                                + e.getMessage(), e);
                numFails++;
            }
            return false;
        }

        public void run() {
            
            while (!shutdown) {

                long sTime = System.currentTimeMillis();

                boolean moreToDo = this.manage();

                if (lastRecoverCount > 0) signalSchedulingChange();

                long spanTime = System.currentTimeMillis() - sTime;

                if (!shutdown && !moreToDo) {
                    long timeToSleep = getMisfireThreshold() - spanTime;
                    if (timeToSleep <= 0) timeToSleep = 50L;

                    if(numFails > 0) timeToSleep = Math.max(getDbRetryInterval(), timeToSleep);
                    
                    if (timeToSleep > 0) try {
                        Thread.sleep(timeToSleep);
                    } catch (Exception ignore) {
                    }
                }
                else if(moreToDo) { // short pause to help balance threads...
                    try {
                        Thread.sleep(50);
                    } catch (Exception ignore) {
                    }                    
                }

            }//while !shutdown
        }
    }

}

// EOF
