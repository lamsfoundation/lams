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


import org.apache.commons.lang.ArrayUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/*
 * @author Hemri Herrera (hemri@tacitknowledge.com)
 * @author Ulises Pulido (upulido@tacitknowledge.com)
 */

public class MissingPatchMigrationRunnerStrategy implements MigrationRunnerStrategy
{

    public boolean shouldMigrationRun(int migrationLevel, PatchInfoStore patchInfoStore) throws MigrationException
    {

        if (patchInfoStore == null)
        {
            throw new IllegalArgumentException("Patch Info Store should not be null");
        }

        return !patchInfoStore.isPatchApplied(migrationLevel);
    }

    public boolean isSynchronized(PatchInfoStore currentPatchInfoStore, PatchInfoStore patchInfoStore) throws MigrationException
    {

        if (currentPatchInfoStore == null || patchInfoStore == null)
        {
            throw new IllegalArgumentException("currentPatchInfoStore and patchInfoStore should not be null");
        }
        Set<Integer> currentPatchInfoStorePatchesApplied = currentPatchInfoStore.getPatchesApplied();
        Set<Integer> patchInfoStorePatchesApplied = patchInfoStore.getPatchesApplied();

        return currentPatchInfoStorePatchesApplied.equals(patchInfoStorePatchesApplied);
    }

    public List<MigrationTask> getRollbackCandidates(List<MigrationTask> allMigrationTasks, int[] rollbackLevels, PatchInfoStore currentPatchInfoStore) throws MigrationException
    {

        validateRollbackLevels(rollbackLevels);

        List<Integer> rollbacksLevelList = Arrays.asList(ArrayUtils.toObject(rollbackLevels));
        List<MigrationTask> rollbackCandidates = new ArrayList<MigrationTask>();


        for (MigrationTask migrationTask : allMigrationTasks)
        {
            if (rollbacksLevelList.contains(migrationTask.getLevel())
                    && currentPatchInfoStore.isPatchApplied(migrationTask.getLevel()))
            {
                rollbackCandidates.add(migrationTask);
            }
        }

        return rollbackCandidates;
    }

    private void validateRollbackLevels(int[] rollbackLevels) throws MigrationException
    {
        if (rollbackLevels == null)
        {
            throw new MigrationException("rollbackLevels should not be null");
        }

        if (rollbackLevels.length == 0)
        {
            throw new MigrationException("rollbackLevels should not be empty");
        }
    }
}
