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

import com.tacitknowledge.util.migration.builders.MockBuilder;
import com.tacitknowledge.util.migration.tasks.rollback.TestRollbackableTask2;
import com.tacitknowledge.util.migration.tasks.rollback.TestRollbackableTask3;
import com.tacitknowledge.util.migration.tasks.rollback.TestRollbackableTask4;
import com.tacitknowledge.util.migration.tasks.rollback.TestRollbackableTask5;
import org.easymock.EasyMock;
import org.easymock.MockControl;
import org.easymock.classextension.IMocksControl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createStrictControl;

/**
 * This class defines unit tests for the Rollback functionality.
 *
 * @author Artie Pesh-Imam (apeshimam@tacitknowledge.com)
 */
public class MigrationProcessRollbackTest extends MigrationListenerTestBase
{
    /**
     * The class under test
     */
    private MigrationProcess runner = null;

    /**
     * Test migration context
     */
    private TestMigrationContext context = null;
    private MockControl patchInfoStoreControl;
    private PatchInfoStore patchInfoStore;
    private PatchInfoStore currentPatchInfoStore;
    private IMocksControl mockControl;
    private MigrationRunnerStrategy migrationStrategy;

    private static final int[] ROLLBACK_LEVELS = new int[]{8};

    /**
     * Constructor for MigrationProcessRollbackTest.
     *
     * @param name the name of the test to run
     */
    public MigrationProcessRollbackTest(String name)
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
        runner.addPatchResourceDirectory(getClass().getPackage().getName()
                + ".tasks.rollback");
        runner.addPostPatchResourceDirectory(getClass().getPackage().getName()
                + ".tasks.post");
        runner.addListener(this);
        context = new TestMigrationContext();
        patchInfoStoreControl = MockControl.createStrictControl(PatchInfoStore.class);
        patchInfoStore = (PatchInfoStore) patchInfoStoreControl.getMock();
        currentPatchInfoStore = MockBuilder.getPatchInfoStore(12);

        mockControl = createStrictControl();
        migrationStrategy = mockControl.createMock(MigrationRunnerStrategy.class);
        List<MigrationTask> rollbackCandidates;
        rollbackCandidates = new ArrayList<MigrationTask>();
        rollbackCandidates.add(new TestRollbackableTask5());
        rollbackCandidates.add(new TestRollbackableTask4());
        rollbackCandidates.add(new TestRollbackableTask3());
        rollbackCandidates.add(new TestRollbackableTask2());
        expect(migrationStrategy.getRollbackCandidates(EasyMock.<List<MigrationTask>>anyObject(), eq(ROLLBACK_LEVELS), eq(currentPatchInfoStore))).andReturn(rollbackCandidates);
        expect(migrationStrategy.getRollbackCandidates(EasyMock.<List<MigrationTask>>anyObject(), eq(ROLLBACK_LEVELS), eq(currentPatchInfoStore))).andReturn(Collections.EMPTY_LIST);

    }

    /**
     * @see junit.framework.TestCase#tearDown()
     */
    protected void tearDown() throws Exception
    {
        super.tearDown();
        // Reset the "fail" bit that may have been set on a previous run.
        // TestMigrationTask2.reset();
        // TestMigrationTask3.setFail(false);
    }

    /**
     * This method tests the basic rollback functionality.
     *
     * @throws MigrationException
     */
    public void testRollbackAllTasks() throws MigrationException
    {
        List migrationTasks = runner.getMigrationTasks();
        assertEquals(5, migrationTasks.size());

        patchInfoStoreControl.expectAndReturn(patchInfoStore.getPatchLevel(), 1, MockControl.ONE_OR_MORE);
        patchInfoStoreControl.replay();

        int level = runner.doMigrations(patchInfoStore, context);
        runner.doPostPatchMigrations(context);
        assertEquals(5, level);
        assertTrue(context.hasExecuted("TestRollbackableTask1"));
        assertTrue(context.hasExecuted("TestRollbackableTask2"));
        assertTrue(context.hasExecuted("TestRollbackableTask3"));
        assertTrue(context.hasExecuted("TestRollbackableTask4"));
        assertTrue(context.hasExecuted("TestRollbackableTask5"));

        // check that the migrations occurred successfully
        assertEquals(5, getMigrationStartedCount());
        assertEquals(5, getMigrationSuccessCount());

        // execute the rollback
        mockControl.replay();
        runner.setMigrationRunnerStrategy(migrationStrategy);
        level = runner.doRollbacks(currentPatchInfoStore, ROLLBACK_LEVELS, context, false);
        assertEquals(4, level);
        assertEquals(4, getRollbackSuccessCount());

    }

    /**
     * This method tests the basic rollback functionality.
     *
     * @throws MigrationException
     */
    public void testRollbackPartialTasks() throws MigrationException
    {
        List l = runner.getMigrationTasks();
        assertEquals(5, l.size());

        patchInfoStoreControl.expectAndReturn(patchInfoStore.getPatchLevel(), 8, MockControl.ONE_OR_MORE);
        patchInfoStoreControl.replay();

        int level = runner.doMigrations(patchInfoStore, context);
        runner.doPostPatchMigrations(context);
        assertEquals(4, level);
        assertEquals(4, getMigrationSuccessCount());

        assertFalse(context.hasExecuted("TestRollbackableTask1"));
        assertTrue(context.hasExecuted("TestRollbackableTask2"));
        assertTrue(context.hasExecuted("TestRollbackableTask3"));
        assertTrue(context.hasExecuted("TestRollbackableTask4"));
        assertTrue(context.hasExecuted("TestRollbackableTask5"));

        // execute the rollback
        mockControl.replay();
        runner.setMigrationRunnerStrategy(migrationStrategy);
        level = runner.doRollbacks(currentPatchInfoStore, ROLLBACK_LEVELS, context, false);
        assertEquals(4, level);
        assertEquals(4, getRollbackSuccessCount());
    }

    /**
     * This method tests the scneario when a non rollbackable task is attempted to rollback.
     * Note that in this scenario, the forceRollback is false.
     *
     * @throws MigrationException
     */
    public void testRollbackNotRollbackableTask() throws MigrationException
    {
        doInitialMigrations();

        // execute the rollback
        try
        {
            int[] rollbackLevels = new int[]{7};
            runner.doRollbacks(currentPatchInfoStore, rollbackLevels, context, false);
        } catch (MigrationException me)
        {
            // expecting exception
        }
        assertEquals(0, getRollbackSuccessCount());
    }

    /**
     * This tests the forceRollback functionality.
     *
     * @throws MigrationException
     */
    public void testForceRollback() throws MigrationException
    {
        doInitialMigrations();

        // execute the rollback
        try
        {
            int[] rollbackLevels = new int[]{7};
            runner.doRollbacks(currentPatchInfoStore, rollbackLevels, context, true);
        } catch (MigrationException me)
        {
            // expecting exception
        }
        assertEquals(5, getRollbackSuccessCount());
    }

    /**
     * this is a private helper method to perform initial migrations
     *
     * @throws MigrationException
     */
    private void doInitialMigrations() throws MigrationException
    {
        List l = runner.getMigrationTasks();
        assertEquals(5, l.size());

        patchInfoStoreControl.expectAndReturn(patchInfoStore.getPatchLevel(), 0, MockControl.ONE_OR_MORE);
        patchInfoStoreControl.replay();
        int level = runner.doMigrations(patchInfoStore, context);
        runner.doPostPatchMigrations(context);
        assertEquals(5, level);
        assertEquals(5, getMigrationSuccessCount());

        assertTrue(context.hasExecuted("TestRollbackableTask1"));
        assertTrue(context.hasExecuted("TestRollbackableTask2"));
        assertTrue(context.hasExecuted("TestRollbackableTask3"));
        assertTrue(context.hasExecuted("TestRollbackableTask4"));
        assertTrue(context.hasExecuted("TestRollbackableTask5"));
    }

    /**
     * this tests the scenario when the user tries to rollback to a level
     * which is greater than the current patch level.
     *
     * @throws MigrationException
     */
    public void testInvalidRollbackLevel() throws MigrationException
    {
        List l = runner.getMigrationTasks();
        assertEquals(5, l.size());

        patchInfoStoreControl.expectAndReturn(patchInfoStore.getPatchLevel(), 0, MockControl.ONE_OR_MORE);
        patchInfoStoreControl.replay();

        int level = runner.doMigrations(patchInfoStore, context);
        runner.doPostPatchMigrations(context);
        assertEquals(5, level);
        assertEquals(5, getMigrationSuccessCount());

        assertTrue(context.hasExecuted("TestRollbackableTask1"));
        assertTrue(context.hasExecuted("TestRollbackableTask2"));
        assertTrue(context.hasExecuted("TestRollbackableTask3"));
        assertTrue(context.hasExecuted("TestRollbackableTask4"));
        assertTrue(context.hasExecuted("TestRollbackableTask5"));


        try
        {
            PatchInfoStore patchInfoStoreBasedOnLevel = MockBuilder.getPatchInfoStore(level);
            int[] rollbackLevels = new int[]{7};
            level = runner.doRollbacks(patchInfoStoreBasedOnLevel, rollbackLevels, context, false);
        } catch (MigrationException me)
        {
            // expected
        }

    }

    /**
     * this test checks that the system correctly when a rollback is attempted
     * on a non-rollbackable task.
     *
     * @throws MigrationException
     */
    public void testMigrationTaskRollback() throws MigrationException
    {
        // add additional directory containing MigrationTask
        runner.addPatchResourceDirectory(getClass().getPackage().getName()
                + ".tasks.rollback.migrationtasks");
        List l = runner.getMigrationTasks();

        assertEquals(6, l.size());

        patchInfoStoreControl.expectAndReturn(patchInfoStore.getPatchLevel(), 7, MockControl.ONE_OR_MORE);
        patchInfoStoreControl.replay();

        int level = runner.doMigrations(patchInfoStore, context);
        runner.doPostPatchMigrations(context);
        assertEquals(6, level);
        assertEquals(6, getMigrationSuccessCount());
        assertTrue(context.hasExecuted("TestRollbackableTask1"));
        assertTrue(context.hasExecuted("TestRollbackableTask2"));
        assertTrue(context.hasExecuted("TestRollbackableTask3"));
        assertTrue(context.hasExecuted("TestRollbackableTask4"));
        assertTrue(context.hasExecuted("TestRollbackableTask5"));
        assertTrue(context.hasExecuted("TestMigrationTaskRollback1"));

        try
        {
            PatchInfoStore nestedPatchInfoStore = MockBuilder.getPatchInfoStore(13);
            int[] rollbackLevels = new int[]{12};
            level = runner.doRollbacks(nestedPatchInfoStore, rollbackLevels, context, false);
        } catch (MigrationException me)
        {
            // expected
        }
        assertEquals(0, level);
    }
}
