/* Copyright 2004 Tacit Knowledge
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tacitknowledge.util.migration.jdbc;

import com.tacitknowledge.util.migration.MigrationException;
import com.tacitknowledge.util.migration.PatchInfoStore;
import com.tacitknowledge.util.migration.jdbc.util.SqlUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;


/**
 * Manages interactions with the "patches" table.  The patches table stores
 * the current patch level for a given system, as well as a system-scoped lock
 * use to avoid concurrent patches to the system.  A system is defined as an
 * exclusive target of a patch.
 * <p/>
 * This class is responsible for:
 * <ul>
 * <li>Validating the existence of the patches table and creating it if it
 * doesn't exist</li>
 * <li>Determining if a patch is currently running on a given system</li>
 * <li>Obtaining and releasing patch locks for a given system</li>
 * <li>Obtaining and incrementing the patch level for a given system</li>
 * </ul>
 * <p/>
 * <strong>TRANSACTIONS:</strong> Transactions should be committed by the calling
 * class as needed.  This class does not explictly commit or rollback transactions.
 *
 * @author Scott Askew (scott@tacitknowledge.com)
 */
public class PatchTable implements PatchInfoStore
{
    /**
     * Class logger
     */
    private static Log log = LogFactory.getLog(PatchTable.class);

    /**
     * The migration configuration
     */
    private JdbcMigrationContext context = null;

    /**
     * Keeps track of table validation (see #createPatchesTableIfNeeded)
     */
    private boolean tableExistenceValidated = false;

    /**
     * Create a new <code>PatchTable</code>.
     *
     * @param migrationContext the migration configuration and connection source
     */
    public PatchTable(JdbcMigrationContext migrationContext)
    {
        this.context = migrationContext;

        if (context.getDatabaseType() == null)
        {
            throw new IllegalArgumentException("The JDBC database type is required");
        }
    }

    /**
     * {@inheritDoc}
     */
    public void createPatchStoreIfNeeded() throws MigrationException
    {
        if (tableExistenceValidated)
        {
            return;
        }

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try
        {
            conn = context.getConnection();

            stmt = conn.prepareStatement(getSql("level.table.exists"));
            stmt.setString(1, context.getSystemName());
            rs = stmt.executeQuery();
            log.debug("'patches' table already exists.");
            tableExistenceValidated = true;
        }
        catch (SQLException e)
        {
            // logging error in case it's not a simple patch table doesn't exist error
            log.debug(e.getMessage());
            SqlUtil.close(null, stmt, rs);

            // check connection is valid before using, because the getConnection() call
            // could have thrown the SQLException
            if (null == conn)
            {
                throw new MigrationException("Unable to create a connection.", e);
            }

            log.info("'patches' table must not exist; creating....");
            try
            {
                stmt = conn.prepareStatement(getSql("patches.create"));
                if (log.isDebugEnabled())
                {
                    log.debug("Creating patches table with SQL '" + getSql("patches.create") + "'");
                }
                stmt.execute();
                context.commit();

                // We don't yet have a patch record for this system; create one
                createSystemPatchRecord();
            }
            catch (SQLException sqle)
            {
                throw new MigrationException("Unable to create patch table", sqle);
            }
            tableExistenceValidated = true;
            log.info("Created 'patches' table.");
        }
        catch (Exception ex)
        {
            throw new MigrationException("Unexpected exception while creating patch store.", ex);
        }
        finally
        {
            SqlUtil.close(conn, stmt, rs);
        }
    }

    /**
     * {@inheritDoc}
     */
    public int getPatchLevel() throws MigrationException
    {
        createPatchStoreIfNeeded();

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try
        {
            conn = context.getConnection();
            stmt = conn.prepareStatement(getSql("level.read"));
            stmt.setString(1, context.getSystemName());
            rs = stmt.executeQuery();
            if (rs.next())
            {
                return rs.getInt(1);
            }

            SqlUtil.close(conn, stmt, rs);
            conn = null;
            stmt = null;
            rs = null;

            return 0;
        }
        catch (SQLException e)
        {
            throw new MigrationException("Unable to get patch level", e);
        }
        finally
        {
            SqlUtil.close(conn, stmt, rs);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void updatePatchLevel(int level) throws MigrationException
    {
        // Make sure a patch record already exists for this system
        getPatchLevel();

        Connection conn = null;
        PreparedStatement stmt = null;
        try
        {
            conn = context.getConnection();
            stmt = conn.prepareStatement(getSql("level.update"));
            stmt.setInt(1, level);
            stmt.setString(2, context.getSystemName());
            stmt.execute();
            context.commit();
        }
        catch (SQLException e)
        {
            throw new MigrationException("Unable to update patch level", e);
        }
        finally
        {
            SqlUtil.close(conn, stmt, null);
        }
    }

    /**
     * {@inheritDoc}
     */
    public boolean isPatchStoreLocked() throws MigrationException
    {
        createPatchStoreIfNeeded();

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try
        {
            conn = context.getConnection();
            stmt = conn.prepareStatement(getSql("lock.read"));
            stmt.setString(1, context.getSystemName());
            stmt.setString(2, context.getSystemName());
            rs = stmt.executeQuery();

            if (rs.next())
            {
                return ("T".equals(rs.getString(1)));
            }
            else
            {
                return false;
            }
        }
        catch (SQLException e)
        {
            throw new MigrationException("Unable to determine if table is locked", e);
        }
        finally
        {
            SqlUtil.close(conn, stmt, rs);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void lockPatchStore() throws MigrationException, IllegalStateException
    {
        createPatchStoreIfNeeded();
        if (!updatePatchLock(true))
        {
            throw new IllegalStateException("Patch table is already locked!");
        }
    }

    /**
     * {@inheritDoc}
     */
    public void unlockPatchStore() throws MigrationException
    {
        updatePatchLock(false);
    }

    /**
     * {@inheritDoc}
     */
    public boolean isPatchApplied(int patchLevel) throws MigrationException
    {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try
        {
            conn = context.getConnection();
            stmt = conn.prepareStatement(getSql("level.exists"));
            stmt.setString(1, context.getSystemName());
            stmt.setString(2, String.valueOf(patchLevel));
            rs = stmt.executeQuery();
            if (rs.next())
            {
                return patchLevel == rs.getInt(1);
            }
            else
            {
                return false;
            }

        }
        catch (SQLException e)
        {
            throw new MigrationException("Unable to determine if patch has been applied", e);
        }
        finally
        {
            SqlUtil.close(conn, stmt, rs);
        }
    }

    public void updatePatchLevelAfterRollBack(int rollbackLevel) throws MigrationException
    {
        // Make sure a patch record already exists for this system
        getPatchLevel();

        Connection conn = null;
        PreparedStatement stmt = null;
        try
        {
            conn = context.getConnection();
            stmt = conn.prepareStatement(getSql("level.rollback"));
            stmt.setInt(1, rollbackLevel);
            stmt.setString(2, context.getSystemName());
            stmt.execute();
            context.commit();
        }
        catch (SQLException e)
        {
            throw new MigrationException("Unable to update patch level", e);
        }
        finally
        {
            SqlUtil.close(conn, stmt, null);
        }

    }

    /**
     * Returns the SQL to execute for the database type associated with this patch table.
     *
     * @param key the key within <code><i>database</i>.properties</code> whose
     *            SQL should be returned
     * @return the SQL to execute for the database type associated with this patch table
     */
    protected String getSql(String key)
    {
        return context.getDatabaseType().getProperty(key);
    }

    /**
     * Creates an initial record in the patches table for this system.
     *
     * @throws SQLException       if an unrecoverable database error occurs
     * @throws MigrationException if an unrecoverable database error occurs
     */
    private void createSystemPatchRecord() throws MigrationException, SQLException
    {
        String systemName = context.getSystemName();
        Connection conn = null;
        PreparedStatement stmt = null;
        try
        {
            conn = context.getConnection();
            stmt = conn.prepareStatement(getSql("level.create"));
            stmt.setString(1, systemName);
            stmt.execute();
            context.commit();
            log.info("Created patch record for " + systemName);
        }
        catch (SQLException e)
        {
            log.error("Error creating patch record for system '" + systemName + "'", e);
            throw e;
        }
        finally
        {
            SqlUtil.close(conn, stmt, null);
        }
    }

    /**
     * Obtains or releases a lock for this system in the patches table.  If the lock
     * was not obtained or released, then <code>false</code> is returned.
     *
     * @param  lock <code>true</code> if a lock is to be obtained, <code>false</code>
     *         if it is to be removed
     * @return true if the lock was updated successfully, otherwise false
     * @throws MigrationException if an unrecoverable database error occurs
     */
    private boolean updatePatchLock(boolean lock) throws MigrationException
    {
        String sqlkey = (lock) ? "lock.obtain" : "lock.release";
        Connection conn = null;
        PreparedStatement stmt = null;

        try
        {
            conn = context.getConnection();
            stmt = conn.prepareStatement(getSql(sqlkey));
            if (log.isDebugEnabled())
            {
                log.debug("Updating patch table lock: " + getSql(sqlkey));
            }
            stmt.setString(1, context.getSystemName());
            if (lock)
            {
                stmt.setString(2, context.getSystemName());
            }

            int rowsUpdated = stmt.executeUpdate();
            boolean lockUpdated = (rowsUpdated == 1);
            context.commit();
            if (log.isDebugEnabled())
            {
                log.debug(((lock) ? "Obtained" : "Released") + " lock? " + lockUpdated);
            }
            return lockUpdated;
        }
        catch (SQLException e)
        {
            throw new MigrationException("Unable to update patch lock to " + lock, e);
        }
        finally
        {
            SqlUtil.close(conn, stmt, null);
        }
    }

    public Set<Integer> getPatchesApplied() throws MigrationException
    {
        createPatchStoreIfNeeded();

        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet resultSet = null;
        Set<Integer> patches = new HashSet<Integer>();
        try
        {
            connection = context.getConnection();
            stmt = connection.prepareStatement(getSql("patches.all"));
            stmt.setString(1, context.getSystemName());
            resultSet = stmt.executeQuery();
            while (resultSet.next())
            {
                patches.add(resultSet.getInt("patch_level"));
            }


        }
        catch (SQLException e)
        {
            throw new MigrationException("Unable to get patch levels", e);
        }
        finally
        {
            SqlUtil.close(connection, stmt, resultSet);
        }
        return patches;
    }
}
