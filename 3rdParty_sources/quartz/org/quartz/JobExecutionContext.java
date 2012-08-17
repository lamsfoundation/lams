
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
import java.util.HashMap;

import org.quartz.spi.TriggerFiredBundle;

/**
 * <p>
 * A context bundle containing handles to various environment information, that
 * is given to a <code>{@link org.quartz.JobDetail}</code> instance as it is
 * executed, and to a <code>{@link Trigger}</code> instance after the
 * execution completes.
 * </p>
 * 
 * <p>
 * The <code>JobDataMap</code> found on this object (via the 
 * <code>getMergedJobDataMap()</code> method) serves as a convenience -
 * it is a merge of the <code>JobDataMap</code> found on the 
 * <code>JobDetail</code> and the one found on the <code>Trigger</code>, with 
 * the value in the latter overriding any same-named values in the former.
 * <i>It is thus considered a 'best practice' that the execute code of a Job
 * retrieve data from the JobDataMap found on this object</i>  NOTE: Do not
 * expect value 'set' into this JobDataMap to somehow be set back onto a
 * <code>StatefulJob</code>'s own JobDataMap.
 * </p>
 * 
 * <p>
 * <code>JobExecutionContext</code> s are also returned from the 
 * <code>Scheduler.getCurrentlyExecutingJobs()</code>
 * method. These are the same instances as those past into the jobs that are
 * currently executing within the scheduler. The exception to this is when your
 * application is using Quartz remotely (i.e. via RMI) - in which case you get
 * a clone of the <code>JobExecutionContext</code>s, and their references to
 * the <code>Scheduler</code> and <code>Job</code> instances have been lost (a
 * clone of the <code>JobDetail</code> is still available - just not a handle
 * to the job instance that is running).
 * </p>
 * 
 * @see #getJobDetail()
 * @see #getScheduler()
 * @see #getMergedJobDataMap()
 * 
 * @see Job
 * @see Trigger
 * @see JobDataMap
 * 
 * @author James House
 */
public class JobExecutionContext implements java.io.Serializable {

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * 
     * Data members.
     * 
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    private transient Scheduler scheduler;

    private Trigger trigger;

    private JobDetail jobDetail;
    
    private JobDataMap jobDataMap;

    private transient Job job;
    
    private Calendar calendar;

    private boolean recovering = false;

    private int numRefires = 0;

    private Date fireTime;

    private Date scheduledFireTime;

    private Date prevFireTime;

    private Date nextFireTime;
    
    private long jobRunTime = -1;
    
    private Object result;
    
    private HashMap data = new HashMap();

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * 
     * Constructors.
     * 
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    /**
     * <p>
     * Create a JobExcecutionContext with the given context data.
     * </p>
     */
    public JobExecutionContext(Scheduler scheduler,
            TriggerFiredBundle firedBundle, Job job) {
        this.scheduler = scheduler;
        this.trigger = firedBundle.getTrigger();
        this.calendar = firedBundle.getCalendar();
        this.jobDetail = firedBundle.getJobDetail();
        this.job = job;
        this.recovering = firedBundle.isRecovering();
        this.fireTime = firedBundle.getFireTime();
        this.scheduledFireTime = firedBundle.getScheduledFireTime();
        this.prevFireTime = firedBundle.getPrevFireTime();
        this.nextFireTime = firedBundle.getNextFireTime();
        
        this.jobDataMap = new JobDataMap();
        this.jobDataMap.putAll(jobDetail.getJobDataMap());
        this.jobDataMap.putAll(trigger.getJobDataMap());
        
        this.jobDataMap.setMutable(false);
        this.trigger.getJobDataMap().setMutable(false);
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
     * Get a handle to the <code>Scheduler</code> instance that fired the
     * <code>Job</code>.
     * </p>
     */
    public Scheduler getScheduler() {
        return scheduler;
    }

    /**
     * <p>
     * Get a handle to the <code>Trigger</code> instance that fired the
     * <code>Job</code>.
     * </p>
     */
    public Trigger getTrigger() {
        return trigger;
    }

    /**
     * <p>
     * Get a handle to the <code>Calendar</code> referenced by the <code>Trigger</code>
     * instance that fired the <code>Job</code>.
     * </p>
     */
    public Calendar getCalendar() {
        return calendar;
    }

    /**
     * <p>
     * If the <code>Job</code> is being re-executed because of a 'recovery'
     * situation, this method will return <code>true</code>.
     * </p>
     */
    public boolean isRecovering() {
        return recovering;
    }

    public void incrementRefireCount() {
        numRefires++;
    }

    public int getRefireCount() {
        return numRefires;
    }

    /**
     * <p>
     * Get the convenience <code>JobDataMap</code> of this execution context.
     * </p>
     * 
     * <p>
     * The <code>JobDataMap</code> found on this object serves as a convenience -
     * it is a merge of the <code>JobDataMap</code> found on the 
     * <code>JobDetail</code> and the one found on the <code>Trigger</code>, with 
     * the value in the latter overriding any same-named values in the former.
     * <i>It is thus considered a 'best practice' that the execute code of a Job
     * retrieve data from the JobDataMap found on this object</i>
     * </p>
     * 
     * <p>NOTE: Do not
     * expect value 'set' into this JobDataMap to somehow be set back onto a
     * <code>StatefulJob</code>'s own JobDataMap.
     * </p>
     * 
     * <p>
     * Attempts to change the contents of this map typically result in an 
     * <code>IllegalStateException</code>.
     * </p>
     * 
     */
    public JobDataMap getMergedJobDataMap() {
        return jobDataMap;
    }

    /**
     * <p>
     * Get the <code>JobDetail</code> associated with the <code>Job</code>.
     * </p>
     */
    public JobDetail getJobDetail() {
        return jobDetail;
    }

    /**
     * <p>
     * Get the instance of the <code>Job</code> that was created for this
     * execution.
     * </p>
     * 
     * <p>
     * Note: The Job instance is not available through remote scheduler
     * interfaces.
     * </p>
     */
    public Job getJobInstance() {
        return job;
    }

    /**
     * The actual time the trigger fired. For instance the scheduled time may
     * have been 10:00:00 but the actual fire time may have been 10:00:03 if
     * the scheduler was too busy.
     * 
     * @return Returns the fireTime.
     * @see #getScheduledFireTime()
     */
    public Date getFireTime() {
        return fireTime;
    }

    /**
     * The scheduled time the trigger fired for. For instance the scheduled
     * time may have been 10:00:00 but the actual fire time may have been
     * 10:00:03 if the scheduler was too busy.
     * 
     * @return Returns the scheduledFireTime.
     * @see #getFireTime()
     */
    public Date getScheduledFireTime() {
        return scheduledFireTime;
    }

    public Date getPreviousFireTime() {
        return prevFireTime;
    }

    public Date getNextFireTime() {
        return nextFireTime;
    }

    public String toString() {
        return "JobExecutionContext:" + " trigger: '"
                + getTrigger().getFullName() + " job: "
                + getJobDetail().getFullName() + " fireTime: '" + getFireTime()
                + " scheduledFireTime: " + getScheduledFireTime()
                + " previousFireTime: '" + getPreviousFireTime()
                + " nextFireTime: " + getNextFireTime() + " isRecovering: "
                + isRecovering() + " refireCount: " + getRefireCount();
    }

    /**
     * Returns the result (if any) that the <code>Job</code> set before its 
     * execution completed (the type of object set as the result is entirely up 
     * to the particular job).
     * 
     * <p>
     * The result itself is meaningless to Quartz, but may be informative
     * to <code>{@link JobListener}s</code> or 
     * <code>{@link TriggerListener}s</code> that are watching the job's 
     * execution.
     * </p> 
     * 
     * @return Returns the result.
     */
    public Object getResult() {
        return result;
    }
    
    /**
     * Set the result (if any) of the <code>Job</code>'s execution (the type of 
     * object set as the result is entirely up to the particular job).
     * 
     * <p>
     * The result itself is meaningless to Quartz, but may be informative
     * to <code>{@link JobListener}s</code> or 
     * <code>{@link TriggerListener}s</code> that are watching the job's 
     * execution.
     * </p> 
     * 
     * @return Returns the result.
     */
    public void setResult(Object result) {
        this.result = result;
    }
    
    /**
     * The amount of time the job ran for (in milliseconds).  The returned 
     * value will be -1 until the job has actually completed (or thrown an 
     * exception), and is therefore generally only useful to 
     * <code>JobListener</code>s and <code>TriggerListener</code>s.
     * 
     * @return Returns the jobRunTime.
     */
    public long getJobRunTime() {
        return jobRunTime;
    }
    
    /**
     * @param jobRunTime The jobRunTime to set.
     */
    public void setJobRunTime(long jobRunTime) {
        this.jobRunTime = jobRunTime;
    }

    /**
     * Put the specified value into the context's data map with the given key.
     * Possibly useful for sharing data between listeners and jobs.
     *
     * <p>NOTE: this data is volatile - it is lost after the job execution
     * completes, and all TriggerListeners and JobListeners have been 
     * notified.</p> 
     *  
     * @param key
     * @param value
     */
    public void put(Object key, Object value) {
        data.put(key, value);
    }
    
    /**
     * Get the value with the given key from the context's data map.
     * 
     * @param key
     */
    public Object get(Object key) {
        return data.get(key);
    }
}
