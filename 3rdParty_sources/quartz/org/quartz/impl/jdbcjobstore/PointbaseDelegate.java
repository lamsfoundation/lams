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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.quartz.Calendar;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;

/**
 * <p>
 * This is a driver delegate for the Pointbase JDBC driver.
 * </p>
 * 
 * @author Gregg Freeman
 */
public class PointbaseDelegate extends StdJDBCDelegate {

    //private static Category log =
    // Category.getInstance(PointbaseJDBCDelegate.class);
    /**
     * <p>
     * Create new PointbaseJDBCDelegate instance.
     * </p>
     * 
     * @param logger
     *          the logger to use during execution
     * @param tablePrefix
     *          the prefix of all table names
     */
    public PointbaseDelegate(Log logger, String tablePrefix, String instanceId) {
        super(logger, tablePrefix, instanceId);
    }

    /**
     * <p>
     * Create new PointbaseJDBCDelegate instance.
     * </p>
     * 
     * @param logger
     *          the logger to use during execution
     * @param tablePrefix
     *          the prefix of all table names
     */
    public PointbaseDelegate(Log logger, String tablePrefix, String instanceId,
            Boolean useProperties) {
        super(logger, tablePrefix, instanceId, useProperties);
    }

    //---------------------------------------------------------------------------
    // jobs
    //---------------------------------------------------------------------------

    /**
     * <p>
     * Insert the job detail record.
     * </p>
     * 
     * @param conn
     *          the DB Connection
     * @param job
     *          the job to insert
     * @return number of rows inserted
     * @throws IOException
     *           if there were problems serializing the JobDataMap
     */
    public int insertJobDetail(Connection conn, JobDetail job)
            throws IOException, SQLException {
        //log.debug( "Inserting JobDetail " + job );
        ByteArrayOutputStream baos = serializeJobData(job.getJobDataMap());
        int len = baos.toByteArray().length;
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());

        PreparedStatement ps = null;

        int insertResult = 0;

        try {
            ps = conn.prepareStatement(rtp(INSERT_JOB_DETAIL));
            ps.setString(1, job.getName());
            ps.setString(2, job.getGroup());
            ps.setString(3, job.getDescription());
            ps.setString(4, job.getJobClass().getName());
            ps.setBoolean(5, job.isDurable());
            ps.setBoolean(6, job.isVolatile());
            ps.setBoolean(7, job.isStateful());
            ps.setBoolean(8, job.requestsRecovery());
            ps.setBinaryStream(9, bais, len);

            insertResult = ps.executeUpdate();
        } finally {
            if (null != ps) {
                try {
                    ps.close();
                } catch (SQLException ignore) {
                }
            }
        }

        if (insertResult > 0) {
            String[] jobListeners = job.getJobListenerNames();
            for (int i = 0; jobListeners != null && i < jobListeners.length; i++)
                insertJobListener(conn, job, jobListeners[i]);
        }

        return insertResult;
    }

    /**
     * <p>
     * Update the job detail record.
     * </p>
     * 
     * @param conn
     *          the DB Connection
     * @param job
     *          the job to update
     * @return number of rows updated
     * @throws IOException
     *           if there were problems serializing the JobDataMap
     */
    public int updateJobDetail(Connection conn, JobDetail job)
            throws IOException, SQLException {
        //log.debug( "Updating job detail " + job );
        ByteArrayOutputStream baos = serializeJobData(job.getJobDataMap());
        int len = baos.toByteArray().length;
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());

        PreparedStatement ps = null;

        int insertResult = 0;

        try {
            ps = conn.prepareStatement(rtp(UPDATE_JOB_DETAIL));
            ps.setString(1, job.getDescription());
            ps.setString(2, job.getJobClass().getName());
            ps.setBoolean(3, job.isDurable());
            ps.setBoolean(4, job.isVolatile());
            ps.setBoolean(5, job.isStateful());
            ps.setBoolean(6, job.requestsRecovery());
            ps.setBinaryStream(7, bais, len);
            ps.setString(8, job.getName());
            ps.setString(9, job.getGroup());

            insertResult = ps.executeUpdate();
        } finally {
            if (null != ps) {
                try {
                    ps.close();
                } catch (SQLException ignore) {
                }
            }
        }

        if (insertResult > 0) {
            deleteJobListeners(conn, job.getName(), job.getGroup());

            String[] jobListeners = job.getJobListenerNames();
            for (int i = 0; jobListeners != null && i < jobListeners.length; i++)
                insertJobListener(conn, job, jobListeners[i]);
        }

        return insertResult;
    }

    public int insertTrigger(Connection conn, Trigger trigger, String state,
            JobDetail jobDetail) throws SQLException, IOException {

        ByteArrayOutputStream baos = serializeJobData(trigger.getJobDataMap());
        int len = baos.toByteArray().length;
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        
        PreparedStatement ps = null;

        int insertResult = 0;

        try {
            ps = conn.prepareStatement(rtp(INSERT_TRIGGER));
            ps.setString(1, trigger.getName());
            ps.setString(2, trigger.getGroup());
            ps.setString(3, trigger.getJobName());
            ps.setString(4, trigger.getJobGroup());
            ps.setBoolean(5, trigger.isVolatile());
            ps.setString(6, trigger.getDescription());
            ps.setBigDecimal(7, new BigDecimal(String.valueOf(trigger
                    .getNextFireTime().getTime())));
            long prevFireTime = -1;
            if (trigger.getPreviousFireTime() != null) {
                prevFireTime = trigger.getPreviousFireTime().getTime();
            }
            ps.setBigDecimal(8, new BigDecimal(String.valueOf(prevFireTime)));
            ps.setString(9, state);
            if (trigger instanceof SimpleTrigger) {
                ps.setString(10, TTYPE_SIMPLE);
            } else if (trigger instanceof CronTrigger) {
                ps.setString(10, TTYPE_CRON);
            } else { // (trigger instanceof BlobTrigger)
                ps.setString(10, TTYPE_BLOB);
            }
            ps.setBigDecimal(11, new BigDecimal(String.valueOf(trigger
                    .getStartTime().getTime())));
            long endTime = 0;
            if (trigger.getEndTime() != null) {
                endTime = trigger.getEndTime().getTime();
            }
            ps.setBigDecimal(12, new BigDecimal(String.valueOf(endTime)));
            ps.setString(13, trigger.getCalendarName());
            ps.setInt(14, trigger.getMisfireInstruction());
            ps.setBinaryStream(15, bais, len);
            
            insertResult = ps.executeUpdate();
        } finally {
            if (null != ps) {
                try {
                    ps.close();
                } catch (SQLException ignore) {
                }
            }
        }

        if (insertResult > 0) {
            String[] trigListeners = trigger.getTriggerListenerNames();
            for (int i = 0; trigListeners != null && i < trigListeners.length; i++)
                insertTriggerListener(conn, trigger, trigListeners[i]);
        }

        return insertResult;
    }
    
    public int updateTrigger(Connection conn, Trigger trigger, String state,
            JobDetail jobDetail) throws SQLException, IOException {

        ByteArrayOutputStream baos = serializeJobData(trigger.getJobDataMap());
        int len = baos.toByteArray().length;
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
                
        PreparedStatement ps = null;

        int insertResult = 0;


        try {
            ps = conn.prepareStatement(rtp(UPDATE_TRIGGER));
                
            ps.setString(1, trigger.getJobName());
            ps.setString(2, trigger.getJobGroup());
            ps.setBoolean(3, trigger.isVolatile());
            ps.setString(4, trigger.getDescription());
            long nextFireTime = -1;
            if (trigger.getNextFireTime() != null) {
                nextFireTime = trigger.getNextFireTime().getTime();
            }
            ps.setBigDecimal(5, new BigDecimal(String.valueOf(nextFireTime)));
            long prevFireTime = -1;
            if (trigger.getPreviousFireTime() != null) {
                prevFireTime = trigger.getPreviousFireTime().getTime();
            }
            ps.setBigDecimal(6, new BigDecimal(String.valueOf(prevFireTime)));
            ps.setString(7, state);
            if (trigger instanceof SimpleTrigger) {
                //                updateSimpleTrigger(conn, (SimpleTrigger)trigger);
                ps.setString(8, TTYPE_SIMPLE);
            } else if (trigger instanceof CronTrigger) {
                //                updateCronTrigger(conn, (CronTrigger)trigger);
                ps.setString(8, TTYPE_CRON);
            } else {
                //                updateBlobTrigger(conn, trigger);
                ps.setString(8, TTYPE_BLOB);
            }
            ps.setBigDecimal(9, new BigDecimal(String.valueOf(trigger
                    .getStartTime().getTime())));
            long endTime = 0;
            if (trigger.getEndTime() != null) {
                endTime = trigger.getEndTime().getTime();
            }
            ps.setBigDecimal(10, new BigDecimal(String.valueOf(endTime)));
            ps.setString(11, trigger.getCalendarName());
            ps.setInt(12, trigger.getMisfireInstruction());
            ps.setBinaryStream(13, bais, len);
            ps.setString(14, trigger.getName());
            ps.setString(15, trigger.getGroup());

            insertResult = ps.executeUpdate();
        } finally {
            if (null != ps) {
                try {
                    ps.close();
                } catch (SQLException ignore) {
                }
            }
        }

        if (insertResult > 0) {
            deleteTriggerListeners(conn, trigger.getName(), trigger.getGroup());

            String[] trigListeners = trigger.getTriggerListenerNames();
            for (int i = 0; trigListeners != null && i < trigListeners.length; i++)
                insertTriggerListener(conn, trigger, trigListeners[i]);
        }

        return insertResult;
    }

    /**
     * <p>
     * Update the job data map for the given job.
     * </p>
     * 
     * @param conn
     *          the DB Connection
     * @param job
     *          the job to update
     * @return the number of rows updated
     */
    public int updateJobData(Connection conn, JobDetail job)
            throws IOException, SQLException {
        //log.debug( "Updating Job Data for Job " + job );
        ByteArrayOutputStream baos = serializeJobData(job.getJobDataMap());
        int len = baos.toByteArray().length;
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        PreparedStatement ps = null;

        try {
            ps = conn.prepareStatement(rtp(UPDATE_JOB_DATA));
            ps.setBinaryStream(1, bais, len);
            ps.setString(2, job.getName());
            ps.setString(3, job.getGroup());

            return ps.executeUpdate();
        } finally {
            if (null != ps) {
                try {
                    ps.close();
                } catch (SQLException ignore) {
                }
            }
        }
    }

    //---------------------------------------------------------------------------
    // triggers
    //---------------------------------------------------------------------------

    //---------------------------------------------------------------------------
    // calendars
    //---------------------------------------------------------------------------

    /**
     * <p>
     * Insert a new calendar.
     * </p>
     * 
     * @param conn
     *          the DB Connection
     * @param calendarName
     *          the name for the new calendar
     * @param calendar
     *          the calendar
     * @return the number of rows inserted
     * @throws IOException
     *           if there were problems serializing the calendar
     */
    public int insertCalendar(Connection conn, String calendarName,
            Calendar calendar) throws IOException, SQLException {
        //log.debug( "Inserting Calendar " + calendarName + " : " + calendar
        // );
        ByteArrayOutputStream baos = serializeObject(calendar);
        byte buf[] = baos.toByteArray();
        ByteArrayInputStream bais = new ByteArrayInputStream(buf);

        PreparedStatement ps = null;

        try {
            ps = conn.prepareStatement(rtp(INSERT_CALENDAR));
            ps.setString(1, calendarName);
            ps.setBinaryStream(2, bais, buf.length);

            return ps.executeUpdate();
        } finally {
            if (null != ps) {
                try {
                    ps.close();
                } catch (SQLException ignore) {
                }
            }
        }
    }

    /**
     * <p>
     * Update a calendar.
     * </p>
     * 
     * @param conn
     *          the DB Connection
     * @param calendarName
     *          the name for the new calendar
     * @param calendar
     *          the calendar
     * @return the number of rows updated
     * @throws IOException
     *           if there were problems serializing the calendar
     */
    public int updateCalendar(Connection conn, String calendarName,
            Calendar calendar) throws IOException, SQLException {
        //log.debug( "Updating calendar " + calendarName + " : " + calendar );
        ByteArrayOutputStream baos = serializeObject(calendar);
        byte buf[] = baos.toByteArray();
        ByteArrayInputStream bais = new ByteArrayInputStream(buf);

        PreparedStatement ps = null;

        try {
            ps = conn.prepareStatement(rtp(UPDATE_CALENDAR));
            ps.setBinaryStream(1, bais, buf.length);
            ps.setString(2, calendarName);

            return ps.executeUpdate();
        } finally {
            if (null != ps) {
                try {
                    ps.close();
                } catch (SQLException ignore) {
                }
            }
        }
    }

    //---------------------------------------------------------------------------
    // protected methods that can be overridden by subclasses
    //---------------------------------------------------------------------------

    /**
     * <p>
     * This method should be overridden by any delegate subclasses that need
     * special handling for BLOBs. The default implementation uses standard
     * JDBC <code>java.sql.Blob</code> operations.
     * </p>
     * 
     * @param rs
     *          the result set, already queued to the correct row
     * @param colName
     *          the column name for the BLOB
     * @return the deserialized Object from the ResultSet BLOB
     * @throws ClassNotFoundException
     *           if a class found during deserialization cannot be found
     * @throws IOException
     *           if deserialization causes an error
     */
    protected Object getObjectFromBlob(ResultSet rs, String colName)
            throws ClassNotFoundException, IOException, SQLException {
        //log.debug( "Getting blob from column: " + colName );
        Object obj = null;

        byte binaryData[] = rs.getBytes(colName);

        InputStream binaryInput = new ByteArrayInputStream(binaryData);

        if (null != binaryInput) {
            ObjectInputStream in = new ObjectInputStream(binaryInput);
            obj = in.readObject();
            in.close();
        }

        return obj;
    }

    /**
     * <p>
     * This method should be overridden by any delegate subclasses that need
     * special handling for BLOBs for job details. The default implementation
     * uses standard JDBC <code>java.sql.Blob</code> operations.
     * </p>
     * 
     * @param rs
     *          the result set, already queued to the correct row
     * @param colName
     *          the column name for the BLOB
     * @return the deserialized Object from the ResultSet BLOB
     * @throws ClassNotFoundException
     *           if a class found during deserialization cannot be found
     * @throws IOException
     *           if deserialization causes an error
     */
    protected Object getJobDetailFromBlob(ResultSet rs, String colName)
            throws ClassNotFoundException, IOException, SQLException {
        //log.debug( "Getting Job details from blob in col " + colName );
        if (canUseProperties()) {
            byte data[] = rs.getBytes(colName);
            if(data == null)
                return null;
            InputStream binaryInput = new ByteArrayInputStream(data);
            return binaryInput;
        }

        return getObjectFromBlob(rs, colName);
    }
}

// EOF
