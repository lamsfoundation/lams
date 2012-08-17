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
package org.quartz.impl.jdbcjobstore;

/**
 * <p>
 * Conveys a scheduler-instance state record.
 * </p>
 * 
 * @author James House
 */
public class SchedulerStateRecord implements java.io.Serializable {

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * 
     * Data members.
     * 
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    private String schedulerInstanceId;

    private long checkinTimestamp;

    private long checkinInterval;

    private String recoverer;

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * 
     * Interface.
     * 
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    /**
     */
    public long getCheckinInterval() {
        return checkinInterval;
    }

    /**
     */
    public long getCheckinTimestamp() {
        return checkinTimestamp;
    }

    /**
     */
    public String getRecoverer() {
        return recoverer;
    }

    /**
     */
    public String getSchedulerInstanceId() {
        return schedulerInstanceId;
    }

    /**
     */
    public void setCheckinInterval(long l) {
        checkinInterval = l;
    }

    /**
     */
    public void setCheckinTimestamp(long l) {
        checkinTimestamp = l;
    }

    /**
     */
    public void setRecoverer(String string) {
        recoverer = string;
    }

    /**
     */
    public void setSchedulerInstanceId(String string) {
        schedulerInstanceId = string;
    }

}

// EOF
