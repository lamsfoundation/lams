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
import com.tacitknowledge.util.migration.tasks.normal.TestMigrationTask1;
import com.tacitknowledge.util.migration.tasks.normal.TestMigrationTask2;
import com.tacitknowledge.util.migration.tasks.normal.TestMigrationTask3;
import com.tacitknowledge.util.migration.tasks.normal.TestMigrationTask4;
import com.tacitknowledge.util.migration.tasks.rollback.*;
import junit.framework.TestCase;
import org.easymock.EasyMock;
import org.easymock.MockControl;
import org.easymock.classextension.IMocksControl;

import java.util.*;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createControl;

/**
 * Test the {@link MissingPatchMigrationRunnerStrategy} class.
 *
 * @author Hemri Herrera (hemri@tacitknowledge.com)
 * @author Ulises Pulido (upulido@tacitknowledge.com)
 */
public class MissingPatchMigrationRunnerStrategyTest extends TestCase {

    private MissingPatchMigrationRunnerStrategy strategy;
    private MockControl patchInfoStoreControl;
    private PatchInfoStore patchInfoStore;
    private List<MigrationTask> allMigrationTasks;
    private TestRollbackableTask2 rollbackableTask2;
    private TestRollbackableTask4 rollbackableTask4;
    private static final int[] ROLLBACK_LEVELS = new int[]{9, 11};
    private IMocksControl mockControl;
    private PatchInfoStore currentPatchInfoStore;

    protected void setUp() throws Exception {
        strategy = new MissingPatchMigrationRunnerStrategy();
        patchInfoStoreControl = MockControl.createControl(PatchInfoStore.class);
        patchInfoStore = (PatchInfoStore) patchInfoStoreControl.getMock();
        allMigrationTasks = new ArrayList<MigrationTask>();
        rollbackableTask2 = new TestRollbackableTask2();
        rollbackableTask4 = new TestRollbackableTask4();
        mockControl = createControl();
        currentPatchInfoStore = mockControl.createMock(PatchInfoStore.class);

        allMigrationTasks.add(new TestRollbackableTask1());
        allMigrationTasks.add(rollbackableTask2);
        allMigrationTasks.add(new TestRollbackableTask3());
        allMigrationTasks.add(rollbackableTask4);
        allMigrationTasks.add(new TestRollbackableTask5());
    }

    public void testShouldMigrationRunReturnsTrueIfPatchWasNotApplied() throws MigrationException {
        patchInfoStoreControl.expectAndReturn(patchInfoStore.isPatchApplied(5), false);
        patchInfoStoreControl.replay();
        boolean actualResult = strategy.shouldMigrationRun(5, patchInfoStore);
        assertTrue("The patch was already applied.", actualResult);
    }

    public void testShouldMigrationRunReturnsFalseIfPatchWasApplied() throws MigrationException {
        patchInfoStoreControl.expectAndReturn(patchInfoStore.isPatchApplied(4), true);
        patchInfoStoreControl.replay();
        boolean actualResult = strategy.shouldMigrationRun(4, patchInfoStore);
        assertFalse("The patch was not applied.", actualResult);
    }

    public void testShouldMigrationThrowIllegalArgumentExceptionIfPatchInfoStoreParameterIsNull() throws MigrationException {

        try {

            strategy.shouldMigrationRun(3, null);
            fail("If parameter Is null an Illegal Argument Exception should have been thrown");

        } catch (IllegalArgumentException exception) {

        }

    }

    public void testSystemIsSynchronized() throws MigrationException {
        Set<Integer> patchInfoStorePatches = new HashSet<Integer>();
        patchInfoStorePatches.add(1);
        PatchInfoStore patchInfoStore = MockBuilder.getPatchInfoStore(3, patchInfoStorePatches);

        Set<Integer> currentPatchInfoStorePatches = new HashSet<Integer>();
        currentPatchInfoStorePatches.add(1);
        PatchInfoStore currentPatchInfoStore = MockBuilder.getPatchInfoStore(3, currentPatchInfoStorePatches);
        boolean systemSync = strategy.isSynchronized(currentPatchInfoStore, patchInfoStore);
        assertTrue("System should be synchronized", systemSync);
    }


    public void testSystemIsNotSynchronized() throws MigrationException {
        Set<Integer> patchInfoStorePatches = new HashSet<Integer>();
        patchInfoStorePatches.add(12);
        PatchInfoStore patchInfoStore = MockBuilder.getPatchInfoStore(3, patchInfoStorePatches);

        Set<Integer> currentPatchInfoStorePatches = new HashSet<Integer>();
        currentPatchInfoStorePatches.add(1);
        PatchInfoStore currentPatchInfoStore = MockBuilder.getPatchInfoStore(3, currentPatchInfoStorePatches);
        boolean systemSync = strategy.isSynchronized(currentPatchInfoStore, patchInfoStore);
        assertFalse("System shouldn't be synchronized", systemSync);
    }

    public void testShouldMigrationThrowIllegalArgumentExceptionIfPatchInfoStoreParametersAreNullWhenIsSync() throws MigrationException {

        try {
            strategy.isSynchronized(null, null);
            fail("If arguments are null an Illegal Argument Exception should have been thrown");
        } catch (IllegalArgumentException exception) {

        }

    }


    public void testGetRollbackCandidatesAction() throws MigrationException {

        expect(currentPatchInfoStore.isPatchApplied(9)).andReturn(true);
        expect(currentPatchInfoStore.isPatchApplied(11)).andReturn(true);
        mockControl.replay();

        List<MigrationTask> rollbackCandidates = strategy.getRollbackCandidates(allMigrationTasks, ROLLBACK_LEVELS, currentPatchInfoStore);

        assertEquals("There should be 2 tasks to be rolledback", 2, rollbackCandidates.size());
        assertTrue("Task 2 should be a candidate", rollbackCandidates.contains(rollbackableTask2));
        assertTrue("Task 4 should be a candidate", rollbackCandidates.contains(rollbackableTask4));

    }

    public void testGetRollbackCandidatesIfPatchNotAppliedItShouldNotBeMarkedAsCandidate() throws MigrationException {

        expect(currentPatchInfoStore.isPatchApplied(9)).andReturn(true);
        expect(currentPatchInfoStore.isPatchApplied(11)).andReturn(false);
        mockControl.replay();

        List<MigrationTask> rollbackCandidates = strategy.getRollbackCandidates(allMigrationTasks, ROLLBACK_LEVELS, currentPatchInfoStore);

        assertEquals("There should be one tasks to be rolledback", 1, rollbackCandidates.size());
        assertTrue("Task 2 should be a candidate", rollbackCandidates.contains(rollbackableTask2));
        assertFalse("Task 4 should be a candidate", rollbackCandidates.contains(rollbackableTask4));

    }

    public void testGetRollbackCandidatesRollbackLevelShouldNotBeNull() {

        try {
            strategy.getRollbackCandidates(allMigrationTasks, null, currentPatchInfoStore);
            fail("An Exception should have been thrown due to rollback levels being null");
        } catch (MigrationException e) {
            //expected
        }
    }

    public void testGetRollbackCandidatesRollbackLevelShouldNotBeEmpty() {

        try {
            strategy.getRollbackCandidates(allMigrationTasks, new int[]{}, currentPatchInfoStore);
            fail("An Exception should have been thrown due to rollback levels being empty");
        } catch (MigrationException e) {
            //expected
        }
    }


}
