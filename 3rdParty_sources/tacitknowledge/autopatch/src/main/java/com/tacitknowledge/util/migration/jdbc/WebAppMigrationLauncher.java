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

import com.tacitknowledge.util.discovery.ClassDiscoveryUtil;
import com.tacitknowledge.util.discovery.WebAppResourceListSource;
import com.tacitknowledge.util.migration.MigrationException;
import com.tacitknowledge.util.migration.jdbc.util.ConfigurationUtil;
import com.tacitknowledge.util.migration.jdbc.util.MigrationUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Launches the migration process upon application context creation.  This class
 * is intentionally fail-fast, meaning that it throws a RuntimeException if any
 * problems arise during migration and will prevent the web application from
 * being fully deployed.
 * <p/>
 * This class expects the following servlet context init parameters:
 * <ul>
 * <li>migration.systemname - the name of the logical system being migrated</li>
 * </ul>
 * <p/>
 * Below is an example of how this class can be configured in web.xml:
 * <pre>
 *   ...
 *   &lt;context-param&gt;
 *       &lt;param-name&gt;migration.systemname&lt;/param-name&gt;
 *       &lt;param-value&gt;milestone&lt;/param-value&gt;
 *   &lt;/context-param&gt;
 *   ...
 *   &lt;!-- immediately after the filter configs... --&gt;
 *   ...
 *   &lt;listener&gt;
 *       &lt;listener-class&gt;
 *           com.tacitknowledge.util.migration.jdbc.WebAppMigrationLauncher
 *       &lt;/listener-class&gt;
 *   &lt;/listener&gt;
 *   ...
 * </pre>
 *
 * @author Scott Askew (scott@tacitknowledge.com)
 * @see com.tacitknowledge.util.migration.MigrationProcess
 */
public class WebAppMigrationLauncher implements ServletContextListener
{
    /**
     * Keeps track of the first run of the class within this web app deployment.
     * This should always be true, but you can never be too careful.
     */
    private static boolean firstRun = true;

    /**
     * Class logger
     */
    private static Log log = LogFactory.getLog(WebAppMigrationLauncher.class);

    /**
     * {@inheritDoc}
     */
    public void contextInitialized(ServletContextEvent sce)
    {
        try
        {

            // WEB-INF/classes and WEB-INF/lib are not in the system classpath (as defined by
            // System.getProperty("java.class.path")); add it to the search path here
            if (firstRun)
            {
                ClassDiscoveryUtil.addResourceListSource(
                        new WebAppResourceListSource(sce.getServletContext().getRealPath("/WEB-INF")));
            }
            firstRun = false;

            String systemName = ConfigurationUtil.getRequiredParam("migration.systemname", sce, this);
            String settings = ConfigurationUtil.getOptionalParam("migration.settings", sce, this);

            // The MigrationLauncher is responsible for handling the interaction
            // between the PatchTable and the underlying MigrationTasks; as each
            // task is executed, the patch level is incremented, etc.
            try
            {
                MigrationUtil.doMigrations(systemName, settings);
            }
            catch (MigrationException e)
            {
                // Runtime exceptions coming from a ServletContextListener prevent the
                // application from being deployed.  In this case, the intention is
                // for migration-enabled applications to fail-fast if there are any
                // errors during migration.
                throw new RuntimeException("Migration exception caught during migration", e);
            }
        }
        catch (RuntimeException e)
        {
            // Catch all exceptions for the sole reason of logging in
            // as many places as possible - debugging migration
            // problems requires detection first, and that means
            // getting the word of failures out.
            log.error(e);
            System.out.println(e.getMessage());
            e.printStackTrace(System.out);
            System.err.println(e.getMessage());
            e.printStackTrace(System.err);

            throw e;
        }
    }

    /**
     * {@inheritDoc}
     */
    public void contextDestroyed(ServletContextEvent sce)
    {
        log.debug("context is being destroyed " + sce);
    }

}
