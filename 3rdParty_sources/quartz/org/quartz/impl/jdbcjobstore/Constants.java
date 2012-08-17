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
 * This interface can be implemented by any <code>{@link
 * org.quartz.impl.jdbcjobstore.DriverDelegate}</code>
 * class that needs to use the constants contained herein.
 * </p>
 * 
 * @author <a href="mailto:jeff@binaryfeed.org">Jeffrey Wescott</a>
 * @author James House
 */
public interface Constants {

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * 
     * Constants.
     * 
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    // Table names
    public static final String TABLE_JOB_DETAILS = "JOB_DETAILS";

    public static final String TABLE_TRIGGERS = "TRIGGERS";

    public static final String TABLE_SIMPLE_TRIGGERS = "SIMPLE_TRIGGERS";

    public static final String TABLE_CRON_TRIGGERS = "CRON_TRIGGERS";

    public static final String TABLE_BLOB_TRIGGERS = "BLOB_TRIGGERS";

    public static final String TABLE_FIRED_TRIGGERS = "FIRED_TRIGGERS";

    public static final String TABLE_JOB_LISTENERS = "JOB_LISTENERS";

    public static final String TABLE_TRIGGER_LISTENERS = "TRIGGER_LISTENERS";

    public static final String TABLE_CALENDARS = "CALENDARS";

    public static final String TABLE_PAUSED_TRIGGERS = "PAUSED_TRIGGER_GRPS";

    public static final String TABLE_LOCKS = "LOCKS";

    public static final String TABLE_SCHEDULER_STATE = "SCHEDULER_STATE";

    // TABLE_JOB_DETAILS columns names
    public static final String COL_JOB_NAME = "JOB_NAME";

    public static final String COL_JOB_GROUP = "JOB_GROUP";

    public static final String COL_IS_DURABLE = "IS_DURABLE";

    public static final String COL_IS_VOLATILE = "IS_VOLATILE";

    public static final String COL_IS_STATEFUL = "IS_STATEFUL";

    public static final String COL_REQUESTS_RECOVERY = "REQUESTS_RECOVERY";

    public static final String COL_JOB_DATAMAP = "JOB_DATA";

    public static final String COL_JOB_CLASS = "JOB_CLASS_NAME";

    public static final String COL_DESCRIPTION = "DESCRIPTION";

    // TABLE_JOB_LISTENERS columns names
    public static final String COL_JOB_LISTENER = "JOB_LISTENER";

    // TABLE_TRIGGERS columns names
    public static final String COL_TRIGGER_NAME = "TRIGGER_NAME";

    public static final String COL_TRIGGER_GROUP = "TRIGGER_GROUP";

    public static final String COL_NEXT_FIRE_TIME = "NEXT_FIRE_TIME";

    public static final String COL_PREV_FIRE_TIME = "PREV_FIRE_TIME";

    public static final String COL_TRIGGER_STATE = "TRIGGER_STATE";

    public static final String COL_TRIGGER_TYPE = "TRIGGER_TYPE";

    public static final String COL_START_TIME = "START_TIME";

    public static final String COL_END_TIME = "END_TIME";

    public static final String COL_MISFIRE_INSTRUCTION = "MISFIRE_INSTR";

    public static final String ALIAS_COL_NEXT_FIRE_TIME = "ALIAS_NXT_FR_TM";

    // TABLE_SIMPLE_TRIGGERS columns names
    public static final String COL_REPEAT_COUNT = "REPEAT_COUNT";

    public static final String COL_REPEAT_INTERVAL = "REPEAT_INTERVAL";

    public static final String COL_TIMES_TRIGGERED = "TIMES_TRIGGERED";

    // TABLE_CRON_TRIGGERS columns names
    public static final String COL_CRON_EXPRESSION = "CRON_EXPRESSION";

    // TABLE_BLOB_TRIGGERS columns names
    public static final String COL_BLOB = "BLOB_DATA";

    public static final String COL_TIME_ZONE_ID = "TIME_ZONE_ID";

    // TABLE_TRIGGER_LISTENERS
    public static final String COL_TRIGGER_LISTENER = "TRIGGER_LISTENER";

    // TABLE_FIRED_TRIGGERS columns names
    public static final String COL_INSTANCE_NAME = "INSTANCE_NAME";

    public static final String COL_FIRED_TIME = "FIRED_TIME";

    public static final String COL_ENTRY_ID = "ENTRY_ID";

    public static final String COL_ENTRY_STATE = "STATE";

    // TABLE_CALENDARS columns names
    public static final String COL_CALENDAR_NAME = "CALENDAR_NAME";

    public static final String COL_CALENDAR = "CALENDAR";

    // TABLE_LOCKS columns names
    public static final String COL_LOCK_NAME = "LOCK_NAME";

    // TABLE_LOCKS columns names
    public static final String COL_LAST_CHECKIN_TIME = "LAST_CHECKIN_TIME";

    public static final String COL_CHECKIN_INTERVAL = "CHECKIN_INTERVAL";

    public static final String COL_RECOVERER = "RECOVERER";

    // MISC CONSTANTS
    public static final String DEFAULT_TABLE_PREFIX = "QRTZ_";

    // STATES
    public final static String STATE_WAITING = "WAITING";

    public final static String STATE_ACQUIRED = "ACQUIRED";

    public final static String STATE_EXECUTING = "EXECUTING";

    public final static String STATE_COMPLETE = "COMPLETE";

    public final static String STATE_BLOCKED = "BLOCKED";

    public final static String STATE_ERROR = "ERROR";

    public final static String STATE_PAUSED = "PAUSED";

    public final static String STATE_PAUSED_BLOCKED = "PAUSED_BLOCKED";

    public final static String STATE_DELETED = "DELETED";

    public final static String STATE_MISFIRED = "MISFIRED";

    public final static String ALL_GROUPS_PAUSED = "_$_ALL_GROUPS_PAUSED_$_";

    // TRIGGER TYPES
    public final static String TTYPE_SIMPLE = "SIMPLE";

    public final static String TTYPE_CRON = "CRON";

    public final static String TTYPE_BLOB = "BLOB";
}

// EOF
