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

import java.sql.Connection;
import java.sql.SQLException;

import com.tacitknowledge.util.migration.TestMigrationContext;

/**
 * A DataSourceMigrationContext that doesn't actually talk to a database.
 * 
 * @author Mike Hardy (mike@tacitknowledge.com)
 */
public class TestDataSourceMigrationContext extends TestMigrationContext
{
    /**
     * Always returns null, doesn't talk to a database
     * 
     * @return null every time
     * @exception SQLException never throws
     */
    public Connection getConnection() throws SQLException
    {
        return null;
    }
}
