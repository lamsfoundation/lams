
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

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * <p>
 * Base class for exceptions thrown by the Quartz <code>{@link Scheduler}</code>.
 * </p>
 * 
 * <p>
 * <code>SchedulerException</code> s may contain a reference to another
 * <code>Exception</code>, which was the underlying cause of the <code>SchedulerException</code>.
 * </p>
 * 
 * @author James House
 */
public class SchedulerException extends Exception {

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * 
     * Constants.
     * 
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    public static final int ERR_UNSPECIFIED = 0;

    public static final int ERR_BAD_CONFIGURATION = 50;

    public static final int ERR_TIME_BROKER_FAILURE = 70;

    public static final int ERR_CLIENT_ERROR = 100;

    public static final int ERR_COMMUNICATION_FAILURE = 200;

    public static final int ERR_UNSUPPORTED_FUNCTION_IN_THIS_CONFIGURATION = 210;

    public static final int ERR_PERSISTENCE = 400;

    public static final int ERR_PERSISTENCE_JOB_DOES_NOT_EXIST = 410;

    public static final int ERR_PERSISTENCE_CALENDAR_DOES_NOT_EXIST = 420;

    public static final int ERR_PERSISTENCE_TRIGGER_DOES_NOT_EXIST = 430;

    public static final int ERR_PERSISTENCE_CRITICAL_FAILURE = 499;

    public static final int ERR_THREAD_POOL = 500;

    public static final int ERR_THREAD_POOL_EXHAUSTED = 510;

    public static final int ERR_THREAD_POOL_CRITICAL_FAILURE = 599;

    public static final int ERR_JOB_LISTENER = 600;

    public static final int ERR_JOB_LISTENER_NOT_FOUND = 610;

    public static final int ERR_TRIGGER_LISTENER = 700;

    public static final int ERR_TRIGGER_LISTENER_NOT_FOUND = 710;

    public static final int ERR_JOB_EXECUTION_THREW_EXCEPTION = 800;

    public static final int ERR_TRIGGER_THREW_EXCEPTION = 850;

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * 
     * Data members.
     * 
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    private Exception cause;

    private int errorCode = ERR_UNSPECIFIED;

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * 
     * Constructors.
     * 
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    public SchedulerException() {
        super();
    }

    public SchedulerException(String msg) {
        super(msg);
    }

    public SchedulerException(String msg, int errorCode) {
        super(msg);
        setErrorCode(errorCode);
    }

    public SchedulerException(Exception cause) {
        super(cause.toString());
        this.cause = cause;
    }

    public SchedulerException(String msg, Exception cause) {
        super(msg);
        this.cause = cause;
    }

    public SchedulerException(String msg, Exception cause, int errorCode) {
        super(msg);
        this.cause = cause;
        setErrorCode(errorCode);
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
     * Return the exception that is the underlying cause of this exception.
     * </p>
     * 
     * <p>
     * This may be used to find more detail about the cause of the error.
     * </p>
     * 
     * @return the underlying exception, or <code>null</code> if there is not
     *         one.
     */
    public Throwable getUnderlyingException() {
        return cause;
    }

    /**
     * <p>
     * Get the error code associated with this exception.
     * </p>
     * 
     * <p>
     * This may be used to find more detail about the cause of the error.
     * </p>
     * 
     * @return one of the ERR_XXX constants defined in this class.
     */
    public int getErrorCode() {
        return errorCode;
    }

    /**
     * <p>
     * Get the error code associated with this exception.
     * </p>
     * 
     * <p>
     * This may be used to provide more detail about the cause of the error.
     * </p>
     * 
     * @param errorCode
     *          one of the ERR_XXX constants defined in this class.
     */
    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    /**
     * <p>
     * Determine if the specified error code is in the <code>'ERR_PERSISTENCE'</code>
     * category of errors.
     * </p>
     */
    public boolean isPersistenceError() {
        return (errorCode >= ERR_PERSISTENCE && errorCode <= ERR_PERSISTENCE + 99);
    }

    /**
     * <p>
     * Determine if the specified error code is in the <code>'ERR_THREAD_POOL'</code>
     * category of errors.
     * </p>
     */
    public boolean isThreadPoolError() {
        return (errorCode >= ERR_THREAD_POOL && errorCode <= ERR_THREAD_POOL + 99);
    }

    /**
     * <p>
     * Determine if the specified error code is in the <code>'ERR_JOB_LISTENER'</code>
     * category of errors.
     * </p>
     */
    public boolean isJobListenerError() {
        return (errorCode >= ERR_JOB_LISTENER && errorCode <= ERR_JOB_LISTENER + 99);
    }

    /**
     * <p>
     * Determine if the specified error code is in the <code>'ERR_TRIGGER_LISTENER'</code>
     * category of errors.
     * </p>
     */
    public boolean isTriggerListenerError() {
        return (errorCode >= ERR_TRIGGER_LISTENER && errorCode <= ERR_TRIGGER_LISTENER + 99);
    }

    /**
     * <p>
     * Determine if the specified error code is in the <code>'ERR_CLIENT_ERROR'</code>
     * category of errors.
     * </p>
     */
    public boolean isClientError() {
        return (errorCode >= ERR_CLIENT_ERROR && errorCode <= ERR_CLIENT_ERROR + 99);
    }

    /**
     * <p>
     * Determine if the specified error code is in the <code>'ERR_CLIENT_ERROR'</code>
     * category of errors.
     * </p>
     */
    public boolean isConfigurationError() {
        return (errorCode >= ERR_BAD_CONFIGURATION && errorCode <= ERR_BAD_CONFIGURATION + 49);
    }

    public String toString() {
        if (cause == null) return super.toString();
        else
            return super.toString() + " [See nested exception: "
                    + cause.toString() + "]";
    }

    /**
     * <P>
     * Print a stack trace to the standard error stream.
     * </P>
     * 
     * <P>
     * This overridden version will print the nested stack trace if available,
     * otherwise it prints only this exception's stack.
     * </P>
     */
    public void printStackTrace() {
        printStackTrace(System.err);
    }

    /**
     * <P>
     * Print a stack trace to the specified stream.
     * </P>
     * 
     * <P>
     * This overridden version will print the nested stack trace if available,
     * otherwise it prints only this exception's stack.
     * </P>
     * 
     * @param out
     *          the stream to which the stack traces will be printed.
     */
    public void printStackTrace(PrintStream out) {
        super.printStackTrace(out);
        if ((cause != null)) {
            synchronized (out) {
                out
                        .println("* Nested Exception (Underlying Cause) ---------------");
                cause.printStackTrace(out);
            }
        }
    }

    /**
     * <P>
     * Print a stack trace to the specified writer.
     * </P>
     * 
     * <P>
     * This overridden version will print the nested stack trace if available,
     * otherwise it prints this exception's stack.
     * </P>
     * 
     * @param out
     *          the writer to which the stack traces will be printed.
     */
    public void printStackTrace(PrintWriter out) {
        super.printStackTrace(out);
        if ((cause != null)) {
            synchronized (out) {
                out
                        .println("* Nested Exception (Underlying Cause) ---------------");
                cause.printStackTrace(out);
            }
        }
    }

}
