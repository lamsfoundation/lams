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
import com.tacitknowledge.util.migration.tasks.rollback.*;
import junit.framework.TestCase;
import org.easymock.IMocksControl;

import java.util.ArrayList;
import java.util.List;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createControl;

/**
 * Test the {@link OrderedMigrationRunnerStrategy} class.
 *
 * @author Oscar Gonzalez (oscar@tacitknowledge.com)
 * @author Hemri Herrera (hemri@tacitknowledge.com)
 * @author Ulises Pulido (upulido@tacitknowledge.com)
 */
public class OrderedMigrationRunnerStrategyTest extends TestCase
{

    private MigrationRunnerStrategy migrationRunnerStrategy;
    private List<MigrationTask> allMigrationTasks;
    private PatchInfoStore currentPatchInfoStore;

    public void setUp() throws Exception
    {
        super.setUp();
        migrationRunnerStrategy = new OrderedMigrationRunnerStrategy();
        allMigrationTasks = new ArrayList<MigrationTask>();
        IMocksControl mockControl = createControl();
        currentPatchInfoStore = mockControl.createMock(PatchInfoStore.class);
        allMigrationTasks.add(new TestRollbackableTask1());
        allMigrationTasks.add(new TestRollbackableTask2());
        allMigrationTasks.add(new TestRollbackableTask3());
        allMigrationTasks.add(new TestRollbackableTask4());
        allMigrationTasks.add(new TestRollbackableTask5());
        expect(currentPatchInfoStore.getPatchLevel()).andReturn(12);
        mockControl.replay();

    }

    public void testShouldMigrationsRunInOrder() throws MigrationException
    {
        PatchInfoStore patchInfoStore = MockBuilder.getPatchInfoStore(2);

        assertTrue("Should be able to run migration if current level is below migration level",
                migrationRunnerStrategy.shouldMigrationRun(3, patchInfoStore));

    }

    public void testShouldMigrationFailIfCurrentLevelIsAboveMigrationLevel() throws MigrationException
    {
        PatchInfoStore patchInfoStore = MockBuilder.getPatchInfoStore(3);
        assertFalse("Should not be able to run migration if current level is above migration level",
                migrationRunnerStrategy.shouldMigrationRun(2, patchInfoStore));

    }

    public void testShouldMigrationFailIfCurrentAndMigrationLevelAreEquals() throws MigrationException
    {

        PatchInfoStore patchInfoStore = MockBuilder.getPatchInfoStore(3);
        assertFalse("Should not be able to run migration if current level and migration level are equal",
                migrationRunnerStrategy.shouldMigrationRun(3, patchInfoStore));
    }

    public void testSystemIsSynchronized() throws MigrationException
    {

        PatchInfoStore patchInfoStore = MockBuilder.getPatchInfoStore(3);
        PatchInfoStore currentPatchInfoStore = MockBuilder.getPatchInfoStore(3);

        boolean systemSync = migrationRunnerStrategy.isSynchronized(currentPatchInfoStore, patchInfoStore);

        assertTrue("System should be synchronized", systemSync);
    }

    public void testSystemIsNotSynchronized() throws MigrationException
    {
        PatchInfoStore patchInfoStore = MockBuilder.getPatchInfoStore(4);
        PatchInfoStore currentPatchInfoStore = MockBuilder.getPatchInfoStore(3);


        boolean systemSync = migrationRunnerStrategy.isSynchronized(currentPatchInfoStore, patchInfoStore);

        assertFalse("System shouldn't be synchronized", systemSync);
    }

    public void testShouldMigrationThrowIllegalArgumentExceptionIfPatchInfoStoreParametersAreNullWhenIsSync() throws MigrationException
    {

        try
        {
            migrationRunnerStrategy.isSynchronized(null, null);
            fail("If arguments are null an Illegal Argument Exception should have been thrown");
        } catch (IllegalArgumentException exception)
        {

        }

    }

    public void testGetRollbackCandidatesAction() throws MigrationException
    {

        int[] rollbackLevels = new int[]{9};

        List<MigrationTask> rollbackCandidates = migrationRunnerStrategy.getRollbackCandidates(allMigrationTasks, rollbackLevels, currentPatchInfoStore);

        assertEquals("Expected rollback candidates should be 3", 3, rollbackCandidates.size());

    }

    public void testGetRollbackCandidatesRollbackLevelsShouldContainOnlyOneLevel() throws MigrationException
    {

        int[] rollbackLevels = new int[]{9, 10};
        try
        {
            List<MigrationTask> rollbackCandidates = migrationRunnerStrategy.getRollbackCandidates(allMigrationTasks, rollbackLevels, currentPatchInfoStore);
            fail("MigrationException is expected due to the strategy does not support more than one rollbackLevel");
        } catch (MigrationException exception)
        {

        }
    }

    public void testGetRollbackCandidatesRollbackLevelShouldBeLowerThanCurrentPatchLevel()
    {
        int[] rollbackLevels = new int[]{13};

        try
        {
            List<MigrationTask> rollbackCandidates = migrationRunnerStrategy.getRollbackCandidates(allMigrationTasks, rollbackLevels, currentPatchInfoStore);
            fail("The rollbackLevel should be lower than the currentPatchLevel");
        } catch (MigrationException e)
        {

        }
    }

    public void testGetRollbackCandidatesRollbackLevelShouldNotBeNull()
    {
        int[] rollbackLevels = null;

        try
        {
            List<MigrationTask> rollbackCandidates = migrationRunnerStrategy.getRollbackCandidates(allMigrationTasks, rollbackLevels, currentPatchInfoStore);
            fail("The rollbackLevel should not be null");
        } catch (MigrationException e)
        {

        }
    }

    public void testGetRollbackCandidatesRollbackLevelShouldNotBeEmpty()
    {
        int[] rollbackLevels = new int[]{};

        try
        {
            List<MigrationTask> rollbackCandidates = migrationRunnerStrategy.getRollbackCandidates(allMigrationTasks, rollbackLevels, currentPatchInfoStore);
            fail("The rollbackLevel should not be empty");
        } catch (MigrationException e)
        {

        }
    }

}
