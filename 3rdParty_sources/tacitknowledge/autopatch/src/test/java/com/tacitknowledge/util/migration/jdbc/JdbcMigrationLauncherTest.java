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

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

import com.tacitknowledge.util.migration.*;
import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.easymock.MockControl;

import com.mockrunner.jdbc.PreparedStatementResultSetHandler;
import com.mockrunner.mock.jdbc.MockConnection;
import com.mockrunner.mock.jdbc.MockResultSet;
import com.tacitknowledge.util.migration.jdbc.util.ConnectionWrapperDataSource;

import static org.easymock.EasyMock.anyInt;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createControl;
import static org.easymock.classextension.EasyMock.createStrictControl;

/**
 * Exercise the data source migration context
 *
 * @author Mike Hardy (mike@tacitknowledge.com)
 */
public class JdbcMigrationLauncherTest extends MigrationListenerTestBase {
    private static final String ORDERED_MIGRATION_STRATEGY = "com.tacitknowledge.util.migration.OrderedMigrationRunnerStrategy";
    /**
     * The mock JDBC connection to use during the tests
     */
    private MockConnection conn = null;

    /**
     * The launcher we're testing
     */
    private JdbcMigrationLauncher launcher = null;

    /**
     * The <code>JDBCMigrationConteext</code> used for testing
     */
    private DataSourceMigrationContext context = new DataSourceMigrationContext();
    private static final String MISSING_PATCH_MIGRATION_STRATEGY = "com.tacitknowledge.util.migration.MissingPatchMigrationRunnerStrategy";
    private IMocksControl rollbackMocksControl;
    private MigrationProcess rollbackMigrationProcessMock;
    private JdbcMigrationLauncher rollbackLauncher;
    private static final int ROLLBACK_LEVEL = 3;
    private static final int[] ROLLBACK_LEVELS = new int[]{ROLLBACK_LEVEL};
    private static final int ROLLBACK_EXPECTED = 5;
    private static final boolean FORCE_ROLLBACK = false;

    /**
     * constructor that takes a name
     *
     * @param name of the test to run
     */
    public JdbcMigrationLauncherTest(String name) {
        super(name);
    }

    /**
     * The Launcher to test
     *
     * @return JdbcMigrationLauncher to test
     */
    public JdbcMigrationLauncher getLauncher() {
        return launcher;
    }

    /**
     * Set the Launcher to test
     *
     * @param launcher JdbcMigrationLauncher to use for testing
     */
    public void setLauncher(JdbcMigrationLauncher launcher) {
        this.launcher = launcher;
    }

    /**
     * @see com.mockrunner.jdbc.JDBCTestCaseAdapter#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();

        // Set up a mock context for some of the tests
        conn = getJDBCMockObjectFactory().getMockConnection();
        context = new DataSourceMigrationContext();
        context.setDataSource(new ConnectionWrapperDataSource(conn));
        context.setSystemName("milestone");
        context.setDatabaseType(new DatabaseType("postgres"));

        // Set up a complete launcher from a known properties file for some other tests

        // Make sure we load our test launcher factory, which fakes out the data source context
        System.getProperties()
                .setProperty("migration.factory",
                        "com.tacitknowledge.util.migration.jdbc.TestJdbcMigrationLauncherFactory");
        setupMigrationLauncher(MigrationRunnerFactory.DEFAULT_MIGRATION_STRATEGY);
        rollbackMocksControl = createControl();

        int migrationTasksExecuted = 3;

        JdbcMigrationContext contextMock = rollbackMocksControl.createMock(JdbcMigrationContext.class);
        rollbackMigrationProcessMock = rollbackMocksControl.createMock(MigrationProcess.class);
        PatchInfoStore patchInfoStoreMock = rollbackMocksControl.createMock(PatchInfoStore.class);
        Connection connectionMock = rollbackMocksControl.createMock(Connection.class);

        //Dependency Interactions
        expect(patchInfoStoreMock.isPatchStoreLocked()).andReturn(false);
        expect(patchInfoStoreMock.getPatchLevel()).andReturn(3);
        patchInfoStoreMock.lockPatchStore();
        patchInfoStoreMock.unlockPatchStore();
        expect(contextMock.getConnection()).andReturn(connectionMock);
        expect(connectionMock.getAutoCommit()).andReturn(true);
        connectionMock.setAutoCommit(false);
        expect(connectionMock.isClosed()).andReturn(false);
        connectionMock.setAutoCommit(true);
        expect(rollbackMigrationProcessMock.doRollbacks(patchInfoStoreMock,
                ROLLBACK_LEVELS,
                contextMock,
                FORCE_ROLLBACK)).andReturn(ROLLBACK_EXPECTED);



        //Setting Dependencies
        rollbackLauncher = new JdbcMigrationLauncher();

        rollbackMigrationProcessMock.addListener(rollbackLauncher);
        rollbackMigrationProcessMock.addMigrationTaskSource(EasyMock.<MigrationTaskSource>anyObject());
        rollbackMigrationProcessMock.addMigrationTaskSource(EasyMock.<MigrationTaskSource>anyObject());
        expect(rollbackMigrationProcessMock.doPostPatchMigrations(contextMock)).andReturn(migrationTasksExecuted);

        LinkedHashMap contexts=new LinkedHashMap();
        contexts.put(contextMock, patchInfoStoreMock);
        rollbackLauncher.setContexts(contexts);

    }

    private JdbcMigrationLauncher setupMigrationLauncher(final String strategy) throws MigrationException {
        JdbcMigrationLauncherFactory factory = new TestJdbcMigrationLauncherFactory(){

            protected Properties loadProperties( InputStream is ) throws IOException {
                Properties props = new Properties();
                props.load(is);

                props.setProperty( "migration.strategy", strategy );

                return props;
            }
        };

        // Create the launcher (this does configure it as a side-effect)
        launcher = factory.createMigrationLauncher("catalog");

        // Make sure we get notification of any migrations
        launcher.getMigrationProcess().addListener(this);

        return launcher;
    }

    /**
     * Verify that the configuration for multi-node made it through
     *
     * @throws Exception if there is a problem
     */
    public void testMultiNodeConfiguration() throws Exception {
        Map contexts = launcher.getContexts();
        assertEquals(2, contexts.size());
        Iterator contextIter = contexts.keySet().iterator();
        JdbcMigrationContext context1 = (JdbcMigrationContext) contextIter.next();
        JdbcMigrationContext context2 = (JdbcMigrationContext) contextIter.next();
        assertNotSame(context1, context2);
        assertEquals("sybase", context1.getDatabaseType().getDatabaseType());
        assertEquals("postgres", context2.getDatabaseType().getDatabaseType());
    }

    /**
     * Test doing migrations with a lock race from a quick cluster
     *
     * @throws Exception if there is a problem
     */
    public void testDoMigrationsWithLockRace() throws Exception {
        // Setup enough for the first
        PreparedStatementResultSetHandler h = conn.getPreparedStatementResultSetHandler();
        MockResultSet rs = h.createResultSet();
        rs.addRow(new Integer[]{new Integer(0)});
        h.prepareGlobalResultSet(rs);


        MockControl mockControl = MockControl.createStrictControl(PatchInfoStore.class);
        PatchInfoStore patchStore = (PatchInfoStore) mockControl.getMock();

        // First they see if it is locked, and it is, so they spin
        patchStore.isPatchStoreLocked();
        mockControl.setReturnValue(true);

        // Second they see if it is locked again, and it isn't, so they try and fail and spin
        patchStore.isPatchStoreLocked();
        mockControl.setReturnValue(false);
        patchStore.getPatchLevel();
        mockControl.setReturnValue(0);
        patchStore.lockPatchStore();
        mockControl.setThrowable(new IllegalStateException("The table is already locked"));

        // Finally they see if it is locked again, and it isn't, and it works
        patchStore.isPatchStoreLocked();
        mockControl.setReturnValue(false);


        patchStore.getPatchLevel();
        mockControl.setReturnValue(2, MockControl.ONE_OR_MORE);
        patchStore.lockPatchStore();

        IMocksControl migrationRunnerStrategyControl = createStrictControl();
        MigrationRunnerStrategy migrationStrategyMock = migrationRunnerStrategyControl.createMock(MigrationRunnerStrategy.class);
        expect(migrationStrategyMock.shouldMigrationRun(anyInt(), eq(patchStore))).andReturn(true).anyTimes();

        patchStore.updatePatchLevel(4);
        patchStore.updatePatchLevel(5);
        patchStore.updatePatchLevel(6);
        patchStore.updatePatchLevel(7);
        patchStore.unlockPatchStore();

        mockControl.replay();
        migrationRunnerStrategyControl.replay();

        TestJdbcMigrationLauncher testLauncher = new TestJdbcMigrationLauncher(context);
        testLauncher.getMigrationProcess().setMigrationRunnerStrategy(migrationStrategyMock);
        testLauncher.setLockPollMillis(0);
        testLauncher.setLockPollRetries(4);
        testLauncher.setIgnoreMigrationSuccessfulEvents(false);
        testLauncher.setPatchStore(patchStore);
        testLauncher.setPatchPath("com.tacitknowledge.util.migration.tasks.normal");
        testLauncher.doMigrations();
        mockControl.verify();
    }

    /**
     * Test doing migrations with a lock override
     *
     * @throws Exception if there is a problem
     */
    public void testLockOverride() throws Exception {
        // Setup enough for the first
        PreparedStatementResultSetHandler h = conn.getPreparedStatementResultSetHandler();
        MockResultSet rs = h.createResultSet();
        rs.addRow(new Integer[]{new Integer(0)});
        h.prepareGlobalResultSet(rs);


        MockControl mockControl = MockControl.createStrictControl(PatchInfoStore.class);
        PatchInfoStore patchStore = (PatchInfoStore) mockControl.getMock();

        // First they see if it is locked three times, and it is, so they spin
        patchStore.isPatchStoreLocked();
        mockControl.setReturnValue(true);
        patchStore.isPatchStoreLocked();
        mockControl.setReturnValue(true);
        patchStore.isPatchStoreLocked();
        mockControl.setReturnValue(true);
        patchStore.isPatchStoreLocked();
        mockControl.setReturnValue(true);

        // after the third time, they unlock it
        patchStore.unlockPatchStore();

        // now the lock succeeds
        patchStore.isPatchStoreLocked();
        mockControl.setReturnValue(false);
        patchStore.getPatchLevel();
        mockControl.setReturnValue(2);
        patchStore.lockPatchStore();

        IMocksControl migrationRunnerStrategyControl = createStrictControl();
        MigrationRunnerStrategy migrationStrategyMock = migrationRunnerStrategyControl.createMock(MigrationRunnerStrategy.class);
        expect(migrationStrategyMock.shouldMigrationRun(anyInt(), eq(patchStore))).andReturn(true).anyTimes();

        patchStore.updatePatchLevel(4);
        patchStore.updatePatchLevel(5);
        patchStore.updatePatchLevel(6);
        patchStore.updatePatchLevel(7);
        patchStore.unlockPatchStore();

        TestJdbcMigrationLauncher testLauncher = new TestJdbcMigrationLauncher(context);
        testLauncher.getMigrationProcess().setMigrationRunnerStrategy(migrationStrategyMock);
        testLauncher.setLockPollMillis(0);
        testLauncher.setLockPollRetries(3);
        testLauncher.setIgnoreMigrationSuccessfulEvents(false);
        testLauncher.setPatchStore(patchStore);
        testLauncher.setPatchPath("com.tacitknowledge.util.migration.tasks.normal");

        mockControl.replay();
        migrationRunnerStrategyControl.replay();
        testLauncher.doMigrations();
        mockControl.verify();
    }

    /**
     * Test that when a migrationSuccessful event fires.  If the
     * 'successful' patch level is less than the current patch level
     * for the context's that is updating, then do not blow away the
     * existing patch level.
     * This case occurs when running patching in force sync mode.
     * Example:
     * node 1 is at patch level 2
     * node 2 is out of sync (or patch level zero).
     * When node 2 is forcesync'ed, node2 applies patch 1,
     * it succeeds and all migration listeners are notified.
     * If we do not protect the nodes, then node 1 would have it's patch level set
     * to 1.  Then when patch 2 is executed, node 1 now thinks it needs to apply
     * it, and chaos ensues.
     *
     * @throws MigrationException
     */
    public void testMigrationSuccessfulDoesNotOverWritePatchLevel() throws MigrationException {
        // not using the 'setup' method's initilization because I really just want
        // to unit test the migration successful method.
        launcher = new JdbcMigrationLauncher();

        int migrationSuccessfulPatchLevel = 1;
        int node1PatchLevel = 2;
        int node2PatchLevel = 0;

        // create mocks
        MockControl node1ContextControl = MockControl.createControl(JdbcMigrationContext.class);
        MockControl node2ContextControl = MockControl.createControl(JdbcMigrationContext.class);
        JdbcMigrationContext node1Context = (JdbcMigrationContext) node1ContextControl.getMock();
        JdbcMigrationContext node2Context = (JdbcMigrationContext) node2ContextControl.getMock();

        MockControl node1PatchInfoStoreControl = MockControl.createControl(PatchInfoStore.class);
        MockControl node2PatchInfoStoreControl = MockControl.createControl(PatchInfoStore.class);
        PatchInfoStore node1PatchInfoStore = (PatchInfoStore) node1PatchInfoStoreControl.getMock();
        PatchInfoStore node2PatchInfoStore = (PatchInfoStore) node2PatchInfoStoreControl.getMock();

        LinkedHashMap contexts = new LinkedHashMap();
        contexts.put(node1Context, node1PatchInfoStore);
        contexts.put(node2Context, node2PatchInfoStore);
        launcher.setContexts(contexts);

        MockControl taskControl = MockControl.createControl(RollbackableMigrationTask.class);
        MockControl contextControl = MockControl.createControl(MigrationContext.class);
        RollbackableMigrationTask task = (RollbackableMigrationTask) taskControl.getMock();
        MigrationContext context = (MigrationContext) contextControl.getMock();

        // set expectations
        // the migration successful event is for patch level 1
        task.getLevel();
        taskControl.setDefaultReturnValue(new Integer(migrationSuccessfulPatchLevel));
        task.getName();
        taskControl.setDefaultReturnValue("patch001_test.sql");

        taskControl.replay();

        IMocksControl migrationStrategyControl = createStrictControl();
        MigrationRunnerStrategy migrationStrategyMock = migrationStrategyControl.createMock(MigrationRunnerStrategy.class);

        // node1 is at patchlevel 2
        node1PatchInfoStore.getPatchLevel();
        node1PatchInfoStoreControl.setDefaultReturnValue(node1PatchLevel);
        node1PatchInfoStoreControl.replay();
        expect(migrationStrategyMock.shouldMigrationRun(1, node1PatchInfoStore)).andReturn(false);

        // node2 is at patchlevel 1
        node2PatchInfoStore.getPatchLevel();
        node2PatchInfoStoreControl.setDefaultReturnValue(node2PatchLevel);
        expect(migrationStrategyMock.shouldMigrationRun(1, node2PatchInfoStore)).andReturn(true);


        node2PatchInfoStore.updatePatchLevel(migrationSuccessfulPatchLevel);
        node2PatchInfoStoreControl.setVoidCallable();

        node2PatchInfoStoreControl.replay();
        migrationStrategyControl.replay();

        launcher.getMigrationProcess().setMigrationRunnerStrategy(migrationStrategyMock);

        // test and validate
        launcher.migrationSuccessful(task, context);
        taskControl.verify();
        node1PatchInfoStoreControl.verify();
        node2PatchInfoStoreControl.verify();
    }


    public void testGetTheMigrationStrategyAlreadyConfigured() throws MigrationException {
        setupMigrationLauncher(MISSING_PATCH_MIGRATION_STRATEGY);

        String currentMigrationStrategy = launcher.getMigrationStrategy();
        assertEquals("Current migration strategy should be '" + MISSING_PATCH_MIGRATION_STRATEGY + "'",
                MISSING_PATCH_MIGRATION_STRATEGY, currentMigrationStrategy);

    }

    public void testGetOrderedAsDefaultStrategyWhenEmptyStrategyIsConfigured() throws MigrationException {
        setupMigrationLauncher(" ");

        MigrationProcess process = launcher.getMigrationProcess();

        assertTrue("Should be instance of default object", process.getMigrationRunnerStrategy() instanceof OrderedMigrationRunnerStrategy);
    }

    public void testNotSettingStrategyGeneratesDefaultMigrationRunner() throws MigrationException {
        setupMigrationLauncher("");

        MigrationProcess process = launcher.getMigrationProcess();

        assertTrue("Should be instance of default object", process.getMigrationRunnerStrategy() instanceof OrderedMigrationRunnerStrategy);

    }


    public void testSettingOrderedStrategyGeneratesDefaultMigrationRunner() throws MigrationException {
        setupMigrationLauncher(ORDERED_MIGRATION_STRATEGY);

        MigrationProcess process = launcher.getMigrationProcess();

        assertTrue("Should be instance of default object", process.getMigrationRunnerStrategy() instanceof OrderedMigrationRunnerStrategy);

    }

    public void testSettingFullClassNameForStrategyGeneratesMigrationProcessAccordingToTheClasNameReceived() throws MigrationException {
        setupMigrationLauncher(MISSING_PATCH_MIGRATION_STRATEGY);

        MigrationProcess process = launcher.getMigrationProcess();

        assertTrue("Should be instance of defined object", process.getMigrationRunnerStrategy() instanceof MissingPatchMigrationRunnerStrategy);
    }

    public void testSettingFullClassNameWithTrailingSpacesForStrategyGeneratesMigrationProcessAccordingToTheClasNameReceived() throws MigrationException {
        setupMigrationLauncher(" " +MISSING_PATCH_MIGRATION_STRATEGY + " ");

        MigrationProcess process = launcher.getMigrationProcess();

        assertTrue("Should be instance of defined object", process.getMigrationRunnerStrategy() instanceof MissingPatchMigrationRunnerStrategy);
    }

    public void testSettingUnrecognizableStrategyThrowsIllegalArgumentException() throws MigrationException {

        try {
            setupMigrationLauncher("unrecognized.strategy");
            fail("It should have thrown an IllegalArgumentException for an unrecognized strategy");
        } catch (IllegalArgumentException ae) {
            //expected
        }

    }

    public void testDoRollbacksActionWithForceRollBackParameter() throws MigrationException, SQLException {

        rollbackMocksControl.replay();
        rollbackLauncher.setMigrationProcess(rollbackMigrationProcessMock);


        int actual = rollbackLauncher.doRollbacks(ROLLBACK_LEVELS, FORCE_ROLLBACK );

        assertEquals("Expected rollbacks should be 5", ROLLBACK_EXPECTED, actual);
        rollbackMocksControl.verify();
    }


    public void testDoRollbacksActionWithoutForceRollbackParameter() throws MigrationException, SQLException {

        rollbackMocksControl.replay();
        rollbackLauncher.setMigrationProcess(rollbackMigrationProcessMock);

        int actual = rollbackLauncher.doRollbacks(ROLLBACK_LEVELS);

        assertEquals("Expected rollbacks should be 5", ROLLBACK_EXPECTED, actual);
        rollbackMocksControl.verify();
    }

}
