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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Load a MigrationLauncherFactory. This will default to loading the
 * JdbcMigrationLauncherFactory, but will examine the system properties
 * for a property called "migration.factory" and load that one if specified
 *
 * @author Jacques Morel
 */
public class JdbcMigrationLauncherFactoryLoader
{
    /**
     * Class logger
     */
    private static Log log = LogFactory.getLog(JdbcMigrationLauncherFactoryLoader.class);


    /**
     * Create the JdbcMigrationLauncherFactory
     *
     * @return JdbcMigrationLauncherFactory (or subclass)
     */
    public JdbcMigrationLauncherFactory createFactory()
    {
        // Get the factory name from the system properties if possible
        String factoryName = System.getProperties().getProperty("migration.factory");
        if (factoryName == null)
        {
            factoryName = JdbcMigrationLauncherFactory.class.getName();
        }
        log.debug("Creating JdbcMigrationLauncher using " + factoryName);

        // Load the factory
        Class factoryClass = null;
        try
        {
            factoryClass = Class.forName(factoryName);
        }
        catch (ClassNotFoundException e)
        {
            throw new IllegalArgumentException("Migration factory class '"
                    + factoryName + "' not found.  Aborting.");
        }
        try
        {
            return (JdbcMigrationLauncherFactory) factoryClass.newInstance();
        }
        catch (Exception e)
        {
            throw new RuntimeException("Problem while instantiating factory class '"
                    + factoryName + "'.  Aborting.", e);
        }
    }
}
