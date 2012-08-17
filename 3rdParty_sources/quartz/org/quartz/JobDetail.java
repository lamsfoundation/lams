
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

import java.util.ArrayList;

/**
 * <p>
 * Conveys the detail properties of a given <code>Job</code> instance.
 * </p>
 * 
 * <p>
 * Quartz does not store an actual instance of a <code>Job</code> class, but
 * instead allows you to define an instance of one, through the use of a <code>JobDetail</code>.
 * </p>
 * 
 * <p>
 * <code>Job</code> s have a name and group associated with them, which
 * should uniquely identify them within a single <code>{@link Scheduler}</code>.
 * </p>
 * 
 * <p>
 * <code>Trigger</code> s are the 'mechanism' by which <code>Job</code> s
 * are scheduled. Many <code>Trigger</code> s can point to the same <code>Job</code>,
 * but a single <code>Trigger</code> can only point to one <code>Job</code>.
 * </p>
 * 
 * @see Job
 * @see StatefulJob
 * @see JobDataMap
 * @see Trigger
 * 
 * @author James House
 * @author Sharada Jambula
 */
public class JobDetail implements Cloneable, java.io.Serializable {

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * 
     * Data members.
     * 
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    private String name;

    private String group = Scheduler.DEFAULT_GROUP;

    private String description;

    private Class jobClass;

    private JobDataMap jobDataMap;

    private boolean volatility = false;

    private boolean durability = false;

    private boolean shouldRecover = false;

    private ArrayList jobListeners = new ArrayList(2);

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * 
     * Constructors.
     * 
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    /**
     * <p>
     * Create a <code>JobDetail</code> with no specified name or group, and
     * the default settings of all the other properties.
     * </p>
     * 
     * <p>
     * Note that the {@link #setName(String)},{@link #setGroup(String)}and
     * {@link #setJobClass(Class)}methods must be called before the job can be
     * placed into a {@link Scheduler}
     * </p>
     */
    public JobDetail() {
        // do nothing...
    }

    /**
     * <p>
     * Create a <code>JobDetail</code> with the given name, and group, and
     * the default settings of all the other properties.
     * </p>
     * 
     * @param group if <code>null</code>, Scheduler.DEFAULT_GROUP will be used.
     * 
     * @exception IllegalArgumentException
     *              if nameis null or empty, or the group is an empty string.
     */
    public JobDetail(String name, String group, Class jobClass) {
        setName(name);
        setGroup(group);
        setJobClass(jobClass);
    }

    /**
     * <p>
     * Create a <code>JobDetail</code> with the given name, and group, and
     * the given settings of all the other properties.
     * </p>
     * 
     * @param group if <code>null</code>, Scheduler.DEFAULT_GROUP will be used.
     * 
     * @exception IllegalArgumentException
     *              if nameis null or empty, or the group is an empty string.
     */
    public JobDetail(String name, String group, Class jobClass,
            boolean volatility, boolean durability, boolean recover) {
        setName(name);
        setGroup(group);
        setJobClass(jobClass);
        setVolatility(volatility);
        setDurability(durability);
        setRequestsRecovery(recover);
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
     * Get the name of this <code>Job</code>.
     * </p>
     */
    public String getName() {
        return name;
    }

    /**
     * <p>
     * Set the name of this <code>Job</code>.
     * </p>
     * 
     * @exception IllegalArgumentException
     *              if name is null or empty.
     */
    public void setName(String name) {
        if (name == null || name.trim().length() == 0)
                throw new IllegalArgumentException("Job name cannot be empty.");

        this.name = name;
    }

    /**
     * <p>
     * Get the group of this <code>Job</code>.
     * </p>
     */
    public String getGroup() {
        return group;
    }

    /**
     * <p>
     * Set the group of this <code>Job</code>.
     * </p>
     * 
     * @param group if <code>null</code>, Scheduler.DEFAULT_GROUP will be used.
     * 
     * @exception IllegalArgumentException
     *              if the group is an empty string.
     */
    public void setGroup(String group) {
        if (group != null && group.trim().length() == 0)
                throw new IllegalArgumentException(
                        "Group name cannot be empty.");

        if(group == null)
            group = Scheduler.DEFAULT_GROUP;
        
        this.group = group;
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
     * Return the description given to the <code>Job</code> instance by its
     * creator (if any).
     * </p>
     * 
     * @return null if no description was set.
     */
    public String getDescription() {
        return description;
    }

    /**
     * <p>
     * Set a description for the <code>Job</code> instance - may be useful
     * for remembering/displaying the purpose of the job, though the
     * description has no meaning to Quartz.
     * </p>
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * <p>
     * Get the instance of <code>Job</code> that will be executed.
     * </p>
     */
    public Class getJobClass() {
        return jobClass;
    }

    /**
     * <p>
     * Set the instance of <code>Job</code> that will be executed.
     * </p>
     * 
     * @exception IllegalArgumentException
     *              if jobClass is null or the class is not a <code>Job</code>.
     */
    public void setJobClass(Class jobClass) {
        if (jobClass == null)
                throw new IllegalArgumentException("Job class cannot be null.");

        if (!Job.class.isAssignableFrom(jobClass))
                throw new IllegalArgumentException(
                        "Job class must implement the Job interface.");

        this.jobClass = jobClass;
    }

    /**
     * <p>
     * Get the <code>JobDataMap</code> that is associated with the <code>Job</code>.
     * </p>
     */
    public JobDataMap getJobDataMap() {
        if (jobDataMap == null) jobDataMap = new JobDataMap();
        return jobDataMap;
    }

    /**
     * <p>
     * Set the <code>JobDataMap</code> to be associated with the <code>Job</code>.
     * </p>
     */
    public void setJobDataMap(JobDataMap jobDataMap) {
        this.jobDataMap = jobDataMap;
    }

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
                throw new SchedulerException("Job's name cannot be null",
                        SchedulerException.ERR_CLIENT_ERROR);

        if (group == null)
                throw new SchedulerException("Job's group cannot be null",
                        SchedulerException.ERR_CLIENT_ERROR);

        if (jobClass == null)
                throw new SchedulerException("Job's class cannot be null",
                        SchedulerException.ERR_CLIENT_ERROR);
    }

    /**
     * <p>
     * Set whether or not the <code>Job</code> should be persisted in the
     * <code>{@link org.quartz.spi.JobStore}</code> for re-use after program
     * restarts.
     * </p>
     * 
     * <p>
     * If not explicitly set, the default value is <code>false</code>.
     * </p>
     */
    public void setVolatility(boolean volatility) {
        this.volatility = volatility;
    }

    /**
     * <p>
     * Set whether or not the <code>Job</code> should remain stored after it
     * is orphaned (no <code>{@link Trigger}s</code> point to it).
     * </p>
     * 
     * <p>
     * If not explicitly set, the default value is <code>false</code>.
     * </p>
     */
    public void setDurability(boolean durability) {
        this.durability = durability;
    }

    /**
     * <p>
     * Set whether or not the the <code>Scheduler</code> should re-execute
     * the <code>Job</code> if a 'recovery' or 'fail-over' situation is
     * encountered.
     * </p>
     * 
     * <p>
     * If not explicitly set, the default value is <code>false</code>.
     * </p>
     * 
     * @see JobExecutionContext#isRecovering()
     * @see JobExecutionContext#isFailedOver()
     */
    public void setRequestsRecovery(boolean shouldRecover) {
        this.shouldRecover = shouldRecover;
    }

    /**
     * <p>
     * Whether or not the <code>Job</code> should not be persisted in the
     * <code>{@link org.quartz.spi.JobStore}</code> for re-use after program
     * restarts.
     * </p>
     * 
     * <p>
     * If not explicitly set, the default value is <code>false</code>.
     * </p>
     * 
     * @return <code>true</code> if the <code>Job</code> should be garbage
     *         collected along with the <code>{@link Scheduler}</code>.
     */
    public boolean isVolatile() {
        return volatility;
    }

    /**
     * <p>
     * Whether or not the <code>Job</code> should remain stored after it is
     * orphaned (no <code>{@link Trigger}s</code> point to it).
     * </p>
     * 
     * <p>
     * If not explicitly set, the default value is <code>false</code>.
     * </p>
     * 
     * @return <code>true</code> if the Job should remain persisted after
     *         being orphaned.
     */
    public boolean isDurable() {
        return durability;
    }

    /**
     * <p>
     * Whether or not the <code>Job</code> implements the interface <code>{@link StatefulJob}</code>.
     * </p>
     */
    public boolean isStateful() {
        if (jobClass == null)
           return false;

        return (StatefulJob.class.isAssignableFrom(jobClass));
    }

    /**
     * <p>
     * Instructs the <code>Scheduler</code> whether or not the <code>Job</code>
     * should be re-executed if a 'recovery' or 'fail-over' situation is
     * encountered.
     * </p>
     * 
     * <p>
     * If not explicitly set, the default value is <code>false</code>.
     * </p>
     * 
     * @see JobExecutionContext#isRecovering()
     * @see JobExecutionContext#isFailedOver()
     */
    public boolean requestsRecovery() {
        return shouldRecover;
    }

    /**
     * <p>
     * Add the specified name of a <code>{@link JobListener}</code> to the
     * end of the <code>Job</code>'s list of listeners.
     * </p>
     */
    public void addJobListener(String name) {
        jobListeners.add(name);
    }

    /**
     * <p>
     * Remove the specified name of a <code>{@link JobListener}</code> from
     * the <code>Job</code>'s list of listeners.
     * </p>
     * 
     * @return true if the given name was found in the list, and removed
     */
    public boolean removeJobListener(String name) {
        return jobListeners.remove(name);
    }

    /**
     * <p>
     * Returns an array of <code>String</code> s containing the names of all
     * <code>{@link JobListener}</code> s assigned to the <code>Job</code>,
     * in the order in which they should be notified.
     * </p>
     */
    public String[] getJobListenerNames() {
        return (String[]) jobListeners.toArray(new String[jobListeners.size()]);
    }

    /**
     * <p>
     * Return a simple string representation of this object.
     * </p>
     */
    public String toString() {
        return "JobDetail '" + getFullName() + "':  jobClass: '"
                + ((getJobClass() == null) ? null : getJobClass().getName())
                + " isStateful: " + isStateful() + " isVolatile: "
                + isVolatile() + " isDurable: " + isDurable()
                + " requestsRecovers: " + requestsRecovery();
    }

    public Object clone() {
        JobDetail copy;
        try {
            copy = (JobDetail) super.clone();
            copy.jobListeners = (ArrayList) jobListeners.clone();
            if (jobDataMap != null)
                    copy.jobDataMap = (JobDataMap) jobDataMap.clone();
        } catch (CloneNotSupportedException ex) {
            throw new IncompatibleClassChangeError("Not Cloneable.");
        }

        return copy;
    }

}
