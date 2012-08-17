
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
package org.quartz.core;

import java.io.IOException;
import java.io.InputStream;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Calendar;
import org.quartz.InterruptableJob;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;
import org.quartz.JobPersistenceException;
import org.quartz.ObjectAlreadyExistsException;
import org.quartz.Scheduler;
import org.quartz.SchedulerContext;
import org.quartz.SchedulerException;
import org.quartz.SchedulerListener;
import org.quartz.Trigger;
import org.quartz.TriggerListener;
import org.quartz.UnableToInterruptJobException;
import org.quartz.impl.SchedulerRepository;
import org.quartz.simpl.SimpleJobFactory;
import org.quartz.spi.JobFactory;
import org.quartz.spi.SchedulerPlugin;
import org.quartz.spi.SchedulerSignaler;

/**
 * <p>
 * This is the heart of Quartz, an indirect implementation of the <code>{@link org.quartz.Scheduler}</code>
 * interface, containing methods to schedule <code>{@link org.quartz.Job}</code>s,
 * register <code>{@link org.quartz.JobListener}</code> instances, etc.
 * </p>// TODO: more docs...
 * 
 * @see org.quartz.Scheduler
 * @see org.quartz.core.QuartzSchedulerThread
 * @see org.quartz.spi.JobStore
 * @see org.quartz.spi.ThreadPool
 * 
 * @author James House
 */
public class QuartzScheduler implements RemotableQuartzScheduler {

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * 
     * Constants.
     * 
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    private static String VERSION_MAJOR = "UNKNOWN";
    private static String VERSION_MINOR = "UNKNOWN";
    private static String VERSION_ITERATION = "UNKNOWN";

    static {
        Properties props = new Properties();
        try {
            InputStream is = 
                QuartzScheduler.class.getResourceAsStream("/build.properties");
            if(is != null) {
                props.load(is);
                VERSION_MAJOR = props.getProperty("version.major");
                VERSION_MINOR = props.getProperty("version.minor");
                VERSION_ITERATION = props.getProperty("version.iter");
            }
        } catch (IOException e) {
            getLog().error("Error loading version info from build.properties.", e);
        }
    }
    

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * 
     * Data members.
     * 
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    private QuartzSchedulerResources resources;

    private QuartzSchedulerThread schedThread;

    private ThreadGroup threadGroup;

    private SchedulerContext context = new SchedulerContext();

    private HashMap jobListeners = new HashMap(10);

    private ArrayList globalJobListeners = new ArrayList(10);

    private HashMap triggerListeners = new HashMap(10);

    private ArrayList globalTriggerListeners = new ArrayList(10);

    private ArrayList schedulerListeners = new ArrayList(10);

    private ArrayList schedulerPlugins = new ArrayList(10);

    private JobFactory jobFactory = new SimpleJobFactory();
    
    ExecutingJobsManager jobMgr = null;

    ErrorLogger errLogger = null;

    private SchedulerSignaler signaler;

    private Random random = new Random();

    private ArrayList holdToPreventGC = new ArrayList(5);

    private boolean signalOnSchedulingChange = true;

    private boolean closed = false;

    private Date initialStart = null;

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * 
     * Constructors.
     * 
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    /**
     * <p>
     * Create a <code>QuartzScheduler</code> with the given configuration
     * properties.
     * </p>
     * 
     * @see QuartzSchedulerResources
     */
    public QuartzScheduler(QuartzSchedulerResources resources,
            SchedulingContext ctxt, long idleWaitTime, long dbRetryInterval)
            throws SchedulerException {
        this.resources = resources;
        try {
            bind();
        } catch (Exception re) {
            throw new SchedulerException(
                    "Unable to bind scheduler to RMI Registry.", re);
        }

        this.schedThread = new QuartzSchedulerThread(this, resources, ctxt);
        if (idleWaitTime > 0) this.schedThread.setIdleWaitTime(idleWaitTime);
        if (dbRetryInterval > 0)
                this.schedThread.setDbFailureRetryInterval(dbRetryInterval);

        jobMgr = new ExecutingJobsManager();
        addGlobalJobListener(jobMgr);
        errLogger = new ErrorLogger();
        addSchedulerListener(errLogger);

        signaler = new SchedulerSignalerImpl(this);
        
        getLog().info("Quartz Scheduler v." + getVersion() + " created.");
    }

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * 
     * Interface.
     * 
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    public String getVersion() {
        return getVersionMajor() + "." + getVersionMinor() + "."
                + getVersionIteration();
    }

    public static String getVersionMajor() {
        return VERSION_MAJOR;
    }

    public static String getVersionMinor() {
        return VERSION_MINOR;
    }

    public static String getVersionIteration() {
        return VERSION_ITERATION;
    }

    public SchedulerSignaler getSchedulerSignaler() {
        return signaler;
    }

    public static Log getLog() {
        return LogFactory.getLog(QuartzScheduler.class);
    }

    /**
     * <p>
     * Bind the scheduler to an RMI registry.
     * </p>
     */
    private void bind() throws RemoteException {
        String host = resources.getRMIRegistryHost();
        // don't export if we're not configured to do so...
        if (host == null || host.length() == 0) return;

        RemotableQuartzScheduler exportable = null;

        if(resources.getRMIServerPort() > 0)
            exportable = (RemotableQuartzScheduler) UnicastRemoteObject
                .exportObject(this, resources.getRMIServerPort());
        else
            exportable = (RemotableQuartzScheduler) UnicastRemoteObject
                .exportObject(this);

        Registry registry = null;

        if (resources.getRMICreateRegistryStrategy().equals(
                QuartzSchedulerResources.CREATE_REGISTRY_AS_NEEDED)) {
            try {
                // First try to get an existing one, instead of creating it,
                // since if
                // we're in a web-app being 'hot' re-depoloyed, then the JVM
                // still
                // has the registry that we created above the first time...
                registry = LocateRegistry.getRegistry(resources
                        .getRMIRegistryPort());
                registry.list();
            } catch (Exception e) {
                registry = LocateRegistry.createRegistry(resources
                        .getRMIRegistryPort());
            }
        } else if (resources.getRMICreateRegistryStrategy().equals(
                QuartzSchedulerResources.CREATE_REGISTRY_ALWAYS)) {
            try {
                registry = LocateRegistry.createRegistry(resources
                        .getRMIRegistryPort());
            } catch (Exception e) {
                // Fall back to an existing one, instead of creating it, since
                // if
                // we're in a web-app being 'hot' re-depoloyed, then the JVM
                // still
                // has the registry that we created above the first time...
                registry = LocateRegistry.getRegistry(resources
                        .getRMIRegistryPort());
            }
        } else {
            registry = LocateRegistry.getRegistry(resources
                    .getRMIRegistryHost(), resources.getRMIRegistryPort());
        }

        registry.rebind(resources.getUniqueIdentifier(), exportable);

        getLog().info("Scheduler bound to RMI registry.");
    }

    /**
     * <p>
     * Un-bind the scheduler from an RMI registry.
     * </p>
     */
    private void unBind() throws RemoteException {
        String host = resources.getRMIRegistryHost();
        // don't un-export if we're not configured to do so...
        if (host == null || host.length() == 0) return;

        Registry registry = LocateRegistry.getRegistry(resources
                .getRMIRegistryHost(), resources.getRMIRegistryPort());

        try {
            registry.unbind(resources.getUniqueIdentifier());
            UnicastRemoteObject.unexportObject(this, true);
        } catch (java.rmi.NotBoundException nbe) {
        }

        getLog().info("Scheduler un-bound from RMI registry.");
    }

    /**
     * <p>
     * Returns the name of the <code>QuartzScheduler</code>.
     * </p>
     */
    public String getSchedulerName() {
        return resources.getName();
    }

    /**
     * <p>
     * Returns the instance Id of the <code>QuartzScheduler</code>.
     * </p>
     */
    public String getSchedulerInstanceId() {
        return resources.getInstanceId();
    }

    /**
     * <p>
     * Returns the name of the <code>QuartzScheduler</code>.
     * </p>
     */
    public ThreadGroup getSchedulerThreadGroup() {
        if (threadGroup == null) {
            threadGroup = new ThreadGroup("QuartzScheduler:"
                    + getSchedulerName());
        }

        return threadGroup;
    }

    public void addNoGCObject(Object obj) {
        holdToPreventGC.add(obj);
    }

    public boolean removeNoGCObject(Object obj) {
        return holdToPreventGC.remove(obj);
    }

    /**
     * <p>
     * Returns the <code>SchedulerContext</code> of the <code>Scheduler</code>.
     * </p>
     */
    public SchedulerContext getSchedulerContext() throws SchedulerException {
        return context;
    }

    public boolean isSignalOnSchedulingChange() {
        return signalOnSchedulingChange;
    }

    public void setSignalOnSchedulingChange(boolean signalOnSchedulingChange) {
        this.signalOnSchedulingChange = signalOnSchedulingChange;
    }

    ///////////////////////////////////////////////////////////////////////////
    ///
    /// Schedululer State Management Methods
    ///
    ///////////////////////////////////////////////////////////////////////////

    /**
     * <p>
     * Starts the <code>QuartzScheduler</code>'s threads that fire <code>{@link org.quartz.Trigger}s</code>.
     * </p>
     * 
     * <p>
     * All <code>{@link org.quartz.Trigger}s</code> that have misfired will
     * be passed to the appropriate TriggerListener(s).
     * </p>
     */
    public void start() throws SchedulerException {

        if (closed)
                throw new SchedulerException(
                        "The Scheduler cannot be restarted after shutdown() has been called.");

        schedThread.togglePause(false);

        if (initialStart == null) {
            initialStart = new Date();
            this.resources.getJobStore().schedulerStarted();            
            startPlugins();
        }

        getLog().info(
                "Scheduler " + resources.getUniqueIdentifier() + " started.");
    }

    /**
     * <p>
     * Temporarily halts the <code>QuartzScheduler</code>'s firing of <code>{@link org.quartz.Trigger}s</code>.
     * </p>
     * 
     * <p>
     * The scheduler is not destroyed, and can be re-started at any time.
     * </p>
     */
    public void standby() {
        schedThread.togglePause(true);
        getLog().info(
                "Scheduler " + resources.getUniqueIdentifier() + " paused.");
    }

    /**
     * <p>
     * Reports whether the <code>Scheduler</code> is paused.
     * </p>
     */
    public boolean isInStandbyMode() {
        return schedThread.isPaused();
    }

    public Date runningSince() {
        return initialStart;
    }

    public int numJobsExecuted() {
        return jobMgr.getNumJobsFired();
    }

    public Class getJobStoreClass() {
        return resources.getJobStore().getClass();
    }

    public boolean supportsPersistence() {
        return resources.getJobStore().supportsPersistence();
    }

    public Class getThreadPoolClass() {
        return resources.getThreadPool().getClass();
    }

    public int getThreadPoolSize() {
        return resources.getThreadPool().getPoolSize();
    }

    /**
     * <p>
     * Halts the <code>QuartzScheduler</code>'s firing of <code>{@link org.quartz.Trigger}s</code>,
     * and cleans up all resources associated with the QuartzScheduler.
     * Equivalent to <code>shutdown(false)</code>.
     * </p>
     * 
     * <p>
     * The scheduler cannot be re-started.
     * </p>
     */
    public void shutdown() {
        shutdown(false);
    }

    /**
     * <p>
     * Halts the <code>QuartzScheduler</code>'s firing of <code>{@link org.quartz.Trigger}s</code>,
     * and cleans up all resources associated with the QuartzScheduler.
     * </p>
     * 
     * <p>
     * The scheduler cannot be re-started.
     * </p>
     * 
     * @param waitForJobsToComplete
     *          if <code>true</code> the scheduler will not allow this method
     *          to return until all currently executing jobs have completed.
     */
    public void shutdown(boolean waitForJobsToComplete) {
        
        if(closed == true)
            return;
        
        getLog().info(
                "Scheduler " + resources.getUniqueIdentifier()
                        + " shutting down.");
        standby();

        closed = true;

        schedThread.halt();

        resources.getThreadPool().shutdown(waitForJobsToComplete);

        if (waitForJobsToComplete) {
            while (jobMgr.getNumJobsCurrentlyExecuting() > 0)
                try {
                    Thread.sleep(100);
                } catch (Exception ignore) {
                }
        }

        resources.getJobStore().shutdown();

        notifySchedulerListenersShutdown();

        shutdownPlugins();

        SchedulerRepository.getInstance().remove(resources.getName());

        holdToPreventGC.clear();

        try {
            unBind();
        } catch (RemoteException re) {
        }

        getLog().info(
                "Scheduler " + resources.getUniqueIdentifier()
                        + " shutdown complete.");
    }

    /**
     * <p>
     * Reports whether the <code>Scheduler</code> has been shutdown.
     * </p>
     */
    public boolean isShutdown() {
        return closed;
    }

    public void validateState() throws SchedulerException {
        if (isShutdown())
                throw new SchedulerException("The Scheduler has been shutdown.");

        // other conditions to check (?)
    }

    /**
     * <p>
     * Return a list of <code>JobExecutionContext</code> objects that
     * represent all currently executing Jobs.
     * </p>
     * 
     * <p>
     * Note that the list returned is an 'instantaneous' snap-shot, and that as
     * soon as it's returned, the true list of executing jobs may be different.
     * </p>
     */
    public List getCurrentlyExecutingJobs() {
        return jobMgr.getExecutingJobs();
    }

    ///////////////////////////////////////////////////////////////////////////
    ///
    /// Scheduling-related Methods
    ///
    ///////////////////////////////////////////////////////////////////////////

    /**
     * <p>
     * Add the <code>{@link org.quartz.Job}</code> identified by the given
     * <code>{@link org.quartz.JobDetail}</code> to the Scheduler, and
     * associate the given <code>{@link org.quartz.Trigger}</code> with it.
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
    public Date scheduleJob(SchedulingContext ctxt, JobDetail jobDetail,
            Trigger trigger) throws SchedulerException {
        validateState();

        jobDetail.validate();

        if (trigger.getJobName() == null) {
            trigger.setJobName(jobDetail.getName());
            trigger.setJobGroup(jobDetail.getGroup());
        } else if (trigger.getJobName() != null
                && !trigger.getJobName().equals(jobDetail.getName())) {
            throw new SchedulerException(
                "Trigger does not reference given job!",
                SchedulerException.ERR_CLIENT_ERROR);
        } else if (trigger.getJobGroup() != null
                && !trigger.getJobGroup().equals(jobDetail.getGroup())) {
            throw new SchedulerException(
                "Trigger does not reference given job!",
                SchedulerException.ERR_CLIENT_ERROR);
        }

        trigger.validate();

        Calendar cal = null;
        if (trigger.getCalendarName() != null) {
            cal = resources.getJobStore().retrieveCalendar(ctxt,
                    trigger.getCalendarName());
        }
        Date ft = trigger.computeFirstFireTime(cal);

        if (ft == null)
                throw new SchedulerException(
                        "Based on configured schedule, the given trigger will never fire.",
                        SchedulerException.ERR_CLIENT_ERROR);

        resources.getJobStore().storeJobAndTrigger(ctxt, jobDetail, trigger);
        notifySchedulerThread();
        notifySchedulerListenersSchduled(trigger);

        return ft;
    }

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
    public Date scheduleJob(SchedulingContext ctxt, Trigger trigger)
            throws SchedulerException {
        validateState();

        trigger.validate();

        Calendar cal = null;
        if (trigger.getCalendarName() != null) {
            cal = resources.getJobStore().retrieveCalendar(ctxt,
                    trigger.getCalendarName());
            if(cal == null)
                throw new SchedulerException(
                    "Calendar not found: " + trigger.getCalendarName(), 
                    SchedulerException.ERR_PERSISTENCE_CALENDAR_DOES_NOT_EXIST);
        }
        Date ft = trigger.computeFirstFireTime(cal);

        if (ft == null)
                throw new SchedulerException(
                        "Based on configured schedule, the given trigger will never fire.",
                        SchedulerException.ERR_CLIENT_ERROR);

        resources.getJobStore().storeTrigger(ctxt, trigger, false);
        notifySchedulerThread();
        notifySchedulerListenersSchduled(trigger);

        return ft;
    }

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
    public void addJob(SchedulingContext ctxt, JobDetail jobDetail,
            boolean replace) throws SchedulerException {
        validateState();

        if (!jobDetail.isDurable() && !replace)
                throw new SchedulerException(
                        "Jobs added with no trigger must be durable.",
                        SchedulerException.ERR_CLIENT_ERROR);

        resources.getJobStore().storeJob(ctxt, jobDetail, replace);
    }

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
    public boolean deleteJob(SchedulingContext ctxt, String jobName,
            String groupName) throws SchedulerException {
        validateState();

        if(groupName == null)
            groupName = Scheduler.DEFAULT_GROUP;
        
        return resources.getJobStore().removeJob(ctxt, jobName, groupName);
    }

    /**
     * <p>
     * Remove the indicated <code>{@link org.quartz.Trigger}</code> from the
     * scheduler.
     * </p>
     */
    public boolean unscheduleJob(SchedulingContext ctxt, String triggerName,
            String groupName) throws SchedulerException {
        validateState();

        if(groupName == null)
            groupName = Scheduler.DEFAULT_GROUP;
        
        if (resources.getJobStore().removeTrigger(ctxt, triggerName, groupName)) {
            notifySchedulerThread();
            notifySchedulerListenersUnschduled(triggerName, groupName);
        } else
            return false;

        return true;
    }


    /**
     * <p>
     * Remove (delete) the <code>{@link org.quartz.Trigger}</code> with the
     * given name, and store the new given one - which must be associated
     * with the same job.
     * </p>
     * 
     * @param triggerName
     *          The name of the <code>Trigger</code> to be removed.
     * @param groupName
     *          The group name of the <code>Trigger</code> to be removed.
     * @param newTrigger
     *          The new <code>Trigger</code> to be stored.
     * @return <code>null</code> if a <code>Trigger</code> with the given
     *         name & group was not found and removed from the store, otherwise
     *         the first fire time of the newly scheduled trigger.
     */
    public Date rescheduleJob(SchedulingContext ctxt, String triggerName,
            String groupName, Trigger newTrigger) throws SchedulerException {
        validateState();

        if(groupName == null)
            groupName = Scheduler.DEFAULT_GROUP;

        newTrigger.validate();

        Calendar cal = null;
        if (newTrigger.getCalendarName() != null) {
            cal = resources.getJobStore().retrieveCalendar(ctxt,
                    newTrigger.getCalendarName());
        }
        Date ft = newTrigger.computeFirstFireTime(cal);

        if (ft == null)
            throw new SchedulerException(
                    "Based on configured schedule, the given trigger will never fire.",
                    SchedulerException.ERR_CLIENT_ERROR);
        
        if (resources.getJobStore().replaceTrigger(ctxt, triggerName, groupName, newTrigger)) {
            notifySchedulerThread();
            notifySchedulerListenersUnschduled(triggerName, groupName);
            notifySchedulerListenersSchduled(newTrigger);
        } else
            return null;

        return ft;
        
    }
    
    
    private String newTriggerId() {
        long r = random.nextLong();
        if (r < 0) r = -r;
        return "MT_"
                + Long.toString(r, 30 + (int) (System.currentTimeMillis() % 7));
    }

    /**
     * <p>
     * Trigger the identified <code>{@link org.quartz.Job}</code> (execute it
     * now) - with a non-volatile trigger.
     * </p>
     */
    public void triggerJob(SchedulingContext ctxt, String jobName,
            String groupName, JobDataMap data) throws SchedulerException {
        validateState();

        if(groupName == null)
            groupName = Scheduler.DEFAULT_GROUP;
        
        Trigger trig = new org.quartz.SimpleTrigger(newTriggerId(),
                Scheduler.DEFAULT_MANUAL_TRIGGERS, jobName, groupName,
                new Date(), null, 0, 0);
        trig.setVolatility(false);
        trig.computeFirstFireTime(null);
        if(data != null)
            trig.setJobDataMap(data);

        boolean collision = true;
        while (collision) {
            try {
                resources.getJobStore().storeTrigger(ctxt, trig, false);
                collision = false;
            } catch (ObjectAlreadyExistsException oaee) {
                trig.setName(newTriggerId());
            }
        }

        notifySchedulerThread();
        notifySchedulerListenersSchduled(trig);
    }

    /**
     * <p>
     * Trigger the identified <code>{@link org.quartz.Job}</code> (execute it
     * now) - with a volatile trigger.
     * </p>
     */
    public void triggerJobWithVolatileTrigger(SchedulingContext ctxt,
            String jobName, String groupName, JobDataMap data) throws SchedulerException {
        validateState();

        if(groupName == null)
            groupName = Scheduler.DEFAULT_GROUP;
        
        Trigger trig = new org.quartz.SimpleTrigger(newTriggerId(),
                Scheduler.DEFAULT_MANUAL_TRIGGERS, jobName, groupName,
                new Date(), null, 0, 0);
        trig.setVolatility(true);
        trig.computeFirstFireTime(null);
        if(data != null)
            trig.setJobDataMap(data);
        
        boolean collision = true;
        while (collision) {
            try {
                resources.getJobStore().storeTrigger(ctxt, trig, false);
                collision = false;
            } catch (ObjectAlreadyExistsException oaee) {
                trig.setName(newTriggerId());
            }
        }

        notifySchedulerThread();
        notifySchedulerListenersSchduled(trig);
    }

    /**
     * <p>
     * Pause the <code>{@link Trigger}</code> with the given name.
     * </p>
     *  
     */
    public void pauseTrigger(SchedulingContext ctxt, String triggerName,
            String groupName) throws SchedulerException {
        validateState();

        if(groupName == null)
            groupName = Scheduler.DEFAULT_GROUP;
        
        resources.getJobStore().pauseTrigger(ctxt, triggerName, groupName);
        notifySchedulerThread();
        notifySchedulerListenersPausedTrigger(triggerName, groupName);
    }

    /**
     * <p>
     * Pause all of the <code>{@link Trigger}s</code> in the given group.
     * </p>
     *  
     */
    public void pauseTriggerGroup(SchedulingContext ctxt, String groupName)
            throws SchedulerException {
        validateState();

        if(groupName == null)
            groupName = Scheduler.DEFAULT_GROUP;
        
        resources.getJobStore().pauseTriggerGroup(ctxt, groupName);
        notifySchedulerThread();
        notifySchedulerListenersPausedTrigger(null, groupName);
    }

    /**
     * <p>
     * Pause the <code>{@link org.quartz.JobDetail}</code> with the given
     * name - by pausing all of its current <code>Trigger</code>s.
     * </p>
     *  
     */
    public void pauseJob(SchedulingContext ctxt, String jobName,
            String groupName) throws SchedulerException {
        validateState();

        if(groupName == null)
            groupName = Scheduler.DEFAULT_GROUP;

        resources.getJobStore().pauseJob(ctxt, jobName, groupName);
        notifySchedulerThread();
        notifySchedulerListenersPausedJob(jobName, groupName);
    }

    /**
     * <p>
     * Pause all of the <code>{@link org.quartz.JobDetail}s</code> in the
     * given group - by pausing all of their <code>Trigger</code>s.
     * </p>
     *  
     */
    public void pauseJobGroup(SchedulingContext ctxt, String groupName)
            throws SchedulerException {
        validateState();

        if(groupName == null)
            groupName = Scheduler.DEFAULT_GROUP;
        
        resources.getJobStore().pauseJobGroup(ctxt, groupName);
        notifySchedulerThread();
        notifySchedulerListenersPausedJob(null, groupName);
    }

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
     */
    public void resumeTrigger(SchedulingContext ctxt, String triggerName,
            String groupName) throws SchedulerException {
        validateState();

        if(groupName == null)
            groupName = Scheduler.DEFAULT_GROUP;
        
        resources.getJobStore().resumeTrigger(ctxt, triggerName, groupName);
        notifySchedulerThread();
        notifySchedulerListenersResumedTrigger(triggerName, groupName);
    }

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
     */
    public void resumeTriggerGroup(SchedulingContext ctxt, String groupName)
            throws SchedulerException {
        validateState();

        if(groupName == null)
            groupName = Scheduler.DEFAULT_GROUP;
        
        resources.getJobStore().resumeTriggerGroup(ctxt, groupName);
        notifySchedulerThread();
        notifySchedulerListenersResumedTrigger(null, groupName);
    }

    public Set getPausedTriggerGroups(SchedulingContext ctxt) throws SchedulerException {
        return resources.getJobStore().getPausedTriggerGroups(ctxt);
    }
    
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
     */
    public void resumeJob(SchedulingContext ctxt, String jobName,
            String groupName) throws SchedulerException {
        validateState();

        if(groupName == null)
            groupName = Scheduler.DEFAULT_GROUP;
        
        resources.getJobStore().resumeJob(ctxt, jobName, groupName);
        notifySchedulerThread();
        notifySchedulerListenersResumedJob(jobName, groupName);
    }

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
     */
    public void resumeJobGroup(SchedulingContext ctxt, String groupName)
            throws SchedulerException {
        validateState();

        if(groupName == null)
            groupName = Scheduler.DEFAULT_GROUP;
        
        resources.getJobStore().resumeJobGroup(ctxt, groupName);
        notifySchedulerThread();
        notifySchedulerListenersResumedJob(null, groupName);
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
     * @see #pause()
     */
    public void pauseAll(SchedulingContext ctxt) throws SchedulerException {
        validateState();

        resources.getJobStore().pauseAll(ctxt);
        notifySchedulerThread();
        notifySchedulerListenersPausedTrigger(null, null);
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
    public void resumeAll(SchedulingContext ctxt) throws SchedulerException {
        validateState();

        resources.getJobStore().resumeAll(ctxt);
        notifySchedulerThread();
        notifySchedulerListenersResumedTrigger(null, null);
    }

    /**
     * <p>
     * Get the names of all known <code>{@link org.quartz.Job}</code> groups.
     * </p>
     */
    public String[] getJobGroupNames(SchedulingContext ctxt)
            throws SchedulerException {
        validateState();

        return resources.getJobStore().getJobGroupNames(ctxt);
    }

    /**
     * <p>
     * Get the names of all the <code>{@link org.quartz.Job}s</code> in the
     * given group.
     * </p>
     */
    public String[] getJobNames(SchedulingContext ctxt, String groupName)
            throws SchedulerException {
        validateState();

        if(groupName == null)
            groupName = Scheduler.DEFAULT_GROUP;
        
        return resources.getJobStore().getJobNames(ctxt, groupName);
    }

    /**
     * <p>
     * Get all <code>{@link Trigger}</code> s that are associated with the
     * identified <code>{@link org.quartz.JobDetail}</code>.
     * </p>
     */
    public Trigger[] getTriggersOfJob(SchedulingContext ctxt, String jobName,
            String groupName) throws SchedulerException {
        validateState();

        if(groupName == null)
            groupName = Scheduler.DEFAULT_GROUP;
        
        return resources.getJobStore().getTriggersForJob(ctxt, jobName,
                groupName);
    }

    /**
     * <p>
     * Get the names of all known <code>{@link org.quartz.Trigger}</code>
     * groups.
     * </p>
     */
    public String[] getTriggerGroupNames(SchedulingContext ctxt)
            throws SchedulerException {
        validateState();

        return resources.getJobStore().getTriggerGroupNames(ctxt);
    }

    /**
     * <p>
     * Get the names of all the <code>{@link org.quartz.Trigger}s</code> in
     * the given group.
     * </p>
     */
    public String[] getTriggerNames(SchedulingContext ctxt, String groupName)
            throws SchedulerException {
        validateState();

        if(groupName == null)
            groupName = Scheduler.DEFAULT_GROUP;
        
        return resources.getJobStore().getTriggerNames(ctxt, groupName);
    }

    /**
     * <p>
     * Get the <code>{@link JobDetail}</code> for the <code>Job</code>
     * instance with the given name and group.
     * </p>
     */
    public JobDetail getJobDetail(SchedulingContext ctxt, String jobName,
            String jobGroup) throws SchedulerException {
        validateState();

        if(jobGroup == null)
            jobGroup = Scheduler.DEFAULT_GROUP;
        
        return resources.getJobStore().retrieveJob(ctxt, jobName, jobGroup);
    }

    /**
     * <p>
     * Get the <code>{@link Trigger}</code> instance with the given name and
     * group.
     * </p>
     */
    public Trigger getTrigger(SchedulingContext ctxt, String triggerName,
            String triggerGroup) throws SchedulerException {
        validateState();

        if(triggerGroup == null)
            triggerGroup = Scheduler.DEFAULT_GROUP;
        
        return resources.getJobStore().retrieveTrigger(ctxt, triggerName,
                triggerGroup);
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
     */
    public int getTriggerState(SchedulingContext ctxt, String triggerName,
            String triggerGroup) throws SchedulerException {
        validateState();

        if(triggerGroup == null)
            triggerGroup = Scheduler.DEFAULT_GROUP;
        
        return resources.getJobStore().getTriggerState(ctxt, triggerName,
                triggerGroup);
    }

    /**
     * <p>
     * Add (register) the given <code>Calendar</code> to the Scheduler.
     * </p>
     * 
     * @throws SchedulerException
     *           if there is an internal Scheduler error, or a Calendar with
     *           the same name already exists, and <code>replace</code> is
     *           <code>false</code>.
     */
    public void addCalendar(SchedulingContext ctxt, String calName,
            Calendar calendar, boolean replace, boolean updateTriggers) throws SchedulerException {
        validateState();

        resources.getJobStore().storeCalendar(ctxt, calName, calendar, replace, updateTriggers);
    }

    /**
     * <p>
     * Delete the identified <code>Calendar</code> from the Scheduler.
     * </p>
     * 
     * @return true if the Calendar was found and deleted.
     * @throws SchedulerException
     *           if there is an internal Scheduler error.
     */
    public boolean deleteCalendar(SchedulingContext ctxt, String calName)
            throws SchedulerException {
        validateState();

        return resources.getJobStore().removeCalendar(ctxt, calName);
    }

    /**
     * <p>
     * Get the <code>{@link Calendar}</code> instance with the given name.
     * </p>
     */
    public Calendar getCalendar(SchedulingContext ctxt, String calName)
            throws SchedulerException {
        validateState();

        return resources.getJobStore().retrieveCalendar(ctxt, calName);
    }

    /**
     * <p>
     * Get the names of all registered <code>{@link Calendar}s</code>.
     * </p>
     */
    public String[] getCalendarNames(SchedulingContext ctxt)
            throws SchedulerException {
        validateState();

        return resources.getJobStore().getCalendarNames(ctxt);
    }

    /**
     * <p>
     * Add the given <code>{@link org.quartz.JobListener}</code> to the
     * <code>Scheduler</code>'s<i>global</i> list.
     * </p>
     * 
     * <p>
     * Listeners in the 'global' list receive notification of execution events
     * for ALL <code>{@link org.quartz.Job}</code>s.
     * </p>
     */
    public void addGlobalJobListener(JobListener jobListener) {
        if (jobListener.getName() == null
                || jobListener.getName().length() == 0)
                throw new IllegalArgumentException(
                        "JobListener name cannot be empty.");

        globalJobListeners.add(jobListener);
    }

    /**
     * <p>
     * Add the given <code>{@link org.quartz.JobListener}</code> to the
     * <code>Scheduler</code>'s list, of registered <code>JobListener</code>s.
     */
    public void addJobListener(JobListener jobListener) {
        if (jobListener.getName() == null
                || jobListener.getName().length() == 0)
                throw new IllegalArgumentException(
                        "JobListener name cannot be empty.");

        jobListeners.put(jobListener.getName(), jobListener);
    }

    /**
     * <p>
     * Remove the given <code>{@link org.quartz.JobListener}</code> from the
     * <code>Scheduler</code>'s list of <i>global</i> listeners.
     * </p>
     * 
     * @return true if the identifed listener was found in the list, and
     *         removed.
     */
    public boolean removeGlobalJobListener(JobListener jobListener) {
        return globalJobListeners.remove(jobListener);
    }

    /**
     * <p>
     * Remove the identifed <code>{@link org.quartz.JobListener}</code> from
     * the <code>Scheduler</code>'s list of registered listeners.
     * </p>
     * 
     * @return true if the identifed listener was found in the list, and
     *         removed.
     */
    public boolean removeJobListener(String name) {
        Object o = jobListeners.remove(name);

        if (o != null) return true;

        return false;
    }

    /**
     * <p>
     * Get a List containing all of the <code>{@link org.quartz.JobListener}</code>
     * s in the <code>Scheduler</code>'s<i>global</i> list.
     * </p>
     */
    public List getGlobalJobListeners() {
        return new LinkedList(globalJobListeners);
    }

    /**
     * <p>
     * Get a Set containing the names of all the <i>non-global</i><code>{@link org.quartz.JobListener}</code>
     * s registered with the <code>Scheduler</code>.
     * </p>
     */
    public Set getJobListenerNames() {
        return Collections.unmodifiableSet(jobListeners.keySet());
    }

    /**
     * <p>
     * Get the <i>non-global</i><code>{@link org.quartz.JobListener}</code>
     * that has the given name.
     * </p>
     */
    public JobListener getJobListener(String name) {
        return (JobListener) jobListeners.get(name);
    }

    /**
     * <p>
     * Add the given <code>{@link org.quartz.TriggerListener}</code> to the
     * <code>Scheduler</code>'s<i>global</i> list.
     * </p>
     * 
     * <p>
     * Listeners in the 'global' list receive notification of execution events
     * for ALL <code>{@link org.quartz.Trigger}</code>s.
     * </p>
     */
    public void addGlobalTriggerListener(TriggerListener triggerListener) {
        if (triggerListener.getName() == null
                || triggerListener.getName().length() == 0)
                throw new IllegalArgumentException(
                        "TriggerListener name cannot be empty.");

        globalTriggerListeners.add(triggerListener);
    }

    /**
     * <p>
     * Add the given <code>{@link org.quartz.TriggerListener}</code> to the
     * <code>Scheduler</code>'s list, of registered <code>TriggerListener</code>s.
     */
    public void addTriggerListener(TriggerListener triggerListener) {
        if (triggerListener.getName() == null
                || triggerListener.getName().length() == 0)
                throw new IllegalArgumentException(
                        "TriggerListener name cannot be empty.");

        triggerListeners.put(triggerListener.getName(), triggerListener);
    }

    /**
     * <p>
     * Remove the given <code>{@link org.quartz.TriggerListener}</code> from
     * the <code>Scheduler</code>'s list of <i>global</i> listeners.
     * </p>
     * 
     * @return true if the identifed listener was found in the list, and
     *         removed.
     */
    public boolean removeGlobalTriggerListener(TriggerListener triggerListener) {
        return globalTriggerListeners.remove(triggerListener);
    }

    /**
     * <p>
     * Remove the identifed <code>{@link org.quartz.TriggerListener}</code>
     * from the <code>Scheduler</code>'s list of registered listeners.
     * </p>
     * 
     * @return true if the identifed listener was found in the list, and
     *         removed.
     */
    public boolean removeTriggerListener(String name) {
        Object o = triggerListeners.remove(name);

        if (o != null) return true;

        return false;
    }

    /**
     * <p>
     * Get a list containing all of the <code>{@link org.quartz.TriggerListener}</code>
     * s in the <code>Scheduler</code>'s<i>global</i> list.
     * </p>
     */
    public List getGlobalTriggerListeners() {
        return new LinkedList(globalTriggerListeners);
    }

    /**
     * <p>
     * Get a Set containing the names of all the <i>non-global</i><code>{@link org.quartz.TriggerListener}</code>
     * s registered with the <code>Scheduler</code>.
     * </p>
     */
    public Set getTriggerListenerNames() {
        return Collections.unmodifiableSet(triggerListeners.keySet());
    }

    /**
     * <p>
     * Get the <i>non-global</i><code>{@link org.quartz.TriggerListener}</code>
     * that has the given name.
     * </p>
     */
    public TriggerListener getTriggerListener(String name) {
        return (TriggerListener) triggerListeners.get(name);
    }

    /**
     * <p>
     * Register the given <code>{@link SchedulerListener}</code> with the
     * <code>Scheduler</code>.
     * </p>
     */
    public void addSchedulerListener(SchedulerListener schedulerListener) {
        schedulerListeners.add(schedulerListener);
    }

    /**
     * <p>
     * Remove the given <code>{@link SchedulerListener}</code> from the
     * <code>Scheduler</code>.
     * </p>
     * 
     * @return true if the identifed listener was found in the list, and
     *         removed.
     */
    public boolean removeSchedulerListener(SchedulerListener schedulerListener) {
        return schedulerListeners.remove(schedulerListener);
    }

    /**
     * <p>
     * Get a List containing all of the <code>{@link SchedulerListener}</code>
     * s registered with the <code>Scheduler</code>.
     * </p>
     */
    public List getSchedulerListeners() {
        return (List) schedulerListeners.clone();
    }

    protected void notifyJobStoreJobComplete(SchedulingContext ctxt,
            Trigger trigger, JobDetail detail, int instCode)
            throws JobPersistenceException {

        resources.getJobStore().triggeredJobComplete(ctxt, trigger, detail,
                instCode);
    }

    protected void notifySchedulerThread() {
        if (isSignalOnSchedulingChange()) schedThread.signalSchedulingChange();
    }

    private List buildTriggerListenerList(String[] additionalLstnrs)
            throws SchedulerException {
        List triggerListeners = getGlobalTriggerListeners();
        for (int i = 0; i < additionalLstnrs.length; i++) {
            TriggerListener tl = getTriggerListener(additionalLstnrs[i]);

            if (tl != null) triggerListeners.add(tl);
            else
                throw new SchedulerException("TriggerListener '"
                        + additionalLstnrs[i] + "' not found.",
                        SchedulerException.ERR_TRIGGER_LISTENER_NOT_FOUND);
        }

        return triggerListeners;
    }

    private List buildJobListenerList(String[] additionalLstnrs)
            throws SchedulerException {
        List jobListeners = getGlobalJobListeners();
        for (int i = 0; i < additionalLstnrs.length; i++) {
            JobListener jl = getJobListener(additionalLstnrs[i]);

            if (jl != null) jobListeners.add(jl);
            else
                throw new SchedulerException("JobListener '"
                        + additionalLstnrs[i] + "' not found.",
                        SchedulerException.ERR_JOB_LISTENER_NOT_FOUND);
        }

        return jobListeners;
    }

    public boolean notifyTriggerListenersFired(JobExecutionContext jec)
            throws SchedulerException {
        // build a list of all trigger listeners that are to be notified...
        List triggerListeners = buildTriggerListenerList(jec.getTrigger()
                .getTriggerListenerNames());

        boolean vetoedExecution = false;
        
        // notify all trigger listeners in the list
        java.util.Iterator itr = triggerListeners.iterator();
        while (itr.hasNext()) {
            TriggerListener tl = (TriggerListener) itr.next();
            try {
                tl.triggerFired(jec.getTrigger(), jec);
                
                if(tl.vetoJobExecution(jec.getTrigger(), jec))
                    vetoedExecution = true;
            } catch (Exception e) {
                SchedulerException se = new SchedulerException(
                        "TriggerListener '" + tl.getName()
                                + "' threw exception: " + e.getMessage(), e);
                se.setErrorCode(SchedulerException.ERR_TRIGGER_LISTENER);
                throw se;
            }
        }
        
        return vetoedExecution;
    }
    

    public void notifyTriggerListenersMisfired(Trigger trigger)
            throws SchedulerException {
        // build a list of all trigger listeners that are to be notified...
        List triggerListeners = buildTriggerListenerList(trigger
                .getTriggerListenerNames());

        // notify all trigger listeners in the list
        java.util.Iterator itr = triggerListeners.iterator();
        while (itr.hasNext()) {
            TriggerListener tl = (TriggerListener) itr.next();
            try {
                tl.triggerMisfired(trigger);
            } catch (Exception e) {
                SchedulerException se = new SchedulerException(
                        "TriggerListener '" + tl.getName()
                                + "' threw exception: " + e.getMessage(), e);
                se.setErrorCode(SchedulerException.ERR_TRIGGER_LISTENER);
                throw se;
            }
        }
    }    

    public void notifyTriggerListenersComplete(JobExecutionContext jec,
            int instCode) throws SchedulerException {
        // build a list of all trigger listeners that are to be notified...
        List triggerListeners = buildTriggerListenerList(jec.getTrigger()
                .getTriggerListenerNames());

        // notify all trigger listeners in the list
        java.util.Iterator itr = triggerListeners.iterator();
        while (itr.hasNext()) {
            TriggerListener tl = (TriggerListener) itr.next();
            try {
                tl.triggerComplete(jec.getTrigger(), jec, instCode);
            } catch (Exception e) {
                SchedulerException se = new SchedulerException(
                        "TriggerListener '" + tl.getName()
                                + "' threw exception: " + e.getMessage(), e);
                se.setErrorCode(SchedulerException.ERR_TRIGGER_LISTENER);
                throw se;
            }
        }
    }

    public void notifyJobListenersToBeExecuted(JobExecutionContext jec)
            throws SchedulerException {
        // build a list of all job listeners that are to be notified...
        List jobListeners = buildJobListenerList(jec.getJobDetail()
                .getJobListenerNames());

        // notify all job listeners
        java.util.Iterator itr = jobListeners.iterator();
        while (itr.hasNext()) {
            JobListener jl = (JobListener) itr.next();
            try {
                jl.jobToBeExecuted(jec);
            } catch (Exception e) {
                SchedulerException se = new SchedulerException(
                        "JobListener '" + jl.getName() + "' threw exception: "
                                + e.getMessage(), e);
                se.setErrorCode(SchedulerException.ERR_JOB_LISTENER);
                throw se;
            }
        }
    }

    public void notifyJobListenersWasVetoed(JobExecutionContext jec)
    throws SchedulerException {
        // build a list of all job listeners that are to be notified...
        List jobListeners = buildJobListenerList(jec.getJobDetail()
                .getJobListenerNames());

        // notify all job listeners
        java.util.Iterator itr = jobListeners.iterator();
        while (itr.hasNext()) {
            JobListener jl = (JobListener) itr.next();
            try {
                jl.jobExecutionVetoed(jec);
            } catch (Exception e) {
                SchedulerException se = new SchedulerException(
                        "JobListener '" + jl.getName() + "' threw exception: "
                        + e.getMessage(), e);
                se.setErrorCode(SchedulerException.ERR_JOB_LISTENER);
                throw se;
            }
        }
    }

    public void notifyJobListenersWasExecuted(JobExecutionContext jec,
            JobExecutionException je) throws SchedulerException {
        // build a list of all job listeners that are to be notified...
        List jobListeners = buildJobListenerList(jec.getJobDetail()
                .getJobListenerNames());

        // notify all job listeners
        java.util.Iterator itr = jobListeners.iterator();
        while (itr.hasNext()) {
            JobListener jl = (JobListener) itr.next();
            try {
                jl.jobWasExecuted(jec, je);
            } catch (Exception e) {
                SchedulerException se = new SchedulerException(
                        "JobListener '" + jl.getName() + "' threw exception: "
                                + e.getMessage(), e);
                se.setErrorCode(SchedulerException.ERR_JOB_LISTENER);
                throw se;
            }
        }
    }

    public void notifySchedulerListenersError(String msg, SchedulerException se) {
        // build a list of all scheduler listeners that are to be notified...
        List schedListeners = getSchedulerListeners();

        // notify all scheduler listeners
        java.util.Iterator itr = schedListeners.iterator();
        while (itr.hasNext()) {
            SchedulerListener sl = (SchedulerListener) itr.next();
            try {
                sl.schedulerError(msg, se);
            } catch (Exception e) {
                getLog()
                        .error(
                                "Error while notifying SchedulerListener of error: ",
                                e);
                getLog().error(
                        "  Original error (for notification) was: " + msg, se);
            }
        }
    }

    public void notifySchedulerListenersSchduled(Trigger trigger) {
        // build a list of all scheduler listeners that are to be notified...
        List schedListeners = getSchedulerListeners();

        // notify all scheduler listeners
        java.util.Iterator itr = schedListeners.iterator();
        while (itr.hasNext()) {
            SchedulerListener sl = (SchedulerListener) itr.next();
            try {
                sl.jobScheduled(trigger);
            } catch (Exception e) {
                getLog().error(
                        "Error while notifying SchedulerListener of scheduled job."
                                + "  Triger=" + trigger.getFullName(), e);
            }
        }
    }

    public void notifySchedulerListenersUnschduled(String triggerName,
            String triggerGroup) {
        // build a list of all scheduler listeners that are to be notified...
        List schedListeners = getSchedulerListeners();

        // notify all scheduler listeners
        java.util.Iterator itr = schedListeners.iterator();
        while (itr.hasNext()) {
            SchedulerListener sl = (SchedulerListener) itr.next();
            try {
                sl.jobUnscheduled(triggerName, triggerGroup);
            } catch (Exception e) {
                getLog().error(
                        "Error while notifying SchedulerListener of unscheduled job."
                                + "  Triger=" + triggerGroup + "."
                                + triggerName, e);
            }
        }
    }

    public void notifySchedulerListenersFinalized(Trigger trigger) {
        // build a list of all scheduler listeners that are to be notified...
        List schedListeners = getSchedulerListeners();

        // notify all scheduler listeners
        java.util.Iterator itr = schedListeners.iterator();
        while (itr.hasNext()) {
            SchedulerListener sl = (SchedulerListener) itr.next();
            try {
                sl.triggerFinalized(trigger);
            } catch (Exception e) {
                getLog().error(
                        "Error while notifying SchedulerListener of finalized trigger."
                                + "  Triger=" + trigger.getFullName(), e);
            }
        }
    }

    public void notifySchedulerListenersPausedTrigger(String name, String group) {
        // build a list of all job listeners that are to be notified...
        List schedListeners = getSchedulerListeners();

        // notify all scheduler listeners
        java.util.Iterator itr = schedListeners.iterator();
        while (itr.hasNext()) {
            SchedulerListener sl = (SchedulerListener) itr.next();
            try {
                sl.triggersPaused(name, group);
            } catch (Exception e) {
                getLog().error(
                        "Error while notifying SchedulerListener of paused trigger/group."
                                + "  Triger=" + group + "." + name, e);
            }
        }
    }

    public void notifySchedulerListenersResumedTrigger(String name, String group) {
        // build a list of all job listeners that are to be notified...
        List schedListeners = getSchedulerListeners();

        // notify all scheduler listeners
        java.util.Iterator itr = schedListeners.iterator();
        while (itr.hasNext()) {
            SchedulerListener sl = (SchedulerListener) itr.next();
            try {
                sl.triggersResumed(name, group);
            } catch (Exception e) {
                getLog().error(
                        "Error while notifying SchedulerListener of resumed trigger/group."
                                + "  Triger=" + group + "." + name, e);
            }
        }
    }

    public void notifySchedulerListenersPausedJob(String name, String group) {
        // build a list of all job listeners that are to be notified...
        List schedListeners = getSchedulerListeners();

        // notify all scheduler listeners
        java.util.Iterator itr = schedListeners.iterator();
        while (itr.hasNext()) {
            SchedulerListener sl = (SchedulerListener) itr.next();
            try {
                sl.jobsPaused(name, group);
            } catch (Exception e) {
                getLog().error(
                        "Error while notifying SchedulerListener of paused job/group."
                                + "  Job=" + group + "." + name, e);
            }
        }
    }

    public void notifySchedulerListenersResumedJob(String name, String group) {
        // build a list of all job listeners that are to be notified...
        List schedListeners = getSchedulerListeners();

        // notify all scheduler listeners
        java.util.Iterator itr = schedListeners.iterator();
        while (itr.hasNext()) {
            SchedulerListener sl = (SchedulerListener) itr.next();
            try {
                sl.jobsResumed(name, group);
            } catch (Exception e) {
                getLog().error(
                        "Error while notifying SchedulerListener of resumed job/group."
                                + "  Job=" + group + "." + name, e);
            }
        }
    }

    public void notifySchedulerListenersShutdown() {
        // build a list of all job listeners that are to be notified...
        List schedListeners = getSchedulerListeners();

        // notify all scheduler listeners
        java.util.Iterator itr = schedListeners.iterator();
        while (itr.hasNext()) {
            SchedulerListener sl = (SchedulerListener) itr.next();
            try {
                sl.schedulerShutdown();
            } catch (Exception e) {
                getLog().error(
                        "Error while notifying SchedulerListener of shutdown.",
                        e);
            }
        }
    }
    /**
     * <p>
     * Add the given <code>{@link org.quartz.spi.SchedulerPlugin}</code> to
     * the <code>Scheduler</code>. This method expects the plugin's
     * "initialize" method to be invoked externally (either before or after
     * this method is called).
     */
    public void addSchedulerPlugin(SchedulerPlugin plugin) {
        schedulerPlugins.add(plugin);
    }


    public void setJobFactory(JobFactory factory) throws SchedulerException {

        if(factory == null)
            throw new IllegalArgumentException("JobFactory cannot be set to null!");

        getLog().info("JobFactory set to: " + factory);

        this.jobFactory = factory;
    }
    
    public JobFactory getJobFactory()  {
        return jobFactory;
    }
    
    
    /**
     * Interrupt all instances of the identified InterruptableJob.
     *  
     * @see org.quartz.core.RemotableQuartzScheduler#interrupt(java.lang.String, java.lang.String)
     */
    public boolean interrupt(SchedulingContext ctxt, String jobName, String groupName) throws UnableToInterruptJobException {

        if(groupName == null)
            groupName = Scheduler.DEFAULT_GROUP;
        
        List jobs = getCurrentlyExecutingJobs();
        java.util.Iterator it = jobs.iterator();
        
        JobExecutionContext jec = null;
        JobDetail jobDetail = null;
        Job job = null;
        
        boolean interrupted = false;
        
        while (it.hasNext()) {
            jec = (JobExecutionContext)it.next();
            jobDetail = jec.getJobDetail();
            if (jobName.equals(jobDetail.getName())
                && groupName.equals(jobDetail.getGroup())){
                job = jec.getJobInstance();
                if (job instanceof InterruptableJob) {
                    ((InterruptableJob)job).interrupt();
                    interrupted = true;
                } else {
                    throw new UnableToInterruptJobException(
                        "Job '"
                        + jobName
                        + "' of group '"
                        + groupName
                        + "' can not be interrupted, since it does not implement "
                        + InterruptableJob.class.getName());
                    
                }
            }                        
        }
        
        return interrupted;
    }
    
    private void shutdownPlugins() {
        java.util.Iterator itr = schedulerPlugins.iterator();
        while (itr.hasNext()) {
            SchedulerPlugin plugin = (SchedulerPlugin) itr.next();
            plugin.shutdown();
        }
    }

    private void startPlugins() {
        java.util.Iterator itr = schedulerPlugins.iterator();
        while (itr.hasNext()) {
            SchedulerPlugin plugin = (SchedulerPlugin) itr.next();
            plugin.start();
        }
    }

}

/////////////////////////////////////////////////////////////////////////////
//
// ErrorLogger - Scheduler Listener Class
//
/////////////////////////////////////////////////////////////////////////////

class ErrorLogger implements SchedulerListener {

    public static Log getLog() {
        return LogFactory.getLog(ErrorLogger.class);
    }

    ErrorLogger() {
    }

    public void jobScheduled(Trigger trigger) {
        // do nothing...
    }

    public void jobUnscheduled(String triggerName, String triggerGroup) {
        // do nothing...
    }

    public void triggerFinalized(Trigger trigger) {
        // do nothing...
    }

    /**
     * <p>
     * Called by the <code>{@link Scheduler}</code> when a <code>{@link Trigger}</code>
     * or group of <code>{@link Trigger}s</code> has been paused.
     * </p>
     * 
     * <p>
     * If a group was paused, then the <code>triggerName</code> parameter
     * will be null.
     * </p>
     */
    public void triggersPaused(String triggerName, String triggerGroup) {
        // do nothing...
    }

    /**
     * <p>
     * Called by the <code>{@link Scheduler}</code> when a <code>{@link Trigger}</code>
     * or group of <code>{@link Trigger}s</code> has been un-paused.
     * </p>
     * 
     * <p>
     * If a group was resumed, then the <code>triggerName</code> parameter
     * will be null.
     * </p>
     */
    public void triggersResumed(String triggerName, String triggerGroup) {
        // do nothing...
    }

    /**
     * <p>
     * Called by the <code>{@link Scheduler}</code> when a <code>{@link org.quartz.JobDetail}</code>
     * or group of <code>{@link org.quartz.JobDetail}s</code> has been
     * paused.
     * </p>
     * 
     * <p>
     * If a group was paused, then the <code>jobName</code> parameter will be
     * null.
     * </p>
     */
    public void jobsPaused(String jobName, String jobGroup) {
        // do nothing...
    }

    /**
     * <p>
     * Called by the <code>{@link Scheduler}</code> when a <code>{@link org.quartz.JobDetail}</code>
     * or group of <code>{@link org.quartz.JobDetail}s</code> has been
     * un-paused.
     * </p>
     * 
     * <p>
     * If a group was paused, then the <code>jobName</code> parameter will be
     * null.
     * </p>
     */
    public void jobsResumed(String jobName, String jobGroup) {
        // do nothing...
    }

    public void schedulerError(String msg, SchedulerException cause) {
        getLog().error(msg, cause);
    }

    public void schedulerShutdown() {
        // do nothing...
    }
}

/////////////////////////////////////////////////////////////////////////////
//
// ExecutingJobsManager - Job Listener Class
//
/////////////////////////////////////////////////////////////////////////////

class ExecutingJobsManager implements JobListener {
    HashMap executingJobs = new HashMap();

    int numJobsFired = 0;

    ExecutingJobsManager() {
    }

    public String getName() {
        return getClass().getName();
    }

    public int getNumJobsCurrentlyExecuting() {
        synchronized (executingJobs) {
            return executingJobs.size();
        }
    }

    public void jobToBeExecuted(JobExecutionContext context) {
        numJobsFired++;

        synchronized (executingJobs) {
            executingJobs
                    .put(context.getTrigger().getFireInstanceId(), context);
        }
    }

    public void jobWasExecuted(JobExecutionContext context,
            JobExecutionException jobException) {
        synchronized (executingJobs) {
            executingJobs.remove(context.getTrigger().getFireInstanceId());
        }
    }

    public int getNumJobsFired() {
        return numJobsFired;
    }

    public List getExecutingJobs() {
        synchronized (executingJobs) {
            return java.util.Collections.unmodifiableList(new ArrayList(
                    executingJobs.values()));
        }
    }

    public void jobExecutionVetoed(JobExecutionContext context) {
        
    }
}
