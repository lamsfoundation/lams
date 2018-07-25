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

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Returns the  strategy we should we apply to decide if a patch needs to be applied
 * or we should rollback
 *
 * @author Oscar Gonzalez (oscar@tacitknowledge.com)
 */

public class MigrationRunnerFactory
{

    private static Log log = LogFactory.getLog(MigrationRunnerFactory.class);
    public static final String DEFAULT_MIGRATION_STRATEGY = "com.tacitknowledge.util.migration.OrderedMigrationRunnerStrategy";

    public static MigrationRunnerStrategy getMigrationRunnerStrategy(String strategy)
    {

        log.info("Strategy received '" + strategy + "'");

        if (StringUtils.isBlank(strategy))
        {
            return new OrderedMigrationRunnerStrategy();

        }

        try
        {
            Class c = Class.forName(strategy.trim());
            MigrationRunnerStrategy runnerStrategy = (MigrationRunnerStrategy) c.newInstance();
            return runnerStrategy;
        }
        catch (Exception e)
        {
            throw new IllegalArgumentException("Strategy selected " + strategy + " cannot be instantiated ", e);
        }


    }

}
