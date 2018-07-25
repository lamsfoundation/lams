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

package com.tacitknowledge.util.migration;

import java.sql.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.tacitknowledge.util.migration.jdbc.MigrationTableUnlock;
import com.tacitknowledge.util.migration.jdbc.util.SqlUtil;

/**
 * Test AutoPatch MultiNode functionality
 * 
 * @author  Mike Hardy (mike@tacitknowledge.com)
 */
public class MigrationUnlockTest extends AutoPatchIntegrationTestBase
{
    /** Class logger */
    private static Log log = LogFactory.getLog(MigrationUnlockTest.class);
    
    /**
     * Constructor 
     * 
     * @param name the name of the test to run
     */
    public MigrationUnlockTest(String name)
    {
        super(name);
    }
    
    /**
     * Test that all the tables were created successfully in all of the databases
     * 
     * @exception Exception if anything goes wrong
     */
    public void testMigrationUnlock() throws Exception
    {
        log.debug("Testing forcible migration table unlocking");
        try
        {
            getLauncher().doMigrations();
        }
        catch (Exception e)
        {
            log.error("Unexpected error", e);
            fail("shouldn't have thrown any exceptions");
        }
        
        // now we should have a patch table. Let's lock it.
        lockPatchTable("orders");
        
        // now lets try to unlock it
        MigrationTableUnlock unlocker = new MigrationTableUnlock();
        unlocker.tableUnlock("orders");
        
        // Now let's see if it worked
        verifyPatchTableNotLocked("orders");
    }
    
    /**
     * Lock the patch table for a database
     * 
     * @param database the database name to lock
     * @exception Exception if getting the patch level fails
     */
    private void lockPatchTable(String database) throws Exception
    {
        Connection conn = DriverManager.getConnection("jdbc:hsqldb:mem:" + database, "sa", "");

        PreparedStatement stmt = conn.prepareStatement("UPDATE patches SET patch_in_progress = 'T' WHERE patch_level in ( SELECT MAX(patch_level) FROM patches WHERE system_name = ?)");
        stmt.setString(1, database);
        int rowCount = stmt.executeUpdate();
        assertEquals(1, rowCount);
        SqlUtil.close(conn, stmt, null);
    }
    
    /**
     * Verify that there is no lock in the patch table for a database
     * 
     * @param database the database name to verify
     * @exception Exception if getting the patch level fails
     */
    private void verifyPatchTableNotLocked(String database) throws Exception
    {
        Connection conn = DriverManager.getConnection("jdbc:hsqldb:mem:" + database, "sa", "");
        PreparedStatement stmt = conn.prepareStatement("SELECT patch_in_progress FROM patches WHERE system_name = ? AND ( patch_in_progress <> 'F' OR patch_level in ( SELECT MAX(patch_level) FROM patches WHERE system_name = ? ))");
        stmt.setString(1, database);
        stmt.setString(2, database);
        ResultSet rs = stmt.executeQuery();
        rs.next();
        assertEquals("F", rs.getString("patch_in_progress"));
        SqlUtil.close(conn, stmt, rs);
    }
}
