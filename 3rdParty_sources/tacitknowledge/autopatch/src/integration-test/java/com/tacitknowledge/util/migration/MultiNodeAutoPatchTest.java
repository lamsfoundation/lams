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

import com.tacitknowledge.util.migration.jdbc.DistributedJdbcMigrationLauncher;
import com.tacitknowledge.util.migration.jdbc.DistributedJdbcMigrationLauncherFactory;
import com.tacitknowledge.util.migration.jdbc.util.SqlUtil;

/**
 * Test AutoPatch MultiNode functionality
 * 
 * @author  Mike Hardy (mike@tacitknowledge.com)
 */
public class MultiNodeAutoPatchTest extends AutoPatchIntegrationTestBase
{
    /** Class logger */
    private static Log log = LogFactory.getLog(MultiNodeAutoPatchTest.class);
    private static final String CATALOG = "catalog";
    private static final String ORDERS = "orders";
    private static final String CORE = "core";

    /**
     * Constructor 
     * 
     * @param name the name of the test to run
     */
    public MultiNodeAutoPatchTest(String name)
    {
        super(name);
    }
    /**
     * Test that all the tables were created successfully in all of the databases
     * 
     * @exception Exception if anything goes wrong
     */
    public void testMultiNodePatch() throws Exception
    {
        log.debug("Testing multi node patching");
        try
        {
            getDistributedLauncher().doMigrations();
        }
        catch (Exception e)
        {
            log.error("Unexpected error", e);
            fail("shouldn't have thrown any exceptions");
        }
        
        // Make sure everything worked out okay
       Connection core = DriverManager.getConnection("jdbc:hsqldb:mem:core", "sa", "");
       Connection orders = getOrderConnection();
       Connection catalog1 = DriverManager.getConnection("jdbc:hsqldb:mem:catalog1", "sa", "");
       Connection catalog2 = DriverManager.getConnection("jdbc:hsqldb:mem:catalog2", "sa", "");
       Connection catalog3 = DriverManager.getConnection("jdbc:hsqldb:mem:catalog3", "sa", "");
       
       // 4 patches should have executed
       assertEquals(4, getPatchLevel(core, CORE));
       assertEquals(4, getPatchLevel(orders, ORDERS));
       assertEquals(4, getPatchLevel(catalog1, CATALOG));
       assertEquals(4, getPatchLevel(catalog2, CATALOG));
       assertEquals(4, getPatchLevel(catalog3, CATALOG));
       
       
       // we should have test values in each table
       verifyTestTable(core, "core_table_1");
       verifyTestTable(orders, "order_table_1");
       verifyTestTable(orders, "order_table_2");
       verifyTestTable(catalog1, "catalog_table_1");
       verifyTestTable(catalog2, "catalog_table_1");
       verifyTestTable(catalog3, "catalog_table_1");
       
       SqlUtil.close(core, null, null);
       SqlUtil.close(orders, null, null);
       SqlUtil.close(catalog1, null, null);
       SqlUtil.close(catalog2, null, null);
       SqlUtil.close(catalog3, null, null);
    }

    /**
     * Test that all the tables were created successfully in all of the databases and then test 
     * the rollback of the final task.
     * 
     * @exception Exception if anything goes wrong
     */
    public void testMultiNodeRollback() throws Exception
    {
        log.debug("Testing multi node rollback");
        try
        {
            getDistributedLauncher().doMigrations();
        }
        catch (Exception e)
        {
            log.error("Unexpected error", e);
            fail("shouldn't have thrown any exceptions");
        }
        
        // Make sure everything worked out okay
       Connection core = DriverManager.getConnection("jdbc:hsqldb:mem:core", "sa", "");
       Connection orders = getOrderConnection();
       Connection catalog1 = DriverManager.getConnection("jdbc:hsqldb:mem:catalog1", "sa", "");
       Connection catalog2 = DriverManager.getConnection("jdbc:hsqldb:mem:catalog2", "sa", "");
       Connection catalog3 = DriverManager.getConnection("jdbc:hsqldb:mem:catalog3", "sa", "");
       
       // 4 patches should have executed
       assertEquals(4, getPatchLevel(core, CORE));
       assertEquals(4, getPatchLevel(orders, ORDERS));
       assertEquals(4, getPatchLevel(catalog1, CATALOG));
       assertEquals(4, getPatchLevel(catalog2, CATALOG));
       assertEquals(4, getPatchLevel(catalog3, CATALOG));
       
       
       // we should have test values in each table
       verifyTestTable(core, "core_table_1");
       verifyTestTable(orders, "order_table_1");
       verifyTestTable(orders, "order_table_2");
       verifyTestTable(catalog1, "catalog_table_1");
       verifyTestTable(catalog2, "catalog_table_1");
       verifyTestTable(catalog3, "catalog_table_1");
       
       try 
       {
    	   getDistributedLauncher().doRollbacks(3);
       } catch(Exception e)
       {
    	   log.error("Unexpected error", e);
    	   fail("should not have received any exceptions");
       }
       
       // 4 patches should have executed
       assertEquals(3, getPatchLevel(core, CORE));
       assertEquals(3, getPatchLevel(orders, ORDERS));
       assertEquals(3, getPatchLevel(catalog1, CATALOG));
       assertEquals(3, getPatchLevel(catalog2, CATALOG));
       assertEquals(3, getPatchLevel(catalog3, CATALOG));
       
       SqlUtil.close(core, null, null);
       SqlUtil.close(orders, null, null);
       SqlUtil.close(catalog1, null, null);
       SqlUtil.close(catalog2, null, null);
       SqlUtil.close(catalog3, null, null);
    }
   
    /**
     * Tests that a when there is a node that is out of sync with the patch level, autopatch
     * detects the node and fails with an error reporting the problem.
     * @throws Exception
     */
    public void testMultiNodePatchDetectsOutOfSyncNode() throws Exception
    {
        // run the base multinode patch to bring the databases up to date
        getDistributedLauncher().doMigrations();

        Connection core = DriverManager.getConnection("jdbc:hsqldb:mem:core", "sa", "");
        Connection orders = getOrderConnection();
        Connection catalog1 = DriverManager.getConnection("jdbc:hsqldb:mem:catalog1", "sa", "");
        Connection catalog2 = DriverManager.getConnection("jdbc:hsqldb:mem:catalog2", "sa", "");
        Connection catalog3 = DriverManager.getConnection("jdbc:hsqldb:mem:catalog3", "sa", "");
        Connection catalog4 = DriverManager.getConnection("jdbc:hsqldb:mem:catalog4", "sa", "");

        // make sure databases are in the state we expect
        assertEquals(4, getPatchLevel(core, CORE));
        assertEquals(4, getPatchLevel(orders, ORDERS));
        assertEquals(4, getPatchLevel(catalog1, CATALOG));
        assertEquals(4, getPatchLevel(catalog2, CATALOG));
        assertEquals(4, getPatchLevel(catalog3, CATALOG));
        try
        {
            getPatchLevel(catalog4, CATALOG);
            fail("patches table should not exist at this point");
        } 
        catch (Exception e)
        {
            // expected
        }

        // create a new migration launcher with a migration.properties that
        // specifies a new node
        DistributedJdbcMigrationLauncherFactory dlFactory = new DistributedJdbcMigrationLauncherFactory();
        DistributedJdbcMigrationLauncher nodeAddedDistributedLauncher = 
            (DistributedJdbcMigrationLauncher) 
              dlFactory.createMigrationLauncher("integration_test", 
                                                "node-added-inttest-migration.properties");
        
        // run the migrations and expect a MigrationException to be raised.
        try
        {
            nodeAddedDistributedLauncher.doMigrations();
            
            fail("should have thrown an exception");
        }
        catch (MigrationException e)
        {
            // expected
        }
        
        // no new patches should have executed
        assertEquals(4, getPatchLevel(core, CORE));
        assertEquals(4, getPatchLevel(orders, ORDERS));
        assertEquals(4, getPatchLevel(catalog1, CATALOG));
        assertEquals(4, getPatchLevel(catalog2, CATALOG));
        assertEquals(4, getPatchLevel(catalog3, CATALOG));
        assertEquals(0, getPatchLevel(catalog4, CATALOG));
        
        SqlUtil.close(core, null, null);
        SqlUtil.close(orders, null, null);
        SqlUtil.close(catalog1, null, null);
        SqlUtil.close(catalog2, null, null);
        SqlUtil.close(catalog3, null, null);
        SqlUtil.close(catalog4, null, null);

    }
    
    /**
     * Tests that when there is a node that is out of sync with the system's patch level,
     * autopatch will detect and patch the node if the flag to force syncing of nodes is set.
     */
    public void testMultiNodePatchesOutOfSyncNode() throws Exception
    {
        // run the base multinode patch to bring the databases up to date
        testMultiNodePatch();
        
        // create a new migration launcher with a migration.properties that
        // specifies a new node
        DistributedJdbcMigrationLauncherFactory dlFactory =
                new DistributedJdbcMigrationLauncherFactory();
        DistributedJdbcMigrationLauncher nodeAddedDistributedLauncher = 
            (DistributedJdbcMigrationLauncher) 
              dlFactory.createMigrationLauncher("integration_test", 
                                                "node-added-inttest-migration.properties");
        DistributedMigrationProcess migrationProcess =
                (DistributedMigrationProcess) nodeAddedDistributedLauncher.getMigrationProcess();
        migrationProcess.setForceSync(true);

        // run the migrations and no MigrationException should be raised.
        nodeAddedDistributedLauncher.doMigrations();
        
        // Make sure everything worked out okay
        Connection catalog4 = DriverManager.getConnection("jdbc:hsqldb:mem:catalog4", "sa", "");
        Connection catalog5 = DriverManager.getConnection("jdbc:hsqldb:mem:catalog5", "sa", "");
        
        // 4 patches should have executed
        assertEquals(4, getPatchLevel(catalog4, CATALOG));
        assertEquals(4, getPatchLevel(catalog5, CATALOG));
        
        
        // we should have test values in each table
        verifyTestTable(catalog4, "catalog_table_1");
        verifyTestTable(catalog5, "catalog_table_1");
        
        SqlUtil.close(catalog4, null, null);
        SqlUtil.close(catalog5, null, null);
    }
    

    /**
     * Verify that a given table exists and that it contains one row
     * with a value equal to the name of the table (this matches the inttest patches)
     * 
     * @param conn the Connection to use
     * @param tableName the table name (and row value) to look for
     * @exception Exception if anything goes wrong
     */
    private void verifyTestTable(Connection conn, String tableName) throws Exception
    {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT value FROM " + tableName);
        rs.next();
        assertEquals(tableName, rs.getString("value"));
    }
}
