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
import com.tacitknowledge.util.migration.tasks.rollback.*;
import junit.framework.TestCase;

import org.easymock.EasyMock;
import org.easymock.classextension.IMocksControl;
import org.easymock.MockControl;

import com.tacitknowledge.util.migration.jdbc.JdbcMigrationContext;
import com.tacitknowledge.util.migration.jdbc.JdbcMigrationLauncher;

import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createControl;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.createStrictControl;

/**
 * Test the {@link DistributedMigrationProcess} class.
 *
 * @author Alex Soto (apsoto@gmail.com)
 */
public class DistributedMigrationProcessTest extends TestCase
{
    private static final int CURRENT_PATCH_LEVEL = 4;
    /** class under test */
    private DistributedMigrationProcess migrationProcess = null;
    private PatchInfoStore currentPatchInfoStore;
    private IMocksControl migrationRunnerStrategyControl;
    private MigrationRunnerStrategy migrationRunnerStrategy;


    /**
     * Setup our tests.
     */
    protected void setUp() throws Exception
    {
        super.setUp();
        migrationProcess = new DistributedMigrationProcess();
        currentPatchInfoStore = MockBuilder.getPatchInfoStore(CURRENT_PATCH_LEVEL);
        migrationRunnerStrategyControl = createStrictControl();
        migrationRunnerStrategy = migrationRunnerStrategyControl.createMock(MigrationRunnerStrategy.class);
    }
    
    protected HashMap createSystems()
    {
        HashMap systems = new HashMap();
        String systemNames[] = {"system1", "system2"};
        
        for(int i = 0; i < systemNames.length; i++)
        {
            //JDBCMi
            
        }
        
        return systems;
    }
    
    public void testValidateControlledSystemsWhenNodePatchLevelsAreInSync() throws Exception
    {

        // system has one node
        String systemName = "system1";
        JdbcMigrationLauncher launcher = new JdbcMigrationLauncher();
        
        MockControl contextControl = MockControl.createControl(JdbcMigrationContext.class);
        
        JdbcMigrationContext context = (JdbcMigrationContext) contextControl.getMock();
        MockControl patchInfoStoreControl = MockControl.createControl(PatchInfoStore.class);
        PatchInfoStore patchInfoStore = (PatchInfoStore) patchInfoStoreControl.getMock();

        expect(migrationRunnerStrategy.isSynchronized(currentPatchInfoStore, patchInfoStore)).andReturn(true);

        migrationRunnerStrategyControl.replay();

        migrationProcess.setMigrationRunnerStrategy(migrationRunnerStrategy);

        // create the launcher's contexts collection
        LinkedHashMap contexts = new LinkedHashMap();
        contexts.put(context, patchInfoStore);
        launcher.setContexts(contexts);
        
        HashMap controlledSystems = new HashMap();
        controlledSystems.put(systemName, launcher);
        
        migrationProcess.setControlledSystems(controlledSystems);
        
        try
        {
            migrationProcess.validateControlledSystems(currentPatchInfoStore);
        }
        catch(Exception e)
        {
            fail("Unexpected exception when validating controlled systems.");
        }
    }
    
    public void testValidateControlledSystemsWhenNodePatchLevelsAreOutOfSync() throws Exception
    {
        // system has one node
        String systemName = "system1";
        JdbcMigrationLauncher launcher = new JdbcMigrationLauncher();
        
        // first node is at the 'current' patch level
        MockControl node1ContextControl = MockControl.createControl(JdbcMigrationContext.class);
        JdbcMigrationContext node1Context = (JdbcMigrationContext) node1ContextControl.getMock();
        MockControl node1PatchInfoStoreControl = MockControl.createControl(PatchInfoStore.class);
        PatchInfoStore node1PatchInfoStore = (PatchInfoStore) node1PatchInfoStoreControl.getMock();
        // setup mock patch info store to return the patch level we want
        node1Context.getDatabaseName();
        node1ContextControl.setReturnValue("node1", MockControl.ONE_OR_MORE);
        node1ContextControl.replay();

        // second node simulates a newly added database instance, it has not been patched
        MockControl node2ContextControl = MockControl.createControl(JdbcMigrationContext.class);
        JdbcMigrationContext node2Context = (JdbcMigrationContext) node2ContextControl.getMock();
        MockControl node2PatchInfoStoreControl = MockControl.createControl(PatchInfoStore.class);
        PatchInfoStore node2PatchInfoStore = (PatchInfoStore) node2PatchInfoStoreControl.getMock();
        // setup mock patch info store to return the patch level we want
        node2Context.getDatabaseName();
        node2ContextControl.setReturnValue("node2", MockControl.ONE_OR_MORE);
        node2ContextControl.replay();

        // create the launcher's contexts collection
        LinkedHashMap contexts = new LinkedHashMap();
        contexts.put(node1Context, node1PatchInfoStore);
        contexts.put(node2Context, node2PatchInfoStore);
        launcher.setContexts(contexts);
        
        HashMap controlledSystems = new HashMap();
        controlledSystems.put(systemName, launcher);
        
        migrationProcess.setControlledSystems(controlledSystems);
        expect(migrationRunnerStrategy.isSynchronized(currentPatchInfoStore, node1PatchInfoStore)).andReturn(true);
        expect(migrationRunnerStrategy.isSynchronized(currentPatchInfoStore, node2PatchInfoStore)).andReturn(false);

        migrationRunnerStrategyControl.replay();

        migrationProcess.setMigrationRunnerStrategy(migrationRunnerStrategy);
        try
        {
            migrationProcess.validateControlledSystems(currentPatchInfoStore);
            fail("Unexpected exception when validating controlled systems.");
        }
        catch(MigrationException me)
        {
        }
        catch(Exception e)
        {
            fail("Unexpected exception when validating controlled systems.");
        }
    }


    public void testDoRollbacksActionMockingAsIfOrderedStrategyWereUsed() throws MigrationException {

        String systemName = "system1";
        JdbcMigrationLauncher launcher = new JdbcMigrationLauncher();
        int[] rollbackLevels = new int[]{9};
        IMocksControl mockControl = createControl();

        JdbcMigrationContext migrationContextMock = mockControl.createMock(JdbcMigrationContext.class);
        MigrationRunnerStrategy migrationRunnerStrategyMock = mockControl.createMock(MigrationRunnerStrategy.class);

        TestRollbackableTask3 rollbackableTask3 = new TestRollbackableTask3();
        TestRollbackableTask4 rollbackableTask4 = new TestRollbackableTask4();
        TestRollbackableTask5 rollbackableTask5 = new TestRollbackableTask5();


        List<MigrationTask> migrationTaskList = new ArrayList<MigrationTask>();
        migrationTaskList.add(new TestRollbackableTask1());
        migrationTaskList.add(new TestRollbackableTask2());
        migrationTaskList.add(rollbackableTask3);
        migrationTaskList.add(rollbackableTask4);
        migrationTaskList.add(rollbackableTask5);

        MigrationProcess migrationProcessMock = mockControl.createMock(MigrationProcess.class);
        expect(migrationProcessMock.getMigrationTasks()).andReturn(migrationTaskList);

        HashMap controlledSystems = new HashMap();
        controlledSystems.put(systemName, launcher);
        launcher.setMigrationProcess(migrationProcessMock);

        PatchInfoStore patchInfoStoreMock = mockControl.createMock(PatchInfoStore.class);
        expect(patchInfoStoreMock.getPatchLevel()).andReturn(12);

        expect(migrationRunnerStrategyMock.isSynchronized(eq(currentPatchInfoStore), EasyMock.<PatchInfoStore>anyObject())).andReturn(true).anyTimes();
        List<MigrationTask> rollbackCandidates = new ArrayList<MigrationTask>();
        rollbackCandidates.add(rollbackableTask5);
        rollbackCandidates.add(rollbackableTask4);
        rollbackCandidates.add(rollbackableTask3);

        expect(migrationRunnerStrategyMock.getRollbackCandidates(migrationTaskList, rollbackLevels, patchInfoStoreMock)).andReturn(rollbackCandidates);
        expect(migrationRunnerStrategyMock.getRollbackCandidates(rollbackCandidates, rollbackLevels, patchInfoStoreMock)).andReturn(Collections.EMPTY_LIST);

        mockControl.replay();

        DistributedMigrationProcess distributedMigrationProcess = new DistributedMigrationProcess();
        distributedMigrationProcess.setMigrationRunnerStrategy(migrationRunnerStrategyMock);
        distributedMigrationProcess.setControlledSystems(controlledSystems);


        boolean forceRollback=false;
        int rollbacksApplied = distributedMigrationProcess.doRollbacks(patchInfoStoreMock,rollbackLevels,migrationContextMock,forceRollback);

        assertEquals("Two rollbacks should be applied", 3, rollbacksApplied);
    }

}
