/* Copyright 2011 Tacit Knowledge
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

import com.tacitknowledge.util.migration.jdbc.DistributedJdbcMigrationLauncher;
import com.tacitknowledge.util.migration.jdbc.DistributedJdbcMigrationLauncherFactory;
import com.tacitknowledge.util.migration.jdbc.JdbcMigrationLauncher;
import com.tacitknowledge.util.migration.jdbc.JdbcMigrationLauncherFactory;
import com.tacitknowledge.util.migration.jdbc.util.SqlUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.*;

/*
 * @author Hemri Herrera (hemri@tacitknowledge.com)
 * @author Ulises Pulido (upulido@tacitknowledge.com)
 */
public class MissingPatchMigrationRunnerStrategyIntegrationTest extends AutoPatchIntegrationTestBase {

    /**
     * Class logger
     */
    private static Log log = LogFactory.getLog(MissingPatchMigrationRunnerStrategyIntegrationTest.class);
    private static final String ORDERS = "orders";

    /**
     * Constructor
     *
     * @param name the name of the test to run
     */
    public MissingPatchMigrationRunnerStrategyIntegrationTest(String name) {
        super(name);
    }

    public void setUp() throws Exception {
    }

    public void testMigrationRunsFromBatches() throws Exception {

        try {
            JdbcMigrationLauncherFactory lFactory = new JdbcMigrationLauncherFactory();
            log.debug("Testing migration from batch 1");
            JdbcMigrationLauncher launcherBatch1 = lFactory.createMigrationLauncher(ORDERS, "missingpatchstrategybatch1-inttest-migration.properties");
            launcherBatch1.doMigrations();

            assertEquals(4, getPatchLevel(getOrderConnection(), ORDERS));
            assertTrue(isPatchApplied(getOrderConnection(), 1, ORDERS));
            assertTrue(isPatchApplied(getOrderConnection(), 4, ORDERS));
            assertFalse(isPatchApplied(getOrderConnection(), 2, ORDERS));
            assertFalse(isPatchApplied(getOrderConnection(), 3, ORDERS));

            log.debug("Testing migration from batch 2");
            JdbcMigrationLauncher launcherBatch2 = lFactory.createMigrationLauncher(ORDERS, "missingpatchstrategybatch2-inttest-migration.properties");
            launcherBatch2.doMigrations();
            currentPatches(ORDERS);

            assertEquals(4, getPatchLevel(getOrderConnection(), ORDERS));
            assertTrue(isPatchApplied(getOrderConnection(), 1, ORDERS));
            assertTrue(isPatchApplied(getOrderConnection(), 3, ORDERS));
            assertTrue(isPatchApplied(getOrderConnection(), 4, ORDERS));
            assertTrue(isPatchApplied(getOrderConnection(), 2, ORDERS));

        } catch (Exception e) {
            log.error("Unexpected error", e);
            fail("shouldn't have thrown any exceptions");
        }

    }

    public void testMigrationRunsFromTwoBatchesOnMultipleNodes() throws Exception, SQLException {
        DistributedJdbcMigrationLauncherFactory dlFactory =
            new DistributedJdbcMigrationLauncherFactory();

        DistributedJdbcMigrationLauncher distributedLauncher1 = (DistributedJdbcMigrationLauncher)
                dlFactory.createMigrationLauncher("nodes",
                        "missingpatchstrategybatch1-inttest-migration.properties");

        distributedLauncher1.doMigrations();

        Connection node1Conn = DriverManager.getConnection("jdbc:hsqldb:mem:node1", "sa", "");
        Connection node2Conn = DriverManager.getConnection("jdbc:hsqldb:mem:node2", "sa", "");

        assertEquals(4, getPatchLevel(node1Conn, "nodes"));
        assertTrue(isPatchApplied(node1Conn, 1, "nodes"));
        assertTrue(isPatchApplied(node1Conn, 4, "nodes"));
        assertFalse(isPatchApplied(node1Conn, 2, "nodes"));
        assertFalse(isPatchApplied(node1Conn, 3, "nodes"));


        assertEquals(4, getPatchLevel(node2Conn, "nodes"));
        assertTrue(isPatchApplied(node2Conn, 1, "nodes"));
        assertTrue(isPatchApplied(node2Conn, 4, "nodes"));
        assertFalse(isPatchApplied(node2Conn, 2, "nodes"));
        assertFalse(isPatchApplied(node2Conn, 3, "nodes"));


        DistributedJdbcMigrationLauncher distributedLauncher2 = (DistributedJdbcMigrationLauncher)
                dlFactory.createMigrationLauncher("nodes",
                        "missingpatchstrategybatch2-inttest-migration.properties");

        distributedLauncher2.doMigrations();

        assertEquals(4, getPatchLevel(node1Conn, "nodes"));
        assertTrue(isPatchApplied(node1Conn, 1, "nodes"));
        assertTrue(isPatchApplied(node1Conn, 4, "nodes"));
        assertTrue(isPatchApplied(node1Conn, 2, "nodes"));
        assertTrue(isPatchApplied(node1Conn, 3, "nodes"));


        assertEquals(4, getPatchLevel(node2Conn, "nodes"));
        assertTrue(isPatchApplied(node2Conn, 1, "nodes"));
        assertTrue(isPatchApplied(node2Conn, 4, "nodes"));
        assertTrue(isPatchApplied(node2Conn, 2, "nodes"));
        assertTrue(isPatchApplied(node2Conn, 3, "nodes"));


        SqlUtil.close(node1Conn, null, null);
        SqlUtil.close(node2Conn, null, null);


    }

    public void testMigrationRunsFromMultipleBatchesOnMultipleNodesWithForcedSync() throws Exception, SQLException {


        testMigrationRunsFromTwoBatchesOnMultipleNodes( );

        DistributedJdbcMigrationLauncherFactory dlFactory =
            new DistributedJdbcMigrationLauncherFactory();

        DistributedJdbcMigrationLauncher distributedLauncher3 = (DistributedJdbcMigrationLauncher)
                dlFactory.createMigrationLauncher("nodes",
                        "missingpatchstrategybatch3-inttest-migration.properties");
        DistributedMigrationProcess distributedMigrationProcess3 = (DistributedMigrationProcess) distributedLauncher3.getMigrationProcess();
        distributedMigrationProcess3.setForceSync(true);
        distributedLauncher3.doMigrations();

        Connection node1Conn = DriverManager.getConnection("jdbc:hsqldb:mem:node1", "sa", "");
        Connection node2Conn = DriverManager.getConnection("jdbc:hsqldb:mem:node2", "sa", "");
        Connection node3Conn = DriverManager.getConnection("jdbc:hsqldb:mem:node3", "sa", "");


        assertEquals(4, getPatchLevel(node1Conn, "nodes"));
        assertTrue(isPatchApplied(node1Conn, 1, "nodes"));
        assertTrue(isPatchApplied(node1Conn, 4, "nodes"));
        assertTrue(isPatchApplied(node1Conn, 2, "nodes"));
        assertTrue(isPatchApplied(node1Conn, 3, "nodes"));


        assertEquals(4, getPatchLevel(node2Conn, "nodes"));
        assertTrue(isPatchApplied(node2Conn, 1, "nodes"));
        assertTrue(isPatchApplied(node2Conn, 4, "nodes"));
        assertTrue(isPatchApplied(node2Conn, 2, "nodes"));
        assertTrue(isPatchApplied(node2Conn, 3, "nodes"));

        assertEquals(4, getPatchLevel(node3Conn, "nodes"));
        assertTrue(isPatchApplied(node3Conn, 1, "nodes"));
        assertTrue(isPatchApplied(node3Conn, 4, "nodes"));
        assertFalse(isPatchApplied(node3Conn, 2, "nodes"));
        assertFalse(isPatchApplied(node3Conn, 3, "nodes"));

        DistributedJdbcMigrationLauncher distributedLauncher4 = (DistributedJdbcMigrationLauncher)
                dlFactory.createMigrationLauncher("nodes",
                        "missingpatchstrategybatch4-inttest-migration.properties");

        DistributedMigrationProcess distributedMigrationProcess4 = (DistributedMigrationProcess) distributedLauncher4.getMigrationProcess();
        distributedMigrationProcess4.setForceSync(true);
        distributedLauncher4.doMigrations();


        assertEquals(4, getPatchLevel(node1Conn, "nodes"));
        assertTrue(isPatchApplied(node1Conn, 1, "nodes"));
        assertTrue(isPatchApplied(node1Conn, 4, "nodes"));
        assertTrue(isPatchApplied(node1Conn, 2, "nodes"));
        assertTrue(isPatchApplied(node1Conn, 3, "nodes"));


        assertEquals(4, getPatchLevel(node2Conn, "nodes"));
        assertTrue(isPatchApplied(node2Conn, 1, "nodes"));
        assertTrue(isPatchApplied(node2Conn, 4, "nodes"));
        assertTrue(isPatchApplied(node2Conn, 2, "nodes"));
        assertTrue(isPatchApplied(node2Conn, 3, "nodes"));

        assertEquals(4, getPatchLevel(node3Conn, "nodes"));
        assertTrue(isPatchApplied(node3Conn, 1, "nodes"));
        assertTrue(isPatchApplied(node3Conn, 4, "nodes"));
        assertTrue(isPatchApplied(node3Conn, 2, "nodes"));
        assertTrue(isPatchApplied(node3Conn, 3, "nodes"));


        SqlUtil.close(node1Conn, null, null);
        SqlUtil.close(node2Conn, null, null);
        SqlUtil.close(node3Conn, null, null);


    }

    private void currentPatches(String database) throws Exception {
        Connection conn = DriverManager.getConnection("jdbc:hsqldb:mem:orders", "sa", "");
        Statement stmt = conn.createStatement();
        ResultSet resultSet = stmt.executeQuery("SELECT * FROM patches");
        while (resultSet.next()) {
            log.info("Result " + resultSet.getInt(2));
        }
        SqlUtil.close(conn, stmt, null);

    }

    private boolean isPatchApplied(Connection conn, int patch_level, String system) throws Exception {

        Statement stmt = conn.createStatement();
        String sql = "SELECT patch_level FROM patches WHERE system_name= '" + system + "' and patch_level=" + patch_level + "";
        ResultSet rs = stmt.executeQuery(sql);
        return rs.next();
    }

    public void testRollbacksRunOnMultipleNode() throws Exception {

        //Applying migrations to node1 and node2
        testMigrationRunsFromTwoBatchesOnMultipleNodes();

        DistributedJdbcMigrationLauncherFactory dlFactory =
            new DistributedJdbcMigrationLauncherFactory();

        DistributedJdbcMigrationLauncher distributedLauncher2 = (DistributedJdbcMigrationLauncher)
                dlFactory.createMigrationLauncher("nodes",
                        "missingpatchstrategybatch2-inttest-migration.properties");

        int[] rollbackLevels = new int[]{ 2, 3 };

        distributedLauncher2.doRollbacks(rollbackLevels);

        Connection node1Conn = DriverManager.getConnection("jdbc:hsqldb:mem:node1", "sa", "");
        Connection node2Conn = DriverManager.getConnection("jdbc:hsqldb:mem:node2", "sa", "");


        assertEquals(4, getPatchLevel(node1Conn, "nodes"));
        assertTrue(isPatchApplied(node1Conn, 1, "nodes"));
        assertTrue(isPatchApplied(node1Conn, 4, "nodes"));
        assertFalse(isPatchApplied(node1Conn, 2, "nodes"));
        assertFalse(isPatchApplied(node1Conn, 3, "nodes"));


        assertEquals(4, getPatchLevel(node2Conn, "nodes"));
        assertTrue(isPatchApplied(node2Conn, 1, "nodes"));
        assertTrue(isPatchApplied(node2Conn, 4, "nodes"));
        assertFalse(isPatchApplied(node2Conn, 2, "nodes"));
        assertFalse(isPatchApplied(node2Conn, 3, "nodes"));


        SqlUtil.close(node1Conn, null, null);
        SqlUtil.close(node2Conn, null, null);

    }

}
