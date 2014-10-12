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

import com.tacitknowledge.util.migration.MigrationContext;
import com.tacitknowledge.util.migration.MigrationException;
import com.tacitknowledge.util.migration.MigrationTask;
import com.tacitknowledge.util.migration.PatchInfoStore;
import com.tacitknowledge.util.migration.RollbackableMigrationTask;

/**
 * Override select things in the JdbcMigrationLauncher for testing purposes
 * 
 * @author Mike Hardy (mike@tacitknowledge.com)
 */
public class TestDistributedJdbcMigrationLauncher extends DistributedJdbcMigrationLauncher
{
    /** Class logger */
    private static Log log = LogFactory.getLog(TestDistributedJdbcMigrationLauncher.class);
    
    /** The PatchInfoStore to use for migrations FIXME need to store a map of them */
    private PatchInfoStore patchStore = null;
    
    /**
     * Delegates to the superclass
     */
    public TestDistributedJdbcMigrationLauncher()
    {
        super();
    }
    
    /**
     * Delegating constructors
     * 
     * @param context the context to use for migration loading
     */
    public TestDistributedJdbcMigrationLauncher(JdbcMigrationContext context)
    {
        super(context);
    }
    
    /**
     * Override the patch store creation to be the patch table we have
     * 
     * @param context the context to use for the patch store
     * @return patchStore held internally
     * @throws MigrationException if creating the store fails
     */
    protected PatchInfoStore createPatchStore(JdbcMigrationContext context) 
        throws MigrationException
    {
        if (patchStore != null)
        {
            return patchStore;
        }
        
        return super.createPatchStore(context);
    }
    
    /**
     * Set the PatchInfoStore object to use for migrations
     * 
     * @param patchStore the PatchInfoStore to use
     */
    public void setPatchStore(PatchInfoStore patchStore)
    {
        this.patchStore = patchStore;
    }

    /**
     * {@inheritDoc}
     */
    public void migrationSuccessful(MigrationTask task, MigrationContext ctx)
        throws MigrationException
    {
        log.debug(this + " silently ignoring a migrationSuccessful call");
    }

    /**
     * {@inheritDoc}
     */
    public void rollbackSuccessful(RollbackableMigrationTask task, int rollbackLevel,
	    MigrationContext context) throws MigrationException
    {
	log.debug(this + " silently ignoring a rollbackSuccessful call");
    }
}
