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

import java.util.Set;

/**
 * Interface for the persistence of information related to the patch level
 * of the system, as well as whether patches are currently being applied
 *
 * @author Mike Hardy (mike@tacitknowledge.com)
 * @author Hemri Herrera (hemri@tacitknowledge.com)
 * @author Ulises Pulido (upulido@tacitknowledge.com)
 */
public interface PatchInfoStore
{
    /**
     * Creates the patch storage area if it has not been done before
     *
     * @throws MigrationException if creation is unsuccessful
     */
    public void createPatchStoreIfNeeded() throws MigrationException;

    /**
     * Returns the current patch level of the system
     *
     * @return the current patch level of the system
     * @throws MigrationException if it is not possible to get the patch level
     */
    public int getPatchLevel() throws MigrationException;

    /**
     * Updates the system patch level to the specified value
     *
     * @param level the new system patch level
     * @throws MigrationException if updating the patch level failed
     */
    public void updatePatchLevel(int level) throws MigrationException;

    /**
     * Determines if the patch store is already locked
     *
     * @return <code>true</code> if the patch store is already locked
     * @throws MigrationException if checking for the lock fails
     */
    public boolean isPatchStoreLocked() throws MigrationException;

    /**
     * Places a lock for this system on the patch store
     *
     * @throws MigrationException    if locking the store fails
     * @throws IllegalStateException if a lock already exists
     */
    public void lockPatchStore() throws MigrationException, IllegalStateException;

    /**
     * Removes any locks for this system on the patch store
     *
     * @throws MigrationException if unlocking the store fails
     */
    public void unlockPatchStore() throws MigrationException;

    /**
     * Determines if a given patch has been applied in the system
     *
     * @throws MigrationException if unlocking the store fails
     */
    public boolean isPatchApplied(int patchLevel) throws MigrationException;

    /**
     * Updates the system patch level to the specified value after rollback
     *
     * @param rollbackLevel the new system patch level
     * @throws MigrationException if updating the patch level failed
     */
    public void updatePatchLevelAfterRollBack(int rollbackLevel) throws MigrationException;

    /**
     * Obtains all patches applied in the system.
     *
     * @return a set containing all patches number applied in the system.
     * @throws MigrationException if retrieving patches fails.
     */
    public Set<Integer> getPatchesApplied() throws MigrationException;
}
