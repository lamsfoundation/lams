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

import java.util.*;

import com.tacitknowledge.util.migration.builders.MockBuilder;
import com.tacitknowledge.util.migration.jdbc.*;
import com.tacitknowledge.util.migration.tasks.rollback.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.easymock.EasyMock;
import org.easymock.MockControl;

import com.mockrunner.mock.jdbc.MockDataSource;
import org.easymock.classextension.IMocksControl;

import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createControl;

/**
 * Test the Distributed auto patch service to make sure it configures and runs
 * correctly
 * 
 * @author Artie Pesh-Imam (apeshimam@tacitknowledge.com)
 */
public class DistributedAutoPatchRollbackTest extends MigrationListenerTestBase
{
    /** Class logger */
    private static Log log = LogFactory.getLog(DistributedAutoPatchRollbackTest.class);
    private static final int[] ROLLBACK_LEVELS = new int[]{8};

    /** The launcher we're testing */
    private DistributedJdbcMigrationLauncher launcher = null;

    /** A MigrationContext for us */
    private TestMigrationContext context = null;
    private PatchInfoStore currentPatchInfoStore;

    /**
     * Just delegates to the superclass
     * 
     * @param name of the test to run
     */
    public DistributedAutoPatchRollbackTest(String name)
    {
        super(name);
    }

    /**
     * Configures a DistributedAutoPatchService and it's child AutoPatchService
     * objects to match the "migration.properties" configuration in the
     * AutoPatch test suite. This let's us reuse the actual functionality checks
     * that verify the configuration was correct, as the launcher is the same,
     * it's just the adapter that's different.
     * 
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception
    {
        // configure the controlled AutoPatchService, first by calling super
        super.setUp();
        log.debug("setting up " + this.getClass().getName());
        
        // Make sure we load our test launcher factory, which fakes out the data source context
        System.getProperties().setProperty("migration.factory",
            "com.tacitknowledge.util.migration.jdbc.TestJdbcMigrationLauncherFactory");
        DistributedJdbcMigrationLauncherFactory factory = new TestDistributedJdbcMigrationLauncherFactory();
        
        // Create the launcher (this does configure it as a side-effect)
        launcher = (DistributedJdbcMigrationLauncher) factory.createMigrationLauncher("orchestration");
        
        // Make sure we get notification of any migrations
        launcher.getMigrationProcess().addListener(this);
        
        context = new TestMigrationContext();
        
        // core sub-system
        AutoPatchService coreService = new TestAutoPatchService();
        coreService.setSystemName("core");
        coreService.setDatabaseType("postgres");
        coreService.setDataSource(new MockDataSource());
        coreService.setPatchPath("patch.core:com.tacitknowledge.util.migration.jdbc.test");
        
        // orders: patch path
        // patches.orders.com.tacitknowledge.util.migration.tasks.normal
        AutoPatchService ordersService = new TestAutoPatchService();
        ordersService.setSystemName("orders");
        ordersService.setDatabaseType("postgres");
        ordersService.setDataSource(new MockDataSource());
        ordersService.setPatchPath("patch.orders:com.tacitknowledge.util.migration.tasks.rollback");
        
        // catalog: patch path patches.catalog
        AutoPatchService catalogService = new TestAutoPatchService();
        catalogService.setPatchPath("patch.catalog");
        
        // make catalog a multi-node patch service
        TestDataSourceMigrationContext catalogContext1 = new TestDataSourceMigrationContext();
        TestDataSourceMigrationContext catalogContext2 = new TestDataSourceMigrationContext();
        catalogContext1.setSystemName("catalog");
        catalogContext2.setSystemName("catalog");
        catalogContext1.setDatabaseType(new DatabaseType("postgres"));
        catalogContext2.setDatabaseType(new DatabaseType("postgres"));
        catalogContext1.setDataSource(new MockDataSource());
        catalogContext2.setDataSource(new MockDataSource());
        catalogService.addContext(catalogContext1);
        catalogService.addContext(catalogContext2);
        
        // configure the DistributedAutoPatchService
        DistributedAutoPatchService distributedPatchService = new TestDistributedAutoPatchService();
        distributedPatchService.setSystemName("orchestration");
        distributedPatchService.setDatabaseType("postgres");
        distributedPatchService.setReadOnly(false);
        AutoPatchService[] controlledSystems = new AutoPatchService[3];
        controlledSystems[0] = coreService;
        controlledSystems[1] = ordersService;
        controlledSystems[2] = catalogService;
        distributedPatchService.setControlledSystems(controlledSystems);
        distributedPatchService.setDataSource(new MockDataSource());
        
        // instantiate everything
        setLauncher(distributedPatchService.getLauncher());
        
        // set ourselves up as a listener for any migrations that run
        getLauncher().getMigrationProcess().addListener(this);
        currentPatchInfoStore = MockBuilder.getPatchInfoStore(12);
    }

    /**
     * Make sure that the task loading works correctly
     * 
     * @exception MigrationException if anything goes wrong
     */
    public void testDistributedMigrationTaskLoading() throws MigrationException
    {
        DistributedMigrationProcess process = (DistributedMigrationProcess) getLauncher().getMigrationProcess();
        assertEquals(8, process.getMigrationTasks().size());
        assertEquals(8, process.getMigrationTasksWithLaunchers().size());
    }

    /**
     * Ensure that overlapping tasks even among sub-launchers are detected
     * 
     * @exception Exception if anything goes wrong
     */
    public void testDistributedMigrationTaskValidation() throws Exception
    {
        MigrationProcess process = getLauncher().getMigrationProcess();
        process.validateTasks(process.getMigrationTasks());
        
        // Make one of the sub-tasks conflict with a sub-task from another launcher
        TestRollbackableTask2.setPatchLevelOverride(new Integer(8));
        try
        {
            process.validateTasks(process.getMigrationTasks());
            fail("We should have thrown an exception - "
                    + "there were overlapping tasks among sub-launchers");
        } 
        catch (MigrationException me)
        {
            // we expect this
        } 
        finally
        {
            // make sure future tests work
            TestRollbackableTask2.reset();
        }
    }

    /**
     * Make sure we get notified of patch application
     * 
     * @exception Exception if anything goes wrong
     */
    public void testDistributedRollbackEvents() throws Exception
    {
        IMocksControl mockControl = createControl();

        // There should be five listener on the main process
        // 1) the distributed launcher
        // 2) this test object
        // 3-5) the three sub-launchers
        assertEquals(5, getLauncher().getMigrationProcess().getListeners().size());

        // The sub-MigrationProcesses should have one listener each - the
        // sub-launcher
        HashMap controlledSystems = ((DistributedMigrationProcess) getLauncher()
                .getMigrationProcess()).getControlledSystems();


        List<MigrationTask> migrationTasks = new ArrayList<MigrationTask>();

        for (Iterator controlledSystemIter = controlledSystems.keySet().iterator(); controlledSystemIter.hasNext();)
        {
            String controlledSystemName = (String) controlledSystemIter.next();
            JdbcMigrationLauncher subLauncher = (JdbcMigrationLauncher) controlledSystems.get(controlledSystemName);
            MigrationProcess subProcess = subLauncher.getMigrationProcess();
            List<MigrationTask> migrationTasksList = subProcess.getMigrationTasks();
            migrationTasks.addAll(migrationTasksList);
            assertEquals(1, subProcess.getListeners().size());

            MigrationProcess migrationProcessMock = mockControl.createMock(MigrationProcess.class);
            expect(migrationProcessMock.getMigrationTasks()).andReturn(migrationTasksList);
            subLauncher.setMigrationProcess(migrationProcessMock);

        }


        // Now do the migrations, and make sure we get the right number of
        // events
        DistributedMigrationProcess process = (DistributedMigrationProcess) getLauncher().getMigrationProcess();

        List<MigrationTask> rollbackCandidates = new ArrayList<MigrationTask>();

        for (MigrationTask migrationTask : migrationTasks) {
            if (migrationTask instanceof TestRollbackableTask2
                    || migrationTask instanceof TestRollbackableTask3
                    || migrationTask instanceof TestRollbackableTask4
                    || migrationTask instanceof TestRollbackableTask5) {

                rollbackCandidates.add(migrationTask);
            }
        }


        MigrationRunnerStrategy migrationRunnerStrategyMock = mockControl.createMock(MigrationRunnerStrategy.class);


        expect(migrationRunnerStrategyMock.getRollbackCandidates(EasyMock.<List<MigrationTask>>anyObject(),
                eq(ROLLBACK_LEVELS), eq(currentPatchInfoStore))).andReturn(rollbackCandidates);
        expect(migrationRunnerStrategyMock.getRollbackCandidates(rollbackCandidates,
                ROLLBACK_LEVELS, currentPatchInfoStore)).andReturn(Collections.EMPTY_LIST);

        expect(migrationRunnerStrategyMock.isSynchronized(eq(currentPatchInfoStore),
                EasyMock.<PatchInfoStore>anyObject())).andReturn(true).anyTimes();

        process.setMigrationRunnerStrategy(migrationRunnerStrategyMock);
        mockControl.replay();

        int currentPatchlevel = 12;

        setReportedPatchLevel(process.getControlledSystems().values(), currentPatchlevel);
        int patches = process.doRollbacks(currentPatchInfoStore, ROLLBACK_LEVELS, getContext(), false);
        assertEquals(4, patches);
        assertEquals(4, getRollbackStartedCount());
        assertEquals(4, getRollbackSuccessCount());
    }

    /**
     * Ensure that read-only mode actually works
     * 
     * @exception Exception if anything goes wrong
     */
    public void testDistributedReadOnlyMode() throws Exception
    {
        int currentPatchLevel = 12;

        DistributedMigrationProcess process = (DistributedMigrationProcess) getLauncher().getMigrationProcess();
        process.validateTasks(process.getMigrationTasks());
        
        // need to mock the patch info stores to return the expected patch levels
        HashMap controlledSystems = process.getControlledSystems();
        setReportedPatchLevel(controlledSystems.values(), currentPatchLevel);
        
        // Make it readonly
        process.setReadOnly(true);
        
        // Now do the migrations, and make sure we get the right number of events
        try
        {
            process.doRollbacks(currentPatchInfoStore, ROLLBACK_LEVELS, getContext(), false);
            fail("There should have been an exception - unapplied patches + read-only don't work");
        } 
        catch (MigrationException me)
        {
            // we expect this
        }
        
        currentPatchLevel = 13;
        // need to mock the patch info stores to return the expected patch levels
        setReportedPatchLevel(controlledSystems.values(), currentPatchLevel);
        //int patches = process.doRollbacks(currentPatchLevel, rollbackPatchLevel, getContext());
        // assertEquals(0, patches);
        assertEquals(0, getRollbackStartedCount());
        assertEquals(0, getRollbackSuccessCount());
    }
    
    /**
     * For the given launchers, set all it's context's patch info stores as mocks
     * that report the given patch level.  This method is a helper to get this
     * test to pass the DisitributedMigrationProcess::validateControlledSystems() test.
     * @param launchers Collection of JDBCMigrationLaunchers
     * @param levelToReport the patch level the mock should report
     * @throws MigrationException 
     */
    protected void setReportedPatchLevel(Collection launchers, int levelToReport) throws MigrationException
    {
        for(Iterator launchersIterator = launchers.iterator(); launchersIterator.hasNext(); )
        {
            JdbcMigrationLauncher launcher = (JdbcMigrationLauncher) launchersIterator.next();
            for(Iterator it = launcher.getContexts().keySet().iterator(); it.hasNext(); )
            {
                MigrationContext ctx = (MigrationContext) it.next();
                MockControl patchInfoStoreControl = MockControl.createControl(PatchInfoStore.class);
                PatchInfoStore patchInfoStore = (PatchInfoStore) patchInfoStoreControl.getMock();
                patchInfoStore.getPatchLevel();
                patchInfoStoreControl.setReturnValue(levelToReport);
                patchInfoStoreControl.replay();
                launcher.getContexts().put(ctx, patchInfoStore);
            }
        }
    }
    
    /**
     * Get the MigrationContext to use during testing
     * 
     * @return TestMigrationContext object
     */
    public TestMigrationContext getContext()
    {
        return context;
    }

    /**
     * Set the MigrationContext to use for testing
     * 
     * @param context a TestMigrationContext object to use for testing
     */
    public void setContext(TestMigrationContext context)
    {
        this.context = context;
    }

    /**
     * Get the launcher to use for testing
     * 
     * @return DistributedJdbcMigrationLauncher to use for testing
     */
    public DistributedJdbcMigrationLauncher getLauncher()
    {
        return launcher;
    }

    /**
     * Set the launcher to test
     * 
     * @param launcher the DistributedJdbcMigrationLauncher to test
     */
    public void setLauncher(DistributedJdbcMigrationLauncher launcher)
    {
        this.launcher = launcher;
    }
}
