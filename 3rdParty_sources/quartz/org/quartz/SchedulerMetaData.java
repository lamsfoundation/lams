
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

/**
 * <p>
 * Describes the settings and capabilities of a given <code>{@link Scheduler}</code>
 * instance.
 * </p>
 * 
 * @author James House
 */
public class SchedulerMetaData implements java.io.Serializable {

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * 
     * Data members.
     * 
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    private String schedName;

    private String schedInst;

    private Class schedClass;

    private boolean isRemote;

    private boolean started;

    private boolean paused;

    private boolean shutdown;

    private Date startTime;

    private int numJobsExec;

    private Class jsClass;

    private boolean jsPersistent;

    private Class tpClass;

    private int tpSize;

    private String version;

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * 
     * Constructors.
     * 
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    public SchedulerMetaData(String schedName, String schedInst,
            Class schedClass, boolean isRemote, boolean started,
            boolean paused, boolean shutdown, Date startTime, int numJobsExec,
            Class jsClass, boolean jsPersistent, Class tpClass, int tpSize,
            String version) {
        this.schedName = schedName;
        this.schedInst = schedInst;
        this.schedClass = schedClass;
        this.isRemote = isRemote;
        this.started = started;
        this.paused = paused;
        this.shutdown = shutdown;
        this.startTime = startTime;
        this.numJobsExec = numJobsExec;
        this.jsClass = jsClass;
        this.jsPersistent = jsPersistent;
        this.tpClass = tpClass;
        this.tpSize = tpSize;
        this.version = version;
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
     * Returns the name of the <code>Scheduler</code>.
     * </p>
     */
    public String getSchedulerName() {
        return schedName;
    }

    /**
     * <p>
     * Returns the instance Id of the <code>Scheduler</code>.
     * </p>
     */
    public String getSchedulerInstanceId() {
        return schedInst;
    }

    /**
     * <p>
     * Returns the class-name of the <code>Scheduler</code> instance.
     * </p>
     */
    public Class getSchedulerClass() {
        return schedClass;
    }

    /**
     * <p>
     * Returns the <code>Date</code> at which the Scheduler started running.
     * </p>
     * 
     * @return null if the scheduler has not been started.
     */
    public Date runningSince() {
        return startTime;
    }

    /**
     * <p>
     * Returns the number of jobs executed since the <code>Scheduler</code>
     * started..
     * </p>
     */
    public int numJobsExecuted() {
        return numJobsExec;
    }

    /**
     * <p>
     * Returns whether the <code>Scheduler</code> is being used remotely (via
     * RMI).
     * </p>
     */
    public boolean isSchedulerRemote() {
        return isRemote;
    }

    /**
     * <p>
     * Returns whether the scheduler has been started.
     * </p>
     * 
     * <p>
     * Note: <code>isStarted()</code> may return <code>true</code> even if
     * <code>isPaused()</code> returns <code>true</code>.
     * </p>
     */
    public boolean isStarted() {
        return started;
    }

    /**
     * <p>
     * Reports whether the <code>Scheduler</code> is paused.
     * </p>
     * 
     * <p>
     * Note: <code>isStarted()</code> may return <code>true</code> even if
     * <code>isPaused()</code> returns <code>true</code>.
     * </p>
     */
    public boolean isPaused() {
        return paused;
    }

    /**
     * <p>
     * Reports whether the <code>Scheduler</code> has been shutdown.
     * </p>
     */
    public boolean isShutdown() {
        return shutdown;
    }

    /**
     * <p>
     * Returns the class-name of the <code>JobStore</code> instance that is
     * being used by the <code>Scheduler</code>.
     * </p>
     */
    public Class getJobStoreClass() {
        return jsClass;
    }

    /**
     * <p>
     * Returns whether or not the <code>Scheduler</code>'s<code>JobStore</code>
     * instance supports persistence.
     * </p>
     */
    public boolean jobStoreSupportsPersistence() {
        return jsPersistent;
    }

    /**
     * <p>
     * Returns the class-name of the <code>ThreadPool</code> instance that is
     * being used by the <code>Scheduler</code>.
     * </p>
     */
    public Class getThreadPoolClass() {
        return tpClass;
    }

    /**
     * <p>
     * Returns the number of threads currently in the <code>Scheduler</code>'s
     * <code>ThreadPool</code>.
     * </p>
     */
    public int getThreadPoolSize() {
        return tpSize;
    }

    /**
     * <p>
     * Returns the version of Quartz that is running.
     * </p>
     */
    public String getVersion() {
        return version;
    }

    /**
     * <p>
     * Return a simple string representation of this object.
     * </p>
     */
    public String toString() {
        try {
            return getSummary();
        } catch (SchedulerException se) {
            return "SchedulerMetaData: undeterminable.";
        }
    }

    /**
     * <p>
     * Returns a formatted (human readable) String describing all the <code>Scheduler</code>'s
     * meta-data values.
     * </p>
     * 
     * <p>
     * The format of the String looks something like this:
     * 
     * <pre>
     * 
     * 
     *  Quartz Scheduler 'SchedulerName' with instanceId 'SchedulerInstanceId' Scheduler class: 'org.quartz.impl.StdScheduler' - running locally. Running since: '11:33am on Jul 19, 2002' Not currently paused. Number of Triggers fired: '123' Using thread pool 'org.quartz.simpl.SimpleThreadPool' - with '8' threads Using job-store 'org.quartz.impl.JDBCJobStore' - which supports persistence.
     * </pre>
     * 
     * </p>
     */
    public String getSummary() throws SchedulerException {
        StringBuffer str = new StringBuffer("Quartz Scheduler (v");
        str.append(getVersion());
        str.append(") '");

        str.append(getSchedulerName());
        str.append("' with instanceId '");
        str.append(getSchedulerInstanceId());
        str.append("'\n");

        str.append("  Scheduler class: '");
        str.append(getSchedulerClass().getName());
        str.append("'");
        if (isSchedulerRemote()) str.append(" - access via RMI.");
        else
            str.append(" - running locally.");
        str.append("\n");

        if (!isShutdown()) {
            if (runningSince() != null) {
                str.append("  Running since: ");
                str.append(runningSince());
            } else
                str.append("NOT STARTED.");
            str.append("\n");

            if (isPaused()) str.append("  Currently PAUSED.");
            else
                str.append("  Not currently paused.");
        } else {
            str.append("  Scheduler has been SHUTDOWN.");
        }
        str.append("\n");

        str.append("  Number of jobs executed: ");
        str.append(numJobsExecuted());
        str.append("\n");

        str.append("  Using thread pool '");
        str.append(getThreadPoolClass().getName());
        str.append("' - with ");
        str.append(getThreadPoolSize());
        str.append(" threads.");
        str.append("\n");

        str.append("  Using job-store '");
        str.append(getJobStoreClass().getName());
        str.append("' - which ");
        if (jobStoreSupportsPersistence()) str.append("supports persistence.");
        else
            str.append("does not support persistence.");
        str.append("\n");

        return str.toString();
    }

}
