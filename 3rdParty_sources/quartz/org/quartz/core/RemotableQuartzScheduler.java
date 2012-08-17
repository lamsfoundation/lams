
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

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.quartz.Calendar;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobListener;
import org.quartz.SchedulerContext;
import org.quartz.SchedulerException;
import org.quartz.SchedulerListener;
import org.quartz.Trigger;
import org.quartz.TriggerListener;
import org.quartz.UnableToInterruptJobException;

/**
 * @author James House
 */
public interface RemotableQuartzScheduler extends Remote {

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * 
     * Interface.
     * 
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    public String getSchedulerName() throws RemoteException;

    public String getSchedulerInstanceId() throws RemoteException;

    public SchedulerContext getSchedulerContext() throws SchedulerException,
            RemoteException;

    public void start() throws SchedulerException, RemoteException;

    public void standby() throws RemoteException;

    public boolean isInStandbyMode() throws RemoteException;

    public void shutdown() throws RemoteException;

    public void shutdown(boolean waitForJobsToComplete) throws RemoteException;

    public boolean isShutdown() throws RemoteException;

    public Date runningSince() throws RemoteException;

    public String getVersion() throws RemoteException;

    public int numJobsExecuted() throws RemoteException;

    public Class getJobStoreClass() throws RemoteException;

    public boolean supportsPersistence() throws RemoteException;

    public Class getThreadPoolClass() throws RemoteException;

    public int getThreadPoolSize() throws RemoteException;

    public List getCurrentlyExecutingJobs() throws SchedulerException,
            RemoteException;

    public Date scheduleJob(SchedulingContext ctxt, JobDetail jobDetail,
            Trigger trigger) throws SchedulerException, RemoteException;

    public Date scheduleJob(SchedulingContext ctxt, Trigger trigger)
            throws SchedulerException, RemoteException;

    public void addJob(SchedulingContext ctxt, JobDetail jobDetail,
            boolean replace) throws SchedulerException, RemoteException;

    public boolean deleteJob(SchedulingContext ctxt, String jobName,
            String groupName) throws SchedulerException, RemoteException;

    public boolean unscheduleJob(SchedulingContext ctxt, String triggerName,
            String groupName) throws SchedulerException, RemoteException;

    public Date rescheduleJob(SchedulingContext ctxt, String triggerName,
            String groupName, Trigger newTrigger) throws SchedulerException, RemoteException;
        
    
    public void triggerJob(SchedulingContext ctxt, String jobName,
            String groupName, JobDataMap data) throws SchedulerException, RemoteException;

    public void triggerJobWithVolatileTrigger(SchedulingContext ctxt,
            String jobName, String groupName, JobDataMap data) throws SchedulerException,
            RemoteException;

    public void pauseTrigger(SchedulingContext ctxt, String triggerName,
            String groupName) throws SchedulerException, RemoteException;

    public void pauseTriggerGroup(SchedulingContext ctxt, String groupName)
            throws SchedulerException, RemoteException;

    public void pauseJob(SchedulingContext ctxt, String jobName,
            String groupName) throws SchedulerException, RemoteException;

    public void pauseJobGroup(SchedulingContext ctxt, String groupName)
            throws SchedulerException, RemoteException;

    public void resumeTrigger(SchedulingContext ctxt, String triggerName,
            String groupName) throws SchedulerException, RemoteException;

    public void resumeTriggerGroup(SchedulingContext ctxt, String groupName)
            throws SchedulerException, RemoteException;

    public Set getPausedTriggerGroups(SchedulingContext ctxt)
        throws SchedulerException, RemoteException;
    
    public void resumeJob(SchedulingContext ctxt, String jobName,
            String groupName) throws SchedulerException, RemoteException;

    public void resumeJobGroup(SchedulingContext ctxt, String groupName)
            throws SchedulerException, RemoteException;

    public void pauseAll(SchedulingContext ctxt) throws SchedulerException,
            RemoteException;

    public void resumeAll(SchedulingContext ctxt) throws SchedulerException,
            RemoteException;

    public String[] getJobGroupNames(SchedulingContext ctxt)
            throws SchedulerException, RemoteException;

    public String[] getJobNames(SchedulingContext ctxt, String groupName)
            throws SchedulerException, RemoteException;

    public Trigger[] getTriggersOfJob(SchedulingContext ctxt, String jobName,
            String groupName) throws SchedulerException, RemoteException;

    public String[] getTriggerGroupNames(SchedulingContext ctxt)
            throws SchedulerException, RemoteException;

    public String[] getTriggerNames(SchedulingContext ctxt, String groupName)
            throws SchedulerException, RemoteException;

    public JobDetail getJobDetail(SchedulingContext ctxt, String jobName,
            String jobGroup) throws SchedulerException, RemoteException;

    public Trigger getTrigger(SchedulingContext ctxt, String triggerName,
            String triggerGroup) throws SchedulerException, RemoteException;

    public int getTriggerState(SchedulingContext ctxt, String triggerName,
            String triggerGroup) throws SchedulerException, RemoteException;

    public void addCalendar(SchedulingContext ctxt, String calName,
            Calendar calendar, boolean replace, boolean updateTriggers) throws SchedulerException,
            RemoteException;

    public boolean deleteCalendar(SchedulingContext ctxt, String calName)
            throws SchedulerException, RemoteException;

    public Calendar getCalendar(SchedulingContext ctxt, String calName)
            throws SchedulerException, RemoteException;

    public String[] getCalendarNames(SchedulingContext ctxt)
            throws SchedulerException, RemoteException;

    public void addGlobalJobListener(JobListener jobListener)
            throws RemoteException;

    public void addJobListener(JobListener jobListener) throws RemoteException;

    public boolean removeGlobalJobListener(JobListener jobListener)
            throws RemoteException;

    public boolean removeJobListener(String name) throws RemoteException;

    public List getGlobalJobListeners() throws RemoteException;

    public Set getJobListenerNames() throws RemoteException;

    public JobListener getJobListener(String name) throws RemoteException;

    public void addGlobalTriggerListener(TriggerListener triggerListener)
            throws RemoteException;

    public void addTriggerListener(TriggerListener triggerListener)
            throws RemoteException;

    public boolean removeGlobalTriggerListener(TriggerListener triggerListener)
            throws RemoteException;

    public boolean removeTriggerListener(String name) throws RemoteException;

    public List getGlobalTriggerListeners() throws RemoteException;

    public Set getTriggerListenerNames() throws RemoteException;

    public TriggerListener getTriggerListener(String name)
            throws RemoteException;

    public void addSchedulerListener(SchedulerListener schedulerListener)
            throws RemoteException;

    public boolean removeSchedulerListener(SchedulerListener schedulerListener)
            throws RemoteException;

    public List getSchedulerListeners() throws RemoteException;

    public boolean interrupt(SchedulingContext ctxt, String jobName, String groupName) throws UnableToInterruptJobException,RemoteException ;
   

}
