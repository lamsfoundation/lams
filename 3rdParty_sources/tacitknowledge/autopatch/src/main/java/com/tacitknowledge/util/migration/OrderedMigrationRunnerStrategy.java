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

import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Helps to get information about how the migrations should run based in an ordered stategy, i.e.
 * If the level of the <code>MigrationTask</code> is greater than the current level and
 * the <code>MigrationTask</code>has not yet been applied. Then the information of that migration
 * is considered missing.
 *
 * @author Oscar Gonzalez (oscar@tacitknowledge.com)
 * @author Hemri Herrera (hemri@tacitknowledge.com)
 * @author Ulises Pulido (upulido@tacitknowledge.com)
 */
public class OrderedMigrationRunnerStrategy implements MigrationRunnerStrategy
{
    public boolean shouldMigrationRun(int migrationLevel, PatchInfoStore patchInfoStore) throws MigrationException
    {
        return migrationLevel > patchInfoStore.getPatchLevel();
    }

    public boolean isSynchronized(PatchInfoStore currentPatchInfoStore, PatchInfoStore patchInfoStore) throws MigrationException
    {

        if (currentPatchInfoStore == null || patchInfoStore == null)
        {
            throw new IllegalArgumentException("currentPatchInfoStore and patchInfoStore should not be null");
        }

        return currentPatchInfoStore.getPatchLevel() == patchInfoStore.getPatchLevel();
    }

    public List<MigrationTask> getRollbackCandidates(List<MigrationTask> allMigrationTasks, int[] rollbackLevels, PatchInfoStore currentPatchInfoStore) throws MigrationException
    {
        validateRollbackLevel(rollbackLevels);

        int rollbackLevel = rollbackLevels[0];
        int currentPatchLevel = currentPatchInfoStore.getPatchLevel();

        if (currentPatchLevel < rollbackLevel)
        {
            throw new MigrationException(
                    "The rollback patch level cannot be greater than the current patch level");
        }

        PatchRollbackPredicate rollbackPredicate = new PatchRollbackPredicate(currentPatchLevel,
                rollbackLevel);
        List<MigrationTask> migrationCandidates = new ArrayList<MigrationTask>();
        migrationCandidates.addAll(allMigrationTasks);
        CollectionUtils.filter(migrationCandidates, rollbackPredicate);
        Collections.sort(migrationCandidates);
        // need to reverse the list do we apply the rollbacks in descending
        // order
        Collections.reverse(migrationCandidates);
        return migrationCandidates;

    }

    private void validateRollbackLevel(int[] rollbackLevels) throws MigrationException
    {
        if (rollbackLevels == null)
        {
            throw new MigrationException("rollbackLevels should not be null");
        }

        if (rollbackLevels.length == 0)
        {
            throw new MigrationException("rollbackLevels should not be empty");
        }

        if (rollbackLevels.length > 1)
        {
            throw new MigrationException("OrderedMigrationRunnerStrategy only supports one rollbackLevel");
        }
    }
}
