
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobPersistenceException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.spi.TriggerFiredBundle;

/**
 * <p>
 * JobRunShell instances are responsible for providing the 'safe' environment
 * for <code>Job</code> s to run in, and for performing all of the work of
 * executing the <code>Job</code>, catching ANY thrown exceptions, updating
 * the <code>Trigger</code> with the <code>Job</code>'s completion code,
 * etc.
 * </p>
 * 
 * <p>
 * A <code>JobRunShell</code> instance is created by a <code>JobRunShellFactory</code>
 * on behalf of the <code>QuartzSchedulerThread</code> which then runs the
 * shell in a thread from the configured <code>ThreadPool</code> when the
 * scheduler determines that a <code>Job</code> has been triggered.
 * </p>
 * 
 * @see JobRunShellFactory
 * @see org.quartz.core.QuartzSchedulerThread
 * @see org.quartz.Job
 * @see org.quartz.Trigger
 * 
 * @author James House
 */
public class JobRunShell implements Runnable {
    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * 
     * Data members.
     * 
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    protected JobExecutionContext jec = null;

    protected QuartzScheduler qs = null;

    protected Scheduler scheduler = null;

    protected SchedulingContext schdCtxt = null;

    protected JobRunShellFactory jobRunShellFactory = null;

    protected boolean shutdownRequested = false;

    protected Log log = LogFactory.getLog(getClass());
    
    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * 
     * Constructors.
     * 
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    /**
     * <p>
     * Create a JobRunShell instance with the given settings.
     * </p>
     * 
     * @param jobRunShellFactory
     *          A handle to the <code>JobRunShellFactory</code> that produced
     *          this <code>JobRunShell</code>.
     * @param scheduler
     *          The <code>Scheduler</code> instance that should be made
     *          available within the <code>JobExecutionContext</code>.
     * @param schdCtxt
     *          the <code>SchedulingContext</code> that should be used by the
     *          <code>JobRunShell</code> when making updates to the <code>JobStore</code>.
     */
    public JobRunShell(JobRunShellFactory jobRunShellFactory,
            Scheduler scheduler, SchedulingContext schdCtxt) {
        this.jobRunShellFactory = jobRunShellFactory;
        this.scheduler = scheduler;
        this.schdCtxt = schdCtxt;
    }

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * 
     * Interface.
     * 
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    private static Log getLog()
    {
        return LogFactory.getLog(JobRunShell.class);
    }
    
    public void initialize(QuartzScheduler qs, TriggerFiredBundle firedBundle)
            throws SchedulerException {
        this.qs = qs;

        Job job = null;
        JobDetail jobDetail = firedBundle.getJobDetail();

        try {
            job = qs.getJobFactory().newJob(firedBundle);
        } catch (SchedulerException se) {
            qs.notifySchedulerListenersError(
                    "An error occured instantiating job to be executed. job= '"
                            + jobDetail.getFullName() + "'", se);
            throw se;
        } catch (Exception e) {
            SchedulerException se = new SchedulerException(
                    "Problem instantiating class '"
                            + jobDetail.getJobClass().getName() + "'", e);
            qs.notifySchedulerListenersError(
                    "An error occured instantiating job to be executed. job= '"
                            + jobDetail.getFullName() + "'", se);
            throw se;
        } catch (Throwable ncdfe) { // such as NoClassDefFoundError
            SchedulerException se = new SchedulerException(
                    "Problem instantiating class '"
                            + jobDetail.getJobClass().getName() + "' - " + ncdfe);
            qs.notifySchedulerListenersError(
                    "An error occured instantiating job to be executed. job= '"
                            + jobDetail.getFullName() + "'", se);
            throw se;
        }

        this.jec = new JobExecutionContext(scheduler, firedBundle, job);
    }

    public void requestShutdown() {
        shutdownRequested = true;
    }

    public void run() {
        Trigger trigger = jec.getTrigger();
        JobDetail jobDetail = jec.getJobDetail();

        do {

            JobExecutionException jobExEx = null;
            Job job = jec.getJobInstance();

            try {
                begin();
            } catch (SchedulerException se) {
                qs.notifySchedulerListenersError("Error executing Job ("
                        + jec.getJobDetail().getFullName()
                        + ": couldn't begin execution.", se);
                break;
            }

            // notify job & trigger listeners...
            try {
                if (!notifyListenersBeginning(jec)) break;
            }
            catch(VetoedException ve) {
                try {
                    complete(true);
                } catch (SchedulerException se) {
                    qs.notifySchedulerListenersError("Error during veto of Job ("
                            + jec.getJobDetail().getFullName()
                            + ": couldn't finalize execution.", se);
                }
                break;
            }

            long startTime = System.currentTimeMillis();
            long endTime = startTime;
            
            // execute the job
            try {
                log.debug("Calling execute on job " + jobDetail.getFullName());
                job.execute(jec);
                endTime = System.currentTimeMillis();
            } catch (JobExecutionException jee) {
                endTime = System.currentTimeMillis();
                jobExEx = jee;
                getLog().info("Job " + jobDetail.getFullName() + 
                        " threw a JobExecutionException: ", jobExEx);
            } catch (Exception e) {
                endTime = System.currentTimeMillis();
                getLog().error("Job " + jobDetail.getFullName() + 
                        " threw an unhandled Exception: ", e);
                SchedulerException se = new SchedulerException(
                        "Job threw an unhandled exception.", e);
                se.setErrorCode(SchedulerException.ERR_JOB_EXECUTION_THREW_EXCEPTION);
                qs.notifySchedulerListenersError("Job ("
                        + jec.getJobDetail().getFullName()
                        + " threw an exception.", se);
                jobExEx = new JobExecutionException(se, false);
                jobExEx.setErrorCode(JobExecutionException.ERR_JOB_EXECUTION_THREW_EXCEPTION);
            } 
            
            jec.setJobRunTime(endTime - startTime);

            // notify all job listeners
            if (!notifyJobListenersComplete(jec, jobExEx)) break;

            int instCode = Trigger.INSTRUCTION_NOOP;

            // update the trigger
            try {
                instCode = trigger.executionComplete(jec, jobExEx);
            } catch (Exception e) {
                // If this happens, there's a bug in the trigger...
                SchedulerException se = new SchedulerException(
                        "Trigger threw an unhandled exception.", e);
                se.setErrorCode(SchedulerException.ERR_TRIGGER_THREW_EXCEPTION);
                qs.notifySchedulerListenersError(
                        "Please report this error to the Quartz developers.",
                        se);
            }

            // notify all trigger listeners
            if (!notifyTriggerListenersComplete(jec, instCode)) break;

            // update job/trigger or re-execute job
            if (instCode == Trigger.INSTRUCTION_RE_EXECUTE_JOB) {
                jec.incrementRefireCount();
                try {
                    complete(false);
                } catch (SchedulerException se) {
                    qs.notifySchedulerListenersError("Error executing Job ("
                            + jec.getJobDetail().getFullName()
                            + ": couldn't finalize execution.", se);
                }
                continue;
            }

            try {
                complete(true);
            } catch (SchedulerException se) {
                qs.notifySchedulerListenersError("Error executing Job ("
                        + jec.getJobDetail().getFullName()
                        + ": couldn't finalize execution.", se);
                continue;
            }

            try {
                qs.notifyJobStoreJobComplete(schdCtxt, trigger, jobDetail,
                        instCode);
            } catch (JobPersistenceException jpe) {
                qs.notifySchedulerListenersError(
                        "An error occured while marking executed job complete. job= '"
                                + jobDetail.getFullName() + "'", jpe);
                if (!completeTriggerRetryLoop(trigger, jobDetail, instCode)) 
                ;
                return;
            }

            break;
        } while (true);

        qs.notifySchedulerThread();

        jobRunShellFactory.returnJobRunShell(this);
    }

    protected void begin() throws SchedulerException {
    }

    protected void complete(boolean successfulExecution)
            throws SchedulerException {
    }

    public void passivate() {
        jec = null;
        qs = null;
    }

    private boolean notifyListenersBeginning(JobExecutionContext jec) throws VetoedException {
        
        boolean vetoed = false;
        
        // notify all trigger listeners
        try {
            vetoed = qs.notifyTriggerListenersFired(jec);
        } catch (SchedulerException se) {
            qs.notifySchedulerListenersError(
                    "Unable to notify TriggerListener(s) while firing trigger "
                            + "(Trigger and Job will NOT be fired!). trigger= "
                            + jec.getTrigger().getFullName() + " job= "
                            + jec.getJobDetail().getFullName(), se);

            return false;
        }

        if(vetoed) {
            try {
                qs.notifyJobListenersWasVetoed(jec);
            } catch (SchedulerException se) {
                qs.notifySchedulerListenersError(
                        "Unable to notify JobListener(s) of vetoed execution " +
                        "while firing trigger (Trigger and Job will NOT be " +
                        "fired!). trigger= "
                        + jec.getTrigger().getFullName() + " job= "
                        + jec.getJobDetail().getFullName(), se);

            }
            throw new VetoedException();
        }
            
        // notify all job listeners
        try {
            qs.notifyJobListenersToBeExecuted(jec);
        } catch (SchedulerException se) {
            qs.notifySchedulerListenersError(
                    "Unable to notify JobListener(s) of Job to be executed: "
                            + "(Job will NOT be executed!). trigger= "
                            + jec.getTrigger().getFullName() + " job= "
                            + jec.getJobDetail().getFullName(), se);

            return false;
        }

        return true;
    }

    private boolean notifyJobListenersComplete(JobExecutionContext jec,
            JobExecutionException jobExEx) {
        try {
            qs.notifyJobListenersWasExecuted(jec, jobExEx);
        } catch (SchedulerException se) {
            qs.notifySchedulerListenersError(
                    "Unable to notify JobListener(s) of Job that was executed: "
                            + "(error will be ignored). trigger= "
                            + jec.getTrigger().getFullName() + " job= "
                            + jec.getJobDetail().getFullName(), se);

            return false;
        }

        return true;
    }

    private boolean notifyTriggerListenersComplete(JobExecutionContext jec,
            int instCode) {
        try {
            qs.notifyTriggerListenersComplete(jec, instCode);

        } catch (SchedulerException se) {
            qs.notifySchedulerListenersError(
                    "Unable to notify TriggerListener(s) of Job that was executed: "
                            + "(error will be ignored). trigger= "
                            + jec.getTrigger().getFullName() + " job= "
                            + jec.getJobDetail().getFullName(), se);

            return false;
        }
        if (jec.getTrigger().getNextFireTime() == null)
                qs.notifySchedulerListenersFinalized(jec.getTrigger());

        return true;
    }

    public boolean completeTriggerRetryLoop(Trigger trigger,
            JobDetail jobDetail, int instCode) {
        while (!shutdownRequested) {
            try {
                Thread.sleep(5 * 1000l); // retry every 5 seconds (the db
                // connection must be failed)
                qs.notifyJobStoreJobComplete(schdCtxt, trigger, jobDetail,
                        instCode);
                return true;
            } catch (JobPersistenceException jpe) {
                qs.notifySchedulerListenersError(
                        "An error occured while marking executed job complete. job= '"
                                + jobDetail.getFullName() + "'", jpe);
            } catch (InterruptedException ignore) {
            }
        }
        return false;
    }

    
    class VetoedException extends Exception
    {
        public VetoedException()
        {
        }
    }
}