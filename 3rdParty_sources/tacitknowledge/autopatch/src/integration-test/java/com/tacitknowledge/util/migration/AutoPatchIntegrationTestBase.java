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

import com.tacitknowledge.util.migration.jdbc.DistributedJdbcMigrationLauncher;
import com.tacitknowledge.util.migration.jdbc.DistributedJdbcMigrationLauncherFactory;
import com.tacitknowledge.util.migration.jdbc.JdbcMigrationLauncher;
import com.tacitknowledge.util.migration.jdbc.JdbcMigrationLauncherFactory;

import com.tacitknowledge.util.migration.jdbc.util.SqlUtil;
import junit.framework.TestCase;

/**
 * Superclass for other integration test cases. Sets up a test database etc.
 * 
 * @author  Mike Hardy (mike@tacitknowledge.com)
 */
public abstract class AutoPatchIntegrationTestBase extends TestCase
{
    /** The DistributedLauncher we're testing */
    private DistributedJdbcMigrationLauncher distributedLauncher = null;
    
    /** A regular launcher we can test */
    private JdbcMigrationLauncher launcher = null;
    
    /** A multi-node launcher we can test */
    private JdbcMigrationLauncher multiNodeLauncher = null;
    
    /**
     * Constructor 
     * 
     * @param name the name of the test to run
     */
    public AutoPatchIntegrationTestBase(String name)
    {
        super(name);
    }
    
    /**
     * Sets up a test database
     * 
     * @exception Exception if anything goes wrong
     */
    public void setUp() throws Exception
    {
        super.setUp();
        DistributedJdbcMigrationLauncherFactory dlFactory =
            new DistributedJdbcMigrationLauncherFactory();
        distributedLauncher = 
            (DistributedJdbcMigrationLauncher) 
              dlFactory.createMigrationLauncher("integration_test", 
                                                "inttest-migration.properties");
        
        JdbcMigrationLauncherFactory lFactory = new JdbcMigrationLauncherFactory();
        launcher = lFactory.createMigrationLauncher("orders", "inttest-migration.properties");
        multiNodeLauncher = lFactory.createMigrationLauncher("catalog", 
                                                             "inttest-migration.properties");
    }
    
    /**
     * Tears down the test database
     * 
     * @exception Exception if anything goes wrong
     */
    public void tearDown() throws Exception
    {
        super.tearDown();
        destroyDatabase("core");
        destroyDatabase("orders");
        destroyDatabase("catalog1");
        destroyDatabase("catalog2");
        destroyDatabase("catalog3");
        destroyDatabase("catalog4");
        destroyDatabase("catalog5");
        destroyDatabase("nodes");
        destroyDatabase("node1");
        destroyDatabase("node2");
        destroyDatabase("node3");
    }
    
    /**
     * Destroys a database so a future test can use it
     * 
     * @param database the name of the database to destroy
     * @exception Exception if anything goes wrong
     */
    protected void destroyDatabase(String database) throws Exception
    {
        Connection conn = DriverManager.getConnection("jdbc:hsqldb:mem:" + database, "sa", "");
        Statement stmt = conn.createStatement();
        stmt.execute("SHUTDOWN");
    }

    /**
     * Get the DistributedLauncher under test
     * 
     * @return DistributedJdbcMigrationLauncher
     */
    public DistributedJdbcMigrationLauncher getDistributedLauncher()
    {
        return distributedLauncher;
    }

    /**
     * Set the DistributedJdbcMigrationLauncher to test
     * 
     * @param distributedLauncher the launcher to test for distributed functionality
     */
    public void setDistributedLauncher(DistributedJdbcMigrationLauncher distributedLauncher)
    {
        this.distributedLauncher = distributedLauncher;
    }

    /**
     * Get the JdbcMigrationLauncher to test
     * 
     * @return JdbcMigrationLauncher to test
     */
    public JdbcMigrationLauncher getLauncher()
    {
        return launcher;
    }

    /**
     * Set the JdbcMigrationLauncher to test
     * 
     * @param launcher the launcher to test for core functionality
     */
    public void setLauncher(JdbcMigrationLauncher launcher)
    {
        this.launcher = launcher;
    }

    /**
     * Get the JdbcMigrationLauncher to test for multi-node functionality
     * 
     * @return JdbcMigrationLauncher configured for multi-node
     */
    public JdbcMigrationLauncher getMultiNodeLauncher()
    {
        return multiNodeLauncher;
    }

    /**
     * Set the JdbcMigrationLauncher to use for multi-node functional testing
     * 
     * @param multiNodeLauncher the launcher to test for multi-node functionality
     */
    public void setMultiNodeLauncher(JdbcMigrationLauncher multiNodeLauncher)
    {
        this.multiNodeLauncher = multiNodeLauncher;
    }

    /**
     * Get the patch level for a given database
     *
     * @param conn the database connection to use
     * @param system
     * @return int representing the patch level
     * @exception Exception if getting the patch level fails
     */
    protected int getPatchLevel(Connection conn, String system) throws Exception
    {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT MAX(patch_level) AS patch_level FROM patches WHERE system_name='" + system + "'");
        rs.next();
        int patchLevel = rs.getInt("patch_level");
        SqlUtil.close(null, stmt, rs);
        return patchLevel;
    }

    protected Connection getOrderConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:hsqldb:mem:orders", "sa", "");
    }
}
