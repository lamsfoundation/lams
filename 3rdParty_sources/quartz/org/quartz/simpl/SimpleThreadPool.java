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
package org.quartz.simpl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.SchedulerConfigException;
import org.quartz.spi.ThreadPool;

/**
 * <p>
 * This is class is a simple implementation of a thread pool, based on the
 * <code>{@link org.quartz.spi.ThreadPool}</code> interface.
 * </p>
 * 
 * <p>
 * <CODE>Runnable</CODE> objects are sent to the pool with the <code>{@link #runInThread(Runnable)}</code>
 * method, which blocks until a <code>Thread</code> becomes available.
 * </p>
 * 
 * <p>
 * The pool has a fixed number of <code>Thread</code>s, and does not grow or
 * shrink based on demand.
 * </p>
 * 
 * @author James House
 * @author Juergen Donnerstag
 */
public class SimpleThreadPool implements ThreadPool {

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * 
     * Data members.
     * 
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    private int count = -1;

    private int prio = Thread.NORM_PRIORITY;

    private boolean isShutdown = false;

    private boolean inheritLoader = false;

    private boolean inheritGroup = true;

    private boolean makeThreadsDaemons = false;

    private ThreadGroup threadGroup;

    private Runnable nextRunnable;

    private Object nextRunnableLock = new Object();

    private WorkerThread[] workers;

    private String threadNamePrefix = "SimpleThreadPoolWorker";
    
    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * 
     * Constructors.
     * 
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    /**
     * <p>
     * Create a new (unconfigured) <code>SimpleThreadPool</code>.
     * </p>
     * 
     * @see #setThreadCount(int)
     * @see #setThreadPriority(int)
     */
    public SimpleThreadPool() {
    }

    /**
     * <p>
     * Create a new <code>SimpleThreadPool</code> with the specified number
     * of <code>Thread</code> s that have the given priority.
     * </p>
     * 
     * @param threadCount
     *          the number of worker <code>Threads</code> in the pool, must
     *          be > 0.
     * @param threadPriority
     *          the thread priority for the worker threads.
     * 
     * @see java.lang.Thread
     */
    public SimpleThreadPool(int threadCount, int threadPriority) {
        setThreadCount(threadCount);
        setThreadPriority(threadPriority);
    }

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * 
     * Interface.
     * 
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    public Log getLog() {
        return LogFactory.getLog(SimpleThreadPool.class);
    }

    public int getPoolSize() {
        return getThreadCount();
    }

    /**
     * <p>
     * Set the number of worker threads in the pool - has no effect after
     * <code>initialize()</code> has been called.
     * </p>
     */
    public void setThreadCount(int count) {
        this.count = count;
    }

    /**
     * <p>
     * Get the number of worker threads in the pool.
     * </p>
     */
    public int getThreadCount() {
        return count;
    }

    /**
     * <p>
     * Set the thread priority of worker threads in the pool - has no effect
     * after <code>initialize()</code> has been called.
     * </p>
     */
    public void setThreadPriority(int prio) {
        this.prio = prio;
    }

    /**
     * <p>
     * Get the thread priority of worker threads in the pool.
     * </p>
     */
    public int getThreadPriority() {
        return prio;
    }

    public void setThreadNamePrefix(String prfx) {
      this.threadNamePrefix = prfx;
    }
    
    public String getThreadNamePrefix() {
      return threadNamePrefix;
    }
    
    /**
     * @return Returns the
     *         threadsInheritContextClassLoaderOfInitializingThread.
     */
    public boolean isThreadsInheritContextClassLoaderOfInitializingThread() {
        return inheritLoader;
    }

    /**
     * @param inheritLoader
     *          The threadsInheritContextClassLoaderOfInitializingThread to
     *          set.
     */
    public void setThreadsInheritContextClassLoaderOfInitializingThread(
            boolean inheritLoader) {
        this.inheritLoader = inheritLoader;
    }

    public boolean isThreadsInheritGroupOfInitializingThread() {
        return inheritGroup;
    }

    public void setThreadsInheritGroupOfInitializingThread(
            boolean inheritGroup) {
        this.inheritGroup = inheritGroup;
    }

    
    /**
     * @return Returns the value of makeThreadsDaemons.
     */
    public boolean isMakeThreadsDaemons() {
        return makeThreadsDaemons;
    }

    /**
     * @param makeThreadsDaemons
     *          The value of makeThreadsDaemons to set.
     */
    public void setMakeThreadsDaemons(boolean makeThreadsDaemons) {
        this.makeThreadsDaemons = makeThreadsDaemons;
    }

    public void initialize() throws SchedulerConfigException {

        if (count <= 0)
                throw new SchedulerConfigException(
                        "Thread count must be > 0");
        if (prio <= 0 || prio > 9)
                throw new SchedulerConfigException(
                        "Thread priority must be > 0 and <= 9");

        if(isThreadsInheritGroupOfInitializingThread())
            threadGroup = Thread.currentThread().getThreadGroup();
        else {
            // follow the threadGroup tree to the root thread group.
            threadGroup = Thread.currentThread().getThreadGroup();
            ThreadGroup parent = threadGroup;
            while ( !parent.getName().equals("main") )
            {
                threadGroup = parent;
                parent = threadGroup.getParent();
            }
            threadGroup = new ThreadGroup(parent, "SimpleThreadPool");
        }
        

        if (isThreadsInheritContextClassLoaderOfInitializingThread())
                getLog().info(
                        "Job execution threads will use class loader of thread: "
                                + Thread.currentThread().getName());

        // create the worker threads and start them
        workers = createWorkerThreads(count);
        for (int i = 0; i < count; ++i) {
            if (isThreadsInheritContextClassLoaderOfInitializingThread()) {
                workers[i].setContextClassLoader(Thread.currentThread()
                        .getContextClassLoader());
            }
        }
    }

    protected WorkerThread[] createWorkerThreads(int count)
    {
        workers = new WorkerThread[count];
        for (int i = 0; i < count; ++i) {
            workers[i] = new WorkerThread(this, threadGroup,
                getThreadNamePrefix() + "-" + i, 
                getThreadPriority(), 
                isMakeThreadsDaemons());
        }
        
        return workers;
    }

    /**
     * <p>
     * Terminate any worker threads in this thread group.
     * </p>
     * 
     * <p>
     * Jobs currently in progress will complete.
     * </p>
     */
    public void shutdown() {
        shutdown(true);
    }

    /**
     * <p>
     * Terminate any worker threads in this thread group.
     * </p>
     * 
     * <p>
     * Jobs currently in progress will complete.
     * </p>
     */
    public void shutdown(boolean waitForJobsToComplete) {
        isShutdown = true;

        // signal each worker thread to shut down
        for (int i = 0; i < workers.length; i++) {
            if (workers[i] != null) workers[i].shutdown();
        }

        // Give waiting (wait(1000)) worker threads a chance to shut down.
        // Active worker threads will shut down after finishing their
        // current job.
        synchronized (nextRunnableLock) {
            nextRunnableLock.notifyAll();
        }

        if (waitForJobsToComplete == true) {
            // Wait until all worker threads are shut down
            int alive = workers.length;
            while (alive > 0) {
                alive = 0;
                for (int i = 0; i < workers.length; i++) {
                    if (workers[i].isAlive()) {
                        try {
                            //if (logger.isDebugEnabled())
                            getLog().debug(
                                    "Waiting for thread no. " + i
                                            + " to shut down");

                            // note: with waiting infinite - join(0) - the
                            // application
                            // may appear to 'hang'. Waiting for a finite time
                            // however
                            // requires an additional loop (alive).
                            alive++;
                            workers[i].join(200);
                        } catch (InterruptedException ex) {
                        }
                    }
                }
            }

            //if (logger.isDebugEnabled()) {
            int activeCount = threadGroup.activeCount();
            if (activeCount > 0)
                    getLog()
                            .info(
                                    "There are still "
                                            + activeCount
                                            + " worker threads active."
                                            + " See javadoc runInThread(Runnable) for a possible explanation");

            getLog().debug("shutdown complete");
            //}
        }
    }

    /**
     * <p>
     * Run the given <code>Runnable</code> object in the next available
     * <code>Thread</code>. If while waiting the thread pool is asked to
     * shut down, the Runnable is executed immediately within a new additional
     * thread.
     * </p>
     * 
     * @param runnable
     *          the <code>Runnable</code> to be added.
     */
    public boolean runInThread(Runnable runnable) {
        if (runnable == null) return false;

        if (isShutdown) {
            try {
                getLog()
                        .info(
                                "SimpleThreadPool.runInThread(): thread pool has been shutdown. Runnable will not be executed");
            } catch(Exception e) {  
                // ignore to help with a tomcat glitch
            }
            
            return false;
        }

        synchronized (nextRunnableLock) {

            // Wait until a worker thread has taken the previous Runnable
            // or until the thread pool is asked to shutdown.
            while ((nextRunnable != null) && !isShutdown) {
                try {
                    nextRunnableLock.wait(1000);
                } catch (InterruptedException ignore) {
                }
            }

            // During normal operation, not shutdown, set the nextRunnable
            // and notify the worker threads waiting (getNextRunnable()).
            if (!isShutdown) {
                nextRunnable = runnable;
                nextRunnableLock.notifyAll();
            }
        }

        // If the thread pool is going down, execute the Runnable
        // within a new additional worker thread (no thread from the pool).
        // note: the synchronized section should be as short (time) as
        //  possible. Starting a new thread is not a quick action.
        if (isShutdown) {
            new WorkerThread(this, threadGroup,
                    "WorkerThread-LastJob", prio, false, runnable);
        }

        return true;
    }

    /**
     * <p>
     * Dequeue the next pending <code>Runnable</code>.
     * </p>
     * 
     * <p>
     * getNextRunnable() should return null if within a specific time no new
     * Runnable is available. This gives the worker thread the chance to check
     * its shutdown flag. In case the worker thread is asked to shut down it
     * will notify on nextRunnableLock, hence interrupt the wait state. That
     * is, the time used for waiting need not be short.
     * </p>
     */
    private Runnable getNextRunnable() throws InterruptedException {
        Runnable toRun = null;

        // Wait for new Runnable (see runInThread()) and notify runInThread()
        // in case the next Runnable is already waiting.
        synchronized (nextRunnableLock) {
            if (nextRunnable == null) nextRunnableLock.wait(1000);

            if (nextRunnable != null) {
                toRun = nextRunnable;
                nextRunnable = null;
                nextRunnableLock.notifyAll();
            }
        }

        return toRun;
    }

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * 
     * WorkerThread Class.
     * 
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    /**
     * <p>
     * A Worker loops, waiting to execute tasks.
     * </p>
     */
    class WorkerThread extends Thread {

        // A flag that signals the WorkerThread to terminate.
        private boolean run = true;

        private SimpleThreadPool tp;

        private Runnable runnable = null;

        /**
         * <p>
         * Create a worker thread and start it. Waiting for the next Runnable,
         * executing it, and waiting for the next Runnable, until the shutdown
         * flag is set.
         * </p>
         */
        WorkerThread(SimpleThreadPool tp, ThreadGroup threadGroup, String name,
                int prio, boolean isDaemon) {

            this(tp, threadGroup, name, prio, isDaemon, null);
        }

        /**
         * <p>
         * Create a worker thread, start it, execute the runnable and terminate
         * the thread (one time execution).
         * </p>
         */
        WorkerThread(SimpleThreadPool tp, ThreadGroup threadGroup, String name,
                int prio, boolean isDaemon, Runnable runnable) {

            super(threadGroup, name);
            this.tp = tp;
            this.runnable = runnable;
            setPriority(prio);
            setDaemon(isDaemon);
            start();
        }

        /**
         * <p>
         * Signal the thread that it should terminate.
         * </p>
         */
        void shutdown() {
            run = false;

            // @todo I'm not really sure if we should interrupt the thread.
            // Javadoc mentions that it interrupts blocked I/O operations as
            // well. Hence the job will most likely fail. I think we should
            // shut the work thread gracefully, by letting the job finish
            // uninterrupted. See SimpleThreadPool.shutdown()
            //interrupt();
        }

        /**
         * <p>
         * Loop, executing targets as they are received.
         * </p>
         */
        public void run() {
            boolean runOnce = (runnable != null);

            while (run) {
                try {
                    if (runnable == null) runnable = tp.getNextRunnable();

                    if (runnable != null) runnable.run();
                } catch (InterruptedException unblock) {
                    // do nothing (loop will terminate if shutdown() was called
                    try {
                        getLog().error("worker threat got 'interrupt'ed.", unblock);
                    } catch(Exception e) {  
                        // ignore to help with a tomcat glitch
                    }
                } catch (Exception exceptionInRunnable) {
                    try {
                        getLog().error("Error while executing the Runnable: ",
                            exceptionInRunnable);
                    } catch(Exception e) {  
                        // ignore to help with a tomcat glitch
                    }
                } finally {
                    if (runOnce) run = false;

                    runnable = null;

                    // repair the thread in case the runnable mucked it up...
                    setPriority(tp.getThreadPriority());
                }
            }

            //if (log.isDebugEnabled())
            try {
                getLog().debug("WorkerThread is shutting down");
            } catch(Exception e) {  
                // ignore to help with a tomcat glitch
            }
}
    }
}
