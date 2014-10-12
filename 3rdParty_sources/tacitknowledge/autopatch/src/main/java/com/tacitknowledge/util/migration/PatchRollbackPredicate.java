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

import org.apache.commons.collections.Predicate;

/**
 * This class defines a predicate for CollectionUtils which evaluates if a
 * given RollbackableMigrationTask should remain in the collection
 *
 * @author Artie Pesh-Imam (apeshimam@tacitknowledge.com)
 * @see Predicate
 */
public class PatchRollbackPredicate implements Predicate
{
    /* initialize both to max value */
    private int rollbackPatchLevel = Integer.MAX_VALUE;
    private int currentPatchLevel = Integer.MAX_VALUE;

    /**
     * Constructor for this predicate.  The current patch level and the rollback
     * patch level are set in this constructor.
     *
     * @param currentPatchLevel
     * @param rollbackPatchLevel
     */
    public PatchRollbackPredicate(int currentPatchLevel, int rollbackPatchLevel)
    {
        this.rollbackPatchLevel = rollbackPatchLevel;
        this.currentPatchLevel = currentPatchLevel;
    }

    /**
     * The evaluate method returns false if the passed object is:
     * <ul>
     * <li>null</li>
     * <li>not an instance of <code>RollbackableMigrationTask</code></li>
     * <li>the level associated with the task is less than the rollback patch level </li>
     * <li>the level associated with the task is greater than the current patch level</li>
     * </ul>
     *
     * @param obj the Object to be evaluated
     * @boolean a boolean indicating if the object falls within the valid range for
     * this rollback
     */
    public boolean evaluate(Object obj)
    {

        if (obj == null)
            return false;

        if (!(obj instanceof RollbackableMigrationTask))
            return false;

        RollbackableMigrationTask task = (RollbackableMigrationTask) obj;
        final int level = task.getLevel().intValue();

        return ((level > rollbackPatchLevel) && (level <= currentPatchLevel));
    }
}
