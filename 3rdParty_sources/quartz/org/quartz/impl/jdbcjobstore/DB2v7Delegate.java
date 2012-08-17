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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import org.quartz.impl.jdbcjobstore.StdJDBCDelegate;
import org.apache.commons.logging.Log;
import org.quartz.Calendar;
import org.quartz.CronTrigger;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.utils.Key;

/**
 * Quartz JDBC delegate for DB2 v7 databases.
 * 
 * @author Blair Jensen
 */
public class DB2v7Delegate extends StdJDBCDelegate {

    public DB2v7Delegate(Log logger, String tablePrefix, String instanceId) {
        super(logger, tablePrefix, instanceId);
    }

    public DB2v7Delegate(Log log, String tablePrefix, String instanceId,
            Boolean useProperties) {
        super(log, tablePrefix, instanceId, useProperties);
    }
    
    public Trigger[] selectTriggersForRecoveringJobs(Connection conn)
            throws SQLException, IOException, ClassNotFoundException {
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = conn
                    .prepareStatement(rtp(SELECT_INSTANCES_RECOVERABLE_FIRED_TRIGGERS));
            ps.setString(1, instanceId);
            ps.setString(2, "1");
            //ps.setBoolean(2, true);
            rs = ps.executeQuery();

            long dumId = System.currentTimeMillis();
            ArrayList list = new ArrayList();
            while (rs.next()) {
                String jobName = rs.getString(COL_JOB_NAME);
                String jobGroup = rs.getString(COL_JOB_GROUP);
                String trigName = rs.getString(COL_TRIGGER_NAME);
                String trigGroup = rs.getString(COL_TRIGGER_GROUP);
                long firedTime = rs.getLong(COL_FIRED_TIME);
                SimpleTrigger rcvryTrig = new SimpleTrigger("recover_"
                        + instanceId + "_" + String.valueOf(dumId++),
                        Scheduler.DEFAULT_RECOVERY_GROUP, new Date(firedTime));
                rcvryTrig.setJobName(jobName);
                rcvryTrig.setJobGroup(jobGroup);
                rcvryTrig
                        .setMisfireInstruction(SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);

                JobDataMap jd = selectTriggerJobDataMap(conn, trigName, trigGroup);
                jd.put("QRTZ_FAILED_JOB_ORIG_TRIGGER_NAME", trigName);
                jd.put("QRTZ_FAILED_JOB_ORIG_TRIGGER_GROUP", trigGroup);
                jd.put("QRTZ_FAILED_JOB_ORIG_TRIGGER_FIRETIME_IN_MILLISECONDS_AS_STRING", String.valueOf(firedTime));
                rcvryTrig.setJobDataMap(jd);
                
                list.add(rcvryTrig);
            }
            Object[] oArr = list.toArray();
            Trigger[] tArr = new Trigger[oArr.length];
            System.arraycopy(oArr, 0, tArr, 0, oArr.length);
            return tArr;
        } finally {
            if (null != rs) {
                try {
                    rs.close();
                } catch (SQLException ignore) {
                }
            }
            if (null != ps) {
                try {
                    ps.close();
                } catch (SQLException ignore) {
                }
            }
        }
    }

    public int insertJobDetail(Connection conn, JobDetail job)
            throws IOException, SQLException {
        ByteArrayOutputStream baos = serializeJobData(job.getJobDataMap());

        PreparedStatement ps = null;

        int insertResult = 0;

        try {

            ps = conn.prepareStatement(rtp(INSERT_JOB_DETAIL));
            ps.setString(1, job.getName());
            ps.setString(2, job.getGroup());
            ps.setString(3, job.getDescription());
            ps.setString(4, job.getJobClass().getName());
            ps.setString(5, toBooleanIntString(job.isDurable()));
            ps.setString(6, toBooleanIntString(job.isVolatile()));
            ps.setString(7, toBooleanIntString(job.isStateful()));
            ps.setString(8, toBooleanIntString(job.requestsRecovery()));
            //ps.setBoolean (5, job.isDurable());
            //ps.setBoolean (6, job.isVolatile());
            //ps.setBoolean (7, job.isStateful());
            //ps.setBoolean (8, job.requestsRecovery());
            ps.setObject(9, baos.toByteArray(), java.sql.Types.BLOB);
            //ps.setBytes (9, baos.toByteArray());

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

    public int updateJobDetail(Connection conn, JobDetail job)
            throws IOException, SQLException {
        ByteArrayOutputStream baos = serializeJobData(job.getJobDataMap());

        PreparedStatement ps = null;

        int insertResult = 0;

        try {
            ps = conn.prepareStatement(rtp(UPDATE_JOB_DETAIL));
            ps.setString(1, job.getDescription());
            ps.setString(2, job.getJobClass().getName());
            ps.setString(3, toBooleanIntString(job.isDurable()));
            ps.setString(4, toBooleanIntString(job.isVolatile()));
            ps.setString(5, toBooleanIntString(job.isStateful()));
            ps.setString(6, toBooleanIntString(job.requestsRecovery()));
            //ps.setBoolean (3, job.isDurable());
            //ps.setBoolean (4, job.isVolatile());
            //ps.setBoolean (5, job.isStateful());
            //ps.setBoolean (6, job.requestsRecovery());
            ps.setObject(7, baos.toByteArray(), java.sql.Types.BLOB);
            //ps.setBytes (7, baos.toByteArray());
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

        PreparedStatement ps = null;

        int insertResult = 0;

        try {
            ps = conn.prepareStatement(rtp(INSERT_TRIGGER));
            ps.setString(1, trigger.getName());
            ps.setString(2, trigger.getGroup());
            ps.setString(3, trigger.getJobName());
            ps.setString(4, trigger.getJobGroup());
            ps.setString(5, toBooleanIntString(trigger.isVolatile()));
            //ps.setBoolean(5, trigger.isVolatile());
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
            ps.setObject(15, baos.toByteArray(), java.sql.Types.BLOB);

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

        PreparedStatement ps = null;

        int insertResult = 0;

        try {
            ps = conn.prepareStatement(rtp(UPDATE_TRIGGER));
            ps.setString(1, trigger.getJobName());
            ps.setString(2, trigger.getJobGroup());
            ps.setString(3, toBooleanIntString(trigger.isVolatile()));
            //ps.setBoolean(3, trigger.isVolatile());
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
                //					  updateSimpleTrigger(conn, (SimpleTrigger)trigger);
                ps.setString(8, TTYPE_SIMPLE);
            } else if (trigger instanceof CronTrigger) {
                //					  updateCronTrigger(conn, (CronTrigger)trigger);
                ps.setString(8, TTYPE_CRON);
            } else {
                //					  updateBlobTrigger(conn, trigger);
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
            ps.setObject(13, baos.toByteArray(), java.sql.Types.BLOB);
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

    public int insertFiredTrigger(Connection conn, Trigger trigger,
            String state, JobDetail job) throws SQLException {
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(rtp(INSERT_FIRED_TRIGGER));
            ps.setString(1, trigger.getFireInstanceId());
            ps.setString(2, trigger.getName());
            ps.setString(3, trigger.getGroup());
            ps.setString(4, toBooleanIntString(trigger.isVolatile()));
            //ps.setBoolean(4, trigger.isVolatile());
            ps.setString(5, instanceId);
            ps.setBigDecimal(6, new BigDecimal(String.valueOf(trigger
                    .getNextFireTime().getTime())));
            ps.setString(7, state);
            if (job != null) {
                ps.setString(8, trigger.getJobName());
                ps.setString(9, trigger.getJobGroup());
                ps.setString(10, toBooleanIntString(job.isStateful()));
                ps.setString(11, toBooleanIntString(job.requestsRecovery()));
                //ps.setBoolean(10, job.isStateful());
                //ps.setBoolean(11, job.requestsRecovery());
            } else {
                ps.setString(8, null);
                ps.setString(9, null);
                ps.setString(10, "0");
                ps.setString(11, "0");
                //ps.setBoolean(10, false);
                //ps.setBoolean(11, false);
            }

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

    public int updateJobData(Connection conn, JobDetail job)
            throws IOException, SQLException {
        ByteArrayOutputStream baos = serializeJobData(job.getJobDataMap());

        PreparedStatement ps = null;

        try {
            ps = conn.prepareStatement(rtp(UPDATE_JOB_DATA));
            ps.setObject(1, baos.toByteArray(), java.sql.Types.BLOB);
            //ps.setBytes(1, baos.toByteArray());
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

    public int insertCalendar(Connection conn, String calendarName,
            Calendar calendar) throws IOException, SQLException {
        ByteArrayOutputStream baos = serializeObject(calendar);

        PreparedStatement ps = null;

        try {
            ps = conn.prepareStatement(rtp(INSERT_CALENDAR));
            ps.setString(1, calendarName);
            ps.setObject(2, baos.toByteArray(), java.sql.Types.BLOB);
            //ps.setBytes(2, baos.toByteArray());

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

    public int updateCalendar(Connection conn, String calendarName,
            Calendar calendar) throws IOException, SQLException {
        ByteArrayOutputStream baos = serializeObject(calendar);

        PreparedStatement ps = null;

        try {
            ps = conn.prepareStatement(rtp(UPDATE_CALENDAR));
            ps.setString(1, calendarName);
            ps.setObject(2, baos.toByteArray(), java.sql.Types.BLOB);
            //ps.setBytes(2, baos.toByteArray());

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

    public int deleteVolatileFiredTriggers(Connection conn) throws SQLException {
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(rtp(DELETE_VOLATILE_FIRED_TRIGGERS));
            ps.setString(1, "1");
            //ps.setBoolean(1, true);

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

    public Key[] selectVolatileTriggers(Connection conn) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = conn.prepareStatement(rtp(SELECT_VOLATILE_TRIGGERS));
            ps.setString(1, "1");
            //ps.setBoolean(1, true);
            rs = ps.executeQuery();

            ArrayList list = new ArrayList();
            while (rs.next()) {
                String triggerName = rs.getString(COL_TRIGGER_NAME);
                String groupName = rs.getString(COL_TRIGGER_GROUP);
                list.add(new Key(triggerName, groupName));
            }
            Object[] oArr = list.toArray();
            Key[] kArr = new Key[oArr.length];
            System.arraycopy(oArr, 0, kArr, 0, oArr.length);
            return kArr;
        } finally {
            if (null != rs) {
                try {
                    rs.close();
                } catch (SQLException ignore) {
                }
            }
            if (null != ps) {
                try {
                    ps.close();
                } catch (SQLException ignore) {
                }
            }
        }
    }

    public Key[] selectVolatileJobs(Connection conn) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = conn.prepareStatement(rtp(SELECT_VOLATILE_JOBS));
            ps.setString(1, "1");
            //ps.setBoolean(1, true);
            rs = ps.executeQuery();

            ArrayList list = new ArrayList();
            while (rs.next()) {
                String triggerName = rs.getString(COL_JOB_NAME);
                String groupName = rs.getString(COL_JOB_GROUP);
                list.add(new Key(triggerName, groupName));
            }
            Object[] oArr = list.toArray();
            Key[] kArr = new Key[oArr.length];
            System.arraycopy(oArr, 0, kArr, 0, oArr.length);
            return kArr;
        } finally {
            if (null != rs) {
                try {
                    rs.close();
                } catch (SQLException ignore) {
                }
            }
            if (null != ps) {
                try {
                    ps.close();
                } catch (SQLException ignore) {
                }
            }
        }
    }

    private static String toBooleanIntString(boolean theBoolean) {
        if (String.valueOf(theBoolean).equals("true")) {
            return "1";
        } else {
            return "0";
        }
    }

}
