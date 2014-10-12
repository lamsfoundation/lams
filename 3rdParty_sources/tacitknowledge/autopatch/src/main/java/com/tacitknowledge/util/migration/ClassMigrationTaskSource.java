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

import com.tacitknowledge.util.discovery.ClassDiscoveryUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Returns a list of all public, concrete classes that implement the
 * <code>MigrationTask</code> in a specific package.
 *
 * @author Scott Askew (scott@tacitknowledge.com)
 */
public class ClassMigrationTaskSource implements MigrationTaskSource
{
    /**
     * Class logger
     */
    private static Log log = LogFactory.getLog(ClassMigrationTaskSource.class);

    /**
     * {@inheritDoc}
     */
    public List<MigrationTask> getMigrationTasks(String packageName) throws MigrationException
    {
        if (packageName == null)
        {
            throw new MigrationException("You must specify a package to get tasks for");
        }

        Class[] taskClasses = ClassDiscoveryUtil.getClasses(packageName, MigrationTask.class);
        log.debug("Found " + taskClasses.length + " patches in " + packageName);
        return instantiateTasks(taskClasses);
    }

    /**
     * Instantiates the given classes
     *
     * @param taskClasses the classes instantiate
     * @return a list of <code>MigrationTasks</code>
     * @throws MigrationException if a class could not be instantiated; this
     *                            is most likely due to the abscense of a default constructor
     */
    private List<MigrationTask> instantiateTasks(Class[] taskClasses) throws MigrationException
    {
        List<MigrationTask> tasks = new ArrayList<MigrationTask>();
        for (int i = 0; i < taskClasses.length; i++)
        {
            Class taskClass = taskClasses[i];
            try
            {
                Object o = taskClass.newInstance();

                // It's not legal to have a null name.
                MigrationTask task = (MigrationTask) o;
                if (task.getName() != null)
                {
                    tasks.add(task);
                }
                else
                {
                    log.warn("MigrationTask " + taskClass.getName()
                            + " had no migration name. Is that intentional? Skipping task.");
                }
            }
            catch (Exception e)
            {
                throw new MigrationException("Could not instantiate MigrationTask "
                        + taskClass.getName(), e);
            }
        }
        return tasks;
    }
}
