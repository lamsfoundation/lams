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

package com.tacitknowledge.util.migration.test.listeners;

import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.tacitknowledge.util.migration.MigrationContext;
import com.tacitknowledge.util.migration.MigrationException;
import com.tacitknowledge.util.migration.MigrationListener;
import com.tacitknowledge.util.migration.MigrationTask;


/**
 * @author Alex Soto <apsoto@gmail.com>
 *
 */
public class TestListener1 implements MigrationListener
{
    /** Class logger */
    private static Log log = LogFactory.getLog(TestListener1.class);
    
    /** system name I was configured with */
    protected String systemName = null;
    
    public void migrationFailed(MigrationTask task, MigrationContext context, MigrationException e) throws MigrationException
    {
        log.debug("migration failed");
    }

    public void migrationStarted(MigrationTask task, MigrationContext context) throws MigrationException
    {
        log.debug("migration started");
    }

    public void migrationSuccessful(MigrationTask task, MigrationContext context) throws MigrationException
    {
        log.debug("migration successful");
    }

    /**
     * @see com.tacitknowledge.util.migration.MigrationListener#initialize(Properties)
     */
    public void initialize(String systemName, Properties properties) throws MigrationException
    {
        log.debug("initialized");
        this.systemName = systemName;
    }

    public String getSystemName()
    {
        return this.systemName;
    }
}
