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

import com.tacitknowledge.util.migration.MigrationException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Support using AutoPatch via injection and allows you to directly set the patch level.
 * Jacques Morel contributed this originally.
 *
 * @author Jacques Morel
 */
public class AutoPatchSupport
{
    /**
     * Class logger
     */
    private static Log log = LogFactory.getLog(AutoPatchSupport.class);

    /**
     * The launcher we'll use
     */
    private JdbcMigrationLauncher launcher;

    /**
     * Create a new support component for the given system name.
     * This will use create a factory for you.
     *
     * @param systemName the name of the system to be patched
     * @throws MigrationException if there is a problem
     * @see JdbcMigrationLauncherFactoryLoader
     */
    public AutoPatchSupport(String systemName) throws MigrationException
    {
        this(new JdbcMigrationLauncherFactoryLoader().createFactory(), systemName);
    }

    /**
     * Create support component with the given factory and system name
     *
     * @param launcherFactory the factory to use for migrations
     * @param systemName      the system to patch
     * @throws MigrationException if there is any problem
     */
    public AutoPatchSupport(JdbcMigrationLauncherFactory launcherFactory,
            String systemName) throws MigrationException
    {
        this(launcherFactory.createMigrationLauncher(systemName));
    }

    /**
     * Create a support component with the given configured launcher
     *
     * @param launcher the launcher to use for the migrations
     */
    public AutoPatchSupport(JdbcMigrationLauncher launcher)
    {
        this.launcher = launcher;
    }

    /**
     * Set the patch level to the specified level
     *
     * @param patchLevel the level to set the patch table to
     * @throws MigrationException if the store can't be locked
     */
    public void setPatchLevel(int patchLevel) throws MigrationException
    {
        Map contextMap = launcher.getContexts();
        Set contexts = contextMap.keySet();
        // FIXME test that setting the patch level works
        for (Iterator i = contexts.iterator(); i.hasNext();)
        {
            JdbcMigrationContext migrationContext = (JdbcMigrationContext) i.next();
            PatchTable patchTable = (PatchTable) contextMap.get(migrationContext);
            patchTable.lockPatchStore();
            patchTable.updatePatchLevel(patchLevel);
            log.info("Set the patch level to " + patchLevel + " for context " + migrationContext);
            patchTable.unlockPatchStore();
        }
    }

    /**
     * Get the current patch level
     *
     * @return int with the patch level in the patch database
     * @throws MigrationException if there is a problem getting the patch level
     */
    public int getPatchLevel() throws MigrationException
    {
        Map contextMap = launcher.getContexts();
        // Any of the patch tables for any of the contexts should be fine, get the first
        PatchTable firstPatchTable = (PatchTable) contextMap.values().iterator().next();
        // FIXME test that getting the patch level works
        return firstPatchTable.getPatchLevel();
    }

    /**
     * Get the highest patch level of all the configured patches
     *
     * @return int with the highest patch level
     * @throws MigrationException if there is problem getting the patch level
     */
    public int getHighestPatchLevel() throws MigrationException
    {
        return launcher.getNextPatchLevel() - 1;
    }
}