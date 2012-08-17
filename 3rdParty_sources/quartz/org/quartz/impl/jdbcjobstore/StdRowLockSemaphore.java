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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * An interface for providing thread/resource locking in order to protect
 * resources from being altered by multiple threads at the same time.
 * 
 * @author jhouse
 */
public class StdRowLockSemaphore implements Semaphore, Constants,
        StdJDBCConstants {

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * 
     * Constants.
     * 
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    public static final String SELECT_FOR_LOCK = "SELECT * FROM "
            + TABLE_PREFIX_SUBST + TABLE_LOCKS + " WHERE " + COL_LOCK_NAME
            + " = ? FOR UPDATE";

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * 
     * Data members.
     * 
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    ThreadLocal lockOwners = new ThreadLocal();

    //  java.util.HashMap threadLocksOb = new java.util.HashMap();
    private String selectWithLockSQL = SELECT_FOR_LOCK;

    private String tablePrefix;

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * 
     * Constructors.
     * 
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    public StdRowLockSemaphore(String tablePrefix, String selectWithLockSQL) {
        this.tablePrefix = tablePrefix;

        if (selectWithLockSQL != null && selectWithLockSQL.trim().length() != 0)
                this.selectWithLockSQL = selectWithLockSQL;

        this.selectWithLockSQL = Util.rtp(this.selectWithLockSQL, tablePrefix);
    }

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * 
     * Interface.
     * 
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    Log getLog() {
        return LogFactory.getLog(getClass());
        //return LogFactory.getLog("LOCK:"+Thread.currentThread().getName());
    }

    private HashSet getThreadLocks() {
        HashSet threadLocks = (HashSet) lockOwners.get();
        if (threadLocks == null) {
            threadLocks = new HashSet();
            lockOwners.set(threadLocks);
        }
        return threadLocks;
    }

    /**
     * Grants a lock on the identified resource to the calling thread (blocking
     * until it is available).
     * 
     * @return true if the lock was obtained.
     */
    public boolean obtainLock(Connection conn, String lockName)
            throws LockException {

        lockName = lockName.intern();

        Log log = getLog();
        
        if(log.isDebugEnabled())
            log.debug(
                "Lock '" + lockName + "' is desired by: "
                        + Thread.currentThread().getName());
        if (!isLockOwner(conn, lockName)) {

            PreparedStatement ps = null;
            ResultSet rs = null;
            try {
                ps = conn.prepareStatement(selectWithLockSQL);
                ps.setString(1, lockName);

                
                if(log.isDebugEnabled())
                    log.debug(
                        "Lock '" + lockName + "' is being obtained: "
                                + Thread.currentThread().getName());
                rs = ps.executeQuery();
                if (!rs.next())
                        throw new SQLException(Util.rtp(
                                "No row exists in table " + TABLE_PREFIX_SUBST
                                        + TABLE_LOCKS + " for lock named: "
                                        + lockName, tablePrefix));
            } catch (SQLException sqle) {
                //Exception src =
                // (Exception)getThreadLocksObtainer().get(lockName);
                //if(src != null)
                //  src.printStackTrace();
                //else
                //  System.err.println("--- ***************** NO OBTAINER!");

                if(log.isDebugEnabled())
                    log.debug(
                        "Lock '" + lockName + "' was not obtained by: "
                                + Thread.currentThread().getName());
                throw new LockException("Failure obtaining db row lock: "
                        + sqle.getMessage(), sqle);
            } finally {
                if (rs != null) try {
                    rs.close();
                } catch (Exception ignore) {
                }
                if (ps != null) try {
                    ps.close();
                } catch (Exception ignore) {
                }
            }
            if(log.isDebugEnabled())
                log.debug(
                    "Lock '" + lockName + "' given to: "
                            + Thread.currentThread().getName());
            getThreadLocks().add(lockName);
            //getThreadLocksObtainer().put(lockName, new
            // Exception("Obtainer..."));
        } else
            if(log.isDebugEnabled())
                log.debug(
                    "Lock '" + lockName + "' Is already owned by: "
                            + Thread.currentThread().getName());

        return true;
    }

    /**
     * Release the lock on the identified resource if it is held by the calling
     * thread.
     */
    public void releaseLock(Connection conn, String lockName) {

        lockName = lockName.intern();

        if (isLockOwner(conn, lockName)) {
            if(getLog().isDebugEnabled())
                getLog().debug(
                    "Lock '" + lockName + "' returned by: "
                            + Thread.currentThread().getName());
            getThreadLocks().remove(lockName);
            //getThreadLocksObtainer().remove(lockName);
        } else
            if(getLog().isDebugEnabled())
                getLog().warn(
                    "Lock '" + lockName + "' attempt to retun by: "
                            + Thread.currentThread().getName()
                            + " -- but not owner!",
                    new Exception("stack-trace of wrongful returner"));

    }

    /**
     * Determine whether the calling thread owns a lock on the identified
     * resource.
     */
    public boolean isLockOwner(Connection conn, String lockName) {

        lockName = lockName.intern();

        return getThreadLocks().contains(lockName);
    }

}
