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

import java.util.List;

import com.tacitknowledge.util.migration.builders.MockBuilder;
import com.tacitknowledge.util.migration.tasks.normal.TestMigrationTask2;
import com.tacitknowledge.util.migration.tasks.normal.TestMigrationTask3;
import org.easymock.MockControl;

/**
 * Test basic migration functionality
 * 
 * @author  Scott Askew (scott@tacitknowledge.com)
 */
public class MigrationTest extends MigrationListenerTestBase
{
    /** The class under test */
    private MigrationProcess runner = null;
    
    /** Test migration context */
    private TestMigrationContext context = null;
    private MockControl patchInfoStoreControl;
    private PatchInfoStore patchInfoStore;

    /**
     * Constructor for MigrationTest.
     * 
     * @param name the name of the test to run
     */
    public MigrationTest(String name)
    {
        super(name);
    }

    /**
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception
    {
        super.setUp();
        runner = new MigrationProcess();
        runner.setMigrationRunnerStrategy(MigrationRunnerFactory.getMigrationRunnerStrategy(null));
        runner.addPatchResourceDirectory(getClass().getPackage().getName() + ".tasks.normal");
        runner.addPatchResourceDirectory(getClass().getPackage().getName() + ".tasks.rollback");
        runner.addPostPatchResourceDirectory(getClass().getPackage().getName() + ".tasks.post");
        runner.addListener(this);
        context = new TestMigrationContext();
        patchInfoStoreControl = MockControl.createStrictControl(PatchInfoStore.class);
        patchInfoStore = (PatchInfoStore) patchInfoStoreControl.getMock();
    }

    /**
     * @see junit.framework.TestCase#tearDown()
     */
    protected void tearDown() throws Exception
    {
        super.tearDown();
        // Reset the "fail" bit that may have been set on a previous run.
        TestMigrationTask2.reset();
        TestMigrationTask3.setFail(false);
    }
    
    /**
     * Validates that the migration runner can run the all patches.
     * 
     * @throws MigrationException if an unexpected error occurs
     */
    public void testRunAllMigrationTasks() throws MigrationException
    {
        List l = runner.getMigrationTasks();
        assertEquals(9, l.size());

        patchInfoStoreControl.expectAndReturn(patchInfoStore.getPatchLevel(), 0, MockControl.ONE_OR_MORE);
        patchInfoStoreControl.replay();

        int level = runner.doMigrations(patchInfoStore, context);
        runner.doPostPatchMigrations(context);
        assertEquals(9, level);
        assertTrue(context.hasExecuted("TestTask1"));
        assertTrue(context.hasExecuted("TestTask2"));
        assertTrue(context.hasExecuted("TestTask3"));
        assertTrue(context.hasExecuted("TestTask4"));
        assertTrue(context.hasExecuted("TestRollbackableTask1"));
        assertTrue(context.hasExecuted("TestRollbackableTask2"));
        assertTrue(context.hasExecuted("TestRollbackableTask3"));
        assertTrue(context.hasExecuted("TestRollbackableTask4"));
        assertTrue(context.hasExecuted("TestRollbackableTask5"));
        assertTrue(context.hasExecuted("TestPostTask1"));
        assertTrue(context.hasExecuted("TestPostTask2"));
        assertEquals(9, getMigrationStartedCount());
        assertEquals(9, getMigrationSuccessCount());
    }
    
    /**
     * Validates that a re-run won't run any patches
     * 
     * @throws MigrationException if an unexpected error occurs
     */
    public void testReRunAllMigrationTasks() throws MigrationException
    {
        List l = runner.getMigrationTasks();
        assertEquals(9, l.size());
        
        // run them all once
        patchInfoStoreControl.expectAndReturn(patchInfoStore.getPatchLevel(), 0, MockControl.ONE_OR_MORE);
        patchInfoStoreControl.replay();

        int level = runner.doMigrations(patchInfoStore, context);
        runner.doPostPatchMigrations(context);
        assertEquals(9, level);
        assertTrue(context.hasExecuted("TestTask1"));
        assertTrue(context.hasExecuted("TestTask2"));
        assertTrue(context.hasExecuted("TestTask3"));
        assertTrue(context.hasExecuted("TestTask4"));
        assertTrue(context.hasExecuted("TestRollbackableTask1"));
        assertTrue(context.hasExecuted("TestRollbackableTask2"));
        assertTrue(context.hasExecuted("TestRollbackableTask3"));
        assertTrue(context.hasExecuted("TestRollbackableTask4"));
        assertTrue(context.hasExecuted("TestRollbackableTask5"));
        assertTrue(context.hasExecuted("TestPostTask1"));
        assertTrue(context.hasExecuted("TestPostTask2"));
        assertEquals(9, getMigrationStartedCount());
        assertEquals(9, getMigrationSuccessCount());
        
        // now re-run them and see what happens
        setMigrationStartedCount(0);
        setMigrationFailedCount(0);
        setMigrationSuccessCount(0);

        patchInfoStoreControl.reset();
        patchInfoStoreControl.expectAndReturn(patchInfoStore.getPatchLevel(), 15, MockControl.ONE_OR_MORE);
        patchInfoStoreControl.replay();

        level = runner.doMigrations(patchInfoStore, context);
        runner.doPostPatchMigrations(context);
        assertEquals(0, level);
        assertTrue(context.hasExecuted("TestTask1"));
        assertTrue(context.hasExecuted("TestTask2"));
        assertTrue(context.hasExecuted("TestTask3"));
        assertTrue(context.hasExecuted("TestTask4"));
        assertTrue(context.hasExecuted("TestRollbackableTask1"));
        assertTrue(context.hasExecuted("TestRollbackableTask2"));
        assertTrue(context.hasExecuted("TestRollbackableTask3"));
        assertTrue(context.hasExecuted("TestRollbackableTask4"));
        assertTrue(context.hasExecuted("TestRollbackableTask5"));
        assertTrue(context.hasExecuted("TestPostTask1"));
        assertTrue(context.hasExecuted("TestPostTask2"));
        assertEquals(0, getMigrationStartedCount());
        assertEquals(0, getMigrationSuccessCount());
    }

    /**
     * Validates that the migration runner will only run the necessary patches
     * to bring the system current 
     * 
     * @throws MigrationException if an unexpected error occurs
     */
    public void testRunPartialMigrationTasks() throws MigrationException
    {
        List l = runner.getMigrationTasks();
        assertEquals(9, l.size());

        patchInfoStoreControl.expectAndReturn(patchInfoStore.getPatchLevel(), 9, MockControl.ONE_OR_MORE);
        patchInfoStoreControl.replay();

        int level = runner.doMigrations(patchInfoStore, context);
        runner.doPostPatchMigrations(context);
        assertEquals(3, level);
        assertFalse(context.hasExecuted("TestTask1"));
        assertFalse(context.hasExecuted("TestTask2"));
        assertFalse(context.hasExecuted("TestTask3"));
        assertFalse(context.hasExecuted("TestTask4"));
        assertFalse(context.hasExecuted("TestRollbackableTask1"));
        assertFalse(context.hasExecuted("TestRollbackableTask2"));
        assertTrue(context.hasExecuted("TestRollbackableTask3"));
        assertTrue(context.hasExecuted("TestRollbackableTask4"));
        assertTrue(context.hasExecuted("TestRollbackableTask5"));
        assertTrue(context.hasExecuted("TestPostTask1"));
        assertTrue(context.hasExecuted("TestPostTask2"));
        assertEquals(3, getMigrationStartedCount());
        assertEquals(3, getMigrationSuccessCount());
    }

    /**
     * Validates that the migration runner will handle busted Migrations ok
     * 
     * @throws MigrationException if an unexpected error occurs
     */
    public void testRunBrokenMigrationTasks() throws MigrationException
    {
        List l = runner.getMigrationTasks();
        TestMigrationTask3.setFail(true);
        assertEquals(9, l.size());
        
        int executedTasks = 0;
        try
        {
            patchInfoStoreControl.expectAndReturn(patchInfoStore.getPatchLevel(), 5, MockControl.ONE_OR_MORE);
            patchInfoStoreControl.replay();
            executedTasks = runner.doMigrations(patchInfoStore, context);
            runner.doPostPatchMigrations(context);
            fail("We called a migration that failed, this should have thrown an exception");
        }
        catch (MigrationException me)
        {
            // we expect this
        }
        assertEquals(0, executedTasks);
        assertFalse(context.hasExecuted("TestTask1"));
        assertFalse(context.hasExecuted("TestTask2"));
        assertFalse(context.hasExecuted("TestTask3"));
        assertFalse(context.hasExecuted("TestTask4"));
        assertFalse(context.hasExecuted("TestRollbackableTask1"));
        assertFalse(context.hasExecuted("TestRollbackableTask2"));
        assertFalse(context.hasExecuted("TestRollbackableTask3"));
        assertFalse(context.hasExecuted("TestRollbackableTask4"));
        assertFalse(context.hasExecuted("TestRollbackableTask5"));
        assertFalse(context.hasExecuted("TestPostTask1"));
        assertFalse(context.hasExecuted("TestPostTask2"));
        assertEquals(1, getMigrationStartedCount());
        assertEquals(0, getMigrationSuccessCount());
        assertEquals(1, getMigrationFailedCount());
    }
    
    /**
     * Verifies that the migration runner checks for patch level uniqueness
     * between migration tasks
     */
    public void testMigrationPatchLevelUniqueness()
    {
        try
        {
            TestMigrationTask2.setPatchLevelOverride(new Integer(7));
            runner.doMigrations(MockBuilder.getPatchInfoStore(0), context);
            fail("Expected a MigrationException due to a task patch level conflict");
        }
        catch (MigrationException e)
        {
            assertTrue("Expected a patch conflict migration exception, not \""
                + e.getMessage() + "\"",
                e.getMessage().indexOf("conflicting patch level") > -1);
        }
    }

    /**
     * Validates that the migration runner will flag tasks with null patch levels
     */
    public void testNullPatchLevel()
    {
        try
        {
            TestMigrationTask2.setPatchLevelOverride(null);
            runner.doMigrations(MockBuilder.getPatchInfoStore(0), new TestMigrationContext());
            fail("Expected a MigrationException due to a null patch level");
        }
        catch (MigrationException e)
        {
            assertTrue("Expected a patch level missing exception, not \""
                + e.getMessage() + "\"",
                e.getMessage().indexOf("patch level defined") > -1);
        }
    }
    
    /**
     * Validates <code>Migration.getNextPatchLevel</code>.
     * 
     * @throws MigrationException if an unexpected error occurs
     */
    public void testGetNextPatchLevel() throws MigrationException
    {
        int level = runner.getNextPatchLevel();
        assertEquals(13, level);
    } 
}
