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
package org.quartz.impl;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.quartz.Calendar;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobListener;
import org.quartz.Scheduler;
import org.quartz.SchedulerContext;
import org.quartz.SchedulerException;
import org.quartz.SchedulerListener;
import org.quartz.SchedulerMetaData;
import org.quartz.Trigger;
import org.quartz.TriggerListener;
import org.quartz.UnableToInterruptJobException;
import org.quartz.core.RemotableQuartzScheduler;
import org.quartz.core.SchedulingContext;
import org.quartz.spi.JobFactory;

/**
 * <p>
 * An implementation of the <code>Scheduler</code> interface that remotely
 * proxies all method calls to the equivalent call on a given <code>QuartzScheduler</code>
 * instance, via RMI.
 * </p>
 * 
 * @see org.quartz.Scheduler
 * @see org.quartz.core.QuartzScheduler
 * @see org.quartz.core.SchedulingContext
 * 
 * @author James House
 */
public class RemoteScheduler implements Scheduler {

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * 
     * Data members.
     * 
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    private RemotableQuartzScheduler rsched;

    private SchedulingContext schedCtxt;

    private String schedId;

    private String rmiHost;

    private int rmiPort;

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * 
     * Constructors.
     * 
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    /**
     * <p>
     * Construct a <code>RemoteScheduler</code> instance to proxy the given
     * <code>RemoteableQuartzScheduler</code> instance, and with the given
     * <code>SchedulingContext</code>.
     * </p>
     */
    public RemoteScheduler(SchedulingContext schedCtxt, String schedId,
            String host, int port) {

        this.schedCtxt = schedCtxt;
        this.schedId = schedId;
        this.rmiHost = host;
        this.rmiPort = port;
    }

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * 
     * Interface.
     * 
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    protected RemotableQuartzScheduler getRemoteScheduler()
            throws SchedulerException {
        if (rsched != null) return rsched;

        try {
            Registry registry = LocateRegistry.getRegistry(rmiHost, rmiPort);

            rsched = (RemotableQuartzScheduler) registry.lookup(schedId);

        } catch (Exception e) {
            SchedulerException initException = new SchedulerException(
                    "Could not get handle to remote scheduler: "
                            + e.getMessage(), e);
            initException
                    .setErrorCode(SchedulerException.ERR_COMMUNICATION_FAILURE);
            throw initException;
        }

        return rsched;
    }

    protected SchedulerException invalidateHandleCreateException(String msg,
            Exception cause) {
        rsched = null;
        SchedulerException ex = new SchedulerException(msg, cause);
        ex.setErrorCode(SchedulerException.ERR_COMMUNICATION_FAILURE);
        return ex;
    }

    /**
     * <p>
     * Returns the name of the <code>Scheduler</code>.
     * </p>
     */
    public String getSchedulerName() throws SchedulerException {
        try {
            return getRemoteScheduler().getSchedulerName();
        } catch (RemoteException re) {
            throw invalidateHandleCreateException(
                    "Error communicating with remote scheduler.", re);
        }
    }

    /**
     * <p>
     * Returns the instance Id of the <code>Scheduler</code>.
     * </p>
     */
    public String getSchedulerInstanceId() throws SchedulerException {
        try {
            return getRemoteScheduler().getSchedulerInstanceId();
        } catch (RemoteException re) {
            throw invalidateHandleCreateException(
                    "Error communicating with remote scheduler.", re);
        }
    }

    public SchedulerMetaData getMetaData() throws SchedulerException {
        try {
            RemotableQuartzScheduler sched = getRemoteScheduler();
            return new SchedulerMetaData(getSchedulerName(),
                    getSchedulerInstanceId(), getClass(), true, sched
                            .runningSince() != null, isPaused(), isShutdown(),
                    sched.runningSince(), sched.numJobsExecuted(), sched
                            .getJobStoreClass(), sched.supportsPersistence(),
                    sched.getThreadPoolClass(), sched.getThreadPoolSize(),
                    sched.getVersion());

        } catch (RemoteException re) {
            throw invalidateHandleCreateException(
                    "Error communicating with remote scheduler.", re);
        }

    }

    /**
     * <p>
     * Returns the <code>SchedulerContext</code> of the <code>Scheduler</code>.
     * </p>
     */
    public SchedulerContext getContext() throws SchedulerException {
        try {
            return getRemoteScheduler().getSchedulerContext();
        } catch (RemoteException re) {
            throw invalidateHandleCreateException(
                    "Error communicating with remote scheduler.", re);
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    ///
    /// Schedululer State Management Methods
    ///
    ///////////////////////////////////////////////////////////////////////////

    /**
     * <p>
     * Calls the equivalent method on the 'proxied' <code>QuartzScheduler</code>.
     * </p>
     */
    public void start() throws SchedulerException {
        try {
            getRemoteScheduler().start();
        } catch (RemoteException re) {
            throw invalidateHandleCreateException(
                    "Error communicating with remote scheduler.", re);
        }
    }

    /**
     * <p>
     * Calls the equivalent method on the 'proxied' <code>QuartzScheduler</code>.
     * </p>
     */
    public void standby() throws SchedulerException {
        try {
            getRemoteScheduler().standby();
        } catch (RemoteException re) {
            throw invalidateHandleCreateException(
                    "Error communicating with remote scheduler.", re);
        }
    }

    /**
     * @see org.quartz.Scheduler#pause()
     * @deprecated
     */
    public void pause() throws SchedulerException {
        this.standby();
    }    
    
    /**
     * <p>
     * Calls the equivalent method on the 'proxied' <code>QuartzScheduler</code>.
     * </p>
     */
    public boolean isInStandbyMode() throws SchedulerException {
        try {
            return getRemoteScheduler().isInStandbyMode();
        } catch (RemoteException re) {
            throw invalidateHandleCreateException(
                    "Error communicating with remote scheduler.", re);
        }
    }

    public boolean isPaused() throws SchedulerException {
        return this.isInStandbyMode();
    }
    /**
     * <p>
     * Calls the equivalent method on the 'proxied' <code>QuartzScheduler</code>.
     * </p>
     */
    public void shutdown() throws SchedulerException {
        try {
            getRemoteScheduler().shutdown();
        } catch (RemoteException re) {
            throw invalidateHandleCreateException(
                    "Error communicating with remote scheduler.", re);
        }
    }

    /**
     * <p>
     * Calls the equivalent method on the 'proxied' <code>QuartzScheduler</code>.
     * </p>
     */
    public void shutdown(boolean waitForJobsToComplete)
            throws SchedulerException {
        try {
            getRemoteScheduler().shutdown(waitForJobsToComplete);
        } catch (RemoteException re) {
            throw invalidateHandleCreateException(
                    "Error communicating with remote scheduler.", re);
        }
    }

    /**
     * <p>
     * Calls the equivalent method on the 'proxied' <code>QuartzScheduler</code>.
     * </p>
     */
    public boolean isShutdown() throws SchedulerException {
        try {
            return getRemoteScheduler().isShutdown();
        } catch (RemoteException re) {
            throw invalidateHandleCreateException(
                    "Error communicating with remote scheduler.", re);
        }
    }

    /**
     * <p>
     * Calls the equivalent method on the 'proxied' <code>QuartzScheduler</code>.
     * </p>
     */
    public List getCurrentlyExecutingJobs() throws SchedulerException {
        try {
            return getRemoteScheduler().getCurrentlyExecutingJobs();
        } catch (RemoteException re) {
            throw invalidateHandleCreateException(
                    "Error communicating with remote scheduler.", re);
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    ///
    /// Scheduling-related Methods
    ///
    ///////////////////////////////////////////////////////////////////////////

    /**
     * <p>
     * Calls the equivalent method on the 'proxied' <code>QuartzScheduler</code>,
     * passing the <code>SchedulingContext</code> associated with this
     * instance.
     * </p>
     */
    public Date scheduleJob(JobDetail jobDetail, Trigger trigger)
            throws SchedulerException {
        try {
            return getRemoteScheduler().scheduleJob(schedCtxt, jobDetail,
                    trigger);
        } catch (RemoteException re) {
            throw invalidateHandleCreateException(
                    "Error communicating with remote scheduler.", re);
        }
    }

    /**
     * <p>
     * Calls the equivalent method on the 'proxied' <code>QuartzScheduler</code>,
     * passing the <code>SchedulingContext</code> associated with this
     * instance.
     * </p>
     */
    public Date scheduleJob(Trigger trigger) throws SchedulerException {
        try {
            return getRemoteScheduler().scheduleJob(schedCtxt, trigger);
        } catch (RemoteException re) {
            throw invalidateHandleCreateException(
                    "Error communicating with remote scheduler.", re);
        }
    }

    /**
     * <p>
     * Calls the equivalent method on the 'proxied' <code>QuartzScheduler</code>,
     * passing the <code>SchedulingContext</code> associated with this
     * instance.
     * </p>
     */
    public void addJob(JobDetail jobDetail, boolean replace)
            throws SchedulerException {
        try {
            getRemoteScheduler().addJob(schedCtxt, jobDetail, replace);
        } catch (RemoteException re) {
            throw invalidateHandleCreateException(
                    "Error communicating with remote scheduler.", re);
        }
    }

    /**
     * <p>
     * Calls the equivalent method on the 'proxied' <code>QuartzScheduler</code>,
     * passing the <code>SchedulingContext</code> associated with this
     * instance.
     * </p>
     */
    public boolean deleteJob(String jobName, String groupName)
            throws SchedulerException {
        try {
            return getRemoteScheduler()
                    .deleteJob(schedCtxt, jobName, groupName);
        } catch (RemoteException re) {
            throw invalidateHandleCreateException(
                    "Error communicating with remote scheduler.", re);
        }
    }

    /**
     * <p>
     * Calls the equivalent method on the 'proxied' <code>QuartzScheduler</code>,
     * passing the <code>SchedulingContext</code> associated with this
     * instance.
     * </p>
     */
    public boolean unscheduleJob(String triggerName, String groupName)
            throws SchedulerException {
        try {
            return getRemoteScheduler().unscheduleJob(schedCtxt, triggerName,
                    groupName);
        } catch (RemoteException re) {
            throw invalidateHandleCreateException(
                    "Error communicating with remote scheduler.", re);
        }
    }

    /**
     * <p>
     * Calls the equivalent method on the 'proxied' <code>QuartzScheduler</code>,
     * passing the <code>SchedulingContext</code> associated with this
     * instance.
     * </p>
     */
    public Date rescheduleJob(String triggerName,
            String groupName, Trigger newTrigger) throws SchedulerException {
        try {
            return getRemoteScheduler().rescheduleJob(schedCtxt, triggerName,
                    groupName, newTrigger);
        } catch (RemoteException re) {
            throw invalidateHandleCreateException(
                    "Error communicating with remote scheduler.", re);
        }
    }
    
    
    /**
     * <p>
     * Calls the equivalent method on the 'proxied' <code>QuartzScheduler</code>,
     * passing the <code>SchedulingContext</code> associated with this
     * instance.
     * </p>
     */
    public void triggerJob(String jobName, String groupName)
            throws SchedulerException {
        triggerJob(jobName, groupName, null);
    }
    
    /**
     * <p>
     * Calls the equivalent method on the 'proxied' <code>QuartzScheduler</code>,
     * passing the <code>SchedulingContext</code> associated with this
     * instance.
     * </p>
     */
    public void triggerJob(String jobName, String groupName, JobDataMap data)
            throws SchedulerException {
        try {
            getRemoteScheduler().triggerJob(schedCtxt, jobName, groupName, data);
        } catch (RemoteException re) {
            throw invalidateHandleCreateException(
                    "Error communicating with remote scheduler.", re);
        }
    }

    /**
     * <p>
     * Calls the equivalent method on the 'proxied' <code>QuartzScheduler</code>,
     * passing the <code>SchedulingContext</code> associated with this
     * instance.
     * </p>
     */
    public void triggerJobWithVolatileTrigger(String jobName, String groupName)
        throws SchedulerException {
        triggerJobWithVolatileTrigger(jobName, groupName, null);
    }
    
    /**
     * <p>
     * Calls the equivalent method on the 'proxied' <code>QuartzScheduler</code>,
     * passing the <code>SchedulingContext</code> associated with this
     * instance.
     * </p>
     */
    public void triggerJobWithVolatileTrigger(String jobName, String groupName, JobDataMap data)
            throws SchedulerException {
        try {
            getRemoteScheduler().triggerJobWithVolatileTrigger(schedCtxt,
                    jobName, groupName, data);
        } catch (RemoteException re) {
            throw invalidateHandleCreateException(
                    "Error communicating with remote scheduler.", re);
        }
    }

    /**
     * <p>
     * Calls the equivalent method on the 'proxied' <code>QuartzScheduler</code>,
     * passing the <code>SchedulingContext</code> associated with this
     * instance.
     * </p>
     */
    public void pauseTrigger(String triggerName, String groupName)
            throws SchedulerException {
        try {
            getRemoteScheduler()
                    .pauseTrigger(schedCtxt, triggerName, groupName);
        } catch (RemoteException re) {
            throw invalidateHandleCreateException(
                    "Error communicating with remote scheduler.", re);
        }
    }

    /**
     * <p>
     * Calls the equivalent method on the 'proxied' <code>QuartzScheduler</code>,
     * passing the <code>SchedulingContext</code> associated with this
     * instance.
     * </p>
     */
    public void pauseTriggerGroup(String groupName) throws SchedulerException {
        try {
            getRemoteScheduler().pauseTriggerGroup(schedCtxt, groupName);
        } catch (RemoteException re) {
            throw invalidateHandleCreateException(
                    "Error communicating with remote scheduler.", re);
        }
    }

    /**
     * <p>
     * Calls the equivalent method on the 'proxied' <code>QuartzScheduler</code>,
     * passing the <code>SchedulingContext</code> associated with this
     * instance.
     * </p>
     */
    public void pauseJob(String jobName, String groupName)
            throws SchedulerException {
        try {
            getRemoteScheduler().pauseJob(schedCtxt, jobName, groupName);
        } catch (RemoteException re) {
            throw invalidateHandleCreateException(
                    "Error communicating with remote scheduler.", re);
        }
    }

    /**
     * <p>
     * Calls the equivalent method on the 'proxied' <code>QuartzScheduler</code>,
     * passing the <code>SchedulingContext</code> associated with this
     * instance.
     * </p>
     */
    public void pauseJobGroup(String groupName) throws SchedulerException {
        try {
            getRemoteScheduler().pauseJobGroup(schedCtxt, groupName);
        } catch (RemoteException re) {
            throw invalidateHandleCreateException(
                    "Error communicating with remote scheduler.", re);
        }
    }

    /**
     * <p>
     * Calls the equivalent method on the 'proxied' <code>QuartzScheduler</code>,
     * passing the <code>SchedulingContext</code> associated with this
     * instance.
     * </p>
     */
    public void resumeTrigger(String triggerName, String groupName)
            throws SchedulerException {
        try {
            getRemoteScheduler().resumeTrigger(schedCtxt, triggerName,
                    groupName);
        } catch (RemoteException re) {
            throw invalidateHandleCreateException(
                    "Error communicating with remote scheduler.", re);
        }
    }

    /**
     * <p>
     * Calls the equivalent method on the 'proxied' <code>QuartzScheduler</code>,
     * passing the <code>SchedulingContext</code> associated with this
     * instance.
     * </p>
     */
    public void resumeTriggerGroup(String groupName) throws SchedulerException {
        try {
            getRemoteScheduler().resumeTriggerGroup(schedCtxt, groupName);
        } catch (RemoteException re) {
            throw invalidateHandleCreateException(
                    "Error communicating with remote scheduler.", re);
        }
    }

    /**
     * <p>
     * Calls the equivalent method on the 'proxied' <code>QuartzScheduler</code>,
     * passing the <code>SchedulingContext</code> associated with this
     * instance.
     * </p>
     */
    public void resumeJob(String jobName, String groupName)
            throws SchedulerException {
        try {
            getRemoteScheduler().resumeJob(schedCtxt, jobName, groupName);
        } catch (RemoteException re) {
            throw invalidateHandleCreateException(
                    "Error communicating with remote scheduler.", re);
        }
    }

    /**
     * <p>
     * Calls the equivalent method on the 'proxied' <code>QuartzScheduler</code>,
     * passing the <code>SchedulingContext</code> associated with this
     * instance.
     * </p>
     */
    public void resumeJobGroup(String groupName) throws SchedulerException {
        try {
            getRemoteScheduler().resumeJobGroup(schedCtxt, groupName);
        } catch (RemoteException re) {
            throw invalidateHandleCreateException(
                    "Error communicating with remote scheduler.", re);
        }
    }

    /**
     * <p>
     * Calls the equivalent method on the 'proxied' <code>QuartzScheduler</code>,
     * passing the <code>SchedulingContext</code> associated with this
     * instance.
     * </p>
     */
    public void pauseAll() throws SchedulerException {
        try {
            getRemoteScheduler().pauseAll(schedCtxt);
        } catch (RemoteException re) {
            throw invalidateHandleCreateException(
                    "Error communicating with remote scheduler.", re);
        }
    }

    /**
     * <p>
     * Calls the equivalent method on the 'proxied' <code>QuartzScheduler</code>,
     * passing the <code>SchedulingContext</code> associated with this
     * instance.
     * </p>
     */
    public void resumeAll() throws SchedulerException {
        try {
            getRemoteScheduler().resumeAll(schedCtxt);
        } catch (RemoteException re) {
            throw invalidateHandleCreateException(
                    "Error communicating with remote scheduler.", re);
        }
    }

    /**
     * <p>
     * Calls the equivalent method on the 'proxied' <code>QuartzScheduler</code>,
     * passing the <code>SchedulingContext</code> associated with this
     * instance.
     * </p>
     */
    public String[] getJobGroupNames() throws SchedulerException {
        try {
            return getRemoteScheduler().getJobGroupNames(schedCtxt);
        } catch (RemoteException re) {
            throw invalidateHandleCreateException(
                    "Error communicating with remote scheduler.", re);
        }
    }

    /**
     * <p>
     * Calls the equivalent method on the 'proxied' <code>QuartzScheduler</code>,
     * passing the <code>SchedulingContext</code> associated with this
     * instance.
     * </p>
     */
    public String[] getJobNames(String groupName) throws SchedulerException {
        try {
            return getRemoteScheduler().getJobNames(schedCtxt, groupName);
        } catch (RemoteException re) {
            throw invalidateHandleCreateException(
                    "Error communicating with remote scheduler.", re);
        }
    }

    /**
     * <p>
     * Calls the equivalent method on the 'proxied' <code>QuartzScheduler</code>,
     * passing the <code>SchedulingContext</code> associated with this
     * instance.
     * </p>
     */
    public Trigger[] getTriggersOfJob(String jobName, String groupName)
            throws SchedulerException {
        try {
            return getRemoteScheduler().getTriggersOfJob(schedCtxt, jobName,
                    groupName);
        } catch (RemoteException re) {
            throw invalidateHandleCreateException(
                    "Error communicating with remote scheduler.", re);
        }
    }

    /**
     * <p>
     * Calls the equivalent method on the 'proxied' <code>QuartzScheduler</code>,
     * passing the <code>SchedulingContext</code> associated with this
     * instance.
     * </p>
     */
    public String[] getTriggerGroupNames() throws SchedulerException {
        try {
            return getRemoteScheduler().getTriggerGroupNames(schedCtxt);
        } catch (RemoteException re) {
            throw invalidateHandleCreateException(
                    "Error communicating with remote scheduler.", re);
        }
    }

    /**
     * <p>
     * Calls the equivalent method on the 'proxied' <code>QuartzScheduler</code>,
     * passing the <code>SchedulingContext</code> associated with this
     * instance.
     * </p>
     */
    public String[] getTriggerNames(String groupName) throws SchedulerException {
        try {
            return getRemoteScheduler().getTriggerNames(schedCtxt, groupName);
        } catch (RemoteException re) {
            throw invalidateHandleCreateException(
                    "Error communicating with remote scheduler.", re);
        }
    }

    /**
     * <p>
     * Calls the equivalent method on the 'proxied' <code>QuartzScheduler</code>,
     * passing the <code>SchedulingContext</code> associated with this
     * instance.
     * </p>
     */
    public JobDetail getJobDetail(String jobName, String jobGroup)
            throws SchedulerException {
        try {
            return getRemoteScheduler().getJobDetail(schedCtxt, jobName,
                    jobGroup);
        } catch (RemoteException re) {
            throw invalidateHandleCreateException(
                    "Error communicating with remote scheduler.", re);
        }
    }

    /**
     * <p>
     * Calls the equivalent method on the 'proxied' <code>QuartzScheduler</code>,
     * passing the <code>SchedulingContext</code> associated with this
     * instance.
     * </p>
     */
    public Trigger getTrigger(String triggerName, String triggerGroup)
            throws SchedulerException {
        try {
            return getRemoteScheduler().getTrigger(schedCtxt, triggerName,
                    triggerGroup);
        } catch (RemoteException re) {
            throw invalidateHandleCreateException(
                    "Error communicating with remote scheduler.", re);
        }
    }

    /**
     * <p>
     * Calls the equivalent method on the 'proxied' <code>QuartzScheduler</code>,
     * passing the <code>SchedulingContext</code> associated with this
     * instance.
     * </p>
     */
    public int getTriggerState(String triggerName, String triggerGroup)
            throws SchedulerException {
        try {
            return getRemoteScheduler().getTriggerState(schedCtxt, triggerName,
                    triggerGroup);
        } catch (RemoteException re) {
            throw invalidateHandleCreateException(
                    "Error communicating with remote scheduler.", re);
        }
    }

    /**
     * <p>
     * Calls the equivalent method on the 'proxied' <code>QuartzScheduler</code>,
     * passing the <code>SchedulingContext</code> associated with this
     * instance.
     * </p>
     */
    public void addCalendar(String calName, Calendar calendar, boolean replace, boolean updateTriggers)
            throws SchedulerException {
        try {
            getRemoteScheduler().addCalendar(schedCtxt, calName, calendar,
                    replace, updateTriggers);
        } catch (RemoteException re) {
            throw invalidateHandleCreateException(
                    "Error communicating with remote scheduler.", re);
        }
    }

    /**
     * <p>
     * Calls the equivalent method on the 'proxied' <code>QuartzScheduler</code>,
     * passing the <code>SchedulingContext</code> associated with this
     * instance.
     * </p>
     */
    public boolean deleteCalendar(String calName) throws SchedulerException {
        try {
            return getRemoteScheduler().deleteCalendar(schedCtxt, calName);
        } catch (RemoteException re) {
            throw invalidateHandleCreateException(
                    "Error communicating with remote scheduler.", re);
        }
    }

    /**
     * <p>
     * Calls the equivalent method on the 'proxied' <code>QuartzScheduler</code>,
     * passing the <code>SchedulingContext</code> associated with this
     * instance.
     * </p>
     */
    public Calendar getCalendar(String calName) throws SchedulerException {
        try {
            return getRemoteScheduler().getCalendar(schedCtxt, calName);
        } catch (RemoteException re) {
            throw invalidateHandleCreateException(
                    "Error communicating with remote scheduler.", re);
        }
    }

    /**
     * <p>
     * Calls the equivalent method on the 'proxied' <code>QuartzScheduler</code>,
     * passing the <code>SchedulingContext</code> associated with this
     * instance.
     * </p>
     */
    public String[] getCalendarNames() throws SchedulerException {
        try {
            return getRemoteScheduler().getCalendarNames(schedCtxt);
        } catch (RemoteException re) {
            throw invalidateHandleCreateException(
                    "Error communicating with remote scheduler.", re);
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    ///
    /// Listener-related Methods
    ///
    ///////////////////////////////////////////////////////////////////////////

    /**
     * <p>
     * Calls the equivalent method on the 'proxied' <code>QuartzScheduler</code>.
     * </p>
     */
    public void addGlobalJobListener(JobListener jobListener)
            throws SchedulerException {
        throw new SchedulerException(
                "Operation not supported for remote schedulers.",
                SchedulerException.ERR_UNSUPPORTED_FUNCTION_IN_THIS_CONFIGURATION);
    }

    /**
     * <p>
     * Calls the equivalent method on the 'proxied' <code>QuartzScheduler</code>.
     * </p>
     */
    public void addJobListener(JobListener jobListener)
            throws SchedulerException {
        throw new SchedulerException(
                "Operation not supported for remote schedulers.",
                SchedulerException.ERR_UNSUPPORTED_FUNCTION_IN_THIS_CONFIGURATION);
    }

    /**
     * <p>
     * Calls the equivalent method on the 'proxied' <code>QuartzScheduler</code>.
     * </p>
     */
    public boolean removeGlobalJobListener(JobListener jobListener)
            throws SchedulerException {
        throw new SchedulerException(
                "Operation not supported for remote schedulers.",
                SchedulerException.ERR_UNSUPPORTED_FUNCTION_IN_THIS_CONFIGURATION);
    }

    /**
     * <p>
     * Calls the equivalent method on the 'proxied' <code>QuartzScheduler</code>.
     * </p>
     */
    public boolean removeJobListener(String name) throws SchedulerException {
        throw new SchedulerException(
                "Operation not supported for remote schedulers.",
                SchedulerException.ERR_UNSUPPORTED_FUNCTION_IN_THIS_CONFIGURATION);
    }

    /**
     * <p>
     * Calls the equivalent method on the 'proxied' <code>QuartzScheduler</code>.
     * </p>
     */
    public List getGlobalJobListeners() throws SchedulerException {
        throw new SchedulerException(
                "Operation not supported for remote schedulers.",
                SchedulerException.ERR_UNSUPPORTED_FUNCTION_IN_THIS_CONFIGURATION);
    }

    /**
     * <p>
     * Calls the equivalent method on the 'proxied' <code>QuartzScheduler</code>.
     * </p>
     */
    public Set getJobListenerNames() throws SchedulerException {
        throw new SchedulerException(
                "Operation not supported for remote schedulers.",
                SchedulerException.ERR_UNSUPPORTED_FUNCTION_IN_THIS_CONFIGURATION);
    }

    /**
     * <p>
     * Calls the equivalent method on the 'proxied' <code>QuartzScheduler</code>.
     * </p>
     */
    public JobListener getJobListener(String name) throws SchedulerException {
        throw new SchedulerException(
                "Operation not supported for remote schedulers.",
                SchedulerException.ERR_UNSUPPORTED_FUNCTION_IN_THIS_CONFIGURATION);
    }

    /**
     * <p>
     * Calls the equivalent method on the 'proxied' <code>QuartzScheduler</code>.
     * </p>
     */
    public void addGlobalTriggerListener(TriggerListener triggerListener)
            throws SchedulerException {
        throw new SchedulerException(
                "Operation not supported for remote schedulers.",
                SchedulerException.ERR_UNSUPPORTED_FUNCTION_IN_THIS_CONFIGURATION);
    }

    /**
     * <p>
     * Calls the equivalent method on the 'proxied' <code>QuartzScheduler</code>.
     * </p>
     */
    public void addTriggerListener(TriggerListener triggerListener)
            throws SchedulerException {
        throw new SchedulerException(
                "Operation not supported for remote schedulers.",
                SchedulerException.ERR_UNSUPPORTED_FUNCTION_IN_THIS_CONFIGURATION);
    }

    /**
     * <p>
     * Calls the equivalent method on the 'proxied' <code>QuartzScheduler</code>.
     * </p>
     */
    public boolean removeGlobalTriggerListener(TriggerListener triggerListener)
            throws SchedulerException {
        throw new SchedulerException(
                "Operation not supported for remote schedulers.",
                SchedulerException.ERR_UNSUPPORTED_FUNCTION_IN_THIS_CONFIGURATION);
    }

    /**
     * <p>
     * Calls the equivalent method on the 'proxied' <code>QuartzScheduler</code>.
     * </p>
     */
    public boolean removeTriggerListener(String name) throws SchedulerException {
        throw new SchedulerException(
                "Operation not supported for remote schedulers.",
                SchedulerException.ERR_UNSUPPORTED_FUNCTION_IN_THIS_CONFIGURATION);
    }

    /**
     * <p>
     * Calls the equivalent method on the 'proxied' <code>QuartzScheduler</code>.
     * </p>
     */
    public List getGlobalTriggerListeners() throws SchedulerException {
        throw new SchedulerException(
                "Operation not supported for remote schedulers.",
                SchedulerException.ERR_UNSUPPORTED_FUNCTION_IN_THIS_CONFIGURATION);
    }

    /**
     * <p>
     * Calls the equivalent method on the 'proxied' <code>QuartzScheduler</code>.
     * </p>
     */
    public Set getTriggerListenerNames() throws SchedulerException {
        throw new SchedulerException(
                "Operation not supported for remote schedulers.",
                SchedulerException.ERR_UNSUPPORTED_FUNCTION_IN_THIS_CONFIGURATION);
    }

    /**
     * <p>
     * Calls the equivalent method on the 'proxied' <code>QuartzScheduler</code>.
     * </p>
     */
    public TriggerListener getTriggerListener(String name)
            throws SchedulerException {
        throw new SchedulerException(
                "Operation not supported for remote schedulers.",
                SchedulerException.ERR_UNSUPPORTED_FUNCTION_IN_THIS_CONFIGURATION);
    }

    /**
     * <p>
     * Calls the equivalent method on the 'proxied' <code>QuartzScheduler</code>.
     * </p>
     */
    public void addSchedulerListener(SchedulerListener schedulerListener)
            throws SchedulerException {
        throw new SchedulerException(
                "Operation not supported for remote schedulers.",
                SchedulerException.ERR_UNSUPPORTED_FUNCTION_IN_THIS_CONFIGURATION);
    }

    /**
     * <p>
     * Calls the equivalent method on the 'proxied' <code>QuartzScheduler</code>.
     * </p>
     */
    public boolean removeSchedulerListener(SchedulerListener schedulerListener)
            throws SchedulerException {
        throw new SchedulerException(
                "Operation not supported for remote schedulers.",
                SchedulerException.ERR_UNSUPPORTED_FUNCTION_IN_THIS_CONFIGURATION);
    }

    /**
     * <p>
     * Calls the equivalent method on the 'proxied' <code>QuartzScheduler</code>.
     * </p>
     */
    public List getSchedulerListeners() throws SchedulerException {
        throw new SchedulerException(
                "Operation not supported for remote schedulers.",
                SchedulerException.ERR_UNSUPPORTED_FUNCTION_IN_THIS_CONFIGURATION);
    }

    /** 
     * @see org.quartz.Scheduler#getPausedTriggerGroups()
     */
    public Set getPausedTriggerGroups() throws SchedulerException {
        try {
            return getRemoteScheduler().getPausedTriggerGroups(schedCtxt);
        } catch (RemoteException re) {
            throw invalidateHandleCreateException(
                    "Error communicating with remote scheduler.", re);
        }
    }

    /**
     * @see org.quartz.Scheduler#interrupt(java.lang.String, java.lang.String)
     */
    public boolean interrupt(String jobName, String groupName) throws UnableToInterruptJobException  {
        try {
            return getRemoteScheduler().interrupt(schedCtxt, jobName, groupName);
        } catch (RemoteException re) {
            throw new UnableToInterruptJobException(invalidateHandleCreateException(
                    "Error communicating with remote scheduler.", re));
        } catch (SchedulerException se) {
            throw new UnableToInterruptJobException(se);
        }
    }

    /**
     * @see org.quartz.Scheduler#setJobFactory(org.quartz.spi.JobFactory)
     */
    public void setJobFactory(JobFactory factory) throws SchedulerException {
        throw new SchedulerException(
                "Operation not supported for remote schedulers.",
                SchedulerException.ERR_UNSUPPORTED_FUNCTION_IN_THIS_CONFIGURATION);
    }
}
