
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
package org.quartz.ee.jta;

import javax.naming.InitialContext;
import javax.transaction.UserTransaction;

import org.quartz.SchedulerConfigException;
import org.quartz.SchedulerException;

/**
 * <p>
 * A helper for obtaining a handle to a UserTransaction...
 * </p>
 * 
 * @author James House
 */
public class UserTransactionHelper {

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * 
     * Constants.
     * 
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    public static final String DEFAULT_USER_TX_LOCATION = "java:comp/UserTransaction";

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * 
     * Data members.
     * 
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    private InitialContext ctxt;

    private UserTransaction ut;

    private String userTxURL;

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * 
     * Constructors.
     * 
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    /**
     * <p>
     * Create a UserTransactionHelper instance with the given settings.
     * </p>
     */
    public UserTransactionHelper(String userTxURL)
            throws SchedulerConfigException {

        try {
            ctxt = new InitialContext();
        } catch (Exception e) {
            throw new SchedulerConfigException(
                    "JTAJobRunShellFactory initialization failed.", e);
        }
        setUserTxLocation(userTxURL);
    }

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * 
     * Interface.
     * 
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    public String getUserTxLocation() {
        return userTxURL;
    }

    /**
     * Set the JNDI URL at which the Application Server's UserTransaction can
     * be found. If not set, the default value is "java:comp/UserTransaction" -
     * which works for nearly all application servers.
     */
    public void setUserTxLocation(String userTxURL) {
        if (userTxURL == null) userTxURL = DEFAULT_USER_TX_LOCATION;

        this.userTxURL = userTxURL;
    }

    public UserTransaction lookup() throws SchedulerException {
        try {
            return (UserTransaction) ctxt.lookup(userTxURL);
        } catch (Exception nse) {
            throw new SchedulerException(
                    "UserTransactionHelper could not lookup/create UserTransaction.",
                    nse);
        }
    }

}