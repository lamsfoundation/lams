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

/**
 * Convenience base class for migration tasks.
 *
 * @author Scott Askew (scott@tacitknowledge.com)
 * @author Artie Pesh-Imam (apeshimam@tacitknowledge.com)
 */
public abstract class MigrationTaskSupport implements RollbackableMigrationTask
{
    protected boolean isRollbackSupported = false;

    /**
     * The name of this migration task
     */
    private String name = this.getClass().getName();

    /**
     * The relative order in which this test should run
     */
    private Integer level;

    /**
     * {@inheritDoc}
     */
    public String getName()
    {
        return name;
    }

    /**
     * Sets the name of this migration task.
     *
     * @param name the name of this migration task
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * {@inheritDoc}
     */
    public Integer getLevel()
    {
        return level;
    }

    /**
     * Sets the relative order in which this test should run
     *
     * @param lvl the relative order in which this test should run
     */
    public void setLevel(Integer lvl)
    {
        this.level = lvl;
    }

    /**
     * {@inheritDoc}
     */
    public int compareTo(Object o)
    {
        MigrationTask task = (MigrationTask) o;
        if (task.getLevel() == null)
        {
            return 1;
        }
        return getLevel().compareTo(task.getLevel());
    }

    /**
     * By default, this method is not supported.
     */
    public void down(MigrationContext context) throws MigrationException
    {
        throw new UnsupportedOperationException("This method is not supported by this task.");
    }

    /**
     * @return a boolean indicating if rollback is supported
     */
    public boolean isRollbackSupported()
    {
        return isRollbackSupported;
    }

    /**
     * Sets the isRollbackSupported attribute
     *
     * @param isRollbackSupported
     */
    public void setRollbackSupported(boolean isRollbackSupported)
    {
        this.isRollbackSupported = isRollbackSupported;
    }

    /**
     * By default, this method delegates to the up method.
     */
    public void migrate(MigrationContext context) throws MigrationException
    {
        up(context);
    }

    /**
     * By default, this method is a no-op. If a legacy task extends
     * MigrationTaskSupport but does not implement up, it would cause a
     * compilation error. This no-op method resolves that issue.
     */
    public void up(MigrationContext context) throws MigrationException
    {
        // no op
    }
}
