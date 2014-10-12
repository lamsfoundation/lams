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


import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;

import com.tacitknowledge.util.migration.*;
import com.tacitknowledge.util.migration.builders.MockBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.easymock.MockControl;

import com.tacitknowledge.util.migration.tasks.normal.TestMigrationTask2;
import org.easymock.classextension.EasyMock;
import org.easymock.classextension.IMocksControl;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createNiceControl;

/**
 * Test the distributed launcher factory
 *
 * @author Mike Hardy (mike@tacitknowledge.com)
 */
public class DistributedJdbcMigrationLauncherFactoryTest extends MigrationListenerTestBase
{
    /**
     * Class logger
     */
    private static Log log = LogFactory.getLog(DistributedJdbcMigrationLauncherFactoryTest.class);

    /**
     * The launcher we're testing
     */
    private DistributedJdbcMigrationLauncher launcher = null;

    /**
     * A MigrationContext for us
     */
    private TestMigrationContext context = null;

    /**
     * constructor that takes a name
     *
     * @param name of the test to run
     */
    public DistributedJdbcMigrationLauncherFactoryTest(String name)
    {
        super(name);
    }

    /**
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception
    {
        super.setUp();

        log.debug("setting up " + this.getClass().getName());

        // Make sure we load our test launcher factory, which fakes out the data source context
        System.getProperties()
                .setProperty("migration.factory",
                        "com.tacitknowledge.util.migration.jdbc.TestJdbcMigrationLauncherFactory");
        DistributedJdbcMigrationLauncherFactory factory =
                new TestDistributedJdbcMigrationLauncherFactory();

        // Create the launcher (this does configure it as a side-effect)
        launcher =
                (DistributedJdbcMigrationLauncher) factory.createMigrationLauncher("orchestration");

        // Make sure we get notification of any migrations
        launcher.getMigrationProcess().addListener(this);
        context = new TestMigrationContext();
    }

    /**
     * @see junit.framework.TestCase#tearDown()
     */
    protected void tearDown() throws Exception
    {
        super.tearDown();
    }

    /**
     * For the given launchers, set all it's context's patch info stores as mocks
     * that report the given patch level.  This method is a helper to get this
     * test to pass the DisitributedMigrationProcess::validateControlledSystems() test.
     *
     * @param launchers     Collection of JDBCMigrationLaunchers
     * @param levelToReport the patch level the mock should report
     * @throws com.tacitknowledge.util.migration.MigrationException
     */
    protected void setReportedPatchLevel(Collection launchers, int levelToReport) throws MigrationException
    {
        for (Iterator launchersIterator = launchers.iterator(); launchersIterator.hasNext(); )
        {
            JdbcMigrationLauncher launcher = (JdbcMigrationLauncher) launchersIterator.next();
            for (Iterator it = launcher.getContexts().keySet().iterator(); it.hasNext(); )
            {
                MigrationContext ctx = (MigrationContext) it.next();

                launcher.getContexts().put(ctx, MockBuilder.getPatchInfoStore(levelToReport));
            }
        }
    }


    /**
     * Test the configuration of the launchers versus a known property file
     */
    public void testDistributedLauncherConfiguration()
    {
        HashMap controlledSystems =
                ((DistributedMigrationProcess) launcher.getMigrationProcess()).getControlledSystems();
        assertEquals(3, controlledSystems.size());
    }

    /**
     * Make sure that the task loading works correctly
     *
     * @throws MigrationException if anything goes wrong
     */
    public void testDistributedMigrationTaskLoading() throws MigrationException
    {
        DistributedMigrationProcess process =
                (DistributedMigrationProcess) launcher.getMigrationProcess();
        assertEquals(7, process.getMigrationTasks().size());
        assertEquals(7, process.getMigrationTasksWithLaunchers().size());
    }

    /**
     * Ensure that overlapping tasks even among sub-launchers are detected
     *
     * @throws Exception if anything goes wrong
     */
    public void testDistributedMigrationTaskValidation() throws Exception
    {
        MigrationProcess process = launcher.getMigrationProcess();
        process.validateTasks(process.getMigrationTasks());

        // Make one of the sub-tasks conflict with a sub-task from another launcher
        TestMigrationTask2.setPatchLevelOverride(new Integer(3));
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
            TestMigrationTask2.reset();
        }
    }

    /**
     * Ensure that read-only mode actually works
     *
     * @throws Exception if anything goes wrong
     */
    public void testDistributedReadOnlyMode() throws Exception
    {
        int currentPatchLevel = 3;

        DistributedMigrationProcess process = (DistributedMigrationProcess) launcher.getMigrationProcess();
        process.validateTasks(process.getMigrationTasks());

        // need to mock the patch info stores to return the expected patch levels
        HashMap controlledSystems = process.getControlledSystems();
        setReportedPatchLevel(controlledSystems.values(), currentPatchLevel);

        // Make it readonly
        process.setReadOnly(true);

        // Now do the migrations, and make sure we get the right number of events
        try
        {
            process.doMigrations(MockBuilder.getPatchInfoStore(currentPatchLevel), context);
            fail("There should have been an exception - unapplied patches + read-only don't work");
        }
        catch (MigrationException me)
        {
            // we expect this
            log.debug("got exception: " + me.getMessage());
        }

        currentPatchLevel = 8;
        // need to mock the patch info stores to return the expected patch levels        
        setReportedPatchLevel(controlledSystems.values(), currentPatchLevel);

        int patches = process.doMigrations(MockBuilder.getPatchInfoStore(currentPatchLevel), context);
        assertEquals(0, patches);
        assertEquals(0, getMigrationStartedCount());
        assertEquals(0, getMigrationSuccessCount());
    }

    /**
     * Make sure we get notified of patch application
     *
     * @throws Exception if anything goes wrong
     */
    public void testDistributedMigrationEvents() throws Exception
    {
        // There should be five listener on the main process
        //  1) the distributed launcher
        //  2) this test object
        //  3-5) the three sub-launchers
        assertEquals(5, launcher.getMigrationProcess().getListeners().size());

        // The sub-MigrationProcesses should have one listener each - the sub-launcher
        HashMap controlledSystems =
                ((DistributedMigrationProcess) launcher.getMigrationProcess()).getControlledSystems();

        for (Iterator controlledSystemIter = controlledSystems.keySet().iterator();
             controlledSystemIter.hasNext(); )
        {
            String controlledSystemName = (String) controlledSystemIter.next();
            JdbcMigrationLauncher subLauncher =
                    (JdbcMigrationLauncher) controlledSystems.get(controlledSystemName);
            MigrationProcess subProcess = subLauncher.getMigrationProcess();
            assertEquals(1, subProcess.getListeners().size());
        }

        // Now do the migrations, and make sure we get the right number of events
        DistributedMigrationProcess process = (DistributedMigrationProcess) launcher.getMigrationProcess();
        int currentPatchlevel = 3;
        setReportedPatchLevel(process.getControlledSystems().values(), currentPatchlevel);
        int patches = process.doMigrations(MockBuilder.getPatchInfoStore(currentPatchlevel), context);
        assertEquals(4, patches);
        assertEquals(4, getMigrationStartedCount());
        assertEquals(4, getMigrationSuccessCount());
    }

    /**
     * Make sure we the right patches go in the right spot
     *
     * @throws Exception if anything goes wrong
     */
    public void testDistributedMigrationContextTargetting() throws Exception
    {
        int currentPatchLevel = 3;
        HashMap controlledSystems =
                ((DistributedMigrationProcess) launcher.getMigrationProcess()).getControlledSystems();
        // set the patch info store to report the current patch level
        setReportedPatchLevel(controlledSystems.values(), currentPatchLevel);
        // Now do the migrations, and make sure we get the right number of events
        MigrationProcess process = launcher.getMigrationProcess();
        process.setMigrationRunnerStrategy(new OrderedMigrationRunnerStrategy());
        process.doMigrations(MockBuilder.getPatchInfoStore(currentPatchLevel), context);

        // The orders schema has four tasks that should go, make sure they did
        JdbcMigrationLauncher ordersLauncher =
                (JdbcMigrationLauncher) controlledSystems.get("orders");
        // FIXME need to test multiple contexts
        TestDataSourceMigrationContext ordersContext =
                (TestDataSourceMigrationContext)
                        ordersLauncher.getContexts().keySet().iterator().next();
        assertEquals("orders", ordersContext.getSystemName());
        assertTrue(ordersContext.hasExecuted("TestTask1"));
        assertTrue(ordersContext.hasExecuted("TestTask2"));
        assertTrue(ordersContext.hasExecuted("TestTask3"));
        assertTrue(ordersContext.hasExecuted("TestTask4"));

        // The core schema has three tasks that should not go, make sure they exist but did not go
        JdbcMigrationLauncher coreLauncher =
                (JdbcMigrationLauncher) controlledSystems.get("core");
        // FIXME need to test multiple contexts
        TestDataSourceMigrationContext coreContext =
                (TestDataSourceMigrationContext) coreLauncher.getContexts().keySet().iterator().next();
        assertEquals(3, coreLauncher.getMigrationProcess().getMigrationTasks().size());
        assertEquals("core", coreContext.getSystemName());
        assertFalse(coreContext.hasExecuted("patch0001_first_patch"));
        assertFalse(coreContext.hasExecuted("patch0002_second_patch"));
        assertFalse(coreContext.hasExecuted("patch0003_third_patch"));
    }

    public void testShouldSetMigrationStrategyToDistributedJdbcMigrationLauncherFromProperties( ) throws MigrationException {
        DistributedJdbcMigrationLauncherFactory factory = new DistributedJdbcMigrationLauncherFactory();
        IMocksControl control = createNiceControl();
        DistributedJdbcMigrationLauncher distributedLauncher = control.createMock(DistributedJdbcMigrationLauncher.class);

        String systemName="mysystem";
        String propertyFileName="migration.properties";
        Properties properties = MockBuilder.getPropertiesWithDistributedSystemConfiguration("mysystem", "mystrategy", "orders");
        distributedLauncher.setMigrationStrategy("mystrategy");
        DistributedMigrationProcess migrationProcess=new DistributedMigrationProcess();
        expect(distributedLauncher.getMigrationProcess() ).andReturn(migrationProcess).anyTimes();
        control.replay();

        factory.configureFromMigrationProperties(distributedLauncher, systemName,properties, propertyFileName);

        control.verify();
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
